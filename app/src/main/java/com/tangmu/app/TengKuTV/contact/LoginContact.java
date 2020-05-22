package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.QQInfoBean;
import com.tangmu.app.TengKuTV.bean.WXinfoBean;

public class LoginContact {
    public interface View extends BaseContact.BaseView {
        void loginSuccess();

        void bindThird(String openId, String head_img, int type);

        void getSignature(String s, String ticket);
    }

    public interface Presenter {
        void login(String mobile, String password);

        void getAccessToken(String code);

        void isBindMobile(WXinfoBean wXinfoBean, QQInfoBean qqInfoBean, int type);

        void getToken(String timestamp);
    }
}
