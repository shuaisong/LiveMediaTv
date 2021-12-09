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
import android.widget.TextView;

import com.tangmu.app.TengKuTV.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by lenovo on 2020/2/22.
 * auther:lenovo
 * Date：2020/2/22
 */
public class CheckableMiGuFrameLayout1 extends FrameLayout implements Checkable {
    private boolean isChecked;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;
    private ImageView imageView;
    private int unchecked_logo;
    private int checked_logo;
    private int color3;
    private int color1;
    private int color2;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;

    public CheckableMiGuFrameLayout1(@NonNull Context context) {
        this(context, null);
    }

    public CheckableMiGuFrameLayout1(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableMiGuFrameLayout1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckableMiGuFrameLayout1);
        isChecked = typedArray.getBoolean(R.styleable.CheckableMiGuFrameLayout1_isChecked1, false);
        unchecked_logo = typedArray.getResourceId(R.styleable.CheckableMiGuFrameLayout1_unchecked_logo, R.drawable.migu1);
        checked_logo = typedArray.getResourceId(R.styleable.CheckableMiGuFrameLayout1_checked_logo, R.drawable.migu2);
        typedArray.recycle();
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus != isChecked){
                    setChecked(hasFocus);
                }
            }
        });
        color1 = Color.parseColor("#302E2F");
        color2 = Color.WHITE;
        color3 = Color.parseColor("#FF0506");
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
        View view =   getChildAt(getChildCount() - 1);
        if (view instanceof ImageView)
            imageView = (ImageView) view;
        textView1 = (TextView) getChildAt(0);
        textView2 = (TextView) getChildAt(1);
        textView3 = (TextView) getChildAt(2);
        setChecked(isChecked);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 20 * width /24;
        setMeasuredDimension(width,height);
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
               if (imageView!=null){
                   imageView.setImageResource(checked_logo);
               }
                 setBackgroundResource(R.drawable.migu_1);
           } else {
               if (imageView!=null){
                   imageView.setImageResource(unchecked_logo);
               }
                 setBackgroundResource(R.drawable.migu3);
           }
           setTextColor(isChecked);
    }

    private void setTextColor(boolean isChecked) {
         if (isChecked){
            textView1.setTextColor(color2);
            textView2.setTextColor(color2);
            textView3.setTextColor(color3);
            textView3.setBackground(getResources().getDrawable(R.drawable.pay_now_bg1));
         }else {
             textView1.setTextColor(color1);
             textView2.setTextColor(color1);
             textView3.setTextColor(color1);
             textView3.setBackground(getResources().getDrawable(R.drawable.pay_now_bg));
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
        void onCheckedChanged(CheckableMiGuFrameLayout1 checkableMiGuFrameLayout, boolean isChecked);
    }
}
