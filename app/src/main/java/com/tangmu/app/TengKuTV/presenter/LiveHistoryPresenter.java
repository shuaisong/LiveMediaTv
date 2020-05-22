package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.LiveHistoryBean;
import com.tangmu.app.TengKuTV.bean.LiveReplayBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;
import com.tangmu.app.TengKuTV.contact.LiveHistoryContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;

import java.util.Objects;

import javax.inject.Inject;

public class LiveHistoryPresenter extends RxPresenter<LiveHistoryContact.View> implements LiveHistoryContact.Presenter {
    @Inject
    public LiveHistoryPresenter() {
    }

    @Override
    public void getDetail(int id) {
        OkGo.<BaseResponse<LiveHistoryBean>>post(Constant.IP + Constant.getLiveHistory)
                .tag(this)
                .params("lr_id", id)
                .execute(new JsonCallback<BaseResponse<LiveHistoryBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<LiveHistoryBean>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            view.showDetail(response.body().getResult());
                        } else {
                            ToastUtil.showText(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<LiveHistoryBean>> response) {
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
