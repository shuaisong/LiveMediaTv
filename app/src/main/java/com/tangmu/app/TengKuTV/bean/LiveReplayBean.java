package com.tangmu.app.TengKuTV.bean;

public class LiveReplayBean {

    /**
     * l_img : images/2020/3/672432825906a51c1bacedaf6d2c5e1b.jpg
     * l_title : 直播2
     * l_title_z : 直播藏文2
     * lr_room_id : 655473
     * lr_id:4
     * lr_add_time : 2020-03-28 15:03:13
     * lr_url : http://1258540389.vod2.myqcloud.com/202f672fvodcq1258540389/1f8e0b885285890800536293181/f0.flv
     */

    private String l_img;
    private String l_title;
    private String l_title_z;
    private int lr_room_id;
    private String lr_add_time;
    private String lr_url;

    public int getLr_id() {
        return lr_id;
    }

    public void setLr_id(int lr_id) {
        this.lr_id = lr_id;
    }

    private int lr_id;

    public String getL_img() {
        return l_img;
    }

    public void setL_img(String l_img) {
        this.l_img = l_img;
    }

    public String getL_title() {
        return l_title;
    }

    public void setL_title(String l_title) {
        this.l_title = l_title;
    }

    public String getL_title_z() {
        return l_title_z;
    }

    public void setL_title_z(String l_title_z) {
        this.l_title_z = l_title_z;
    }

    public int getLr_room_id() {
        return lr_room_id;
    }

    public void setLr_room_id(int lr_room_id) {
        this.lr_room_id = lr_room_id;
    }

    public String getLr_add_time() {
        return lr_add_time;
    }

    public void setLr_add_time(String lr_add_time) {
        this.lr_add_time = lr_add_time;
    }

    public String getLr_url() {
        return lr_url;
    }

    public void setLr_url(String lr_url) {
        this.lr_url = lr_url;
    }
}
