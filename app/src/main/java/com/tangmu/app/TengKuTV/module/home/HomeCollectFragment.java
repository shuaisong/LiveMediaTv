package com.tangmu.app.TengKuTV.module.home;

import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.module.mine.collect.CollectFragment;

import butterknife.BindView;

public class HomeCollectFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.radio_collect)
    RadioGroup radioCollect;
    private BaseFragment currentFragment;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        radioCollect.setOnCheckedChangeListener(this);
        radioCollect.check(R.id.video_collect);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home_collect;
    }

    @Override
    protected void setupFragComponent(AppComponent appComponent) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.video_collect:
                showFragment(0);
                break;
            case R.id.book_collect:
                showFragment(1);
                break;
            case R.id.dubbing_collect:
                showFragment(2);
                break;
        }
    }

    private void showFragment(int position) {
        FragmentManager supportFragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        Fragment fragmentByTag = supportFragmentManager.findFragmentByTag(String.valueOf(position));
        if (fragmentByTag != null) {
            fragmentTransaction.hide(currentFragment).show(fragmentByTag).commit();
            currentFragment = (BaseFragment) fragmentByTag;
        } else {
            BaseFragment baseFragment = newFragment(position);
            if (baseFragment != null) {
                if (currentFragment != null)
                    fragmentTransaction.hide(currentFragment).
                            add(R.id.content, baseFragment, String.valueOf(position)).commit();
                else fragmentTransaction.
                        add(R.id.content, baseFragment, String.valueOf(position)).commit();
                currentFragment = baseFragment;
            }
        }
    }

    private BaseFragment newFragment(int position) {
        switch (position) {
            case 0:
                return CollectFragment.getInstance(1);
            case 1:
                return CollectFragment.getInstance(3);
            case 2:
                return CollectFragment.getInstance(4);
        }
        return null;
    }
}
