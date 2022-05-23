package com.jesper.seckill.mapper;

import com.jesper.seckill.bean.AccountInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/5/24 0:10
 */
@Mapper
@Component
public interface AccountInfoMapper {

    /**
     * 增加金额
     * @param accountNo
     * @param amount
     * @return
     */
    @Update("update account_info set account_balance=account_balance+#{amount} where account_no=#{accountNo}")
    int updateAccountBalance(@Param("accountNo") String accountNo, @Param("amount") Double amount);

    @Select("select * from account_info  where account_no=#{accountNo}")
    AccountInfo findByIdAccountNo(@Param("accountNo") String accountNo);

    /**
     * 查询之前是否已经添加过
     * @param txNo
     * @return
     */
    @Select("select count(1) from de_duplication where tx_no = #{txNo}")
    int isExistTx(String txNo);

    @Insert("insert into de_duplication values(#{txNo},now());")
    int addTx(String txNo);





}

