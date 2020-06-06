package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.BannerBean;
import com.tangmu.app.TengKuTV.bean.LiveBean;
import com.tangmu.app.TengKuTV.bean.LiveReplayBean;
import com.tangmu.app.TengKuTV.contact.LiveContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;

import javax.inject.Inject;

public class LivePresenter extends RxPresenter<LiveContact.View> implements LiveContact.Presenter {
    @Inject
    public LivePresenter() {
    }


    @Override
    public void getLiveReply(int page) {
        OkGo.<BaseListResponse<LiveReplayBean>>post(Constant.IP + Constant.moreReplyLive)
                .tag(this)
                .params("page", page)
                .params("size", 20)
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
    public void getBanner() {
        OkGo.<BaseListResponse<BannerBean>>post(Constant.IP + Constant.bannerImg)
                .tag(this)
                .params("type", 3)
                .execute(new JsonCallback<BaseListResponse<BannerBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<BannerBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.ShowBanner(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<BannerBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getTopLive() {
        OkGo.<BaseListResponse<LiveBean>>post(Constant.IP + Constant.tvLivePage)
                .tag(this)
                .execute(new JsonCallback<BaseListResponse<LiveBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<LiveBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showTopLive(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<LiveBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }
}
