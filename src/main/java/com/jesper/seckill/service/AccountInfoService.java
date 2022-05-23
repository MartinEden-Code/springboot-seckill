//package com.jesper.seckill.service;
//
//import com.alibaba.fastjson.JSONObject;
//import com.jesper.seckill.bean.AccountChangeEvent;
//import com.jesper.seckill.config.JmsConfig;
//import com.jesper.seckill.mapper.AccountInfoMapper;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * @author hongxingyi
// * @description TODO 转账 业务类
// * @date 2022/5/24 0:09
// */
//
//@Service
//public class AccountInfoService {
//
//    @Autowired
//    private AccountInfoMapper accountInfoDao;
//
//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;
//
//    private static Logger logger = LoggerFactory.getLogger(AccountInfoService.class);
//
//    /**
//     * 发送MQ转账消息
//     *
//     * @param accountChangeEvent
//     */
//    public void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("accountChange", accountChangeEvent);
//        String jsonString = jsonObject.toJSONString();
//        //生成message类型
//        Message<String> message = MessageBuilder.withPayload(jsonString).build();
//        //发送一条事务消息
//        String producerGroup = "producer_group_txmsg_bank1";
//
//        rocketMQTemplate.sendMessageInTransaction(producerGroup, JmsConfig.TRANSACTION_TOPIC, message, null);
//    }
//
//    /**
//     * 更新账户，扣减金额
//     *
//     * @param accountChangeEvent
//     */
//    @Transactional
//    public void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
//        //幂等判断
//        Integer txNo = accountInfoDao.isExistTx(accountChangeEvent.getTxNo());
//        if (txNo > 0) {
//            logger.info("已经扣减过，无需重复扣减");
//            return;
//        }
//        //扣减金额
//        accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(), accountChangeEvent.getAmount() * -1);
//        //添加事务日志
//        accountInfoDao.addTx(accountChangeEvent.getTxNo());
//        if (accountChangeEvent.getAmount() == 3) {
//            throw new RuntimeException("人为制造异常");
//        }
//    }
//
//
//    //consumer 消费者添加金额业务【这两个业务理应分开在不同的服务，以体现和模拟分布式服务转账场景】 ***********************************************************
//
//    /**
//     * 更新账户，增加金额
//     *
//     * @param accountChangeEvent
//     */
//    @Transactional
//    public void addAccountInfoBalance(AccountChangeEvent accountChangeEvent) {
//        logger.info("bank2更新本地账号，账号：{},金额：{}", accountChangeEvent.getAccountNo(), accountChangeEvent.getAmount());
//
//        Integer doSuccess = accountInfoDao.isExistTx(accountChangeEvent.getTxNo());
//        if (doSuccess > 0) {
//            return;
//        }
//        //增加金额
//        accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(), accountChangeEvent.getAmount());
//        //添加事务记录，用于幂等
//        accountInfoDao.addTx(accountChangeEvent.getTxNo());
//        if (accountChangeEvent.getAmount() == 4) {
//            throw new RuntimeException("人为制造异常");
//        }
//    }
//
//}
//
