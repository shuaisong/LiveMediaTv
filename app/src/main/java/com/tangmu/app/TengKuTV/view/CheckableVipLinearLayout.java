package com.tangmu.app.TengKuTV.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tangmu.app.TengKuTV.R;

/**
 * Created by lenovo on 2020/2/22.
 * auther:lenovo
 * Date：2020/2/22
 */
public class CheckableVipLinearLayout extends LinearLayout implements Checkable {
    private boolean isChecked;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;
    private ImageView imgIndicator;
    private ImageView imageView2;
    private ImageView imageView1;
    private ImageView imageView0;

    public CheckableVipLinearLayout(@NonNull Context context) {
        this(context, null);
    }

    public CheckableVipLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableVipLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckableVipLinearLayout);
        isChecked = typedArray.getBoolean(R.styleable.CheckableVipLinearLayout_isChecked, false);
        typedArray.recycle();
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus != isChecked)
                    setChecked(hasFocus);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            toggle();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        FrameLayout frameLayout = (FrameLayout) getChildAt(0);
        LinearLayout linearLayout = (LinearLayout) frameLayout.getChildAt(0);
        imageView0 = (ImageView) linearLayout.getChildAt(0);
        imageView1 = (ImageView) linearLayout.getChildAt(1);
        imageView2 = (ImageView) linearLayout.getChildAt(2);

        imgIndicator = (ImageView) getChildAt(1);
        setChecked(isChecked);
    }

    /**
     * Change the checked state of the view
     *
     * @param checked The new checked state
     */
    @Override
    public void setChecked(boolean checked) {
        boolean preChecked = isChecked;
        isChecked = checked;
        if (preChecked != checked && mOnCheckedChangeWidgetListener != null) {
            mOnCheckedChangeWidgetListener.onCheckedChanged(this, isChecked);
        }
        if (isChecked) {
            imageView0.setBackgroundResource(R.mipmap.buy_vip_checked1);
            imageView1.setBackgroundResource(R.mipmap.buy_vip_checked2);
            imageView2.setBackgroundResource(R.mipmap.buy_vip_checked3);

            imgIndicator.setVisibility(VISIBLE);
        } else {
            imageView0.setBackgroundResource(R.mipmap.buy_vip_unchecked1);
            imageView1.setBackgroundResource(R.mipmap.buy_vip_unchecked2);
            imageView2.setBackgroundResource(R.mipmap.buy_vip_unchecked3);

            imgIndicator.setVisibility(INVISIBLE);
        }
    }

    /**
     * @return The current checked state of the view
     */
    @Override
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * Change the checked state of the view to the inverse of its current state
     */
    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    public void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(CheckableVipLinearLayout checkableVipLinearLayout, boolean isChecked);
    }
}
