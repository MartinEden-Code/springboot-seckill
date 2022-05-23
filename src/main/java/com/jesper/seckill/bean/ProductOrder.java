package com.jesper.seckill.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hongxingyi
 * @description TODO RocketMq 生产者消息
 * @date 2022/5/20 11:08
 */
public class ProductOrder implements Serializable {

    private static final long serialVersionUID = 7004771998065694977L;

    //订单id
    private long orderId;

    //操作类型
    private String type;


    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public  ProductOrder(){}

    public  ProductOrder(long orderId ,String type){
        this.orderId = orderId;
        this.type = type;

    }

    public static List<ProductOrder> getOrderList(){
        //模拟 业务消息顺序产生场景
        List<ProductOrder> list = new ArrayList<>();
        list.add(new ProductOrder(111L,"创建订单"));
        list.add(new ProductOrder(222L,"创建订单"));
        list.add(new ProductOrder(111L,"支付订单"));
        list.add(new ProductOrder(222L,"支付订单"));
        list.add(new ProductOrder(111L,"完成订单"));
        list.add(new ProductOrder(333L,"创建订单"));
        list.add(new ProductOrder(222L,"完成订单"));
        list.add(new ProductOrder(333L,"支付订单"));
        list.add(new ProductOrder(333L,"完成订单"));

        return list;

    }

    @Override
    public String toString() {
        return "ProductOrder{" +
                "orderId=" + orderId +
                ", type='" + type + '\'' +
                '}';
    }
}

