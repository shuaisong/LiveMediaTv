package com.tangmu.app.TengKuTV.contact;

import com.tangmu.app.TengKuTV.base.BaseContact;

public class RegisterContact {
    public interface View extends BaseContact.BaseView {
        void registerSuccess();

        void showVerifyError(String msg);
    }

    public interface Presenter {
        void register(String mobile, String password, String code);

        void sendVerify(String mobile, int type);//忘记密码发送验证码是传1 用户注册时传2
    }
}
