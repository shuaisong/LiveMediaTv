package com.tangmu.app.TengKuTV.module.main;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.adapter.HomePageAdapter;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.bean.VersionBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerActivityComponent;
import com.tangmu.app.TengKuTV.contact.HomeContact;
import com.tangmu.app.TengKuTV.module.home.ChannelFragment;
import com.tangmu.app.TengKuTV.module.home.HomeChildFragment;
import com.tangmu.app.TengKuTV.module.home.HomeDubbingFragment;
import com.tangmu.app.TengKuTV.module.home.HomeVipFragment;
import com.tangmu.app.TengKuTV.module.live.LiveFragment;
import com.tangmu.app.TengKuTV.presenter.HomePresenter;
import com.tangmu.app.TengKuTV.utils.InstallUtil;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.LoadingDialog;
import com.tangmu.app.TengKuTV.view.TitleView;
import com.tangmu.app.TengKuTV.view.VersionUpdateDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.inject.Inject;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;


public class MainActivity extends BaseActivity implements HomeContact.View, View.OnFocusChangeListener {
    @Inject
    HomePresenter presenter;
    @BindView(R.id.titleView)
    TitleView titleView;

    private LoadingDialog loadingDialog;
    private VersionBean versionBean;
    private VersionUpdateDialog updateDialog;
    private Timer timer;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    private ArrayList<CategoryBean> categories;
    private HomePageAdapter homePageAdapter;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void initData() {
        presenter.getCategory();
    }

    @Override
    protected void initView() {
        presenter.attachView(this);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        if (timer != null) timer.cancel();
        presenter.detachView();
        if (updateDialog != null) {
            updateDialog.dismiss();
        }
        super.onDestroy();
    }

    /**
     * Called when the focus state of a view has changed.
     *
     * @param v        The view whose state has changed.
     * @param hasFocus The new focus state of v.
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            TabLayout.Tab tabAt = tablayout.getTabAt(mViewPager.getCurrentItem());
            if (tabAt != null&&tabAt.view !=v) {
                v.clearFocus();
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

    static class ViewHolder {
        TextView tvTabName;
        ImageView ivTab;
        View tabView;

        public ViewHolder(View tabView) {
            this.tabView = tabView;
            tvTabName = (TextView) tabView.findViewById(R.id.tv_tab_name);
            ivTab = (ImageView) tabView.findViewById(R.id.iv_tab);
        }
    }
    private void setTabView() {
         ViewHolder holder = null;
        for (int i = 0; i < categories.size(); i++) {
            //依次获取标签
            TabLayout.Tab tab = tablayout.getTabAt(i);
            //为每个标签设置布局

            tab.setCustomView(R.layout.tab_item);
            tab.view.setFocusableInTouchMode(true);
            tab.view.setOnFocusChangeListener(this);
            holder = new ViewHolder(tab.getCustomView());
            //为标签填充数据
            CategoryBean categoryBean = categories.get(i);
            if (categoryBean.getVt_title().equals("VIP")) {
                holder.tvTabName.setVisibility(View.GONE);
                holder.ivTab.setVisibility(View.VISIBLE);
                holder.ivTab.setImageResource(R.mipmap.ic_vip_title);
            } else {
                if (categoryBean.getVt_title().equals("推荐")) {
                    holder.tvTabName.setText(Util.showText("推  荐", categoryBean.getVt_title_z()));
                } else if (categoryBean.getVt_title().equals("配音")) {
                    holder.tvTabName.setText(Util.showText("配  音", categoryBean.getVt_title_z()));
                } else if (categoryBean.getVt_title().equals("直播")) {
                    holder.tvTabName.setText(Util.showText("直  播", categoryBean.getVt_title_z()));
                } else
                    holder.tvTabName.setText(Util.showText(categoryBean.getVt_title(), categoryBean.getVt_title_z()));
            }

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
    @Override
    public int setLayoutId() {
        return R.layout.activity_main1;
    }

    @Override
    public void showCategory(List<CategoryBean> categoryBeans) {
        categories = (ArrayList<CategoryBean>) categoryBeans;
        ArrayList<Class> fragments = new ArrayList<>(4);
        for (CategoryBean categoryBean : categoryBeans) {
            if (categoryBean.getVt_title().equals("VIP")) {
                fragments.add(HomeVipFragment.class);
                CategoryBean livCategoryBean = new CategoryBean();
                livCategoryBean.setVt_title("直播");
                livCategoryBean.setVt_title_z("ཐད་གཏོང།");
                livCategoryBean.setVt_pid(-2);
                categoryBeans.add(categoryBeans.indexOf(categoryBean) + 1, livCategoryBean);
                fragments.add(LiveFragment.class);
            } else if (categoryBean.getVt_title().contains("配音")) {
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

        mViewPager.setOffscreenPageLimit(3);
        if (homePageAdapter == null) {
            homePageAdapter = new HomePageAdapter(getSupportFragmentManager(), fragments,categoryBeans);
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
    @Override
    protected void onResume() {
        super.onResume();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//            currentVersion.setText(String.format(getString(R.string.current_version), packageInfo.versionCode));
            getNewVersion(packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        titleView.updateTV_Vip();
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new UpdateTimeTask(this,titleView), 1000, 1000);
        }

    }

    public int keyCode;

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
    public void onNetChange(boolean hasNet) {
        super.onNetChange(hasNet);
        if (hasNet  ) {
            initData();
        }
    }

    private void getNewVersion(int versionCode) {
        OkGo.<BaseResponse<VersionBean>>post(Constant.IP + Constant.detectionNewVersion)
                .params("v_type", 4)
                .params("code", versionCode).tag(this)
                .execute(new JsonCallback<BaseResponse<VersionBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<VersionBean>> response) {
                        if (response.body().getStatus() == 0) {
                            versionBean = response.body().getResult();
                            tipDialog(versionBean);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<VersionBean>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });


    }


    private void tipDialog(VersionBean result) {
        if (updateDialog == null){
            updateDialog = new VersionUpdateDialog(this, result);
            updateDialog.setPayClickListener(new VersionUpdateDialog.VersionUpdateListener() {
                @Override
                public void versionUpdate(View view) {
                downLoadApk(Constant.Pic_IP + result.getV_url());
                }
            });}
        updateDialog.show();
    }
    public ArrayList<CategoryBean> getCategory() {
        return categories;
    }

    public int getIndex() {
        return mViewPager.getCurrentItem();
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
    public void showLiveFragment() {
        if (homePageAdapter == null) return;
        int itemPosition = homePageAdapter.getItemPosition(LiveFragment.class);
        if (itemPosition != mViewPager.getCurrentItem()) {
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
        if (itemPosition != mViewPager.getCurrentItem()) {
            mViewPager.setCurrentItem(itemPosition);
            tablayout.getTabAt(itemPosition).view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tablayout.getTabAt(itemPosition).view.requestFocus();
                }
            }, 100);
        }
    }
    private void downLoadApk(String url) {
        loadingDialog = new LoadingDialog(this);
        OkGo.<File>get(url).params("If-Modified-Since", System.currentTimeMillis())
                .execute(new FileCallback(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
                        , "tengku.apk") {
                    @Override
                    public void onSuccess(Response<File> response) {
                        loadingDialog.dismiss();
                        InstallUtil.installApk(response.body().getAbsolutePath());
                    }

                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        File file = new File(Environment.
                                getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tengku.apk");
                        if (file.exists()) file.delete();
                        loadingDialog.show();
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        loadingDialog.dismiss();
                        ToastUtil.showText(Util.handleError(response.getException()));
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        loadingDialog.setProgress((int) (progress.fraction * 100));
                    }
                });
    }

}
