package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.GiftBean;
import com.tangmu.app.TengKuTV.bean.LiveBannerBean;
import com.tangmu.app.TengKuTV.bean.LiveDetailBean;
import com.tangmu.app.TengKuTV.bean.LiveReplayBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;

import java.util.List;

public class LivingContact {
    public interface View extends BaseContact.BaseView {


        void collectSuccess(Integer uc_id);
        void unCollectSuccess();
        void showLiveDetail(LiveDetailBean result);

        void showLiveReply(List<LiveReplayBean> result);

        void showTVAd(List<VideoAdBean> result);
    }

    public interface Presenter {

        void unCollect(int roomId);

        void collect(int roomid);

        void getLiveDetail(int id);

        void getRecommend();

        void getTVAd();

    }
}
