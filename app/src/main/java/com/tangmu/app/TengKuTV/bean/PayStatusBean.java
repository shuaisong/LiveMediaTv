package com.tangmu.app.TengKuTV.bean;

/**
 * Created by lenovo on 2021/10/20.
 * auther:lenovo
 * Date：2021/10/20
 */

public class PayStatusBean {

    private String result;
    private String resultDesc;
    private String payResult;
    private String successPayMent;
    private String externalSeqNum;

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

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }

    public String getSuccessPayMent() {
        return successPayMent;
    }

    public void setSuccessPayMent(String successPayMent) {
        this.successPayMent = successPayMent;
    }

    public String getExternalSeqNum() {
        return externalSeqNum;
    }

    public void setExternalSeqNum(String externalSeqNum) {
        this.externalSeqNum = externalSeqNum;
    }
}
