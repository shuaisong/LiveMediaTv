package com.tangmu.app.TengKuTV.adapter;

import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.bean.HomeChildBean;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.Util;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.ScreenUtils;

public class HomeChildAdapter extends BaseMultiItemQuickAdapter<HomeChildBean, BaseViewHolder> implements View.OnFocusChangeListener {

    private final int radius;
    private boolean isVip;
    private int itemHeight;
    private int topTitleHeight;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public HomeChildAdapter(List<HomeChildBean> data) {
        super(data);
        addItemType(HomeChildBean.MOVIE, R.layout.item_home_movie_list);
        addItemType(HomeChildBean.TITLE, R.layout.item_home_title);
        CustomApp app = CustomApp.getApp();
        radius = AutoSizeUtils.dp2px(app, 5);
        itemHeight = AutoSizeUtils.dp2px(app, 300);
        topTitleHeight = AutoSizeUtils.dp2px(app, 84);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeChildBean item) {
        helper.itemView.setOnFocusChangeListener(this);
        switch (item.getItemType()) {
            case HomeChildBean.TITLE:
                if (isVip) {
                    helper.setTextColor(R.id.recommend_title, mContext.getResources().getColor(R.color.vip));
                }
                helper.setText(R.id.recommend_title, Util.showText(item.getTitleBean().getS_title(), item.getTitleBean().getS_title_z()));
                break;
            case HomeChildBean.MOVIE:
                HomeChildRecommendBean.VideoBean movieBean = item.getMovieBean();
                GlideUtils.getRequest(mContext, Util.convertImgPath(item.getMovieBean().getVm_img())).placeholder(R.mipmap.img_default)
                        .transform(new CenterCrop(), new RoundedCorners(radius))
                        .into((ImageView) helper.getView(R.id.image));
                if (isVip | movieBean.getVm_is_pay() == 2) {
                    helper.setVisible(R.id.vip, true);
                } else {
                    helper.setVisible(R.id.vip, false);
                }
//                if (movieBean.getVm_update_status() == 2) {
//                    helper.setText(R.id.endTime, mContext.getResources().getString(R.string.update_done));
//                } else
//                    helper.setText(R.id.endTime, String.format(mContext.getResources().getString(R.string.update_status), movieBean.getCount()));
                helper.setText(R.id.title, Util.showText(movieBean.getVm_title(), movieBean.getVm_title_z()));
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

    private boolean isellipsis(String title, TextView des) {
        TextPaint paint = des.getPaint();
        return paint.measureText(title) > des.getMeasuredWidth();
    }

    public void isVip() {
        this.isVip = true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
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
