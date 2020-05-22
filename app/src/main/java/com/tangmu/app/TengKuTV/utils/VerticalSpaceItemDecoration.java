package com.tangmu.app.TengKuTV.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private int mTop;

    public VerticalSpaceItemDecoration(int mSpace, int top) {
        this.mSpace = mSpace;
        this.mTop = top;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int mPosition = parent.getChildAdapterPosition(view);
        RecyclerView.LayoutManager mLayoutManager = parent.getLayoutManager();
        if (mLayoutManager instanceof LinearLayoutManager) {
            outRect.bottom = mSpace;
            if (mTop != 0) {
                if (mPosition == 0) {
                    outRect.top = mTop;
                }
            }
        }
    }

}