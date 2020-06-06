package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.CollectBean;
import com.tangmu.app.TengKuTV.contact.CollectContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;

import javax.inject.Inject;

public class CollectPresenter extends RxPresenter<CollectContact.View> implements CollectContact.Presenter {
    @Inject
    public CollectPresenter() {
    }

    @Override
    public void unCollect(String uc_id) {
        OkGo.<BaseResponse>post(Constant.IP + Constant.unCollect)
                .params("token", PreferenceManager.getInstance().getLogin().getToken())
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
    public void getCollects(int page, int type) {
        OkGo.<BaseListResponse<CollectBean>>post(Constant.IP + Constant.collectionList)
                .tag(this)
                .params("type", type)
                .params("page", page)
                .params("token", PreferenceManager.getInstance().getLogin().getToken())
                .params("size", 20)
                .execute(new JsonCallback<BaseListResponse<CollectBean>>() {
                    @Override
                    public void onVerifySuccess(Response<BaseListResponse<CollectBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showCollects(response.body().getResult());
                        } else {
                            view.showCollectsFail(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<CollectBean>> response) {
                        super.onError(response);
                        view.showCollectsFail(handleError(response.getException()));
                    }
                });
    }
}
