package com.tencent.liteav.demo.play.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.liteav.demo.play.R;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.bean.ImgFrameBean;
import com.tencent.liteav.demo.play.bean.TCPlayImageSpriteInfo;
import com.tencent.liteav.demo.play.bean.TCPlayKeyFrameDescInfo;
import com.tencent.liteav.demo.play.bean.TCVideoQuality;
import com.tencent.liteav.demo.play.bean.VideoBean;
import com.tencent.liteav.demo.play.net.TCLogReport;
import com.tencent.liteav.demo.play.utils.TCTimeUtil;
import com.tencent.liteav.demo.play.utils.TCVideoGestureUtil;
import com.tencent.liteav.demo.play.view.PauseAdView;
import com.tencent.liteav.demo.play.view.TCPointSeekBar;
import com.tencent.liteav.demo.play.view.TCVideoProgressLayout;
import com.tencent.liteav.demo.play.view.TCVodAnthologyView;
import com.tencent.liteav.demo.play.view.TCVodMoreView;
import com.tencent.liteav.demo.play.view.TCVodQualityView;
import com.tencent.liteav.demo.play.view.TCVodSettingMoreView;
import com.tencent.liteav.demo.play.view.TCVolumeBrightnessProgressLayout;
import com.tencent.rtmp.TXImageSprite;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.LogUtils;
import me.jessyan.autosize.utils.ScreenUtils;

/**
 * ????????????????????????
 * <p>
 * ???{@link TCControllerWindow}???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
 * ?????????????????????????????????????????????????????????????????????
 * <p>
 * 1?????????????????????{@link #onClick(View)}
 * <p>
 * 2?????????????????????{@link #onTouchEvent(MotionEvent)}
 * <p>
 * 3??????????????????????????????{@link #onProgressChanged(TCPointSeekBar, int, boolean)}
 * {@link #onStartTrackingTouch(TCPointSeekBar)}{@link #onStopTrackingTouch(TCPointSeekBar)}
 * <p>
 * 4????????????????????????????????????{@link #onSeekBarPointClick(View, int)}
 * <p>
 * 5?????????????????????{@link #onQualitySelect(TCVideoQuality)}
 * <p>
 * 6?????????????????????{@link #onSpeedChange(float)}
 * <p>
 * 7?????????????????????{@link #onMirrorChange(boolean)}
 * <p>
 * 8?????????????????????{@link #onHWAcceleration(boolean)}
 */
public class TCControllerFullScreen extends RelativeLayout implements IController, View.OnClickListener,
        TCVodMoreView.Callback, TCVodQualityView.Callback, TCPointSeekBar.OnSeekBarChangeListener, TCPointSeekBar.OnSeekBarPointClickListener, TCVodAnthologyView.Callback, View.OnFocusChangeListener {

    // UI??????
    private LinearLayout mLayoutBottom;                          // ???????????????????????????
    private ImageView mIvPause;                               // ??????????????????
    private TextView mTvBackToLive;                          // ??????????????????
    private ImageView mIvWatermark;                           // ??????
    private TextView mTvCurrent;                             // ??????????????????
    private TextView mTvDuration;                            // ???????????????
    private TCPointSeekBar mSeekBarProgress;                       // ???????????????
    private LinearLayout mLayoutReplay;                          // ????????????????????????
    private ProgressBar mPbLiveLoading;                         // ?????????
    private TCVolumeBrightnessProgressLayout mGestureVolumeBrightnessProgressLayout; // ????????????????????????
    private TCVideoProgressLayout mGestureVideoProgressLayout;            // ????????????????????????

    private TextView mTvQuality;                             // ??????????????????
    private TCVodQualityView mVodQualityView;                        // ??????????????????
    private TextView mTvVttText;                             // ???????????????????????????

    private IControllerCallback mControllerCallback;                    // ??????????????????
    private HideViewControllerViewRunnable mHideViewRunnable;                      // ?????????????????????
    private HideLockViewRunnable mHideLockViewRunnable;                  // ???????????????????????????
    private GestureDetector mGestureDetector;                       // ?????????????????????
    private TCVideoGestureUtil mVideoGestureUtil;                      // ??????????????????
    private boolean isSetPausedImg;
    private BaseQuickAdapter<ImgFrameBean, BaseViewHolder> imgFrameAdapter;
    private RecyclerView imgFrames;
    private boolean haveImgFrame;//??????????????????

    public void setVideoQualityCallback(TCVodQualityView.VideoQualityCallback videoQualityCallback) {
        mVodQualityView.setVideoQualityCallback(videoQualityCallback);
    }

    public boolean isShowing() {
        return isShowing;
    }

    private boolean isShowing;                              // ??????????????????
    private boolean mIsChangingSeekBarProgress;             // ????????????????????????????????????SeekBar?????????????????????update?????????
    private int mPlayType;                              // ????????????????????????
    private int mCurrentPlayState = -1;                 // ??????????????????
    private long mDuration;                              // ???????????????
    private long mLivePushDuration;                      // ?????????????????????
    private long mProgress;                              // ??????????????????

    private Bitmap mBackgroundBmp;                         // ?????????
    private Bitmap mWaterMarkBmp;                          // ?????????
    private float mWaterMarkBmpX;                         // ??????x??????
    private float mWaterMarkBmpY;                         // ??????y??????

    private boolean mDanmuOn;                               // ??????????????????
    private TXImageSprite mTXImageSprite;                         // ???????????????
    private List<TCPlayKeyFrameDescInfo> mTXPlayKeyFrameDescInfoList;            // ???????????????
    private int mSelectedPos = -1;                      // ???????????????????????????
    private boolean mLockScreen;                            // ????????????

    private TCVideoQuality mDefaultVideoQuality;                   // ????????????
    private List<TCVideoQuality> mVideoQualityList;                      // ????????????
    private boolean mFirstShowQuality;                      // ?????????????????????????????????
    private LinearLayout mNormalLayoutBottom;
    private RadioGroup mFullLayoutBottom;
    private TCVodAnthologyView anthologyView;
    private TextView tvAnthology;
    private TCVodSettingMoreView tcVodSettingMoreView;
    private PauseAdView pauseAdView;
    private TextView mVideoType;
    private TextView mTvQuality1;

    public TCControllerFullScreen(Context context) {
        super(context);
        init(context);
    }

    public TCControllerFullScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TCControllerFullScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * ????????????????????????????????????????????????/??????/?????????????????????
     */
    private void init(Context context) {
        initView(context);
        mHideViewRunnable = new HideViewControllerViewRunnable(this);
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mLockScreen) return false;
                togglePlayState();
                show();
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                toggle();
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent downEvent, MotionEvent moveEvent, float distanceX, float distanceY) {
                if (mLockScreen) return false;
                if (downEvent == null || moveEvent == null) {
                    return false;
                }
                if (mVideoGestureUtil != null && mGestureVolumeBrightnessProgressLayout != null) {
                    mVideoGestureUtil.check(mGestureVolumeBrightnessProgressLayout.getHeight(), downEvent, moveEvent, distanceX, distanceY);
                }
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                if (mLockScreen) return true;
                if (mVideoGestureUtil != null) {
                    mVideoGestureUtil.reset(getWidth(), mSeekBarProgress.getProgress());
                }
                return true;
            }

        });
        mGestureDetector.setIsLongpressEnabled(false);

        mVideoGestureUtil = new TCVideoGestureUtil(getContext());
        mVideoGestureUtil.setVideoGestureListener(new TCVideoGestureUtil.VideoGestureListener() {
            @Override
            public void onBrightnessGesture(float newBrightness) {
                if (mGestureVolumeBrightnessProgressLayout != null) {
                    mGestureVolumeBrightnessProgressLayout.setProgress((int) (newBrightness * 100));
                    mGestureVolumeBrightnessProgressLayout.setImageResource(R.drawable.ic_light_max);
                    mGestureVolumeBrightnessProgressLayout.show();
                }
            }

            @Override
            public void onVolumeGesture(float volumeProgress) {
                if (mGestureVolumeBrightnessProgressLayout != null) {
                    mGestureVolumeBrightnessProgressLayout.setImageResource(R.drawable.ic_volume_max);
                    mGestureVolumeBrightnessProgressLayout.setProgress((int) volumeProgress);
                    mGestureVolumeBrightnessProgressLayout.show();
                }
            }

            @Override
            public void onSeekGesture(int progress) {
                mIsChangingSeekBarProgress = true;
                if (mGestureVideoProgressLayout != null) {

                    if (progress > mSeekBarProgress.getMax()) {
                        progress = mSeekBarProgress.getMax();
                    }
                    if (progress < 0) {
                        progress = 0;
                    }
                    mGestureVideoProgressLayout.setProgress(progress);
                    mGestureVideoProgressLayout.show();

                    float percentage = ((float) progress) / mSeekBarProgress.getMax();
                    float currentTime = (mDuration * percentage);
                    if (mPlayType == SuperPlayerConst.PLAYTYPE_LIVE || mPlayType == SuperPlayerConst.PLAYTYPE_LIVE_SHIFT) {
                        if (mLivePushDuration > SuperPlayerConst.MAX_SHIFT_TIME) {
                            currentTime = (int) (mLivePushDuration - SuperPlayerConst.MAX_SHIFT_TIME * (1 - percentage));
                        } else {
                            currentTime = mLivePushDuration * percentage;
                        }
                        mGestureVideoProgressLayout.setTimeText(TCTimeUtil.formattedTime((long) currentTime));
                    } else {
                        mGestureVideoProgressLayout.setTimeText(TCTimeUtil.formattedTime((long) currentTime) + " / " + TCTimeUtil.formattedTime((long) mDuration));
                    }
                    setThumbnail(progress);
                }
                if (mSeekBarProgress != null)
                    mSeekBarProgress.setProgress(progress);
            }
        });
    }

    /**
     * ?????????view
     */
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.vod_controller_fullscreen, this);
        pauseAdView = (PauseAdView) findViewById(R.id.pause_ad_view);
        mVideoType = (TextView) findViewById(R.id.videoType);
        mLayoutBottom = (LinearLayout) findViewById(R.id.layout_bottom);
        mLayoutBottom.setOnClickListener(this);
        mNormalLayoutBottom = (LinearLayout) findViewById(R.id.normal_layout_bottom);
        mFullLayoutBottom = (RadioGroup) findViewById(R.id.full_layout_bottom);
        findViewById(R.id.tv_anthology).setOnClickListener(this);
        findViewById(R.id.tv_quality).setOnClickListener(this);
        findViewById(R.id.tv_more).setOnClickListener(this);
        mLayoutReplay = (LinearLayout) findViewById(R.id.layout_replay);

        mIvPause = (ImageView) findViewById(R.id.iv_pause);
        mTvCurrent = (TextView) findViewById(R.id.tv_current);
        mTvDuration = (TextView) findViewById(R.id.tv_duration);

        mSeekBarProgress = (TCPointSeekBar) findViewById(R.id.seekbar_progress);
        mSeekBarProgress.setProgress(0);
        mSeekBarProgress.setOnPointClickListener(this);
        mSeekBarProgress.setOnSeekBarChangeListener(this);
        mTvQuality = (TextView) findViewById(R.id.tv_quality);
        mTvQuality1 = (TextView) findViewById(R.id.tv_quality1);
        tvAnthology = (TextView) findViewById(R.id.tv_anthology);
        mTvQuality1.setOnClickListener(this);
        mTvBackToLive = (TextView) findViewById(R.id.tv_backToLive);
        mPbLiveLoading = (ProgressBar) findViewById(R.id.pb_live);

        mVodQualityView = (TCVodQualityView) findViewById(R.id.vodQualityView);
        mVodQualityView.setCallback(this);

        tcVodSettingMoreView = (TCVodSettingMoreView) findViewById(R.id.settingMoreView);
        tcVodSettingMoreView.setCallback(this);

        anthologyView = (TCVodAnthologyView) findViewById(R.id.anthologyView);
        anthologyView.setCallback(this);

        mTvBackToLive.setOnClickListener(this);
        mLayoutReplay.setOnClickListener(this);
        mIvPause.setOnClickListener(this);
        mTvQuality.setOnClickListener(this);
        tvAnthology.setOnClickListener(this);
        mTvVttText = (TextView) findViewById(R.id.large_tv_vtt_text);
        mTvVttText.setOnClickListener(this);
        mGestureVolumeBrightnessProgressLayout = (TCVolumeBrightnessProgressLayout) findViewById(R.id.gesture_progress);
        mGestureVideoProgressLayout = (TCVideoProgressLayout) findViewById(R.id.video_progress_layout);
        mIvWatermark = findViewById(R.id.large_iv_water_mark);
        initImgFrames();
        mDefaultVideoQuality = new TCVideoQuality();
        mDefaultVideoQuality.index = 2;
    }

    private void initImgFrames() {
        imgFrames = findViewById(R.id.imgFrames);
        imgFrameAdapter = new BaseQuickAdapter<ImgFrameBean, BaseViewHolder>(R.layout.item_img_frame) {
            @Override
            protected void convert(BaseViewHolder helper, ImgFrameBean item) {
                helper.itemView.setOnFocusChangeListener(TCControllerFullScreen.this);
                helper.setText(R.id.time, TCTimeUtil.formattedTime(item.getTime()));
                if (item.getBitmap() != null) {
                    helper.setImageBitmap(R.id.image, item.getBitmap());
                }
            }
        };
        imgFrameAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImgFrameBean item = imgFrameAdapter.getItem(position);
                if (item != null) {
                    mControllerCallback.onSeekTo(item.getTime());
                    hide();
                }
            }
        });
        imgFrames.setAdapter(imgFrameAdapter);
    }

    /**
     * ????????????????????????
     *
     * @param view      ????????????
     * @param isVisible ?????????true ?????????false
     */
    private void toggleView(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    /**
     * ??????????????????
     * <p>
     * ?????????????????????/??????????????????????????????
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
                mLayoutReplay.setVisibility(View.GONE);
                break;
        }
        show();
    }


    /**
     * ????????????????????????
     */
    public void toggle() {
        if (!mLockScreen) {
            if (isShowing) {
                hide();
            } else {
                show();
                if (mHideViewRunnable != null) {
                    getHandler().removeCallbacks(mHideViewRunnable);
                    getHandler().postDelayed(mHideViewRunnable, 7000);
                }
            }
        } else {
            if (mHideLockViewRunnable != null) {
                getHandler().removeCallbacks(mHideLockViewRunnable);
                getHandler().postDelayed(mHideLockViewRunnable, 7000);
            }
        }

    }

    /**
     * ????????????
     *
     * @param callback ????????????????????????
     */
    @Override
    public void setCallback(IControllerCallback callback) {
        mControllerCallback = callback;
    }

    /**
     * ????????????
     *
     * @param bmp ?????????
     * @param x   ?????????x??????
     * @param y   ?????????y??????
     */
    @Override
    public void setWatermark(Bitmap bmp, float x, float y) {
        mWaterMarkBmp = bmp;
        mWaterMarkBmpY = y;
        mWaterMarkBmpX = x;
    }

    /**
     * ????????????
     */
    @Override
    public void show() {
        isShowing = true;
        mLayoutBottom.setVisibility(View.VISIBLE);
        mFullLayoutBottom.setVisibility(VISIBLE);
        if (mPlayType != SuperPlayerConst.PLAYTYPE_LIVE) {
            mFullLayoutBottom.setVisibility(VISIBLE);
            mTvQuality1.setVisibility(GONE);
        } else {
            View viewById = mVodQualityView.findViewById(R.id.lv_quality);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewById.getLayoutParams();
            layoutParams.setMarginEnd(AutoSizeUtils.dp2px(getContext(), 70));
            layoutParams.bottomMargin = AutoSizeUtils.dp2px(getContext(), 12);
            layoutParams.gravity = Gravity.END;
            viewById.setLayoutParams(layoutParams);
            mFullLayoutBottom.setVisibility(GONE);
            mTvQuality1.setVisibility(VISIBLE);
        }
        if (mHideLockViewRunnable != null) {
            this.getHandler().removeCallbacks(mHideLockViewRunnable);
        }
        /*if (mPlayType == SuperPlayerConst.PLAYTYPE_LIVE_SHIFT) {
            if (mLayoutBottom.getVisibility() == VISIBLE)
                mTvBackToLive.setVisibility(View.VISIBLE);
        }*/
        List<TCPointSeekBar.PointParams> pointParams = new ArrayList<>();
        if (mTXPlayKeyFrameDescInfoList != null)
            for (TCPlayKeyFrameDescInfo info : mTXPlayKeyFrameDescInfoList) {
                int progress = (int) (info.time / mDuration * mSeekBarProgress.getMax());
                pointParams.add(new TCPointSeekBar.PointParams(progress, Color.WHITE));
            }
        mSeekBarProgress.setPointList(pointParams);
        if (mHideViewRunnable != null&&TCControllerFullScreen.this.getHandler()!=null) {
            TCControllerFullScreen.this.getHandler().removeCallbacks(mHideViewRunnable);
            TCControllerFullScreen.this.getHandler().postDelayed(mHideViewRunnable, 15000);
        }
    }

    /**
     * ????????????
     */
    @Override
    public void hide() {
        isShowing = false;
        isShowingMenu = false;
        mIsChangingSeekBarProgress = false;
        imgFrames.setVisibility(GONE);
        mLayoutBottom.setVisibility(View.GONE);
        mFullLayoutBottom.setVisibility(GONE);
        mFullLayoutBottom.check(-1);
        mVodQualityView.setVisibility(View.GONE);
        mTvQuality1.setVisibility(GONE);
        mTvVttText.setVisibility(GONE);
        anthologyView.setVisibility(GONE);
        if (mPlayType == SuperPlayerConst.PLAYTYPE_LIVE_SHIFT) {
            mTvBackToLive.setVisibility(View.GONE);
        }
        mVodQualityView.setVisibility(GONE);
        tcVodSettingMoreView.setVisibility(GONE);
    }

    /**
     * ?????????????????????
     */
    @Override
    public void release() {
        releaseTXImageSprite();
    }

    /**
     * ??????????????????
     *
     * @param playState ????????????{@link com.tencent.liteav.demo.play.SuperPlayerConst#PLAYSTATE_PLAYING}
     *                  ????????????{@link com.tencent.liteav.demo.play.SuperPlayerConst#PLAYSTATE_LOADING}
     *                  ??????   {@link com.tencent.liteav.demo.play.SuperPlayerConst#PLAYSTATE_PAUSE}
     *                  ????????????{@link com.tencent.liteav.demo.play.SuperPlayerConst#PLAYSTATE_END}
     */
    @Override
    public void updatePlayState(int playState) {
        switch (playState) {
            case SuperPlayerConst.PLAYSTATE_PLAYING:
                mIvPause.setImageResource(R.drawable.ic_vod_pause_normal);
                toggleView(mPbLiveLoading, false);
                toggleView(mLayoutReplay, false);
                pauseAdView.hide();
                break;
            case SuperPlayerConst.PLAYSTATE_LOADING:
                mIvPause.setImageResource(R.drawable.ic_vod_pause_normal);
                toggleView(mPbLiveLoading, true);
                toggleView(mLayoutReplay, false);
                pauseAdView.hide();
                break;
            case SuperPlayerConst.PLAYSTATE_PAUSE:
                mIvPause.setImageResource(R.drawable.ic_vod_play_normal);
                toggleView(mLayoutReplay, false);
                if (isSetPausedImg)
                    pauseAdView.show();
                break;
            case SuperPlayerConst.PLAYSTATE_END:
                mIvPause.setImageResource(R.drawable.ic_vod_play_normal);
                toggleView(mLayoutReplay, true);
                if (isSetPausedImg)
                    pauseAdView.show();
                break;
        }
        mCurrentPlayState = playState;
    }

    /**
     * ????????????????????????
     *
     * @param list ????????????
     */
    @Override
    public void setVideoQualityList(List<TCVideoQuality> list) {
        for (TCVideoQuality tcVideoQuality : list) {
            if (!TextUtils.isEmpty(tcVideoQuality.name))
                if (tcVideoQuality.name.equals("FLU")) {
                    tcVideoQuality.index = 0;
                } else if (tcVideoQuality.name.equals("SD")) {
                    tcVideoQuality.index = 1;
                } else if (tcVideoQuality.name.equals("HD")) {
                    tcVideoQuality.index = 2;
                } else if (tcVideoQuality.name.equals("FHD")) {
                    tcVideoQuality.index = 3;
                } else if (tcVideoQuality.name.equals("2K")) {
                    tcVideoQuality.index = 4;
                } else if (tcVideoQuality.name.equals("4K")) {
                    tcVideoQuality.index = 5;
                }
        }
        mVideoQualityList = list;
        mFirstShowQuality = false;
    }

    /**
     * ??????????????????
     *
     * @param title ????????????
     */
    @Override
    public void updateTitle(String title) {
    }

    /**
     * ????????????????????????
     *
     * @param current  ????????????(???)
     * @param duration ???????????????(???)
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
            if (!mIsChangingSeekBarProgress)
                mSeekBarProgress.setProgress(progress);
            mTvDuration.setText(TCTimeUtil.formattedTime(mDuration));
        }
    }

    /**
     * ??????????????????
     *
     * @param type ??????     {@link com.tencent.liteav.demo.play.SuperPlayerConst#PLAYTYPE_VOD}
     *             ??????     {@link com.tencent.liteav.demo.play.SuperPlayerConst#PLAYTYPE_LIVE}
     *             ????????????  {@link com.tencent.liteav.demo.play.SuperPlayerConst#PLAYTYPE_LIVE_SHIFT}
     */
    @Override
    public void updatePlayType(int type) {
        mPlayType = type;
        switch (type) {
            case SuperPlayerConst.PLAYTYPE_VOD:
                mTvBackToLive.setVisibility(View.GONE);
                mTvDuration.setVisibility(View.VISIBLE);
                break;
            case SuperPlayerConst.PLAYTYPE_LIVE:
                mTvBackToLive.setVisibility(View.GONE);
                mTvDuration.setVisibility(View.GONE);
                mSeekBarProgress.setProgress(100);
                break;
            case SuperPlayerConst.PLAYTYPE_LIVE_SHIFT:
                if (mLayoutBottom.getVisibility() == VISIBLE)
                    mTvBackToLive.setVisibility(View.VISIBLE);
                mTvDuration.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * ????????????
     *
     * @param bitmap ?????????
     */
    @Override
    public void setBackground(Bitmap bitmap) {

    }

    /**
     * ????????????
     */
    @Override
    public void showBackground() {

    }

    /**
     * ????????????
     */
    @Override
    public void hideBackground() {

    }

    /**
     * ????????????????????????
     *
     * @param videoQuality ??????
     */
    @Override
    public void updateVideoQuality(TCVideoQuality videoQuality) {
        if (videoQuality != null) {
            if (!TextUtils.isEmpty(videoQuality.name))
                if (videoQuality.name.equals("FLU")) {
                    videoQuality.index = 0;
                } else if (videoQuality.name.equals("SD")) {
                    videoQuality.index = 1;
                } else if (videoQuality.name.equals("HD")) {
                    videoQuality.index = 2;
                } else if (videoQuality.name.equals("FHD")) {
                    videoQuality.index = 3;
                } else if (videoQuality.name.equals("2K")) {
                    videoQuality.index = 4;
                } else if (videoQuality.name.equals("4K")) {
                    videoQuality.index = 5;
                }
            mVodQualityView.setDefaultSelectedQuality(videoQuality.index);
        }
    }

    /**
     * ?????????????????????
     *
     * @param info ???????????????
     */
    @Override
    public void updateImageSpriteInfo(TCPlayImageSpriteInfo info) {
        releaseTXImageSprite();
        // ????????????????????????????????????
        mGestureVideoProgressLayout.setProgressVisibility(info == null || info.imageUrls == null || info.imageUrls.size() == 0);
        if (mPlayType == SuperPlayerConst.PLAYTYPE_VOD) {
            mTXImageSprite = new TXImageSprite(getContext());
            if (info != null) {
                // ?????????ELK??????
                TCLogReport.getInstance().uploadLogs(TCLogReport.ELK_ACTION_IMAGE_SPRITE, 0, 0);
                mTXImageSprite.setVTTUrlAndImageUrls(info.webVttUrl, info.imageUrls);
                for (String imageUrl : info.imageUrls) {
                    LogUtils.e("?????????==" + imageUrl);
                }
                haveImgFrame = true;
                initImgFrames();
            } else {
                haveImgFrame = false;
                mTXImageSprite.setVTTUrlAndImageUrls(null, null);
            }
        }

    }

    private void releaseTXImageSprite() {

        if (mTXImageSprite != null) {
            mTXImageSprite.release();
            mTXImageSprite = null;
        }
    }

    /**
     * ?????????????????????
     *
     * @param list ?????????????????????
     */
    @Override
    public void updateKeyFrameDescInfo(List<TCPlayKeyFrameDescInfo> list) {
        mTXPlayKeyFrameDescInfoList = list;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (disableGestureScroll) return true;
        if (mGestureDetector != null)
            mGestureDetector.onTouchEvent(event);

        if (!mLockScreen) {
            if (event.getAction() == MotionEvent.ACTION_UP && mVideoGestureUtil != null && mVideoGestureUtil.isVideoProgressModel()) {
                int progress = mVideoGestureUtil.getVideoProgress();
                if (progress > mSeekBarProgress.getMax()) {
                    progress = mSeekBarProgress.getMax();
                }
                if (progress < 0) {
                    progress = 0;
                }
                mSeekBarProgress.setProgress(progress);

                int seekTime = 0;
                float percentage = progress * 1.0f / mSeekBarProgress.getMax();
                if (mPlayType == SuperPlayerConst.PLAYTYPE_LIVE || mPlayType == SuperPlayerConst.PLAYTYPE_LIVE_SHIFT) {
                    if (mLivePushDuration > SuperPlayerConst.MAX_SHIFT_TIME) {
                        seekTime = (int) (mLivePushDuration - SuperPlayerConst.MAX_SHIFT_TIME * (1 - percentage));
                    } else {
                        seekTime = (int) (mLivePushDuration * percentage);
                    }
                } else {
                    seekTime = (int) (percentage * mDuration);
                }
                if (isNeedBuy && seekTime >= 5 * 60) {
                    seekTime = 5 * 60;
                    mControllerCallback.onSeekTo(seekTime);
                    mControllerCallback.onPause();
                    setVisibility(GONE);
                    disableGestureScroll = true;
                }
                if (mControllerCallback != null) {
                    mControllerCallback.onSeekTo(seekTime);
                }
                mIsChangingSeekBarProgress = false;
            }
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            this.getHandler().removeCallbacks(mHideViewRunnable);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            this.getHandler().postDelayed(mHideViewRunnable, 7000);
        }
        return true;
    }

    /**
     * ????????????????????????
     */
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_pause) { //??????\????????????
            togglePlayState();

        } else if (i == R.id.layout_replay) { //????????????
            replay();

        } else if (i == R.id.tv_backToLive) { //??????????????????
            if (mControllerCallback != null) {
                mControllerCallback.onResumeLive();
            }
        } else if (i == R.id.large_tv_vtt_text) { //???????????????????????????
            seekToKeyFramePos();
        } else if (i == R.id.tv_anthology) {
            mVodQualityView.hide();
            tcVodSettingMoreView.hide();
            anthologyView.show();
        } else if (i == R.id.tv_quality) {
            anthologyView.hide();
            tcVodSettingMoreView.hide();
            mVodQualityView.show();
            showQualityView();
        } else if (i == R.id.tv_more) {
            anthologyView.hide();
            mVodQualityView.hide();
            tcVodSettingMoreView.show();
        } else if (i == R.id.tv_quality1) {
            mVodQualityView.show();
            showQualityView();
        }
    }

    /**
     * ????????????
     */
    private void toggleDanmu() {
        mDanmuOn = !mDanmuOn;
        if (mControllerCallback != null) {
            mControllerCallback.onDanmuToggle(mDanmuOn);
        }
    }

    /**
     * ????????????????????????
     */
    private void showMoreView() {
        hide();
    }

    private void showAnthologyView() {
        hide();
        anthologyView.setVisibility(View.VISIBLE);
    }

    /**
     * ????????????????????????
     */
    private void showQualityView() {
        if (mVideoQualityList == null || mVideoQualityList.size() == 0) {
            return;
        }
        if (mVideoQualityList.size() == 1 && (mVideoQualityList.get(0) == null || TextUtils.isEmpty(mVideoQualityList.get(0).title))) {
            return;
        }
        // ?????????????????????????????????
        mVodQualityView.setVisibility(View.VISIBLE);
        if (!mFirstShowQuality && mDefaultVideoQuality != null) {
            for (int i = 0; i < mVideoQualityList.size(); i++) {
                TCVideoQuality quality = mVideoQualityList.get(i);
                if (quality != null && quality.title != null && quality.index == mDefaultVideoQuality.index) {
                    mVodQualityView.setDefaultSelectedQuality(quality.index);
                    break;
                }
            }
            mFirstShowQuality = true;
        }
        mVodQualityView.setVideoQualityList(mVideoQualityList);
    }

    /**
     * ??????????????????
     */
    private void toggleLockState() {
        mLockScreen = !mLockScreen;
        if (mHideLockViewRunnable != null) {
            this.getHandler().removeCallbacks(mHideLockViewRunnable);
            this.getHandler().postDelayed(mHideLockViewRunnable, 7000);
        }

    }

    /**
     * ??????
     */
    private void replay() {
        toggleView(mLayoutReplay, false);
        if (mControllerCallback != null) {
            mControllerCallback.onResume();
        }
    }

    /**
     * ???????????????????????????
     */
    private void seekToKeyFramePos() {
        float time = mTXPlayKeyFrameDescInfoList != null ? mTXPlayKeyFrameDescInfoList.get(mSelectedPos).time : 0;
        if (mControllerCallback != null) {
            mControllerCallback.onSeekTo((int) time);
            mControllerCallback.onResume();
        }
        mTvVttText.setVisibility(GONE);
        toggleView(mLayoutReplay, false);
    }

    @Override
    public void onProgressChanged(TCPointSeekBar seekBar, int progress, boolean isFromUser) {
        if (mGestureVideoProgressLayout != null && isFromUser) {
            mGestureVideoProgressLayout.show();
            float percentage = ((float) progress) / seekBar.getMax();
            float currentTime = (mDuration * percentage);
            if (isNeedBuy && currentTime >= 60 * 5) {
                mGestureVideoProgressLayout.setProgress(progress);
                seekBar.setProgress(progress);
                seekBar.setEnabled(false);
                mControllerCallback.onSeekTo(5 * 60);
                mControllerCallback.onPause();
                setVisibility(GONE);
                disableGestureScroll = true;
                return;
            }
            if (mPlayType == SuperPlayerConst.PLAYTYPE_LIVE || mPlayType == SuperPlayerConst.PLAYTYPE_LIVE_SHIFT) {
                if (mLivePushDuration > SuperPlayerConst.MAX_SHIFT_TIME) {
                    currentTime = (int) (mLivePushDuration - SuperPlayerConst.MAX_SHIFT_TIME * (1 - percentage));
                } else {
                    currentTime = mLivePushDuration * percentage;
                }
                mGestureVideoProgressLayout.setTimeText(TCTimeUtil.formattedTime((long) currentTime));
            } else {
                mGestureVideoProgressLayout.setTimeText(TCTimeUtil.formattedTime((long) currentTime) + " / " + TCTimeUtil.formattedTime((long) mDuration));
            }
            mGestureVideoProgressLayout.setProgress(progress);
        }
        // ?????????????????????
        if (isFromUser && mPlayType == SuperPlayerConst.PLAYTYPE_VOD) {
            setThumbnail(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(TCPointSeekBar seekBar) {
        this.getHandler().removeCallbacks(mHideViewRunnable);
    }

    @Override
    public void onStopTrackingTouch(TCPointSeekBar seekBar) {
        int curProgress = seekBar.getProgress();
        int maxProgress = seekBar.getMax();

        switch (mPlayType) {
            case SuperPlayerConst.PLAYTYPE_VOD:
                if (curProgress >= 0 && curProgress <= maxProgress) {
                    // ??????????????????
                    toggleView(mLayoutReplay, false);
                    float percentage = ((float) curProgress) / maxProgress;
                    int position = (int) (mDuration * percentage);
                    if (mControllerCallback != null) {
                        mControllerCallback.onSeekTo(position);
                        mControllerCallback.onResume();
                    }
                }
                break;
            case SuperPlayerConst.PLAYTYPE_LIVE:
            case SuperPlayerConst.PLAYTYPE_LIVE_SHIFT:
                toggleView(mPbLiveLoading, true);
                int seekTime = (int) (mLivePushDuration * curProgress * 1.0f / maxProgress);
                if (mLivePushDuration > SuperPlayerConst.MAX_SHIFT_TIME) {
                    seekTime = (int) (mLivePushDuration - SuperPlayerConst.MAX_SHIFT_TIME * (maxProgress - curProgress) * 1.0f / maxProgress);
                }
                if (mControllerCallback != null) {
                    mControllerCallback.onSeekTo(seekTime);
                }
                break;
        }
        this.getHandler().postDelayed(mHideViewRunnable, 7000);
    }

    @Override
    public void onSeekBarPointClick(final View view, final int pos) {
        if (mHideLockViewRunnable != null) {
            this.getHandler().removeCallbacks(mHideViewRunnable);
            this.getHandler().postDelayed(mHideViewRunnable, 7000);
        }
        if (mTXPlayKeyFrameDescInfoList != null) {
            //ELK????????????
            TCLogReport.getInstance().uploadLogs(TCLogReport.ELK_ACTION_PLAYER_POINT, 0, 0);
            mSelectedPos = pos;
            view.post(new Runnable() {
                @Override
                public void run() {
                    int[] location = new int[2];
                    view.getLocationInWindow(location);

                    int viewX = location[0];
                    TCPlayKeyFrameDescInfo info = mTXPlayKeyFrameDescInfoList.get(pos);
                    String content = info.content;

                    mTvVttText.setText(TCTimeUtil.formattedTime((long) info.time) + " " + content);
                    mTvVttText.setVisibility(VISIBLE);
                    adjustVttTextViewPos(viewX);
                }
            });
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param progress ????????????
     */
    private void setThumbnail(int progress) {
        float percentage = ((float) progress) / mSeekBarProgress.getMax();
        float seekTime = (mDuration * percentage);
        if (mTXImageSprite != null) {
            Bitmap bitmap = mTXImageSprite.getThumbnail(seekTime);
            if (bitmap != null) {
                mGestureVideoProgressLayout.setThumbnail(bitmap);
            }
        }
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param viewX ???????????????view
     */
    private void adjustVttTextViewPos(final int viewX) {
        mTvVttText.post(new Runnable() {
            @Override
            public void run() {
                int width = mTvVttText.getWidth();

                int marginLeft = viewX - width / 2;

                LayoutParams params = (LayoutParams) mTvVttText.getLayoutParams();
                params.leftMargin = marginLeft;

                if (marginLeft < 0) {
                    params.leftMargin = 0;
                }

                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                if (marginLeft + width > screenWidth) {
                    params.leftMargin = screenWidth - width;
                }

                mTvVttText.setLayoutParams(params);
            }
        });
    }

    @Override
    public void onSpeedChange(float speedLevel) {
        if (mControllerCallback != null) {
            mControllerCallback.onSpeedChange(speedLevel);
        }
    }

    @Override
    public void onMirrorChange(boolean isMirror) {
        if (mControllerCallback != null) {
            mControllerCallback.onMirrorToggle(isMirror);
        }
    }

    @Override
    public void onHWAcceleration(boolean isAccelerate) {
        if (mControllerCallback != null) {
            mControllerCallback.onHWAccelerationToggle(isAccelerate);
        }
    }

    @Override
    public void fullScale(boolean fullScale) {
        mControllerCallback.onSwitchPlayScale(fullScale);
    }

    @Override
    public void jumpTitleTail(boolean jumpTitleTail) {

    }

    @Override
    public void onQualitySelect(TCVideoQuality quality) {
        if (mControllerCallback != null) {
            mControllerCallback.onQualityChange(quality);
        }
        mVodQualityView.setVisibility(View.GONE);
    }

    @Override
    public void onAnthologySelect(int position) {
        if (mControllerCallback != null) {
            mControllerCallback.onAnthologyChange(position);
        }
        anthologyView.setVisibility(View.GONE);
    }

    public void setIsNeedVip(boolean b) {
        anthologyView.setIsNeedVip(b);
    }


    private boolean isDefaultLanguage;

    public void isDefaultLanguage(boolean defaultLanguage) {
        isDefaultLanguage = defaultLanguage;
    }

    public void setVideoType(int videoType) {
        switch (videoType) {
            case SuperPlayerConst.PLAYTYPE_VOD:
                break;
            case SuperPlayerConst.PLAYTYPE_LIVE:
                mTvCurrent.setVisibility(INVISIBLE);
                mTvDuration.setVisibility(INVISIBLE);
                mSeekBarProgress.setVisibility(INVISIBLE);
                mTvQuality1.setVisibility(VISIBLE);
                mVideoType.setText(getContext().getString(R.string.living));
                break;
            case SuperPlayerConst.PLAYTYPE_LIVE_SHIFT:
                mVideoType.setText(getContext().getString(R.string.living_shift));
                break;
        }
    }

    private boolean isNeedBuy;
    private boolean disableGestureScroll;

    public void setNeedBuy(boolean visible) {
        isNeedBuy = visible;
    }

    public void setEnableScroll() {
        disableGestureScroll = false;
        mSeekBarProgress.setEnabled(true);
    }

    public void setDefaultQuality(int defaultQualityIndex) {
        mDefaultVideoQuality.index = defaultQualityIndex;
    }

    boolean isShowingMenu;

    public void showMenu() {
        isShowingMenu = true;
        if (!isShowing)
            show();
    }

    private void showProgress(int keyCode, long duration) {
        if (duration != 0) {
            int progress = mSeekBarProgress.getProgress();
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (progress > 0)
                    progress = progress - 1;
            } else {
                if (progress < mSeekBarProgress.getMax())
                    progress = progress + 1;
            }
            float percentage = progress * 1.0f / mSeekBarProgress.getMax();
            long l = (long) (percentage * duration);
            mControllerCallback.onSeekTo((int) l);
            mSeekBarProgress.setmIsOnDrag(false);
            mSeekBarProgress.setProgress(progress);
        }
    }

    public void showProgress(int keyCode, long current, long duration) {
        if (isShowingMenu) return;
        mIsChangingSeekBarProgress = true;
        if (!haveImgFrame) {//???????????????
            showProgress(keyCode, duration);
            if (!isShowing)
                show();
            if (mFullLayoutBottom.getVisibility() != GONE)
                mFullLayoutBottom.setVisibility(GONE);
            return;
        }
        if (imgFrames.getVisibility() != VISIBLE)
            imgFrames.setVisibility(VISIBLE);
        if (imgFrameAdapter.getData().isEmpty()) {
            for (int i = 0; i < duration; i += 10) {
                Bitmap thumbnail = mTXImageSprite.getThumbnail(i);
                imgFrameAdapter.getData().add(new ImgFrameBean(i, thumbnail));
                imgFrameAdapter.notifyItemInserted(imgFrameAdapter.getData().size() - 1);
            }

        }
        if (!isShowing) {
            show();
            int i = (int) (current / 10);
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) imgFrames.getLayoutManager();
            if (linearLayoutManager != null) {
                linearLayoutManager.scrollToPositionWithOffset(i, 0);
                imgFrames.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        View viewByPosition = imgFrameAdapter.getViewByPosition(imgFrames, i, R.id.item_img_frame);
                        if (viewByPosition != null) {
                            viewByPosition.requestFocus();
                            imgFrames.scrollBy(getScrollAmount(viewByPosition)[0], 0);
                        } else {
                            LogUtils.e("viewByPosition = null");
                        }
                    }
                }, 100);
            }

        }
        if (mFullLayoutBottom.getVisibility() != GONE)
            mFullLayoutBottom.setVisibility(GONE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            int childAdapterPosition = imgFrames.getChildAdapterPosition(v);
            ImgFrameBean item = imgFrameAdapter.getItem(childAdapterPosition);
            if (isNeedBuy && item != null && item.getTime() > 5 * 60) {
                hide();
                mControllerCallback.onSeekTo(5 * 60);
                mControllerCallback.onPause();
                disableGestureScroll = true;
                return;
            }
            imgFrames.scrollBy(getScrollAmount(v)[0], 0);
            if (item != null && mDuration != 0) {
                mSeekBarProgress.setProgress((int) (item.getTime() * 100 / mDuration));
            }
            if (mHideViewRunnable != null) {
                TCControllerFullScreen.this.getHandler().removeCallbacks(mHideViewRunnable);
                TCControllerFullScreen.this.getHandler().postDelayed(mHideViewRunnable, 3000);
            }
        }
    }

    /**
     * ???????????????????????????,?????????????????????????????????
     *
     * @param view
     */
    private int[] getScrollAmount(View view) {
        int[] out = new int[2];
        view.getLocationOnScreen(out);
        out[0] = out[0] - ScreenUtils.getScreenSize(getContext())[0] / 2;
        return out;
    }


    /**
     * ?????????????????????runnable
     */
    private static class HideLockViewRunnable implements Runnable {
        private WeakReference<TCControllerFullScreen> mWefControllerFullScreen;

        public HideLockViewRunnable(TCControllerFullScreen controller) {
            mWefControllerFullScreen = new WeakReference<>(controller);
        }

        @Override
        public void run() {

        }
    }

    public void setVideoAnthologyList(List<VideoBean> list) {
        tvAnthology.setVisibility(VISIBLE);
        anthologyView.setVideoAnthologyList(list);
    }

    /**
     * ?????????????????????
     *
     * @param position
     */
    public void setSelectedAnthology(int position) {
        anthologyView.setSelectedAnthology(position);
    }

    public void setAdImage(Drawable bitmap) {
        isSetPausedImg = true;
        pauseAdView.setAdImage(bitmap);
    }

    public PauseAdView getPauseAdView() {
        return pauseAdView;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            event = new KeyEvent(event.getDownTime(), event.getEventTime(), event.getAction(),
                    KeyEvent.KEYCODE_DPAD_CENTER, event.getMetaState(),
                    event.getDeviceId(), event.getScanCode(), event.getFlags(), event.getSource());
        }
        if (pauseAdView.getVisibility() == VISIBLE) {
            pauseAdView.hide();
        }
        return super.dispatchKeyEvent(event);
    }
}
