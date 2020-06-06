package com.tencent.liteav.demo.play.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.tencent.liteav.demo.play.R;

public class TCVodSettingMoreView extends LinearLayout implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private TCVodMoreView.Callback callback;

    public TCVodSettingMoreView(Context context) {
        super(context);
        init(context);
    }

    public TCVodSettingMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TCVodSettingMoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.tcv_setting_more, this);
        RadioGroup radioGroup_jump = findViewById(R.id.radioGroup_jump);
        RadioGroup radioGroup_scale = findViewById(R.id.radioGroup_scale);
        radioGroup_jump.setOnCheckedChangeListener(this);
        radioGroup_scale.setOnCheckedChangeListener(this);
        findViewById(R.id.jump).setOnClickListener(this);
        findViewById(R.id.no_jump).setOnClickListener(this);
        findViewById(R.id.original_scale).setOnClickListener(this);
        findViewById(R.id.full_screen).setOnClickListener(this);
    }

    public void setCallback(TCVodMoreView.Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.jump) {
            callback.jumpTitleTail(true);
        } else if (checkedId == R.id.no_jump) {
            callback.jumpTitleTail(false);
        } else if (checkedId == R.id.original_scale) {
            callback.fullScale(false);
        } else if (checkedId == R.id.full_screen) {
            callback.fullScale(true);
        }
    }

    public void show() {
        setVisibility(VISIBLE);
        findViewById(R.id.jump).requestFocus();
    }

    public void hide() {
        setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        callback.hide();
    }
}
