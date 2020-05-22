package com.tangmu.app.TengKuTV.view;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.tangmu.app.TengKuTV.R;


public class CustomLoadMoreView extends LoadMoreView {
    private boolean endVisible = true;

    public void setEndVisible(boolean endVisible) {
        this.endVisible = endVisible;
    }

    @Override
    public int getLayoutId() {
        return R.layout.load_more_view;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return endVisible ? R.id.load_more_load_end_view : 0;
    }

}
