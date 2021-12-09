package com.tangmu.app.TengKuTV.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2021/10/16.
 * auther:lenovo
 * Date：2021/10/16
 */

public class AttributesBeanXXX implements Serializable {
    private String productCode;
    private String productInfo;
    private String orderContentId;
    private String productPrice;
    private String unit;//单位（1.天、2.连续包月、3.单月、4.年、5.季、6.固定时长、7.按次）
    private String cycle;
    private String validstarttime;
    private String validendtime;
    private String price;
    private String displayPrority;
    private String paymentType;
    private String isSalesStrategy;
    private String combineProduct;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public String getOrderContentId() {
        return orderContentId;
    }

    public void setOrderContentId(String orderContentId) {
        this.orderContentId = orderContentId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getValidstarttime() {
        return validstarttime;
    }

    public void setValidstarttime(String validstarttime) {
        this.validstarttime = validstarttime;
    }

    public String getValidendtime() {
        return validendtime;
    }

    public void setValidendtime(String validendtime) {
        this.validendtime = validendtime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDisplayPrority() {
        return displayPrority;
    }

    public void setDisplayPrority(String displayPrority) {
        this.displayPrority = displayPrority;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getIsSalesStrategy() {
        return isSalesStrategy;
    }

    public void setIsSalesStrategy(String isSalesStrategy) {
        this.isSalesStrategy = isSalesStrategy;
    }

    public String getCombineProduct() {
        return combineProduct;
    }

    public void setCombineProduct(String combineProduct) {
        this.combineProduct = combineProduct;
    }
}
