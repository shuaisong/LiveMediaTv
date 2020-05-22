package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.BookDetailDataBean;

public class BookDetailContact {
    public interface View extends BaseContact.BaseView {
        void showDetail(BookDetailDataBean bookDetailDataBean);

        void collectSuccess(Integer uc_id);

        void unCollectSuccess();

    }

    public interface Presenter {
        void getDetail(int b_id);

        void unCollect(int b_id);

        void collect(int b_id);

    }
}
