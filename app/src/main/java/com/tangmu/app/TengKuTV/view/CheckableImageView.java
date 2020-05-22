package com.tangmu.app.TengKuTV.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.tangmu.app.TengKuTV.R;

public class CheckableImageView extends ImageView implements Checkable {
    private boolean isChecked;
    private int unCheckedImg;
    private int checkedImg;
    private int tintColor;

    public CheckableImageView(Context context) {
        this(context, null);
    }

    public CheckableImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckableImageView);
        unCheckedImg = typedArray.getResourceId(R.styleable.CheckableImageView_unCheckedImg, 0);
        checkedImg = typedArray.getResourceId(R.styleable.CheckableImageView_checkedImg, 0);
        tintColor = typedArray.getColor(R.styleable.CheckableImageView_tintColor, Color.RED);
        isChecked = typedArray.getBoolean(R.styleable.CheckableImageView_checked, false);
        typedArray.recycle();
        refresh();
    }

    private void refresh() {
        if (isChecked) {
            if (checkedImg != 0) {
                setImageResource(checkedImg);
            } else {
                setColorFilter(tintColor);
            }
        } else {
            if (unCheckedImg != 0) {
                setImageResource(unCheckedImg);
            } else {
                setColorFilter(Color.TRANSPARENT);
            }
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if (isChecked == checked) {
            return;
        }
        isChecked = checked;
        if (isChecked) {
            if (checkedImg != 0) {
                setImageResource(checkedImg);
            } else {
                setColorFilter(tintColor);
            }
        } else {
            if (unCheckedImg != 0) {
                setImageResource(unCheckedImg);
            } else {
                setColorFilter(Color.TRANSPARENT);
            }
        }
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
