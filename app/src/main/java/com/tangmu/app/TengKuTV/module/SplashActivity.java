package com.tangmu.app.TengKuTV.module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.bean.LaunchAdBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.module.main.MainActivity;
import com.tangmu.app.TengKuTV.utils.GlideApp;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerVideoId;
import com.tencent.liteav.demo.play.SuperPlayerView;

import java.util.Timer;

public class SplashActivity extends BaseActivity {
    private LaunchAdBean launchAdBean;

    private ImageView img;
    private TextView jump;
    private TimeCount timeCount;
    private boolean timeDown = true;
    private SuperPlayerView superPlayer;
    private View jumpTime;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initData() {
        getLanchAd();
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendMessageDelayed(handler.obtainMessage(), 5000);
            }
        }).start();
    }

    @Override
    protected void initView() {
        img = findViewById(R.id.img);
        jump = findViewById(R.id.jump);
        jumpTime = findViewById(R.id.jumpTime);
        superPlayer = findViewById(R.id.superPlayer);
        superPlayer.disableControl();
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PLAYING) {
            superPlayer.onPause();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (launchAdBean == null) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                showTVAd();
            }
        }
    };

    private void handleResult(LaunchAdBean launchAdBean) {
        jumpTime.setVisibility(View.VISIBLE);
        if (launchAdBean.getPa_type() == 1) {
            img.setVisibility(View.VISIBLE);
            superPlayer.setVisibility(View.GONE);
            if (timeCount == null) {
                timeCount = new TimeCount(3600, 1000);
                timeCount.start();
            }
        } else {
            img.setVisibility(View.GONE);
            superPlayer.setVisibility(View.VISIBLE);
            superPlayer.onResume();
            superPlayer.setBalanceTimeCallBack(new SuperPlayerView.BalanceTimeCallBack() {
                @Override
                public void showBalance(int balance) {
                    if (timeCount == null) {
                        timeCount = new TimeCount(3600, 1000);
                        timeCount.start();
                    } else {
                        if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_END) {
                            jump();
                        }

                    }
                }
            });
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
//                                model.videoId.fileId = "5285890803501733066"; // 配置 FileId
//                                model.url=Util.convertVideoPath("202f672fvodcq1258540389/686fb6675285890801751433148/Pl1KsjsgyDMA.mp4");
                                model.title = "";
                                superPlayer.setAutoPlay(false);
                                superPlayer.playWithModel(model);
                            }
                        }
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
        if (timeCount != null && timeDown) {
            timeCount.cancel();
        }
        if (superPlayer.getVisibility() == View.VISIBLE)
            superPlayer.resetPlayer();
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

    /**
     * 验证码发送计时器
     */
    class TimeCount extends CountDownTimer {


        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            jump.setText(String.valueOf(millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            timeDown = false;
            jump.setText(R.string.jump);
            if (launchAdBean != null && launchAdBean.getPa_type() == 1)
                jump();
        }
    }
}
