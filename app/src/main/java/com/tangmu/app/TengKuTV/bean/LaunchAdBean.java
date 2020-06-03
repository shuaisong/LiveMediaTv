package com.tangmu.app.TengKuTV.bean;

import java.io.Serializable;

public class LaunchAdBean implements Serializable {

    /**
     * pa_id : 1
     * pa_type : 1
     * pa_url : images/2020/4/dfd2644802de81ce73a5f592ae5a2179.jpg
     * pa_status : 1
     * pa_add_time : 2020-04-02 17:10:30
     * pa_fileid : null
     */

    private int pa_id;
    private int pa_type;
    private String pa_url;
    private int pa_status;
    private String pa_add_time;
    private String pa_fileid;

    public int getPa_id() {
        return pa_id;
    }

    public void setPa_id(int pa_id) {
        this.pa_id = pa_id;
    }

    public int getPa_type() {
        return pa_type;
    }

    public void setPa_type(int pa_type) {
        this.pa_type = pa_type;
    }

    public String getPa_url() {
        return pa_url;
    }

    public void setPa_url(String pa_url) {
        this.pa_url = pa_url;
    }

    public int getPa_status() {
        return pa_status;
    }

    public void setPa_status(int pa_status) {
        this.pa_status = pa_status;
    }

    public String getPa_add_time() {
        return pa_add_time;
    }

    public void setPa_add_time(String pa_add_time) {
        this.pa_add_time = pa_add_time;
    }

    public String getPa_fileid() {
        return pa_fileid;
    }

    public void setPa_fileid(String pa_fileid) {
        this.pa_fileid = pa_fileid;
    }
}
