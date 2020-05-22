package com.tangmu.app.TengKuTV.module.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.adapter.HomeChildAdapter;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.bean.BannerBean;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.bean.HomeChildBean;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerFragmentComponent;
import com.tangmu.app.TengKuTV.contact.HomeChildContact;
import com.tangmu.app.TengKuTV.db.PlayHistoryInfo;
import com.tangmu.app.TengKuTV.db.PlayHistoryManager;
import com.tangmu.app.TengKuTV.module.book.BookActivity;
import com.tangmu.app.TengKuTV.module.main.MainActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieListActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.tangmu.app.TengKuTV.module.playhistory.PlayHistoryActivity;
import com.tangmu.app.TengKuTV.module.search.VideoSearchActivity;
import com.tangmu.app.TengKuTV.presenter.HomeChildPresenter;
import com.tangmu.app.TengKuTV.utils.BannerClickListener;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.MovieItemDecoration;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.SingleLineItemDecoration;
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
public class HomeChildFragment extends BaseFragment implements HomeChildContact.View, View.OnFocusChangeListener {
    @Inject
    HomeChildPresenter presenter;
    @BindView(R.id.category)
    RecyclerView mCategory;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.swip)
    SwipeRefreshLayout swip;
    @BindView(R.id.banner)
    Banner banner;
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
    private BaseQuickAdapter<CategoryBean.SecondBean, BaseViewHolder> mCategoryAdapter;
    private CategoryBean categoryBean;
    private HomeChildAdapter homeChildAdapter;
    private boolean initData;
    private BannerClickListener bannerClickListener;
    private boolean isNeedRed;
    private int categoryItemWidth;
    private List<PlayHistoryInfo> allVideo;
    private ViewPager bannerViewPager;

    public HomeChildFragment() {
    }

    public static HomeChildFragment newInstance(CategoryBean categoryBean) {
        Bundle args = new Bundle();
        args.putSerializable("Category", categoryBean);
        HomeChildFragment fragment = new HomeChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!initData) {
            swip.setRefreshing(true);
            presenter.getBanner(categoryBean.getVt_id());
            presenter.getRecMovie(categoryBean.getVt_id());
            initData = true;
        }
        initHistory();
    }

    /**
     * 初始化view
     */
    @Override
    protected void initView() {
        presenter.attachView(this);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (categoryBean != null) {
                    presenter.getBanner(categoryBean.getVt_id());
                    presenter.getRecMovie(categoryBean.getVt_id());
                }
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
                        .placeholder(R.mipmap.img_default).into(imageView);
            }
        });
        banner.setBannerTitles(null);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
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
        initCategory();
        initMovie();
    }

    private void initMovie() {
        mRecyclerview.addItemDecoration(new MovieItemDecoration(getActivity()));
        homeChildAdapter = new HomeChildAdapter(Collections.<HomeChildBean>emptyList());
        homeChildAdapter.bindToRecyclerView(mRecyclerview);
        mRecyclerview.setAdapter(homeChildAdapter);
        homeChildAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeChildBean item = homeChildAdapter.getItem(position);
                if (item != null && item.getMovieBean() != null) {
                    Intent intent;
                    if (item.getMovieBean().getVm_type() == 2) {
                        intent = new Intent(getActivity(), TVDetailActivity.class);
                    } else
                        intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtra("id", item.getMovieBean().getVm_id());
                    if (item.getMovieBean().getVt_id_one() != 0)
                        intent.putExtra("c_id", item.getMovieBean().getVt_id_one());
                    else {
                        intent.putExtra("c_id", categoryBean.getVt_id());
                    }
                    startActivity(intent);
                }
            }
        });
    }


    private void initCategory() {
        categoryBean = (CategoryBean) getArguments().getSerializable("Category");
        boolean isRecommend = "推荐".equals(categoryBean.getVt_title());
        isNeedRed = false;
        if (isRecommend) {
            CategoryBean.SecondBean secondBean = new CategoryBean.SecondBean();
            secondBean.setVt_title("经典配音");
            secondBean.setVt_pid(-2);
            secondBean.setVt_title_z("ཕུལ་བྱུང་སྒྲ་སྦྱོར།");
            categoryBean.getSecond().add(secondBean);
        } else if (categoryBean.getVt_id() == 2) {
            isNeedRed = true;
        }
        int itemDecoration = AutoSizeUtils.dp2px(getActivity(), 16);
        if (isRecommend)
            categoryItemWidth = (ScreenUtils.getScreenSize(getActivity())[0] -
                    AutoSizeUtils.dp2px(getActivity(), 62) - itemDecoration * 3) / 4;
        else
            categoryItemWidth = (ScreenUtils.getScreenSize(getActivity())[0] - AutoSizeUtils.dp2px(getActivity(), 62)
                    - itemDecoration * 4) / 5;
        SingleLineItemDecoration singleLineItemDecoration = new SingleLineItemDecoration(itemDecoration / 2);
        mCategory.addItemDecoration(singleLineItemDecoration);
        mCategoryAdapter = new BaseQuickAdapter<CategoryBean.SecondBean, BaseViewHolder>(R.layout.item_category,
                categoryBean.getSecond()) {
            @Override
            protected void convert(BaseViewHolder helper, CategoryBean.SecondBean item) {
                View view = helper.getView(R.id.item_category);
                view.setOnFocusChangeListener(HomeChildFragment.this);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = categoryItemWidth;
                view.setLayoutParams(layoutParams);
                TextView name = helper.getView(R.id.name);
                if (isNeedRed) {
                    name.setTextSize(20);
                    name.setTypeface(Typeface.SANS_SERIF);
                    view.setBackground(getResources().getDrawable(R.drawable.red_bg_focus_select));
                } else {
                    view.setBackground(getResources().getDrawable(Constant.c_bgs[mCategoryAdapter.getData().indexOf(item) % 4]));
                }
                if (!PreferenceManager.getInstance().isDefaultLanguage()) {
                    TextView tv_name = helper.getView(R.id.name);
                    tv_name.setLineSpacing(0, 0.7f);
                }
                helper.setText(R.id.name, Util.showText(item.getVt_title(), item.getVt_title_z()));
                if (item.getVt_icon_img() != null)
                    GlideUtils.getRequest(getActivity(), Util.convertImgPath(item.getVt_icon_img()))
                            .into((ImageView) helper.getView(R.id.image));
                else {
                    if (item.getVt_pid() == -1) {
                        helper.setImageResource(R.id.image, R.mipmap.rec_c);
                    }
                    if (item.getVt_pid() == -2) {
                        helper.setImageResource(R.id.image, R.mipmap.dubbing_c);
                    }
                }
            }
        };
        mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CategoryBean.SecondBean item = mCategoryAdapter.getItem(position);
                if (item == null) return;
                HomeFragment parentFragment = (HomeFragment) getParentFragment();
                if (item.getVt_pid() == -2) {
                    assert parentFragment != null;
                    parentFragment.showDubbingFrament();
                    return;
                }
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    assert parentFragment != null;
                    Intent intent = new Intent(getActivity(), MovieListActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("PCategory", parentFragment.getCategory());
                    intent.putExtra("index", parentFragment.getIndex());
                    startActivity(intent);
                }
            }
        });
        mCategory.setAdapter(mCategoryAdapter);
    }


    /**
     * @return xml id
     */
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home_child;
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
        if (swip.isRefreshing()) swip.setRefreshing(false);
        ToastUtil.showText(msg);
    }

    @Override
    public void ShowBanner(List<BannerBean> bannerBeans) {
        banner.setImages(bannerBeans);
        bannerClickListener.setBannerBeans(bannerBeans);
        banner.start();
    }

    @Override
    public void showMovie(List<HomeChildBean> movieBeans) {
        if (swip.isRefreshing()) swip.setRefreshing(false);
        homeChildAdapter.setNewData(movieBeans);
    }

    @Override
    public void showChildMovie(List<HomeChildRecommendBean.VideoBean> movieBeans) {

    }

    @OnClick({R.id.history1, R.id.history2, R.id.history3, R.id.search, R.id.collect, R.id.live, R.id.book, R.id.banner})
    public void onViewClicked(View view) {
        Intent intent;
        PlayHistoryInfo playHistoryInfo;
        switch (view.getId()) {
            case R.id.banner:
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
            case R.id.search:
                startActivity(new Intent(getActivity(), VideoSearchActivity.class));
                break;
            case R.id.live:
                HomeFragment parentFragment = (HomeFragment) getParentFragment();
                if (parentFragment != null) {
                    parentFragment.showLiveFragment();
                }
                break;
            case R.id.book:
                startActivity(new Intent(getActivity(), BookActivity.class));
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
            ViewParent parent = mCategory.getParent().getParent();
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
        int i = ScreenUtils.getScreenSize(view.getContext())[1];
        out[1] = out[1] - i / 2;
        return out;
    }
}
