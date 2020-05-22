package com.tangmu.app.TengKuTV.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.ScreenUtils;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class BookItemDecoration extends RecyclerView.ItemDecoration {

    private final int space;
    private final int bottom;
    private final int left_0;

    public BookItemDecoration(Context context) {
        bottom = AutoSizeUtils.dp2px(context, 9);
        left_0 = AutoSizeUtils.dp2px(context, 22);
        space = (ScreenUtils.getScreenSize(context)[0] - left_0 * 2 - AutoSizeUtils.dp2px(context, 80) * 3) / 4;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int position = parent.getChildAdapterPosition(view);
        if (layoutManager instanceof GridLayoutManager) {
            if (position % 3 == 0) {
                outRect.left = left_0;
                outRect.right = space;
            } else if (position % 3 == 2) {
                outRect.left = space;
                outRect.right = left_0;
            } else {
                outRect.left = space;
                outRect.right = space;
            }
            outRect.bottom = bottom;
        }
    }
}
