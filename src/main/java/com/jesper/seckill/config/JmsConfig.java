package com.jesper.seckill.config;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/5/19 23:52
 */
public class JmsConfig {


    public static final String NAME_SERVER = "localhost:9876";

    /**
     * 延迟消息
     */
    public static final String DELAY_TOPIC = "DELAY_TOPIC";

    public static final String DEMO_TOPIC = "DemoTopic";

    public static final String DEMO_TAG = "DemoTag";

    /**
     * 顺序消息
     */
    public static final String TOPIC_ORDERLY = "topic_orderly";

    /**
     * 事务消息
     */
    public static final String TRANSACTION_TOPIC = "transaction_topic";

    /**
     * rocketMQtemplate 模板类配合使用
     */
    public static final String SENDMESSAGE = "sendMessage";


}

