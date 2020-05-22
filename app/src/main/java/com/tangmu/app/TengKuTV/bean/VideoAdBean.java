package com.tangmu.app.TengKuTV.bean;

public class VideoAdBean {

    /**
     * ta_id : 2
     * ta_img : images/2020/5/03ccec58cc88a1e70e13b36692e852bf.jpg
     * ta_type : 1
     * ta_url : 2
     */

    private int ta_id;
    private String ta_img;
    private int ta_type;//1 视频 2 配音 3 网页
    private String ta_url;
    private int vm_type;
    private int vt_id_one;

    public int getVt_id_one() {
        return vt_id_one;
    }

    public void setVt_id_one(int vt_id_one) {
        this.vt_id_one = vt_id_one;
    }

    public int getVm_type() {
        return vm_type;
    }

    public void setVm_type(int vm_type) {
        this.vm_type = vm_type;
    }

    public int getTa_id() {
        return ta_id;
    }

    public void setTa_id(int ta_id) {
        this.ta_id = ta_id;
    }

    public String getTa_img() {
        return ta_img;
    }

    public void setTa_img(String ta_img) {
        this.ta_img = ta_img;
    }

    public int getTa_type() {
        return ta_type;
    }

    public void setTa_type(int ta_type) {
        this.ta_type = ta_type;
    }

    public String getTa_url() {
        return ta_url;
    }

    public void setTa_url(String ta_url) {
        this.ta_url = ta_url;
    }
}
