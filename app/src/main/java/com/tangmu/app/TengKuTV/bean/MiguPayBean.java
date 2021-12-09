package com.tangmu.app.TengKuTV.bean;

/**
 * Created by lenovo on 2021/10/18.
 * auther:lenovo
 * Date：2021/10/18
 */

public class MiguPayBean {

    private String result;
    private String resultDesc;
    private String externalSeqNum;
    private String qrCode;
    private String qrCodeImgUrl;
    private String qrCodeImg;
    private String payParam;
    private SdkmesBean sdkmes;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getExternalSeqNum() {
        return externalSeqNum;
    }

    public void setExternalSeqNum(String externalSeqNum) {
        this.externalSeqNum = externalSeqNum;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCodeImgUrl() {
        return qrCodeImgUrl;
    }

    public void setQrCodeImgUrl(String qrCodeImgUrl) {
        this.qrCodeImgUrl = qrCodeImgUrl;
    }

    public String getQrCodeImg() {
        return qrCodeImg;
    }

    public void setQrCodeImg(String qrCodeImg) {
        this.qrCodeImg = qrCodeImg;
    }

    public String getPayParam() {
        return payParam;
    }

    public void setPayParam(String payParam) {
        this.payParam = payParam;
    }


    public SdkmesBean getSdkmes() {
        return sdkmes;
    }

    public void setSdkmes(SdkmesBean sdkmes) {
        this.sdkmes = sdkmes;
    }
}
