package com.lianfu.lf_scancode_accounts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class ProtocolItem {

    private String devType;
    private String devName;
    private Integer money;
    private Integer number;
    private Integer totalMoney;

    @Override
    public String toString() {
        return "ProtocolItem{" +
                "devType=" + devType +
                ", devName='" + devName + '\'' +
                ", money=" + money +
                ", number=" + number +
                ", totalMoney=" + totalMoney +
                '}';
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getDevName() {
        return devName.split("_")[0];
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }

    public void ProtocolItem(String devType, String devName, Integer money, Integer number, Integer totalMoney) {
        this.devType = devType;
        this.devName = devName;
        this.money = money;
        this.number = number;
        this.totalMoney = totalMoney;
    }

    public void ProtocolItem() {
    }
}
