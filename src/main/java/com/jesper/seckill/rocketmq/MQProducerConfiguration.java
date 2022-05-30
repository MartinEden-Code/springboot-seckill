//package com.jesper.seckill.rocketmq;
//
////import com.alibaba.rocketmq.client.exception.MQClientException;
////import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
//import com.jesper.seckill.exception.GlobalException;
//import com.jesper.seckill.result.CodeMsg;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.client.producer.DefaultMQProducer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.StringUtils;
//
///**
// * @author hongxingyi
// * @description TODO rocketmq 生产者 配置类
// * @date 2022/5/19 16:32
// */
//@Configuration
//public class MQProducerConfiguration {
//
//    public static final Logger LOGGER = LoggerFactory.getLogger(MQProducerConfiguration.class);
//
//    /**
//     * 发送同一类消息的设置为同一个group，保证唯一,默认不需要设置，rocketmq会使用ip@pid(pid代表jvm名字)作为唯一标示
//     */
//    @Value("${rocketmq.producer.groupName}")
//    private String groupName;
//
//    @Value("${rocketmq.producer.namesrvAddr}")
//    private String namesrvAddr;
//
//    /**
//     * 消息最大大小，默认4M
//     */
//    @Value("${rocketmq.producer.maxMessageSize}")
//    private Integer maxMessageSize ;
//    /**
//     * 消息发送超时时间，默认3秒
//     */
//    @Value("${rocketmq.producer.sendMsgTimeout}")
//    private Integer sendMsgTimeout;
//    /**
//     * 消息发送失败重试次数，默认2次
//     */
//    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
//    private Integer retryTimesWhenSendFailed;
//
//    @Bean
//    public DefaultMQProducer getRocketMQProducer()  {
//
//        if (StringUtils.isEmpty(groupName)){
//            throw new GlobalException(CodeMsg.ROCKETMQ_GROUPNAME_ERROR);
//        }
//        if (StringUtils.isEmpty(namesrvAddr)){
//            throw new GlobalException(CodeMsg.ROCKETMQ_NAMEADDR_ERROR);
//        }
//
//        DefaultMQProducer producer;
//        producer = new DefaultMQProducer(this.groupName);
//
//        producer.setNamesrvAddr(this.namesrvAddr);
//        producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");
//
//        //如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
//        //producer.setInstanceName(instanceName);
//        if(this.maxMessageSize!=null){
//            producer.setMaxMessageSize(this.maxMessageSize);
//        }
//        if(this.sendMsgTimeout!=null){
//            producer.setSendMsgTimeout(this.sendMsgTimeout);
//        }
//        //如果发送消息失败，设置重试次数，默认为2次
//        if(this.retryTimesWhenSendFailed!=null){
//            producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
//        }
//
//        try {
//            producer.start();
//
//            LOGGER.info(String.format("producer is start ! groupName:[%s],namesrvAddr:[%s]"
//                    , this.groupName, this.namesrvAddr));
//        } catch (MQClientException e) {
//            LOGGER.error(String.format("producer is error {}", e.getMessage(),e));
//            throw new GlobalException(CodeMsg.ROCKETMQ_PRODUCER_ERROR);
//        }
//        return producer;
//
//    }
//
//
//}
//
