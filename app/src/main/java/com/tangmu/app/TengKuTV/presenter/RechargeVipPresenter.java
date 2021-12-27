package com.tangmu.app.TengKuTV.presenter;

import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.migu.sdk.api.CommonInfo;
import com.migu.sdk.api.CommonPayInfo;
import com.migu.sdk.api.MiguSdk;
import com.migu.sdk.api.PayCallBack;
import com.shcmcc.tools.GetSysInfo;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.AuthenticationBean;
import com.tangmu.app.TengKuTV.bean.MiguLoginBean;
import com.tangmu.app.TengKuTV.bean.MiguPayBean;
import com.tangmu.app.TengKuTV.bean.OrderBean;
import com.tangmu.app.TengKuTV.bean.PayInfoBean;
import com.tangmu.app.TengKuTV.bean.PayStatusBean;
import com.tangmu.app.TengKuTV.bean.SdkmesBean;
import com.tangmu.app.TengKuTV.bean.TVProductBean;
import com.tangmu.app.TengKuTV.contact.RechargeVipContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.MiGuJsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;

import javax.inject.Inject;

import androidx.annotation.Nullable;

public class RechargeVipPresenter extends RxPresenter<RechargeVipContact.View> implements RechargeVipContact.Presenter {

    @Inject
    public RechargeVipPresenter() {
    }

    @Override
    public void getUserInfo() {
        GetSysInfo getSysInfo = GetSysInfo.getInstance("10086", "", CustomApp.getApp());
        String firmwareVersion = getSysInfo.getFirmwareVersion();
        String snNum = getSysInfo.getSnNum();
        String terminalType = getSysInfo.getTerminalType();
        String hardwareVersion = getSysInfo.getHardwareVersion();
        String epgToken = getSysInfo.getEpgToken();
        String epgUserId = getSysInfo.getEpgUserId();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("firmwareVersion", firmwareVersion);
        jsonObject.addProperty("terminalType", terminalType);
        jsonObject.addProperty("hardwareVersion", hardwareVersion);
        PreferenceManager.getInstance().setToken(epgToken);
        OkGo.<BaseResponse<MiguLoginBean>>post(Constant.IP + Constant.tvRegister)
                .params("token", epgToken)
                .params("stbid", snNum)
                .params("user_name", epgUserId)
                .params("mes", jsonObject.toString())
                .execute(new JsonCallback<BaseResponse<MiguLoginBean>>() {
                    @Override
                    public void onError(Response<BaseResponse<MiguLoginBean>> response) {
                        super.onError(response);
                        if (view != null)
                            ToastUtil.showText(handleError(response.getException()));
                    }

                    @Override
                    public void onSuccess(Response<BaseResponse<MiguLoginBean>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            PreferenceManager.getInstance().setTuid(response.body().getResult().getU_id());
                            PreferenceManager.getInstance().updateLogin(response.body().getResult());
                            if (view != null)
                                view.showUserInfo(response.body().getResult());
                        } else {
                            if (view != null)
                                ToastUtil.showText(response.body().getMsg());
                        }
                    }

                });
    }

    @Override
    public void miguPay(SdkmesBean sdkmesBean, String price, String orderContentId) {
        CommonInfo commonInfo = new CommonInfo();
        commonInfo.setOrderId(sdkmesBean.getOrderId());
        commonInfo.setcType(sdkmesBean.getCtype());
        commonInfo.setOperCode(sdkmesBean.getOperCode());
        commonInfo.setPayNum(sdkmesBean.getPayNum());
        commonInfo.setStbId(sdkmesBean.getStbID());
        LogUtil.e("OrderId:" + commonInfo.getOrderId() + "\ncType:" + commonInfo.getcType()
                + "\nOperCode:" + commonInfo.getOperCode() + "\nOperCode:" + commonInfo.getPayNum()
                + "\nStbId:" + commonInfo.getStbId());

        CommonPayInfo commonPayInfo = new CommonPayInfo();
        commonPayInfo.setOrderId(sdkmesBean.getOrderId());
        // TODO: 2021/10/18
        PayInfoBean payInfo = sdkmesBean.getPayInfos().getPayInfo();
        commonPayInfo.setChannelId(payInfo.getChannelCode());
        commonPayInfo.setIsMonthly(payInfo.getIsMonthly());
        commonPayInfo.setCpId("699458");
//        commonPayInfo.setCpId("699213");
//        commonPayInfo.setContentId("1980113900");
        commonPayInfo.setContentId(orderContentId);
        commonPayInfo.setProductId(payInfo.getProductCode());
        commonPayInfo.setPrice(price);
        commonPayInfo.setSpCode(payInfo.getSpCode());
        commonPayInfo.setServCode(payInfo.getServCode());
        LogUtil.e("OrderId:" + commonPayInfo.getOrderId() + "\nChannelId:" + commonPayInfo.getChannelId()
                + "\nIsMonthly:" + commonPayInfo.getIsMonthly() + "\nCpId:" + commonPayInfo.getCpId() +
                "\nContentId:" + commonPayInfo.getContentId() + "\nPrice" + price
                + "\nSpCode:" + commonPayInfo.getSpCode() + "\nServCode:" + commonPayInfo.getServCode() + "\nProductId:"
                + commonPayInfo.getProductId()
                + "\n");

        CommonPayInfo[] commonPayInfos = new CommonPayInfo[1];
        commonPayInfos[0] = commonPayInfo;
        MiguSdk.pay(getContext(), commonInfo, commonPayInfos, "", "", new PayCallBack.IPayCallback() {
            @Override
            public void onResult(int i, String s, String s1) {
                LogUtil.d("miguPay onResult:" + i + ":" + s + ":" + s1);
            }
        });
        view.getPayResult();
    }


    @Override
    public void createOrder(String price, int vip_type, String productCode, String accountIdentify, String orderContentId) {
        OkGo.<BaseResponse<OrderBean>>post(Constant.IP + Constant.addOrder)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("tu_id", PreferenceManager.getInstance().getTuid())
//                .params("product_code",  productCode)
                .params("account_identify", accountIdentify)
                .params("price", price)
                .params("vip_type", vip_type)
                .params("type", 1).execute(new JsonCallback<BaseResponse<OrderBean>>() {
            @Override
            protected void onVerifySuccess(Response<BaseResponse<OrderBean>> response) {
                super.onVerifySuccess(response);
                if (response.body().getStatus() == 0) {
                    OrderBean result = response.body().getResult();
                    result.setPrice(price);
                    view.showOrder(result,orderContentId);
                } else view.showError(response.body().getMsg());
            }

            @Override
            public void onError(Response<BaseResponse<OrderBean>> response) {
                super.onError(response);
                view.showError(handleError(response.getException()));
            }
        });
    }

    @Override
    public void miguAuthentications(String userId, String terminalId) {
        OkGo.<String>post(Constant.IP + Constant.authentications)
                .cacheMode(CacheMode.NO_CACHE)
                .params("userId", userId)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("terminalId", terminalId).tag(this)
                .execute(new MiGuJsonCallback() {
                    @Override
                    protected void miguSuccess(@Nullable AuthenticationBean authenticationBean) {
                        if (authenticationBean != null && authenticationBean.getStatus() == 0
                                && "0".equals(authenticationBean.getResult().getBody().getAuthorize().getAttributes().getResult())) {//鉴权成功
//                            view.AuthenticationSuccess();
                            getProductList();
                            view.showPhone(authenticationBean.getResult().getBody().getAuthorize().getAttributes().getAccountIdentify());
                        } else if (authenticationBean == null) {//数据错误
                            view.showError("服务器错误");
                        } else if (authenticationBean.getResult().getBody().getAuthorize().getProductToOrderList() == null) {
                            view.showMiguError(authenticationBean.getResult().getBody().getAuthorize().getAttributes().getResultDesc());
//                            view.showError(authenticationBean.getResult().getBody().getAuthorize().getAttributes().getResultDesc());
                        } else {
                            getProductList();
                            view.showPhone(authenticationBean.getResult().getBody().getAuthorize().getAttributes().getAccountIdentify());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void pay(int payType, String order, String price, String orderContentId) {
        OkGo.<BaseResponse<MiguPayBean>>post(Constant.IP + Constant.payOrder)
                .cacheMode(CacheMode.NO_CACHE)
                .params("order_no", order)
                .params("price", price)
                .params("content_id", orderContentId)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("pay_type", payType).tag(this)
                .execute(new JsonCallback<BaseResponse<MiguPayBean>>() {
                    @Override
                    protected void onVerifySuccess(Response<BaseResponse<MiguPayBean>> response) {
                        super.onVerifySuccess(response);
                        if (response.body().getStatus() == 0 && "0".equals(response.body().getResult().getResult())) {
                            if (payType == 16) {
                                miguPay(response.body().getResult().getSdkmes(), price,orderContentId);
                            } else {
                                view.showPayCode(response.body().getResult().getQrCodeImg());
                            }
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<MiguPayBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }


    @Override
    public void payStatus(String orderNo) {
        OkGo.<BaseResponse<PayStatusBean>>post(Constant.IP + Constant.payStatus)
                .cacheMode(CacheMode.NO_CACHE)
                .params("order_no", orderNo)
                .params("token", PreferenceManager.getInstance().getToken())
                .tag(this)
                .execute(new JsonCallback<BaseResponse<PayStatusBean>>() {
                    @Override
                    protected void onVerifySuccess(Response<BaseResponse<PayStatusBean>> response) {
                        super.onVerifySuccess(response);
                        if (response.body().getStatus() == 0) {
                            PayStatusBean result = response.body().getResult();
                            if ("0".equals(result.getResult())) {
                                if ("0".equals(result.getPayResult())) {//0.支付成功 1.支付失败 2.支付受理中
                                    // 3.等待支付  (当result为0时显示该字段)4. 需要用户短信二次确认
                                    view.showPayResult(true, "");
                                } else if ("1".equals(result.getPayResult())) {
                                    view.showPayResult(false, result.getResultDesc());
                                } else {

                                }
                            } else {
                                view.showPayResult(false, result.getResultDesc());
                            }
                        } else {
                            view.showPayResult(false, response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<PayStatusBean>> response) {
                        super.onError(response);
                        view.showNetError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getProductList() {
        OkGo.<BaseListResponse<TVProductBean>>post(Constant.IP + Constant.productLists)
                .tag(this)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("type", 1)//type 1会员 2单片
                .execute(new JsonCallback<BaseListResponse<TVProductBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<TVProductBean>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            view.showRechargeBeans(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<TVProductBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }
}
