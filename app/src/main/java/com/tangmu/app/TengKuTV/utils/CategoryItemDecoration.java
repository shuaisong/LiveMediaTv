package com.tangmu.app.TengKuTV.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class CategoryItemDecoration extends RecyclerView.ItemDecoration {

    private final int left_0;
    private final int space;

    public CategoryItemDecoration(Context context) {
        left_0 = AutoSizeUtils.dp2px(context, 24);
        space = AutoSizeUtils.dp2px(context, 11);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            RecyclerView.Adapter adapter = parent.getAdapter();
            if (adapter != null) {
                int mPosition = parent.getChildAdapterPosition(view);
                if (mPosition == 0) {
                    outRect.left = left_0;
                    outRect.right = space;
                } else if (mPosition == adapter.getItemCount() - 1) {
                    outRect.right = left_0;
                    outRect.left = space;
                } else {
                    outRect.left = space;
                    outRect.right = space;
                }
            }


        }
    }
}
