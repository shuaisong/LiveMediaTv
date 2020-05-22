package com.tangmu.app.TengKuTV.bean;

public class WxAccessToken {

    /**
     * errcode : 40013
     * errmsg : invalid appid
     * "expires_in":7200
     * "access_token":"ACCESS_TOKEN"
     */
    private int expires_in;
    private int errcode;
    private String errmsg;
    private String access_token;

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
