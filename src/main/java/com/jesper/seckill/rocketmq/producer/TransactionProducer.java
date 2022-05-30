//package com.jesper.seckill.rocketmq.producer;
//
//
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.client.producer.LocalTransactionState;
//import org.apache.rocketmq.client.producer.TransactionListener;
//import org.apache.rocketmq.client.producer.TransactionMQProducer;
//import org.apache.rocketmq.common.message.Message;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.*;
//
///**
// * @author hongxingyi
// * @description TODO 事务消息的使用
// * @date 2022/5/20 14:41
// */
//@Component
//public class TransactionProducer {
//
//    private String producerGroup = "trac_producer_group";
//
//    //事务监听器
//    private TransactionListener transactionListener = new TransactionListenerImpl();
//
//    private TransactionMQProducer producer = null;
//
//    //发送事务消息，producer需要配置线程池  一般自定义线程池的时候，需要给线程加个名称
//    private ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
//            new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
//        @Override
//        public Thread newThread(Runnable r) {
//            Thread thread = new Thread(r);
//            thread.setName("client-transaction-msg-check-thread");
//            return thread;
//        }
//    });
//
//
//    public TransactionProducer() {
//        producer = new TransactionMQProducer(producerGroup);
//        producer.setNamesrvAddr("localhost:9876");
//        //配置事务监听器
//        producer.setTransactionListener(transactionListener);
//        //配置线程池
//        producer.setExecutorService(executorService);
//        // 指定NameServer地址，多个地址以 ; 隔开
//
//        start();
//        System.out.println("Transaction procduer is started");
//
//
//    }
//
//    public TransactionMQProducer getProducer() {
//        return this.producer;
//    }
//
//    /**
//     * 对象在使用之前必须要调用一次，只能初始化一次
//     */
//    public void start() {
//        try {
//            this.producer.start();
//        } catch (MQClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 一般在应用上下文，使用上下文监听器，进行关闭
//     */
//    public void shutdown() {
//        this.producer.shutdown();
//    }
//
//}
//
///**
// * 实际处理业务的类，可能是本地带事务性的方法中处理
// * @author asus
// */
//class TransactionListenerImpl implements TransactionListener {
//
//    /**
//     * 执行本地事务代码时回调
//     * @param msg
//     * @param arg
//     * @return
//     */
//    @Override
//    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//
//        System.out.println("====executeLocalTransaction=======");
//
//        String body = new String(msg.getBody());
//        String key = msg.getKeys();
//        String transactionId = msg.getTransactionId();
//        System.out.println("transactionId="+transactionId+", key="+key+", body="+body);
//        // 执行本地事务begin TODO
//
//        // 执行本地事务end TODO
//        System.out.println("arg参数是： "+arg);
//        int status = Integer.parseInt(arg.toString());
//
//        //二次确认消息，然后消费者可以消费(消费者TransactionConsumer可以正常消费消息)
//        if(status == 1){
//            return LocalTransactionState.COMMIT_MESSAGE;
//        }
//
//        //回滚消息，broker端会删除半消息（消费者TransactionConsumer无法接受到消息）
//        if(status == 2){
//            return LocalTransactionState.ROLLBACK_MESSAGE;
//        }
//
//        //broker端会进行回查消息(checkLocalTransaction)，再或者什么都不响应
//        if(status == 3){
//            return LocalTransactionState.UNKNOW;
//        }
//
//        return LocalTransactionState.UNKNOW;
//    }
//
//
//    /**
//     * 发送确认消息时的检查回调函数 broker会指定间隔时间去回调该函数(并不会在执行本地事务执行后立即调用回调消息方法，而是隔一段时间才回去调用)，超过一定次数后就不再继续而丢弃这条消息
//     * @param msg
//     * @return
//     */
//    @Override
//    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
//
//        System.out.println("====checkLocalTransaction 回调消息检查=======");
//        String body = new String(msg.getBody());
//        String key = msg.getKeys();
//        String transactionId = msg.getTransactionId();
//        System.out.println("transactionId="+transactionId+", key="+key+", body="+body);
//        //要么commit 要么rollback
//        //可以根据key去检查本地事务消息是否完成
//        return LocalTransactionState.COMMIT_MESSAGE;
//    }
//
//}
//
