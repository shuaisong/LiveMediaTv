package com.tangmu.app.TengKuTV.module;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.bean.LaunchAdBean;
import com.tangmu.app.TengKuTV.module.main.MainActivity;
import com.tangmu.app.TengKuTV.utils.GlideApp;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerVideoId;
import com.tencent.liteav.demo.play.SuperPlayerView;

/**
 * 广告页
 */
public class AdActivity extends AppCompatActivity {

    private ImageView img;
    private TextView jump;
    private TimeCount timeCount;
    private boolean timeDown = true;
    private SuperPlayerView superPlayer;
    private LaunchAdBean launchAdBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        initView();
    }

    private void handleResult(LaunchAdBean launchAdBean) {
        if (launchAdBean.getPa_type() == 1) {
            superPlayer.setVisibility(View.GONE);
            GlideApp.with(AdActivity.this)
                    .load(Util.convertImgPath(launchAdBean.getPa_url())).centerCrop()
                    .into(img);
            if (timeCount == null) {
                timeCount = new TimeCount(3200, 1000);
                timeCount.start();
            }
        } else {
            img.setVisibility(View.GONE);
            superPlayer.setVisibility(View.VISIBLE);
            SuperPlayerModel model = new SuperPlayerModel();
            model.appId = Constant.PLAYID;// 配置 AppId
            model.videoId = new SuperPlayerVideoId();
            model.videoId.fileId = launchAdBean.getPa_fileid(); // 配置 FileId
            model.title = "";
            superPlayer.playWithModel(model);
            superPlayer.setBalanceTimeCallBack(new SuperPlayerView.BalanceTimeCallBack() {
                @Override
                public void showBalance(int balance) {
                    if (timeCount == null) {
                        timeCount = new TimeCount(3200, 1000);
                        timeCount.start();
                    } else {
                        if (balance <= 0) {
                            jump();
                        }
                    }
                }
            });
        }
    }


    protected void initView() {
        img = findViewById(R.id.img);
        jump = findViewById(R.id.jump);
        superPlayer = findViewById(R.id.superPlayer);
        superPlayer.disableControl();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PLAYING) {
            superPlayer.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        launchAdBean = (LaunchAdBean) getIntent().getSerializableExtra("ad");
        if (launchAdBean != null) {
            handleResult(launchAdBean);
        } else {
            startActivity();
        }
    }

    public int setLayoutId() {
        return R.layout.activity_ad;
    }


    @Override
    protected void onDestroy() {
        if (timeCount != null && timeDown) {
            timeCount.cancel();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            jump();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

    }

    private void jump() {
        if (timeCount != null && timeDown) timeCount.cancel();
        timeCount = null;
        startActivity();
    }

    private void startActivity() {
        startActivity(new Intent(this, MainActivity.class));
        if (superPlayer.getVisibility() == View.VISIBLE)
            superPlayer.resetPlayer();
        finish();
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
