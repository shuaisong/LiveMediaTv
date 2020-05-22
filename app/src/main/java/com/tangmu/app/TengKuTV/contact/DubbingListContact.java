package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.DubbingListBean;

import java.util.List;

public class DubbingListContact {
    public interface View extends BaseContact.BaseView {
        void showDubbings(List<DubbingListBean> dubbingListBeans);

        void praiseSuccess(int position);

        void unPraiseSuccess(int position);
    }

    public interface Presenter {
        void getDubbing(int page);

        void praise(int id, int position);

        void unPraise(int id, int position);

        void share(int id);
    }
}
