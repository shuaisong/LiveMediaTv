package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.contact.HomeContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.ToastUtil;

import javax.inject.Inject;

public class HomePresenter extends RxPresenter<HomeContact.View> implements HomeContact.Presenter {
    @Inject
    public HomePresenter() {
    }

    @Override
    public void getCategory() {
        OkGo.<BaseListResponse<CategoryBean>>post(Constant.IP + Constant.videoType)
                .tag(this)
                .execute(new JsonCallback<BaseListResponse<CategoryBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<CategoryBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showCategory(response.body().getResult());
                        } else {
                            ToastUtil.showText(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<CategoryBean>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }
}
