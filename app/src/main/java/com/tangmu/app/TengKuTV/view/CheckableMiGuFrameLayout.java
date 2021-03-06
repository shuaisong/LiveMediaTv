package com.tangmu.app.TengKuTV.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tangmu.app.TengKuTV.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by lenovo on 2020/2/22.
 * auther:lenovo
 * Date：2020/2/22
 */
public class CheckableMiGuFrameLayout extends FrameLayout implements Checkable {
    private boolean isChecked;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;
    private int color1;
    private int color2;
    private List<TextView> textViews;

    public CheckableMiGuFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public CheckableMiGuFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableMiGuFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckableVipLinearLayout);
        isChecked = typedArray.getBoolean(R.styleable.CheckableVipLinearLayout_isChecked, false);
        typedArray.recycle();
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus != isChecked) {
                    setChecked(hasFocus);
                }
            }
        });

        color1 = Color.parseColor("#E4D5B3");

        color2 = Color.WHITE;

        textViews = new ArrayList<>();
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
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof TextView){
                textViews.add((TextView) childAt);
            }
        }
        setChecked(isChecked);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 226 * width / 345;
        setMeasuredDimension(width, height);
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
            setBackgroundResource(R.drawable.migu_pay_checked);
        } else {
            setBackgroundResource(R.drawable.migu_pay_uncheck);
        }
        for (TextView textView : textViews) {
            if (isChecked){
                textView.setTextColor(color1);
            }else {
                textView.setTextColor(color2);
            }
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
        void onCheckedChanged(CheckableMiGuFrameLayout checkableMiGuFrameLayout, boolean isChecked);
    }
}
