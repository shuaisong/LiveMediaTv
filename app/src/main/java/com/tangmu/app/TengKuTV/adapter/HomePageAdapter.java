package com.tangmu.app.TengKuTV.adapter;

import android.view.ViewGroup;

import com.tangmu.app.TengKuTV.bean.CategoryBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

public class HomePageAdapter extends FragmentStatePagerAdapter {
    private final FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public HomePageAdapter(FragmentManager fm, List<Class> fragments, List<CategoryBean> categoryBeans) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
        this.categoryBeans = categoryBeans;
        this.fragmentManager = fm;
    }

    private final List<Class> fragments;
    private final List<CategoryBean> categoryBeans;

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int i) {
        try {
            if (categoryBeans.get(i).getVt_pid() == -1){
                Method newInstance = fragments.get(i).getDeclaredMethod("newInstance", ArrayList.class);
                return  (Fragment)newInstance.invoke(fragments.get(i),categoryBeans);
            }else {
                Method newInstance = fragments.get(i).getDeclaredMethod("newInstance", CategoryBean.class);
                return  (Fragment)newInstance.invoke(fragments.get(i),categoryBeans.get(i));
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object instanceof Class) {
            for (int i = 0; i < fragments.size(); i++) {
                if (fragments.get(i) .equals(object)) {
                    return i;
                }
            }

        }
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        if (fragmentTransaction==null)fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach((Fragment) object);
    }
}
