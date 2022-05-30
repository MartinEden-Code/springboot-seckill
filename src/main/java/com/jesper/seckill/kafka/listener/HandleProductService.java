//package com.jesper.seckill.kafka.listener;
//
//import com.alibaba.fastjson.JSONObject;
//import com.jesper.seckill.Ehcache.ProductEcacheService;
//import com.jesper.seckill.bean.ProductInfo;
//import com.jesper.seckill.service.ProductInfoService;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
///**
// * @author hongxingyi
// * @description TODO kafka 商品维度化缓存 服务监听器
// * @date 2022/5/26 10:30
// */
//@Component
//public class HandleProductService {
//
//    Logger logger = LoggerFactory.getLogger(HandleProductService.class);
//
//    @Value("${kafka.consumer.topic}")
//    private String product_topic;
//
//    @Autowired
//    private ProductInfoService productInfoService;
//
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    @Autowired
//    private ProductEcacheService productEcacheService;
//
//
//    @KafkaListener(topics="product")
//    public void listenProductChange(ConsumerRecord<?, ?> record){
//        logger.info("收到消息为：{} ",record.value());
//
//        logger.info("商品队列收到变更商品价格消息，准备重新拉取最新商品数据 ......");
//        try {
//            String value = (String)record.value();
//            ProductInfo productInfo = JSONObject.parseObject(value, ProductInfo.class);
//            ProductInfo newDbs = productInfoService.getProductInfoById(productInfo.getId());
//            if(newDbs != null){
//                productEcacheService.saveLocalCache(newDbs);
//                redisTemplate.delete("product:" + productInfo.getId());
//                redisTemplate.opsForValue().set("product:"+productInfo.getId(), JSONObject.toJSONString(newDbs));
//            }
//            logger.info("已经重新获取到最新的商品信息，并写入缓存 ......");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
