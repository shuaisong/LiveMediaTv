package com.tencent.liteav.demo.play.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.liteav.demo.play.R;

public class TCVodDubbingView extends LinearLayout implements View.OnFocusChangeListener {

    private static final float SCALE_MIN_VALUE = 1.0f;
    private static final float SCALE_MAX_VALUE = 1.3f;
    private BaseQuickAdapter<String, BaseViewHolder> adapter;

    public TCVodDubbingView(Context context) {
        super(context);
        initView(context);
    }

    public TCVodDubbingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TCVodDubbingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_tov_dubbing, this, false);
        RecyclerView dubbingRecyclerview = findViewById(R.id.dubbing_recyclerview);
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_dubbing) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.itemView.setOnFocusChangeListener(TCVodDubbingView.this);
            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        dubbingRecyclerview.setAdapter(adapter);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        TextView tvTitle = v.findViewById(R.id.title);
        if (hasFocus) {
            scaleIn(v);
            tvTitle.setTextColor(getResources().getColor(R.color.dubbing_focus));
        } else {
            scaleOut(v);
            tvTitle.setTextColor(Color.WHITE);
        }
    }

    private void scaleIn(View v) {
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY",
                SCALE_MIN_VALUE, SCALE_MAX_VALUE);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX",
                SCALE_MIN_VALUE, SCALE_MAX_VALUE);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleX).with(scaleY);
        animSet.setDuration(200);
        animSet.start();
    }

    private void scaleOut(View v) {
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY",
                SCALE_MAX_VALUE, SCALE_MIN_VALUE);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX",
                SCALE_MAX_VALUE, SCALE_MIN_VALUE);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleX).with(scaleY);
        animSet.setDuration(200);
        animSet.start();
    }
}
