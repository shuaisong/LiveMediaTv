package com.tangmu.app.TengKuTV.module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.shcmcc.tools.GetSysInfo;
import com.tangmu.app.TengKuTV.AppStatusService;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.bean.LaunchAdBean;
import com.tangmu.app.TengKuTV.bean.MiguLoginBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.module.main.MainActivity;
import com.tangmu.app.TengKuTV.utils.GlideApp;
import com.tangmu.app.TengKuTV.utils.InstallUtil;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerVideoId;
import com.tencent.liteav.demo.play.SuperPlayerView;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class SplashActivity extends BaseActivity {
    private LaunchAdBean launchAdBean;

    private ImageView img;
    private FrameLayout fragment_bg;
    private TextView jump;
    private TimeCount timeCount;
    private boolean timeDown = true;
    private SuperPlayerView superPlayer;
    private View jumpTime;
    private RelativeLayout no_net;
    private Timer timer;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initData() {
        if (InstallUtil.CheckRootPathSU()) {
            LogUtil.e("device is rooted");
        }
        getUserInfo();
    }

    @Override
    protected void initView() {
        img = findViewById(R.id.img);
        jump = findViewById(R.id.jump);
        fragment_bg = findViewById(R.id.fragment_bg);
        jumpTime = findViewById(R.id.jumpTime);
        superPlayer = findViewById(R.id.superPlayer);
        no_net = findViewById(R.id.no_net);
        findViewById(R.id.tv_set_net).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                try {
                    startActivityForResult(intent, 2002);
                } catch (Exception ignored) {
                }
            }
        });
        superPlayer.disableControl();
    }

    private void getUserInfo() {
//        GetSysInfo getSysInfo = GetSysInfo.getInstance("10086", "", getApplicationContext());
//        String firmwareVersion = getSysInfo.getFirmwareVersion();
//        String snNum = getSysInfo.getSnNum();
//        String terminalType = getSysInfo.getTerminalType();
//        String hardwareVersion = getSysInfo.getHardwareVersion();
//
//        String  epgToken = getSysInfo.getEpgToken();
//        String epgUserId = getSysInfo.getEpgUserId();
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("firmwareVersion",firmwareVersion);
//        jsonObject.addProperty("terminalType",terminalType);
//        jsonObject.addProperty("hardwareVersion",hardwareVersion);
//
//        ancyUserInfo(epgToken,snNum,epgUserId,jsonObject.toString());

        ancyUserInfo("5167bb95141133d31072abf3e35921b326wx", "006201FF0001007006BB60D21C6EB759", "0107723607936491059",
                "{\"firmwareVersion\":\"HiSTBAndroidV5/Hi3798MV300/Hi3798MV300:4.4.2/CM201-22/004.992.062:eng/test-keys\",\"terminalType\":\"CM201-22\",\"hardwareVersion\":\"004.992.062\"}");

    }

    private void ancyUserInfo(String epgToken, String snNum, String epgUserId, String s) {
        PreferenceManager.getInstance().setToken(epgToken);
        PreferenceManager.getInstance().setUserName(epgUserId);
        OkGo.<BaseResponse<MiguLoginBean>>post(Constant.IP + Constant.tvRegister)
                .params("token", epgToken)
                .params("stbid", snNum)
                .params("user_name", epgUserId)
                .params("mes", s)
                .execute(new JsonCallback<BaseResponse<MiguLoginBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<MiguLoginBean>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            PreferenceManager.getInstance().setTuid(response.body().getResult().getU_id());
                            PreferenceManager.getInstance().setLogin(response.body().getResult());
                            handler = new TimeHandler(SplashActivity.this);
                            timer = new Timer();
                            HandlerTask task = new HandlerTask(SplashActivity.this);
                            timer.schedule(task, 4000, 1000);
                        } else {
                            ToastUtil.showText(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<MiguLoginBean>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, AppStatusService.class));
        if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PAUSE) {
            superPlayer.onResume();
        }
    }

    private void setNoNet() {
        if (checkNetAvailable()) {
            no_net.setVisibility(View.GONE);
//            fragment_bg.setBackground(getResources().getDrawable(R.mipmap.splash_bg));
            getLanchAd();
        } else {
            fragment_bg.setBackgroundColor(getResources().getColor(R.color.black));
            no_net.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PLAYING) {
            superPlayer.onPause();
        }
    }


    @SuppressLint("HandlerLeak")
    Handler handler; /*= new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (timer != null) {
                    timer.cancel();
                }
                setNoNet();
            } else {
                if (launchAdBean == null) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showTVAd();
                }
            }
        }
    };
*/

    private void handleResult(LaunchAdBean launchAdBean) {
        jumpTime.setVisibility(View.VISIBLE);
        if (launchAdBean.getPa_type() == 1) {
            img.setVisibility(View.VISIBLE);
            superPlayer.setVisibility(View.GONE);
            if (timeCount == null) {
                timeCount = new TimeCount(3600, 1000, SplashActivity.this);
                timeCount.start();
            }
        } else {
            img.setVisibility(View.GONE);
            superPlayer.setVisibility(View.VISIBLE);
            superPlayer.onResume();
            superPlayer.setBalanceTimeCallBack(new TimeCallback());
        }
    }

    class TimeCallback implements SuperPlayerView.BalanceTimeCallBack {

        @Override
        public void showBalance(int balance) {
            if (timeCount == null) {
                timeCount = new TimeCount(3600, 1000, SplashActivity.this);
                timeCount.start();
            } else {
                if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_END) {
                    jump();
                }
            }
        }
    }

    private void showTVAd() {
        handleResult(launchAdBean);
    }

    private void getLanchAd() {
        OkGo.<BaseResponse<LaunchAdBean>>post(Constant.IP + Constant.LaunchAd)
                .params("type", 1)
                .execute(new JsonCallback<BaseResponse<LaunchAdBean>>() {

                    @Override
                    public void onSuccess(Response<BaseResponse<LaunchAdBean>> response) {
                        super.onSuccess(response);
                        if (response.body().getStatus() == 0) {
                            launchAdBean = response.body().getResult();
                            handler.sendMessage(handler.obtainMessage());
                            if (launchAdBean.getPa_type() == 1)
                                GlideApp.with(SplashActivity.this)
                                        .load(Util.convertImgPath(launchAdBean.getPa_url()))
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true).centerCrop()
                                        .into(img);
                            else {
                                SuperPlayerModel model = new SuperPlayerModel();
                                model.appId = Constant.PLAYID;// 配置 AppId
                                model.videoId = new SuperPlayerVideoId();
                                model.videoId.fileId = launchAdBean.getPa_fileid(); // 配置 FileId
//                                model.videoId.fileId = "5285890803501733066"; // 配置 FileId   广告
//                                model.videoId.fileId = "5285890801344549602"; // 配置 FileId   视频
//                                model.url = Util.convertVideoPath("202f672fvodcq1258540389/f14244245285890803501733066/z9CVspaxI9AA.mp4");
                                model.title = "";
                                superPlayer.setAutoPlay(true);
                                superPlayer.playWithModel(model);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<LaunchAdBean>> response) {
                        super.onError(response);
                        LogUtil.e(response.getException().getMessage());
                        startActivity();
                    }
                });
    }

    private void jump() {
        if (timeCount != null && timeDown) timeCount.cancel();
        timeCount = null;
        startActivity();
    }

    private void startActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
        superPlayer.setBalanceTimeCallBack(null);
        if (superPlayer.getVisibility() == View.VISIBLE) {
            superPlayer.resetPlayer();
            superPlayer.release();
            superPlayer = null;
        }
        if (timeCount != null && timeDown) {
            timeCount.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (jumpTime.getVisibility() == View.VISIBLE)
                jump();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2002) {
            setNoNet();
        }
    }

    /**
     * 验证码发送计时器
     */
    static class TimeCount extends CountDownTimer {
        WeakReference<SplashActivity> weakReference;

        TimeCount(long millisInFuture, long countDownInterval, SplashActivity a) {
            super(millisInFuture, countDownInterval);
            weakReference = new WeakReference<SplashActivity>(a);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (weakReference.get() == null) return;
            weakReference.get().jump.setText(String.valueOf(millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            if (weakReference.get() == null) return;
            weakReference.get().timeDown = false;
            weakReference.get().jump.setText(R.string.jump);
            if (weakReference.get().launchAdBean != null && weakReference.get().launchAdBean.getPa_type() == 1)
                weakReference.get().jump();
        }
    }

    static class TimeHandler extends Handler {
        WeakReference<SplashActivity> weakReference;

        public TimeHandler(SplashActivity a) {
            weakReference = new WeakReference<SplashActivity>(a);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (weakReference.get() == null) return;
            if (msg.what == 1) {
                if (weakReference.get().timer != null) {
                    weakReference.get().timer.cancel();
                }
                weakReference.get().setNoNet();
            } else {
                if (weakReference.get().launchAdBean == null) {
                    Intent intent = new Intent(weakReference.get(), MainActivity.class);
                    weakReference.get().startActivity(intent);
                    weakReference.get().finish();
                } else {
                    weakReference.get().showTVAd();
                }
            }
        }
    }

    static class HandlerTask extends TimerTask {

        WeakReference<SplashActivity> weakReference;

        public HandlerTask(SplashActivity a) {
            weakReference = new WeakReference<SplashActivity>(a);
        }

        @Override
        public void run() {
            if (weakReference.get() == null) return;
            Message message = new Message();
            message.what = 1;
            weakReference.get().handler.sendMessage(message);
        }
    }
}
