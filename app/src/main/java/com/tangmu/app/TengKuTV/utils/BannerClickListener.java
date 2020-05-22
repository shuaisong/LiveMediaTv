package com.tangmu.app.TengKuTV.utils;

import android.content.Context;
import android.content.Intent;

import com.tangmu.app.TengKuTV.bean.BannerBean;
import com.tangmu.app.TengKuTV.module.WebViewActivity;
import com.tangmu.app.TengKuTV.module.book.PlayBookActivity;
import com.tangmu.app.TengKuTV.module.live.LivingActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

public class BannerClickListener implements OnBannerListener {
    private List<BannerBean> bannerBeans;
    private Context context;

    public BannerClickListener(Context context) {
        this.context = context;
    }

    public void setBannerBeans(List<BannerBean> bannerBeans) {
        this.bannerBeans = bannerBeans;
    }

    @Override
    public void OnBannerClick(int position) {
        if (bannerBeans == null||bannerBeans.isEmpty()) return;
        BannerBean bannerBean = bannerBeans.get(position);
        Intent intent = null;
        switch (bannerBean.getB_type2()) {
            case 1:
                try {
                    intent = new Intent(context, TVDetailActivity.class);
                    intent.putExtra("id", Integer.valueOf(bannerBean.getB_url()));
                    intent.putExtra("c_id", bannerBean.getVt_id_one());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    intent = null;
                }
                break;
            case 2:
                try {
                    intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra("id", Integer.valueOf(bannerBean.getB_url()));
                    intent.putExtra("c_id", bannerBean.getVt_id_one());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    intent = null;
                }
                break;
            case 3:
                try {
                    intent = new Intent(context, PlayBookActivity.class);
                    intent.putExtra("id", Integer.valueOf(bannerBean.getB_url()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    intent = null;
                }
                break;
            case 4:
                intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", bannerBean.getB_url());
                break;
            case 5:
                break;
            case 6:
                if (bannerBean.getB_live_type() == 1) {//直播
                    intent = new Intent(context, LivingActivity.class);
                    intent.putExtra("url", bannerBean.getPull_url());
                    intent.putExtra("id", bannerBean.getB_url());
                }
                break;
        }
        if (intent != null) {
            context.startActivity(intent);
        }
    }
}
