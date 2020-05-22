package com.tencent.liteav.demo.play.bean;

import java.io.Serializable;

public class VideoBean implements Serializable {
    /**
     * v_url : http://1258540389.vod2.myqcloud.com/202f672fvodcq1258540389/2ac1e2a45285890800180859201/nVWb1vmYa18A.mp4
     * v_s_path :
     * v_g_path :
     * v_c_path :
     * v_l_path :
     * v_episode_num : 1
     * v_title : 第一集
     * v_title_z : 第一集藏文
     * v_fileid : 5285890799981075308
     */
    public int getV_is_pay() {
        return v_is_pay;
    }

    public void setV_is_pay(int v_is_pay) {
        this.v_is_pay = v_is_pay;
    }

    private int v_is_pay;//0；1单集付费
    private String v_url;
    private String v_s_path;
    private String v_g_path;
    private String v_c_path;
    private String v_l_path;
    private int v_episode_num;
    private String v_title;
    private String v_title_z;
    private String v_fileid;

    public String getV_url() {
        return v_url;
    }

    public void setV_url(String v_url) {
        this.v_url = v_url;
    }

    public String getV_s_path() {
        return v_s_path;
    }

    public void setV_s_path(String v_s_path) {
        this.v_s_path = v_s_path;
    }

    public String getV_g_path() {
        return v_g_path;
    }

    public void setV_g_path(String v_g_path) {
        this.v_g_path = v_g_path;
    }

    public String getV_c_path() {
        return v_c_path;
    }

    public void setV_c_path(String v_c_path) {
        this.v_c_path = v_c_path;
    }

    public String getV_l_path() {
        return v_l_path;
    }

    public void setV_l_path(String v_l_path) {
        this.v_l_path = v_l_path;
    }

    public int getV_episode_num() {
        return v_episode_num;
    }

    public void setV_episode_num(int v_episode_num) {
        this.v_episode_num = v_episode_num;
    }

    public String getV_title() {
        return v_title;
    }

    public void setV_title(String v_title) {
        this.v_title = v_title;
    }

    public String getV_title_z() {
        return v_title_z;
    }

    public void setV_title_z(String v_title_z) {
        this.v_title_z = v_title_z;
    }

    public String getV_fileid() {
        return v_fileid;
    }

    public void setV_fileid(String v_fileid) {
        this.v_fileid = v_fileid;
    }
}
