package com.tencent.liteav.demo.play.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.tencent.liteav.demo.play.R;
import com.tencent.liteav.demo.play.utils.SPUtils;

public class TCVodSettingMoreView extends LinearLayout implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private TCVodMoreView.Callback callback;
    private Context cxt;

    public TCVodSettingMoreView(Context context) {
        super(context);
        this.cxt = context;
        init(context);
    }

    public TCVodSettingMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.cxt = context;
        init(context);
    }

    public TCVodSettingMoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.cxt = context;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.tcv_setting_more, this);
        RadioGroup radioGroup_jump = findViewById(R.id.radioGroup_jump);
        RadioGroup radioGroup_scale = findViewById(R.id.radioGroup_scale);
        boolean isJump = SPUtils.init(cxt).getBoolean("jump", true);
        if (isJump) {
            radioGroup_jump.check(R.id.jump);
        } else {
            radioGroup_jump.check(R.id.no_jump);
        }

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
            SPUtils.init(cxt).putBoolean("jump", true);
        } else if (checkedId == R.id.no_jump) {
            SPUtils.init(cxt).putBoolean("jump", false);
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
