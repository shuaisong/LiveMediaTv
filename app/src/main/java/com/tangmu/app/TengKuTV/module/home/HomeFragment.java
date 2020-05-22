package com.tangmu.app.TengKuTV.module.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.adapter.HomePageAdapter;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerFragmentComponent;
import com.tangmu.app.TengKuTV.contact.HomeContact;
import com.tangmu.app.TengKuTV.module.live.LiveFragment;
import com.tangmu.app.TengKuTV.presenter.HomePresenter;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements HomeContact.View {
    @Inject
    HomePresenter presenter;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    private ArrayList<CategoryBean> categories;
    private HomePageAdapter homePageAdapter;

    @Override
    protected void initData() {
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
        ArrayList<BaseFragment> fragments = new ArrayList<>(4);
        for (CategoryBean categoryBean : categoryBeans) {
            if (categoryBean.getVt_title().equals("VIP")) {
                fragments.add(HomeVipFragment.newInstance(categoryBean));
                CategoryBean livCategoryBean = new CategoryBean();
                livCategoryBean.setVt_title("直播");
                livCategoryBean.setVt_title_z("ཐད་གཏོང།");
                livCategoryBean.setVt_pid(-2);
                categoryBeans.add(categoryBeans.indexOf(categoryBean) + 1, livCategoryBean);
                fragments.add(new LiveFragment());
            } else if (categoryBean.getVt_title().contains("配音")) {
                PreferenceManager.getInstance().setDubbingId(categoryBean.getVt_id());
                fragments.add(HomeDubbingFragment.newInstance(categoryBean));
            } else {
                fragments.add(HomeChildFragment.newInstance(categoryBean));
            }
        }
        CategoryBean categoryBean = new CategoryBean();
        categoryBean.setVt_pid(-1);
        categoryBean.setVt_title(getString(R.string.all_channel));
        categoryBean.setVt_title_z(getString(R.string.all_channel));
        categories.add(categoryBean);

        ChannelFragment channelFragment = new ChannelFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Category", (ArrayList<CategoryBean>) categoryBeans);
        channelFragment.setArguments(bundle);
        fragments.add(channelFragment);
        if (homePageAdapter == null) {
            homePageAdapter = new HomePageAdapter(getChildFragmentManager(), fragments);
            mViewPager.setAdapter(homePageAdapter);
        } else homePageAdapter.notifyDataSetChanged();
        tablayout.setupWithViewPager(mViewPager);
        setTabView();
    }

    @Override
    public void showError(String msg) {
        ToastUtil.showText(msg);
    }

    public void setFragment(int position) {
        mViewPager.setCurrentItem(position);
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
            holder = new ViewHolder(tab.getCustomView());
            //为标签填充数据
            CategoryBean categoryBean = categories.get(i);
            if (categoryBean.getVt_title().equals("VIP")) {
                holder.tvTabName.setVisibility(View.GONE);
                holder.ivTab.setVisibility(View.VISIBLE);
                holder.ivTab.setImageResource(R.mipmap.ic_vip_title);
                holder.tabView.setBackground(getResources().getDrawable(R.drawable.vip_bg_focus));
            } else
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
        }
    }

    public void showDubbingFrament() {
        if (homePageAdapter == null) return;
        int itemPosition = homePageAdapter.getItemPosition(HomeDubbingFragment.class);
        if (itemPosition != PagerAdapter.POSITION_UNCHANGED) {
            mViewPager.setCurrentItem(itemPosition);
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
