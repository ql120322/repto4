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
public class CashoutOrder {
    private Integer cashoutOrderId; // 序号
    private String cashoutOrderNo; // 订单号
    private String cashoutDate; // 订单日期
    private String cashoutMoney; // 提现金额
    private String cashoutOpenId; // 提现用户openid
    private String wechatUserName; // 提现用户名
    private String cashoutStatus; // 订单金额 1-已完成 2-待付款
    //-----------------------------------------------------------
    private String cashoutnickName;//微信名
    private String cashoutmerid;// mer_id
    private String cashoutmode; //返现模式
    private String cashoutoldbalance;//提现前余额
    private String cashoutmeridmoney; //merid 对应的余额
    private String cashoutnewmoney;
    private List<Shop> shop;

    public String getCashoutnewmoney() {
        return cashoutnewmoney;
    }

    public void setCashoutnewmoney(String cashout_newmoney) {
        this.cashoutnewmoney = cashout_newmoney;
    }

    public List<Shop> getShop() {
        return shop;
    }

    public void setShop(List<Shop> shop) {
        this.shop = shop;
    }

    public String getCashoutmeridmoney() {
        return cashoutmeridmoney;
    }

    public void setCashoutmeridmoney(String cashoutmeridmoney) {
        this.cashoutmeridmoney = cashoutmeridmoney;
    }

    public String getCashoutnickName() {
        return cashoutnickName;
    }

    public void setCashoutnickName(String cashoutnickName) {
        this.cashoutnickName = cashoutnickName;
    }

    public String getCashoutmerid() {
        return cashoutmerid;
    }

    public void setCashoutmerid(String cashoutmerid) {
        this.cashoutmerid = cashoutmerid;
    }

    public String getCashoutmode() {
        return cashoutmode;
    }

    public void setCashoutmode(String cashoutmode) {
        this.cashoutmode = cashoutmode;
    }

    public String getCashoutoldbalance() {
        return cashoutoldbalance;
    }

    public void setCashoutoldbalance(String cashoutoldbalance) {
        this.cashoutoldbalance = cashoutoldbalance;
    }

    public void setCashoutOpenId(String cashoutOpenId) {
        this.cashoutOpenId = cashoutOpenId;
    }

    public void setCashoutMoney(String cashoutMoney) {
        this.cashoutMoney = cashoutMoney;
    }

    public String getCashoutMoney() {
        return cashoutMoney;
    }

    public String getCashoutOpenId() {
        return cashoutOpenId;
    }

    public Integer getCashoutOrderId() {
        return cashoutOrderId;
    }

    public String getCashoutOrderNo() {
        return cashoutOrderNo;
    }

    public String getCashoutDate() {
        return cashoutDate;
    }

    public String getWechatUserName() {
        return wechatUserName;
    }

    public String getCashoutStatus() {
        return cashoutStatus;
    }

    public void setCashoutOrderId(Integer cashoutOrderId) {
        this.cashoutOrderId = cashoutOrderId;
    }

    public void setCashoutOrderNo(String cashoutOrderNo) {
        this.cashoutOrderNo = cashoutOrderNo;
    }

    public void setCashoutDate(String cashoutDate) {
        this.cashoutDate = cashoutDate;
    }

    public void setWechatUserName(String wechatUserName) {
        this.wechatUserName = wechatUserName;
    }

    public void setCashoutStatus(String cashoutStatus) {
        this.cashoutStatus = cashoutStatus;
    }
}
