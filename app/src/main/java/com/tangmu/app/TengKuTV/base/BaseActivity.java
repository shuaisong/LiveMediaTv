package com.tangmu.app.TengKuTV.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.NetReceiver;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.utils.CleanInputLeakUtils;
import com.tangmu.app.TengKuTV.utils.GlideApp;
import com.tangmu.app.TengKuTV.utils.GlideRequest;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.GlideCircleWithBorder;
import com.tangmu.app.TengKuTV.view.TitleView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import me.jessyan.autosize.internal.CustomAdapt;


/**
 * Created by ysh on 2019/5/25 0025.
 */

public abstract class BaseActivity extends AppCompatActivity implements CustomAdapt, NetReceiver.NetChangeListener {

    private long lastClickTime;
    private boolean isDefaultLanguage;
    private NetReceiver netReceiver;
    private ImageView wifiImage;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);            //横屏
        setContentView(setLayoutId());
//        updateBG();
        ButterKnife.bind(this);
        setupActivityComponent(CustomApp.getApp().getAppComponent());
        initView();
        initData();
        netReceiver = new NetReceiver();
        netReceiver.setNetChangeListener(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netReceiver, intentFilter);
        wifiImage = findViewById(R.id.wifi);
        if (wifiImage != null)
            if (checkNetAvailable()) {
                LogUtil.e("网络正常");
                wifiImage.setImageResource(R.mipmap.ic_wifi);
            } else {
                LogUtil.e("网络失败");
                wifiImage.setImageResource(R.mipmap.no_net);
            }
    }

    protected  void checkIsHijack(){
        ActivityManager activityManager = (ActivityManager) (CustomApp.getApp().getSystemService(android.content.Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses != null) {
            String processName = appProcesses.get(0).processName;
            if(!processName.equalsIgnoreCase(getPackageName())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("警告");
                builder.setPositiveButton("检测到窗口被劫持" , null );
                builder.show();
            }
        }
    }

    private String getAppLanguage() {
        isDefaultLanguage = PreferenceManager.getInstance().isDefaultLanguage();
        return isDefaultLanguage ? "zh" : "bo";
    }

//    protected void attachBaseContext(Context paramContext) {
//        super.attachBaseContext(AppLanguageUtils.attachBaseContext(paramContext, getAppLanguage()));
//    }

    protected void updateBG() {
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.black_bg));
    }

    @Override
    protected void onPause() {
        super.onPause();
//        checkIsHijack();
    }

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        return timeD <= 300;
    }

    public boolean checkNetAvailable() {
        boolean available;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) available = false;
        else
            available = activeNetworkInfo.isAvailable();
       /* if (!available) {
            ToastUtil.showText("网络不可用,请检查网络!");
        }*/
        LogUtil.d("NetAvailable:" + available);
        return available;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick()) {
                return true;
            }
            View currentFocus = getCurrentFocus();
            if (!(currentFocus instanceof EditText)) {
                hindSoft();
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    protected void setHead(String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions().error(R.mipmap.default_head).
                placeholder(R.mipmap.default_head)
                .circleCrop();//错误图、占位图
        GlideRequest<Drawable> apply = GlideApp.with(this)
                .load(R.mipmap.default_head).apply(requestOptions);//解决占位图非圆形问题
        GlideUtils.getRequest(this, url).apply(requestOptions).thumbnail(apply)
                .into(imageView);
    }

    protected void setHeadWithBorder(String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions().error(R.mipmap.default_head)
                .placeholder(R.mipmap.default_head)
                .override(100, 100)
                .transform(new GlideCircleWithBorder(this, 1, Color.WHITE));//错误图、占位图
        GlideRequest<Drawable> apply = GlideApp.with(this)
                .load(R.mipmap.default_head).apply(requestOptions);//解决占位图非圆形问题
        GlideUtils.getRequest(this, url).apply(requestOptions).thumbnail(apply)
                .into(imageView);
    }

    protected abstract void setupActivityComponent(AppComponent appComponent);

    protected abstract void initData();

    protected abstract void initView();

    public abstract int setLayoutId();



    @Override
    protected void onResume() {
        super.onResume();
//        if (isDefaultLanguage != PreferenceManager.getInstance().isDefaultLanguage())
//            recreate();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(netReceiver);
        CleanInputLeakUtils.getInstance().fixInputMethodManagerLeak(this);
        Glide.get(this).clearMemory();
        super.onDestroy();
    }

    protected void hindSoft() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            manager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    protected boolean isLogin() {
        if (PreferenceManager.getInstance().getLogin() != null) return true;
        else {
            return false;
        }
    }

    protected boolean isClickLogin() {
        if (PreferenceManager.getInstance().getLogin() != null) return true;
        else {
            ToastUtil.showText(getString(R.string.login_tip1));
            return false;
        }
    }

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 648;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            event = new KeyEvent(event.getDownTime(), event.getEventTime(), event.getAction(),
                    KeyEvent.KEYCODE_DPAD_CENTER, event.getMetaState(),
                    event.getDeviceId(), event.getScanCode(), event.getFlags(), event.getSource());
        }
        return super.dispatchKeyEvent(event);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        View currentFocus = getCurrentFocus();
//        if (currentFocus != null) {
//            LogUtil.e(event.getKeyCode() + currentFocus.toString());
//            ToastUtil.showText(event.getKeyCode() + currentFocus.toString());
//        } else {
//            ToastUtil.showText(event.getKeyCode() + "currentFocus = null");
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void finish() {
        super.finish();
        System.gc();
    }

    @Override
    public void onNetChange(boolean hasNet) {
        if (wifiImage != null) {
            if (hasNet) {
                wifiImage.setImageResource(R.mipmap.ic_wifi);
            } else {
                wifiImage.setImageResource(R.mipmap.no_net);
            }
        }
    }
   protected static class UpdateTimeTask extends TimerTask {

        private WeakReference<TitleView> titleView;
        private WeakReference<Activity> activity;
        public UpdateTimeTask(Activity activity, TitleView titleView) {
            this.titleView = new WeakReference<>(titleView);
            this.activity = new WeakReference<>(activity);
        }

        /**
         * The action to be performed by this timer task.
         */
        @Override
        public void run() {
            if (activity.get()==null||titleView.get()==null)return;
            activity.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    long currentTimeMillis = System.currentTimeMillis();
                    titleView.get().setTime(Util.convertSystemTime(currentTimeMillis));
                }
            });
        }

    }
}
