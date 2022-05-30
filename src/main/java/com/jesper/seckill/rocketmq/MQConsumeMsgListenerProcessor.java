//package com.jesper.seckill.rocketmq;
//
//
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import java.util.List;
//
///**
// * @author hongxingyi
// * @description TODO 消费者监听 MessageListenerConcurrently 并行消费
// * @date 2022/5/19 16:35
// */
//@Component
//public class MQConsumeMsgListenerProcessor implements MessageListenerConcurrently {
//
//    private static final Logger logger = LoggerFactory.getLogger(MQConsumeMsgListenerProcessor.class);
//
//    @Override
//    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//        //消费消息 ，重写consumeMessage方法，放入具体业务
//        if(CollectionUtils.isEmpty(msgs)){
//            logger.info("接收到的消息为空，不做任何处理");
//            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//        }
//        MessageExt messageExt = msgs.get(0);
//        String msg = new String(messageExt.getBody());
//        //logger.info("接收到的消息是："+messageExt.toString());
//        logger.info("接收到的消息是："+msg);
//        if(messageExt.getTopic().equals("你的topic")){
//            if(messageExt.getTags().equals("你的tag")){
//                int reconsumeTimes = messageExt.getReconsumeTimes();
//                if(reconsumeTimes == 3){
//                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//                }
//                //TODO 处理对应的业务逻辑
//            }
//        }
//
//        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//    }
//
//}
//
