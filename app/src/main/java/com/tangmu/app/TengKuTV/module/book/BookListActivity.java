package com.tangmu.app.TengKuTV.module.book;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.material.tabs.TabLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.bean.LoginBean;
import com.tangmu.app.TengKuTV.bean.MoreBookBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.TopMiddleDecoration;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.CustomLoadMoreView;
import com.tangmu.app.TengKuTV.view.TitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class BookListActivity extends BaseActivity implements View.OnFocusChangeListener {
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swip)
    SwipeRefreshLayout swip;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.listView)
    RecyclerView listView;
    private int page = 1;
    private BaseQuickAdapter<MoreBookBean, BaseViewHolder> quickAdapter;
    private CategoryBean categoryBean;
    private View noData;
    private View noNet;
    private ArrayList<CategoryBean> categories;
    private BaseQuickAdapter<CategoryBean.SecondBean, BaseViewHolder> bookSecondChoiceAdapter;
    private CategoryBean.SecondBean secondBean;
    private int index;
    private int position;
    private Timer timer;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initData() {
        categories = (ArrayList<CategoryBean>) getIntent().getSerializableExtra("PCategory");
        index = getIntent().getIntExtra("index", 0);
        position = getIntent().getIntExtra("position", 0);
        categoryBean = categories.get(index);
        setTabView();
        bookSecondChoiceAdapter.setNewData(categoryBean.getSecond());
        secondBean = categoryBean.getSecond().get(position);
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                View viewByPosition = bookSecondChoiceAdapter.getViewByPosition(listView, position, R.id.check);
                if (viewByPosition != null)
                    viewByPosition.requestFocus();
                title.findViewById(R.id.logo).setFocusable(false);
            }
        }, 100);
        assert categoryBean != null;
        getTypeBooks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        title.updateTV_Vip();
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long currentTimeMillis = System.currentTimeMillis();
                            title.setTime(Util.convertSystemTime(currentTimeMillis));
                        }
                    });
                }
            }, 1000, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null) timer.cancel();
        OkGo.getInstance().cancelTag(this);
        super.onDestroy();
    }

    private void getTypeBooks() {
        OkGo.<BaseListResponse<MoreBookBean>>post(Constant.IP + Constant.bookTypeDe)
                .tag(this)
                .params("page", page)
                .params("size", 20)
                .params("p_id", secondBean.getVt_id())
                .execute(new JsonCallback<BaseListResponse<MoreBookBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<MoreBookBean>> response) {
                        quickAdapter.isUseEmpty(true);
                        if (swip.isRefreshing()) swip.setRefreshing(false);
                        if (response.body().getStatus() == 0) {
                            List<MoreBookBean> result = response.body().getResult();
                            if (page == 1) {
                                Util.setNodata(noData, noNet, !checkNetAvailable());
                                quickAdapter.setNewData(result);
                            } else {
                                quickAdapter.getData().addAll(result);
                                if (result.size() < 20) {
                                    quickAdapter.loadMoreEnd();
                                } else quickAdapter.loadMoreComplete();
                            }
                            page++;
                        } else {
                            if (page == 1) {
                                Util.setNodata(noData, noNet, !checkNetAvailable());
                                quickAdapter.notifyDataSetChanged();
                            } else
                                quickAdapter.loadMoreFail();
                            ToastUtil.showText(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<MoreBookBean>> response) {
                        super.onError(response);
                        quickAdapter.isUseEmpty(true);
                        if (swip.isRefreshing()) swip.setRefreshing(false);
                        if (page == 1) {
                            Util.setNodata(noData, noNet, !checkNetAvailable());
                        } else {
                            quickAdapter.loadMoreFail();
                        }
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }

    @Override
    protected void initView() {
        initSecondCategory();
        initBooks();
    }

    private void initSecondCategory() {
        TopMiddleDecoration topMiddleDecoration = new TopMiddleDecoration(AutoSizeUtils.dp2px(this, 55));
        listView.addItemDecoration(topMiddleDecoration);
        bookSecondChoiceAdapter = new BaseQuickAdapter<CategoryBean.SecondBean, BaseViewHolder>(R.layout.item_book_second) {
            @Override
            protected void convert(BaseViewHolder helper, CategoryBean.SecondBean item) {
//                int index = bookSecondChoiceAdapter.getData().indexOf(item);
                helper.itemView.setOnFocusChangeListener(BookListActivity.this);
                helper/*.setChecked(R.id.check, index == position)*/
                        .setText(R.id.check, Util.showText(item.getVt_title(), item.getVt_title_z()));
            }
        };
        listView.setAdapter(bookSecondChoiceAdapter);
  /*      bookSecondChoiceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int prePosition = BookListActivity.this.position;
                BookListActivity.this.position = position;
                bookSecondChoiceAdapter.notifyItemChanged(position, "");
                bookSecondChoiceAdapter.notifyItemChanged(prePosition, "");
                page = 1;
                swip.setRefreshing(true);
                secondBean = bookSecondChoiceAdapter.getItem(position);
                getTypeBooks();
            }
        });*/
    }

    private void initBooks() {
        quickAdapter = new BaseQuickAdapter<MoreBookBean, BaseViewHolder>(R.layout.item_book_list) {
            @Override
            protected void convert(BaseViewHolder helper, MoreBookBean item) {
                helper.setText(R.id.title, Util.showText(item.getB_title(), item.getB_title_z()))
                        .setText(R.id.author, Util.showText(item.getB_title(), item.getB_title_z()));
                GlideUtils.getRequest(BookListActivity.this, Util.convertImgPath(item.getB_img())).placeholder(R.drawable.default_img)
                        .transform(new CenterCrop(), new RoundedCorners(AutoSizeUtils.dp2px(BookListActivity.this, 5)))
                        .override(85,110).into((ImageView) helper.getView(R.id.image));
            }
        };
        quickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MoreBookBean item = quickAdapter.getItem(position);
                if (item == null) return;
                Intent intent = new Intent(BookListActivity.this, PlayBookActivity.class);
                intent.putExtra("id", item.getB_id());
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
                getTypeBooks();
            }
        }, recyclerview);
        recyclerview.setAdapter(quickAdapter);
    }


    @Override
    public int setLayoutId() {
        return R.layout.activity_book_list;
    }

    /**
     * ??????Tab?????????
     */
    private void setTabView() {
        ViewHolder holder = null;
        for (int i = 0; i < categories.size(); i++) {
            //??????????????????
            TabLayout.Tab tab = tablayout.newTab();
            //???????????????????????????
            tab.setCustomView(R.layout.tab_item);
            tab.view.setOnFocusChangeListener(this);
            holder = new ViewHolder(tab.getCustomView());
            //?????????????????????
            CategoryBean categoryBean = categories.get(i);
            holder.tvTabName.setText(Util.showText(categoryBean.getVt_title(), categoryBean.getVt_title_z()));
            if (index == i) {
                tablayout.selectTab(tab);
            }
            tablayout.addTab(tab);
        }

        //tab?????????????????????
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                index = tab.getPosition();
                CategoryBean categoryBean = categories.get(index);
                position = 0;
                bookSecondChoiceAdapter.setNewData(categoryBean.getSecond());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private int keyCode;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.keyCode = keyCode;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v instanceof TabLayout.TabView) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    TabLayout.Tab tabAt = tablayout.getTabAt(index);
                    if (tabAt != null) {
                        tabAt.view.requestFocus();
                    }
                } else {
                    ViewGroup parent = (ViewGroup) v.getParent();
                    int index = parent.indexOfChild(v);
                    TabLayout.Tab tabAt = tablayout.getTabAt(index);
                    if (tabAt != null)
                        tabAt.select();
                }
            } else if (v.getId() == R.id.check) {
                page = 1;
                swip.setRefreshing(true);
                secondBean = bookSecondChoiceAdapter.getItem(position);
                getTypeBooks();
            }
        }
    }

    class ViewHolder {
        TextView tvTabName;
        ImageView ivTab;

        public ViewHolder(View tabView) {
            tvTabName = (TextView) tabView.findViewById(R.id.tv_tab_name);
            ivTab = (ImageView) tabView.findViewById(R.id.iv_tab);
        }
    }
}
