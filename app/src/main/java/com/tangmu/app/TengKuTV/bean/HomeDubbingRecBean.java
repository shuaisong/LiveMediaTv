package com.tangmu.app.TengKuTV.bean;

import java.io.Serializable;
import java.util.List;

public class HomeDubbingRecBean implements Serializable {

    /**
     * s_title : 热门配音
     * s_title_z : 热门配音
     * video : [{"uw_id":2,"dv_id":2,"dl_id":"1","dv_title":"2的title","dv_title_z":"དགོད་བྲོའི་གཏམ་གླེང་།","dv_img":"images/2020/3/0150dce62299d0b8078e41e6b380e8f2.jpg","type":1,"lable_title":[{"dl_title":"11","dl_title_z":"11"}]},{"uw_id":1,"dv_id":2,"dl_id":"2","dv_title":"就是这么没","dv_title_z":"དགོད་བྲོའི་གཏམ་གླེང་། ","dv_img":"images/2020/3/0150dce62299d0b8078e41e6b380e8f2.jpg","type":1,"lable_title":[{"dl_title":"22","dl_title_z":"22"}]},{"uw_id":12,"dv_id":2,"dl_id":"1","dv_title":"就是这么没12","dv_title_z":"དགོད་བྲོའི་གཏམ་གླེང་།","dv_img":"images/2020/3/0150dce62299d0b8078e41e6b380e8f2.jpg","type":1,"lable_title":[{"dl_title":"11","dl_title_z":"11"}]},{"uw_id":4,"dv_id":2,"dl_id":"1","dv_title":"就是这么没12","dv_title_z":"དགོད་བྲོའི་གཏམ་གླེང་།","dv_img":"images/2020/3/0150dce62299d0b8078e41e6b380e8f2.jpg","type":1,"lable_title":[{"dl_title":"11","dl_title_z":"11"}]}]
     * s_sorts : 2
     */

    private String s_title;
    private String s_title_z;
    private int s_sorts;
    private List<VideoBean> video;

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

    public int getS_sorts() {
        return s_sorts;
    }

    public void setS_sorts(int s_sorts) {
        this.s_sorts = s_sorts;
    }

    public List<VideoBean> getVideo() {
        return video;
    }

    public void setVideo(List<VideoBean> video) {
        this.video = video;
    }

    public static class VideoBean {
        /**
         * uw_id : 2
         * dv_id : 2
         * dl_id : 1
         * dv_title : 2的title
         * dv_title_z : དགོད་བྲོའི་གཏམ་གླེང་།
         * dv_img : images/2020/3/0150dce62299d0b8078e41e6b380e8f2.jpg
         * type : 1
         * lable_title : [{"dl_title":"11","dl_title_z":"11"}]
         */

        private int uw_id;
        private int dv_id;
        private String dl_id;
        private String dv_title;
        private String dv_title_z;
        private String dv_img;
        private int type;
        private List<LableTitleBean> lable_title;

        public int getUw_id() {
            return uw_id;
        }

        public void setUw_id(int uw_id) {
            this.uw_id = uw_id;
        }

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

        public String getDv_img() {
            return dv_img;
        }

        public void setDv_img(String dv_img) {
            this.dv_img = dv_img;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<LableTitleBean> getLable_title() {
            return lable_title;
        }

        public void setLable_title(List<LableTitleBean> lable_title) {
            this.lable_title = lable_title;
        }

        public static class LableTitleBean {
            /**
             * dl_title : 11
             * dl_title_z : 11
             */

            private String dl_title;
            private String dl_title_z;

            public String getDl_title() {
                return dl_title;
            }

            public void setDl_title(String dl_title) {
                this.dl_title = dl_title;
            }

            public String getDl_title_z() {
                return dl_title_z;
            }

            public void setDl_title_z(String dl_title_z) {
                this.dl_title_z = dl_title_z;
            }
        }
    }
}
