package com.tangmu.app.TengKuTV.module.playhistory;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.module.home.HomeHistoryFragment;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.TitleView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class PlayHistoryActivity extends BaseActivity {
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
        Fragment history = supportFragmentManager.findFragmentByTag("History");
        if (history == null) {
            HomeHistoryFragment homeHistoryFragment = new HomeHistoryFragment();
            homeHistoryFragment.setArguments(getIntent().getExtras());
            fragmentTransaction.
                    add(R.id.content, homeHistoryFragment, "History").commit();
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
        titleView.updateTV_Vip();
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
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_play_history;
    }


}
