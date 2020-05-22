package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.DubbingListBean;
import com.tangmu.app.TengKuTV.contact.DubbingListContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;

import java.util.Objects;

import javax.inject.Inject;

public class DubbingListPresenter extends RxPresenter<DubbingListContact.View> implements DubbingListContact.Presenter {
    @Inject
    public DubbingListPresenter() {
    }


    @Override
    public void getDubbing(int page) {
        PostRequest<BaseListResponse<DubbingListBean>> params = OkGo.<BaseListResponse<DubbingListBean>>post(Constant.IP + Constant.moreWorks)
                .params("page", page)
                .params("size", 20);
        if (PreferenceManager.getInstance().getLogin() != null) {
            params.params("u_id", PreferenceManager.getInstance().getLogin().getU_id());
        }
        params.tag(this)
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
    public void praise(int id, final int position) {
        OkGo.<BaseResponse>post(Constant.IP + Constant.worksLikeAdd)
                .tag(this)
                .params("token", Objects.requireNonNull(PreferenceManager.getInstance().getLogin()).getToken())
                .params("type", 1)
                .params("uw_id", id).execute(new JsonCallback<BaseResponse>() {
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
    public void unPraise(int id, final int position) {
        OkGo.<BaseResponse>post(Constant.IP + Constant.unworksLike)
                .tag(this)
                .params("token", Objects.requireNonNull(PreferenceManager.getInstance().getLogin()).getToken())
                .params("type", 1)
                .params("uw_id", id).execute(new JsonCallback<BaseResponse>() {
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
    public void share(int id) {
        OkGo.<BaseResponse>post(Constant.IP + Constant.shareNum)
                .params("uw_id", id).execute(new JsonCallback<BaseResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse> response) {
                super.onSuccess(response);
            }
        });
    }
}
