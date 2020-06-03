package com.tangmu.app.TengKuTV.module.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.bean.BannerBean;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.bean.MoreBookBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerFragmentComponent;
import com.tangmu.app.TengKuTV.contact.BookChildContact;
import com.tangmu.app.TengKuTV.db.PlayHistoryInfo;
import com.tangmu.app.TengKuTV.db.PlayHistoryManager;
import com.tangmu.app.TengKuTV.module.playhistory.PlayHistoryActivity;
import com.tangmu.app.TengKuTV.module.search.BookSearchActivity;
import com.tangmu.app.TengKuTV.presenter.BookChildPresenter;
import com.tangmu.app.TengKuTV.utils.BannerClickListener;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.SingleLineItemDecoration;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.math.BigDecimal;
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
public class BookChildFragment extends BaseFragment implements BookChildContact.View, View.OnFocusChangeListener {

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
    private BaseQuickAdapter<MoreBookBean, BaseViewHolder> mMayAdapter;
    private CategoryBean categoryBean;
    private boolean initData;
    private BannerClickListener bannerClickListener;
    private List<PlayHistoryInfo> allBook;
    private ViewPager bannerViewPager;
    private int itemHeight;
    private int topTitleHeight;

    public BookChildFragment() {
    }

    @Inject
    BookChildPresenter presenter;
    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.category)
    RecyclerView mCategory;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.may_list)
    RecyclerView may_list;
    @BindView(R.id.swip)
    SwipeRefreshLayout swipeRefreshLayout;
    private BaseQuickAdapter<CategoryBean.SecondBean, BaseViewHolder> mCategoryAdapter;
    private BaseQuickAdapter<MoreBookBean, BaseViewHolder> mBookAdapter;

    public static BookChildFragment newInstance(CategoryBean categoryBean) {
        BookChildFragment bookChildFragment = new BookChildFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", categoryBean);
        bookChildFragment.setArguments(bundle);
        return bookChildFragment;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        itemHeight = AutoSizeUtils.dp2px(getActivity(), 300);
        topTitleHeight = AutoSizeUtils.dp2px(getActivity(), 84);
    }

    private void initHistory() {
        allBook = PlayHistoryManager.getAllBook();
        if (allBook.isEmpty()) {
            history1.setVisibility(View.GONE);
            history2.setVisibility(View.GONE);
        } else if (allBook.size() == 1) {
            history1.setVisibility(View.VISIBLE);
            history2.setVisibility(View.GONE);
            PlayHistoryInfo playHistoryInfo = allBook.get(0);
            tvHistory1Progress.setText(String.format(Locale.CHINA, getString(R.string.haveViewed), playHistoryInfo.getB_progress()));
            tvHistory1.setText(String.format(Locale.CHINA, getString(R.string.play_history_title), Util.showText(playHistoryInfo.getB_title(),
                    playHistoryInfo.getB_title_z()), playHistoryInfo.getB_position() + 1));
        } else {
            history1.setVisibility(View.VISIBLE);
            history2.setVisibility(View.VISIBLE);
            PlayHistoryInfo playHistoryInfo = allBook.get(0);
            tvHistory1Progress.setText(String.format(Locale.CHINA, getString(R.string.haveViewed), playHistoryInfo.getB_progress()));
            tvHistory1.setText(String.format(Locale.CHINA, getString(R.string.play_history_title), Util.showText(playHistoryInfo.getB_title(),
                    playHistoryInfo.getB_title_z()), playHistoryInfo.getB_position() + 1));
            playHistoryInfo = allBook.get(1);
            tvHistory2Progress.setText(String.format(Locale.CHINA, getString(R.string.haveViewed), playHistoryInfo.getB_progress()));
            tvHistory2.setText(String.format(Locale.CHINA, getString(R.string.play_history_title), Util.showText(playHistoryInfo.getB_title(),
                    playHistoryInfo.getB_title_z()), playHistoryInfo.getB_position() + 1));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!initData) {
            swipeRefreshLayout.setRefreshing(true);
            presenter.getBanner(categoryBean.getVt_id());
//        presenter.getAd();
            presenter.getRecBook(categoryBean.getVt_id());
            presenter.getBooks(categoryBean.getVt_id());
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getBanner(categoryBean.getVt_id());
                //        presenter.getAd();
                presenter.getRecBook(categoryBean.getVt_id());
                presenter.getBooks(categoryBean.getVt_id());
            }
        });
        initBanner();
        initRecycer();
        history1.setOnFocusChangeListener(this);
        history2.setOnFocusChangeListener(this);
        history3.setOnFocusChangeListener(this);
        search.setOnFocusChangeListener(this);
        collect.setOnFocusChangeListener(this);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    private void initRecycer() {
        initCategory();
        initMayList();
        initBooks();
    }

    private void initMayList() {
        SingleLineItemDecoration singleLineItemDecoration =
                new SingleLineItemDecoration(AutoSizeUtils.dp2px(getContext(), 13));
        may_list.addItemDecoration(singleLineItemDecoration);
        mMayAdapter = new BaseQuickAdapter<MoreBookBean, BaseViewHolder>(R.layout.item_may_book) {
            @Override
            protected void convert(BaseViewHolder helper, MoreBookBean item) {
                helper.itemView.setOnFocusChangeListener(BookChildFragment.this);
                helper.setText(R.id.title, Util.showText(item.getB_title(), item.getB_title_z()));
                helper.setText(R.id.author, Util.showText(item.getB_title(), item.getB_title_z()));
                GlideUtils.getRequest(getActivity(), Util.convertImgPath(item.getB_img())).placeholder(R.mipmap.img_default)
                        .transform(new CenterCrop(), new RoundedCorners(AutoSizeUtils.dp2px(getActivity(), 3)))
                        .into((ImageView) helper.getView(R.id.image));
            }
        };
        mMayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MoreBookBean item = mMayAdapter.getItem(position);
                if (item == null) return;
                Intent intent = new Intent(getActivity(), PlayBookActivity.class);
                intent.putExtra("id", item.getB_id());
                startActivity(intent);
            }
        });
        may_list.setAdapter(mMayAdapter);
    }

    private void initBooks() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.book_divider));
        mRecyclerview.addItemDecoration(dividerItemDecoration);
        mBookAdapter = new BaseQuickAdapter<MoreBookBean, BaseViewHolder>(R.layout.item_book) {
            @Override
            protected void convert(BaseViewHolder helper, MoreBookBean item) {
                int b_num = item.getB_num();
                String numStr;
                if (b_num >= 10000) {
                    BigDecimal bigDecimal = new BigDecimal(b_num);
                    float v = bigDecimal.divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP).floatValue();
                    numStr = v + getString(R.string.wan);
                } else {
                    numStr = String.valueOf(b_num);
                }
                helper.setText(R.id.title, Util.showText(item.getB_title(), item.getB_title_z()))
                        .setText(R.id.num1, numStr)
                        .setText(R.id.info, Util.showText(item.getB_des(), item.getB_title_z()));
                GlideUtils.getRequest(getActivity(), Util.convertImgPath(item.getB_img())).placeholder(R.mipmap.img_default)
                        .transform(new CenterCrop(), new RoundedCorners(AutoSizeUtils.dp2px(getActivity(), 5)))
                        .into((ImageView) helper.getView(R.id.image));
            }
        };
        mBookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MoreBookBean item = mBookAdapter.getItem(position);
                if (item == null) return;
                Intent intent = new Intent(getActivity(), PlayBookActivity.class);
                intent.putExtra("id", item.getB_id());
                startActivity(intent);
            }
        });
        mRecyclerview.setAdapter(mBookAdapter);
    }

    private void initCategory() {
        categoryBean = (CategoryBean) getArguments().getSerializable("type");
        int itemDecoration = AutoSizeUtils.dp2px(getActivity(), 16);
        int categoryItemWidth = (ScreenUtils.getScreenSize(getActivity())[0] - AutoSizeUtils.dp2px(getActivity(), 62)
                - itemDecoration * 4) / 5;
        SingleLineItemDecoration singleLineItemDecoration = new SingleLineItemDecoration(itemDecoration / 2);
        mCategory.addItemDecoration(singleLineItemDecoration);
        mCategoryAdapter = new BaseQuickAdapter<CategoryBean.SecondBean, BaseViewHolder>(R.layout.item_book_category, categoryBean.getSecond()) {
            @Override
            protected void convert(BaseViewHolder helper, CategoryBean.SecondBean item) {
                View view = helper.getView(R.id.item_category);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = categoryItemWidth;
                view.setLayoutParams(layoutParams);
                view.setBackground(getResources().getDrawable(Constant.c_bgs[mCategoryAdapter.getData().indexOf(item) % 4]));
                GlideUtils.getRequest(BookChildFragment.this, Util.convertImgPath(item.getVt_icon_img()))
                        .into((ImageView) helper.getView(R.id.image));
                helper.setText(R.id.name, Util.showText(item.getVt_title(), item.getVt_title_z()));
            }
        };
        mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookActivity activity = (BookActivity) getActivity();
                if (activity != null) {
                    Intent intent = new Intent(getActivity(), BookListActivity.class);
                    intent.putExtra("position", position);
                    activity.startActivity(intent);
                }
            }
        });
        mCategory.setAdapter(mCategoryAdapter);
    }

    private void initBanner() {
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                GlideUtils.getRequest(imageView, Util.convertImgPath(((BannerBean) path).getB_img()))
                        .placeholder(R.mipmap.img_default).into(imageView);
            }
        });
        mBanner.setBannerTitles(null);
        //设置mBanner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(1500);
        //设置指示器位置（当mBanner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        bannerClickListener = new BannerClickListener(getActivity());
        mBanner.setOnBannerListener(bannerClickListener);
        bannerViewPager = mBanner.findViewById(R.id.bannerViewPager);
        mBanner.setOnFocusChangeListener(this);
    }

    /**
     * @return xml id
     */
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_book_child;
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
    public void ShowBanner(List<BannerBean> bannerBeans) {
        if (bannerBeans != null && !bannerBeans.isEmpty()) {
            mBanner.setImages(bannerBeans);
            bannerClickListener.setBannerBeans(bannerBeans);
            mBanner.start();
        }
    }

    @Override
    public void showRecBooks(List<MoreBookBean> moreBookBeanList) {
        mMayAdapter.setNewData(moreBookBeanList);
    }

    @Override
    public void showBooks(List<MoreBookBean> bookBeanList) {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        mBookAdapter.setNewData(bookBeanList);
    }


    @OnClick({R.id.history1, R.id.history2, R.id.history3, R.id.collect, R.id.search, R.id.banner})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.banner:
                if (bannerClickListener != null)
                    bannerClickListener.OnBannerClick(mBanner.toRealPosition(bannerViewPager.getCurrentItem()));
                break;
            case R.id.history1:
                PlayHistoryInfo playHistoryInfo = allBook.get(0);
                intent = new Intent(getActivity(), PlayBookActivity.class);
                intent.putExtra("id", playHistoryInfo.getB_id());
                break;
            case R.id.history2:
                PlayHistoryInfo playHistoryInfo1 = allBook.get(1);
                intent = new Intent(getActivity(), PlayBookActivity.class);
                intent.putExtra("id", playHistoryInfo1.getB_id());
                break;
            case R.id.history3:
                intent = new Intent(getActivity(), PlayHistoryActivity.class);
                break;
            case R.id.collect:
                if (isClickLogin()) {
                    intent = new Intent(getActivity(), PlayHistoryActivity.class);
                    intent.putExtra("position", 1);
                }
                break;
            case R.id.search:
                intent = new Intent(getActivity(), BookSearchActivity.class);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            int[] amount = getScrollAmount(v);//计算需要滑动的距离
            ViewParent parent = may_list.getParent().getParent();
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
