package com.jesper.seckill.service;

import com.jesper.seckill.bean.OrderInfo;
import com.jesper.seckill.bean.SeckillOrder;
import com.jesper.seckill.bean.User;
import com.jesper.seckill.redis.RedisService;
import com.jesper.seckill.redis.SeckillKey;
import com.jesper.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jiangyunxiong on 2018/5/23.
 */
@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    //保证这三个操作，减库存 下订单 写入秒杀订单是一个事物
    @Transactional
    public OrderInfo seckill(User user, GoodsVo goods){
        //减库存
        boolean success = goodsService.reduceStock(goods);
        //减库存这个操作的返回值为true的时候才继续后面的下订单，否则会出现生成的订单数量远远多于卖出商品的数量。
        if (success){
            //下订单 写入秒杀订单
            return orderService.createOrder(user, goods);
        }else {
            //库存不足，设置该商品库存状态
            setGoodsOver(goods.getId());
            return null;
        }
    }

    /**
     * 获取秒杀结果
     * @param userId
     * @param goodsId
     * @return
     */
    public long getSeckillResult(long userId, long goodsId){
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(userId, goodsId);
        //如果订单为空，有两种状态，排队和库存不足导致的失败，根据标记状态来判断
        if (order != null){
            return order.getOrderId();
        }else{
            //获取库存状态
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            }else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver, ""+goodsId);
    }
}
