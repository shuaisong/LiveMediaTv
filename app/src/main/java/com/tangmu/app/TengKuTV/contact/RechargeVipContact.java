package com.tangmu.app.TengKuTV.contact;

import android.os.Handler;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.OrderBean;
import com.tangmu.app.TengKuTV.bean.RechargeGifBean;
import com.tangmu.app.TengKuTV.bean.UserInfoBean;
import com.tangmu.app.TengKuTV.bean.VipBean;
import com.tangmu.app.TengKuTV.bean.WxBean;

import java.util.List;

public class RechargeVipContact {
    public interface View extends BaseContact.BaseView {
        void showVipBeans(List<VipBean> vipBeans);
        void showOrder(OrderBean orderBean);

        void showPayResult(boolean isSuccess);

        void showRechargeBeans(List<RechargeGifBean> result);

        void showUserInfo(UserInfoBean userInfoBean);

        void showPayCode(String result);
    }

    public interface Presenter {
        void getVipBeans();


        void weChatPayInfo(String order_num, String price);

        void createOrder(String ca_id ,int vip_type);

        void weChatPay(WxBean bean);

        void getUserInfo();

        void getRecharges();
    }
}
