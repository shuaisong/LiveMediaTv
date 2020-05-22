package com.tangmu.app.TengKuTV.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class TopMiddleDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private int mTop;
    private Paint paint;

/*    public TopMiddleDecoration(int mSpace, int color) {
        this.mSpace = mSpace;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setAntiAlias(true);
    }*/

    public TopMiddleDecoration(int mSpace) {
        this.mTop = mSpace;
        this.mSpace = mSpace;
    }

    public TopMiddleDecoration(int mTop, int mSpace) {
        this.mSpace = mSpace;
        this.mTop = mTop;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager mLayoutManager = parent.getLayoutManager();
        if (mLayoutManager instanceof LinearLayoutManager) {
            if (!(mLayoutManager instanceof GridLayoutManager)) {
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                if (((LinearLayoutManager) mLayoutManager).getOrientation() == LinearLayoutManager.VERTICAL) {
                    if (childAdapterPosition == 0)
                        outRect.top = mTop;
                    outRect.bottom = mSpace;
                }
            }
        }
    }

/*    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (paint == null) return;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            float top = view.getTop();
            float bottom = top + mSpace;
            c.drawRect(0, top, view.getWidth(), bottom, paint);
        }
    }*/
}
