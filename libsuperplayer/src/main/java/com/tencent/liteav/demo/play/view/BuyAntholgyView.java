package com.tencent.liteav.demo.play.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.liteav.demo.play.R;

public class BuyAntholgyView extends LinearLayout {


    private TextView to_buy, to_buy1, buy_tip;
    private View buy_line;

    public BuyAntholgyView(Context context) {
        super(context);
        init(context);
    }

    public BuyAntholgyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BuyAntholgyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.antholgy_buy_view, this);
        buy_line = findViewById(R.id.buy_line);
        to_buy = findViewById(R.id.to_buy);
        to_buy1 = findViewById(R.id.to_buy1);
        buy_tip = findViewById(R.id.buy_tip);
    }


    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }


    public void setBuyClickListener(OnClickListener onClickListener) {
        findViewById(R.id.to_buy)
                .setOnClickListener(onClickListener);
        findViewById(R.id.to_buy1)
                .setOnClickListener(onClickListener);
    }

    public void showFullVisible() {
        to_buy.setVisibility(INVISIBLE);
        buy_line.setVisibility(VISIBLE);
    }

    public boolean isFullVisible() {
        return getVisibility() == VISIBLE && buy_line.getVisibility() == VISIBLE;
    }

    public void fullScreen(boolean isFull) {
        if (isFull) {
            to_buy1.setTextSize(33);
            to_buy.setTextSize(20);
            buy_tip.setTextSize(25);
        } else {
            to_buy1.setTextSize(15);
            to_buy.setTextSize(12);
            buy_tip.setTextSize(16);
        }
    }
}
