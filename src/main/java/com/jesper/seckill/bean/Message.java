package com.jesper.seckill.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hongxingyi
 * @description TODO kafka 消息类
 * @date 2022/5/24 23:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private long id;
    private String msg;
    private Date sendTime;
}
