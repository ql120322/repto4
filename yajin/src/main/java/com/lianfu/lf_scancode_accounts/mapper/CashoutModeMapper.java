package com.lianfu.lf_scancode_accounts.mapper;

import com.lianfu.lf_scancode_accounts.model.CashoutMode;

import com.lianfu.lf_scancode_accounts.model.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface CashoutModeMapper {
    public List<Shop> selectCashoutModeByMersId(int[] mers);

    //押金审核需要
     String seletmodes(int id);

}
