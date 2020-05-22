package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.BookRecmendSeachBean;
import com.tangmu.app.TengKuTV.bean.BookSearchBean;
import com.tangmu.app.TengKuTV.contact.BookSearchContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;

import javax.inject.Inject;

public class BookSearchPresenter extends RxPresenter<BookSearchContact.View> implements BookSearchContact.Presenter {
    @Inject
    public BookSearchPresenter() {
    }


    @Override
    public void getBookRecommend() {
        OkGo.<BaseListResponse<BookRecmendSeachBean>>post(Constant.IP + Constant.tvBookHotSearch)
                .tag(this)
                .execute(new JsonCallback<BaseListResponse<BookRecmendSeachBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<BookRecmendSeachBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showBookOrders(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<BookRecmendSeachBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getBooks(String content) {
        OkGo.<BaseListResponse<BookSearchBean>>post(Constant.IP + Constant.tvBookSearch)
                .tag(this)
                .params("content", content)
                .execute(new JsonCallback<BaseListResponse<BookSearchBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<BookSearchBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showBooks(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<BookSearchBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }


    @Override
    public void addSearchNum(int b_id) {
        OkGo.<BaseResponse>post(Constant.IP + Constant.addBookSearchNum)
                .tag(this)
                .params("b_id", b_id)
                .execute(new JsonCallback<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {

                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        super.onError(response);

                    }
                });
    }
}
