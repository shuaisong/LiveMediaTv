package com.tangmu.app.TengKuTV.module.home;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.adapter.HomePageAdapter;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerFragmentComponent;
import com.tangmu.app.TengKuTV.contact.HomeContact;
import com.tangmu.app.TengKuTV.module.live.LiveFragment;
import com.tangmu.app.TengKuTV.module.main.MainActivity;
import com.tangmu.app.TengKuTV.presenter.HomePresenter;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class HomeFragment extends BaseFragment implements HomeContact.View, View.OnFocusChangeListener {
    @Inject
    HomePresenter presenter;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    private ArrayList<CategoryBean> categories;
    private HomePageAdapter homePageAdapter;

    @Override
    public void initData() {
        presenter.getCategory();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void initView() {
        presenter.attachView(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setupFragComponent(AppComponent appComponent) {
        DaggerFragmentComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void showCategory(List<CategoryBean> categoryBeans) {
        categories = (ArrayList<CategoryBean>) categoryBeans;
        ArrayList<Class> fragments = new ArrayList<>(4);
        for (CategoryBean categoryBean : categoryBeans) {
            if (categoryBean.getVt_title().equals("VIP")) {
                fragments.add(HomeVipFragment.class);
                CategoryBean livCategoryBean = new CategoryBean();
                livCategoryBean.setVt_title("??????");
                livCategoryBean.setVt_title_z("????????????????????????");
                livCategoryBean.setVt_pid(-2);
                categoryBeans.add(categoryBeans.indexOf(categoryBean) + 1, livCategoryBean);
                fragments.add(LiveFragment.class);
            } else if (categoryBean.getVt_title().contains("??????")) {
                PreferenceManager.getInstance().setDubbingId(categoryBean.getVt_id());
                fragments.add(HomeDubbingFragment.class);
            } else {
                fragments.add(HomeChildFragment.class);
            }
        }
        CategoryBean categoryBean = new CategoryBean();
        categoryBean.setVt_pid(-1);
        categoryBean.setVt_title(getString(R.string.all_channel));
        categoryBean.setVt_title_z(getString(R.string.all_channel));
        categories.add(categoryBean);

        fragments.add(ChannelFragment.class);
        if (homePageAdapter == null) {
            homePageAdapter = new HomePageAdapter(getChildFragmentManager(), fragments,categoryBeans);
            mViewPager.setAdapter(homePageAdapter);
        } else homePageAdapter.notifyDataSetChanged();
        tablayout.setupWithViewPager(mViewPager);
        setTabView();
        tablayout.getTabAt(0).view.requestFocus();
    }

    @Override
    public void showError(String msg) {
        ToastUtil.showText(msg);
    }

    public void setFragment(int position) {
        mViewPager.setCurrentItem(position);
        tablayout.getTabAt(position).view.postDelayed(new Runnable() {
            @Override
            public void run() {
                tablayout.getTabAt(position).view.requestFocus();
            }
        }, 100);
    }

    /**
     * ??????Tab?????????
     */
    private void setTabView() {
        ViewHolder holder = null;
        for (int i = 0; i < categories.size(); i++) {
            //??????????????????
            TabLayout.Tab tab = tablayout.getTabAt(i);
            //???????????????????????????

            tab.setCustomView(R.layout.tab_item);
            tab.view.setFocusableInTouchMode(true);
            tab.view.setOnFocusChangeListener(this);
            holder = new ViewHolder(tab.getCustomView());
            //?????????????????????
            CategoryBean categoryBean = categories.get(i);
            if (categoryBean.getVt_title().equals("VIP")) {
                holder.tvTabName.setVisibility(View.GONE);
                holder.ivTab.setVisibility(View.VISIBLE);
                holder.ivTab.setImageResource(R.mipmap.ic_vip_title);
            } else {
                if (categoryBean.getVt_title().equals("??????")) {
                    holder.tvTabName.setText(Util.showText("???  ???", categoryBean.getVt_title_z()));
                } else if (categoryBean.getVt_title().equals("??????")) {
                    holder.tvTabName.setText(Util.showText("???  ???", categoryBean.getVt_title_z()));
                } else if (categoryBean.getVt_title().equals("??????")) {
                    holder.tvTabName.setText(Util.showText("???  ???", categoryBean.getVt_title_z()));
                } else
                    holder.tvTabName.setText(Util.showText(categoryBean.getVt_title(), categoryBean.getVt_title_z()));
            }

        }

        //tab?????????????????????
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
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

    public ArrayList<CategoryBean> getCategory() {
        return categories;
    }

    public int getIndex() {
        return mViewPager.getCurrentItem();
    }

    public void showLiveFragment() {
        if (homePageAdapter == null) return;
        int itemPosition = homePageAdapter.getItemPosition(LiveFragment.class);
        if (itemPosition != PagerAdapter.POSITION_UNCHANGED) {
            mViewPager.setCurrentItem(itemPosition);
            tablayout.getTabAt(itemPosition).view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tablayout.getTabAt(itemPosition).view.requestFocus();
                }
            }, 100);
        }

    }

    public void showDubbingFrament() {
        if (homePageAdapter == null) return;
        int itemPosition = homePageAdapter.getItemPosition(HomeDubbingFragment.class);
        if (itemPosition != PagerAdapter.POSITION_UNCHANGED) {
            mViewPager.setCurrentItem(itemPosition);
            tablayout.getTabAt(itemPosition).view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tablayout.getTabAt(itemPosition).view.requestFocus();
                }
            }, 100);
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v instanceof TabLayout.TabView) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    if (activity.keyCode == KeyEvent.KEYCODE_DPAD_UP || activity.keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
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
        }
    }

    class ViewHolder {
        TextView tvTabName;
        ImageView ivTab;
        View tabView;

        public ViewHolder(View tabView) {
            this.tabView = tabView;
            tvTabName = (TextView) tabView.findViewById(R.id.tv_tab_name);
            ivTab = (ImageView) tabView.findViewById(R.id.iv_tab);
        }
    }

}
