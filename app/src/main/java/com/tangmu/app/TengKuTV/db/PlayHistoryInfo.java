package com.tangmu.app.TengKuTV.db;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 用户视频历史信息类
 * 需要ormlite库支持
 * Created by Martin on 2017/4/24.
 */
@DatabaseTable(tableName = "PlayHistoryInfo")
public class PlayHistoryInfo {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int b_id;
    @DatabaseField
    private String b_title;
    @DatabaseField
    private String b_title_z;

    @DatabaseField
    private String b_img;
    @DatabaseField
    private int b_progress;
    @DatabaseField
    private int b_update_time;
    @DatabaseField
    private int vm_type;
    @DatabaseField
    private int b_type;////1 视频 2音频
    @DatabaseField
    private int b_position;
    @DatabaseField
    private int id_one;//一级分类

    public int getB_id_one() {
        return id_one;
    }

    public void setB_id_one(int id_one) {
        this.id_one = id_one;
    }


    public int getVm_type() {
        return vm_type;
    }

    public void setVm_type(int vm_type) {
        this.vm_type = vm_type;
    }

    public int getB_type() {
        return b_type;
    }

    public void setB_type(int b_type) {
        this.b_type = b_type;
    }

    public int getB_progress() {
        return b_progress;
    }

    public void setB_progress(int b_progress) {
        this.b_progress = b_progress;
    }

    public int getB_update_time() {
        return b_update_time;
    }

    public void setB_update_time(int b_update_time) {
        this.b_update_time = b_update_time;
    }

    public int getB_position() {
        return b_position;
    }

    public void setB_position(int b_position) {
        this.b_position = b_position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getB_id() {
        return b_id;
    }

    public void setB_id(int b_id) {
        this.b_id = b_id;
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

    public String getB_img() {
        return b_img;
    }

    public void setB_img(String b_img) {
        this.b_img = b_img;
    }


    // 必须顶一个无参数的构造函数，否则会报【virtual method】异常
    public PlayHistoryInfo() {
    }

    /*将json字符串转换成model*/
    public static PlayHistoryInfo parse(String jsonStr) {
        return (new Gson()).fromJson(jsonStr, PlayHistoryInfo.class);
    }


}