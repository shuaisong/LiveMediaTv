package com.tangmu.app.TengKuTV.utils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.videoupload.TXUGCPublish;
import com.tangmu.app.TengKuTV.videoupload.TXUGCPublishTypeDef;

public class VodUploadManager {
    public VodUploadManager() {
    }

    public static VodUploadManager getVodUploadManager() {
        if (vodUploadManager == null) vodUploadManager = new VodUploadManager();
        return vodUploadManager;
    }

    private static VodUploadManager vodUploadManager;

    public void uploadFile(String filePath) {
        getToken(filePath);
    }

    private void getToken(final String filePath) {
        OkGo.<BaseResponse<String>>post(Constant.IP + Constant.vodSings)
                .execute(new JsonCallback<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<String>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            upload(filePath, response.body().getResult());
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<String>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }

    TXUGCPublishTypeDef.ITXVideoPublishListener listener;

    public void setListener(TXUGCPublishTypeDef.ITXVideoPublishListener listener) {
        this.listener = listener;
    }

    private void upload(String filePath, String key) {
        TXUGCPublish mVideoPublish = new TXUGCPublish(CustomApp.getApp(), filePath);
        mVideoPublish.setListener(listener);
        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        param.signature = key;
        param.videoPath = filePath;
        int publishCode = mVideoPublish.publishVideo(param);
    }
}
