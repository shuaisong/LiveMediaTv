package com.tangmu.app.TengKuTV.module.live;

import android.content.Context;
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
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.bean.BannerBean;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
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
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.MovieItemDecoration;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class LiveFragment extends BaseFragment implements LiveContact.View, View.OnFocusChangeListener {
    @Inject
    LivePresenter presenter;
    @BindView(R.id.banner_title)
    TextView bannerTitle;
    @BindView(R.id.banner)
    Banner banner;
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
    private int itemHeight;
    private int topTitleHeight;
    private BannerClickListener bannerClickListener;
    private ViewPager bannerViewPager;

    public static LiveFragment newInstance(CategoryBean categoryBean) {
        return new LiveFragment();
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        itemHeight = AutoSizeUtils.dp2px(getActivity(), 300);
        topTitleHeight = AutoSizeUtils.dp2px(getActivity(), 84);
        swipeRefreshLayout.setRefreshing(true);
        presenter.getLiveReply();
    }

    @Override
    public void onDestroy() {
        if (superPlayer!=null) {
            superPlayer.resetPlayer();
        }
        presenter.detachView();
        LogUtil.e("直播"+"onDestroy");
        super.onDestroy();
    }

    @Override
    public void onStop() {
        LogUtil.e("直播"+"onStop");
        if (superPlayer!=null) {
            superPlayer.resetPlayer();
        }
        presenter.detachView();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (superPlayer!=null) {
            superPlayer.resetPlayer();
        }
        presenter.detachView();
        LogUtil.e("直播"+"onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        LogUtil.e("直播"+"onPause");
        if (superPlayer!=null) {
            if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PLAYING) {
                superPlayer.onPause();
            }
            superPlayer.resetPlayer();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e("直播"+"onResume");
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
                presenter.getLiveReply();
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
        superPlayer.findViewById(R.id.controller_small).setOnFocusChangeListener(this);
        initBanner();
    }


    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                GlideUtils.getRequest(imageView, Util.convertImgPath(((BannerBean) path).getB_img()))
                        .override(400,200).placeholder(R.drawable.default_img_banner).into(imageView);
            }
        });
        banner.setBannerTitles(null);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(4500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        bannerClickListener = new BannerClickListener(getActivity());
        banner.setOnBannerListener(bannerClickListener);
        bannerViewPager = banner.findViewById(R.id.bannerViewPager);
        banner.setOnFocusChangeListener(this);
    }

    private void initLiveHistoryList() {
        historyList.addItemDecoration(new MovieItemDecoration(getActivity()));
        liveHistoryAdapter = new BaseQuickAdapter<LiveReplayBean, BaseViewHolder>(R.layout.item_live_history) {
            @Override
            protected void convert(BaseViewHolder helper, LiveReplayBean item) {
                helper.itemView.setOnFocusChangeListener(LiveFragment.this);
                helper.setText(R.id.title, Util.showText(item.getL_title(), item.getL_title_z()))
                        .setText(R.id.time, item.getLr_add_time());
                GlideUtils.getRequest(getActivity(), Util.convertImgPath(item.getL_img()))
                        .override(250,320).placeholder(R.drawable.default_img)
                        .into((ImageView) helper.getView(R.id.image));
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
        this.liveBeans.clear();
        this.liveBeans.addAll(liveBeans);
        if (!liveBeans.isEmpty()) {
            LiveBean liveBean = liveBeans.get(0);
            if (liveBean.getL_live_type() == 1) {
                banner.setVisibility(View.GONE);
                superPlayer.setVisibility(View.VISIBLE);
                bannerTitle.setVisibility(View.VISIBLE);
                SuperPlayerModel superPlayerModel = new SuperPlayerModel();
                superPlayerModel.url = liveBean.getPull_url();
                superPlayer.playWithModel(superPlayerModel);
            } else {
                banner.setVisibility(View.VISIBLE);
                bannerTitle.setVisibility(View.GONE);
                superPlayer.setVisibility(View.GONE);
                presenter.getBanner();
            }
            if (liveBeans.size() > 2) {
                liveBean = liveBeans.get(2);
                live2.setVisibility(View.VISIBLE);
                GlideUtils.getRequest(this, Util.convertImgPath(liveBean.getL_img()))
                        .override(300,150).centerCrop().into(ivLive2);
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
                        .override(300,150).centerCrop().into(ivLive1);
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
            presenter.getBanner();
        }
    }


    @Override
    public void ShowBanner(List<BannerBean> result) {
        banner.setImages(result);
        bannerClickListener.setBannerBeans(result);
        banner.start();
    }

    @Override
    public void showLiveReply(List<LiveReplayBean> liveReplayBeans) {
        liveHistoryAdapter.setNewData(liveReplayBeans);
    }

    @Override
    public void showError(String msg) {
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        ToastUtil.showText(msg);
    }

    @OnClick({R.id.history1, R.id.history2, R.id.history3, R.id.search, R.id.collect, R.id.live1, R.id.live2, R.id.controller_small, R.id.banner})
    public void onViewClicked(View view) {
        Intent intent;
        PlayHistoryInfo playHistoryInfo;
        LiveBean liveBean;
        switch (view.getId()) {
            case R.id.banner:
                List<BannerBean> bannerBeans = bannerClickListener.getBannerBeans();
                if (bannerBeans == null || bannerBeans.isEmpty()) return;
                if (bannerClickListener != null)
                    bannerClickListener.OnBannerClick(banner.toRealPosition(bannerViewPager.getCurrentItem()));
                break;
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
            ViewParent parent = historyList.getParent().getParent();
            if (v.getId() == R.id.item_live_history) {
                int[] amount = getScrollAmount(v);//计算需要滑动的距离
                if (parent instanceof NestedScrollView) {
                    ((NestedScrollView) parent).scrollBy(0, amount[1]);
                }
            } else {
                ((NestedScrollView) parent).scrollTo(0, 0);
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
