//package com.jesper.seckill.service;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.jesper.seckill.Ehcache.ProductEcacheService;
//import com.jesper.seckill.bean.ProductInfo;
//import com.jesper.seckill.mapper.ProductInfoMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
///**
// * @author hongxingyi
// * @description TODO kafka 商品维度化缓存 相关业务类
// * @date 2022/5/26 10:25
// */
//@Component
//@Service("productInfoService")
//public class ProductInfoService {
//
//    private static Logger logger = LoggerFactory.getLogger(ProductInfoService.class);
//
//    @Value("${spring.kafka.producer.topic}")
//    private String product_topic;
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @Autowired
//    private ProductInfoMapper productInfoMapper;
//
//    public int updateProductPrice(ProductInfo productInfo) {
//        productInfoMapper.updateProductPrice(productInfo);
//        int retry = 0;
//        try {
//            kafkaTemplate.send(product_topic, JSONObject.toJSONString(productInfo));
//        } catch (Exception e) {
//            logger.error("消息发送失败，这是第:" +retry + "次");
//            retry ++;
//            while(retry <3){
//                kafkaTemplate.send(product_topic,JSONObject.toJSONString(productInfo));
//                if(retry == 3){
//                    break;
//                }
//            }
//        }
//
//        return 1;
//    }
//
//    /*public ProductInfo getProductInfoById(int id) {
//        return productInfoMapper.selectProductInfoById(id);
//    }*/
//
//
//    //************************ ehcache 缓存
//    @Autowired
//    private ProductEcacheService productEcacheService;
//
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    public ProductInfo getProductInfoById(int id) {
//
//        String productInfoStr = redisTemplate.opsForValue().get("product:"+id);
//        if(!StringUtils.isEmpty(productInfoStr)){
//            JSONObject object = JSON.parseObject(productInfoStr);
//            ProductInfo productInfo = JSONObject.toJavaObject(object, ProductInfo.class);
//            System.out.println("我是从redis中查出来的: ======= >>>" + id);
//            return productInfo;
//        }else{
//            //如果redis里面不存在，则去查一下ehcache
//            ProductInfo cacheInfo = productEcacheService.getLocalCache(id);
//            if(cacheInfo != null){
//                System.out.println("我是从ehcache中查出来的: ======= >>>" + id);
//                return cacheInfo;
//            }else{//ehcache 也不存在，则就需要查询数据库了
//                ProductInfo dbInfo = productInfoMapper.selectProductInfoById(id);
//                if(dbInfo != null){
//                    redisTemplate.opsForValue().set("product:"+id, JSONObject.toJSONString(dbInfo));
//                    System.out.println("我是从mysql中查出来的: ======= >>>" + id);
//                    productEcacheService.saveLocalCache(dbInfo);
//                    return dbInfo;
//                }else{
//                    return null;
//                }
//            }
//        }
//
//    }
//}
