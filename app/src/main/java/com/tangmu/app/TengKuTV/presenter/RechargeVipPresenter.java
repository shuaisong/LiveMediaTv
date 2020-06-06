package com.tangmu.app.TengKuTV.presenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.alipay.sdk.app.PayTask;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.OrderBean;
import com.tangmu.app.TengKuTV.bean.RechargeGifBean;
import com.tangmu.app.TengKuTV.bean.UserInfoBean;
import com.tangmu.app.TengKuTV.bean.VipBean;
import com.tangmu.app.TengKuTV.bean.WxBean;
import com.tangmu.app.TengKuTV.contact.RechargeVipContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.sql.Time;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

public class RechargeVipPresenter extends RxPresenter<RechargeVipContact.View> implements RechargeVipContact.Presenter {

    private Timer timer;
    private String order_num;

    @Inject
    public RechargeVipPresenter() {
    }

    @Override
    public void getUserInfo() {
        OkGo.<BaseResponse<UserInfoBean>>post(Constant.IP + Constant.userMes)
                .params("token", PreferenceManager.getInstance().getLogin().getToken())
                .execute(new JsonCallback<BaseResponse<UserInfoBean>>() {
                    @Override
                    public void onVerifySuccess(Response<BaseResponse<UserInfoBean>> response) {
                        if (response.body().getStatus() == 0) {
                            PreferenceManager.getInstance().updateLogin(response.body().getResult());
                            if (view != null)
                                view.showUserInfo(response.body().getResult());
                        } else {
                            if (view != null)
                                ToastUtil.showText(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<UserInfoBean>> response) {
                        super.onError(response);
                        if (view != null)
                            ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getRecharges() {
        OkGo.<BaseListResponse<RechargeGifBean>>post(Constant.IP + Constant.gold)
                .tag(this)
                .params("type", 1)
                .execute(new JsonCallback<BaseListResponse<RechargeGifBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<RechargeGifBean>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            view.showRechargeBeans(response.body().getResult());
                        } else {
                            ToastUtil.showText(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<RechargeGifBean>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getVipBeans() {
        OkGo.<BaseListResponse<VipBean>>post(Constant.IP + Constant.vipMes)
                .tag(this)
                .params("type", 1)
                .execute(new JsonCallback<BaseListResponse<VipBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<VipBean>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            view.showVipBeans(response.body().getResult());
                        } else {
                            ToastUtil.showText(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<VipBean>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }


    @Override
    public void weChatPayInfo(String order_num, String price) {
        RechargeVipPresenter.this.order_num = order_num;
        OkGo.<BaseResponse<String>>post(Constant.IP + Constant.pay)
                .cacheMode(CacheMode.NO_CACHE)
                .params("token", PreferenceManager.getInstance().getLogin().getToken())
                .params("order_no", order_num)
                .params("pay_method", 5)
                .params("price", price).tag(this)
                .execute(new JsonCallback<BaseResponse<String>>() {
                    @Override
                    public void onVerifySuccess(Response<BaseResponse<String>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showPayCode(response.body().getResult());
                            getPayResult();
                        } else view.showError(response.body().getMsg());
                    }

                    @Override
                    public void onError(Response<BaseResponse<String>> response) {
                        super.onError(response);
                        view.showError(response.body().getMsg());
                    }
                });
    }

    private void getPayResult() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(order_num))
                        getWxPayResult(order_num);
                }
            }, 2000, 2000);
        }

    }

    private void getWxPayResult(String order_num) {
        OkGo.<BaseResponse<Integer>>post(Constant.IP + Constant.payCallbacks)
                .params("order_no", order_num)
                .execute(new JsonCallback<BaseResponse<Integer>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Integer>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            if (response.body().getResult() == 2) {
                                view.showPayResult(true);
                                timer.cancel();
                                timer = null;
                            } else {

                            }
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<Integer>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                    }
                });
    }

    @Override
    public void createOrder(String price, int vip_type) {
        OkGo.<BaseResponse<OrderBean>>post(Constant.IP + Constant.createOrder)
                .params("token", PreferenceManager.getInstance().getLogin().getToken())
                .params("price", price)
                .params("vip_type", vip_type)
                .params("type", 1).execute(new JsonCallback<BaseResponse<OrderBean>>() {
            @Override
            protected void onVerifySuccess(Response<BaseResponse<OrderBean>> response) {
                super.onVerifySuccess(response);
                if (response.body().getStatus() == 0)
                    view.showOrder(response.body().getResult());
                else view.showError(response.body().getMsg());
            }

            @Override
            public void onError(Response<BaseResponse<OrderBean>> response) {
                super.onError(response);
                view.showError(handleError(response.getException()));
            }
        });
    }

    @Override
    public void weChatPay(WxBean bean) {
        IWXAPI api;
        if (view instanceof Fragment)
            api = WXAPIFactory.createWXAPI(((Fragment) view).getContext(), bean.getAppid(), false);
        else {
            api = WXAPIFactory.createWXAPI(((Activity) view), bean.getAppid(), false);
        }
        if (!api.isWXAppInstalled()) {
            view.showPayResult(false);
            ToastUtil.showText(CustomApp.getApp().getString(R.string.wechat_install_tip));
            return;
        }
        PayReq request = new PayReq();
        request.appId = bean.getAppid();
        request.partnerId = bean.getPartnerid();
        request.prepayId = bean.getPrepayid();
        request.packageValue = bean.getPackages();
        request.nonceStr = bean.getNoncestr();
        request.timeStamp = bean.getTimestamp() + "";
        request.sign = bean.getSign();
        //调用api接口，发送数据到微信
        boolean b = api.sendReq(request);
        if (!b) {
            view.showPayResult(false);
        }
    }
}
