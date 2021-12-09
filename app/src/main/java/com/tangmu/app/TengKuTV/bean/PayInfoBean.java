package com.tangmu.app.TengKuTV.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovo on 2021/10/19.
 * auther:lenovo
 * Date：2021/10/19
 */

public class PayInfoBean {
    @SerializedName("Index")
    private String index;
    @SerializedName("IsMonthly")
    private String isMonthly;
    @SerializedName("CustomPeriod")
    private List<?> customPeriod;
    @SerializedName("BillTimes")
    private List<?> billTimes;
    @SerializedName("BillInterval")
    private List<?> billInterval;
    @SerializedName("CampaignId")
    private List<?> campaignId;
    @SerializedName("Fee")
    private String fee;
    @SerializedName("SpCode")
    private String spCode;
    @SerializedName("ServCode")
    private String servCode;
    @SerializedName("ChannelCode")
    private String channelCode;
    @SerializedName("CooperateCode")
    private String cooperateCode;
    @SerializedName("ProductCode")
    private String productCode;
    @SerializedName("ContentCode")
    private String contentCode;
    @SerializedName("PlatForm_Code")
    private List<?> platForm_Code;
    @SerializedName("Cpparam")
    private List<?> cpparam;
    @SerializedName("ReserveParam")
    private List<?> reserveParam;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIsMonthly() {
        return isMonthly;
    }

    public void setIsMonthly(String isMonthly) {
        this.isMonthly = isMonthly;
    }

    public List<?> getCustomPeriod() {
        return customPeriod;
    }

    public void setCustomPeriod(List<?> customPeriod) {
        this.customPeriod = customPeriod;
    }

    public List<?> getBillTimes() {
        return billTimes;
    }

    public void setBillTimes(List<?> billTimes) {
        this.billTimes = billTimes;
    }

    public List<?> getBillInterval() {
        return billInterval;
    }

    public void setBillInterval(List<?> billInterval) {
        this.billInterval = billInterval;
    }

    public List<?> getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(List<?> campaignId) {
        this.campaignId = campaignId;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getSpCode() {
        return spCode;
    }

    public void setSpCode(String spCode) {
        this.spCode = spCode;
    }

    public String getServCode() {
        return servCode;
    }

    public void setServCode(String servCode) {
        this.servCode = servCode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getCooperateCode() {
        return cooperateCode;
    }

    public void setCooperateCode(String cooperateCode) {
        this.cooperateCode = cooperateCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getContentCode() {
        return contentCode;
    }

    public void setContentCode(String contentCode) {
        this.contentCode = contentCode;
    }

    public List<?> getPlatForm_Code() {
        return platForm_Code;
    }

    public void setPlatForm_Code(List<?> platForm_Code) {
        this.platForm_Code = platForm_Code;
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
