package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.DubbingMaterialBean;

import java.io.File;

public class DubbingRankContact {
    public interface View extends BaseContact.BaseView {
        void downloadSuccess(File file);

        void showDetail(DubbingMaterialBean result);

        void showProgress(int fraction);
    }

    public interface Presenter {
        void getDetail(int id);

        void downloadFile(String url);
    }
}
