//package com.jesper.seckill.kafka.consumer;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.TopicPartition;
//
//import java.util.Arrays;
//import java.util.Properties;
//
///**
// * @author hongxingyi
// * @description TODO 配合 ParProducer 消费指定分区消息
// * @date 2022/5/26 17:10
// */
//
//public class ParConsumerB {
//
//
//    public static void main(String[] args){
//        Properties properties = new Properties();
//        properties.put("bootstrap.servers", "localhost:9092");
//        properties.put("group.id", "zcy-group");
//        properties.put("enable.auto.commit", "true");
//        properties.put("auto.commit.interval.ms", "1000");
//        properties.put("auto.offset.reset", "latest");
//        properties.put("session.timeout.ms", "30000");
//        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//
//        org.apache.kafka.clients.consumer.KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
//        //kafkaConsumer.subscribe(Arrays.asList("second"));
//        kafkaConsumer.assign(Arrays.asList(new TopicPartition("second",0)));
//        while (true) {
//            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
//            for (ConsumerRecord<String, String> record : records) {
//                System.out.println("-----------------");
//                int partition = record.partition();
//                System.out.printf("partition = %d,offset = %d, value = %s", partition,record.offset(), record.value());
//                System.out.println("ddddddddddddddddddd");
//            }
//        }
//    }
//
//
//
//}
