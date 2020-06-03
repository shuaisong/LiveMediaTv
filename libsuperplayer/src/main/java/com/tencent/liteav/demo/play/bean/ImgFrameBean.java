package com.tencent.liteav.demo.play.bean;

import android.graphics.Bitmap;

public class ImgFrameBean {
    private int time;
    private Bitmap bitmap;

    public ImgFrameBean(int time, Bitmap bitmap) {
        this.time = time;
        this.bitmap = bitmap;
    }

    public int getTime() {
        return time;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
