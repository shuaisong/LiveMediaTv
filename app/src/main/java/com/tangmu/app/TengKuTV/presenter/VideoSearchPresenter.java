package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;
import com.tangmu.app.TengKuTV.bean.VideoRecmendSeachBean;
import com.tangmu.app.TengKuTV.contact.VideoSearchContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;

import javax.inject.Inject;

public class VideoSearchPresenter extends RxPresenter<VideoSearchContact.View> implements VideoSearchContact.Presenter {
    @Inject
    public VideoSearchPresenter() {
    }


    @Override
    public void getVideoRecommend() {
        OkGo.<BaseListResponse<VideoRecmendSeachBean>>post(Constant.IP + Constant.tvHotSearch)
                .tag(this)
                .execute(new JsonCallback<BaseListResponse<VideoRecmendSeachBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<VideoRecmendSeachBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showVideoOrders(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<VideoRecmendSeachBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void getVideos(String content) {
        OkGo.<BaseListResponse<HomeChildRecommendBean.VideoBean>>post(Constant.IP + Constant.tvSearch)
                .tag(this)
                .params("content", content)
                .execute(new JsonCallback<BaseListResponse<HomeChildRecommendBean.VideoBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<HomeChildRecommendBean.VideoBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showVideos(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<HomeChildRecommendBean.VideoBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void addSearchNum(int vm_id) {
        OkGo.<BaseResponse>post(Constant.IP + Constant.addSearchNum)
                .tag(this)
                .params("vm_id", vm_id)
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
