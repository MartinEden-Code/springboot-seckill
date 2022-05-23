package com.jesper.seckill.rocketmq.consumer;

import com.jesper.seckill.config.JmsConfig;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *    1.如果两个消费者group和topic都一样，则二者轮循接收消息
 *    2.如果两个消费者topic一样，而group不一样，则消息变成广播机制
 * @author hongxingyi
 * @description TODO 监听消息  MessageListenerOrderly：顺序消费 消费端顺序获取订单的各个流程信息
 * @date 2022/5/20 11:18
 */
@Component
public class PayConsumerOrderly {

    private DefaultMQPushConsumer consumer;

    //
    //注意修改消费者组，否则会产生其他问题
    private String consumerGroup = "pay_consumer_orderly_group";

    public PayConsumerOrderly() throws MQClientException, MQClientException {

        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        // 默认是集群方式，可以更改为广播，但是广播方式不支持重试
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.subscribe(JmsConfig.TOPIC_ORDERLY, "*");

        //注意这里不再是MessageListenerConcurrently 并行消费  而是MessageListenerOrderly 顺序消费
        consumer.registerMessageListener(new MessageListenerOrderly() {

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                MessageExt msg = msgs.get(0);
                int times = msg.getReconsumeTimes();
                //System.out.println("重试次数=" + times);

                int queueId = msg.getQueueId();
                System.out.println("当前消费的消息的队列编号是:" + queueId);

                try {
                    System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(),
                            new String(msgs.get(0).getBody()) + "\r\n");
                    return ConsumeOrderlyStatus.SUCCESS;
                } catch (Exception e) {
                    times++;
                    e.printStackTrace();
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            }
        });

        consumer.start();
        System.out.println("consumer 顺序消费 start ...");
    }

}

