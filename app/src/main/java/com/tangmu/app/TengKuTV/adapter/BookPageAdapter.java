package com.tangmu.app.TengKuTV.adapter;

import android.os.Build;
import android.view.ViewGroup;

import com.tangmu.app.TengKuTV.bean.CategoryBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

public class BookPageAdapter extends FragmentStatePagerAdapter {
    public BookPageAdapter(FragmentManager fm, List<Class> fragments, List<CategoryBean> categoryBeans) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
        this.categoryBeans = categoryBeans;
        this.fragmentManager = fm;

    }
    private FragmentManager fragmentManager;
    private List<Class> fragments;
    private List<CategoryBean> categoryBeans;
    private FragmentTransaction fragmentTransaction;

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int i) {
        try {
            Method newInstance = fragments.get(i).getDeclaredMethod("newInstance", CategoryBean.class);
            return   (Fragment)newInstance.invoke(fragments.get(i),categoryBeans.get(i));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        if (fragmentTransaction==null)fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach((Fragment) object);
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
