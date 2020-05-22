package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.BannerBean;
import com.tangmu.app.TengKuTV.bean.HomeChildBean;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;

import java.util.List;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class HomeChildContact {
    public interface View extends BaseContact.BaseView {
        void showError(String msg);

        void ShowBanner(List<BannerBean> bannerBeans);

        void showMovie(List<HomeChildBean> movieBeans);

        void showChildMovie(List<HomeChildRecommendBean.VideoBean> movieBeans);
    }

    public interface Presenter {

        void getRecMovie(int type);

        void updateRecommend(int s_sorts);

        void getBanner(int pid);
    }
}
