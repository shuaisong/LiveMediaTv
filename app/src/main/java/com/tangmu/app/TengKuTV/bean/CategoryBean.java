package com.tangmu.app.TengKuTV.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class CategoryBean implements Serializable {

    /**
     * vt_id : 1
     * vt_pid : 0
     * vt_title : 电视剧
     * vt_title_z : 电视剧
     * vt_icon_img :
     * vt_vip_status : 0
     * second : [{"vt_id":3,"vt_pid":1,"vt_title":"情感电视剧","vt_title_z":"情感电视剧","vt_icon_img":"","vt_vip_status":0,"second":[]},{"vt_id":4,"vt_pid":1,"vt_title":"犯罪电视剧","vt_title_z":"犯罪电视剧","vt_icon_img":"","vt_vip_status":0,"second":[]}]
     */

    private int vt_id;
    private int vt_pid;
    private String vt_title;
    private String vt_title_z;
    private String vt_icon_img;
    private int vt_vip_status;
    private List<SecondBean> second;

    public int getVt_id() {
        return vt_id;
    }

    public void setVt_id(int vt_id) {
        this.vt_id = vt_id;
    }

    public int getVt_pid() {
        return vt_pid;
    }

    public void setVt_pid(int vt_pid) {
        this.vt_pid = vt_pid;
    }

    public String getVt_title() {
        return vt_title;
    }

    public void setVt_title(String vt_title) {
        this.vt_title = vt_title;
    }

    public String getVt_title_z() {
        return vt_title_z;
    }

    public void setVt_title_z(String vt_title_z) {
        this.vt_title_z = vt_title_z;
    }

    public String getVt_icon_img() {
        return vt_icon_img;
    }

    public void setVt_icon_img(String vt_icon_img) {
        this.vt_icon_img = vt_icon_img;
    }

    public int getVt_vip_status() {
        return vt_vip_status;
    }

    public void setVt_vip_status(int vt_vip_status) {
        this.vt_vip_status = vt_vip_status;
    }

    public List<SecondBean> getSecond() {
        return second;
    }

    public void setSecond(List<SecondBean> second) {
        this.second = second;
    }

    public static class SecondBean implements Serializable {
        /**
         * vt_id : 3
         * vt_pid : 1
         * vt_title : 情感电视剧
         * vt_title_z : 情感电视剧
         * vt_icon_img :
         * vt_vip_status : 0
         * second : []
         */

        private int vt_id;
        private int vt_pid;
        private String vt_title;
        private String vt_title_z;
        private String vt_icon_img;
        private int vt_vip_status;
        private List<?> second;

        public int getVt_id() {
            return vt_id;
        }

        public void setVt_id(int vt_id) {
            this.vt_id = vt_id;
        }

        public int getVt_pid() {
            return vt_pid;
        }

        public void setVt_pid(int vt_pid) {
            this.vt_pid = vt_pid;
        }

        public String getVt_title() {
            return vt_title;
        }

        public void setVt_title(String vt_title) {
            this.vt_title = vt_title;
        }

        public String getVt_title_z() {
            return vt_title_z;
        }

        public void setVt_title_z(String vt_title_z) {
            this.vt_title_z = vt_title_z;
        }

        public String getVt_icon_img() {
            return vt_icon_img;
        }

        public void setVt_icon_img(String vt_icon_img) {
            this.vt_icon_img = vt_icon_img;
        }

        public int getVt_vip_status() {
            return vt_vip_status;
        }

        public void setVt_vip_status(int vt_vip_status) {
            this.vt_vip_status = vt_vip_status;
        }

        public List<?> getSecond() {
            return second;
        }

        public void setSecond(List<?> second) {
            this.second = second;
        }
    }
}
