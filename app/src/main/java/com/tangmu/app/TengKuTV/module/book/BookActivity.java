package com.tangmu.app.TengKuTV.module.book;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.adapter.BookPageAdapter;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.module.playhistory.PlayHistoryActivity;
import com.tangmu.app.TengKuTV.module.search.BookSearchActivity;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.TitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class BookActivity extends BaseActivity implements View.OnFocusChangeListener {

    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.book_viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    private BookPageAdapter bookPageAdapter;
    private ArrayList<Class> fragments = new ArrayList<>();
    private ArrayList<CategoryBean> categories = new ArrayList<>();
    private Timer timer;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }


    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        getCategories();
    }

    /**
     * 初始化view
     */
    @Override
    protected void initView() {
        initNavigation();
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_book;
    }

    private void initNavigation() {

    }

    @OnClick({R.id.search, R.id.history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search:
                startActivity(new Intent(this, BookSearchActivity.class));
                break;
            case R.id.history:
                startActivity(new Intent(this, PlayHistoryActivity.class));
                break;
        }
    }


    private void getCategories() {
        OkGo.<BaseListResponse<CategoryBean>>post(Constant.IP + Constant.bookType)
                .tag(this)
                .execute(new JsonCallback<BaseListResponse<CategoryBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<CategoryBean>> response) {
                        if (response.body().getStatus() == 0) {
                            List<CategoryBean> categoryBeans = response.body().getResult();
                            categories.clear();
                            fragments.clear();
                            categories.addAll(categoryBeans);
                            for (int i = 0; i < categoryBeans.size(); i++) {
                                fragments.add(BookChildFragment.class);
                            }

                            if (bookPageAdapter == null) {
                                bookPageAdapter = new BookPageAdapter(getSupportFragmentManager(), fragments,categoryBeans);
                                mViewPager.setAdapter(bookPageAdapter);
                            } else {
                                bookPageAdapter.notifyDataSetChanged();
                            }
                            tablayout.setupWithViewPager(mViewPager);
                            setTabView();
                            tablayout.getTabAt(0).view.requestFocus();
                        } else {
                            ToastUtil.showText(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<CategoryBean>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }


    @Override
    public void startActivity(Intent intent) {
        intent.putExtra("PCategory", categories);
        intent.putExtra("index", mViewPager.getCurrentItem());
        super.startActivity(intent);
    }

    public ArrayList<CategoryBean> getCategory() {
        return categories;
    }

    public int getIndex() {
        return mViewPager.getCurrentItem();
    }

    /**
     * 设置Tab的样式
     */
    private void setTabView() {
        ViewHolder holder = null;
        for (int i = 0; i < categories.size(); i++) {
            //依次获取标签
            TabLayout.Tab tab = tablayout.getTabAt(i);
            //为每个标签设置布局
            tab.setCustomView(R.layout.tab_item);
            tab.view.setOnFocusChangeListener(this);
            holder = new ViewHolder(tab.getCustomView());
            //为标签填充数据
            CategoryBean categoryBean = categories.get(i);
            holder.tvTabName.setText(Util.showText(categoryBean.getVt_title(), categoryBean.getVt_title_z()));

        }

        //tab选中的监听事件
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                mViewPager.setCurrentItem(tab.getPosition());

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
        View currentFocus = getCurrentFocus();
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && currentFocus != null) {
            View view = currentFocus.focusSearch(View.FOCUS_LEFT);
            if (view != null) {
                if (view instanceof TabLayout.TabView && !(currentFocus instanceof TabLayout.TabView)) {
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v instanceof TabLayout.TabView && v.hasFocus()) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                TabLayout.Tab tabAt = tablayout.getTabAt(mViewPager.getCurrentItem());
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
        }
    }


    static class ViewHolder {
        TextView tvTabName;
        ImageView ivTab;

        public ViewHolder(View tabView) {
            tvTabName = (TextView) tabView.findViewById(R.id.tv_tab_name);
            ivTab = (ImageView) tabView.findViewById(R.id.iv_tab);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        titleView.updateTV_Vip();
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new UpdateTimeTask(this,titleView), 0, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null) timer.cancel();
        if (fragments!=null)fragments.clear();
        super.onDestroy();
    }
}
