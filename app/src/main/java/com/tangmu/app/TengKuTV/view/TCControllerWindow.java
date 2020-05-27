package com.tangmu.app.TengKuTV.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.bean.TCPlayImageSpriteInfo;
import com.tencent.liteav.demo.play.bean.TCPlayKeyFrameDescInfo;
import com.tencent.liteav.demo.play.bean.TCVideoQuality;
import com.tencent.liteav.demo.play.controller.IController;
import com.tencent.liteav.demo.play.controller.IControllerCallback;
import com.tencent.liteav.demo.play.utils.TCTimeUtil;
import com.tencent.liteav.demo.play.view.TCPointSeekBar;

import java.util.List;

/**
 * 窗口模式播放控件
 * <p>
 * 除基本播放控制外，还有手势控制快进快退、手势调节亮度音量等
 * <p>
 * 1、点击事件监听{@link #onClick(View)}
 * <p>
 * 2、触摸事件监听{@link #onTouchEvent(MotionEvent)}
 * <p>
 */
public class TCControllerWindow extends RelativeLayout implements IController, View.OnClickListener,
        TCPointSeekBar.OnSeekBarChangeListener {

    // UI控件
    private ImageView mIvPause;                               // 暂停播放按钮
    private TextView mTvTitle;                               // 视频名称文本
    private ImageView mBackground;                            // 背景

    private IControllerCallback mControllerCallback;                    // 播放控制回调

    private boolean mIsChangingSeekBarProgress;             // 进度条是否正在拖动，避免SeekBar由于视频播放的update而跳动
    private int mPlayType;                              // 当前播放视频类型
    private int mCurrentPlayState = -1;                 // 当前播放状态
    private long mDuration;                              // 视频总时长
    private long mLivePushDuration;                      // 直播推流总时长
    private long mProgress;                              // 当前播放进度

    private Bitmap mBackgroundBmp;                         // 背景图
    private long mLastClickTime;                         // 上次点击事件的时间
    private TCPointSeekBar mSeekBarProgress;
    private TextView bookAuthor;
    private TextView mTvCurrent;
    private TextView mTvDuration;
    private TextView mTvBookIntrol;
    private ImageView mIcCover;
    private Animation rotateAnimation;


    public TCControllerWindow(Context context) {
        super(context);
        init(context);
    }

    public TCControllerWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TCControllerWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化控件、手势检测监听器、亮度/音量/播放进度的回调
     */
    private void init(Context context) {
        initView(context);
    }

    /**
     * 初始化view
     */
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.controller_window, this);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIcCover = (ImageView) findViewById(R.id.cover);
        mTvBookIntrol = (TextView) findViewById(R.id.book_introl);
        bookAuthor = (TextView) findViewById(R.id.book_author);
        mIvPause = (ImageView) findViewById(R.id.iv_pause);
        findViewById(R.id.play_forward).setOnClickListener(this);
        findViewById(R.id.play_back).setOnClickListener(this);
        mSeekBarProgress = (TCPointSeekBar) findViewById(R.id.seekbar_progress);
        mSeekBarProgress.setProgress(0);
        mSeekBarProgress.setMax(100);
        mTvCurrent = (TextView) findViewById(R.id.tv_current);
        mTvDuration = (TextView) findViewById(R.id.tv_duration);

        mIvPause.setOnClickListener(this);

        mSeekBarProgress.setOnSeekBarChangeListener(this);


        mBackground = (ImageView) findViewById(R.id.small_iv_background);
        setBackground(mBackgroundBmp);
        initAnimate();
    }

    private void initAnimate() {
        rotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        rotateAnimation.setFillAfter(true);
    }

    /**
     * 切换播放状态
     * <p>
     * 双击和点击播放/暂停按钮会触发此方法
     */
    private void togglePlayState() {
        switch (mCurrentPlayState) {
            case SuperPlayerConst.PLAYSTATE_PAUSE:
            case SuperPlayerConst.PLAYSTATE_END:
                if (mControllerCallback != null) {
                    mControllerCallback.onResume();
                }
                break;
            case SuperPlayerConst.PLAYSTATE_PLAYING:
            case SuperPlayerConst.PLAYSTATE_LOADING:
                if (mControllerCallback != null) {
                    mControllerCallback.onPause();
                }
                break;
        }
        show();
    }


    /**
     * 设置回调
     *
     * @param callback 回调接口实现对象
     */
    @Override
    public void setCallback(IControllerCallback callback) {
        mControllerCallback = callback;
    }

    /**
     * 设置水印
     *
     * @param bmp 水印图
     * @param x   水印的x坐标
     * @param y   水印的y坐标
     */
    @Override
    public void setWatermark(final Bitmap bmp, float x, float y) {
    }

    /**
     * 显示控件
     */
    @Override
    public void show() {

    }

    /**
     * 隐藏控件
     */
    @Override
    public void hide() {

    }

    /**
     * 释放控件的内存
     */
    @Override
    public void release() {
    }

    /**
     * 更新播放状态
     *
     * @param playState 正在播放{@link SuperPlayerConst#PLAYSTATE_PLAYING}
     *                  正在加载{@link SuperPlayerConst#PLAYSTATE_LOADING}
     *                  暂停   {@link SuperPlayerConst#PLAYSTATE_PAUSE}
     *                  播放结束{@link SuperPlayerConst#PLAYSTATE_END}
     */
    @Override
    public void updatePlayState(int playState) {
        switch (playState) {
            case SuperPlayerConst.PLAYSTATE_PLAYING:
            case SuperPlayerConst.PLAYSTATE_LOADING:
                mIcCover.startAnimation(rotateAnimation);
                mIvPause.setImageResource(R.drawable.ic_playing_foucs_select);
                break;
            case SuperPlayerConst.PLAYSTATE_PAUSE:
            case SuperPlayerConst.PLAYSTATE_END:
                rotateAnimation.cancel();
                mIvPause.setImageResource(R.drawable.play_paused_foucs_select);
                break;
        }
        mCurrentPlayState = playState;
    }

    /**
     * 设置视频画质信息
     *
     * @param list 画质列表
     */
    @Override
    public void setVideoQualityList(List<TCVideoQuality> list) {
    }

    /**
     * 更新视频名称
     *
     * @param title 视频名称
     */
    @Override
    public void updateTitle(String title) {
    }

    /**
     * 更新视频播放进度
     *
     * @param current  当前进度(秒)
     * @param duration 视频总时长(秒)
     */
    @Override
    public void updateVideoProgress(long current, long duration) {
        mProgress = current < 0 ? 0 : current;
        mDuration = duration < 0 ? 0 : duration;
        mTvCurrent.setText(TCTimeUtil.formattedTime(mProgress));

        float percentage = mDuration > 0 ? ((float) mProgress / (float) mDuration) : 1.0f;
        if (mProgress == 0) {
            mLivePushDuration = 0;
            percentage = 0;
        }
        if (mPlayType == SuperPlayerConst.PLAYTYPE_LIVE || mPlayType == SuperPlayerConst.PLAYTYPE_LIVE_SHIFT) {
            mLivePushDuration = mLivePushDuration > mProgress ? mLivePushDuration : mProgress;
            long leftTime = mDuration - mProgress;
            mDuration = mDuration > SuperPlayerConst.MAX_SHIFT_TIME ? SuperPlayerConst.MAX_SHIFT_TIME : mDuration;
            percentage = 1 - (float) leftTime / (float) mDuration;
        }

        if (percentage >= 0 && percentage <= 1) {
            int progress = Math.round(percentage * mSeekBarProgress.getMax());
            if (!mIsChangingSeekBarProgress) {
                if (mPlayType == SuperPlayerConst.PLAYTYPE_LIVE) {
                    mSeekBarProgress.setProgress(mSeekBarProgress.getMax());
                } else {
                    mSeekBarProgress.setProgress(progress);
                }
            }
            mTvDuration.setText(TCTimeUtil.formattedTime(mDuration));
        }
    }

    /**
     * 更新播放类型
     *
     * @param type 点播     {@link SuperPlayerConst#PLAYTYPE_VOD}
     *             点播     {@link SuperPlayerConst#PLAYTYPE_LIVE}
     *             直播回看  {@link SuperPlayerConst#PLAYTYPE_LIVE_SHIFT}
     */
    @Override
    public void updatePlayType(int type) {

    }

    /**
     * 设置背景
     *
     * @param bitmap 背景图
     */
    @Override
    public void setBackground(final Bitmap bitmap) {
        this.post(new Runnable() {
            @Override
            public void run() {
                if (bitmap == null) return;
                if (mBackground == null) {
                    mBackgroundBmp = bitmap;
                } else {
                    setBitmap(mBackground, mBackgroundBmp);
                }
            }
        });
    }

    /**
     * 设置目标ImageView显示的图片
     */
    private void setBitmap(ImageView view, Bitmap bitmap) {
        if (view == null || bitmap == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(new BitmapDrawable(getContext().getResources(), bitmap));
        } else {
            view.setBackgroundDrawable(new BitmapDrawable(getContext().getResources(), bitmap));
        }
    }

    /**
     * 显示背景
     */
    @Override
    public void showBackground() {
        this.post(new Runnable() {
            @Override
            public void run() {
                ValueAnimator alpha = ValueAnimator.ofFloat(0.0f, 1);
                alpha.setDuration(500);
                alpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (Float) animation.getAnimatedValue();
                        mBackground.setAlpha(value);
                        if (value == 1) {
                            mBackground.setVisibility(VISIBLE);
                        }
                    }
                });
                alpha.start();
            }
        });
    }

    /**
     * 隐藏背景
     */
    @Override
    public void hideBackground() {
        this.post(new Runnable() {
            @Override
            public void run() {
                if (mBackground.getVisibility() != View.VISIBLE) return;
                ValueAnimator alpha = ValueAnimator.ofFloat(1.0f, 0.0f);
                alpha.setDuration(500);
                alpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (Float) animation.getAnimatedValue();
                        mBackground.setAlpha(value);
                        if (value == 0) {
                            mBackground.setVisibility(GONE);
                        }
                    }
                });
                alpha.start();
            }
        });
    }

    /**
     * 更新视频播放画质
     *
     * @param videoQuality 画质
     */
    @Override
    public void updateVideoQuality(TCVideoQuality videoQuality) {

    }

    /**
     * 更新雪碧图信息
     *
     * @param info 雪碧图信息
     */
    @Override
    public void updateImageSpriteInfo(TCPlayImageSpriteInfo info) {

    }

    /**
     * 更新关键帧信息
     *
     * @param list 关键帧信息列表
     */
    @Override
    public void updateKeyFrameDescInfo(List<TCPlayKeyFrameDescInfo> list) {

    }


    /**
     * 设置点击事件监听
     */
    @Override
    public void onClick(View view) {
        if (System.currentTimeMillis() - mLastClickTime < 300) { //限制点击频率
            return;
        }
        mLastClickTime = System.currentTimeMillis();
        int id = view.getId();
        if (id == R.id.iv_pause) { //暂停\播放按钮
            togglePlayState();
        } else if (id == R.id.play_forward) { //下一集
            if (selectBookCallback != null) {
                mTvCurrent.setText("00:00");
                mTvDuration.setText("00:00");
                mSeekBarProgress.setProgress(0);
                selectBookCallback.forward();
            }
        } else if (id == R.id.play_back) { //上一集
            if (selectBookCallback != null) {
                mTvCurrent.setText("00:00");
                mTvDuration.setText("00:00");
                mSeekBarProgress.setProgress(0);
                selectBookCallback.back();
            }
        }
    }

    private SuperBookPlayerView.SelectBookCallback selectBookCallback;

    public void setBookSelectCallback(SuperBookPlayerView.SelectBookCallback selectBookCallback) {
        this.selectBookCallback = selectBookCallback;
    }

    public void setInfo(String title, String author, String des) {
        bookAuthor.setText(String.format("%s%s", getResources().getString(R.string.author), author));
        mTvTitle.setText(title);
        mTvBookIntrol.setText(des);
    }

    @Override
    public void onProgressChanged(TCPointSeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            float percentage = ((float) progress) / seekBar.getMax();
            float currentTime = (mDuration * percentage);
            mTvCurrent.setText(TCTimeUtil.formattedTime((long) currentTime));
        }
    }

    @Override
    public void onStartTrackingTouch(TCPointSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(TCPointSeekBar seekBar) {
        int curProgress = seekBar.getProgress();
        int maxProgress = seekBar.getMax();
        if (curProgress >= 0 && curProgress <= maxProgress) {
            float percentage = ((float) curProgress) / maxProgress;
            int position = (int) (mDuration * percentage);
            if (mControllerCallback != null) {
                mControllerCallback.onSeekTo(position);
                mControllerCallback.onResume();
            }
        }
    }

    public void setCover(String coverImgPath) {
        GlideUtils.getRequest(getContext(), coverImgPath)
                .circleCrop().into(mIcCover);
        mIcCover.startAnimation(rotateAnimation);
    }
}
