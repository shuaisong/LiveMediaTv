package com.tangmu.app.TengKuTV.bean;

import java.util.List;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class DubbingBean {

    /**
     * uw_url : 321321
     * uw_title : 2的title
     * uw_title_z : དགོད་བྲོའི་གཏམ་གླེང་།
     * uw_like_num : 0
     * uw_com_num : 0
     * u_nick_name : 小白兔
     * u_img : https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2757611226,1116495548&fm=15&gp=0.jpg
     * like_status : 0
     */
    private int uc_id;
    /**
     * uw_add_time : 2020-04-23 11:57:05
     * dv_id : 0
     * uw_type : 1
     * users : []
     * users_num : 0
     * is_no_dubbing : 1
     * is_collec_status : 1
     */

    private String uw_add_time;
    private int dv_id;
    private int uw_type;
    private int users_num;
    private int is_no_dubbing;
    private int is_collec_status;
    private List<?> users;

    public void setUc_id(int uc_id) {
        this.uc_id = uc_id;
    }

    private String uw_url;
    private String uw_title;
    private String uw_title_z;
    private int uw_like_num;
    private int uw_com_num;
    private String u_nick_name;
    private String u_img;
    private int like_status;
    /**
     * u_id : 9
     */

    private int u_id;

    public String getUw_url() {
        return uw_url;
    }

    public void setUw_url(String uw_url) {
        this.uw_url = uw_url;
    }

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

    public int getUw_like_num() {
        return uw_like_num;
    }

    public void setUw_like_num(int uw_like_num) {
        this.uw_like_num = uw_like_num;
    }

    public int getUw_com_num() {
        return uw_com_num;
    }

    public void setUw_com_num(int uw_com_num) {
        this.uw_com_num = uw_com_num;
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

    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public int getUc_id() {
        return uc_id;
    }

    public String getUw_add_time() {
        return uw_add_time;
    }

    public void setUw_add_time(String uw_add_time) {
        this.uw_add_time = uw_add_time;
    }

    public int getDv_id() {
        return dv_id;
    }

    public void setDv_id(int dv_id) {
        this.dv_id = dv_id;
    }

    public int getUw_type() {
        return uw_type;
    }

    public void setUw_type(int uw_type) {
        this.uw_type = uw_type;
    }

    public int getUsers_num() {
        return users_num;
    }

    public void setUsers_num(int users_num) {
        this.users_num = users_num;
    }

    public int getIs_no_dubbing() {
        return is_no_dubbing;
    }

    public void setIs_no_dubbing(int is_no_dubbing) {
        this.is_no_dubbing = is_no_dubbing;
    }

    public int getIs_collec_status() {
        return is_collec_status;
    }

    public void setIs_collec_status(int is_collec_status) {
        this.is_collec_status = is_collec_status;
    }

    public List<?> getUsers() {
        return users;
    }

    public void setUsers(List<?> users) {
        this.users = users;
    }
}
