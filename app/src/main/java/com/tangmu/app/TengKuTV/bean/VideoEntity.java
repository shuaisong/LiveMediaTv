package com.tangmu.app.TengKuTV.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class VideoEntity implements Serializable, Comparable<VideoEntity> {
    private int ID;
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public int getDuration() {
        return duration;
    }

    private int duration;
    private long size;
    private long date;

    public VideoEntity(int ID, String filePath, int duration, long size, long date) {
        this.ID = ID;
        this.filePath = filePath;
        this.duration = duration;
        this.size = size;
        this.date = date;
    }

    @Override
    public int compareTo(@NonNull VideoEntity o) {
        long i = this.date - o.date;//先按照时间排序
        if (i == 0) {
            i = this.ID - o.ID;//如果时间相等了再用Id进行排序
        }
        return (int) -i;
    }
}
