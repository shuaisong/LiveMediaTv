package com.tangmu.app.TengKuTV;

import android.os.Build;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.AppModule;
import com.tangmu.app.TengKuTV.component.DaggerAppComponent;
import com.tangmu.app.TengKuTV.utils.CrashHandler;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tencent.bugly.crashreport.CrashReport;

import java.security.cert.CertificateException;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.X509TrustManager;

import androidx.multidex.MultiDexApplication;
import me.jessyan.autosize.AutoSizeConfig;
import okhttp3.OkHttpClient;

public class CustomApp extends MultiDexApplication {
    public static String COMPRESSORDIR;
    public static boolean canCache = true;
    private AppComponent appComponent;
    private static CustomApp application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initComponent();
        if (getExternalCacheDir() != null) {
            COMPRESSORDIR = getExternalCacheDir().getAbsolutePath();
        } else COMPRESSORDIR = getCacheDir().getAbsolutePath();
        PreferenceManager.init(this);
        AutoSizeConfig.getInstance().setCustomFragment(true);
        initOKGO();
        CrashHandler.getInstance().init(this);
        canCache = PreferenceManager.getInstance().getCanCache();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CrashReport.initCrashReport(getApplicationContext(), "a2a5e80a67", true);
                    System.loadLibrary("mg20pbase");
                }catch (Exception e){
                    LogUtil.e(e.getMessage());
                }
            }
        }).start();
    }

    private void initOKGO() {
        HttpHeaders mHttpHeaders = new HttpHeaders();
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        mHttpHeaders.headersMap = headers;
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        //log??????
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BASIC);        //log????????????????????????log?????????????????????
        loggingInterceptor.setColorLevel(Level.INFO);                               //log????????????????????????log???????????????????????????
        mBuilder.addInterceptor(loggingInterceptor);                                 //??????OkGo??????debug??????
        //???????????????????????????60???
        mBuilder.readTimeout(10 * 1000, TimeUnit.MILLISECONDS);      //???????????????????????????
        mBuilder.writeTimeout(10 * 1000, TimeUnit.MILLISECONDS);     //???????????????????????????
        mBuilder.connectTimeout(10 * 1000, TimeUnit.MILLISECONDS);   //???????????????????????????
        if (Build.VERSION.SDK_INT < 21) {
            mBuilder.sslSocketFactory(new Tls12SocketFactory(trustAllCert), trustAllCert);
        }
        OkGo.getInstance().init(CustomApp.getApp())
                .addCommonHeaders(mHttpHeaders)
                .setOkHttpClient((mBuilder).build())
                .setRetryCount(0)
                .setCacheMode(CacheMode.DEFAULT)
                .setCacheTime(1000 * 60 * 60 * 24);
    }

    //?????????????????????????????????TrustManager
    final X509TrustManager trustAllCert = new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    };

    private void initComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static CustomApp getApp() {
        return application;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
