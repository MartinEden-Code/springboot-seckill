//package com.jesper.seckill.kafka.consumer;
//
//import java.util.*;
//
//import com.jesper.seckill.kafka.KeyWithRandomPartitioner;
//import org.apache.kafka.clients.consumer.*;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.TopicPartition;
//
///**
// * @author hongxingyi
// * @description TODO kafka offset机制
// * @date 2022/5/27 16:17
// */
//public class AssignConsumer {
//
//    public static void main(String[] args){
//        Properties properties = new Properties();
//        //指定kafka的broker地址，集群中多个以,分割
//        properties.put("bootstrap.servers", "localhost:9092");
//        //指定消费者组，建议指定
//        properties.put("group.id", "zcy-group");
//        //每次拉取消息完毕，consumer会自动commit提交offset
//        //properties.put("enable.auto.commit", "true");//false为手动提交
//        properties.put("enable.auto.commit", "false");//false为手动提交
//
//        //每搁1S提交一次offset
//        properties.put("auto.commit.interval.ms", "1000");
//        properties.put("auto.offset.reset", "latest");
//        properties.put("session.timeout.ms", "30000");
//        //消费端设置反序列化器
//        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//
//        //自定义分区写入策略  轮询策略和随机策略会导致消息乱序问题
//        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, KeyWithRandomPartitioner.class.getName());
//
//        //PARTITION_ASSIGNMENT_STRATEGY_CONFIG = "partition.assignment.strategy"  默认是range范围分区策略
//        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RoundRobinAssignor");
//
//
//
//        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
//        //kafkaConsumer.subscribe(Arrays.asList("second"));
//        //指定消费分区的时候，使用assign
//        kafkaConsumer.assign(Arrays.asList(new TopicPartition("second",0)));
//
//        Set<TopicPartition> assignment = new HashSet<>();
//        while (assignment.size()<=0){
//            kafkaConsumer.poll(100);
//            //确保消费者能够获得一个分区
//            assignment = kafkaConsumer.assignment();
//        }
//
//        //指定从分区末尾消费
//        Map<TopicPartition, Long> offsets = kafkaConsumer.endOffsets(assignment);
//
//        for(TopicPartition tp : assignment){
//            //seek方法从指定位置消费，从分区末尾消费【也就是最新的一条消息】
//            kafkaConsumer.seek(tp,offsets.get(tp) + 1);
//
//            //从第50条消息开始消费
//            //kafkaConsumer.seek(tp,50);
//        }
//
//        //消费
//        while (true) {
//            //poll(5000)： 如果拉到数据的话 会立即放回；如果拉不到数据的话，等5s后返回
//            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
//            for (ConsumerRecord<String, String> record : records) {
//                System.out.println("-----------------");
//                int partition = record.partition();
//                System.out.printf("partition = %d,offset = %d, value = %s", partition,record.offset(), record.value());
//                System.out.println();
//
//                //每次消费完一条消息后手动提交offset
//                //kafkaConsumer.commitAsync();
//            }
//        }
//    }
//
//}
//
//
