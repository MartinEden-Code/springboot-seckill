//package com.jesper.seckill.rocketmq.listener;
//
//import com.alibaba.fastjson.JSONObject;
//import com.jesper.seckill.bean.AccountChangeEvent;
//import com.jesper.seckill.mapper.AccountInfoMapper;
//import com.jesper.seckill.service.AccountInfoService;
//
//import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
//import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
//import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.Message;
//import org.springframework.stereotype.Component;
//
///**
// * @author hongxingyi
// * @description TODO 转账消息监听器 执行本地事务时，可通过 executeLocalTransaction回调方法得知 消息是否成功发送到broker
// * @date 2022/5/24 0:19
// */
//
//@Component
//@RocketMQTransactionListener(txProducerGroup = "producer_group_txmsg_bank1")
//public class TransactionMsgListener implements RocketMQLocalTransactionListener { //RocketMQLocalTransactionListener 和 TransactionListener 有什么区别？
//
//    private static Logger logger = LoggerFactory.getLogger(TransactionMsgListener.class);
//
//    @Autowired
//    AccountInfoService accountInfoService;
//
//    @Autowired
//    AccountInfoMapper accountInfoDao;
//
//    /**
//     * 事务消息发送后的回调方法，当消息发送给mq成功，此方法被回调
//     * @param message
//     * @param o
//     * @return
//     */
//    @Override
//    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
//        try {
//            //解析message，转成AccountChangeEvent
//            String messageString = new String((byte[]) message.getPayload());
//            JSONObject jsonObject = JSONObject.parseObject(messageString);
//            String accountChangeString = jsonObject.getString("accountChange");
//            AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChangeString, AccountChangeEvent.class);
//            //执行本地事务，扣减金额
//            accountInfoService.doUpdateAccountBalance(accountChangeEvent);
//            //当返回RocketMQLocalTransactionState.COMMIT，自动向mq发送commit消息，mq将消息的状态改为可消费
//            return RocketMQLocalTransactionState.COMMIT;
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("执行本地事务失败");
//            return RocketMQLocalTransactionState.ROLLBACK;
//        }
//    }
//
//    /**
//     * 事务状态回查，查询是否扣减金额
//     * @param message
//     * @return
//     */
//    @Override
//    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
//        String messageString = new String((byte[]) message.getPayload());
//        JSONObject jsonObject = JSONObject.parseObject(messageString);
//        String accountChangeString = jsonObject.getString("accountChange");
//        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChangeString, AccountChangeEvent.class);
//        //事务id
//        String txNo = accountChangeEvent.getTxNo();
//        logger.info("事务id:{}",txNo);
//        int existTx = accountInfoDao.isExistTx(txNo);
//        if (existTx > 0) {
//            logger.info("扣减金额成功");
//            return RocketMQLocalTransactionState.COMMIT;
//        } else {
//            return RocketMQLocalTransactionState.UNKNOWN;
//        }
//    }
//}
