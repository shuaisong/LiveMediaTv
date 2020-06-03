package com.tangmu.app.TengKuTV.module.live;

import android.content.Intent;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.bean.BannerBean;
import com.tangmu.app.TengKuTV.bean.LiveBean;
import com.tangmu.app.TengKuTV.bean.LiveReplayBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerFragmentComponent;
import com.tangmu.app.TengKuTV.contact.LiveContact;
import com.tangmu.app.TengKuTV.db.PlayHistoryInfo;
import com.tangmu.app.TengKuTV.db.PlayHistoryManager;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.tangmu.app.TengKuTV.module.playhistory.PlayHistoryActivity;
import com.tangmu.app.TengKuTV.module.search.VideoSearchActivity;
import com.tangmu.app.TengKuTV.presenter.LivePresenter;
import com.tangmu.app.TengKuTV.utils.BannerClickListener;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.MovieItemDecoration;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.CustomLoadMoreView;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerView;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.ScreenUtils;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class LiveFragment extends BaseFragment implements LiveContact.View, View.OnFocusChangeListener {
    @Inject
    LivePresenter presenter;
    @BindView(R.id.bannerTitle)
    TextView bannerTitle;
    @BindView(R.id.banner_img)
    ImageView bannerImg;
    @BindView(R.id.historyList)
    RecyclerView historyList;
    @BindView(R.id.swip)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tv_history1)
    TextView tvHistory1;
    @BindView(R.id.tv_history1_progress)
    TextView tvHistory1Progress;
    @BindView(R.id.history1)
    LinearLayout history1;
    @BindView(R.id.tv_history2)
    TextView tvHistory2;
    @BindView(R.id.tv_history2_progress)
    TextView tvHistory2Progress;
    @BindView(R.id.history2)
    LinearLayout history2;
    @BindView(R.id.history3)
    TextView history3;
    @BindView(R.id.search)
    LinearLayout search;
    @BindView(R.id.collect)
    LinearLayout collect;
    @BindView(R.id.iv_live1)
    ImageView ivLive1;
    @BindView(R.id.live_type1)
    TextView liveType1;
    @BindView(R.id.live1)
    FrameLayout live1;
    @BindView(R.id.iv_live2)
    ImageView ivLive2;
    @BindView(R.id.live_type2)
    TextView liveType2;
    @BindView(R.id.live2)
    FrameLayout live2;
    @BindView(R.id.superPlayer)
    SuperPlayerView superPlayer;
    private BaseQuickAdapter<LiveReplayBean, BaseViewHolder> liveHistoryAdapter;
    private List<PlayHistoryInfo> allVideo;
    private ArrayList<LiveBean> liveBeans = new ArrayList<>();
    private int page = 1;
    private int itemHeight;
    private int topTitleHeight;
    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        itemHeight = AutoSizeUtils.dp2px(getActivity(), 300);
        topTitleHeight = AutoSizeUtils.dp2px(getActivity(), 84);
        swipeRefreshLayout.setRefreshing(true);
        presenter.getLiveReply(page);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PLAYING) {
            superPlayer.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!liveBeans.isEmpty()) {
            if (liveBeans.get(0).getL_live_type() == 1 && superPlayer.getPlayState() != SuperPlayerConst.PLAYSTATE_PLAYING)
                superPlayer.onResume();
        }
        presenter.getTopLive();
        initHistory();
    }

    /**
     * 初始化view
     */
    @Override
    protected void initView() {
        presenter.attachView(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getTopLive();
                page = 1;
                presenter.getLiveReply(page);
            }
        });
        superPlayer.disableBottom();
        initLiveHistoryList();
        history1.setOnFocusChangeListener(this);
        history2.setOnFocusChangeListener(this);
        history3.setOnFocusChangeListener(this);
        search.setOnFocusChangeListener(this);
        collect.setOnFocusChangeListener(this);
        live1.setOnFocusChangeListener(this);
        live2.setOnFocusChangeListener(this);
        bannerImg.setOnFocusChangeListener(this);
        superPlayer.findViewById(R.id.controller_small).setOnFocusChangeListener(this);
    }

    private void initLiveHistoryList() {
        historyList.addItemDecoration(new MovieItemDecoration(getActivity()));
        liveHistoryAdapter = new BaseQuickAdapter<LiveReplayBean, BaseViewHolder>(R.layout.item_live_history) {
            @Override
            protected void convert(BaseViewHolder helper, LiveReplayBean item) {
                helper.itemView.setOnFocusChangeListener(LiveFragment.this);
                helper.setText(R.id.title, Util.showText(item.getL_title(), item.getL_title_z()))
                        .setText(R.id.time, item.getLr_add_time());
                GlideUtils.getRequest(getActivity(), Util.convertImgPath(item.getL_img())).transform(new CenterCrop(), new RoundedCorners(AutoSizeUtils.dp2px(getActivity()
                        , 5))).into((ImageView) helper.getView(R.id.image));
            }
        };
        liveHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LiveReplayBean item = liveHistoryAdapter.getItem(position);
                if (item != null) {
                    Intent intent = new Intent(getActivity(), HistoryLiveActivity.class);
                    intent.putExtra("id", item.getLr_id());
                    intent.putExtra("roomId", item.getLr_room_id());
                    startActivity(intent);
                }
            }
        });
        liveHistoryAdapter.setLoadMoreView(new CustomLoadMoreView());
        liveHistoryAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.getLiveReply(page);
            }
        }, historyList);
        historyList.setAdapter(liveHistoryAdapter);
    }

    /**
     * @return xml id
     */
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_live;
    }

    /**
     * mvp 关联
     *
     * @param appComponent appComponent
     */
    @Override
    protected void setupFragComponent(AppComponent appComponent) {
        DaggerFragmentComponent.builder().appComponent(appComponent).build().inject(this);
    }


    @Override
    public void showTopLive(List<LiveBean> liveBeans) {
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);

        for (int i = 0; i < liveBeans.size(); i++) {
            if (liveBeans.get(i).getL_live_type() == 1) {
                if (i - 1 >= 0) {
                    if (liveBeans.get(i).getL_live_type() != 1) {
                        Collections.swap(liveBeans, i, i - 1);
                    }
                }
            }
        }
        this.liveBeans.addAll(liveBeans);
        if (!liveBeans.isEmpty()) {
            LiveBean liveBean = liveBeans.get(0);
            if (liveBean.getL_live_type() == 1) {
                bannerTitle.setText(getString(R.string.live));
                SuperPlayerModel superPlayerModel = new SuperPlayerModel();
                superPlayerModel.url = liveBean.getPull_url();
                superPlayer.playWithModel(superPlayerModel);
                bannerImg.setVisibility(View.INVISIBLE);
            } else {
                bannerImg.setVisibility(View.VISIBLE);
                bannerTitle.setText(getString(R.string.look_back));
                GlideUtils.getRequest(this, Util.convertImgPath(liveBean.getL_img()))
                        .centerCrop().into(bannerImg);
            }
            if (liveBeans.size() > 2) {
                liveBean = liveBeans.get(2);
                live2.setVisibility(View.VISIBLE);
                GlideUtils.getRequest(this, Util.convertImgPath(liveBean.getL_img()))
                        .centerCrop().into(ivLive2);
                if (liveBean.getL_live_type() == 1) {
                    liveType2.setText(getString(R.string.live));
                } else {
                    liveType2.setText(getString(R.string.look_back));
                }
            }
            if (liveBeans.size() > 1) {
                liveBean = liveBeans.get(1);
                live1.setVisibility(View.VISIBLE);
                GlideUtils.getRequest(this, Util.convertImgPath(liveBean.getL_img()))
                        .centerCrop().into(ivLive1);
                if (liveBean.getL_live_type() == 1) {
                    liveType1.setText(getString(R.string.live));
                } else {
                    liveType1.setText(getString(R.string.look_back));
                }
            }

        } else {
            live1.setVisibility(View.INVISIBLE);
            live2.setVisibility(View.INVISIBLE);
            bannerTitle.setVisibility(View.INVISIBLE);
            superPlayer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showLiveReply(List<LiveReplayBean> liveReplayBeans) {
        if (page == 1) {
            liveHistoryAdapter.setNewData(liveReplayBeans);
        } else {
            liveHistoryAdapter.getData().addAll(liveReplayBeans);
            if (liveReplayBeans.size() < 20) {
                liveHistoryAdapter.loadMoreEnd();
            } else liveHistoryAdapter.loadMoreComplete();
        }
        page++;
    }

    @Override
    public void showError(String msg) {
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        ToastUtil.showText(msg);
    }

    @OnClick({R.id.history1, R.id.history2, R.id.history3, R.id.search, R.id.collect, R.id.live1, R.id.live2, R.id.banner_img, R.id.controller_small})
    public void onViewClicked(View view) {
        Intent intent;
        PlayHistoryInfo playHistoryInfo;
        LiveBean liveBean;
        switch (view.getId()) {
            case R.id.history1:
                playHistoryInfo = allVideo.get(0);
                if (playHistoryInfo.getVm_type() == 2) {
                    intent = new Intent(getActivity(), TVDetailActivity.class);
                } else
                    intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("id", playHistoryInfo.getB_id());
                intent.putExtra("c_id", playHistoryInfo.getB_id_one());
                startActivity(intent);
                break;
            case R.id.history2:
                playHistoryInfo = allVideo.get(1);
                if (playHistoryInfo.getVm_type() == 2) {
                    intent = new Intent(getActivity(), TVDetailActivity.class);
                } else
                    intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("id", playHistoryInfo.getB_id());
                intent.putExtra("c_id", playHistoryInfo.getB_id_one());
                startActivity(intent);
                break;
            case R.id.search:
                startActivity(new Intent(getActivity(), VideoSearchActivity.class));
                break;
            case R.id.history3:
                intent = new Intent(getActivity(), PlayHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.collect:
                if (isClickLogin()) {
                    intent = new Intent(getActivity(), PlayHistoryActivity.class);
                    intent.putExtra("position", 1);
                    startActivity(intent);
                }
                break;
            case R.id.controller_small:
                liveBean = liveBeans.get(0);
                if (liveBean.getL_live_type() == 1) {
                    intent = new Intent(getActivity(), LivingActivity.class);
                    intent.putExtra("url", liveBean.getPull_url());
                    intent.putExtra("url1", liveBean.getPull_url1());
                    intent.putExtra("url2", liveBean.getPull_url2());
                    intent.putExtra("id", liveBean.getL_room_id());
                } else {
                    intent = new Intent(getActivity(), HistoryLiveActivity.class);
                    intent.putExtra("id", liveBean.getLr_id());
                }
                startActivity(intent);
                break;
            case R.id.banner_img:
                liveBean = liveBeans.get(0);
                intent = new Intent(getActivity(), HistoryLiveActivity.class);
                intent.putExtra("id", liveBean.getLr_id());
                startActivity(intent);
                break;
            case R.id.live1:
                liveBean = liveBeans.get(1);
                if (liveBean.getL_live_type() == 1) {
                    intent = new Intent(getActivity(), LivingActivity.class);
                    intent.putExtra("url", liveBean.getPull_url());
                    intent.putExtra("url1", liveBean.getPull_url1());
                    intent.putExtra("url2", liveBean.getPull_url2());
                    intent.putExtra("id", liveBean.getL_room_id());
                } else {
                    intent = new Intent(getActivity(), HistoryLiveActivity.class);
                    intent.putExtra("id", liveBean.getLr_id());
                }
                startActivity(intent);
                break;
            case R.id.live2:
                liveBean = liveBeans.get(2);
                if (liveBean.getL_live_type() == 1) {
                    intent = new Intent(getActivity(), LivingActivity.class);
                    intent.putExtra("url", liveBean.getPull_url());
                    intent.putExtra("url1", liveBean.getPull_url1());
                    intent.putExtra("url2", liveBean.getPull_url2());
                    intent.putExtra("id", liveBean.getL_room_id());
                } else {
                    intent = new Intent(getActivity(), HistoryLiveActivity.class);
                    intent.putExtra("id", liveBean.getLr_id());
                }
                startActivity(intent);
                break;
        }
    }

    private void initHistory() {
        allVideo = PlayHistoryManager.getAllVideo();
        if (allVideo.isEmpty()) {
            history1.setVisibility(View.GONE);
            history2.setVisibility(View.GONE);
        } else if (allVideo.size() == 1) {
            history1.setVisibility(View.VISIBLE);
            history2.setVisibility(View.GONE);
            PlayHistoryInfo playHistoryInfo = allVideo.get(0);
            tvHistory1Progress.setText(String.format(Locale.CHINA, getString(R.string.haveViewed), playHistoryInfo.getB_progress()));
            tvHistory1.setText(String.format(Locale.CHINA, getString(R.string.play_history_title), Util.showText(playHistoryInfo.getB_title(),
                    playHistoryInfo.getB_title_z()), playHistoryInfo.getB_position() + 1));
        } else {
            history1.setVisibility(View.VISIBLE);
            history2.setVisibility(View.VISIBLE);
            PlayHistoryInfo playHistoryInfo = allVideo.get(0);
            tvHistory1Progress.setText(String.format(Locale.CHINA, getString(R.string.haveViewed), playHistoryInfo.getB_progress()));
            tvHistory1.setText(String.format(Locale.CHINA, getString(R.string.play_history_title), Util.showText(playHistoryInfo.getB_title(),
                    playHistoryInfo.getB_title_z()), playHistoryInfo.getB_position() + 1));
            playHistoryInfo = allVideo.get(1);
            tvHistory2Progress.setText(String.format(Locale.CHINA, getString(R.string.haveViewed), playHistoryInfo.getB_progress()));
            tvHistory2.setText(String.format(Locale.CHINA, getString(R.string.play_history_title), Util.showText(playHistoryInfo.getB_title(),
                    playHistoryInfo.getB_title_z()), playHistoryInfo.getB_position() + 1));
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            int[] amount = getScrollAmount(v);//计算需要滑动的距离
            ViewParent parent = historyList.getParent().getParent();
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
