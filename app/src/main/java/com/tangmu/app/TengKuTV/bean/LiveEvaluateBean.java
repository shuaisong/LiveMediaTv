package com.tangmu.app.TengKuTV.bean;

public class LiveEvaluateBean {

    /**
     * lc_id : 7
     * lc_content : 我是小学生
     * u_id : 9
     * room_id : 338267
     * lc_like_num : 0
     * lc_add_time : 2020-04-02 11:53:23
     * like_status : 0
     * u_nick_name:
     * u_img:
     */

    private int lc_id;
    private String lc_content;
    private int u_id;
    private int room_id;
    private int lc_like_num;
    private String lc_add_time;
    private int like_status;
    private String u_img;

    public String getU_img() {
        return u_img;
    }

    public void setU_img(String u_img) {
        this.u_img = u_img;
    }

    public String getU_nick_name() {
        return u_nick_name;
    }

    public void setU_nick_name(String u_nick_name) {
        this.u_nick_name = u_nick_name;
    }

    private String u_nick_name;

    public int getLc_id() {
        return lc_id;
    }

    public void setLc_id(int lc_id) {
        this.lc_id = lc_id;
    }

    public String getLc_content() {
        return lc_content;
    }

    public void setLc_content(String lc_content) {
        this.lc_content = lc_content;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getLc_like_num() {
        return lc_like_num;
    }

    public void setLc_like_num(int lc_like_num) {
        this.lc_like_num = lc_like_num;
    }

    public String getLc_add_time() {
        return lc_add_time;
    }

    public void setLc_add_time(String lc_add_time) {
        this.lc_add_time = lc_add_time;
    }

    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
    }
}
