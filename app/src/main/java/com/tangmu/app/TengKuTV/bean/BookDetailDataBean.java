package com.tangmu.app.TengKuTV.bean;

import java.io.Serializable;
import java.util.List;

public class BookDetailDataBean implements Serializable {

    /**
     * b_id : 2
     * vt_id_one : 8
     * vt_id_two : 10
     * b_img : images/2020/3/f9c0c2d477475913b0aef2561fe22e8e.jpg
     * b_title : 斗破苍穹
     * b_title_z : 斗破苍穹藏文
     * b_author : 我爱吃西红柿
     * b_episode : 2341
     * b_num : 0
     * b_des : 《斗破苍穹》是一本连载于起点中文网的古装玄幻小说，作者是起点白金作家天蚕土豆（李虎），已完结。这里是属于斗气的世界，没有花俏艳丽的魔法，有的，仅仅是繁衍到巅峰的斗气！心潮澎湃，无限幻想，迎风挥击千层浪，少年不败热血！新书等级制度：斗之气，斗者，斗师，大斗师，斗灵，斗王，斗皇，斗宗，斗尊，斗圣，斗帝
     * b_des_z : 《斗破苍穹》是一本连载于起点中文网的古装玄幻小说，作者是起点白金作家天蚕土豆（李虎），已完结。这里是属于斗气的世界，没有花俏艳丽的魔法，有的，仅仅是繁衍到巅峰的斗气！心潮澎湃，无限幻想，迎风挥击千层浪，少年不败热血！新书等级制度：斗之气，斗者，斗师，大斗师，斗灵，斗王，斗皇，斗宗，斗尊，斗圣，斗帝
     * b_status : 2
     * b_update_status : 1
     * b_add_time : 2020-03-27 15:27:37
     * b_search_num : 1
     * b_recommend : 1
     * collec_num : 5
     * book_detail : [{"bd_id":2,"b_id":2,"bd_name":"第一章","bd_name_z":"第一章藏文","bd_vedio_url":"202f672fvodcq1258540389/96e01d8b5285890800524176655/3xAO7kyM9EQA.mp3","bd_status":1,"bd_number":1,"bd_add_time":"2020-03-27 15:27:37","bd_fileid":"5285890800524176655"}]
     */

    private int b_id;
    private int vt_id_one;
    private int vt_id_two;
    private String b_img;
    private String b_title;
    private String b_title_z;
    private String b_author;
    private int b_episode;
    private int b_num;
    private String b_des;
    private String b_des_z;
    private int b_status;
    private int b_update_status;
    private String b_add_time;
    private int b_search_num;
    private int b_recommend;
    private int collec_num;
    private List<BookDetailBean> book_detail;
    private int is_collec_status;
    private int progress;
    private int uc_id;

    public int getUc_id() {
        return uc_id;
    }

    public void setUc_id(int uc_id) {
        this.uc_id = uc_id;
    }

    public int getIs_collec_status() {
        return is_collec_status;
    }

    public void setIs_collec_status(int is_collec_status) {
        this.is_collec_status = is_collec_status;
    }

    public int getB_id() {
        return b_id;
    }

    public void setB_id(int b_id) {
        this.b_id = b_id;
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

    public String getB_img() {
        return b_img;
    }

    public void setB_img(String b_img) {
        this.b_img = b_img;
    }

    public String getB_title() {
        return b_title;
    }

    public void setB_title(String b_title) {
        this.b_title = b_title;
    }

    public String getB_title_z() {
        return b_title_z;
    }

    public void setB_title_z(String b_title_z) {
        this.b_title_z = b_title_z;
    }

    public String getB_author() {
        return b_author;
    }

    public void setB_author(String b_author) {
        this.b_author = b_author;
    }

    public int getB_episode() {
        return b_episode;
    }

    public void setB_episode(int b_episode) {
        this.b_episode = b_episode;
    }

    public int getB_num() {
        return b_num;
    }

    public void setB_num(int b_num) {
        this.b_num = b_num;
    }

    public String getB_des() {
        return b_des;
    }

    public void setB_des(String b_des) {
        this.b_des = b_des;
    }

    public String getB_des_z() {
        return b_des_z;
    }

    public void setB_des_z(String b_des_z) {
        this.b_des_z = b_des_z;
    }

    public int getB_status() {
        return b_status;
    }

    public void setB_status(int b_status) {
        this.b_status = b_status;
    }

    public int getB_update_status() {
        return b_update_status;
    }

    public void setB_update_status(int b_update_status) {
        this.b_update_status = b_update_status;
    }

    public String getB_add_time() {
        return b_add_time;
    }

    public void setB_add_time(String b_add_time) {
        this.b_add_time = b_add_time;
    }

    public int getB_search_num() {
        return b_search_num;
    }

    public void setB_search_num(int b_search_num) {
        this.b_search_num = b_search_num;
    }

    public int getB_recommend() {
        return b_recommend;
    }

    public void setB_recommend(int b_recommend) {
        this.b_recommend = b_recommend;
    }

    public int getCollec_num() {
        return collec_num;
    }

    public void setCollec_num(int collec_num) {
        this.collec_num = collec_num;
    }

    public List<BookDetailBean> getBook_detail() {
        return book_detail;
    }

    public void setBook_detail(List<BookDetailBean> book_detail) {
        this.book_detail = book_detail;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public static class BookDetailBean implements Serializable {
        /**
         * bd_id : 2
         * b_id : 2
         * bd_name : 第一章
         * bd_name_z : 第一章藏文
         * bd_vedio_url : 202f672fvodcq1258540389/96e01d8b5285890800524176655/3xAO7kyM9EQA.mp3
         * bd_status : 1
         * bd_number : 1
         * bd_add_time : 2020-03-27 15:27:37
         * bd_fileid : 5285890800524176655
         * bd_times
         */

        private int bd_id;
        private int b_id;
        private String bd_name;
        private String bd_name_z;
        private String bd_vedio_url;
        private int bd_status;
        private int bd_number;
        private String bd_add_time;
        private String bd_fileid;
        private String bd_times;

        public String getBd_times() {
            return bd_times;
        }

        public void setBd_times(String bd_times) {
            this.bd_times = bd_times;
        }

        public int getBd_id() {
            return bd_id;
        }

        public void setBd_id(int bd_id) {
            this.bd_id = bd_id;
        }

        public int getB_id() {
            return b_id;
        }

        public void setB_id(int b_id) {
            this.b_id = b_id;
        }

        public String getBd_name() {
            return bd_name;
        }

        public void setBd_name(String bd_name) {
            this.bd_name = bd_name;
        }

        public String getBd_name_z() {
            return bd_name_z;
        }

        public void setBd_name_z(String bd_name_z) {
            this.bd_name_z = bd_name_z;
        }

        public String getBd_vedio_url() {
            return bd_vedio_url;
        }

        public void setBd_vedio_url(String bd_vedio_url) {
            this.bd_vedio_url = bd_vedio_url;
        }

        public int getBd_status() {
            return bd_status;
        }

        public void setBd_status(int bd_status) {
            this.bd_status = bd_status;
        }

        public int getBd_number() {
            return bd_number;
        }

        public void setBd_number(int bd_number) {
            this.bd_number = bd_number;
        }

        public String getBd_add_time() {
            return bd_add_time;
        }

        public void setBd_add_time(String bd_add_time) {
            this.bd_add_time = bd_add_time;
        }

        public String getBd_fileid() {
            return bd_fileid;
        }

        public void setBd_fileid(String bd_fileid) {
            this.bd_fileid = bd_fileid;
        }
    }
}
