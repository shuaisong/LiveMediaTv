package com.tangmu.app.TengKuTV.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.tangmu.app.TengKuTV.R;


public class CustomPraiseView extends LinearLayout implements Checkable {
    private boolean isChecked;
    private CheckBox ic_checkBox;
    private CheckBox tv_checkBox;
    private int checked_resourceId;
    private int unchecked_resourceId;

    public CustomPraiseView(Context context) {
        this(context, null);
    }

    public CustomPraiseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPraiseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomPraiseView);
        unchecked_resourceId = typedArray.getResourceId(R.styleable.CustomPraiseView_unchecked_bg, 0);
        checked_resourceId = typedArray.getResourceId(R.styleable.CustomPraiseView_checked_bg, 0);
        typedArray.recycle();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (ic_checkBox == null) {
            View childAt = getChildAt(0);
            if (childAt instanceof CheckBox)
                ic_checkBox = (CheckBox) childAt;
        }
        if (tv_checkBox == null) {
            View childAt = getChildAt(1);
            if (childAt instanceof CheckBox)
                tv_checkBox = (CheckBox) childAt;
        }
    }

    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;
        if (checked_resourceId != 0 && unchecked_resourceId != 0)
            setBackgroundResource(checked ? checked_resourceId : unchecked_resourceId);
        if (ic_checkBox != null) {
            ic_checkBox.setChecked(checked);
            ic_checkBox.invalidate();
        }
        if (tv_checkBox != null)
            tv_checkBox.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
