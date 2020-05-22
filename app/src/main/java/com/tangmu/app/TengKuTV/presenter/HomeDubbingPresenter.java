package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.BannerBean;
import com.tangmu.app.TengKuTV.bean.DubbingListBean;
import com.tangmu.app.TengKuTV.bean.HomeDubbingRecBean;
import com.tangmu.app.TengKuTV.contact.HomeDubbingContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.Util;

import javax.inject.Inject;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class HomeDubbingPresenter extends RxPresenter<HomeDubbingContact.View> implements HomeDubbingContact.Presenter {
    @Inject
    public HomeDubbingPresenter() {
    }

    @Override
    public void getDubbing(int page) {
        OkGo.<BaseListResponse<DubbingListBean>>post(Constant.IP + Constant.moreWorks)
                .tag(this)
                .params("page", page)
                .params("size", 20)
                .execute(new JsonCallback<BaseListResponse<DubbingListBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<DubbingListBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showDubbing(response.body().getResult());
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
    public void getBanner(int vt_id) {
        OkGo.<BaseListResponse<BannerBean>>post(Constant.IP + Constant.bannerImg)
                .tag(this)
                .params("type", 1)
                .params("p_id", vt_id)
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
