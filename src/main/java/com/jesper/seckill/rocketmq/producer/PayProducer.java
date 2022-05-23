package com.jesper.seckill.rocketmq.producer;


import com.jesper.seckill.config.JmsConfig;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;

/**
 * @author hongxingyi
 * @description TODO 消息延迟 生成者
 * @date 2022/5/19 23:58
 */
@Component
public class PayProducer {

    private String producerGroup = "pay_producer_group";

    private DefaultMQProducer producer;

    public  PayProducer(){
        producer = new DefaultMQProducer(producerGroup);

        //生产者投递消息重试次数
        producer.setRetryTimesWhenSendFailed(3);

        //指定NameServer地址，多个地址以 ; 隔开
        producer.setNamesrvAddr(JmsConfig.NAME_SERVER);

        start();
    }

    public DefaultMQProducer getProducer(){
        return this.producer;
    }

    /**
     * 对象在使用之前必须要调用一次，只能初始化一次
     */
    public void start(){
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 一般在应用上下文，使用上下文监听器，进行关闭
     */
    public void shutdown(){
        this.producer.shutdown();
    }

}
