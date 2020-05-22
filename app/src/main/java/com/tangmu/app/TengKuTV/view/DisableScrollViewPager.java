package com.tangmu.app.TengKuTV.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.tangmu.app.TengKuTV.utils.LogUtil;

public class DisableScrollViewPager extends ViewPager {
    public DisableScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public DisableScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
            return super.dispatchKeyEvent(event);
        else
            return false;
    }
}
