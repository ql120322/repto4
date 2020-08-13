package com.lianfu.lf_scancode_accounts.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashoutQueryVO {
    private String totalDeposit; // 总押金
    private String totalCashout; // 可提现金额
    //-------------------------------------------
    private String cashoutmode;//返现模式
    private String cashoutmerid;//mer_id
    private String cashoutold_balance;//提现前余额
    private String cashoutmeridmoney;////merid 对应的余额
    private String newmoney;//提现后余额

    public String getNewmoney() {
        return newmoney;
    }

    public void setNewmoney(String newmoney) {
        this.newmoney = newmoney;
    }

    public String getCashoutmeridmoney() {
        return cashoutmeridmoney;
    }

    public void setCashoutmeridmoney(String cashoutmeridmoney) {
        this.cashoutmeridmoney = cashoutmeridmoney;
    }

    public String getCashoutmode() {
        return cashoutmode;
    }

    public void setCashoutmode(String cashoutmode) {
        this.cashoutmode = cashoutmode;
    }

    public String getCashoutmerid() {
        return cashoutmerid;
    }

    public void setCashoutmerid(String cashoutmerid) {
        this.cashoutmerid = cashoutmerid;
    }

    public String getCashoutold_balance() {
        return cashoutold_balance;
    }

    public void setCashoutold_balance(String cashoutold_balance) {
        this.cashoutold_balance = cashoutold_balance;
    }

    public String getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(String totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    public void setTotalCashout(String totalCashout) {
        this.totalCashout = totalCashout;
    }

    public void setTotalRem(String totalRem) {
        this.totalRem = totalRem;
    }

    public String getTotalCashout() {
        return totalCashout;
    }

    public String getTotalRem() {
        return totalRem;
    }

    public List<Shop> getShopConfigs() {
        return shopConfigs;
    }

    public void setShopConfigs(List<Shop> shopConfigs) {
        this.shopConfigs = shopConfigs;
    }

    private String totalRem; //
    private List<Shop> shopConfigs;

}
