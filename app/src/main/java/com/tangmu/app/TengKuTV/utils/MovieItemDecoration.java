package com.tangmu.app.TengKuTV.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class MovieItemDecoration extends RecyclerView.ItemDecoration {

    private final int space;
    private final int bottom;

    public MovieItemDecoration(Context context) {
        bottom = AutoSizeUtils.dp2px(context, 11);
        space = AutoSizeUtils.dp2px(context, 8);
    }

    public MovieItemDecoration(int space, int bottom) {
        this.space = space;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = bottom;
            /*int spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            int childAdapterPosition = parent.getChildAdapterPosition(view);
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = bottom;
            if (childAdapterPosition % spanCount == 0) {
                outRect.left = 0;
            } else if (childAdapterPosition % spanCount == spanCount - 1) {
                outRect.right = 0;
            }*/
        }
    }
}
