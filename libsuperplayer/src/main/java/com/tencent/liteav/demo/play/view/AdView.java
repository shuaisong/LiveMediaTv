package com.tencent.liteav.demo.play.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tencent.liteav.demo.play.R;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.bean.TCPlayImageSpriteInfo;
import com.tencent.liteav.demo.play.bean.TCPlayKeyFrameDescInfo;
import com.tencent.liteav.demo.play.bean.TCVideoQuality;
import com.tencent.liteav.demo.play.controller.IController;
import com.tencent.liteav.demo.play.controller.IControllerCallback;

import java.util.List;

public class AdView extends FrameLayout implements IController {

    private TextView tvDuration;

    public AdView(Context context) {
        super(context);
        init(context);
    }

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setDuration(int duration) {
        tvDuration.setText(String.valueOf(duration));
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.vod_ad_window, this);
        tvDuration = findViewById(R.id.tv_duration);
    }

    /**
     * 设置回调
     *
     * @param callback 回调接口实现对象
     */
    @Override
    public void setCallback(IControllerCallback callback) {
    }

    @Override
    public void setWatermark(Bitmap bmp, float x, float y) {

    }

    @Override
    public void show() {
        setVisibility(VISIBLE);
    }

    @Override
    public void hide() {
        setVisibility(GONE);
    }

    @Override
    public void release() {

    }

    @Override
    public void updatePlayState(int playState) {

    }

    @Override
    public void setVideoQualityList(List<TCVideoQuality> list) {

    }

    @Override
    public void updateTitle(String title) {

    }

    @Override
    public void updateVideoProgress(long current, long duration) {
        setDuration((int) (duration - current));
    }

    @Override
    public void updatePlayType(int type) {

    }

    @Override
    public void setBackground(Bitmap bitmap) {

    }

    @Override
    public void showBackground() {

    }

    @Override
    public void hideBackground() {

    }

    @Override
    public void updateVideoQuality(TCVideoQuality videoQuality) {

    }

    @Override
    public void updateImageSpriteInfo(TCPlayImageSpriteInfo info) {

    }

    @Override
    public void updateKeyFrameDescInfo(List<TCPlayKeyFrameDescInfo> list) {

    }


    public void setAdClickListener(OnClickListener onClickListener) {
       // findViewById(R.id.free_ad).setOnClickListener(onClickListener);
    }
}
