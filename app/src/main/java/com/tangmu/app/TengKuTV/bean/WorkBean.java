package com.tangmu.app.TengKuTV.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2020/2/24.
 * auther:lenovo
 * Date：2020/2/24
 */
public class WorkBean implements Serializable {

    /**
     * uw_title : 磨破融融泄泄外婆哥们
     * uw_title_z : 磨破融融泄泄外婆哥们
     * uw_id : 21
     * uw_img : images/202004/60b140de-7e19-11ea-87a3-00163e035ffb.jpg
     * uw_check_status : 1
     * uw_time : 200520
     * uw_add_time : 46秒前
     */

    private String uw_title;
    private String uw_title_z;
    private int uw_id;
    private String uw_img;
    private int uw_check_status;
    private int uw_time;
    private String uw_add_time;
    private int uw_type;

    public int getUw_type() {
        return uw_type;
    }

    public void setUw_type(int uw_type) {
        this.uw_type = uw_type;
    }

    public void setDv_id(int dv_id) {
        this.dv_id = dv_id;
    }

    public int getDv_id() {
        return dv_id;
    }

    private int dv_id;
    public String getUw_title() {
        return uw_title;
    }

    public void setUw_title(String uw_title) {
        this.uw_title = uw_title;
    }

    public String getUw_title_z() {
        return uw_title_z;
    }

    public void setUw_title_z(String uw_title_z) {
        this.uw_title_z = uw_title_z;
    }

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

    public int getUw_check_status() {
        return uw_check_status;
    }

    public void setUw_check_status(int uw_check_status) {
        this.uw_check_status = uw_check_status;
    }

    public int getUw_time() {
        return uw_time;
    }

    public void setUw_time(int uw_time) {
        this.uw_time = uw_time;
    }

    public String getUw_add_time() {
        return uw_add_time;
    }

    public void setUw_add_time(String uw_add_time) {
        this.uw_add_time = uw_add_time;
    }
}
