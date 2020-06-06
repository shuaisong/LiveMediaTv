package com.tencent.liteav.demo.play.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.liteav.demo.play.R;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.bean.TCPlayImageSpriteInfo;
import com.tencent.liteav.demo.play.bean.TCPlayKeyFrameDescInfo;
import com.tencent.liteav.demo.play.bean.TCVideoQuality;
import com.tencent.liteav.demo.play.controller.IController;
import com.tencent.liteav.demo.play.controller.IControllerCallback;

import java.util.List;

public class VipTipView extends LinearLayout {


    private TextView openVip;
    private TextView login_tip;
    private TextView open_vip_tip;

    public VipTipView(Context context) {
        super(context);
        init(context);
    }

    public VipTipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VipTipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.vod_vip_tip_window, this);
        openVip = (TextView) findViewById(R.id.open_vip);
        login_tip = (TextView) findViewById(R.id.login_tip);
        open_vip_tip = (TextView) findViewById(R.id.open_vip_tip);
    }


    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }


    public void setLoginClickListener(OnClickListener onClickListener) {
        findViewById(R.id.open_vip)
                .setOnClickListener(onClickListener);
    }

    public void isDefaultLanguage(boolean defaultLanguage) {
        TextView textView = findViewById(R.id.login_tip);
        String string = getResources().getString(R.string.if_is_vip);
        if (defaultLanguage) {
            SpannableString spannableString = new SpannableString(string);
            ForegroundColorSpan colorSpan_white = new ForegroundColorSpan(Color.WHITE);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FFD55D"));
            spannableString.setSpan(colorSpan, string.length() - 2, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(colorSpan_white, 0, string.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        } else {
            textView.setTextColor(Color.WHITE);
            textView.setText(string);
        }
    }

    public void fullScreen(boolean isFull) {
        if (isFull) {
            openVip.setTextSize(33);
            login_tip.setTextSize(20);
            open_vip_tip.setTextSize(25);
        } else {
            openVip.setTextSize(15);
            login_tip.setTextSize(11);
            open_vip_tip.setTextSize(18);
        }
    }
}
