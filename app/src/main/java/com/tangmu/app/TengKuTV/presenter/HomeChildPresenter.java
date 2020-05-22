package com.tangmu.app.TengKuTV.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.BannerBean;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;
import com.tangmu.app.TengKuTV.contact.HomeChildContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.Util;

import javax.inject.Inject;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class HomeChildPresenter extends RxPresenter<HomeChildContact.View> implements HomeChildContact.Presenter {
    @Inject
    public HomeChildPresenter() {
    }


    @Override
    public void getRecMovie(int type) {
        OkGo.<BaseListResponse<HomeChildRecommendBean>>post(Constant.IP + Constant.homevideoLists)
                .tag(this)
                .params("p1_id", type)
                .execute(new JsonCallback<BaseListResponse<HomeChildRecommendBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<HomeChildRecommendBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showMovie(Util.convertHomeRecommendVideo(response.body().getResult()));
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<HomeChildRecommendBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void updateRecommend(int s_sorts) {
        OkGo.<BaseListResponse<HomeChildRecommendBean.VideoBean>>post(Constant.IP + Constant.change)
                .tag(this)
                .params("s_id", s_sorts)
                .execute(new JsonCallback<BaseListResponse<HomeChildRecommendBean.VideoBean>>() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<HomeChildRecommendBean.VideoBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showChildMovie(response.body().getResult());
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
    public void getBanner(int pid) {
        OkGo.<BaseListResponse<BannerBean>>post(Constant.IP + Constant.bannerImg)
                .tag(this)
                .params("type", 1)
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
