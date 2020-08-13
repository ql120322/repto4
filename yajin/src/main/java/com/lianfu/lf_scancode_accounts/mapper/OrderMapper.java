package com.lianfu.lf_scancode_accounts.mapper;

import com.lianfu.lf_scancode_accounts.model.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface OrderMapper {
    String selectCashoutMoney(String merid);

    String selectCashoutMoneyModel2(String merid);


}
