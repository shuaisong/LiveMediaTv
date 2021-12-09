package com.tangmu.app.TengKuTV.adapter;

import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.bean.HomeChildBean;
import com.tangmu.app.TengKuTV.bean.HomeDubbingBean;
import com.tangmu.app.TengKuTV.bean.HomeDubbingRecBean;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.Util;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class HomeDubbingAdapter extends BaseMultiItemQuickAdapter<HomeDubbingBean, BaseViewHolder> implements View.OnFocusChangeListener {

    private final int itemHeight;
    private final int topTitleHeight;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public HomeDubbingAdapter(List<HomeDubbingBean> data) {
        super(data);
        addItemType(HomeDubbingBean.MOVIE, R.layout.item_dubbing);
        addItemType(HomeDubbingBean.TITLE, R.layout.item_dubbing_title);
        itemHeight = AutoSizeUtils.dp2px(CustomApp.getApp(), 300);
        topTitleHeight = AutoSizeUtils.dp2px(CustomApp.getApp(), 84);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeDubbingBean item) {
        helper.itemView.setOnFocusChangeListener(this);
        switch (item.getItemType()) {
            case HomeDubbingBean.TITLE:
//                if (item.getTitleBean().getS_title().equals("热门配音")) {
//                    helper.setVisible(R.id.more, true);
//                } else {
//                    helper.setVisible(R.id.more, false);
//                }
//                helper.setNestView(R.id.more);
                helper.setText(R.id.title, Util.showText(item.getTitleBean().getS_title(), item.getTitleBean().getS_title_z()));
                break;
            case HomeDubbingBean.MOVIE:
                HomeDubbingRecBean.VideoBean movieBean = item.getMovieBean();
                String imgUrl;
                if (movieBean.getType() == 1)//配音
                    imgUrl = Util.convertVideoPath(movieBean.getDv_img());
                else imgUrl = Util.convertImgPath(movieBean.getDv_img());
                GlideUtils.getRequest(mContext, imgUrl).placeholder(R.drawable.default_img)
                        .override(250,320).into((ImageView) helper.getView(R.id.image));
                helper.setText(R.id.title, Util.showText(movieBean.getDv_title(), movieBean.getDv_title_z()));
                break;
        }
    }

    @Override
    protected boolean isFixedViewType(int type) {
        if (type == HomeChildBean.TITLE) {
            return true;
        }
        return super.isFixedViewType(type);
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        LogUtil.e("onFocusChange");
        if (hasFocus) {
            RecyclerView recyclerView = getRecyclerView();
            if (recyclerView == null) return;
            int[] amount = getScrollAmount(v);//计算需要滑动的距离
            ViewParent parent = recyclerView.getParent().getParent();
            if (parent instanceof NestedScrollView) {
                ((NestedScrollView) parent).scrollBy(0, amount[1]);
            }
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
        out[1] = out[1] - itemHeight / 2 - topTitleHeight;
        return out;
    }
}
