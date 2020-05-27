package com.tangmu.app.TengKuTV.module.home;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.db.PlayHistoryInfo;
import com.tangmu.app.TengKuTV.db.PlayHistoryManager;
import com.tangmu.app.TengKuTV.module.book.PlayBookActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieListActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.Util;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;

public class HomeVideoHistoryFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private BaseQuickAdapter<PlayHistoryInfo, BaseViewHolder> videoHistoryAdapter;
    private int clickPosition;

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        videoHistoryAdapter = new BaseQuickAdapter<PlayHistoryInfo, BaseViewHolder>(R.layout.item_history, getData()) {
            @Override
            protected void convert(BaseViewHolder helper, PlayHistoryInfo playHistoryInfo) {
                helper.setText(R.id.title, Util.showText(playHistoryInfo.getB_title(), playHistoryInfo.getB_title_z()))
                        .setText(R.id.tv_progress, String.format(Locale.CHINA, mContext.getResources().getString(R.string.Watched),
                                playHistoryInfo.getB_progress()))
                        .setProgress(R.id.progress, playHistoryInfo.getB_progress());
                GlideUtils.getRequest(mContext, Util.convertImgPath(playHistoryInfo.getB_img()))
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.image));
            }
        };
        videoHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PlayHistoryInfo item = videoHistoryAdapter.getItem(position);
                if (item == null) return;
                clickPosition = position;
                Intent intent;
                if (item.getB_type() == 1) {
                    if (item.getVm_type() == 2) {
                        intent = new Intent(getActivity(), TVDetailActivity.class);
                    } else
                        intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtra("id", item.getB_id());
                    intent.putExtra("c_id", item.getB_id_one());
                } else {
                    intent = new Intent(getActivity(), PlayBookActivity.class);
                    intent.putExtra("id", item.getB_id());
                }
                startActivityForResult(intent, 100);
            }
        });
        TextView textView = new TextView(this.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(17);
        textView.setTextColor(Color.WHITE);
        textView.setText(getResources().getString(R.string.nodata));
        textView.setLayoutParams(layoutParams);
        videoHistoryAdapter.setEmptyView(textView);
        videoHistoryAdapter.isUseEmpty(true);
        recyclerview.setAdapter(videoHistoryAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            PlayHistoryInfo item = videoHistoryAdapter.getItem(clickPosition);
            if (item != null) {
                PlayHistoryInfo history = PlayHistoryManager.getHistory(item.getB_id(), item.getB_type());
                if (clickPosition == 0) {
                    videoHistoryAdapter.getData().set(clickPosition, history);
                    videoHistoryAdapter.notifyItemChanged(0, "");
                    return;
                }
                videoHistoryAdapter.getData().remove(clickPosition);
                videoHistoryAdapter.getData().add(0, item);
                videoHistoryAdapter.notifyItemRemoved(clickPosition);
                videoHistoryAdapter.notifyItemInserted(0);
                recyclerview.scrollToPosition(0);
            }

        }
    }

    private List<PlayHistoryInfo> getData() {
        return PlayHistoryManager.getAll();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home_video_history;
    }

    @Override
    protected void setupFragComponent(AppComponent appComponent) {

    }
}
