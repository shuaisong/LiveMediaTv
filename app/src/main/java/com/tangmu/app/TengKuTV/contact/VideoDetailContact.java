package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.AdBean;
import com.tangmu.app.TengKuTV.bean.BodyBean;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;
import com.tangmu.app.TengKuTV.bean.OrderBean;
import com.tangmu.app.TengKuTV.bean.SdkmesBean;
import com.tangmu.app.TengKuTV.bean.TVProductBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;
import com.tangmu.app.TengKuTV.bean.VideoDetailBean;

import java.util.List;

public class VideoDetailContact {
    public interface View extends BaseContact.BaseView {
        void showDetail(VideoDetailBean videoDetailBean);

        void showRecommend(List<HomeChildRecommendBean.VideoBean> videoBeans);

        void collectSuccess(Integer uc_id);

        void unCollectSuccess();

        void getPayResult();

        void showAd(AdBean adBean);

        void showAdError(String msg);

        void showTVAd(List<VideoAdBean> videoAdBeans);

        void showOrder(OrderBean result, String orderContentId);

        void showPayCode(String result);

        void showPayResult(boolean payResult, String msg);

        void AuthenticationFail(BodyBean productToOrderList);

        void showNetError(String msg);

        void showRechargeBeans(List<TVProductBean> result, String accountIdentify);

        void showMiguError(String resultDesc);
    }

    public interface Presenter {
        void getDetail(int vm_id);

        void getRecommend(int p_id);

        void unCollect(int vm_id);

        void collect(int vm_id);

        void getAd(int id);

        void getTvAd(int p_id);

        void createOrder(String price, int vip_type, String productCode, String accountIdentify, int id, String orderContentId);

        void weChatPayInfo(String order_no, String price);

        void miguAuthentications(String userId, String terminalId);

        void getProductList(String accountIdentify);

        void pay(int payType, String order, String price, String orderContentId);

        void miguPay(SdkmesBean sdkmesBean, String price, String orderContentId);

        void payStatus(String order_no);
    }
}
