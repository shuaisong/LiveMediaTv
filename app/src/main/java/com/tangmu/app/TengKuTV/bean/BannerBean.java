package com.tangmu.app.TengKuTV.bean;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class BannerBean {

    public int getB_live_type() {
        return b_live_type;
    }

    public void setB_live_type(int b_live_type) {
        this.b_live_type = b_live_type;
    }

    public String getPull_url() {
        return pull_url;
    }

    public void setPull_url(String pull_url) {
        this.pull_url = pull_url;
    }

    /**
     * b_id : 4
     * b_img : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585048725126&di=f6b0c5402644f236017d7c6a2b8711fa&imgtype=0&src=http%3A%2F%2Fhahae.ewoka.com%2Fuploads%2Fallimg%2F150609%2F724255_150609115403_5.jpg
     * b_type : 1
     * b_sorts : 1
     * b_title : 首页轮播图
     * b_title_z : 首页轮播图
     * b_status : 1
     * b_add_time : 2020-03-24 16:23:35
     * b_type2 : 1
     * b_url : 1
     * vt_id_one : 1
     */
    private int b_live_type;//1直播 2预约
    private String pull_url;//推流地址
    private int b_id;
    private String b_img;
    private int b_type;
    private int b_sorts;
    private String b_title;
    private String b_title_z;
    private int b_status;
    private String b_add_time;
    private int b_type2;
    private String b_url;
    private int vt_id_one;

    public int getB_id() {
        return b_id;
    }

    public void setB_id(int b_id) {
        this.b_id = b_id;
    }

    public String getB_img() {
        return b_img;
    }

    public void setB_img(String b_img) {
        this.b_img = b_img;
    }

    public int getB_type() {
        return b_type;
    }

    public void setB_type(int b_type) {
        this.b_type = b_type;
    }

    public int getB_sorts() {
        return b_sorts;
    }

    public void setB_sorts(int b_sorts) {
        this.b_sorts = b_sorts;
    }

    public String getB_title() {
        return b_title;
    }

    public void setB_title(String b_title) {
        this.b_title = b_title;
    }

    public String getB_title_z() {
        return b_title_z;
    }

    public void setB_title_z(String b_title_z) {
        this.b_title_z = b_title_z;
    }

    public int getB_status() {
        return b_status;
    }

    public void setB_status(int b_status) {
        this.b_status = b_status;
    }

    public String getB_add_time() {
        return b_add_time;
    }

    public void setB_add_time(String b_add_time) {
        this.b_add_time = b_add_time;
    }

    public int getB_type2() {
        return b_type2;
    }

    public void setB_type2(int b_type2) {
        this.b_type2 = b_type2;
    }

    public String getB_url() {
        return b_url;
    }

    public void setB_url(String b_url) {
        this.b_url = b_url;
    }

    public int getVt_id_one() {
        return vt_id_one;
    }

    public void setVt_id_one(int vt_id_one) {
        this.vt_id_one = vt_id_one;
    }
}
