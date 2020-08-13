package com.lianfu.lf_scancode_accounts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor    //2020-6-23 齐亮 创建 后台管理实体类
public class Lf_gl {

    private  int id;
    private  String filename;
    private  String filepath;
    private  String lfname;
    private  String stardate;
    private  String endDate;
    private  String download;
    private  String contractname;
    private  String inputfilepath;
    private  String state;
    private  String email;
    private  String phone;
    private  String Thecontact;
    private  String  Electronicdelivery;

    public String getElectronicdelivery() {
        return Electronicdelivery;
    }

    public void setElectronicdelivery(String electronicdelivery) {
        Electronicdelivery = electronicdelivery;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getThecontact() {
        return Thecontact;
    }

    public void setThecontact(String thecontact) {
        Thecontact = thecontact;
    }

    public int getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getLfname() {
        return lfname;
    }

    public String getStardate() {
        return stardate;
    }

    public String getDownload() {
        return download;
    }

    public String getContractname() {
        return contractname;
    }

    public String getInputfilepath() {
        return inputfilepath;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setLfname(String lfname) {
        this.lfname = lfname;
    }

    public void setStardate(String stardate) {
        this.stardate = stardate;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public void setContractname(String contractname) {
        this.contractname = contractname;
    }

    public void setInputfilepath(String inputfilepath) {
        this.inputfilepath = inputfilepath;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
