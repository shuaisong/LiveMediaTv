package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;
import com.tangmu.app.TengKuTV.bean.LoginBean;

public class ThirdBindContact {
    public interface View extends BaseContact.BaseView {
        void loginSuccess(LoginBean loginBean);

        void showVerifyError(String msg);
    }

    public interface Presenter {
        void thirdLogin(String openId, String mobile, String head_img, String code, int type);

        void sendVerify(String mobile);
    }
}
