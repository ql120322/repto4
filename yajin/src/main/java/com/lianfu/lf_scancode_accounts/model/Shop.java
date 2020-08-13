package com.lianfu.lf_scancode_accounts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    private String shopName;
    private String deviceNo;
    private String merId;        // 店铺id
    private String cashoutMode; //返押金模式
    private String lastCashout; //device 的
    private String firstMonthReturn;  // 首月
    private String normalMonthReturn; // 次月
    private String receivableCash; // 实收押金
    private String remainderCash; // 押金余额
    private String realReturnMoney;
    private boolean cashstate;

    public String getShopName() {
        return shopName;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public String getMerId() {
        return merId;
    }

    public String getCashoutMode() {
        return cashoutMode;
    }

    public String getLastCashout() {
        return lastCashout;
    }

    public String getFirstMonthReturn() {
        return firstMonthReturn;
    }

    public String getNormalMonthReturn() {
        return normalMonthReturn;
    }

    public String getReceivableCash() {
        return receivableCash;
    }

    public String getRemainderCash() {
        return remainderCash;
    }

    public String getRealReturnMoney() {
        return realReturnMoney;
    }

    public boolean isCashstate() {
        return cashstate;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public void setCashoutMode(String cashoutMode) {
        this.cashoutMode = cashoutMode;
    }

    public void setLastCashout(String lastCashout) {
        this.lastCashout = lastCashout;
    }

    public void setFirstMonthReturn(String firstMonthReturn) {
        this.firstMonthReturn = firstMonthReturn;
    }

    public void setNormalMonthReturn(String normalMonthReturn) {
        this.normalMonthReturn = normalMonthReturn;
    }

    public void setReceivableCash(String receivableCash) {
        this.receivableCash = receivableCash;
    }

    public void setRemainderCash(String remainderCash) {
        this.remainderCash = remainderCash;
    }

    public void setRealReturnMoney(String realReturnMoney) {
        this.realReturnMoney = realReturnMoney;
    }

    public void setCashstate(boolean cashstate) {
        this.cashstate = cashstate;
    }

    public String getUpdateRemainderCash() {
        if (receivableCash != null && remainderCash != null)
            return new BigDecimal(this.remainderCash).subtract(new BigDecimal(realReturnMoney)).toString();
        else
            return "";
    }

}
