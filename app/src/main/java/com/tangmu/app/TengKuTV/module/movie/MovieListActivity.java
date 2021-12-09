package com.tangmu.app.TengKuTV.module.movie;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.MovieItemDecoration;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.TopMiddleDecoration;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.CustomLoadMoreView;
import com.tangmu.app.TengKuTV.view.TitleView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class MovieListActivity extends BaseActivity implements View.OnFocusChangeListener {

    TextView noData;
    RelativeLayout noNet;
    @BindView(R.id.swip)
    SwipeRefreshLayout swip;
    @BindView(R.id.category1)
    RecyclerView category1;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.screen)
    CheckBox screen;
    @BindView(R.id.category2)
    RecyclerView category2;
    @BindView(R.id.radio_all)
    RadioButton radioAll;
    @BindView(R.id.radio_free)
    RadioButton radioFree;
    @BindView(R.id.radio_pay)
    RadioButton radioPay;
    @BindView(R.id.movie_is_pay)
    RadioGroup movieIsPay;
    @BindView(R.id.years_recyclerView)
    RecyclerView yearsRecyclerView;
    @BindView(R.id.movie_recyclerView)
    RecyclerView movieRecyclerView;
    @BindView(R.id.showP)
    ImageView showP;
    @BindView(R.id.tv_title_view)
    TitleView titleView;
    private int page = 1;
    private BaseQuickAdapter<HomeChildRecommendBean.VideoBean, BaseViewHolder> quickAdapter;
    private BaseQuickAdapter<String, BaseViewHolder> yearAdapter;
    private int currentSelectYear;
    private BaseQuickAdapter<CategoryBean, BaseViewHolder> videoFirstChoiceAdapter;
    private BaseQuickAdapter<CategoryBean.SecondBean, BaseViewHolder> videoSecondChoiceAdapter;
    private CategoryBean currentCategoryBean;
    private int index;
    private int position;
    private int payType;
    private int selectYear;
    private Timer timer;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initData() {
        ArrayList<CategoryBean> categoryBeans = (ArrayList<CategoryBean>) getIntent().getSerializableExtra("PCategory");
        index = getIntent().getIntExtra("index", 0);
        videoFirstChoiceAdapter.setNewData(categoryBeans);
        currentCategoryBean = categoryBeans.get(index);
        position = getIntent().getIntExtra("position", 0);
        List<CategoryBean.SecondBean> second = currentCategoryBean.getSecond();
        removeDubbing(second);
        videoSecondChoiceAdapter.setNewData(second);
        CategoryBean.SecondBean currentSecondBean = second.get(position);
        tvCategory.setText(Util.showText(currentSecondBean.getVt_title(), currentSecondBean.getVt_title_z()));
        category2.postDelayed(new Runnable() {
            @Override
            public void run() {
                View viewByPosition = videoSecondChoiceAdapter.getViewByPosition(category2, position, R.id.check_second);
                if (viewByPosition != null) {
                    viewByPosition.requestFocus();
                }
            }
        }, 100);
        categoryBeans.remove(categoryBeans.size() - 1);
        Iterator<CategoryBean> iterator = categoryBeans.iterator();
        while (iterator.hasNext()) {
            CategoryBean categoryBean = iterator.next();
            if (categoryBean.getVt_title().contains("配音")) {
                iterator.remove();
            }
            if (categoryBean.getVt_pid() == -2) {
                iterator.remove();
            }
        }
        swip.setRefreshing(true);
//        getVideoList();
    }

    private void removeDubbing(List<CategoryBean.SecondBean> second) {
        for (int i = 0; i < second.size(); i++) {
            if (second.get(i).getVt_pid() == -2) {
                second.remove(i);
                break;
            }
        }
    }

    private void getVideoList() {
        CategoryBean.SecondBean item = videoSecondChoiceAdapter.getItem(position);
        if (item == null) {
            swip.setRefreshing(false);
            quickAdapter.getData().clear();
            quickAdapter.notifyDataSetChanged();
        } else {
            PostRequest<BaseListResponse<HomeChildRecommendBean.VideoBean>> postRequest = OkGo.<BaseListResponse<HomeChildRecommendBean.VideoBean>>post(Constant.IP + Constant.twoTypeDetail)
                    .tag(this)
                    .params("page", page)
                    .params("size", 20)
                    .params("p_id", item.getVt_id());
            if (payType != 0) {
                postRequest.params("type", payType);//1免费 2付费
            }
            if (selectYear != 0)
                postRequest.params("time", selectYear);//年限
            postRequest.execute(new JsonCallback<BaseListResponse<HomeChildRecommendBean.VideoBean>>() {
                @Override
                public void onSuccess(Response<BaseListResponse<HomeChildRecommendBean.VideoBean>> response) {
                    if (swip.isRefreshing())
                        swip.setRefreshing(false);
                    if (response.body().getStatus() == 0) {
                        showVideo(response.body().getResult());
                    } else {
                        ToastUtil.showText(response.body().getMsg());
                    }
                }

                @Override
                public void onError(Response<BaseListResponse<HomeChildRecommendBean.VideoBean>> response) {
                    super.onError(response);
                    if (swip.isRefreshing())
                        swip.setRefreshing(false);
                    if (page == 1) {
                        quickAdapter.isUseEmpty(true);
                        noNet.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                        quickAdapter.setNewData(Collections.<HomeChildRecommendBean.VideoBean>emptyList());
                    } else {
                        quickAdapter.loadMoreFail();
                    }
                    ToastUtil.showText(handleError(response.getException()));
                }
            });
        }
    }

    private void showVideo(List<HomeChildRecommendBean.VideoBean> videoBeans) {
        if (page == 1) {
            noNet.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
            quickAdapter.isUseEmpty(true);
            quickAdapter.setNewData(videoBeans);
        } else {
            quickAdapter.getData().addAll(videoBeans);
            if (videoBeans.size() < 20) {
                quickAdapter.loadMoreEnd();
            } else {
                quickAdapter.loadMoreComplete();
            }
        }
        page++;
    }

    @Override
    protected void initView() {
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                freshVideoList();
            }
        });
        initMovies();
        initC1List();
        initC2List();
        initYears();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long currentTimeMillis = System.currentTimeMillis();
                            titleView.setTime(Util.convertSystemTime(currentTimeMillis));
                        }
                    });
                }
            }, 0, 1000);
        }
        titleView.updateTV_Vip();
    }

    private void initYears() {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        int currentYear = instance.get(Calendar.YEAR);
        ArrayList<String> years = new ArrayList<>();
        years.add(getString(R.string.all));
        for (int i = currentYear; i > 1979; i--) {
            years.add(String.valueOf(i));
        }
        yearAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_year, years) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.item_year, item)
                        .setChecked(R.id.item_year, years.indexOf(item) == currentSelectYear);
            }
        };
        yearAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (currentSelectYear != position) {
                    int pre = currentSelectYear;
                    currentSelectYear = position;
                    yearAdapter.notifyItemChanged(pre, "");
                    yearAdapter.notifyItemChanged(currentSelectYear, "");
                    freshVideoList();
                }
            }
        });
        yearsRecyclerView.setAdapter(yearAdapter);
    }

    private void initC2List() {

        TopMiddleDecoration topMiddleDecoration =
                new TopMiddleDecoration(AutoSizeUtils.dp2px(this, 22), AutoSizeUtils.dp2px(this, 39));
        category2.addItemDecoration(topMiddleDecoration);
        videoSecondChoiceAdapter = new BaseQuickAdapter<CategoryBean.SecondBean, BaseViewHolder>(R.layout.item_video_second) {
            @Override
            protected void convert(BaseViewHolder helper, CategoryBean.SecondBean item) {
                helper.itemView.setOnFocusChangeListener(MovieListActivity.this);
                helper.setText(R.id.check_second, Util.showText(item.getVt_title(), item.getVt_title_z()));
//                if (position == videoSecondChoiceAdapter.getData().indexOf(item)) {
//                    helper.setChecked(R.id.check_second, true);
//                } else
//                    helper.setChecked(R.id.check_second, false);
            }
        };
//        videoSecondChoiceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                int preposition = MovieListActivity.this.position;
//                MovieListActivity.this.position = position;
//                CategoryBean.SecondBean currentSecondBean = videoSecondChoiceAdapter.getItem(position);
//                tvCategory.setText(Util.showText(currentSecondBean.getVt_title(), currentSecondBean.getVt_title_z()));
//                videoSecondChoiceAdapter.notifyItemChanged(position, "");
//                videoSecondChoiceAdapter.notifyItemChanged(preposition, "");
//            }
//        });
        category2.setAdapter(videoSecondChoiceAdapter);
    }

    private void initC1List() {
        TopMiddleDecoration topMiddleDecoration =
                new TopMiddleDecoration(AutoSizeUtils.dp2px(this, 29), AutoSizeUtils.dp2px(this, 10));
        category1.addItemDecoration(topMiddleDecoration);
        videoFirstChoiceAdapter = new BaseQuickAdapter<CategoryBean, BaseViewHolder>(R.layout.item_video_first) {
            @Override
            protected void convert(BaseViewHolder helper, CategoryBean item) {
                helper.itemView.setOnFocusChangeListener(MovieListActivity.this);
                helper.setText(R.id.check_first, Util.showText(item.getVt_title(), item.getVt_title_z()));
            }
        };
//        videoFirstChoiceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                int preIndex = index;
//                index = position;
//                videoFirstChoiceAdapter.notifyItemChanged(preIndex, "");
//                videoFirstChoiceAdapter.notifyItemChanged(index, "");
//                CategoryBean item = videoFirstChoiceAdapter.getItem(position);
//                if (item == null) {
//                    videoSecondChoiceAdapter.getData().clear();
//                    videoSecondChoiceAdapter.notifyDataSetChanged();
//                } else {
//                    List<CategoryBean.SecondBean> second = item.getSecond();
//                    removeDubbing(second);
//                    MovieListActivity.this.position = 0;
//                    videoSecondChoiceAdapter.setNewData(second);
//                }
//            }
//        });
        category1.setAdapter(videoFirstChoiceAdapter);
    }

    private void initMovies() {
        movieRecyclerView.addItemDecoration(new MovieItemDecoration(AutoSizeUtils.dp2px(this, 9), AutoSizeUtils.dp2px(this, 19)));
        quickAdapter = new BaseQuickAdapter<HomeChildRecommendBean.VideoBean, BaseViewHolder>(R.layout.item_movie_list) {
            @Override
            protected void convert(BaseViewHolder helper, HomeChildRecommendBean.VideoBean item) {
                String titleStr = Util.showText(item.getVm_title(), item.getVm_title_z());
                helper.setText(R.id.title, titleStr).setVisible(R.id.vip, item.getVm_is_pay() == 2);
                GlideUtils.getRequest(MovieListActivity.this, Util.convertImgPath(item.getVm_img())).placeholder(R.drawable.default_img)
                        .override(250,320).centerCrop().into((ImageView) helper.getView(R.id.image));
                if (item.getVm_update_status() == 2) {
                    helper.setText(R.id.update_status, getResources().getString(R.string.update_done));
                } else if (item.getVm_update_status() == 1)
                    helper.setText(R.id.update_status, String.format(getResources().getString(R.string.update_status), item.getCount()));

            }
        };
        quickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeChildRecommendBean.VideoBean item = quickAdapter.getItem(position);
                if (item == null) return;
                Intent intent;
                if (item.getVm_type() == 2) {
                    intent = new Intent(MovieListActivity.this, TVDetailActivity.class);
                } else
                    intent = new Intent(MovieListActivity.this, MovieDetailActivity.class);
                intent.putExtra("id", item.getVm_id());
                if (item.getVt_id_one() != 0)
                    intent.putExtra("c_id", item.getVt_id_one());
                else {
                    CategoryBean item1 = videoFirstChoiceAdapter.getItem(index);
                    if (item1 != null)
                        intent.putExtra("c_id", item1.getVt_id());
                }
                startActivity(intent);
            }
        });
        View view = LayoutInflater.from(this).inflate(R.layout.list_no_data, null);
        noData = view.findViewById(R.id.no_data);
        noNet = view.findViewById(R.id.no_net);
        quickAdapter.setEmptyView(view);
        quickAdapter.isUseEmpty(false);
        quickAdapter.setLoadMoreView(new CustomLoadMoreView());
        quickAdapter.setFooterViewAsFlow(true);
        quickAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getVideoList();
            }
        }, movieRecyclerView);
        movieRecyclerView.setAdapter(quickAdapter);
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_movie_list;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && currentFocus.getId() == R.id.check_first) {
                category1.setVisibility(View.GONE);
                showP.setVisibility(View.VISIBLE);
//                screen.requestFocus();
                View viewByPosition = videoFirstChoiceAdapter.getViewByPosition(category2, 0, R.id.check_second);
                if (viewByPosition != null) {
                    viewByPosition.requestFocus();
                }
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && (currentFocus.getId() == R.id.check_second || currentFocus.getId() == R.id.screen)) {
                category1.setVisibility(View.INVISIBLE);
                List<CategoryBean> data = videoFirstChoiceAdapter.getData();
                CategoryBean.SecondBean item = videoSecondChoiceAdapter.getItem(position);
                category1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (item != null)
                            for (int i = 0; i < data.size(); i++) {
                                if (data.get(i).getVt_id() == item.getVt_pid()) {
                                    index = i;
                                    break;
                                }
                            }
                        View viewByPosition = videoFirstChoiceAdapter.getViewByPosition(category1, index, R.id.check_first);
                        category1.setVisibility(View.VISIBLE);
                        if (viewByPosition != null) {
                            viewByPosition.requestFocus();
                        }
                        category1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

                showP.setVisibility(View.GONE);
            }
        } else {
            LogUtil.e("currentFocus is null");
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.showP, R.id.radio_all, R.id.radio_free, R.id.radio_pay, R.id.screen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.showP:
                ViewGroup.LayoutParams layoutParams = category2.getLayoutParams();
                layoutParams.width = AutoSizeUtils.dp2px(this, 130);
                category2.setLayoutParams(layoutParams);
                category1.setVisibility(View.VISIBLE);
                showP.setVisibility(View.GONE);
                break;
            case R.id.radio_all:
            case R.id.radio_free:
            case R.id.radio_pay:
                freshVideoList();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    private void getYears() {
        selectYear = 0;
        String yearAdapterItem = yearAdapter.getItem(currentSelectYear);
        if (yearAdapterItem != null)
            try {
                selectYear = Integer.valueOf(yearAdapterItem);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
    }

    private void getPayType() {
        int checkedRadioButtonId = movieIsPay.getCheckedRadioButtonId();
        payType = 0;
        switch (checkedRadioButtonId) {
            case R.id.radio_all:
                break;
            case R.id.radio_free:
                payType = 1;
                break;
            case R.id.radio_pay:
                payType = 2;
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v.getId() == R.id.check_first) {
                int childAdapterPosition = category1.getChildAdapterPosition(v);
                if (index != childAdapterPosition) {
                    index = childAdapterPosition;
                    CategoryBean item = videoFirstChoiceAdapter.getItem(index);
                    if (item == null) {
                        videoSecondChoiceAdapter.getData().clear();
                        videoSecondChoiceAdapter.notifyDataSetChanged();
                    } else {
                        tvCategory.setText(Util.showText(item.getVt_title(), item.getVt_title_z()));
                        List<CategoryBean.SecondBean> second = item.getSecond();
                        removeDubbing(second);
                        MovieListActivity.this.position = 0;
                        videoSecondChoiceAdapter.setNewData(second);
                    }
                }

            }
            if (v.getId() == R.id.check_second) {
                position = category2.getChildAdapterPosition(v);
                CategoryBean.SecondBean item = videoSecondChoiceAdapter.getItem(position);
                if (item != null) {
                    tvCategory.setText(Util.showText(item.getVt_title(), item.getVt_title_z()));
                }
                freshVideoList();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null) timer.cancel();
        super.onDestroy();
    }

    private void freshVideoList() {
        page = 1;
        swip.setRefreshing(true);
        getPayType();
        getYears();
        getVideoList();
    }
}
