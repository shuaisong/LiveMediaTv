package com.tangmu.app.TengKuTV.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class EllipsisView extends View {

    private int radius;
    private int divider;
    private Paint paint;

    public EllipsisView(Context context) {
        this(context, null);
    }

    public EllipsisView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EllipsisView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        radius = AutoSizeUtils.dp2px(context, 1.0f);
        divider = AutoSizeUtils.dp2px(context, 4);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(radius * 8 + 3 * divider, 2 * radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 4; i++) {
            canvas.drawCircle(radius * (i * 2 + 1) + divider * i, radius, radius, paint);
        }
    }
}
