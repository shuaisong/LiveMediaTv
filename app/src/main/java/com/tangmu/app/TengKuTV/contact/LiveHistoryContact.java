package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.LiveHistoryBean;
import com.tangmu.app.TengKuTV.bean.LiveReplayBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;

import java.util.List;

public class LiveHistoryContact {
    public interface View extends BaseContact.BaseView {


        void showDetail(LiveHistoryBean liveHistoryBean);

        void showLiveReply(List<LiveReplayBean> result);

        void showTVAd(List<VideoAdBean> result);
    }

    public interface Presenter {


        void getDetail(int id);

        void getRecommend();

        void getTVAd();
    }
}
