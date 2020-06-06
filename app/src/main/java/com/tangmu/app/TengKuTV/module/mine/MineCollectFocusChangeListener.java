package com.tangmu.app.TengKuTV.module.mine;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.tangmu.app.TengKuTV.CustomApp;

import me.jessyan.autosize.utils.ScreenUtils;

public class MineCollectFocusChangeListener implements View.OnFocusChangeListener {
    public MineCollectFocusChangeListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    private RecyclerView recyclerView;

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            int[] scrollAmount = getScrollAmount(v);
            recyclerView.scrollBy(scrollAmount[0], 0);
        }
    }

    /**
     * 计算需要滑动的距离,使焦点在滑动中始终居中
     *
     * @param view
     */
    private int[] getScrollAmount(View view) {
        int[] out = new int[2];
        view.getLocationOnScreen(out);
        int[] screenSize = ScreenUtils.getScreenSize(CustomApp.getApp());
        out[0] = out[0] - screenSize[0] / 2;
        return out;
    }
}
