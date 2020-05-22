package com.tangmu.app.TengKuTV.presenter;

import android.os.Environment;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.base.RxPresenter;
import com.tangmu.app.TengKuTV.bean.DubbingMaterialBean;
import com.tangmu.app.TengKuTV.contact.DubbingRankContact;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.Util;

import java.io.File;

import javax.inject.Inject;

public class DubbingRankPresenter extends RxPresenter<DubbingRankContact.View> implements DubbingRankContact.Presenter {
    @Inject
    public DubbingRankPresenter() {
    }

    @Override
    public void getDetail(int id) {
        OkGo.<BaseResponse<DubbingMaterialBean>>post(Constant.IP + Constant.dubbingStar)
                .tag(this)
                .params("dv_id", id)
                .execute(new JsonCallback<BaseResponse<DubbingMaterialBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<DubbingMaterialBean>> response) {
                        if (response.body().getStatus() == 0) {
                            view.showDetail(response.body().getResult());
                        } else {
                            view.showError(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<DubbingMaterialBean>> response) {
                        super.onError(response);
                        view.showError(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void downloadFile(String url) {
     /*   OssManager downloadInstance = OssManager.getDownloadInstance(url);
        downloadInstance.setListener(new OssManager.OSSListener() {
            @Override
            public void onSuccess(String cosPath) {
                view.downloadSuccess(new File(cosPath));
            }

            @Override
            public void onFail(String msg) {
                view.showError(msg);
            }

            @Override
            public void onProgress(int progress) {
                view.showProgress(progress);
            }
        });*/
        OkGo.<File>get(Util.convertImgPath(url))
                .tag(this)
                .params("If-Modified-Since", System.currentTimeMillis())
                .execute(new FileCallback(CustomApp.getApp().getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath(), url.substring(url.lastIndexOf("/"))) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        view.downloadSuccess(response.body());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        view.showProgress((int) (progress.fraction * 100));
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        File body = response.body();
                        if (body != null && body.exists())
                            body.delete();
                        view.showError(Util.handleError(response.getException()));
                    }
                });
    }

}
