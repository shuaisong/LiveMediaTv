package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.LiveBean;
import com.tangmu.app.TengKuTV.bean.LiveReplayBean;

import java.util.List;

public class LiveContact {
    public interface View extends BaseContact.BaseView {

        void showLiveReply(List<LiveReplayBean> liveBeans);

        void showTopLive(List<LiveBean> result);
    }

    public interface Presenter {

        void getLiveReply(int page);

        void getTopLive();
    }
}
