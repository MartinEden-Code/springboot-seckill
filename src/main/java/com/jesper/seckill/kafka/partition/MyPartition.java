//package com.jesper.seckill.kafka.partition;
//
//import org.apache.kafka.clients.producer.Partitioner;
//import org.apache.kafka.common.Cluster;
//
//import java.util.Map;
//
///**
// * @author hongxingyi
// * @description TODO 实现Partition接口，将消息发送给指定分区
// * @date 2022/5/26 11:01
// */
//public class MyPartition implements Partitioner {
//
//    /**
//     * 发送分区为 1 的消息
//     */
//    @Override
//    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
//        return 1;
//    }
//
//    @Override
//    public void close() {
//
//    }
//
//    @Override
//    public void configure(Map<String, ?> map) {
//
//    }
//}
