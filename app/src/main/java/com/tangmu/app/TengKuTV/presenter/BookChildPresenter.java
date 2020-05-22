package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.AdBean;
import com.tangmu.app.TengKuTV.bean.BannerBean;
import com.tangmu.app.TengKuTV.bean.MoreBookBean;
import com.tangmu.app.TengKuTV.contact.BookChildContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;

import javax.inject.Inject;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class BookChildPresenter extends RxPresenter<BookChildContact.View> implements BookChildContact.Presenter {
    @Inject
    public BookChildPresenter() {
    }


    @Override
    public void getRecBook(int type) {
        OkGo.<BaseListResponse<MoreBookBean>>post(Constant.IP + Constant.youMaylikes)
                .tag(this)
                .params("p1_id", type)
                .execute(new JsonCallback<BaseListResponse<MoreBookBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<MoreBookBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showRecBooks(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<MoreBookBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getBooks(int type) {
        OkGo.<BaseListResponse<MoreBookBean>>post(Constant.IP + Constant.bookLists)
                .tag(this)
                .params("p1_id", type)
                .execute(new JsonCallback<BaseListResponse<MoreBookBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<MoreBookBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showBooks(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<MoreBookBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getBanner(int pid) {
        OkGo.<BaseListResponse<BannerBean>>post(Constant.IP + Constant.bannerImg)
                .tag(this)
                .params("type", 2)
                .params("p_id", pid)
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

}
