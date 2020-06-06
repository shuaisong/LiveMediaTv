package com.tencent.liteav.demo.play.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.liteav.demo.play.R;
import com.tencent.liteav.demo.play.bean.VideoBean;
import com.tencent.liteav.demo.play.bean.VideoSortBean;
import com.tencent.liteav.demo.play.utils.AnthologyItemDecoration;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.ScreenUtils;

/**
 * Created by yuejiaoli on 2018/7/4.
 * <p>
 * 视频选集选择弹框
 * <p>
 */

public class TCVodAnthologyView extends LinearLayout {
    private static final String TAG = TCVodAnthologyView.class.getSimpleName();

    private Context mContext;
    private Callback mCallback;      // 回调
    private RecyclerView anthologyView;      // 选集listView
    private List<VideoBean> mList;          // 列表
    private int mClickPos = -1; // 当前的下表
    private boolean isNeedVip;
    private BaseQuickAdapter<VideoBean, BaseViewHolder> anthologyAdapter;
    private BaseQuickAdapter<VideoSortBean, BaseViewHolder> anthologyAdapter1;

    public TCVodAnthologyView(Context context) {
        super(context);
        init(context);
    }

    public TCVodAnthologyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TCVodAnthologyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mList = new ArrayList<VideoBean>();
        LayoutInflater.from(mContext).inflate(R.layout.player_antholgy_popup_view, this);
        anthologyView = (RecyclerView) findViewById(R.id.antholgyView);
        int screenWidth = ScreenUtils.getScreenSize(getContext())[0];
        int leftDivider = AutoSizeUtils.dp2px(getContext(), 48);
        int itemDivider = (screenWidth - leftDivider - 10 * AutoSizeUtils.dp2px(getContext(), 75)
                - AutoSizeUtils.dp2px(getContext(), 36)) / 9;
        anthologyView.addItemDecoration(new AnthologyItemDecoration(leftDivider, itemDivider / 2));
        // 选集listView
        RecyclerView anthologyView1 = (RecyclerView) findViewById(R.id.antholgyView1);
        anthologyView1.addItemDecoration(new AnthologyItemDecoration(leftDivider, itemDivider / 2));
        anthologyAdapter = new BaseQuickAdapter<VideoBean, BaseViewHolder>(R.layout.item_anthology, mList) {
            @Override
            protected void convert(BaseViewHolder helper, VideoBean item) {
                Log.e(TAG, "convert: " + item.getV_title_z());
                int v_episode_num = item.getV_episode_num();
                helper.setText(R.id.num, String.valueOf(v_episode_num));
                if (anthologyAdapter.getData().indexOf(item) == mClickPos) {
                    helper.setVisible(R.id.play, true).setVisible(R.id.num, false);
                } else helper.setVisible(R.id.play, false).setVisible(R.id.num, true);
                if (isNeedVip) {
                    helper.setVisible(R.id.ic_vip, true);
                    if (item.getV_is_pay() == 2) {
                        helper.setImageResource(R.id.ic_vip, R.mipmap.ic_vip_d);
                    } else
                        helper.setImageResource(R.id.ic_vip, R.mipmap.ic_xian_mian);
                } else {
                    if (item.getV_is_pay() == 2) {
                        helper.setVisible(R.id.ic_vip, true).setImageResource(R.id.ic_vip, R.mipmap.ic_vip_d);
                    } else
                        helper.setVisible(R.id.ic_vip, false);
                }
            }
        };
        anthologyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mCallback != null) {
                    if (mList != null && mList.size() > 0) {
                        VideoBean videoBean = mList.get(position);
                        if (videoBean != null && position != mClickPos) {
                            mCallback.onAnthologySelect(position);
                        }
                    }
                    mCallback.hide();
                }
                int prePos = mClickPos;
                mClickPos = position;
                anthologyAdapter.notifyItemChanged(prePos, "");
                anthologyAdapter.notifyItemChanged(mClickPos, "");
                anthologyAdapter1.notifyDataSetChanged();
            }
        });
        anthologyAdapter.setHasStableIds(true);
        anthologyView.setItemAnimator(null);
        anthologyView.setAdapter(anthologyAdapter);

        anthologyAdapter1 = new BaseQuickAdapter<VideoSortBean, BaseViewHolder>(R.layout.item_anthology) {
            @Override
            protected void convert(BaseViewHolder helper, VideoSortBean item) {
                VideoBean item1 = anthologyAdapter.getItem(mClickPos);
                if (item1 != null) {
                    if (item.getMaxVideo().getV_episode_num() >= item1.getV_episode_num() && item.getMinVideo().getV_episode_num() <= item1.getV_episode_num()) {
                        helper.setBackgroundRes(R.id.item_anthology, R.drawable.anthology_bg);
                    } else {
                        helper.setBackgroundRes(R.id.item_anthology, R.drawable.history4_bg);
                    }
                }
                helper.setVisible(R.id.ic_vip, false)
                        .setText(R.id.num, item.getMinVideo().getV_episode_num() + "-" + item.getMaxVideo().getV_episode_num());
            }
        };
        anthologyAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VideoSortBean item = anthologyAdapter1.getItem(position);
                if (item != null) {
                    int index = anthologyAdapter.getData().indexOf(item.getMinVideo());
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) anthologyView.getLayoutManager();
                    if (linearLayoutManager != null) {
                        linearLayoutManager.scrollToPositionWithOffset(index, 0);
                    }
                }
            }
        });
        anthologyView1.setAdapter(anthologyAdapter1);
    }

    /**
     * 设置回调
     *
     * @param callback
     */
    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    /**
     * 设置画质列表
     *
     * @param list
     */
    public void setVideoAnthologyList(List<VideoBean> list) {
        mList.clear();
        mList.addAll(list);
        int num = 0;
        VideoSortBean videoSortBean = null;
        ArrayList<VideoSortBean> videoSortBeans = new ArrayList<>();
        for (VideoBean videoBean : mList) {
            if (num == 0) {
                videoSortBean = new VideoSortBean();
                videoSortBean.setMinVideo(videoBean);
                videoSortBean.setMaxVideo(videoBean);
                videoSortBeans.add(videoSortBean);
                num++;
            } else if (num == 9) {
                videoSortBean.setMaxVideo(videoBean);
                num = 0;
            } else {
                videoSortBean.setMaxVideo(videoBean);
                num++;
            }
        }
        anthologyAdapter.notifyDataSetChanged();
        anthologyAdapter1.setNewData(videoSortBeans);
    }

    /**
     * 设置选中的选集
     *
     * @param position
     */
    public void setSelectedAnthology(int position) {
        if (position < 0) position = 0;
        mClickPos = position;
        anthologyAdapter.notifyDataSetChanged();
        anthologyAdapter1.notifyDataSetChanged();
    }

    public void setIsNeedVip(boolean b) {
        isNeedVip = b;
    }

    public void hide() {
        setVisibility(GONE);
    }

    public void show() {
        setVisibility(VISIBLE);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                View viewByPosition = anthologyAdapter.getViewByPosition(anthologyView, mClickPos, R.id.item_anthology);
                if (viewByPosition != null) {
                    viewByPosition.requestFocus();
                }
            }
        }, 200);

    }


    /**
     * 回调
     */
    public interface Callback {
        /**
         * 选集选择回调
         *
         * @param position
         */
        void onAnthologySelect(int position);

        void hide();
    }
}
