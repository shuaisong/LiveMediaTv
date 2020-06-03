package com.tencent.liteav.demo.play.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.liteav.demo.play.R;
import com.tencent.liteav.demo.play.bean.TCVideoQuality;
import com.tencent.liteav.demo.play.utils.AnthologyItemDecoration;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by yuejiaoli on 2018/7/4.
 * <p>
 * 视频画质选择弹框
 * <p>
 * 1、设置画质列表{@link #setVideoQualityList(List)}
 * <p>
 * 2、设置默认选中的画质{@link #setDefaultSelectedQuality(int)}
 */

public class TCVodQualityView extends FrameLayout {
    private Context mContext;
    private Callback mCallback;      // 回调
    private RecyclerView mListView;      // 画质listView
    private QualityAdapter mAdapter;       // 画质列表适配器
    private List<TCVideoQuality> mList;          // 画质列表
    private int mClickPos = -1; // 当前的画质下表
    private int defaultQualityIndex = -1;

    public void setVideoQualityCallback(VideoQualityCallback videoQualityCallback) {
        this.videoQualityCallback = videoQualityCallback;
    }

    private VideoQualityCallback videoQualityCallback;

    public TCVodQualityView(Context context) {
        super(context);
        init(context);
    }

    public TCVodQualityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TCVodQualityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mList = new ArrayList<TCVideoQuality>();
        LayoutInflater.from(mContext).inflate(R.layout.player_quality_popup_view, this);
        mListView = (RecyclerView) findViewById(R.id.lv_quality);
        int leftDivider = AutoSizeUtils.dp2px(getContext(), 48);
        int itemDivider = AutoSizeUtils.dp2px(getContext(), 32);
        mListView.addItemDecoration(new AnthologyItemDecoration(leftDivider, itemDivider));
        mAdapter = new QualityAdapter(R.layout.player_quality_item_view, mList);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mList != null && mList.size() > 0) {
                    TCVideoQuality quality = mList.get(position);
                    if (quality != null && position != mClickPos) {
                        if (videoQualityCallback != null) {
                            if (videoQualityCallback.onQualitySelect(quality)) {
                                return;
                            }
                        }
                        if (mCallback != null) {
                            mCallback.onQualitySelect(quality);
                        }
                    }
                }
                int preClick = mClickPos;
                mClickPos = position;
                mAdapter.notifyItemChanged(preClick, "");
                mAdapter.notifyItemChanged(mClickPos, "");
            }
        });
        mListView.setAdapter(mAdapter);
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
    public void setVideoQualityList(List<TCVideoQuality> list) {
        mList.clear();
        for (TCVideoQuality tcVideoQuality : list) {
            setQualityTitle(tcVideoQuality);
        }
        mList.addAll(list);
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).index == defaultQualityIndex) {
                mClickPos = i;
            }
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            View viewByPosition = mAdapter.getViewByPosition(mListView, mClickPos, R.id.item_quality);
            if (viewByPosition != null) {
                viewByPosition.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewByPosition.requestFocus();
                    }
                }, 200);
            }
        }
    }

    private void setQualityTitle(TCVideoQuality tcVideoQuality) {
        if (!TextUtils.isEmpty(tcVideoQuality.name))
            switch (tcVideoQuality.name) {
                case "FLU":
                    tcVideoQuality.title = getResources().getString(R.string._180p);
                    break;
                case "SD":
                    tcVideoQuality.title = getResources().getString(R.string._270p);
                    break;
                case "HD":
                    tcVideoQuality.title = getResources().getString(R.string._480p);
                    break;
                case "FHD":
                    tcVideoQuality.title = getResources().getString(R.string._720p);
                    break;
            }
    }

    /**
     * 设置默认选中的清晰度
     *
     * @param index
     */
    public void setDefaultSelectedQuality(int index) {
        defaultQualityIndex = index;
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }

    class QualityAdapter extends BaseQuickAdapter<TCVideoQuality, BaseViewHolder> {


        public QualityAdapter(int layoutResId, List<TCVideoQuality> mList) {
            super(layoutResId, mList);
        }

        @Override
        protected void convert(BaseViewHolder helper, TCVideoQuality item) {
            if (!TextUtils.isEmpty(item.name)) {
                if (item.name.equals("FLU")) {
                    helper.setGone(R.id.image, false);
                } else if (item.name.equals("SD")) {
                    helper.setGone(R.id.image, false);
                } else if (item.name.equals("HD")) {
                    helper.setGone(R.id.image, false);
                } else if (item.name.equals("FHD")) {
                    helper.setGone(R.id.image, false);
                } else if (item.name.equals("2K")) {
                    helper.setGone(R.id.image, true).setImageResource(R.id.image, R.mipmap.need_login);
                } else if (item.name.equals("4K")) {
                    helper.setGone(R.id.image, true).setImageResource(R.id.image, R.mipmap.need_vip);
                }
            } else {
                helper.setGone(R.id.image, false);
            }
            if (mClickPos == mList.indexOf(item)) {
                helper.setBackgroundRes(R.id.item_quality, R.drawable.full_bottom_radio_chceked);
            } else {
                helper.setBackgroundRes(R.id.item_quality, R.drawable.history4_bg);
            }
            helper.setText(R.id.tv_quality, item.title);
        }


    }

    /**
     * 画质item view
     */
    class QualityItemView extends RelativeLayout {

        private TextView mTvQuality;

        public QualityItemView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init(context);
        }

        public QualityItemView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public QualityItemView(Context context) {
            super(context);
            init(context);
        }

        private void init(Context context) {
            LayoutInflater.from(context).inflate(R.layout.player_quality_item_view, this);
            mTvQuality = (TextView) findViewById(R.id.tv_quality);
        }

        /**
         * 设置画质名称
         *
         * @param qualityName
         */
        public void setQualityName(String qualityName) {
            mTvQuality.setText(qualityName);
        }

        /**
         * 设置画质item是否为选择状态
         *
         * @param isChecked
         */
        public void setSelected(boolean isChecked) {
            mTvQuality.setSelected(isChecked);
        }
    }

    /**
     * 回调
     */
    public interface Callback {
        /**
         * 画质选择回调
         *
         * @param quality
         */
        void onQualitySelect(TCVideoQuality quality);
    }

    /**
     * 回调
     */
    public interface VideoQualityCallback {
        /**
         * 画质选择回调
         *
         * @param quality
         * @return
         */
        boolean onQualitySelect(TCVideoQuality quality);//是否需要登录和充值
    }

}
