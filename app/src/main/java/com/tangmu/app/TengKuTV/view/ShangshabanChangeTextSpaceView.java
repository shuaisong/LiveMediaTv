package com.tangmu.app.TengKuTV.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tangmu.app.TengKuTV.R;


/**
 * Created by lenovo on 2020/2/22.
 * auther:lenovo
 * Date：2020/2/22
 */
public class ShangshabanChangeTextSpaceView extends TextView {
    private float spacing;


    public ShangshabanChangeTextSpaceView(Context context) {
        this(context,null);
    }

    public ShangshabanChangeTextSpaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShangshabanChangeTextSpaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShangshabanChangeTextSpaceView);
        spacing = typedArray.getFloat(R.styleable.ShangshabanChangeTextSpaceView_space, 0);
        typedArray.recycle();
    }

    public float getSpacing() {
        return this.spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
        applySpacing();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        applySpacing();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        applySpacing();
    }

    private void applySpacing() {
        CharSequence text = getText();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            builder.append(text.charAt(i));
            if (i + 1 < text.length()) {
                builder.append("\u00A0");
            }
        }
        SpannableString finalText = new SpannableString(builder.toString());
        if (builder.toString().length() > 1) {
            for (int i = 1; i < builder.toString().length(); i += 2) {
                finalText.setSpan(new ScaleXSpan((spacing + 1) / 10), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        super.setText(finalText, BufferType.SPANNABLE);
    }


}
