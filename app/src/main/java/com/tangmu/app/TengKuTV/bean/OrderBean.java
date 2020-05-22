package com.tangmu.app.TengKuTV.bean;

public class OrderBean {
    /**
     * order_no : 111577152884232667435
     * price : 112.00
     */

    private String order_no;
    private String price;

    public int getCn_id() {
        return cn_id;
    }

    public void setCn_id(int cn_id) {
        this.cn_id = cn_id;
    }

    private int cn_id;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
