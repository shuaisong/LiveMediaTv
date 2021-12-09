package com.tangmu.app.TengKuTV.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.LoginBean;
import com.tangmu.app.TengKuTV.contact.ThirdBindContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.Util;

import javax.inject.Inject;

public class ThirdBindPresenter extends RxPresenter<ThirdBindContact.View> implements ThirdBindContact.Presenter {
    @Inject
    public ThirdBindPresenter() {
    }

    @SuppressLint("MissingPermission")
    @Override
    public void thirdLogin(String openId, String mobile, String head_img, String code, String nickName, int type) {
        TelephonyManager telephonyManager = (TelephonyManager) ((Activity) view).getSystemService(Context.TELEPHONY_SERVICE);
        OkGo.<BaseResponse<LoginBean>>post(Constant.IP + Constant.mobileBind)
                .params("mobile", mobile)
                .params("type", type)
                .params("code", code)
                .params("openid", openId)
                .params("device_no", TextUtils.isEmpty(telephonyManager.getDeviceId())? Util.getPhoneSign():telephonyManager.getDeviceId())
                .params("u_img", head_img)
                .params("u_nick_name", nickName)
                .tag(this)
                .execute(new JsonCallback<BaseResponse<LoginBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<LoginBean>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            LoginBean result = response.body().getResult();
                            view.loginSuccess(result);
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<LoginBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void sendVerify(String mobile) {
        OkGo.<BaseResponse<Object>>post(Constant.IP + Constant.sendCode)
                .tag(this)
                .params("mobile", mobile)
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
}
