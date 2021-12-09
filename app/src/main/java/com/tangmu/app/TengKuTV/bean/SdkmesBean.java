package com.tangmu.app.TengKuTV.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovo on 2021/10/19.
 * auther:lenovo
 * Date：2021/10/19
 */

public class SdkmesBean {
    @SerializedName("Ctype")
    private String ctype;
    @SerializedName("OrderId")
    private String orderId;
    @SerializedName("PayNum")
    private String payNum;
    @SerializedName("BizType")
    private String bizType;
    @SerializedName("StbID")
    private String stbID;
    @SerializedName("ChargePolicy")
    private String chargePolicy;
    @SerializedName("CustomBizExpiryDate")
    private List<?> customBizExpiryDate;
    @SerializedName("OperCode")
    private String operCode;
    @SerializedName("PayInfos")
    private PayInfosBean payInfos;
    @SerializedName("Cpparam")
    private List<?> cpparam;
    @SerializedName("ReserveParam")
    private List<?> reserveParam;

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayNum() {
        return payNum;
    }

    public void setPayNum(String payNum) {
        this.payNum = payNum;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getStbID() {
        return stbID;
    }

    public void setStbID(String stbID) {
        this.stbID = stbID;
    }

    public String getChargePolicy() {
        return chargePolicy;
    }

    public void setChargePolicy(String chargePolicy) {
        this.chargePolicy = chargePolicy;
    }

    public List<?> getCustomBizExpiryDate() {
        return customBizExpiryDate;
    }

    public void setCustomBizExpiryDate(List<?> customBizExpiryDate) {
        this.customBizExpiryDate = customBizExpiryDate;
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(String operCode) {
        this.operCode = operCode;
    }

    public PayInfosBean getPayInfos() {
        return payInfos;
    }

    public void setPayInfos(PayInfosBean payInfos) {
        this.payInfos = payInfos;
    }

    public List<?> getCpparam() {
        return cpparam;
    }

    public void setCpparam(List<?> cpparam) {
        this.cpparam = cpparam;
    }

    public List<?> getReserveParam() {
        return reserveParam;
    }

    public void setReserveParam(List<?> reserveParam) {
        this.reserveParam = reserveParam;
    }
}
