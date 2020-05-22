package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.AdBean;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean.VideoBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;
import com.tangmu.app.TengKuTV.bean.VideoDetailBean;
import com.tangmu.app.TengKuTV.contact.VideoDetailContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;

import javax.inject.Inject;

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
    public void getDetail(int vm_id) {
        PostRequest<BaseResponse<VideoDetailBean>> postRequest = OkGo.<BaseResponse<VideoDetailBean>>post(Constant.IP + Constant.videoDetail)
                .params("vm_id", vm_id);
        if (PreferenceManager.getInstance().getLogin() != null) {
            postRequest.params("u_id", PreferenceManager.getInstance().getLogin().getU_id());
        }
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
        OkGo.<BaseListResponse<HomeChildRecommendBean.VideoBean>>post(Constant.IP + Constant.recommendVideo)
                .params("p_id", p_id)
                .tag(this)
                .execute(new JsonCallback<BaseListResponse<HomeChildRecommendBean.VideoBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<VideoBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showRecommend(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<BaseListResponse<HomeChildRecommendBean.VideoBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void unCollect(int vm_id) {
        OkGo.<BaseResponse>post(Constant.IP + Constant.unCollect)
                .params("token", PreferenceManager.getInstance().getLogin().getToken())
                .params("uc_id", vm_id)
                .tag(this)
                .execute(new JsonCallback<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
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
                .params("token", PreferenceManager.getInstance().getLogin().getToken())
                .params("audio_id", vm_id)
                .params("type", 1)
                .tag(this)
                .execute(new JsonCallback<BaseResponse<Integer>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Integer>> response) {
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
}
