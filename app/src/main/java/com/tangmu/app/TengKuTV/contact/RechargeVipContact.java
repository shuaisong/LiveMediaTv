package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.BodyBean;
import com.tangmu.app.TengKuTV.bean.MiguLoginBean;
import com.tangmu.app.TengKuTV.bean.OrderBean;
import com.tangmu.app.TengKuTV.bean.SdkmesBean;
import com.tangmu.app.TengKuTV.bean.TVProductBean;

import java.util.List;

public class RechargeVipContact {
    public interface View extends BaseContact.BaseView {
        void showOrder(OrderBean orderBean, String orderContentId);

        void showPayResult(boolean isSuccess,String msg);

        void showRechargeBeans(List<TVProductBean> result);

        void showUserInfo(MiguLoginBean userInfoBean);

        void showPayCode(String result);

        void AuthenticationFail(BodyBean productToOrderList);

        void getPayResult();

        void AuthenticationSuccess();

        void showPhone(String accountIdentify);

        void showNetError(String handleError);

        void showMiguError(String msg);
    }

    public interface Presenter {


        void createOrder(String price, int vip_type, String productCode, String accountIdentify, String orderContentId);

        void getUserInfo();

        void miguPay(SdkmesBean sdkmesBean, String price, String orderContentId);

        void miguAuthentications(String userId, String terminalId);

        void pay(int payType, String order, String price, String orderContentId);

        void payStatus(String orderNo);

        void getProductList();
    }
}
