package com.tangmu.app.TengKuTV.module.home;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.component.AppComponent;

import butterknife.BindView;


public class HomeHistoryFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.radio_home_history)
    RadioGroup radioHomeHistory;
    @BindView(R.id.play_record)
    RadioButton playRecord;
    @BindView(R.id.play_collect)
    RadioButton playCollect;
    private BaseFragment currentFragment;

    @Override
    protected void initData() {
        showFragment(0);
    }

    @Override
    protected void initView() {
        Bundle arguments = getArguments();
        int position = 0;
        if (arguments != null)
            position = arguments.getInt("position");
        if (position == 0)
            radioHomeHistory.check(R.id.play_record);
        else radioHomeHistory.check(R.id.play_collect);
        radioHomeHistory.setOnCheckedChangeListener(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home_history;
    }

    @Override
    protected void setupFragComponent(AppComponent appComponent) {

    }

    private void showFragment(int position) {
        FragmentManager supportFragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        Fragment fragmentByTag = supportFragmentManager.findFragmentByTag(String.valueOf(position));
        if (fragmentByTag != null) {
            if (currentFragment != null) {
                if (currentFragment != fragmentByTag)
                    fragmentTransaction.hide(currentFragment).show(fragmentByTag).commit();
            }
            currentFragment = (BaseFragment) fragmentByTag;
        } else {
            BaseFragment baseFragment = newFragment(position);
            if (baseFragment != null) {
                if (currentFragment != null) {
                    fragmentTransaction.hide(currentFragment).
                            add(R.id.content, baseFragment, String.valueOf(position)).commit();
                } else fragmentTransaction.
                        add(R.id.content, baseFragment, String.valueOf(position)).commit();
                currentFragment = baseFragment;
            }
        }
    }

    private BaseFragment newFragment(int position) {
        switch (position) {
            case 0:
                return new HomeVideoHistoryFragment();
            case 1:
                return new HomeCollectFragment();
        }
        return null;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.play_record:
                showFragment(0);
                break;
            case R.id.play_collect:
                if (isClickLogin())
                    showFragment(1);
                else {
                    radioHomeHistory.check(R.id.play_record);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (radioHomeHistory.getCheckedRadioButtonId() == R.id.play_collect) {
            if (isLogin())
                showFragment(1);
        }
    }

}
