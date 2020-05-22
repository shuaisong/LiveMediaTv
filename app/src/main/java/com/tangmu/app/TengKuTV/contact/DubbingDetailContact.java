package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.DubbingBean;
import com.tangmu.app.TengKuTV.bean.DubbingEvaluateBean;
import com.tangmu.app.TengKuTV.bean.DubbingListBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;

import java.util.List;

public class DubbingDetailContact {
    public interface View extends BaseContact.BaseView {
        void showDetail(DubbingBean dubbingBean);

        void collectSuccess(int uc_id);

        void unCollectSuccess();

        void showDubbings(List<DubbingListBean> result);

        void showRecmend(List<DubbingListBean> result);

        void showTVAd(List<VideoAdBean> result);
    }

    public interface Presenter {
        void getDetail(int uw_id);

        void collect(int id);

        void unCollect(int uc_id);

        void getDubbing(int page);

        void getRecomend();

        void getTvAd(int p_id);
    }
}
