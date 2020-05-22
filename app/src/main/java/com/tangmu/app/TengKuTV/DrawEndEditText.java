package com.tangmu.app.TengKuTV;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

public class DrawEndEditText extends AppCompatEditText {
    private Drawable mClearDrawable;
    private EndClickListener endClickListener;
    private boolean isPassword;

    public void setEndClickListener(EndClickListener endClickListener) {
        this.endClickListener = endClickListener;
    }

    public DrawEndEditText(Context context) {
        this(context, null);
    }

    public DrawEndEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public boolean isPassword() {
        return isPassword;
    }

    public DrawEndEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawEndEditText);
        isPassword = typedArray.getBoolean(R.styleable.DrawEndEditText_is_password, false);
        typedArray.recycle();
        init();
    }

    private void init() {
        //获取EditText的DrawableRight,getCompoundDrawablesRelative()获取Drawable的四个位置的数组
        mClearDrawable = getCompoundDrawablesRelative()[2];
        if (mClearDrawable != null)
            //设置图标的位置以及大小,getIntrinsicWidth()获取显示出来的大小而不是原图片的带小
            mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth() + 10, mClearDrawable.getIntrinsicHeight() + 10);
        //默认设置隐藏图标
        setClearIconVisible(false);
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setClearIconVisible(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();
        setCompoundDrawablesRelative(compoundDrawablesRelative[0],
                compoundDrawablesRelative[1], right, compoundDrawablesRelative[3]);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawablesRelative()[2] != null) {
                //getTotalPaddingRight()图标左边缘至控件右边缘的距离
                //getWidth() - getTotalPaddingRight()表示从最左边到图标左边缘的位置
                //getWidth() - getPaddingRight()表示最左边到图标右边缘的位置
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable && endClickListener != null) {
                    endClickListener.onClick(this);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setEndDrawable(Drawable endDrawable) {
        mClearDrawable = endDrawable;
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth() + 10, mClearDrawable.getIntrinsicHeight() + 10);
    }

    public interface EndClickListener {
        void onClick(DrawEndEditText drawEndEditText);
    }
}
