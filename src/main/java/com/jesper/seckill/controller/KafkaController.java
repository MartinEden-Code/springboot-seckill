//package com.jesper.seckill.controller;
//
//import com.jesper.seckill.bean.ProductInfo;
//import com.jesper.seckill.kafka.producer.KafkaProducer;
//import com.jesper.seckill.result.CodeMsg;
//import com.jesper.seckill.result.Result;
//import com.jesper.seckill.service.ProductInfoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
///**
// * @author hongxingyi
// * @description TODO kafaka controller类
// * @date 2022/5/24 23:22
// */
//
//@Controller
//@RequestMapping("/kafka")
//public class KafkaController {
//
//    //******************************* springboot 简单整合kafuka**********************************************************
//    @Autowired
//    private KafkaProducer producer;
//
//    @RequestMapping("/sendMsg")
//    @ResponseBody
//    public String testSendMsg(){
//        producer.send();
//        return "success";
//    }
//
//    //**************************** 使用kafka进行商品维度化缓存解决方案*******************************************************
//    @Autowired
//    private ProductInfoService kafkaService;
//
//    //http://localhost:8082/admin/updatePrice?id=11213&price=66
//    @RequestMapping("/updatePrice")
//    @ResponseBody
//    public Result<String> updateProductPrice(ProductInfo productInfo){
//        int res = kafkaService.updateProductPrice(productInfo);
//        if(res != 0){
//            return Result.success("update success");
//        }
//        return Result.error(CodeMsg.SERVER_ERROR);
//    }
//
//    //上一步更新之后查询
//    @RequestMapping("/getProductInfo")
//    @ResponseBody
//    public Result<Object> getProductInfo(int id){
//        ProductInfo productInfo = kafkaService.getProductInfoById(id);
//        if(productInfo != null){
//            return Result.success(productInfo);
//        }
//        return Result.error(CodeMsg.SERVER_ERROR);
//    }
//
//    //****************************  kafka发送消息至指定分区 详见 KafkaTest**************************************************
//
//
//    //****************************  kafka指定分区消费 详见 KafkaTest**************************************************
//
//
//    //******************************kafka offset机制 **********************
//
//
//    //***************** kafaka 消息分区策略********************************************
//    //1.轮询策略
//    //2.按key分区策略
//
//
//    //****************  kafka消费者分区消费策略  详情见 kafka.md     ************************************************
//    //背景： 消费者组的rebalance机制： rebalance机制称为再均衡 是kafka中确保Consumer group下的所有consumer 如何达成一致，最优分配订阅topic分区的一种机制
//    //发生时机： 1.consumer 消费者个数发生变化 2.topic个数发生变化，3.topic分区数发生变化
//    //1.range范围分配策略【默认，容易产生数据倾斜】
//    //2.roundRobin 轮询策略【把所有的分区和consumer列出，轮询consumer和partition，尽可能均匀分配】
//    //3.strickey年薪分配策略，【再执行一次新的分配之前，考虑上一次分配的结果，尽量少的调整分配的变动，节省大量开销】
//
//
//}
//
