package com.tangmu.app.TengKuTV.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tangmu.app.TengKuTV.base.BaseFragment;

import java.util.List;

public class BookPageAdapter extends FragmentPagerAdapter {
    public BookPageAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    private List<BaseFragment> fragments;


    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
