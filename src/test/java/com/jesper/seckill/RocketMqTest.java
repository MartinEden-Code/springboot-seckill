package com.jesper.seckill;

import com.jesper.seckill.bean.ProductOrder;
import com.jesper.seckill.config.JmsConfig;
import com.jesper.seckill.rocketmq.producer.PayProducer;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.consumer.PullResultExt;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/5/19 16:49
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class RocketMqTest {

    private static final Logger logger = LoggerFactory.getLogger(RocketMqTest.class);

    /**使用RocketMq的生产者，使用默认配置*/
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Autowired
    private DefaultMQPullConsumer defaultMQPullConsumer;


    @Test
    public void send() throws MQClientException, RemotingException, MQBrokerException, InterruptedException{
        String msg = "demo msg test";
        logger.info("开始发送消息："+msg);
        Message sendMsg = new Message("DemoTopic","DemoTag",msg.getBytes());
        //默认3秒超时
        SendResult sendResult = defaultMQProducer.send(sendMsg);
        logger.info("消息发送响应信息："+sendResult.toString());
    }


    /**
     * 连续生成多条消息
     * @throws MQClientException
     * @throws InterruptedException
     */
    @Test
    public void sendMutil() throws MQClientException, InterruptedException {

        //需要一个producer group名字作为构造方法的参数，这里为producer1 todo 这里如果不指定producergroup名字会出错
        DefaultMQProducer producer = new DefaultMQProducer("producer1");

        //设置NameServer地址,此处应改为实际NameServer地址，多个地址之间用；分隔
        //NameServer的地址必须有，但是也可以通过环境变量的方式设置，不一定非得写死在代码里
        producer.setNamesrvAddr("localhost:9876");

        //为避免程序启动的时候报错，添加此代码，可以让rocketMq自动创建topickey
        producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");
        producer.start();

        for(int i=0;i<10;i++){
            try {
                Message message = new Message("DemoTopic", "DemoTag",
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

                SendResult sendResult = producer.send(message);

                System.out.println("发送的消息ID:" + sendResult.getMsgId() +"--- 发送消息的状态：" + sendResult.getSendStatus());
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }

        producer.shutdown();

    }

    /**
     *  启动消费者（push方式 优点：实时性高，但增加服务端负载，如果push速度够快会出现很多问题），持续监听生产者数据并打印
     * @param args
     * @throws MQClientException
     */
    /*public static void main(String[] args) throws MQClientException {
        //设置消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("CID_LRW_DEV_SUBS");

        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        //设置消费者端消息拉取策略，表示从哪里开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        //设置消费者拉取消息的策略，*表示消费该topic下的所有消息，也可以指定tag进行消息过滤
        consumer.subscribe("DemoTopic", "*");

        //消费者端启动消息监听，一旦生产者发送消息被监听到，就打印消息，和rabbitmq中的handlerDelivery类似
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt messageExt : msgs) {
                    String topic = messageExt.getTopic();
                    String tag = messageExt.getTags();
                    String msg = new String(messageExt.getBody());
                    System.out.println("*********************************");
                    System.out.println("消费响应：msgId : " + messageExt.getMsgId() + ",  msgBody : " + msg + ",	tag:" + tag + ", topic:" + topic);
                    System.out.println("*********************************");
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        //调用start()方法启动consumer
        consumer.start();
        System.out.println("Consumer Started....");
    }*/

    //*****************************  rocketMq 消费消息的两种方式 pull push **********************************************
    //1.pull 消费者主动去 broker拉取  取消息过程需要自己写，步骤：首先通过目标topic中拿到messageQueue队列集合;遍历MessageQueue集合，然后针对每个MessageQueue
    //批量取消息，一次取完后，记录该队列下一次要取的开始 offset 直到取完 再换另一个MessageQueue  注意，生产者消费者可以指定固定的MessageQueue,

    //2.push 主动推送给消费者  consumer把轮询过程封装了，并注册MessageListener监听器，取到消息后唤醒 MessageListener的ConsumeMessage方法来消费，对用户
    //而言，感觉消息是被推送过来的
    //*****************************  **********************************************************************************

    /**
     * 指定消息投递的队列
     */
    @Test
    public void sendQueueSelect() throws InterruptedException {

        // 发送10条消息到Topic为TopicTest，tag为TagA，消息内容为“Hello RocketMQ”拼接上i的值
        for (int i = 0; i < 10; i++) {
            try {
                Message msg = new Message(JmsConfig.DEMO_TOPIC, // topic
                        JmsConfig.DEMO_TAG, // tag
                        "i" + i, ("Hello RocketMQ " + i).getBytes("utf-8")// body
                );

                // 调用producer的send()方法发送消息
                // 这里调用的是同步的方式，所以会有返回结果
                //SendResult sendResult = defaultMQProducer.send(msg);

                //指定消息投递的队列，同步的方式，会有返回结果
				SendResult sendResult = defaultMQProducer.send(msg, new MessageQueueSelector() {
					@Override
					public MessageQueue select(List<MessageQueue> queues, Message msg, Object queNum) {
						int queueNum = Integer.parseInt(queNum.toString());
						return queues.get(queueNum);
					}
				}, 0);

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

    //消费者 pull拉取数据 todo 为什么能重复的消费同样的消息 ？
    private static final Map<MessageQueue, Long> offsetTable = new HashMap<MessageQueue, Long>();

    @Test
    public  void pullConsumer() throws Exception {

        offsetTable.clear();

        try {
            Set<MessageQueue> mqs = defaultMQPullConsumer.fetchSubscribeMessageQueues(JmsConfig.DEMO_TOPIC);
            for (MessageQueue mq : mqs) {
                System.out.println("Consume from the queue: " + mq);

                System.out.println("当前获取的消息的归属队列是: " + mq.getQueueId());
                if (mq.getQueueId() == 0) {

                    //System.out.println("我是从第1个队列获取消息的");
                    // long offset = consumer.fetchConsumeOffset(mq, true);
                    // PullResultExt pullResult
                    // =(PullResultExt)consumer.pull(mq,
                    // null, getMessageQueueOffset(mq), 32);
                    // 消息未到达默认是阻塞10秒，private long consumerPullTimeoutMillis =
                    // 1000 *
                    // 10;

                    PullResultExt pullResult = (PullResultExt) defaultMQPullConsumer.pullBlockIfNotFound(mq, null,
                            getMessageQueueOffset(mq), 32);

                    putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
                    switch (pullResult.getPullStatus()) {

                        case FOUND:

                            List<MessageExt> messageExtList = pullResult.getMsgFoundList();
                            for (MessageExt m : messageExtList) {
                                System.out.println("收到了消息:" + new String(m.getBody()));
                            }
                            break;

                        case NO_MATCHED_MSG:
                            break;

                        case NO_NEW_MSG:
                            break;

                        case OFFSET_ILLEGAL:
                            break;

                        default:
                            break;
                    }
                }
            }

        } catch (MQClientException e) {
            e.printStackTrace();
        }
        //关闭消费者
        defaultMQPullConsumer.shutdown();

    }

    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        offsetTable.put(mq, offset);
    }

    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = offsetTable.get(mq);
        if(offset != null) {
            return offset;
        }
        return 0;
    }


    //******************************顺序消费****************************************************************************
    @Autowired
    private PayProducer payProducer;

    /**
     * 发送顺序消息
     * @return
     * @throws Exception
     */
    @Test
    public void sendOrderly() throws Exception{
        List<ProductOrder> list = ProductOrder.getOrderList();
        for(int i=0;i<list.size();i++){
            ProductOrder order = list.get(i);
            Message message = new Message(JmsConfig.TOPIC_ORDERLY, "",order.getOrderId()+"", order.toString().getBytes());

            SendResult sendResult = payProducer.getProducer().send(message, new MessageQueueSelector() {
                //相同的orderId对应的消息将会被投递到相同的队列中
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    long orderId = (Long)arg;
                    long index = orderId % mqs.size();
                    return mqs.get((int)index);
                }

            },order.getOrderId());
            logger.info("发送结果{}, msg={} ",sendResult.getSendStatus(), sendResult.toString());
        }
    }



}
