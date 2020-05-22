package com.tangmu.app.TengKuTV.bean;

public class LiveBean {

    /**
     * l_id : 7
     * l_img : images/2020/3/20c7d85bcb8d7996a7b585c34061ad17.jpg
     * l_live_type : 1
     * l_title : 直播3
     * l_title_z : 直播藏文3
     * l_room_id : 818254
     *  "pull_url1": "http://play.longdawenhua.cn/live/551394_liuchang.flv",
     *       "pull_url2": "http://play.longdawenhua.cn/live/551394_biaoqing.flv",
     * pull_url : http://play.longdawenhua.cn/live/818254.flv
     */

    private int lr_id;
    /**
     * pull_url1 : http://play.longdawenhua.cn/live/551394_liuchang.flv
     * pull_url2 : http://play.longdawenhua.cn/live/551394_biaoqing.flv
     */

    private String pull_url1;
    private String pull_url2;

    public int getLr_id() {
        return lr_id;
    }

    private int l_id;
    private String l_img;
    private int l_live_type;
    private String l_title;
    private String l_title_z;
    private int l_room_id;
    private String pull_url;

    public int getL_id() {
        return l_id;
    }

    public void setL_id(int l_id) {
        this.l_id = l_id;
    }

    public String getL_img() {
        return l_img;
    }

    public void setL_img(String l_img) {
        this.l_img = l_img;
    }

    public int getL_live_type() {
        return l_live_type;
    }

    public void setL_live_type(int l_live_type) {
        this.l_live_type = l_live_type;
    }

    public String getL_title() {
        return l_title;
    }

    public void setL_title(String l_title) {
        this.l_title = l_title;
    }

    public String getL_title_z() {
        return l_title_z;
    }

    public void setL_title_z(String l_title_z) {
        this.l_title_z = l_title_z;
    }

    public int getL_room_id() {
        return l_room_id;
    }

    public void setL_room_id(int l_room_id) {
        this.l_room_id = l_room_id;
    }

    public String getPull_url() {
        return pull_url;
    }

    public void setPull_url(String pull_url) {
        this.pull_url = pull_url;
    }

    public String getPull_url1() {
        return pull_url1;
    }

    public void setPull_url1(String pull_url1) {
        this.pull_url1 = pull_url1;
    }

    public String getPull_url2() {
        return pull_url2;
    }

    public void setPull_url2(String pull_url2) {
        this.pull_url2 = pull_url2;
    }
}
