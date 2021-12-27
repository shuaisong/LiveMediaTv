package com.tangmu.app.TengKuTV.bean;

/**
 * Created by lenovo on 2021/10/7.
 * auther:lenovo
 * Date：2021/10/7
 */

public class MiguLoginBean {
    private int u_id;
    private int tu_vip_status;
    private String tu_vip_expire;
    private int tu_is_month;//是否包月：1：否，2：是

    public int getTu_is_month() {
        return tu_is_month;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public int getTu_vip_status() {
        return tu_vip_status;
    }

    public void setTu_vip_status(int tu_vip_status) {
        this.tu_vip_status = tu_vip_status;
    }

    public String getTu_vip_expire() {
        return tu_vip_expire;
    }

    public void setTu_vip_expire(String tu_vip_expire) {
        this.tu_vip_expire = tu_vip_expire;
    }
}
