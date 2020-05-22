package com.tangmu.app.TengKuTV.utils;

import android.content.Context;

import com.tangmu.app.TengKuTV.bean.LiveBannerBean;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

public class LiveBannerClickListener implements OnBannerListener {
    private List<LiveBannerBean> bannerBeans;
    private Context context;

    public LiveBannerClickListener(Context context) {
        this.context = context;
    }

    public void setBannerBeans(List<LiveBannerBean> bannerBeans) {
        this.bannerBeans = bannerBeans;
    }

    @Override
    public void OnBannerClick(int position) {
    }
}
