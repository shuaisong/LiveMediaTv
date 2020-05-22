package com.tangmu.app.TengKuTV.bean;

import com.google.gson.annotations.SerializedName;

public class WxBean {

    /**
     * appid : wx76653ab62ca2a0d4
     * prepayid : wx23112911297210d6f094ec141280010900
     * partnerid : 1562925421
     * package : Sign=WXPay
     * timestamp : 1577071751
     * noncestr : d989dc063b7b2cca0eba08b91763d7da
     * sign : 38969C4ED043D13B5071EC03E0039621
     * packages : Sign=WXPay
     * code : 1
     */

    private String appid;
    private String prepayid;
    private String partnerid;
    @SerializedName("package")
    private String packageX;
    private int timestamp;
    private String noncestr;
    private String sign;
    private String packages;
    private int code;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
