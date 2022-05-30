//package com.jesper.seckill.kafka.producer;
//
///**
// * @author hongxingyi
// * @description TODO 发送消息
// * @date 2022/5/24 23:14
// */
//
//import java.util.Date;
//import java.util.UUID;
//
//import com.jesper.seckill.bean.Message;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//@Component
//public class KafkaProducer {
//
//    private static Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    private Gson gson = new GsonBuilder().create();
//
//    //发送消息方法
//    public void send() {
//        for(int i=0;i<5;i++){
//            Message message = new Message();
//            message.setId(System.currentTimeMillis());
//            message.setMsg(UUID.randomUUID().toString()+ "---" +i);
//            message.setSendTime(new Date());
//            logger.info("发送消息 ----->>>>>  message = {}", gson.toJson(message));
//            kafkaTemplate.send("demo", gson.toJson(message));
//        }
//    }
//
//
//}
//
//
