package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.LiveEvaluateBean;
import com.tangmu.app.TengKuTV.contact.LiveHistoryEvaluteContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;

import java.util.Objects;

import javax.inject.Inject;

public class LiveHistoryEvalutePresenter extends RxPresenter<LiveHistoryEvaluteContact.View> implements LiveHistoryEvaluteContact.Presenter {
    @Inject
    public LiveHistoryEvalutePresenter() {
    }

    @Override
    public void praise(int lc_id, final int position) {
        OkGo.<BaseResponse>post(Constant.IP + Constant.liveLike)
                .tag(this)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("lc_id", lc_id)
                .execute(new JsonCallback<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            view.praiseSuccess(position);
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
    public void unPraise(int lc_id, final int position) {
        OkGo.<BaseResponse>post(Constant.IP + Constant.unliveLike)
                .tag(this)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("lc_id", lc_id)
                .execute(new JsonCallback<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            view.unPraiseSuccess(position);
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
    public void getEvaluates(int page, int id) {
        PostRequest<BaseListResponse<LiveEvaluateBean>> postRequest = OkGo.<BaseListResponse<LiveEvaluateBean>>post(Constant.IP + Constant.liveCommentLists)
                .tag(this)
                .params("room_id", id)
                .params("page", page)
                .params("size", 20);
        if (PreferenceManager.getInstance().getLogin() != null) {
            postRequest.params("u_id", Objects.requireNonNull(PreferenceManager.getInstance().getLogin()).getU_id());
        }
        postRequest.execute(new JsonCallback<BaseListResponse<LiveEvaluateBean>>() {
            @Override
            public void onSuccess(Response<BaseListResponse<LiveEvaluateBean>> response) {
                super.onSuccess(response);
                if (response.body().getStatus() == 0) {
                    view.showEvaluates(response.body().getResult());
                } else {
                    view.showEvaluatesFail(response.body().getMsg());
                }
            }

            @Override
            public void onError(Response<BaseListResponse<LiveEvaluateBean>> response) {
                super.onError(response);
                view.showEvaluatesFail(handleError(response.getException()));
            }
        });
    }
}
