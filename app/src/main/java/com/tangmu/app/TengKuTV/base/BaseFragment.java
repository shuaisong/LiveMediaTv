package com.tangmu.app.TengKuTV.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.utils.GlideApp;
import com.tangmu.app.TengKuTV.utils.GlideRequest;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.view.GlideCircleWithBorder;

import butterknife.ButterKnife;
import me.jessyan.autosize.utils.AutoSizeUtils;


/**
 * Created by Administrator on 2019/5/27 0027.
 */

public abstract class BaseFragment extends Fragment {
    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutID(), container, false);
            setupFragComponent(CustomApp.getApp().getAppComponent());
            ButterKnife.bind(this, rootView);
            initView();
            initData();
        }
        return rootView;
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化view
     */
    protected abstract void initView();

    protected void hindSoft() {
        InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            manager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    protected boolean isLogin() {
        if (PreferenceManager.getInstance().getLogin() != null) return true;
        else {
            return false;
        }
    }

    protected boolean isClickLogin() {
        if (PreferenceManager.getInstance().getLogin() != null) return true;
        else {
            ToastUtil.showText(getString(R.string.login_tip1));
            return false;
        }
    }

    /**
     * @return xml id
     */
    protected abstract int getLayoutID();

    /**
     * mvp 关联
     *
     * @param appComponent appComponent
     */
    protected abstract void setupFragComponent(AppComponent appComponent);

    protected void setHead(String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions().error(R.mipmap.default_head).
                placeholder(R.mipmap.default_head)
                .override(100, 100)
                .circleCrop();//错误图、占位图
        GlideRequest<Drawable> apply = GlideApp.with(this)
                .load(R.mipmap.default_head).apply(requestOptions);//解决占位图非圆形问题
        GlideUtils.getRequest(this, url).apply(requestOptions).thumbnail(apply)
                .into(imageView);
    }

    protected void setHeadWithBorder(String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions().error(R.mipmap.default_head).
                placeholder(R.mipmap.default_head)
                .override(100, 100)
                .transform(new GlideCircleWithBorder(getActivity(), 1, Color.WHITE));//错误图、占位图
        GlideRequest<Drawable> apply = GlideApp.with(this)
                .load(R.mipmap.default_head).apply(requestOptions);//解决占位图非圆形问题
        GlideUtils.getRequest(this, url).apply(requestOptions).thumbnail(apply)
                .into(imageView);
    }

    protected void setRoundedHead(String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions().error(R.mipmap.default_head).
                placeholder(R.mipmap.default_head)
                .override(100, 100)
                .transform(new CircleCrop(), new RoundedCorners(AutoSizeUtils.dp2px(getContext(), 1)));//错误图、占位图
        GlideRequest<Drawable> apply = GlideApp.with(this)
                .load(R.mipmap.default_head).apply(requestOptions);//解决占位图非圆形问题
        GlideUtils.getRequest(this, url).apply(requestOptions).thumbnail(apply)
                .into(imageView);
    }

}
