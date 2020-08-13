package com.lianfu.lf_scancode_accounts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



public class Protocol {

    private Integer ruanjianfei = 0;
    private Integer yingjianfei = 0;
    private Integer zongjine = 0;
    private String cooperationPolicy;//合作政策
    private String depositRefund;//押金返还策略
    private String taxId;//税号
    private String companyName; // 公司名
    private String phone;//电话
    private String bank;//开户行
    private String accountNumber;//账号
    private String address;//地址
    private String startDate;
    private String endDate;
    private String Atime; //签订时间
    private String Asite; //签订地点
    private String AddressOfService; //送达地址
    private String Thecontact; //联系人
    private String Contactphonenumber; // 联系电话
    private String Electronicdelivery; // 是否电子送达
    private String EMail;//电子邮箱
    private String wx_zhi;
    private String zfb;

    public String getWx_zhi() {
        return wx_zhi;
    }

    public void setWx_zhi(String wx_zhi) {
        this.wx_zhi = wx_zhi;
    }

    public String getZfb() {
        return zfb;
    }

    public void setZfb(String zfb) {
        this.zfb = zfb;
    }

    private List<ProtocolItem> protocolItems;




    public Integer getRuanjianfei() {
        return ruanjianfei;
    }

    public void setRuanjianfei(Integer ruanjianfei) {
        this.ruanjianfei = ruanjianfei;
    }

    public Integer getYingjianfei() {
        return yingjianfei;
    }

    public void setYingjianfei(Integer yingjianfei) {
        this.yingjianfei = yingjianfei;
    }

    public Integer getZongjine() {
        return yingjianfei + ruanjianfei;
    }

    public void setZongjine(Integer zongjine) {
        this.zongjine = zongjine;
    }

    public String getCooperationPolicy() {
        return cooperationPolicy;
    }

    public void setCooperationPolicy(String cooperationPolicy) {
        this.cooperationPolicy = cooperationPolicy;
    }

    public String getDepositRefund() {
        return depositRefund;
    }

    public void setDepositRefund(String depositRefund) {
        this.depositRefund = depositRefund;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<ProtocolItem> getProtocolItems() {
        return protocolItems;
    }

    public void setProtocolItems(List<ProtocolItem> protocolItems) {
        this.protocolItems = protocolItems;
    }

    public String getAtime() {
        return Atime;
    }

    public String getAsite() {
        return Asite;
    }

    public String getAddressOfService() {
        return AddressOfService;
    }

    public String getThecontact() {
        return Thecontact;
    }

    public String getContactphonenumber() {
        return Contactphonenumber;
    }

    public String getElectronicdelivery() {
        return Electronicdelivery;
    }

    public String getEMail() {
        return EMail;
    }


    public void setAtime(String atime) {
        Atime = atime;
    }

    public void setAsite(String asite) {
        Asite = asite;
    }

    public void setAddressOfService(String addressOfService) {
        AddressOfService = addressOfService;
    }

    public void setThecontact(String thecontact) {
        Thecontact = thecontact;
    }

    public void setContactphonenumber(String contactphonenumber) {
        Contactphonenumber = contactphonenumber;
    }

    public void setElectronicdelivery(String electronicdelivery) {
        Electronicdelivery = electronicdelivery;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    @Override
    public String toString() {
        return "Protocol{" +
                "ruanjianfei=" + ruanjianfei +
                ", yingjianfei=" + yingjianfei +
                ", zongjine=" + zongjine +
                ", cooperationPolicy='" + cooperationPolicy + '\'' +
                ", depositRefund='" + depositRefund + '\'' +
                ", taxId='" + taxId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", phone='" + phone + '\'' +
                ", bank='" + bank + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", address='" + address + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", Atime='" + Atime + '\'' +
                ", Asite='" + Asite + '\'' +
                ", AddressOfService='" + AddressOfService + '\'' +
                ", Thecontact='" + Thecontact + '\'' +
                ", Contactphonenumber='" + Contactphonenumber + '\'' +
                ", Electronicdelivery='" + Electronicdelivery + '\'' +
                ", EMail='" + EMail + '\'' +
                ", protocolItems=" + protocolItems +
                '}';
    }


    public Protocol(Integer ruanjianfei, Integer yingjianfei, Integer zongjine, String cooperationPolicy, String depositRefund, String taxId, String companyName, String phone, String bank, String accountNumber, String address, String startDate, String endDate, String atime, String asite, String addressOfService, String thecontact, String contactphonenumber, String electronicdelivery, String EMail, List<ProtocolItem> protocolItems) {
        this.ruanjianfei = ruanjianfei;
        this.yingjianfei = yingjianfei;
        this.zongjine = zongjine;
        this.cooperationPolicy = cooperationPolicy;
        this.depositRefund = depositRefund;
        this.taxId = taxId;
        this.companyName = companyName;
        this.phone = phone;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        Atime = atime;
        Asite = asite;
        AddressOfService = addressOfService;
        Thecontact = thecontact;
        Contactphonenumber = contactphonenumber;
        Electronicdelivery = electronicdelivery;
        this.EMail = EMail;
        this.protocolItems = protocolItems;
    }

    public Protocol() {
    }
}
