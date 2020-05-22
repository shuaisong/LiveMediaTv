package com.tangmu.app.TengKuTV.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SingleLineItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;

    public SingleLineItemDecoration(int mSpace) {
        this.mSpace = mSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int mPosition = parent.getChildAdapterPosition(view);
        RecyclerView.LayoutManager mLayoutManager = parent.getLayoutManager();
        if (mLayoutManager instanceof LinearLayoutManager)
            if (mLayoutManager instanceof GridLayoutManager) {
                int spanCount = ((GridLayoutManager) mLayoutManager).getSpanCount();
                outRect.right = 0;
                outRect.left = 0;
                if (mPosition % spanCount == 0) {
                    outRect.right = mSpace / 2;
                } else if (mPosition % spanCount == spanCount - 1) {
                    outRect.left = mSpace / 2;
                } else {
                    outRect.left = mSpace / 2;
                    outRect.right = mSpace / 2;
                }
            } else {
                if (mPosition == 0) {
                    outRect.left = 0;
                    outRect.right = mSpace;
                } else if (mPosition == (parent.getAdapter().getItemCount() - 1)) {
                    outRect.right = 0;
                    outRect.left = mSpace;
                } else {
                    outRect.right = mSpace;
                    outRect.left = mSpace;
                }
            }
    }

}
