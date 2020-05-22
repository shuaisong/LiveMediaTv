package com.tangmu.app.TengKuTV.bean;

public class BillBean {

    /**
     * o_pay_method : 2
     * o_recharge_gold : 68
     * o_pay_time : 2020-04-09 07:03:19
     * o_price : 0.01
     */

    private int o_pay_method;
    private int o_recharge_gold;
    private String o_pay_time;
    private String o_price;

    public int getO_pay_method() {
        return o_pay_method;
    }

    public void setO_pay_method(int o_pay_method) {
        this.o_pay_method = o_pay_method;
    }

    public int getO_recharge_gold() {
        return o_recharge_gold;
    }

    public void setO_recharge_gold(int o_recharge_gold) {
        this.o_recharge_gold = o_recharge_gold;
    }

    public String getO_pay_time() {
        return o_pay_time;
    }

    public void setO_pay_time(String o_pay_time) {
        this.o_pay_time = o_pay_time;
    }

    public String getO_price() {
        return o_price;
    }

    public void setO_price(String o_price) {
        this.o_price = o_price;
    }
}
