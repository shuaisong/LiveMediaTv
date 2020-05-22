package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.AdBean;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;
import com.tangmu.app.TengKuTV.bean.VideoDetailBean;

import java.util.List;

public class VideoDetailContact {
    public interface View extends BaseContact.BaseView {
        void showDetail(VideoDetailBean videoDetailBean);

        void showRecommend(List<HomeChildRecommendBean.VideoBean> videoBeans);

        void collectSuccess(Integer uc_id);

        void unCollectSuccess();

        void showAd(AdBean adBean);

        void showAdError(String msg);

        void showTVAd(List<VideoAdBean> videoAdBeans);
    }

    public interface Presenter {
        void getDetail(int vm_id);

        void getRecommend(int p_id);

        void unCollect(int vm_id);

        void collect(int vm_id);

        void getAd(int id);

        void getTvAd(int p_id);
    }
}
