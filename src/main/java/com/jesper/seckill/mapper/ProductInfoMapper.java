package com.jesper.seckill.mapper;

import com.jesper.seckill.bean.ProductInfo;
import com.jesper.seckill.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/5/28 17:21
 */
@Mapper
public interface ProductInfoMapper {
    /**
     * 修改商品信息
     * @return
     */
    @Update("update productinfo set price=#{price}  where id=#{id}")
    int updateProductPrice(ProductInfo productInfo);

    /**
     * 查询商品信息
     * @param id
     * @return
     */
    @Select("select * from productinfo where id = #{id}")
    public ProductInfo selectProductInfoById(@Param("id")int id);

}
