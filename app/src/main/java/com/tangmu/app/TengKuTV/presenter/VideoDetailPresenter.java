package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.migu.sdk.api.CommonInfo;
import com.migu.sdk.api.CommonPayInfo;
import com.migu.sdk.api.MiguSdk;
import com.migu.sdk.api.PayCallBack;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.AdBean;
import com.tangmu.app.TengKuTV.bean.AuthenticationBean;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean.VideoBean;
import com.tangmu.app.TengKuTV.bean.MiguPayBean;
import com.tangmu.app.TengKuTV.bean.OrderBean;
import com.tangmu.app.TengKuTV.bean.PauseADBean;
import com.tangmu.app.TengKuTV.bean.PayInfoBean;
import com.tangmu.app.TengKuTV.bean.PayStatusBean;
import com.tangmu.app.TengKuTV.bean.SdkmesBean;
import com.tangmu.app.TengKuTV.bean.TVProductBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;
import com.tangmu.app.TengKuTV.bean.VideoDetailBean;
import com.tangmu.app.TengKuTV.contact.VideoDetailContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.MiGuJsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;

import javax.inject.Inject;

import androidx.annotation.Nullable;

public class VideoDetailPresenter extends RxPresenter<VideoDetailContact.View> implements VideoDetailContact.Presenter {
    @Inject
    public VideoDetailPresenter() {
    }

    @Override
    public void getAd(int id) {
        OkGo.<BaseResponse<AdBean>>post(Constant.IP + Constant.videoAd)
                .tag(this)
                .params("type", 1)
                .params("p1_id", id)
                .execute(new JsonCallback<BaseResponse<AdBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<AdBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showAd(response.body().getResult());
                        } else {
                            view.showAdError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<AdBean>> response) {
                        super.onError(response);
                        view.showAdError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getTvAd(int p_id) {
        OkGo.<BaseListResponse<VideoAdBean>>post(Constant.IP + Constant.TvAd)
                .tag(this)
                .params("p_id", p_id)
                .execute(new JsonCallback<BaseListResponse<VideoAdBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<VideoAdBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showTVAd(response.body().getResult());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<VideoAdBean>> response) {
                        super.onError(response);
                    }
                });
    }

    @Override
    public void createOrder(String price, int vip_type, String productCode, String accountIdentify, int id, String orderContentId) {
        OkGo.<BaseResponse<OrderBean>>post(Constant.IP + Constant.addOrder)
                .params("token", PreferenceManager.getInstance().getToken())
               .params("tu_id", PreferenceManager.getInstance().getTuid())
//                .params("product_code",  productCode)
                .params("account_identify",  accountIdentify)
                .params("price", price)
                .params("vip_type", vip_type)
                .params("type", 3).params("vm_id", id).execute(new JsonCallback<BaseResponse<OrderBean>>() {
            @Override
            protected void onVerifySuccess(Response<BaseResponse<OrderBean>> response) {
                super.onVerifySuccess(response);
                if (response.body().getStatus() == 0){
                    OrderBean result = response.body().getResult();
                    result.setPrice(price);
                    view.showOrder(result,orderContentId);
                }
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
    public void getDetail(int vm_id) {
        PostRequest<BaseResponse<VideoDetailBean>> postRequest = OkGo.<BaseResponse<VideoDetailBean>>post(Constant.IP + Constant.videoDetail)
                .params("vm_id", vm_id)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("u_id", PreferenceManager.getInstance().getTuid());
        postRequest.tag(this)
                .execute(new JsonCallback<BaseResponse<VideoDetailBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<VideoDetailBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showDetail(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<BaseResponse<VideoDetailBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getRecommend(int p_id) {
        OkGo.<BaseListResponse<VideoBean>>post(Constant.IP + Constant.recommendVideo)
                .params("p_id", p_id)
                .tag(this)
                .execute(new JsonCallback<BaseListResponse<VideoBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<VideoBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showRecommend(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<BaseListResponse<VideoBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void unCollect(int vm_id) {
        OkGo.<BaseResponse>post(Constant.IP + Constant.unCollect)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("uc_id", vm_id)
                .tag(this)
                .execute(new JsonCallback<BaseResponse>() {
                    @Override
                    public void onVerifySuccess(Response<BaseResponse> response) {
                        if (response.body().getStatus() == 0) {
                            view.unCollectSuccess();
                        } else {
                            view.showError(response.body().getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void collect(int vm_id) {
        OkGo.<BaseResponse<Integer>>post(Constant.IP + Constant.collect)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("audio_id", vm_id)
                .params("type", 1)
                .tag(this)
                .execute(new JsonCallback<BaseResponse<Integer>>() {
                    @Override
                    public void onVerifySuccess(Response<BaseResponse<Integer>> response) {
                        if (response.body().getStatus() == 0) {
                            view.collectSuccess(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<BaseResponse<Integer>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    public void weChatPayInfo(String order_num, String price) {
        OkGo.<BaseResponse<String>>post(Constant.IP + Constant.pay)
                .cacheMode(CacheMode.NO_CACHE)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("order_no", order_num)
                .params("pay_method", 5)
                .params("price", price).tag(this)
                .execute(new JsonCallback<BaseResponse<String>>() {
                    @Override
                    public void onVerifySuccess(Response<BaseResponse<String>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showPayCode(response.body().getResult());
                        } else view.showError(response.body().getMsg());
                    }

                    @Override
                    public void onError(Response<BaseResponse<String>> response) {
                        super.onError(response);
                        view.showError(response.body().getMsg());
                    }
                });
    }

    public void getWxPayResult(String order_no) {
        OkGo.<BaseResponse<Integer>>post(Constant.IP + Constant.payCallbacks)
                .params("order_no", order_no)
                .execute(new JsonCallback<BaseResponse<Integer>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Integer>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            if (response.body().getResult() == 2) {
                                view.showPayResult(true, handleError(response.getException()));
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<Integer>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                        view.showPayResult(false, handleError(response.getException()));
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
        LogUtil.e("OrderId:"+commonInfo.getOrderId()+"\ncType:"+commonInfo.getcType()
                +"\nOperCode:"+commonInfo.getOperCode()+"\nOperCode:"+commonInfo.getPayNum()
                +"\nStbId:"+commonInfo.getStbId());

        CommonPayInfo commonPayInfo = new CommonPayInfo();
        commonPayInfo.setOrderId(sdkmesBean.getOrderId());
        // TODO: 2021/10/18
        PayInfoBean payInfo = sdkmesBean.getPayInfos().getPayInfo();
        commonPayInfo.setChannelId(payInfo.getChannelCode());
        commonPayInfo.setIsMonthly(payInfo.getIsMonthly());
        commonPayInfo.setCpId("699458");
//        commonPayInfo.setCpId("699213");
        commonPayInfo.setContentId(orderContentId);
        commonPayInfo.setPrice(price);
        commonPayInfo.setSpCode(payInfo.getSpCode());
        commonPayInfo.setServCode(payInfo.getServCode());
        commonPayInfo.setProductId(payInfo.getProductCode());

        LogUtil.e("OrderId:"+commonPayInfo.getOrderId()+"\nChannelId:"+commonPayInfo.getChannelId()
                +"\nIsMonthly:"+commonPayInfo.getIsMonthly()+"\nCpId:"+commonPayInfo.getCpId()+
                "\nContentId:"+commonPayInfo.getContentId()+"\nPrice"+price
                +"\nSpCode:"+commonPayInfo.getSpCode()+"\nServCode:"+commonPayInfo.getServCode()+"\nProductId:"
                +commonPayInfo.getProductId()
                +"\n");

        CommonPayInfo[] commonPayInfos = new CommonPayInfo[1];
        commonPayInfos[0] = commonPayInfo;
        MiguSdk.pay(getContext(), commonInfo, commonPayInfos, "", "", new PayCallBack.IPayCallback() {
            @Override
            public void onResult(int i, String s, String s1) {
                LogUtil.d("miguPay onResult:"+i+":"+s+":"+s1);
            }
        });
        view.getPayResult();
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
                        if (authenticationBean != null && authenticationBean.getStatus()==0
                                && "0".equals(authenticationBean.getResult().getBody().getAuthorize().getAttributes().getResult())){//鉴权成功
                            getProductList(authenticationBean.getResult().getBody().getAuthorize().getAttributes().getAccountIdentify());
                        }else if (authenticationBean == null){//数据错误
                            view.showError("服务器错误");
                        }else if (authenticationBean.getResult().getBody().getAuthorize().getProductToOrderList()==null){
                            view.showMiguError(authenticationBean.getResult().getBody().getAuthorize().getAttributes().getResultDesc());
                            view.showError(authenticationBean.getResult().getBody().getAuthorize().getAttributes().getResultDesc());
                        }else {
                            getProductList(authenticationBean.getResult().getBody().getAuthorize().getAttributes().getAccountIdentify());
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
    public void getProductList(String accountIdentify) {
        OkGo.<BaseListResponse<TVProductBean>>post(Constant.IP + Constant.productLists)
                .tag(this)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("type", 2)//type 1会员 2单片
                .execute(new JsonCallback<BaseListResponse<TVProductBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<TVProductBean>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            view.showRechargeBeans(response.body().getResult(),accountIdentify);
                        } else {
                            ToastUtil.showText(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<TVProductBean>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void pay(int payType, String order, String price, String orderContentId) {
        OkGo.<BaseResponse<MiguPayBean>>post(Constant.IP + Constant.payOrder)
                .cacheMode(CacheMode.NO_CACHE)
                .params("order_no", order)
                .params("price", price)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("pay_type", payType).tag(this)
                .execute(new JsonCallback<BaseResponse<MiguPayBean>>() {
                    @Override
                    protected void onVerifySuccess(Response<BaseResponse<MiguPayBean>> response) {
                        super.onVerifySuccess(response);
                        if (response.body().getStatus()==0&&"0".equals(response.body().getResult().getResult())){
                            if (payType==16){
                                miguPay(response.body().getResult().getSdkmes(),price,orderContentId);
                            }else  {
                                view.showPayCode(response.body().getResult().getQrCodeImg());
                            }
                        }else {
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
                        if (response.body().getStatus()==0){
                            PayStatusBean result = response.body().getResult();
                            if ("0".equals(result.getResult())){
                                if ("0".equals(result.getPayResult())){//0.支付成功 1.支付失败 2.支付受理中
                                    // 3.等待支付  (当result为0时显示该字段)4. 需要用户短信二次确认
                                    view.showPayResult(true,"");
                                }else if ("1".equals(result.getPayResult())){
                                    view.showPayResult(false,result.getResultDesc());
                                }else {

                                }
                            }else  {
                                view.showPayResult(false,result.getResultDesc());
                            }
                        }else {
                            view.showPayResult(false,response.body().getMsg());
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
    public void getTVPauseAD(int id) {
        OkGo.<BaseResponse<PauseADBean>>post(Constant.IP + Constant.getVideoStopAd)
                .params("p_id", id)
                .execute(new JsonCallback<BaseResponse<PauseADBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<PauseADBean>> response) {
                        super.onSuccess(response);
                        if (0 == response.body().getStatus()) {
                            view.showPauseAd(response.body().getResult().getImages());
                        }
                    }
                });
    }
}
