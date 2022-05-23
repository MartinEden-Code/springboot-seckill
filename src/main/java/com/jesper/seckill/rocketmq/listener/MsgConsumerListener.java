//package com.jesper.seckill.rocketmq.listener;
//
//import com.alibaba.fastjson.JSONObject;
//import com.jesper.seckill.bean.AccountChangeEvent;
//import com.jesper.seckill.config.JmsConfig;
//import com.jesper.seckill.service.AccountInfoService;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @author hongxingyi
// * @description TODO 转账消费者监听类 监听指定的topic,收到消息后执行本地事务
// * @date 2022/5/24 0:34
// */
//@Component
//@RocketMQMessageListener(consumerGroup = "consumer_group_txmsg_bank2",topic = JmsConfig.TRANSACTION_TOPIC)
//public class MsgConsumerListener implements RocketMQListener<String> {
//
//    private static Logger log = LoggerFactory.getLogger(MsgConsumerListener.class);
//
//    @Autowired
//    AccountInfoService accountInfoService;
//
//    /**
//     * 接收消息,执行本地事务
//     * @param message
//     */
//    @Override
//    public void onMessage(String message) {
//
//        log.info("开始消费消息:{}",message);
//        JSONObject jsonObject = JSONObject.parseObject(message);
//        String accountChangeString = jsonObject.getString("accountChange");
//        //转成AccountChangeEvent
//        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChangeString, AccountChangeEvent.class);
//        //设置账号为李四的
//        accountChangeEvent.setAccountNo("2");
//        //更新本地账户，增加金额
//        accountInfoService.addAccountInfoBalance(accountChangeEvent);
//    }
//}
