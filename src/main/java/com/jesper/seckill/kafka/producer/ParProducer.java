//package com.jesper.seckill.kafka.producer;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerRecord;
//
//import java.util.Properties;
//
///**
// * @author hongxingyi
// * @description TODO 配合 ParConsumerA   ParConsumerB 发送指定分区消息
// * @date 2022/5/26 16:34
// */
//public class ParProducer {
//
//    private final KafkaProducer<String, String> producer;
//
//    public final static String TOPIC = "second";
//
//    public ParProducer() {
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "localhost:9092");
//        //所有follower都响应了才认为消息提交成功，即"committed"
//        props.put("acks", "all");
//        //设置重试次数，0表示无限重试
//        props.put("retries", 0);
//        //batch.size当批量的数据大小达到设定值后，就会立即发送，不顾下面的linger.ms
//        props.put("batch.size", 16384);
//        //延迟1ms发送，这项设置将通过增加小的延迟来完成--即，不是立即发送一条记录，
//        // producer将会等待给定的延迟时间以允许其他消息记录发送，这些消息记录可以批量处理
//        props.put("linger.ms", 1);
//        //producer可以用来缓存数据的内存大小。
//        props.put("buffer.memory", 33554432);
//        //发送的消息进行序列化，kafka提供了常用的一些默认类型的序列化类型，也可以自己实现序列化接口
//        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
//        producer = new KafkaProducer<String, String>(props);
//    }
//
//    /**
//     * 奇数号的消息发送到分区1，偶数号的消息发送到分区0
//     * 同一个topic下的具体的某一个partition中的消息是有序的，模拟消息分类过滤
//     */
//    public void produceSpecialMessage() {
//        int messageNo = 1;
//        final int COUNT = 9;
//        while (messageNo < COUNT) {
//            String key = String.valueOf(messageNo);
//            if(messageNo % 2 ==0){
//                System.out.println("发送消息分区到0");
//                String data = String.format("hello KafkaProducer message %s from special partition 0 ", key);
//                //偶数的消息发送到分区0
//                try {
//                    producer.send(new ProducerRecord<String, String>("second", 0,key,data));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }else {
//                System.out.println("发送消息分区到1");
//                String data = String.format("hello KafkaProducer message %s from special partition 1 ", key);
//                //基数的消息发送到分区1
//                try {
//                    producer.send(new ProducerRecord<String, String>("second", 1,key,data));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            messageNo++;
//        }
//        producer.close();
//    }
//
//    public static void main(String[] args) {
//        new ParProducer().produceSpecialMessage();
//    }
//
//}
//
