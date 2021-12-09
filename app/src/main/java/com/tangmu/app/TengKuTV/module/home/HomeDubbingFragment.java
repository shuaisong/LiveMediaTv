package com.tangmu.app.TengKuTV.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.adapter.HomeDubbingAdapter;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.bean.BannerBean;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.bean.DubbingListBean;
import com.tangmu.app.TengKuTV.bean.HomeDubbingBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerFragmentComponent;
import com.tangmu.app.TengKuTV.contact.HomeDubbingContact;
import com.tangmu.app.TengKuTV.db.PlayHistoryInfo;
import com.tangmu.app.TengKuTV.db.PlayHistoryManager;
import com.tangmu.app.TengKuTV.module.book.BookActivity;
import com.tangmu.app.TengKuTV.module.dubbing.ShowDubbingVideoActivity;
import com.tangmu.app.TengKuTV.module.main.MainActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.tangmu.app.TengKuTV.module.playhistory.PlayHistoryActivity;
import com.tangmu.app.TengKuTV.module.search.VideoSearchActivity;
import com.tangmu.app.TengKuTV.presenter.HomeDubbingPresenter;
import com.tangmu.app.TengKuTV.utils.BannerClickListener;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.MovieItemDecoration;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

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
public class HomeDubbingFragment extends BaseFragment implements HomeDubbingContact.View, View.OnFocusChangeListener {
    @Inject
    HomeDubbingPresenter presenter;
    @BindView(R.id.recommend_recyclerview)
    RecyclerView mRecommendRecyclerview;
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
    @BindView(R.id.live)
    LinearLayout live;
    @BindView(R.id.book)
    LinearLayout book;
    @BindView(R.id.banner)
    Banner banner;
    private CategoryBean categoryBean;
    private List<BannerBean> bannerBeans;
    private List<PlayHistoryInfo> allVideo;
    private BannerClickListener bannerClickListener;
    private BaseQuickAdapter<DubbingListBean, BaseViewHolder> homeDubbingAdapter;
    private int page = 1;
    private ViewPager bannerViewPager;
    private int itemHeight;
    private int topTitleHeight;

    public HomeDubbingFragment() {
    }

    public static HomeDubbingFragment newInstance(CategoryBean categoryBean) {
        HomeDubbingFragment homeDubbingFragment = new HomeDubbingFragment();
        Bundle args = new Bundle();
        args.putSerializable("Category", categoryBean);
        homeDubbingFragment.setArguments(args);
        return homeDubbingFragment;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        itemHeight = AutoSizeUtils.dp2px(getActivity(), 300);
        topTitleHeight = AutoSizeUtils.dp2px(getActivity(), 84);
        swipeRefreshLayout.setRefreshing(true);
        categoryBean = (CategoryBean) getArguments().getSerializable("Category");
        if (categoryBean != null) {
            presenter.getBanner(categoryBean.getVt_id());
        }
        presenter.getDubbing(page);
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
                presenter.getBanner(categoryBean.getVt_id());
                page = 1;
                presenter.getDubbing(page);
            }
        });
        initRecycer();
        initBanner();
        history1.setOnFocusChangeListener(this);
        history2.setOnFocusChangeListener(this);
        history3.setOnFocusChangeListener(this);
        search.setOnFocusChangeListener(this);
        collect.setOnFocusChangeListener(this);
        live.setOnFocusChangeListener(this);
        book.setOnFocusChangeListener(this);
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

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    private void initRecycer() {
        mRecommendRecyclerview.addItemDecoration(new MovieItemDecoration(getContext()));
        int radius = AutoSizeUtils.dp2px(CustomApp.getApp(), 5);
        homeDubbingAdapter = new BaseQuickAdapter<DubbingListBean, BaseViewHolder>(R.layout.item_dubbing) {
            @Override
            protected void convert(BaseViewHolder helper, DubbingListBean item) {
                helper.itemView.setOnFocusChangeListener(HomeDubbingFragment.this);
                GlideUtils.getRequest(mContext, Util.convertVideoPath(item.getUw_img())).placeholder(R.drawable.default_img)
                        .override(250,320).into((ImageView) helper.getView(R.id.image));
                helper.setText(R.id.title, Util.showText(item.getUw_title(), item.getUw_title_z()));
            }
        };
        homeDubbingAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.getDubbing(page);
            }
        }, mRecommendRecyclerview);
        homeDubbingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DubbingListBean item = homeDubbingAdapter.getItem(position);
                if (item != null) {
                    Intent intent = new Intent(getActivity(), ShowDubbingVideoActivity.class);
                    intent.putExtra("id", item.getUw_id());
                    startActivity(intent);
                }
            }
        });

        mRecommendRecyclerview.setAdapter(homeDubbingAdapter);
    }

    /**
     * @return xml id
     */
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_dubbing;
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
    public void showError(String msg) {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        ToastUtil.showText(msg);
    }

    @Override
    public void ShowBanner(List<BannerBean> result) {
        bannerBeans = result;
        bannerClickListener.setBannerBeans(result);
        banner.setImages(bannerBeans);
        banner.start();
    }

    @Override
    public void showDubbing(List<DubbingListBean> dubbingListBeans) {
        swipeRefreshLayout.setRefreshing(false);
        if (page == 1) {
            homeDubbingAdapter.setNewData(dubbingListBeans);
        } else {
            if (dubbingListBeans.size() < 20) {
                homeDubbingAdapter.loadMoreEnd();
            } else {
                homeDubbingAdapter.loadMoreComplete();
            }
        }
        page++;
    }

    @OnClick({R.id.search, R.id.collect, R.id.live, R.id.book, R.id.history1, R.id.history2, R.id.history3, R.id.banner})
    public void onViewClicked(View view) {
        Intent intent;
        PlayHistoryInfo playHistoryInfo;
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
            case R.id.live:
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.showLiveFragment();
                }
                break;
            case R.id.book:
                startActivity(new Intent(getContext(), BookActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initHistory();
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
            ViewParent parent = mRecommendRecyclerview.getParent().getParent();
            if (v.getId() == R.id.item_dubbing) {
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
