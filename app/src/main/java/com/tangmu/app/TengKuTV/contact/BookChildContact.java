package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.AdBean;
import com.tangmu.app.TengKuTV.bean.BannerBean;
import com.tangmu.app.TengKuTV.bean.MoreBookBean;

import java.util.List;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class BookChildContact {
    public interface View extends BaseContact.BaseView {
        void showError(String msg);

        void ShowBanner(List<BannerBean> bannerBeans);

        void showRecBooks(List<MoreBookBean> moreBookBeanList);

        void showBooks(List<MoreBookBean> bookBeanList);

    }

    public interface Presenter {

        void getRecBook(int type);

        void getBooks(int type);

        void getBanner(int pid);

    }
}
