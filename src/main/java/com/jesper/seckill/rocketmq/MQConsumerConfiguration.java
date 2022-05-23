package com.jesper.seckill.rocketmq;

import com.jesper.seckill.exception.GlobalException;
import com.jesper.seckill.result.CodeMsg;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/5/19 16:34
 */
@Configuration
public class MQConsumerConfiguration {
    public static final Logger logger = LoggerFactory.getLogger(MQConsumerConfiguration.class);

    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.groupName}")
    private String groupName;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.topics}")
    private String topics;
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;

    @Autowired
    private MQConsumeMsgListenerProcessor mqMessageListenerProcessor;

    /**
     * 消费者通过push 接受生产者数据
     * @return
     */
    @Bean
    public DefaultMQPushConsumer getRocketMQConsumer() {

        if (StringUtils.isEmpty(groupName)){
            throw new GlobalException(CodeMsg.ROCKETMQ_GROUPNAME_ERROR);
        }
        if (StringUtils.isEmpty(namesrvAddr)){
            throw new GlobalException(CodeMsg.ROCKETMQ_NAMEADDR_ERROR);
        }
        if(StringUtils.isEmpty(topics)){
            throw new GlobalException(CodeMsg.ROCKETMQ_TOPIC_ERROR);
        }

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(mqMessageListenerProcessor);

        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        /**
         * 设置消费模型，集群还是广播，默认为集群
         */
        //consumer.setMessageModel(MessageModel.CLUSTERING);

        /**
         * 设置一次消费消息的条数，默认为1条
         */
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);

        try {
            /**
             * 设置该消费者订阅的主题和tag，如果是订阅该主题下的所有tag，
             * 则tag使用*；如果需要指定订阅该主题下的某些tag，则使用||分割，例如tag1||tag2||tag3
             */
        	/*String[] topicTagsArr = topics.split(";");
        	for (String topicTags : topicTagsArr) {
        		String[] topicTag = topicTags.split("~");
        		consumer.subscribe(topicTag[0],topicTag[1]);
			}*/
            consumer.subscribe(topics, "*");

            consumer.start();
            logger.info("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}",groupName,topics,namesrvAddr);

        } catch (Exception e) {
            logger.error("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}",groupName,topics,namesrvAddr,e);
            throw new GlobalException(CodeMsg.ROCKETMQ_CONSUMER_ERROR);
        }

        return consumer;
    }


    /**
     * 消费者通过pull 主动拉取生产者消息
     * @return
     */
    @Bean
    public DefaultMQPullConsumer getRocketMQPullConsumer() throws MQClientException {

        if (StringUtils.isEmpty(groupName)){
            throw new GlobalException(CodeMsg.ROCKETMQ_GROUPNAME_ERROR);
        }
        if (StringUtils.isEmpty(namesrvAddr)){
            throw new GlobalException(CodeMsg.ROCKETMQ_NAMEADDR_ERROR);
        }
        if(StringUtils.isEmpty(topics)){
            throw new GlobalException(CodeMsg.ROCKETMQ_TOPIC_ERROR);
        }
        //todo 注意 pullConsumer指定的 consumer group 不能和 上面配置的 DefaultMQPushConsumer 名字相同
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("pullConsumer");
        consumer.setNamesrvAddr(namesrvAddr);

        try {
            consumer.start();
            logger.info("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}",groupName,topics,namesrvAddr);
        } catch (Exception e) {
            logger.error("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}",groupName,topics,namesrvAddr,e);
            throw new GlobalException(CodeMsg.ROCKETMQ_CONSUMER_ERROR);
        }
        return consumer;
    }
}

