package com.tangmu.app.TengKuTV.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by lenovo on 2020/2/26.
 * auther:lenovo
 * Date：2020/2/26
 */
public class CollectItemDecoration extends RecyclerView.ItemDecoration {


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            RecyclerView.Adapter adapter = parent.getAdapter();
            if (adapter != null) {
                int mPosition = parent.getChildAdapterPosition(view);
                if (mPosition == 0) {
                    outRect.top = 0;
                } else outRect.top = AutoSizeUtils.dp2px(parent.getContext(), 32);
            }


        }
    }
}
