package com.tangmu.app.TengKuTV.bean;

import java.io.Serializable;

public class TitleBean implements Serializable {

    /**
     * s_id : 1
     * s_title : 今日推荐
     * s_title_z : 今日推荐
     */

    private int s_id;
    private String s_title;
    private String s_title_z;

    public boolean isMoreType() {
        return isMoreType;
    }

    private boolean isMoreType;


    public String getS_title() {
        return s_title;
    }

    public void setS_title(String s_title) {
        this.s_title = s_title;
    }

    public String getS_title_z() {
        return s_title_z;
    }

    public void setS_title_z(String s_title_z) {
        this.s_title_z = s_title_z;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public void setIsMoreType(boolean isMoreType) {
        this.isMoreType = isMoreType;
    }
}
