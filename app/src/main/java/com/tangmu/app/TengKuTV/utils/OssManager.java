package com.tangmu.app.TengKuTV.utils;

import android.os.Environment;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.bean.OssTokenBean;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLDownloadTask;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials;
import com.tencent.qcloud.core.auth.SessionQCloudCredentials;
import com.tencent.qcloud.core.common.QCloudClientException;

import org.json.JSONArray;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class OssManager {


    private CosXmlService cosXmlService;
    private OSSListener OSSListener;
    private COSXMLUploadTask cosxmlUploadTask;
    private COSXMLDownloadTask cosxmlDownloadTask;
    private Observer<Response<?>> observer;

    private OssManager() {
    }

    private static OssManager uploadManager;
    private static OssManager downloadManager;

    public static OssManager getDownloadInstance(String ossPath) {
        if (downloadManager == null) downloadManager = new OssManager();
        downloadManager.getToken(ossPath);
        return downloadManager;
    }

    private void getToken(final String ossPath) {
        OkGo.<BaseResponse<OssTokenBean>>post(Constant.IP + Constant.ossToken)
                .tag(this)
                .execute(new JsonCallback<BaseResponse<OssTokenBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<OssTokenBean>> response) {
                        init(response.body().getResult());
                        String downloadDir;
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            downloadDir = CustomApp.getApp().getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath();
                        } else
                            downloadDir = CustomApp.getApp().getExternalCacheDir().getAbsolutePath();
                        downLoadFile(ossPath, downloadDir, ossPath.substring(ossPath.lastIndexOf("/")));
                    }

                    @Override
                    public void onError(Response<BaseResponse<OssTokenBean>> response) {
                        super.onError(response);
                        if (OSSListener != null)
                            OSSListener.onFail(handleError(response.getException()));
                    }
                });
    }

    public static OssManager getUploadInstance(String filepath) {
        if (uploadManager == null) uploadManager = new OssManager();
        uploadManager.getTokenOssPath(filepath);
        return uploadManager;
    }

    public static OssManager updateUploadInstance(String filepath) {
        if (uploadManager == null) uploadManager = new OssManager();
        uploadManager.getTokenOssPath(filepath);
        return uploadManager;
    }

    private void getTokenOssPath(final String filePath) {
        Observable<Response<BaseResponse<OssTokenBean>>> responseObservable =
                OkGo.<BaseResponse<OssTokenBean>>post(Constant.IP + Constant.ossToken)
                        .tag(this)
                        .converter(new JsonCallback<BaseResponse<OssTokenBean>>() {
                            @Override
                            public void onSuccess(Response<BaseResponse<OssTokenBean>> response) {

                            }
                        }).adapt(new ObservableResponse<BaseResponse<OssTokenBean>>()).subscribeOn(Schedulers.io());
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(filePath);
        Observable<Response<BaseListResponse<String>>> responseObservable1 =
                OkGo.<BaseListResponse<String>>post(Constant.IP + Constant.getCloudfileSavePath)
                        .params("filename", jsonArray.toString())
                        .tag(this)
                        .converter(new JsonCallback<BaseListResponse<String>>() {
                            @Override
                            public void onSuccess(Response<BaseListResponse<String>> response) {

                            }
                        }).adapt(new ObservableResponse<BaseListResponse<String>>()).subscribeOn(Schedulers.io());
        observer = Observable.merge(responseObservable1, responseObservable).subscribeWith(new Observer<Response<?>>() {

            private List<String> remoteList;

            @Override
            public void onSubscribe(Disposable d) {
                LogUtil.d("");
            }

            @Override
            public void onNext(Response<?> response) {
                if (response.body() instanceof BaseResponse) {
                    OssTokenBean ossTokenBean = (OssTokenBean) ((BaseResponse) response.body()).getResult();
                    init(ossTokenBean);
                }
                if (response.body() instanceof BaseListResponse) {
                    remoteList = ((BaseListResponse<String>) response.body()).getResult();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.d(e.getMessage());
                if (OSSListener != null) OSSListener.onFail(e.getMessage());
            }

            @Override
            public void onComplete() {
                upload(remoteList.get(0), filePath);
            }
        });
    }

    private void init(OssTokenBean ossTokenBean) {
        String region = "ap-beijing";
// 创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setRegion(region)
                .isHttps(true) // 使用 HTTPS 请求, 默认为 HTTP 请求
                .builder();
        /*
         * 初始化 {@link QCloudCredentialProvider} 对象，来给 SDK 提供临时密钥
         */
        MyCredentialProvider credentialProvider = new MyCredentialProvider();
        credentialProvider.setToken(ossTokenBean);
        cosXmlService = new CosXmlService(CustomApp.getApp(), serviceConfig, credentialProvider);
    }


    public void upload(final String cosPath, final String srcPath) {
// 初始化 TransferConfig
        TransferConfig transferConfig = new TransferConfig.Builder().build();

        /*若有特殊要求，则可以如下进行初始化定制。例如限定当对象 >= 2M 时，启用分块上传，且分块上传的分块大小为1M，当源对象大于5M时启用分块复制，且分块复制的大小为5M。*/
        transferConfig = new TransferConfig.Builder()
                .setDividsionForCopy(5 * 1024 * 1024) // 是否启用分块复制的最小对象大小
                .setSliceSizeForCopy(5 * 1024 * 1024) // 分块复制时的分块大小
                .setDivisionForUpload(2 * 1024 * 1024) // 是否启用分块上传的最小对象大小
                .setSliceSizeForUpload(2 * 1024 * 1024) // 分块上传时的分块大小
                .build();

// 初始化 TransferManager
        TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);

        String bucket = "tk-1253334841-1258540389"; //存储桶，格式：BucketName-APPID
        String uploadId = null; //若存在初始化分块上传的 UploadId，则赋值对应的 uploadId 值用于续传；否则，赋值 null
// 上传对象
        cosxmlUploadTask = transferManager.upload(bucket, cosPath, srcPath, uploadId);


//设置上传进度回调
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                OSSListener.onProgress((int) (complete * 100 / target));
            }
        });
//设置返回结果回调
        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                OSSListener.onSuccess(cosPath);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                OSSListener.onFail(serviceException.getMessage());
            }
        });
//设置任务状态回调, 可以查看任务过程
//        cosxmlUploadTask.setTransferStateListener(new TransferStateListener() {
//            @Override
//            public void onStateChanged(TransferState state) {
//            }
//        });

/**
 若有特殊要求，则可以如下操作：
 PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, cosPath, srcPath);
 putObjectRequest.setRegion(region); //设置存储桶所在的地域
 putObjectRequest.setNeedMD5(true); //是否启用 Md5 校验
 COSXMLUploadTask cosxmlUploadTask = transferManager.upload(putObjectRequest, uploadId);
 */

    }

    public void cancelUpload() {
        //取消上传
        OkGo.getInstance().cancelTag(this);
        if (cosxmlUploadTask != null)
            cosxmlUploadTask.cancel();
    }

    public void downLoadFile(String cosPath, final String savePathDir, final String savedFileName) {
        String bucket = "tk-1253334841-1258540389"; //存储桶，格式：BucketName-APPID
//下载对象
        TransferConfig transferConfig = new TransferConfig.Builder().build();
//初始化 TransferManager
        TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);
        cosxmlDownloadTask = transferManager.download(CustomApp.getApp(), bucket, cosPath, savePathDir, savedFileName);
//设置下载进度回调
        cosxmlDownloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                OSSListener.onProgress((int) (complete * 100 / target));
            }
        });
//设置返回结果回调
        cosxmlDownloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                if (OSSListener != null) {
                    OSSListener.onSuccess(savePathDir + File.separator + savedFileName);
                }
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                if (OSSListener != null) {
                    OSSListener.onFail(serviceException.getMessage());
                }
            }
        });
//设置任务状态回调，可以查看任务过程
//        cosxmlDownloadTask.setTransferStateListener(new TransferStateListener() {
//            @Override
//            public void onStateChanged(TransferState state) {
//            }
//        });

/**
 若有特殊要求，则可以如下操作：
 GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, cosPath, localDir, localFileName);
 getObjectRequest.setRegion(region); //设置存储桶所在的地域
 COSXMLDownloadTask cosxmlDownloadTask = transferManager.download(context, getObjectRequest);
 */

    }

    //取消下载
    public void cancelDownload() {
        if (cosxmlDownloadTask != null) {
            cosxmlDownloadTask.cancel();
        }
    }

    public void setListener(OSSListener listener) {
        this.OSSListener = listener;
    }


    public interface OSSListener {
        void onSuccess(String cosPath);

        void onFail(String msg);

        void onProgress(int progress);
    }

    class MyCredentialProvider extends BasicLifecycleCredentialProvider {
        // 然后解析响应，获取密钥信息
        String tmpSecretId = "COS_SECRETID"; //临时密钥 secretId
        String tmpSecretKey = "COS_SECRETKEY"; //临时密钥 secretKey
        String sessionToken = "TOKEN"; //临时密钥 Token
        long expiredTime = 1556183496L;//临时密钥有效截止时间戳，单位是秒

        /*强烈建议返回服务器时间作为签名的开始时间，用来避免由于用户手机本地时间偏差过大导致的签名不正确 */
        // 返回服务器时间作为签名的起始时间
        long startTime = 1556182000L; //临时密钥有效起始时间，单位是秒

        @Override
        protected QCloudLifecycleCredentials fetchNewCredentials() throws QCloudClientException {
            // 最后返回临时密钥信息对象
            return new SessionQCloudCredentials(tmpSecretId, tmpSecretKey, sessionToken, startTime, expiredTime);
        }

        public void setToken(OssTokenBean ossTokenBean) {
            tmpSecretId = ossTokenBean.getCredentials().getTmpSecretId();
            tmpSecretKey = ossTokenBean.getCredentials().getTmpSecretKey();
            sessionToken = ossTokenBean.getCredentials().getSessionToken();
            expiredTime = ossTokenBean.getExpiredTime();
            startTime = ossTokenBean.getStartTime();
        }
    }
}
