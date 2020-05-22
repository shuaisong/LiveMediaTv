package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.CollectBean;

import java.util.List;

public class CollectContact {
    public interface View extends BaseContact.BaseView {
        void unCollectSuccess();

        void showCollects(List<CollectBean> collectBeans);

        void showCollectsFail(String msg);
    }

    public interface Presenter {
        void unCollect(String uc_id);

        void getCollects(int page, int type);
    }

}
