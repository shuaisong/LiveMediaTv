package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.BookDetailDataBean;
import com.tangmu.app.TengKuTV.contact.BookDetailContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;

import javax.inject.Inject;

public class BookDetailPresenter extends RxPresenter<BookDetailContact.View> implements BookDetailContact.Presenter {
    @Inject
    public BookDetailPresenter() {
    }

    @Override
    public void getDetail(int b_id) {
        PostRequest<BaseResponse<BookDetailDataBean>> postRequest = OkGo.<BaseResponse<BookDetailDataBean>>post(Constant.IP + Constant.bookDetail)
                .params("b_id", b_id);
        if (PreferenceManager.getInstance().getLogin() != null) {
            postRequest.params("u_id", PreferenceManager.getInstance().getLogin().getU_id());
        }
        postRequest.tag(this)
                .execute(new JsonCallback<BaseResponse<BookDetailDataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<BookDetailDataBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showDetail(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<BaseResponse<BookDetailDataBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void unCollect(int b_id) {
        OkGo.<BaseResponse>post(Constant.IP + Constant.unCollect)
                .params("token", PreferenceManager.getInstance().getLogin().getToken())
                .params("uc_id", b_id)
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
    public void collect(int b_id) {
        OkGo.<BaseResponse<Integer>>post(Constant.IP + Constant.collect)
                .params("token", PreferenceManager.getInstance().getLogin().getToken())
                .params("audio_id", b_id)
                .params("type", 3)
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
}
