package com.tangmu.app.TengKuTV.module.main;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TableLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.bean.LoginBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerActivityComponent;
import com.tangmu.app.TengKuTV.contact.MainContact;
import com.tangmu.app.TengKuTV.module.home.HomeFragment;
import com.tangmu.app.TengKuTV.module.vip.FreeVipActivity;
import com.tangmu.app.TengKuTV.presenter.MainPresenter;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.FreeVIPTip;
import com.tangmu.app.TengKuTV.view.TitleView;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;


public class MainActivity extends BaseActivity implements MainContact.View {
    @Inject
    MainPresenter presenter;
    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.content)
    FrameLayout content;
    private Timer timer;
    private FreeVIPTip freeVIPTip;
    private HomeFragment homeFragment;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void initData() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        homeFragment = (HomeFragment) supportFragmentManager.findFragmentByTag("home");
        if (homeFragment == null) {
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            homeFragment = new HomeFragment();
            fragmentTransaction.
                    add(R.id.content, homeFragment, "home").commit();
        }
        if (PreferenceManager.getInstance().getVisitor() == null)
            presenter.getVisitor();
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
        super.onDestroy();
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        titleView.updateTV_Vip();
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LoginBean login = PreferenceManager.getInstance().getLogin();
                            if (login != null && login.getIs_receive() != 2) {
                                initReceiveVIP();
                            }
                            long currentTimeMillis = System.currentTimeMillis();
                            titleView.setTime(Util.convertSystemTime(currentTimeMillis));
                        }
                    });
                }
            }, 1000, 1000);
        }

    }

    private void initReceiveVIP() {
        if (freeVIPTip == null) {
            freeVIPTip = new FreeVIPTip(MainActivity.this);
            freeVIPTip.show(content);
        }

    }

    public int keyCode;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.keyCode = keyCode;
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (freeVIPTip != null && freeVIPTip.isShowing()) {
                startActivity(new Intent(this, FreeVipActivity.class));
                freeVIPTip.dismiss();
            }
        }
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
        if (hasNet && homeFragment != null && homeFragment.getCategory() == null) {
            homeFragment.initData();
        }
    }
}
