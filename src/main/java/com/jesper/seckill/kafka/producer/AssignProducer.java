//package com.jesper.seckill.kafka.producer;
//
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import java.util.Properties;
//
///**
// * @author hongxingyi
// * @description TODO kafka offset 机制
// * @date 2022/5/27 16:14
// */
//public class AssignProducer {
//
//    private final KafkaProducer<String, String> producer;
//
//    public final static String TOPIC = "second";
//
//    public AssignProducer() {
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "localhost:9092");//xxx服务器ip
//        props.put("acks", "all");//所有follower都响应了才认为消息提交成功，即"committed"
//        props.put("retries", 0);//retries = MAX 一直重试直到认为消息投递成功)
//        //batch.size当批量的数据大小达到设定值后，就会立即发送，不顾下面的linger.ms
//        props.put("batch.size", 16384);//producer采用批处理消息发送机制，以减少请求次数.默认的批量处理消息字节数
//        //延迟1ms发送，这项设置将通过增加小的延迟来完成--即，不是立即发送一条记录，
//        // producer将会等待给定的延迟时间以允许其他消息记录发送，这些消息记录可以批量处理
//        props.put("linger.ms", 1);
//        //producer可以用来缓存数据的内存大小。
//        props.put("buffer.memory", 33554432);
//        //发送的消息进行序列化，kafka提供了常用的一些默认类型的序列化类型，也可以自己实现序列化接口
//        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
//        //设置发送到指定的partition
//        producer = new KafkaProducer<String, String>(props);
//    }
//
//    public void produce() {
//        int messageNo = 1;
//        final int COUNT = 10;
//        while (messageNo < COUNT) {
//            String key = String.valueOf(messageNo);
//            String data = String.format("hello KafkaProducer message %s from special partition 0 ", key);
//            try {
//                producer.send(new ProducerRecord<String, String>(TOPIC, 0,key,data));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            messageNo++;
//        }
//        producer.close();
//    }
//
//    public static void main(String[] args) {
//        new AssignProducer().produce();
//    }
//
//}
//
