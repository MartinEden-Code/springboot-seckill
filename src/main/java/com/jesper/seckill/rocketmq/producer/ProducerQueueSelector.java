package com.jesper.seckill.rocketmq.producer;


import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hongxingyi
 * @description TODO 指定消息投递的队列
 * @date 2022/5/20 9:12
 */
@Component
public class ProducerQueueSelector {

    /**使用RocketMq的生产者，使用默认配置*/
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    public void sendQueueSelect() throws Exception {
        // 发送10条消息到Topic为TopicTest，tag为TagA，消息内容为“Hello RocketMQ”拼接上i的值
        for (int i = 0; i < 10; i++) {
            try {
                Message msg = new Message("TopicTest", // topic
                        "TagA", // tag
                        "i" + i, ("Hello RocketMQ " + i).getBytes("utf-8")// body
                );
                // 调用producer的send()方法发送消息
                // 这里调用的是同步的方式，所以会有返回结果
                SendResult sendResult = defaultMQProducer.send(msg);

                //指定消息投递的队列，同步的方式，会有返回结果
				/*SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
					@Override
					public MessageQueue select(List<MessageQueue> queues, Message msg, Object queNum) {
						int queueNum = Integer.parseInt(queNum.toString());
						return queues.get(queueNum);
					}
				}, 0);*/

                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "," + i);
                // System.out.println(sendResult.getSendStatus()); //发送结果状态
                // 打印返回结果，可以看到消息发送的状态以及一些相关信息
                System.out.println("当前消息投递到的队列是 : " + sendResult.getMessageQueue().getQueueId());
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }

        // 发送完消息之后，调用shutdown()方法关闭producer
        defaultMQProducer.shutdown();

    }

}

