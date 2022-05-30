package com.jesper.seckill.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/5/28 17:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {
    private int id;
    private String name;
    private int price;
}
