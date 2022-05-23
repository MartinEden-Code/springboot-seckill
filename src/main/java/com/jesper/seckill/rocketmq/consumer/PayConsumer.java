package com.jesper.seckill.rocketmq.consumer;

import com.jesper.seckill.config.JmsConfig;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author hongxingyi
 * *   1.如果两个消费者group和topic都一样，则二者轮循接收消息
 *  *   2.如果两个消费者topic一样，而group不一样，则消息变成广播机制
 * @description TODO 延迟消息消费者，其实就是一个监听队列的程序（启动项目时就已经监听）
 * @date 2022/5/20 0:00
 */
@Component
public class PayConsumer {

    private DefaultMQPushConsumer consumer;

    private String consumerGroup = "pay_consumer_group";

    public  PayConsumer() throws MQClientException, MQClientException {

        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        //默认是集群方式，可以更改为广播，但是广播方式不支持重试
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.subscribe(JmsConfig.DELAY_TOPIC, "*");


        consumer.registerMessageListener( new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt msg = msgs.get(0);
                int times = msg.getReconsumeTimes();
                System.out.println("重试次数="+times);

                try {
                    System.out.printf("%s Receive New Messages: %s %n",
                            Thread.currentThread().getName(), new String(msgs.get(0).getBody()));

                    String topic = msg.getTopic();
                    String body = new String(msg.getBody(), "utf-8");
                    String tags = msg.getTags();
                    String keys = msg.getKeys();

                    System.out.println("topic=" + topic + ", tags=" + tags + ", keys=" + keys + ", msg=" + body);

                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

                } catch (Exception e) {
                    System.out.println("消费异常");
                    //如果重试2次不成功，则记录，人工介入
                    if(times >= 2){
                        System.out.println("重试次数大于2，记录数据库，发短信通知开发人员或者运营人员");
                        //TODO 记录数据库，发短信通知开发人员或者运营人员
                        //告诉broker，消息成功
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });

        consumer.start();
        System.out.println("consumer listener start ...");
    }

}

