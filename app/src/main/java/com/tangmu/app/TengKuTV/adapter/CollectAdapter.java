package com.tangmu.app.TengKuTV.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.bean.CollectBean;
import com.tangmu.app.TengKuTV.module.book.PlayBookActivity;
import com.tangmu.app.TengKuTV.module.dubbing.ShowDubbingVideoActivity;
import com.tangmu.app.TengKuTV.module.mine.MineCollectFocusChangeListener;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.Util;

import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.ScreenUtils;

public class CollectAdapter extends BaseQuickAdapter<CollectBean, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener {

    private final int itemWidth;
    private MineCollectFocusChangeListener focusChangeListener;

    public void setFocusChangeListener(MineCollectFocusChangeListener focusChangeListener) {
        this.focusChangeListener = focusChangeListener;
    }

    public CollectAdapter() {
        super(R.layout.item_record);
        CustomApp app = CustomApp.getApp();
        int i = AutoSizeUtils.dp2px(app, 175);
        itemWidth = (ScreenUtils.getScreenSize(app)[0] - i) / 5;
        setOnItemClickListener(this);
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectBean item) {
        ViewGroup.LayoutParams layoutParams = helper.itemView.getLayoutParams();
        layoutParams.width = itemWidth;
        helper.itemView.setLayoutParams(layoutParams);
        if (focusChangeListener != null) {
            helper.itemView.setOnFocusChangeListener(focusChangeListener);
        }
        helper.setText(R.id.title, Util.showText(item.getTitle(), item.getTitle_z()));
        if (item.getUc_type() == 4) {
            GlideUtils.getRequest(mContext, Util.convertVideoPath(item.getImg()))
                    .centerCrop().override(167,255).into((ImageView) helper.getView(R.id.image));
        } else
            GlideUtils.getRequest(mContext, Util.convertImgPath(item.getImg()))
                    .centerCrop().override(167,255).into((ImageView) helper.getView(R.id.image));
        if (item.getVm_type() == 1) {
            helper.setImageResource(R.id.isVip, R.mipmap.icon_fufei);
        } else {
            helper.setImageResource(R.id.isVip, R.mipmap.vip_tag_bg);
        }
        if (item.getVm_is_pay() == 2) {
            helper.setVisible(R.id.isVip, true);
        } else {
            helper.setVisible(R.id.isVip, false);
        }
        if (item.getVm_update_status() == 2) {
            helper.setText(R.id.update_status, mContext.getResources().getString(R.string.update_done));
        } else if (item.getVm_update_status() == 1)
            helper.setText(R.id.update_status, String.format(mContext.getResources().getString(R.string.update_status), item.getCount()));

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CollectBean item = getItem(position);
        if (item == null) return;
        Intent intent = null;
        switch (item.getUc_type()) {
            case 1:
                if (item.getVm_type() == 2) {
                    intent = new Intent(mContext, TVDetailActivity.class);
                } else
                    intent = new Intent(mContext, MovieDetailActivity.class);
                intent.putExtra("id", item.getUc_audio_id());
                intent.putExtra("c_id", item.getVt_id_one());
                break;
            case 3:
                intent = new Intent(mContext, PlayBookActivity.class);
                intent.putExtra("id", item.getUc_audio_id());
                break;
            case 4:
                intent = new Intent(mContext, ShowDubbingVideoActivity.class);
                intent.putExtra("id", item.getUc_audio_id());
                break;
        }
        if (intent != null) mContext.startActivity(intent);
    }
}
