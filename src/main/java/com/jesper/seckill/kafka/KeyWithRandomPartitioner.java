//package com.jesper.seckill.kafka;
//
///**
// * @author hongxingyi
// * @description TODO
// * @date 2022/5/27 17:33
// */
//
//import org.apache.kafka.clients.producer.Partitioner;
//import org.apache.kafka.common.Cluster;
//import org.apache.kafka.common.PartitionInfo;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
///**
// * 自定义分区写入策略类
// */
//public class KeyWithRandomPartitioner implements Partitioner {
//
//    private Random r;
//
//    @Override
//    public int partition(String topic, Object key, byte[] bytes, Object value, byte[] bytes1, Cluster cluster) {
//        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
//        int numPartitions = partitions.size();
//        System.out.println(numPartitions);
//        return Integer.valueOf(value.toString())%numPartitions;
//    }
//
//    @Override
//    public void close() {
//
//    }
//
//    @Override
//    public void configure(Map<String, ?> map) {
//        r = new Random();
//    }
//
//}
//
