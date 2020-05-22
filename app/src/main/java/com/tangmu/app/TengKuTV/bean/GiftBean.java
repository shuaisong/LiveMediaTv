package com.tangmu.app.TengKuTV.bean;

import java.io.Serializable;

public class GiftBean implements Serializable {

    /**
     * lg_id : 9
     * lg_title : 烟花
     * lg_title_z : 烟花
     * lg_img : images/2020/3/b2c41f2a8a15ccdfeb3368073ed18da4.jpg
     * lg_price : 100
     * lg_status : 1
     * lg_add_time : 2020-01-17 14:03:52
     */

    private int lg_id;
    private String lg_title;
    private String lg_title_z;
    private String lg_img;
    private int lg_price;
    private int lg_status;
    private String lg_add_time;

    public int getLg_id() {
        return lg_id;
    }

    public void setLg_id(int lg_id) {
        this.lg_id = lg_id;
    }

    public String getLg_title() {
        return lg_title;
    }

    public void setLg_title(String lg_title) {
        this.lg_title = lg_title;
    }

    public String getLg_title_z() {
        return lg_title_z;
    }

    public void setLg_title_z(String lg_title_z) {
        this.lg_title_z = lg_title_z;
    }

    public String getLg_img() {
        return lg_img;
    }

    public void setLg_img(String lg_img) {
        this.lg_img = lg_img;
    }

    public int getLg_price() {
        return lg_price;
    }

    public void setLg_price(int lg_price) {
        this.lg_price = lg_price;
    }

    public int getLg_status() {
        return lg_status;
    }

    public void setLg_status(int lg_status) {
        this.lg_status = lg_status;
    }

    public String getLg_add_time() {
        return lg_add_time;
    }

    public void setLg_add_time(String lg_add_time) {
        this.lg_add_time = lg_add_time;
    }
}
