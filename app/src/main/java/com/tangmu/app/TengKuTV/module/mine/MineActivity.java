package com.tangmu.app.TengKuTV.module.mine;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.TitleView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class MineActivity extends BaseActivity {
    @BindView(R.id.titleView)
    TitleView titleView;
    private Timer timer;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initData() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        Fragment mine = supportFragmentManager.findFragmentByTag("Mine");
        if (mine == null) {
            MineFragment mineFragment = new MineFragment();
            fragmentTransaction.
                    add(R.id.content, mineFragment, "Mine").commit();
        }
        titleView.findViewById(R.id.logo).setFocusable(false);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onDestroy() {
        if (timer != null) timer.cancel();
        super.onDestroy();
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

    public void updateTitle() {
        titleView.updateTV_Vip();
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_mine;
    }

}
