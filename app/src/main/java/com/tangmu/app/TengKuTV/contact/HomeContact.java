package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.CategoryBean;

import java.util.List;

public class HomeContact {
    public interface View extends BaseContact.BaseView {
        void showCategory(List<CategoryBean> categoryBeans);
    }

    public interface Presenter {
        void getCategory();
    }
}
