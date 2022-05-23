//package com.jesper.seckill.rocketmq.consumer;
//
//import com.jesper.seckill.config.JmsConfig;
//import org.apache.rocketmq.spring.annotation.MessageModel;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.stereotype.Component;
//
///**
// * @author hongxingyi
// *   1.如果两个消费者group和topic都一样，则二者轮循接收消息
// *   2.如果两个消费者topic一样，而group不一样，则消息变成广播机制
// * @description TODO 和 rocketMqtemplate 模板 配合定义的消费者
// * @date 2022/5/23 9:55
// */
//@Component
//@RocketMQMessageListener(
//        topic = JmsConfig.SENDMESSAGE,               // 1.topic：消息的发送者使用同一个topic
//        consumerGroup = "test-group",               // 2.group：不用和生产者group相同 ( 在RocketMQ中消费者和发送者组没有关系 )
//        selectorExpression = "*",                  // 3.tag：设置为 * 时，表示全部。
//        messageModel = MessageModel.CLUSTERING    // 4.消费模式：默认 CLUSTERING （ CLUSTERING：负载均衡 ）（ BROADCASTING：广播机制 ）
//)
//public class Consumer implements RocketMQListener<String> {
//
//    @Override
//    public void onMessage(String str) {
//        try {
//            // 睡眠五十毫秒，确保消息成功接收
//            Thread.sleep(50);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(str);
//    }
//}
