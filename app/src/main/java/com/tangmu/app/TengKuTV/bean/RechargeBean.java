package com.tangmu.app.TengKuTV.bean;

public class RechargeBean {

    /**
     * rm_id : 1
     * rm_mobile_type : 1
     * rm_money : 1.00
     * rm_gold : 68
     * rm_add_time : 2020-04-08 16:20:49
     */

    private int rm_id;
    private int rm_mobile_type;
    private String rm_money;
    private int rm_gold;
    private String rm_add_time;

    public int getRm_id() {
        return rm_id;
    }

    public void setRm_id(int rm_id) {
        this.rm_id = rm_id;
    }

    public int getRm_mobile_type() {
        return rm_mobile_type;
    }

    public void setRm_mobile_type(int rm_mobile_type) {
        this.rm_mobile_type = rm_mobile_type;
    }

    public String getRm_money() {
        return rm_money;
    }

    public void setRm_money(String rm_money) {
        this.rm_money = rm_money;
    }

    public int getRm_gold() {
        return rm_gold;
    }

    public void setRm_gold(int rm_gold) {
        this.rm_gold = rm_gold;
    }

    public String getRm_add_time() {
        return rm_add_time;
    }

    public void setRm_add_time(String rm_add_time) {
        this.rm_add_time = rm_add_time;
    }
}
