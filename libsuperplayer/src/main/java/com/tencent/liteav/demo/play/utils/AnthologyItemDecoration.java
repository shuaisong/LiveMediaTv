package com.tencent.liteav.demo.play.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class AnthologyItemDecoration extends RecyclerView.ItemDecoration {
    private int left;
    private int space;
    private int bottom;

    public AnthologyItemDecoration(int left, int space) {
        this.left = left;
        this.space = space;
    }

    public AnthologyItemDecoration(int left, int space, int bottom) {
        this.left = left;
        this.space = space;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            outRect.bottom = bottom;
            if (layoutManager instanceof GridLayoutManager) {
                int spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
                if (position % spanCount == 0) {
                    outRect.left = left;
                    outRect.right = space;
                } else if (position % spanCount == spanCount - 1) {
                    outRect.left = space;
                    outRect.right = 0;
                } else {
                    outRect.left = space;
                    outRect.right = space;
                }
                outRect.bottom = AutoSizeUtils.dp2px(parent.getContext(), 9);
            } else {
                if (position == 0) {
                    outRect.left = left;
                    outRect.right = space;
                } else {
                    outRect.left = space;
                    outRect.right = space;
                }
            }
        }
    }
}
