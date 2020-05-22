package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.CategoryBean;

import java.util.List;

public class MainContact {
    public interface View extends BaseContact.BaseView {
    }

    public interface Presenter {

        void getVisitor();
    }
}
