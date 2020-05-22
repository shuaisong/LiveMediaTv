package com.tencent.liteav.demo.play.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.liteav.demo.play.R;

/**
 * 暂停广告
 */
public class PauseAdView extends FrameLayout implements View.OnClickListener {

    private ImageView imageView;

    public PauseAdView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PauseAdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PauseAdView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context)
                .inflate(R.layout.vod_pause_ad_view, this);
        imageView = findViewById(R.id.image);
        findViewById(R.id.ad_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        hide();
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }


    public void setAdImage(Drawable bitmap) {
        imageView.setImageDrawable(bitmap);
    }
}
