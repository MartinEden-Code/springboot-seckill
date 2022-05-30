//package com.jesper.seckill.controller;
//
//
//import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.exception.MQBrokerException;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.client.impl.consumer.PullResultExt;
//import org.apache.rocketmq.client.producer.*;
//import org.apache.rocketmq.common.message.Message;
//import org.apache.rocketmq.common.message.MessageExt;
//import com.jesper.seckill.bean.ProductOrder;
//import com.jesper.seckill.config.JmsConfig;
//import com.jesper.seckill.result.Result;
//import com.jesper.seckill.rocketmq.producer.PayProducer;
//import com.jesper.seckill.rocketmq.producer.TransactionProducer;
//import org.apache.rocketmq.common.message.MessageQueue;
//import org.apache.rocketmq.remoting.exception.RemotingException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * @author hongxingyi
// * @description TODO
// * @date 2022/5/19 16:16
// */
//@Controller
//@RequestMapping("/rocket")
//public class RocketMqController {
//
//
//
//    /**使用RocketMq的生产者*/
//    @Autowired
//    private DefaultMQProducer defaultMQProducer;
//
//    /**
//     * pull 方式消费
//     */
//    @Autowired
//    private DefaultMQPullConsumer defaultMQPullConsumer;
//
//    @Autowired
//    private DefaultMQPushConsumer defaultMQPushConsumer;
//
//    private static final Logger logger = LoggerFactory.getLogger(RocketMqController.class);
//
//    @RequestMapping(value = "/sendMessage", method = RequestMethod.GET)
//    @ResponseBody
//    public Result<String> send() throws MQClientException, MQBrokerException, InterruptedException, RemotingException {
//        String msg = "demo msg test";
//        logger.info("开始发送消息："+msg);
//        Message sendMsg = new Message("DemoTopic","DemoTag",msg.getBytes());
//        //默认3秒超时
//        SendResult sendResult = defaultMQProducer.send(sendMsg);
//        logger.info("消息发送响应信息："+sendResult.toString());
//
//        return Result.success("发送rocketMq消息成功！");
//    }
//
//    //*****************  发送延迟消息    *********************************************************************************
//
//    @Autowired
//    private PayProducer payProducer;
//    /**
//     * http://localhost:8083/rocket/delay?text=hello
//     * 发送延迟消息
//     * @param text
//     * @return
//     */
//    @RequestMapping(value = "/delay", method = RequestMethod.GET)
//    @ResponseBody
//    public Result<String> sendDelayMsg(String text) throws MQClientException, RemotingException, InterruptedException{
//        Message message = new Message(JmsConfig.DELAY_TOPIC, "delay_order",("this is a delay message:" + text).getBytes());
//
//        //"1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
//        message.setDelayTimeLevel(3);
//        payProducer.getProducer().send(message, new SendCallback() {
//
//            //消息发送成功回调
//            @Override
//            public void onSuccess(SendResult sendResult) {
//                System.out.printf("发送结果=%s, msg=%s ", sendResult.getSendStatus(), sendResult.toString());
//            }
//
//            //消息异常回调
//            @Override
//            public void onException(Throwable e) {
//                e.printStackTrace();
//                //补偿机制，根据业务情况进行使用，看是否进行重试
//            }
//        });
//        return Result.success("RocketMq发送延迟消息成功！");
//    }
//
//    //*****************************  rocketMq 消费消息的两种方式 pull push **********************************************
//    //1.pull 消费者主动去 broker拉取  取消息过程需要自己写，步骤：首先通过目标topic中拿到messageQueue队列集合;遍历MessageQueue集合，然后针对每个MessageQueue
//    //批量取消息，一次取完后，记录该队列下一次要取的开始 offset 直到取完 再换另一个MessageQueue  注意，生产者消费者可以指定固定的MessageQueue,
//
//    //2.push 主动推送给消费者  consumer把轮询过程封装了，并注册MessageListener监听器，取到消息后唤醒 MessageListener的ConsumeMessage方法来消费，对用户
//    //而言，感觉消息是被推送过来的
//    //*****************************  **********************************************************************************
//
//    @RequestMapping(value = "/sendQueueSelect", method = RequestMethod.GET)
//    @ResponseBody
//    public Result<String> sendQueueSelect() throws InterruptedException {
//
//        // 发送10条消息到Topic为TopicTest，tag为TagA，消息内容为“Hello RocketMQ”拼接上i的值
//        for (int i = 0; i < 10; i++) {
//            try {
//                Message msg = new Message(JmsConfig.DEMO_TOPIC, // topic
//                        JmsConfig.DEMO_TAG, // tag
//                        "i" + i, ("Hello RocketMQ " + i).getBytes("utf-8")// body
//                );
//
//                // 调用producer的send()方法发送消息
//                // 这里调用的是同步的方式，所以会有返回结果
//                //SendResult sendResult = defaultMQProducer.send(msg);
//
//                //指定消息投递的队列，同步的方式，会有返回结果
//                SendResult sendResult = defaultMQProducer.send(msg, new MessageQueueSelector() {
//                    @Override
//                    public MessageQueue select(List<MessageQueue> queues, Message msg, Object queNum) {
//                        int queueNum = Integer.parseInt(queNum.toString());
//                        return queues.get(queueNum);
//                    }
//                }, 0);
//
//                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "," + i);
//                // System.out.println(sendResult.getSendStatus()); //发送结果状态
//                // 打印返回结果，可以看到消息发送的状态以及一些相关信息
//                System.out.println("当前消息投递到的队列是 : " + sendResult.getMessageQueue().getQueueId());
//            } catch (Exception e) {
//                e.printStackTrace();
//                Thread.sleep(1000);
//            }
//        }
//
//        // 发送完消息之后，调用shutdown()方法关闭producer
//        defaultMQProducer.shutdown();
//        return Result.success("RocketMq发送 指定队列消息成功");
//    }
//
//    //消费者 pull拉取数据 todo 为什么能重复的消费同样的消息？
//    private static final Map<MessageQueue, Long> offsetTable = new HashMap<MessageQueue, Long>();
//
//    /**
//     * pull方式轮询拉取队列中的消息
//     * @throws Exception
//     */
//    @RequestMapping(value = "/pullConsumer", method = RequestMethod.GET)
//    @ResponseBody
//    public  Result<String> pullConsumer() throws Exception {
//
//        offsetTable.clear();
//
//        try {
//            Set<MessageQueue> mqs = defaultMQPullConsumer.fetchSubscribeMessageQueues(JmsConfig.DEMO_TOPIC);
//            for (MessageQueue mq : mqs) {
//                System.out.println("Consume from the queue: " + mq);
//
//                System.out.println("当前获取的消息的归属队列是: " + mq.getQueueId());
//                if (mq.getQueueId() == 0) {
//
//                    PullResultExt pullResult = (PullResultExt) defaultMQPullConsumer.pullBlockIfNotFound(mq, null,
//                            getMessageQueueOffset(mq), 32);
//
//                    putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
//                    switch (pullResult.getPullStatus()) {
//
//                        case FOUND:
//
//                            List<MessageExt> messageExtList = pullResult.getMsgFoundList();
//                            for (MessageExt m : messageExtList) {
//                                System.out.println("收到了消息:" + new String(m.getBody()));
//                            }
//                            break;
//
//                        case NO_MATCHED_MSG:
//                            break;
//
//                        case NO_NEW_MSG:
//                            break;
//
//                        case OFFSET_ILLEGAL:
//                            break;
//
//                        default:
//                            break;
//                    }
//                }
//            }
//
//        } catch (MQClientException e) {
//            e.printStackTrace();
//        }
//        //关闭消费者
//        defaultMQPullConsumer.shutdown();
//        return Result.success("RocketMq 消费者pull消息成功");
//    }
//
//    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
//        offsetTable.put(mq, offset);
//    }
//
//    private static long getMessageQueueOffset(MessageQueue mq) {
//        Long offset = offsetTable.get(mq);
//        if(offset != null) {
//            return offset;
//        }
//        return 0;
//    }
//
//    //*****************************  rocketMq 顺序消费（发送消息时发送到指定队列 消费消息时按顺序消费） ******************************************************************
//    // 如何实现顺序消费： 1.同一个订单消息一定要发送到同一个队列  2.一个队列只有一个消费者（不同消费者不能订阅同一个topic主题），不能出现多个消费者并行消费情况（否则 ）
//
//
//    //步骤：1.发送一组顺序订单消息，创建订单、支付订单、完成订单（每组订单消息有相同的订单号）
//    //     2.根据订单号将相同的订单号的消息发送到指定的队列
//    //     3.消费端顺序获取订单的各个流程信息
//    //*****************************  **********************************************************************************
//
//    //http://localhost:8083/rocket/sendOrderly
//    /**
//     * 发送顺序消息  对应消费者：PayConsumerOrderly  监听消息且顺序消费消息
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "/sendOrderly", method = RequestMethod.GET)
//    @ResponseBody
//    public Result<String> sendOrderly() throws Exception{
//
//        List<ProductOrder> list = ProductOrder.getOrderList();
//        for(int i=0;i<list.size();i++){
//            ProductOrder order = list.get(i);
//            Message message = new Message(JmsConfig.TOPIC_ORDERLY, "",order.getOrderId()+"", order.toString().getBytes());
//
//            SendResult sendResult = payProducer.getProducer().send(message, new MessageQueueSelector() {
//                //相同的orderId对应的消息将会被投递到相同的队列中  todo 且该队列只有一个消费者，同一个队列不能出现多个消费者并行消费的情况 虽然队列不能并行消费？， 但可以并行消费不同的队列？
//                @Override
//                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
//                    long orderId = (Long)arg;
//                    long index = orderId % mqs.size();
//                    return mqs.get((int)index);
//                }
//
//            },order.getOrderId());
//            logger.info("发送结果{}, msg={} ",sendResult.getSendStatus(), sendResult.toString());
//        }
//        return Result.success("RocketMq发送顺序消息成功");
//    }
//
//
//
//
//
//    //*****************************  rocketMq 事务消息的使用 ******************************************************************
//    // 保证事务的最终一致性 半消息： producer 发送消息到broker后未找到对应的消费者后 broker会把此消息标记为半消息 producer无法对此消息进行二次确认
//    // 消息回查 ：broker 会不定时扫描某条长期处于半消息状态的消息，主动向生产者发送询问该消息的最终状态，即消息回查 ； 网络抖动 生产者重启 导致某条消息的二次确认丢失
//
//    //步骤 rocketMq.md
//    //*****************************  **********************************************************************************
//    @Autowired
//    private TransactionProducer transactionProducer;
//
//    //http:/localhost:8083/sendTransactionMsg?tag=apk&otherParam=1
//    /**
//     * 发送事务性消息
//     * @param tag
//     * @param otherParam
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/sendTransactionMsg")
//    @ResponseBody
//    public Result<String> sendTransactionMsg(String tag, String otherParam) throws Exception {
//        Message message = new Message(JmsConfig.TRANSACTION_TOPIC, tag, tag + "_key", tag.getBytes());
//        TransactionSendResult sendResult = transactionProducer.getProducer().sendMessageInTransaction(message, otherParam);
//        logger.info("发送结果{}, sendResult{}", sendResult.getSendStatus(), sendResult.toString());
//        Thread.sleep(1000);
//        //transactionProducer.getProducer().shutdown();
//        return Result.success("发送成功");
//    }
//
//
////    //***********************************rocketmqTemplate 模板使用*****************************************************
////    //【无法和上面自定义的producery一起使用 producer.start会调用两次造成冲突，目前没找到解决办法】 上面都是直接定义生产者和消费者*******
////
////    @Autowired
////    private RocketMQTemplate rocketMQTemplate;
////
////    /**
////     * 普通字符串消息
////     */
////    public void sendMessage() {
////        String json = "普通消息";
////        rocketMQTemplate.convertAndSend(JmsConfig.SENDMESSAGE, json);
////    }
////
////    /**
////     * 同步消息
////     */
////    public void syncSend() {
////        String json = "同步消息";
////        SendResult sendMessage = rocketMQTemplate.syncSend(JmsConfig.SENDMESSAGE, json);
////        System.out.println(sendMessage);
////    }
////
////    /**
////     * 异步消息
////     */
////    public void asyncSend() {
////        String json = "异步消息";
////        SendCallback callback = new SendCallback() {
////            @Override
////            public void onSuccess(SendResult sendResult) {
////                System.out.println("123");
////            }
////
////            @Override
////            public void onException(Throwable throwable) {
////                System.out.println("456");
////            }
////        };
////        rocketMQTemplate.asyncSend(JmsConfig.SENDMESSAGE, json, callback);
////    }
////
////    /**
////     * 单向消息
////     */
////    public void onewaySend() {
////        String json = "单向消息";
////        rocketMQTemplate.sendOneWay(JmsConfig.SENDMESSAGE, json);
////    }
//
//
//    //************************************* rocketMQ 实现分布式事务解决方案（也可也用seata等）**************************************
//    //************************************* 例子，银行之间转账**************************************
//
////    @Autowired
////    private AccountInfoService accountInfoService;
////
////    //http://localhost:8083/rocket/transfer?accountNo=1&amount=2
////
////    @RequestMapping(value = "/transfer", method = RequestMethod.GET)
////    @ResponseBody
////    public Result<String> transfer(@RequestParam("accountNo") String accountNo, @RequestParam("amount") Double amount) {
////        //创建一个事务id，作为消息内容发到mq，后面将会在查询事务状态被用到
////        String tx_no = UUID.randomUUID().toString();
////        AccountChangeEvent accountChangeEvent = new AccountChangeEvent(accountNo, amount, tx_no);
////        //发送消息
////        accountInfoService.sendUpdateAccountBalance(accountChangeEvent);
////        return Result.success("转账成功");
////    }
//
//
//}
