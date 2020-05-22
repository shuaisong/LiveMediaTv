package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.BannerBean;
import com.tangmu.app.TengKuTV.bean.DubbingListBean;

import java.util.List;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class HomeDubbingContact {
    public interface View extends BaseContact.BaseView {
        void ShowBanner(List<BannerBean> bannerBeans);

        void showDubbing(List<DubbingListBean> dubbingListBeans);
    }

    public interface Presenter {
        void getBanner(int vt_id);

        void getDubbing(int type);

    }
}
