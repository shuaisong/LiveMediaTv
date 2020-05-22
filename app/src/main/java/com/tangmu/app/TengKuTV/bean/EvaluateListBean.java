package com.tangmu.app.TengKuTV.bean;

import java.util.List;

public class EvaluateListBean {

    /**
     * uw_id : 21
     * uw_img : images/202004/60b140de-7e19-11ea-87a3-00163e035ffb.jpg
     * uw_url : images/202004/6114a4da-7e19-11ea-b89e-00163e035ffb.mp4
     * u_nick_name : 杨 帅
     * u_img : images/202004/7e01f114-7e06-11ea-b89e-00163e035ffb.jpg
     * wc_add_time : 2020-04-15 10:52:25
     * wc_content : 我以前也是无所谓
     * wc_id : 13
     * reply : []
     */

    private int uw_id;
    private String uw_img;
    private String uw_url;
    private String u_nick_name;
    private String u_img;
    private String wc_add_time;
    private String wc_content;
    private int wc_id;
    private List<?> reply;

    public int getUw_id() {
        return uw_id;
    }

    public void setUw_id(int uw_id) {
        this.uw_id = uw_id;
    }

    public String getUw_img() {
        return uw_img;
    }

    public void setUw_img(String uw_img) {
        this.uw_img = uw_img;
    }

    public String getUw_url() {
        return uw_url;
    }

    public void setUw_url(String uw_url) {
        this.uw_url = uw_url;
    }

    public String getU_nick_name() {
        return u_nick_name;
    }

    public void setU_nick_name(String u_nick_name) {
        this.u_nick_name = u_nick_name;
    }

    public String getU_img() {
        return u_img;
    }

    public void setU_img(String u_img) {
        this.u_img = u_img;
    }

    public String getWc_add_time() {
        return wc_add_time;
    }

    public void setWc_add_time(String wc_add_time) {
        this.wc_add_time = wc_add_time;
    }

    public String getWc_content() {
        return wc_content;
    }

    public void setWc_content(String wc_content) {
        this.wc_content = wc_content;
    }

    public int getWc_id() {
        return wc_id;
    }

    public void setWc_id(int wc_id) {
        this.wc_id = wc_id;
    }

    public List<?> getReply() {
        return reply;
    }

    public void setReply(List<?> reply) {
        this.reply = reply;
    }
}
