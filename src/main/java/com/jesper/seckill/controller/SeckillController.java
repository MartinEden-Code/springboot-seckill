package com.jesper.seckill.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.jesper.seckill.bean.SeckillOrder;
import com.jesper.seckill.bean.User;
import com.jesper.seckill.rabbitmq.MQSender;
import com.jesper.seckill.rabbitmq.SeckillMessage;
import com.jesper.seckill.redis.GoodsKey;
import com.jesper.seckill.redis.RedisService;
import com.jesper.seckill.result.CodeMsg;
import com.jesper.seckill.result.Result;
import com.jesper.seckill.service.GoodsService;
import com.jesper.seckill.service.OrderService;
import com.jesper.seckill.service.SeckillService;
import com.jesper.seckill.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by jiangyunxiong on 2018/5/22.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedisService redisService;

    /**
     * rabbitmq
     */
    @Autowired
    MQSender sender;

    /**
     * 基于令牌桶算法的限流实现类。每秒产生10个令牌 控制一秒内最多只能有10个请求得到服务
     */
    RateLimiter rateLimiter = RateLimiter.create(10);

    /**
     * 做标记，判断该商品是否被处理过了
     */
    private HashMap<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * GET POST
     * 1、GET幂等,服务端获取数据，无论调用多少次结果都一样
     * 2、POST，向服务端提交数据，不是幂等
     * <p>
     * 将同步下单改为异步下单
     * 大并发的瓶颈就是数据库。应对并发最有效的就是缓存
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(Model model, User user, @RequestParam("goodsId") long goodsId) {
        //限流
        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            return  Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
        }

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", user);
        //库存不足后，后面的请求对数据库基本没有压力
        //内存标记，判断商品是否秒杀完毕，减少redis访问:
        // 比如说前面10个请求已经将redis中缓存的库存减到0了，那么后面的请求会继续将redis中的库存减为负数，
        // 显然后面的请求将redis中的库存减成负数是多余的，而且还增加了redis的访问量。
        // 那么这里就做一个内存标记，缓存中库存大于零的时候内存标记为false，当缓存中的库存减为0时内存标记就为true。当为false时请求能往下走，反之直接返回秒杀结束。
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getGoodsStock, "" + goodsId);//10
        if (stock < 0) {
            //stock<0说明redisService中已经没有该商品信息：1.被秒杀完；2.没有存入该商品信息。因此先 系统初始化,将商品信息加载到redis和本地内存
            //afterPropertiesSet将商品和库存数据同步到redis中，所有的抢购操作都在redis中进行处理，通过Redis预减少库存减少数据库访问
            afterPropertiesSet();
            long stock2 = redisService.decr(GoodsKey.getGoodsStock, "" + goodsId);//10
            if(stock2 < 0){
                localOverMap.put(goodsId, true);
                return Result.error(CodeMsg.SECKILL_OVER);
            }
        }
        //判断重复秒杀
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }
        //入队
        SeckillMessage message = new SeckillMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        //为了保护系统不受高流量的冲击而导致系统崩溃的问题，使用RabbitMQ用异步队列处理下单，实际做了一层缓冲保护，做了一个窗口模型，窗口模型会实时的刷新用户秒杀的状态。
        sender.sendSeckillMessage(message);
        return Result.success(0);//排队中
    }

    /**
     * 系统初始化,将商品信息加载到redis和本地内存
     */
    @Override
    public void afterPropertiesSet() {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goods : goodsVoList) {
            redisService.set(GoodsKey.getGoodsStock, "" + goods.getId(), goods.getStockCount());
            //初始化商品都是没有处理过的
            localOverMap.put(goods.getId(), false);
        }
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model, User user,
                                      @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long orderId = seckillService.getSeckillResult(user.getId(), goodsId);
        return Result.success(orderId);
    }
}
