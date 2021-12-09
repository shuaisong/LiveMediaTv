package com.tangmu.app.TengKuTV.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.tangmu.app.TengKuTV.bean.MiguLoginBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerFragmentComponent;
import com.tangmu.app.TengKuTV.contact.HomeChildContact;
import com.tangmu.app.TengKuTV.module.book.BookActivity;
import com.tangmu.app.TengKuTV.module.main.MainActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieListActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.tangmu.app.TengKuTV.module.vip.MiGuActivity;
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

import javax.inject.Inject;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.ScreenUtils;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class HomeVipFragment extends BaseFragment implements HomeChildContact.View, View.OnFocusChangeListener {
    @Inject
    HomeChildPresenter presenter;
    @BindView(R.id.category)
    RecyclerView mCategory;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.swip)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.head)
    ImageView head;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.logined_view)
    LinearLayout loginedView;
    @BindView(R.id.login_ti2)
    TextView loginTi2;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.live)
    LinearLayout live;
    @BindView(R.id.book)
    LinearLayout book;
    private HomeChildAdapter homeChildAdapter;
    private CategoryBean categoryBean;
    private BaseQuickAdapter<CategoryBean.SecondBean, BaseViewHolder> mCategoryAdapter;
    private BannerClickListener bannerClickListener;
    private ViewPager bannerViewPager;

    public static HomeVipFragment newInstance(CategoryBean categoryBean) {
        Bundle args = new Bundle();
        args.putSerializable("Category", categoryBean);
        HomeVipFragment fragment = new HomeVipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        swipeRefreshLayout.setRefreshing(true);
        presenter.getBanner(categoryBean.getVt_id());
        presenter.getRecMovie(categoryBean.getVt_id());
    }

    /**
     * 初始化view
     */
    @Override
    protected void initView() {
        presenter.attachView(this);
        initBanner();
        initRecycer();
        live.setOnFocusChangeListener(this);
        book.setOnFocusChangeListener(this);
        loginTi2.setOnFocusChangeListener(this);
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
        homeChildAdapter.isVip();
        mRecyclerview.setAdapter(homeChildAdapter);
        homeChildAdapter.bindToRecyclerView(mRecyclerview);
        homeChildAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeChildBean item = homeChildAdapter.getItem(position);
                if (item == null) return;
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
        });
    }

    private void initCategory() {
        categoryBean = (CategoryBean) getArguments().getSerializable("Category");
        int itemDecoration = AutoSizeUtils.dp2px(getActivity(), 16);
        int categoryItemWidth = (ScreenUtils.getScreenSize(getActivity())[0] - AutoSizeUtils.dp2px(getActivity(), 62)
                - itemDecoration * 4) / 5;
        SingleLineItemDecoration singleLineItemDecoration = new SingleLineItemDecoration(itemDecoration / 2);
        mCategory.addItemDecoration(singleLineItemDecoration);
        mCategoryAdapter = new BaseQuickAdapter<CategoryBean.SecondBean, BaseViewHolder>(R.layout.item_category,
                categoryBean.getSecond()) {
            @Override
            protected void convert(BaseViewHolder helper, CategoryBean.SecondBean item) {
                View view = helper.getView(R.id.item_category);
                view.setOnFocusChangeListener(HomeVipFragment.this);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = categoryItemWidth;
                view.setLayoutParams(layoutParams);
                view.setBackground(getResources().getDrawable(Constant.c_bgs[mCategoryAdapter.getData().indexOf(item) % 4]));
                if (!PreferenceManager.getInstance().isDefaultLanguage()) {
                    TextView tv_name = helper.getView(R.id.name);
                    tv_name.setLineSpacing(0, 0.7f);
                }
                helper.setText(R.id.name, Util.showText(item.getVt_title(), item.getVt_title_z()))
                        .setTextColor(R.id.name, getResources().getColor(R.color.vip));
                if (item.getVt_icon_img() != null)
                    GlideUtils.getRequest(getActivity(), Util.convertImgPath(item.getVt_icon_img()))
                            .into((ImageView) helper.getView(R.id.image));

            }
        };
        mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CategoryBean.SecondBean item = mCategoryAdapter.getItem(position);
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    Intent intent = new Intent(getActivity(), MovieListActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("PCategory", activity.getCategory());
                    intent.putExtra("index", activity.getIndex());
                    startActivity(intent);
                }
            }
        });
        mCategory.setAdapter(mCategoryAdapter);
    }

    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                GlideUtils.getRequest(imageView, Util.convertImgPath(((BannerBean) path).getB_img()))
                        .override(400, 200)
                        .placeholder(R.drawable.default_img_banner).into(imageView);
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

    /**
     * @return xml id
     */
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home_vip_child;
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
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
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
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        homeChildAdapter.setNewData(movieBeans);
    }

    @Override
    public void showChildMovie(List<HomeChildRecommendBean.VideoBean> movieBeans) {

    }

    @OnClick({R.id.login_ti2, R.id.live, R.id.book, R.id.banner})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.banner:
                List<BannerBean> bannerBeans = bannerClickListener.getBannerBeans();
                if (bannerBeans == null || bannerBeans.isEmpty()) return;
                if (bannerClickListener != null)
                    bannerClickListener.OnBannerClick(banner.toRealPosition(bannerViewPager.getCurrentItem()));
                break;
            case R.id.login_ti2:
                startActivity(new Intent(getContext(), MiGuActivity.class));
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
        MiguLoginBean login = PreferenceManager.getInstance().getLogin();
        if (login != null) {
            loginedView.setVisibility(View.VISIBLE);
            nickName.setText(PreferenceManager.getInstance().getUserName());
            if (login.getTu_vip_status() == 1) {
                loginTi2.setVisibility(View.INVISIBLE);
            } else {
                loginTi2.setVisibility(View.VISIBLE);
            }
        } else {
            loginedView.setVisibility(View.GONE);
            loginTi2.setVisibility(View.VISIBLE);
//            head.setImageResource(R.mipmap.default_head);
            nickName.setText("");
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            ViewParent parent = mCategory.getParent().getParent();
            if (v.getId() == R.id.item_movie) {
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
        int i = ScreenUtils.getScreenSize(view.getContext())[1];
        out[1] = out[1] - i / 2;
        return out;
    }
}
