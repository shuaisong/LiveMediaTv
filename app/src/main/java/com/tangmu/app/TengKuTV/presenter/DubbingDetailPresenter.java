package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.DubbingBean;
import com.tangmu.app.TengKuTV.bean.DubbingEvaluateBean;
import com.tangmu.app.TengKuTV.bean.DubbingListBean;
import com.tangmu.app.TengKuTV.bean.PauseADBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;
import com.tangmu.app.TengKuTV.contact.DubbingDetailContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;

import java.util.Objects;

import javax.inject.Inject;

public class DubbingDetailPresenter extends RxPresenter<DubbingDetailContact.View> implements DubbingDetailContact.Presenter {
    @Inject
    public DubbingDetailPresenter() {
    }

    @Override
    public void getDetail(int uw_id) {
        PostRequest<BaseResponse<DubbingBean>> postRequest = OkGo.<BaseResponse<DubbingBean>>post(Constant.IP + Constant.userWorksDetail)
                .tag(this)
                .params("uw_id", uw_id);
        if (PreferenceManager.getInstance().getLogin() != null) {
            postRequest.params("u_id", Objects.requireNonNull(PreferenceManager.getInstance().getLogin()).getU_id());
        }
        postRequest.execute(new JsonCallback<BaseResponse<DubbingBean>>() {
            @Override
            public void onSuccess(Response<BaseResponse<DubbingBean>> response) {
                super.onSuccess(response);
                if (response.body().getStatus() == 0) {
                    view.showDetail(response.body().getResult());
                } else view.showError(response.body().getMsg());
            }

            @Override
            public void onError(Response<BaseResponse<DubbingBean>> response) {
                super.onError(response);
                view.showError(handleError(response.getException()));
            }
        });
    }

    @Override
    public void collect(int id) {
        OkGo.<BaseResponse<Integer>>post(Constant.IP + Constant.collect)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("audio_id", id)
                .params("type", 4)
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

    @Override
    public void unCollect(int uc_id) {
        OkGo.<BaseResponse>post(Constant.IP + Constant.unCollect)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("uc_id", uc_id)
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
    public void getDubbing(int page) {
        OkGo.<BaseListResponse<DubbingListBean>>post(Constant.IP + Constant.moreWorks)
                .params("page", page)
                .params("size", 20).tag(this)
                .execute(new JsonCallback<BaseListResponse<DubbingListBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<DubbingListBean>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            view.showDubbings(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<DubbingListBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getRecomend() {
        OkGo.<BaseListResponse<DubbingListBean>>post(Constant.IP + Constant.worksRecommend)
                .tag(this)
                .execute(new JsonCallback<BaseListResponse<DubbingListBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<DubbingListBean>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            view.showRecmend(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<DubbingListBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
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
