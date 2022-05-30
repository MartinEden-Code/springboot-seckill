//package com.jesper.seckill.rocketmq.consumer;
//
//import com.jesper.seckill.config.JmsConfig;
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// *    1.如果两个消费者group和topic都一样，则二者轮循接收消息
// *    2.如果两个消费者topic一样，而group不一样，则消息变成广播机制
// * @author hongxingyi
// * @description TODO 事务消息消费者
// * @date 2022/5/20 16:45
// */
//
//
//@Component
//public class TransactionConsumer {
//
//    private DefaultMQPushConsumer consumer;
//
//    private String consumerGroup = "transac_consumer_group";
//
//    public TransactionConsumer() throws MQClientException {
//
//        consumer = new DefaultMQPushConsumer(consumerGroup);
//        consumer.setNamesrvAddr("localhost:9876");
//        //设置消费者端拉取消息策略，表示从哪里开始消费
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//        // 默认是集群方式，可以更改为广播，但是广播方式不支持重试  *表示该topic下的所有消息，也可也指定tag进行消息过滤
//        consumer.setMessageModel(MessageModel.CLUSTERING);
//        consumer.subscribe(JmsConfig.TRANSACTION_TOPIC, "*");
//
//        //消费者端启动消息监听，一旦生产者发送消息被监听到，就打印消息   。
//        consumer.registerMessageListener(new MessageListenerConcurrently() {
//
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//                MessageExt msg = msgs.get(0);
//                String key = msg.getKeys();
//                try {
//
//                    System.out.printf("%s 2 Receive New Messages: %s %n", Thread.currentThread().getName(),
//                            new String(msgs.get(0).getBody()));
//                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//                } catch (Exception e) {
//                    System.out.println("消费异常");
//                    e.printStackTrace();
//                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//                }
//            }
//        });
//
//        consumer.start();
//        System.out.println("transation consumer  start ...");
//    }
//
//}
