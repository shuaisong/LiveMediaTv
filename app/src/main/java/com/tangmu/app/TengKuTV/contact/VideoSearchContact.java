package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;
import com.tangmu.app.TengKuTV.bean.VideoRecmendSeachBean;

import java.util.List;

public class VideoSearchContact {
    public interface View extends BaseContact.BaseView {


        void showVideoOrders(List<VideoRecmendSeachBean> serachVideoBeans);

        void showVideos(List<HomeChildRecommendBean.VideoBean> videoBeans);

    }

    public interface Presenter {

        void getVideoRecommend();

        void getVideos(String content);

        void addSearchNum(int vm_id);
    }
}
