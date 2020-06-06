package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.GiftBean;
import com.tangmu.app.TengKuTV.bean.LiveBannerBean;
import com.tangmu.app.TengKuTV.bean.LiveDetailBean;
import com.tangmu.app.TengKuTV.bean.LiveReplayBean;
import com.tangmu.app.TengKuTV.bean.LoginBean;
import com.tangmu.app.TengKuTV.bean.UserInfoBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;
import com.tangmu.app.TengKuTV.contact.LivingContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;

import javax.inject.Inject;

public class LivingPresenter extends RxPresenter<LivingContact.View> implements LivingContact.Presenter {
    @Inject
    public LivingPresenter() {
    }


    @Override
    public void unCollect(int roomId) {
        OkGo.<BaseResponse>post(Constant.IP + Constant.unCollect)
                .params("token", PreferenceManager.getInstance().getLogin().getToken())
                .params("uc_id", roomId)
                .tag(this)
                .execute(new JsonCallback<BaseResponse>() {
                    @Override
                    public void onVerifySuccess(Response<BaseResponse> response) {
                        if (response.body().getStatus() == 0) {
                            view.unCollectSuccess();
                        } else {
                            ToastUtil.showText(response.body().getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void collect(int roomid) {
        OkGo.<BaseResponse<Integer>>post(Constant.IP + Constant.collect)
                .params("token", PreferenceManager.getInstance().getLogin().getToken())
                .params("audio_id", roomid)
                .params("type", 2)
                .tag(this)
                .execute(new JsonCallback<BaseResponse<Integer>>() {
                    @Override
                    public void onVerifySuccess(Response<BaseResponse<Integer>> response) {
                        if (response.body().getStatus() == 0) {
                            view.collectSuccess(response.body().getResult());
                        } else {
                            ToastUtil.showText(response.body().getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<BaseResponse<Integer>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getLiveDetail(int id) {
        PostRequest<BaseResponse<LiveDetailBean>> postRequest = OkGo.<BaseResponse<LiveDetailBean>>post(Constant.IP + Constant.liveDetail)
                .params("room_id", id)
                .params("type", 1);
        LoginBean login = PreferenceManager.getInstance().getLogin();
        if (login != null) {
            postRequest.params("u_id", login.getU_id());
        }
        postRequest.execute(new JsonCallback<BaseResponse<LiveDetailBean>>() {
            @Override
            public void onSuccess(Response<BaseResponse<LiveDetailBean>> response) {
                super.onSuccess(response);
                if (response.body().getStatus() == 0) {
                    view.showLiveDetail(response.body().getResult());
                } else {
                    ToastUtil.showText(response.body().getMsg());
                }
            }

            @Override
            public void onError(Response<BaseResponse<LiveDetailBean>> response) {
                super.onError(response);
                ToastUtil.showText(handleError(response.getException()));
            }
        });
    }

    @Override
    public void getRecommend() {
        OkGo.<BaseListResponse<LiveReplayBean>>post(Constant.IP + Constant.liveReplyRecommend)
                .tag(this)
                .execute(new JsonCallback<BaseListResponse<LiveReplayBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<LiveReplayBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showLiveReply(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<LiveReplayBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getTVAd() {
        OkGo.<BaseListResponse<VideoAdBean>>post(Constant.IP + Constant.TvAd)
                .tag(this)
                .params("p_id", 99)
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

}
