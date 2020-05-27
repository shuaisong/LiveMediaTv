package com.tangmu.app.TengKuTV;

import androidx.multidex.MultiDexApplication;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.AppModule;
import com.tangmu.app.TengKuTV.component.DaggerAppComponent;
import com.tangmu.app.TengKuTV.utils.CrashHandler;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;

import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

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
    }

    private void initOKGO() {
        HttpHeaders mHttpHeaders = new HttpHeaders();
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        mHttpHeaders.headersMap = headers;
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        mBuilder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //超时时间设置，默认60秒
        mBuilder.readTimeout(10 * 1000, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        mBuilder.writeTimeout(10 * 1000, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        mBuilder.connectTimeout(10 * 1000, TimeUnit.MILLISECONDS);   //全局的连接超时时间
        OkGo.getInstance().init(CustomApp.getApp())
                .addCommonHeaders(mHttpHeaders)
                .setOkHttpClient(mBuilder.build())
                .setRetryCount(0)
                .setCacheMode(CacheMode.DEFAULT)
                .setCacheTime(1000 * 60 * 60 * 24);
    }


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
