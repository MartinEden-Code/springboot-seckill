package com.jesper.seckill;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/5/26 10:44
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class KafkaTest {

    /**
     * todo 生产者将消息发到指定的主题分区下 类似于rocketMq中的tag
     */
    //***** 方式1 直接在发送消息时指定
/*    @Test
    public  void sendPartitions() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        //所有follower都响应了才认为消息提交成功，即"committed"
        properties.put("acks", "all");
        //设置重试次数，0表示无限重试
        properties.put("retries", "3");
        //batch.size当批量的数据大小达到设定值后，就会立即发送，不顾下面的linger.ms
        properties.put("batch.size", "16384");
        //延迟1ms发送，这项设置将通过增加小的延迟来完成--即，不是立即发送一条记录，producer将会等待给定的延迟时间以允许其他消息记录发送，这些消息记录可以批量处理
        properties.put("linger.ms", 1);
        //producer可以用来缓存数据的内存大小。
        properties.put("buffer.memory", 33554432);
        //key和value的序列化
        //发送的消息进行序列化，kafka提供了常用的一些默认类型的序列化类型，也可以自己实现序列化接口
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //构造生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        //发送消息
        for (int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<String, String>("second", 2,"congge " ,"val = "+ i)
                    , new ProducerCallBackV2());
        }
        //关闭连接资源
        producer.close();

    }

    *//**
     * 生产者回调消息
     *//*
    static class ProducerCallBackV2 implements Callback {
        public void onCompletion(RecordMetadata metadata, Exception e) {
            if(e == null){
                System.out.println("offset : " + metadata.offset());
                System.out.println("partition : " + metadata.partition());
                System.out.println("topic : " +metadata.topic());
                System.out.println("===============================");
            }
        }
    }

    //****** 方式2 实现Partitioner接口
    @Test
    public  void sendPartitionsByImplementPatitions() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("acks", "all");
        properties.put("retries", "3");
        properties.put("batch.size", "16384");
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        //key和value的序列化
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //添加自定义分区器
        properties.put("partitioner.class", "com.jesper.seckill.kafka.partition.MyPartition");

        //构造生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        System.out.println("实现Partition接口以实现 分区消息发送 ");
        //发送消息
        for (int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<String, String>("second", "congge-self ", "val = " + i)
                    , new ProducerCallBackV3());
        }
        //关闭连接资源
        producer.close();

    }
    *//**
     * 实现Partiion发送指定分区 生产者回调消息 (发送成功后回调该消息，注意和rocketMq回调方法进行区别)
     *//*
    static class ProducerCallBackV3 implements Callback {

        public void onCompletion(RecordMetadata metadata, Exception e) {
            if (e == null) {
                System.out.println("offset : " + metadata.offset());
                System.out.println("partition : " + metadata.partition());
                System.out.println("topic : " + metadata.topic());
                System.out.println("===============================");
            }
        }

    }*/


    //









}
