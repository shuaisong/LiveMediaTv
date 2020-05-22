package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.LiveEvaluateBean;

import java.util.List;

public class LiveHistoryEvaluteContact {
    public interface View extends BaseContact.BaseView {

        void showEvaluates(List<LiveEvaluateBean> evaluateBeans);

        void praiseSuccess(int position);

        void unPraiseSuccess(int position);

        void showEvaluatesFail(String msg);
    }

    public interface Presenter {

        void praise(int lc_id, int position);

        void unPraise(int lc_id, int position);

        void getEvaluates(int page, int id);

    }
}
