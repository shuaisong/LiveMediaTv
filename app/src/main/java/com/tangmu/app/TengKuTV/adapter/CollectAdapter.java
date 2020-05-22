package com.tangmu.app.TengKuTV.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.bean.CollectBean;
import com.tangmu.app.TengKuTV.module.book.PlayBookActivity;
import com.tangmu.app.TengKuTV.module.dubbing.ShowDubbingVideoActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.Util;

public class CollectAdapter extends BaseQuickAdapter<CollectBean, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener {
    public CollectAdapter() {
        super(R.layout.item_record);
        setOnItemClickListener(this);
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectBean item) {
        helper.setText(R.id.title, Util.showText(item.getTitle(), item.getTitle_z()));
        if (item.getUc_type() == 4) {
            GlideUtils.getRequest(mContext, Util.convertVideoPath(item.getImg()))
                    .centerCrop().into((ImageView) helper.getView(R.id.image));
        } else
            GlideUtils.getRequest(mContext, Util.convertImgPath(item.getImg()))
                    .centerCrop().into((ImageView) helper.getView(R.id.image));
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
