package com.tangmu.app.TengKuTV.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 2021/10/19.
 * auther:lenovo
 * Date：2021/10/19
 */

public class PayInfosBean {
    @SerializedName("PayInfo")
    private PayInfoBean payInfo;

    public PayInfoBean getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfoBean payInfo) {
        this.payInfo = payInfo;
    }
}
