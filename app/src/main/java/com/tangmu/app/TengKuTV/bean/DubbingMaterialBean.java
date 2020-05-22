package com.tangmu.app.TengKuTV.bean;

import java.util.List;

public class DubbingMaterialBean {

    /**
     * dv_id : 2
     * dl_id : 1
     * dv_img : images/2020/3/e8736ba8d65cf975a3bc9947d1674421.jpg
     * dv_title : 测试2
     * dv_title_z : 都是
     * dv_url_video : http://tv.longdawenhua.cn/vodel_2019082711210221751.mp4
     * dv_content : 打
     * dv_content_z : 大萨达
     * dv_num : 22
     * dv_status : 1
     * dv_bg_video : 大萨达撒
     * dv_add_time : 2020-01-15 10:59:40
     * user : [{"u_id":2,"u_nick_name":"小白兔","u_img":"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2757611226,1116495548&fm=15&gp=0.jpg","uw_num":16661,"uw_url":"321321","uw_id":2},{"u_id":10,"u_nick_name":"176****7071","u_img":"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2757611226,1116495548&fm=15&gp=0.jpg","uw_num":10000,"uw_url":"321321","uw_id":1},{"u_id":2,"u_nick_name":"小白兔","u_img":"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2757611226,1116495548&fm=15&gp=0.jpg","uw_num":344,"uw_url":"321321","uw_id":12},{"u_id":2,"u_nick_name":"小白兔","u_img":"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2757611226,1116495548&fm=15&gp=0.jpg","uw_num":23,"uw_url":"321321","uw_id":4},{"u_id":3,"u_nick_name":"zzzzz","u_img":"http://tk-1253334841-1258540389.cos.ap-beijing.myqcloud.com/ACF6696A-6218-4E74-9EC3-94B7662B4250","uw_num":11,"uw_url":"321321","uw_id":3}]
     */

    private int dv_id;
    private String dl_id;
    private String dv_img;
    private String dv_title;
    private String dv_title_z;
    private String dv_url_video;
    private String dv_content;
    private String dv_content_z;
    private int dv_num;
    private int dv_status;
    private String dv_bg_video;
    private String dv_add_time;
    private List<UserBean> user;

    public int getDv_id() {
        return dv_id;
    }

    public void setDv_id(int dv_id) {
        this.dv_id = dv_id;
    }

    public String getDl_id() {
        return dl_id;
    }

    public void setDl_id(String dl_id) {
        this.dl_id = dl_id;
    }

    public String getDv_img() {
        return dv_img;
    }

    public void setDv_img(String dv_img) {
        this.dv_img = dv_img;
    }

    public String getDv_title() {
        return dv_title;
    }

    public void setDv_title(String dv_title) {
        this.dv_title = dv_title;
    }

    public String getDv_title_z() {
        return dv_title_z;
    }

    public void setDv_title_z(String dv_title_z) {
        this.dv_title_z = dv_title_z;
    }

    public String getDv_url_video() {
        return dv_url_video;
    }

    public void setDv_url_video(String dv_url_video) {
        this.dv_url_video = dv_url_video;
    }

    public String getDv_content() {
        return dv_content;
    }

    public void setDv_content(String dv_content) {
        this.dv_content = dv_content;
    }

    public String getDv_content_z() {
        return dv_content_z;
    }

    public void setDv_content_z(String dv_content_z) {
        this.dv_content_z = dv_content_z;
    }

    public int getDv_num() {
        return dv_num;
    }

    public void setDv_num(int dv_num) {
        this.dv_num = dv_num;
    }

    public int getDv_status() {
        return dv_status;
    }

    public void setDv_status(int dv_status) {
        this.dv_status = dv_status;
    }

    public String getDv_bg_video() {
        return dv_bg_video;
    }

    public void setDv_bg_video(String dv_bg_video) {
        this.dv_bg_video = dv_bg_video;
    }

    public String getDv_add_time() {
        return dv_add_time;
    }

    public void setDv_add_time(String dv_add_time) {
        this.dv_add_time = dv_add_time;
    }

    public List<UserBean> getUser() {
        return user;
    }

    public void setUser(List<UserBean> user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * u_id : 2
         * u_nick_name : 小白兔
         * u_img : https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2757611226,1116495548&fm=15&gp=0.jpg
         * uw_num : 16661
         * uw_url : 321321
         * uw_id : 2
         */

        private int u_id;
        private String u_nick_name;
        private String u_img;
        private int uw_num;
        private String uw_url;
        private int uw_id;

        public int getU_id() {
            return u_id;
        }

        public void setU_id(int u_id) {
            this.u_id = u_id;
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

        public int getUw_num() {
            return uw_num;
        }

        public void setUw_num(int uw_num) {
            this.uw_num = uw_num;
        }

        public String getUw_url() {
            return uw_url;
        }

        public void setUw_url(String uw_url) {
            this.uw_url = uw_url;
        }

        public int getUw_id() {
            return uw_id;
        }

        public void setUw_id(int uw_id) {
            this.uw_id = uw_id;
        }
    }
}
