package com.jesper.seckill.rocketmq.consumer;

import com.jesper.seckill.config.JmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hongxingyi
 * @description TODO 消费者 pull主动拉取消息
 * @date 2022/5/20 9:29
 */
@Component
public class PullConsumer {


    /*private static final Map<MessageQueue, Long> offsetTable = new HashMap<MessageQueue, Long>();

    @Autowired
    private DefaultMQPullConsumer defaultMQPullConsumer;


    public  void pull() throws Exception {
        offsetTable.clear();

        try {
            Set<MessageQueue> mqs = defaultMQPullConsumer.fetchSubscribeMessageQueues(JmsConfig.DEMO_TOPIC);
            for (MessageQueue mq : mqs) {
                // System.out.println("Consume from the queue: " + mq);
                System.out.println("当前获取的消息的归属队列是: " + mq.getQueueId());
                // if (mq.getQueueId() == 0) {

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
            // }

        } catch (MQClientException e) {
            e.printStackTrace();
        }

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
    }*/

}
