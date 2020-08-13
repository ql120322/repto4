package com.lianfu.lf_scancode_accounts.mapper;

import com.lianfu.lf_scancode_accounts.model.CashoutOrder;
import com.lianfu.lf_scancode_accounts.model.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface CashoutOrderMapper {
    List<CashoutOrder> getCashoutOrders(String openid);

    void addCashoutOrder(CashoutOrder co);

    void updateDevice(Shop co);

    void updateOrders(Shop co);
//---------------------------------- 押金审核添加
    List<CashoutOrder>  seletCashoutOrders();
    CashoutOrder  seletCashoutOrdersWhereId(int id);



}
