//package com.jesper.seckill.kafka.consumer;
//import java.util.Optional;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
///**
// * @author hongxingyi
// * @description TODO 简单的kafka消费类
// * @date 2022/5/24 23:18
// */
//
//@Component
//public class KafkaConsumer {
//
//    private static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
//
//    @KafkaListener(topics = {"demo"})
//    public void listen(ConsumerRecord<?, ?> record) {
//        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
//        if (kafkaMessage.isPresent()) {
//            Object message = kafkaMessage.get();
//            logger.info("----------------- record =" + record);
//            logger.info("------------------ message =" + message);
//        }
//
//    }
//
//
//}
//
//
