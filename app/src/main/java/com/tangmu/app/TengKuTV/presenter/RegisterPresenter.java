package com.tangmu.app.TengKuTV.presenter;

import android.util.Base64;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.contact.RegisterContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;

import javax.inject.Inject;

public class RegisterPresenter extends RxPresenter<RegisterContact.View> implements RegisterContact.Presenter {

    @Inject
    public RegisterPresenter() {
    }

    @Override
    public void sendVerify(String mobile, int type) {
        OkGo.<BaseResponse<Object>>post(Constant.IP + Constant.sendCode)
                .tag(this)
                .params("mobile", mobile)
                .params("type", type)
                .execute(new JsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        if (response.body().getStatus() != 0) {
                            view.showVerifyError(response.body().getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<BaseResponse<Object>> response) {
                        super.onError(response);
                        view.showVerifyError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void register(String mobile, String password, String code) {
        password = Base64.encodeToString(password.getBytes(), Base64.DEFAULT);
        OkGo.<BaseResponse<Object>>post(Constant.IP + Constant.verifyCodeApiReg)
                .tag(this)
                .params("mobile", mobile)
                .params("password", password)
                .params("code", code)
                .execute(new JsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        if (response.body().getStatus() == 0) {
                            view.registerSuccess();
                        } else {
                            view.showError(response.body().getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<BaseResponse<Object>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }
}
