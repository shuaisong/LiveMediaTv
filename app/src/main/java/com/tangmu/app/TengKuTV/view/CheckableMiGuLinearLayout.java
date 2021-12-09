package com.tangmu.app.TengKuTV.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tangmu.app.TengKuTV.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by lenovo on 2020/2/22.
 * auther:lenovo
 * Date：2020/2/22
 */
public class CheckableMiGuLinearLayout extends LinearLayout implements Checkable {
    private boolean isChecked;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;
    private TextView textView1;
    private TextView textView3;
    private TextView textView2;
    private TextView textView4;

    public CheckableMiGuLinearLayout(@NonNull Context context) {
        this(context, null);
    }

    public CheckableMiGuLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableMiGuLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckableVipLinearLayout);
        isChecked = typedArray.getBoolean(R.styleable.CheckableVipLinearLayout_isChecked, false);
        typedArray.recycle();
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus != isChecked){
                    if (!hasFocus){
                        View currentFocus = ((Activity) getContext()).getCurrentFocus();
                        if (currentFocus==null)return;
                        if (currentFocus instanceof CheckableMiGuFrameLayout){
                            setChecked(true);
                         }else {
                            setChecked(false);
                        }
                    }else {
                        setChecked(true);
                    }
                }
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 1056 * width /1753;
        setMeasuredDimension(width,height);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textView1 = (TextView) getChildAt(0);
        LinearLayout linearLayout = (LinearLayout) getChildAt(1);
        textView2 = (TextView)linearLayout. getChildAt(0);
        textView3 = (TextView) linearLayout. getChildAt(1);
        textView4 = (TextView) getChildAt(2);
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
             setBackgroundResource(R.drawable.migu_checked1);
             textView1.setTextColor(getResources().getColor(R.color.white));
             textView2.setTextColor(getResources().getColor(R.color.white));
             textView3.setTextColor(getResources().getColor(R.color.white));
             textView4.setTextColor(getResources().getColor(R.color.white));
        } else {
            setBackgroundResource(R.drawable.migu_uncheck1);
            textView1.setTextColor(getResources().getColor(R.color.vip));
            textView2.setTextColor(getResources().getColor(R.color.vip));
            textView3.setTextColor(getResources().getColor(R.color.vip));
            textView4.setTextColor(getResources().getColor(R.color.vip));
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
        void onCheckedChanged(CheckableMiGuLinearLayout checkableMiGuLinearLayout, boolean isChecked);
    }
}
