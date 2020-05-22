package com.tangmu.app.TengKuTV.bean;

/**
 * Created by lenovo on 2020/2/26.
 * auther:lenovo
 * Date：2020/2/26
 */
public class CollectBean {

    /**
     * uc_id : 24
     * uc_audio_id : 551394
     * u_id : 9
     * uc_type : 2
     * title : 测试
     * title_z : 测试
     * img : images/2020/3/1faf83df0a20072baabdb629eefcaf0b.jpg
     * des : 测试
     * des_z : 测试
     */

    private int uc_id;
    private int uc_audio_id;
    private int u_id;
    private int uc_type;
    private String title;
    private String title_z;
    private String img;
    private String des;
    private String des_z;
    /**
     * vm_type : 1
     */

    private int vm_type;
    /**
     * status : 1
     */

    private int status;//1直播2:回放
    /**
     * pull_url : http://play.longdawenhua.cn/live/551394.flv
     */

    private String pull_url;
    /**
     * vt_id_one : 1
     * vt_id_two : 9
     */

    private int vt_id_one;
    private int vt_id_two;

    public int getUc_id() {
        return uc_id;
    }

    public void setUc_id(int uc_id) {
        this.uc_id = uc_id;
    }

    public int getUc_audio_id() {
        return uc_audio_id;
    }

    public void setUc_audio_id(int uc_audio_id) {
        this.uc_audio_id = uc_audio_id;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public int getUc_type() {
        return uc_type;
    }

    public void setUc_type(int uc_type) {
        this.uc_type = uc_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_z() {
        return title_z;
    }

    public void setTitle_z(String title_z) {
        this.title_z = title_z;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDes_z() {
        return des_z;
    }

    public void setDes_z(String des_z) {
        this.des_z = des_z;
    }

    public int getVm_type() {
        return vm_type;
    }

    public void setVm_type(int vm_type) {
        this.vm_type = vm_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPull_url() {
        return pull_url;
    }

    public void setPull_url(String pull_url) {
        this.pull_url = pull_url;
    }

    public int getVt_id_one() {
        return vt_id_one;
    }

    public void setVt_id_one(int vt_id_one) {
        this.vt_id_one = vt_id_one;
    }

    public int getVt_id_two() {
        return vt_id_two;
    }

    public void setVt_id_two(int vt_id_two) {
        this.vt_id_two = vt_id_two;
    }
}
