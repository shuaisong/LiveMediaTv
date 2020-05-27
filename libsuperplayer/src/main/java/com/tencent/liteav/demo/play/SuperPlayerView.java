package com.tencent.liteav.demo.play;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tencent.liteav.basic.log.TXCLog;
import com.tencent.liteav.demo.play.bean.TCResolutionName;
import com.tencent.liteav.demo.play.bean.VideoBean;
import com.tencent.liteav.demo.play.controller.IControllerCallback;
import com.tencent.liteav.demo.play.controller.TCControllerFloat;
import com.tencent.liteav.demo.play.controller.TCControllerFullScreen;
import com.tencent.liteav.demo.play.controller.TCControllerWindow;
import com.tencent.liteav.demo.play.net.TCLogReport;
import com.tencent.liteav.demo.play.protocol.IPlayInfoProtocol;
import com.tencent.liteav.demo.play.protocol.IPlayInfoRequestCallback;
import com.tencent.liteav.demo.play.protocol.TCPlayInfoParams;
import com.tencent.liteav.demo.play.protocol.TCPlayInfoProtocolV2;
import com.tencent.liteav.demo.play.protocol.TCPlayInfoProtocolV4;
import com.tencent.liteav.demo.play.utils.TCImageUtil;
import com.tencent.liteav.demo.play.utils.TCNetWatcher;
import com.tencent.liteav.demo.play.utils.TCUrlUtil;
import com.tencent.liteav.demo.play.utils.TCVideoQualityUtil;
import com.tencent.liteav.demo.play.view.AdView;
import com.tencent.liteav.demo.play.view.BuyAntholgyView;
import com.tencent.liteav.demo.play.view.TCDanmuView;
import com.tencent.liteav.demo.play.bean.TCVideoQuality;
import com.tencent.liteav.demo.play.view.TCVodAnthologyView;
import com.tencent.liteav.demo.play.view.TCVodQualityView;
import com.tencent.liteav.demo.play.view.VipTipView;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXBitrateItem;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by liyuejiao on 2018/7/3.
 * <p>
 * 超级播放器view
 * <p>
 * 具备播放器基本功能，此外还包括横竖屏切换、悬浮窗播放、画质切换、硬件加速、倍速播放、镜像播放、手势控制等功能，同时支持直播与点播
 * <p>
 * 使用方式极为简单，只需要在布局文件中引入并获取到该控件，通过{@link #playWithModel(SuperPlayerModel)}传入{@link SuperPlayerModel}即可实现视频播放
 * <p>
 * 1、播放视频{@link #playWithModel(SuperPlayerModel)}
 * <p>
 * 2、设置回调{@link #setPlayerViewCallback(OnSuperPlayerViewCallback)}
 * <p>
 * 3、点播相关：初始化播放器{@link #initVodPlayer(Context)}，播放事件监听{@link #onPlayEvent(TXVodPlayer, int, Bundle)}，
 * 网络事件监听{@link #onNetStatus(TXVodPlayer, Bundle)}
 * <p>
 * 4、直播相关：初始化播放器{@link #initLivePlayer(Context)}，播放事件监听{@link #onPlayEvent(int, Bundle)}，
 * 网络事件监听{@link #onNetStatus(Bundle)}
 * <p>
 * 5、controller回调实现{@link #mControllerCallback}
 * <p>
 * 5、退出播放释放内存{@link #resetPlayer()}
 */

public class SuperPlayerView extends RelativeLayout implements ITXVodPlayListener, ITXLivePlayListener {
    private static final String TAG = "SuperPlayerView";
    private boolean isDefaultLanguage;
    private List<VideoBean> videoBeans;
    private int currentPosition;//视频列表第几个
    private AdView adView;
    private VipTipView vipTipView;
    private int resId;
    private ViewGroup oldParent;
    private LinearLayout newParent;
    private BuyAntholgyView buyAntholgyView;

    public void setVideoType(int videoType) {
        mControllerFullScreen.setVideoType(videoType);
        mControllerWindow.setVideoType(videoType);
    }

    public void isDefaultLanguage(boolean defaultLanguage) {
        isDefaultLanguage = defaultLanguage;
        mControllerFullScreen.isDefaultLanguage(defaultLanguage);
        if (vipTipView != null) vipTipView.isDefaultLanguage(defaultLanguage);
    }

    public void setAnthologyData(List<VideoBean> videoBeans) {
        this.videoBeans = videoBeans;
        mControllerFullScreen.setVideoAnthologyList(videoBeans);
    }

    public void setIsNeedVip(boolean b) {
        mControllerFullScreen.setIsNeedVip(b);
    }


    public void requestFullMode() {
        mControllerCallback.onSwitchPlayMode(SuperPlayerConst.PLAYMODE_FULLSCREEN);
    }

    public int getPosition() {
        return currentPosition;
    }


    public void disableControl() {
        mControllerWindow.setVisibility(GONE);
        mControllerFullScreen.setVisibility(GONE);
    }

    private boolean showAd = false;

    public void setShowAd(boolean b) {
        showAd = b;
        if (showAd) {
            mControllerWindow.setVisibility(GONE);
            mControllerFullScreen.setVisibility(GONE);
            adView.setVisibility(VISIBLE);
        } else {
            mControllerWindow.setVisibility(VISIBLE);
            mControllerFullScreen.setVisibility(VISIBLE);
            adView.setVisibility(GONE);
        }
    }

    private String AdFileID;
    private String v_fileid;
    private String title;
    private int playid;

    public void setInfo(int playid, String va_fileid, String v_fileid, String title) {
        AdFileID = va_fileid;
        this.v_fileid = v_fileid;
        this.playid = playid;
        SuperPlayerModel superPlayerModel = new SuperPlayerModel();
        superPlayerModel.appId = playid;
        superPlayerModel.videoId = new SuperPlayerVideoId();
        superPlayerModel.videoId.fileId = va_fileid;
        superPlayerModel.title = title;
        this.title = title;
        playWithModel(superPlayerModel);
    }

    public void setAdFreeClick(OnClickListener onClickListener) {
        adView.setAdClickListener(onClickListener);
    }

    public void setBuyClickListener(OnClickListener onClickListener) {
        buyAntholgyView.setBuyClickListener(onClickListener);
    }

    public void setLoginClick(OnClickListener onClickListener) {
        vipTipView.setLoginClickListener(onClickListener);
    }

    public void setBuyViewVisible(boolean visible) {
        mControllerWindow.setNeedBuy(visible);
        mControllerFullScreen.setNeedBuy(visible);
        buyAntholgyView.setVisibility(visible ? VISIBLE : GONE);
        if (!visible) {
            mControllerWindow.setVisibility(VISIBLE);
            mControllerFullScreen.setVisibility(VISIBLE);
            mControllerFullScreen.setEnableScroll();
            mControllerWindow.setEnableScroll();
        }
    }

    public void showLoginTip(boolean showTip) {
        if (showTip) {
            vipTipView.show();
            mControllerFullScreen.setVisibility(GONE);
            mControllerWindow.setVisibility(GONE);
        } else {
            vipTipView.hide();
            mControllerFullScreen.setVisibility(VISIBLE);
            mControllerWindow.setVisibility(VISIBLE);
        }
    }

    private TCVodAnthologyView.Callback callback;

    public void setOnAnthologySelect(TCVodAnthologyView.Callback callback) {
        this.callback = callback;
    }

    public void setVideoQualityCallback(TCVodQualityView.VideoQualityCallback videoQualityCallback) {
        mControllerFullScreen.setVideoQualityCallback(videoQualityCallback);
    }

    public void showMenu() {
        if (!mControllerFullScreen.isShowing())
            mControllerFullScreen.show();
    }

    public View getBuyAntholgyView() {
        return buyAntholgyView;
    }

    private int defaultQualityIndex;

    public void setDefaultQualitySet(int defaultQualityIndex) {
        mDefaultQualitySet = true;
        this.defaultQualityIndex = defaultQualityIndex;
        mControllerFullScreen.setDefaultQuality(defaultQualityIndex);
    }

    private int vmStartTime;//片头时间
    private int vmEndTime;//片尾时间
    private boolean isJump;//是否跳过片头片尾

    public void setStartAndEndTime(boolean isJump, int vm_opening_time, int vm_ending_time) {
        vmStartTime = vm_opening_time;
        vmEndTime = vm_ending_time;
        this.isJump = isJump;
    }

    public void disableBottom() {
        mControllerWindow.disableBottom();
    }

    private int startProgress = 0;

    public void setCurrent(int progress) {
        startProgress = progress;
    }

    private enum PLAYER_TYPE {
        PLAYER_TYPE_NULL,
        PLAYER_TYPE_VOD,
        PLAYER_TYPE_LIVE;
    }

    private Context mContext;
    // UI
    private ViewGroup mRootView;                      // SuperPlayerView的根view
    private TXCloudVideoView mTXCloudVideoView;              // 腾讯云视频播放view
    private TCControllerFullScreen mControllerFullScreen;          // 全屏模式控制view
    private TCControllerWindow mControllerWindow;              // 窗口模式控制view
    private TCControllerFloat mControllerFloat;               // 悬浮窗模式控制view
    private TCDanmuView mDanmuView;                     // 弹幕
    private ViewGroup.LayoutParams mLayoutParamWindowMode;         // 窗口播放时SuperPlayerView的布局参数
    private ViewGroup.LayoutParams mLayoutParamFullScreenMode;     // 全屏播放时SuperPlayerView的布局参数
    private LayoutParams mVodControllerWindowParams;     // 窗口controller的布局参数
    private LayoutParams mVodControllerFullScreenParams; // 全屏controller的布局参数

    private WindowManager mWindowManager;                 // 悬浮窗窗口管理器
    private WindowManager.LayoutParams mWindowParams;                  // 悬浮窗布局参数

    private SuperPlayerModel mCurrentModel;                  // 当前播放的model
    private IPlayInfoProtocol mCurrentProtocol;               // 当前视频信息协议类

    private TXVodPlayer mVodPlayer;                     // 点播播放器
    private TXVodPlayConfig mVodPlayConfig;                 // 点播播放器配置
    private TXLivePlayer mLivePlayer;                    // 直播播放器
    private TXLivePlayConfig mLivePlayConfig;                // 直播播放器配置

    private OnSuperPlayerViewCallback mPlayerViewCallback;            // SuperPlayerView回调
    private TCNetWatcher mWatcher;                       // 网络质量监视器
    private String mCurrentPlayVideoURL;           // 当前播放的url
    private int mCurrentPlayType;               // 当前播放类型
    private int mCurrentPlayMode = SuperPlayerConst.PLAYMODE_WINDOW;    // 当前播放模式
    private int mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PLAYING; // 当前播放状态
    private boolean mIsMultiBitrateStream;          // 是否是多码流url播放
    private boolean mIsPlayWithFileId;              // 是否是腾讯云fileId播放
    private long mReportLiveStartTime = -1;      // 直播开始时间，用于上报使用时长
    private long mReportVodStartTime = -1;       // 点播开始时间，用于上报使用时长
    private boolean mDefaultQualitySet;             // 标记播放多码流url时是否设置过默认画质
    private boolean mLockScreen = false;            // 是否锁定屏幕
    private boolean mChangeHWAcceleration;          // 切换硬解后接收到第一个关键帧前的标记位
    private int mSeekPos;                       // 记录切换硬解时的播放时间
    private long mMaxLiveProgressTime;           // 观看直播的最大时长
    private PLAYER_TYPE mCurPlayType = PLAYER_TYPE.PLAYER_TYPE_NULL;    //当前播放类型

    private final int OP_SYSTEM_ALERT_WINDOW = 24;    // 支持TYPE_TOAST悬浮窗的最高API版本

    public SuperPlayerView(Context context) {
        super(context);
        initView(context);
    }

    public SuperPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SuperPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化view
     *
     * @param context
     */
    private void initView(Context context) {
        mContext = context;
        mRootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.super_vod_player_view, null);
        mTXCloudVideoView = (TXCloudVideoView) mRootView.findViewById(R.id.cloud_video_view);
        adView = (AdView) mRootView.findViewById(R.id.adView);
        vipTipView = (VipTipView) mRootView.findViewById(R.id.vipTipView);
        buyAntholgyView = (BuyAntholgyView) mRootView.findViewById(R.id.buyAntholgyView);
        mControllerFullScreen = (TCControllerFullScreen) mRootView.findViewById(R.id.controller_large);
        mControllerWindow = (TCControllerWindow) mRootView.findViewById(R.id.controller_small);
        mControllerFloat = (TCControllerFloat) mRootView.findViewById(R.id.controller_float);
        mDanmuView = (TCDanmuView) mRootView.findViewById(R.id.danmaku_view);

        mVodControllerWindowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mVodControllerFullScreenParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        mControllerFullScreen.setCallback(mControllerCallback);
        mControllerWindow.setCallback(mControllerCallback);
        mControllerFloat.setCallback(mControllerCallback);
        adView.setCallback(mControllerCallback);
        removeAllViews();
        mRootView.removeView(mDanmuView);
        mRootView.removeView(mTXCloudVideoView);
        mRootView.removeView(mControllerWindow);
        mRootView.removeView(mControllerFullScreen);
        mRootView.removeView(mControllerFloat);

        addView(mTXCloudVideoView);
        mRootView.removeView(vipTipView);
        addView(vipTipView);
        mRootView.removeView(adView);
        addView(adView);

        if (mCurrentPlayMode == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
            addView(mControllerFullScreen);
            mControllerFullScreen.hide();
        } else if (mCurrentPlayMode == SuperPlayerConst.PLAYMODE_WINDOW) {
            addView(mControllerWindow);
            mControllerWindow.hide();
        }
//        addView(mDanmuView);
        mRootView.removeView(buyAntholgyView);
        addView(buyAntholgyView);
        buyAntholgyView.setVisibility(GONE);
        post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentPlayMode == SuperPlayerConst.PLAYMODE_WINDOW) {
                    mLayoutParamWindowMode = getLayoutParams();
                }
                try {
                    // 依据上层Parent的LayoutParam类型来实例化一个新的fullscreen模式下的LayoutParam
                    Class parentLayoutParamClazz = getLayoutParams().getClass();
                    Constructor constructor = parentLayoutParamClazz.getDeclaredConstructor(int.class, int.class);
                    mLayoutParamFullScreenMode = (ViewGroup.LayoutParams) constructor.newInstance(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        TCLogReport.getInstance().setAppName(context);
        TCLogReport.getInstance().setPackageName(context);
    }

    /**
     * 初始化点播播放器
     *
     * @param context
     */
    private void initVodPlayer(Context context) {
        if (mVodPlayer != null)
            return;
        mVodPlayer = new TXVodPlayer(context);
        SuperPlayerGlobalConfig config = SuperPlayerGlobalConfig.getInstance();
        mVodPlayConfig = new TXVodPlayConfig();

        File sdcardDir = context.getExternalFilesDir(null);
        if (sdcardDir != null) {
            mVodPlayConfig.setCacheFolderPath(sdcardDir.getPath() + "/txcache");
        }
        mVodPlayConfig.setMaxCacheItems(config.maxCacheItem);
        mVodPlayer.setConfig(mVodPlayConfig);
        mVodPlayer.setRenderMode(config.renderMode);
        mVodPlayer.setVodListener(this);
        if (mDefaultQualitySet) {
            mVodPlayer.setBitrateIndex(defaultQualityIndex);
        }
        mVodPlayer.enableHardwareDecode(config.enableHWAcceleration);
    }


    public void setBackground(Drawable drawable) {
        mControllerWindow.setBackground(drawable);
    }

    /**
     * 初始化直播播放器
     *
     * @param context
     */
    private void initLivePlayer(Context context) {
        if (mLivePlayer != null)
            return;
        mLivePlayer = new TXLivePlayer(context);
        SuperPlayerGlobalConfig config = SuperPlayerGlobalConfig.getInstance();
        mLivePlayConfig = new TXLivePlayConfig();
        mLivePlayer.setConfig(mLivePlayConfig);
        mLivePlayer.setRenderMode(config.renderMode);
        mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mLivePlayer.setPlayListener(this);
        mLivePlayer.enableHardwareDecode(config.enableHWAcceleration);
    }

    public void setWatermark(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        mControllerWindow.setWatermark(bitmap, 0.9f, 0.1f);
    }

    /**
     * 播放视频
     *
     * @param model
     */
    public void playWithModel(final SuperPlayerModel model) {
        mCurrentModel = model;
        if (videoBeans != null) {
            for (int i = 0; i < videoBeans.size(); i++) {
                if (videoBeans.get(i).getV_fileid().equals(model.videoId.fileId)) {
                    mControllerFullScreen.setSelectedAnthology(i);
                    currentPosition = i;
                }
            }
        }
        stopPlay();
        initLivePlayer(getContext());
        initVodPlayer(getContext());
        // 清空关键帧和视频打点信息
        mControllerFullScreen.updateImageSpriteInfo(null);
        mControllerFullScreen.updateKeyFrameDescInfo(null);
        TCPlayInfoParams params = new TCPlayInfoParams();
        params.appId = model.appId;
        if (model.videoId != null) {
            params.fileId = model.videoId.fileId;
            params.videoId = model.videoId;
            mCurrentProtocol = new TCPlayInfoProtocolV4(params);
        } else if (model.videoIdV2 != null) {
            params.fileId = model.videoIdV2.fileId;
            params.videoIdV2 = model.videoIdV2;
            mCurrentProtocol = new TCPlayInfoProtocolV2(params);
        }
        if (model.videoId != null || model.videoIdV2 != null) { // 根据FileId播放
            mCurrentProtocol.sendRequest(new IPlayInfoRequestCallback() {
                @Override
                public void onSuccess(IPlayInfoProtocol protocol, TCPlayInfoParams param) {
                    TXCLog.i(TAG, "onSuccess: protocol params = " + param.toString());
                    mReportVodStartTime = System.currentTimeMillis();
                    mVodPlayer.setPlayerView(mTXCloudVideoView);
                    playModeVideo(mCurrentProtocol);
                    updatePlayType(SuperPlayerConst.PLAYTYPE_VOD);
                    String title = !TextUtils.isEmpty(model.title) ? model.title :
                            (mCurrentProtocol.getName() != null && !TextUtils.isEmpty(mCurrentProtocol.getName())) ? mCurrentProtocol.getName() : "";
                    updateTitle(title);
                    updateVideoProgress(0, 0);
                    mControllerFullScreen.updateImageSpriteInfo(mCurrentProtocol.getImageSpriteInfo());
                    mControllerFullScreen.updateKeyFrameDescInfo(mCurrentProtocol.getKeyFrameDescInfo());
                }

                @Override
                public void onError(int errCode, String message) {
                    TXCLog.i(TAG, "onFail: errorCode = " + errCode + " message = " + message);
                    Toast.makeText(SuperPlayerView.this.getContext(), "播放视频文件失败 code = " + errCode + " msg = " + message, Toast.LENGTH_SHORT).show();
                }
            });
        } else { // 根据URL播放
            String videoURL = null;
            List<TCVideoQuality> videoQualities = new ArrayList<>();
            TCVideoQuality defaultVideoQuality = null;
            if (model.multiURLs != null && !model.multiURLs.isEmpty()) {// 多码率URL播放
                int i = 0;
                for (SuperPlayerModel.SuperPlayerURL superPlayerURL : model.multiURLs) {
                    if (i == model.playDefaultIndex) {
                        videoURL = superPlayerURL.url;
                    }
                    videoQualities.add(new TCVideoQuality(i++, superPlayerURL.qualityName, superPlayerURL.url));
                }
                defaultVideoQuality = videoQualities.get(model.playDefaultIndex);
            } else if (!TextUtils.isEmpty(model.url)) { // 传统URL模式播放
//                videoQualities.add(new TCVideoQuality(0, "", model.url));
//                defaultVideoQuality = videoQualities.get(0);
                videoURL = model.url;
            }

            if (TextUtils.isEmpty(videoURL)) {
                Toast.makeText(this.getContext(), "播放视频失败，播放连接为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TCUrlUtil.isRTMPPlay(videoURL)) { // 直播播放器：普通RTMP流播放
                mReportLiveStartTime = System.currentTimeMillis();
                mLivePlayer.setPlayerView(mTXCloudVideoView);
                playLiveURL(videoURL, TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
            } else if (TCUrlUtil.isFLVPlay(videoURL)) { // 直播播放器：直播FLV流播放
                mReportLiveStartTime = System.currentTimeMillis();
                mLivePlayer.setPlayerView(mTXCloudVideoView);
                playTimeShiftLiveURL(model);
                if (model.multiURLs != null && !model.multiURLs.isEmpty()) {
                    startMultiStreamLiveURL(videoURL);
                }
            } else { // 点播播放器：播放点播文件
                mReportVodStartTime = System.currentTimeMillis();
                mVodPlayer.setPlayerView(mTXCloudVideoView);
                playVodURL(videoURL);
            }
            boolean isLivePlay = (TCUrlUtil.isRTMPPlay(videoURL) || TCUrlUtil.isFLVPlay(videoURL));
            updatePlayType(isLivePlay ? SuperPlayerConst.PLAYTYPE_LIVE : SuperPlayerConst.PLAYTYPE_VOD);
            updateTitle(model.title);
            updateVideoProgress(0, 0);
            mControllerFullScreen.setVideoQualityList(videoQualities);
            mControllerFullScreen.updateVideoQuality(defaultVideoQuality);
        }
    }

    /**
     * 播放FileId视频
     *
     * @param protocol
     */
    private void playModeVideo(IPlayInfoProtocol protocol) {
        playVodURL(protocol.getUrl());
        List<TCVideoQuality> videoQualityArrayList = protocol.getVideoQualityList();
        if (videoQualityArrayList != null) {
            mControllerFullScreen.setVideoQualityList(videoQualityArrayList);
            mIsMultiBitrateStream = false;
        } else {
            mIsMultiBitrateStream = true;
        }
        TCVideoQuality defaultVideoQuality = protocol.getDefaultVideoQuality();
        if (defaultVideoQuality != null)
            mControllerFullScreen.updateVideoQuality(defaultVideoQuality);
    }

    /**
     * 播放直播URL
     */
    private void playLiveURL(String url, int playType) {
        mCurrentPlayVideoURL = url;
        if (mLivePlayer != null) {
            mLivePlayer.setPlayListener(this);
            int result = mLivePlayer.startPlay(url, playType); // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
            if (result != 0) {
                TXCLog.e(TAG, "playLiveURL videoURL:" + url + ",result:" + result);
            } else {
                mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PLAYING;
                mCurPlayType = PLAYER_TYPE.PLAYER_TYPE_LIVE;
                TXCLog.e(TAG, "playLiveURL mCurrentPlayState:" + mCurrentPlayState);
            }
        }
    }

    /**
     * 播放点播url
     */
    private void playVodURL(String url) {
        if (url == null || "".equals(url)) return;
        mCurrentPlayVideoURL = url;
        if (url.contains(".m3u8")) {
            mIsMultiBitrateStream = true;
        }
        if (mVodPlayer != null) {
            mDefaultQualitySet = false;
            mVodPlayer.setStartTime(0);
            mVodPlayer.setAutoPlay(true);
            mVodPlayer.setVodListener(this);
            int ret = mVodPlayer.startPlay(url);
            if (ret == 0) {
                mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PLAYING;
                mCurPlayType = PLAYER_TYPE.PLAYER_TYPE_VOD;
                if (mDanmuView != null && mDanmuView.isPrepared() && mDanmuView.isPaused()) {
                    mDanmuView.resume();
                }
                TXCLog.e(TAG, "playVodURL mCurrentPlayState:" + mCurrentPlayState);
            }
        }
        mIsPlayWithFileId = false;
    }

    /**
     * 播放时移直播url
     */
    private void playTimeShiftLiveURL(final SuperPlayerModel model) {
        final String liveURL = model.url;
        final String bizid = liveURL.substring(liveURL.indexOf("//") + 2, liveURL.indexOf("."));
        final String domian = SuperPlayerGlobalConfig.getInstance().playShiftDomain;
        final String streamid = liveURL.substring(liveURL.lastIndexOf("/") + 1, liveURL.lastIndexOf("."));
        final int appid = model.appId;
        TXCLog.i(TAG, "bizid:" + bizid + ",streamid:" + streamid + ",appid:" + appid);
        playLiveURL(liveURL, TXLivePlayer.PLAY_TYPE_LIVE_FLV);
        try {
            int bizidNum = Integer.valueOf(bizid);
            mLivePlayer.prepareLiveSeek(domian, bizidNum);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            TXCLog.e(TAG, "playTimeShiftLiveURL: bizidNum 错误 = %s " + bizid);
        }
    }

    /**
     * 配置多码流url
     *
     * @param url
     */
    private void startMultiStreamLiveURL(String url) {
        mLivePlayConfig.setAutoAdjustCacheTime(false);
        mLivePlayConfig.setMaxAutoAdjustCacheTime(5);
        mLivePlayConfig.setMinAutoAdjustCacheTime(5);
        mLivePlayer.setConfig(mLivePlayConfig);
        if (mWatcher == null) mWatcher = new TCNetWatcher(mContext);
        mWatcher.start(url, mLivePlayer);
    }

    /**
     * 更新标题
     *
     * @param title 视频名称
     */
    private void updateTitle(String title) {
        mControllerWindow.updateTitle(title);
        mControllerFullScreen.updateTitle(title);
    }

    private long current;
    private long duration;

    public long getDuration() {
        return duration;
    }

    public int getProgress() {
        if (duration == 0) return 0;
        return (int) (current * 100 / duration);
    }

    /**
     * 更新播放进度
     *
     * @param current  当前播放进度(秒)
     * @param duration 总时长(秒)
     */
    private void updateVideoProgress(long current, long duration) {
        this.current = current;
        this.duration = duration;
        mControllerWindow.updateVideoProgress(current, duration);
        mControllerFullScreen.updateVideoProgress(current, duration);
        if (adView.getVisibility() == VISIBLE) {
            adView.setDuration((int) (duration - current));
        }
        if (balanceTimeCallBack != null) {
            balanceTimeCallBack.showBalance((int) (duration - current));
        }
        if (buyAntholgyView.getVisibility() == VISIBLE) {
            if (current >= 5 * 60) {
                buyAntholgyView.showFullVisible();
                mControllerFullScreen.setVisibility(GONE);
                mControllerWindow.setVisibility(GONE);
                onPause();
            }
        }
        if (isJump && vmEndTime != 0 && adView.getVisibility() == GONE && buyAntholgyView.getVisibility() == GONE && vipTipView.getVisibility() == GONE) {
            if (current >= vmEndTime) {
                Bundle bundle = new Bundle();
                bundle.putString(TXLiveConstants.EVT_DESCRIPTION, "isJump");
                onPlayEvent(mVodPlayer, TXLiveConstants.PLAY_EVT_PLAY_END, bundle);
            }
        }
    }

    public void setBalanceTimeCallBack(BalanceTimeCallBack balanceTimeCallBack) {
        this.balanceTimeCallBack = balanceTimeCallBack;
    }

    BalanceTimeCallBack balanceTimeCallBack;

    public interface BalanceTimeCallBack {
        void showBalance(int balance);
    }

    /**
     * 更新播放类型
     *
     * @param playType
     */
    private void updatePlayType(int playType) {
        mCurrentPlayType = playType;
        mControllerWindow.updatePlayType(playType);
        mControllerFullScreen.updatePlayType(playType);
    }

    /**
     * 更新播放状态
     *
     * @param playState
     */
    private void updatePlayState(int playState) {
        mCurrentPlayState = playState;
        mControllerWindow.updatePlayState(playState);
        mControllerFullScreen.updatePlayState(playState);
    }

    /**
     * resume生命周期回调
     */
    public void onResume() {
        if (mDanmuView != null && mDanmuView.isPrepared() && mDanmuView.isPaused()) {
            mDanmuView.resume();
        }
        resume();
    }

    private void resume() {
        if (mVodPlayer != null) {
            mVodPlayer.resume();
            mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PLAYING;
            updatePlayState(mCurrentPlayState);
        }
    }

    /**
     * pause生命周期回调
     */
    public void onPause() {
        if (mDanmuView != null && mDanmuView.isPrepared()) {
            mDanmuView.pause();
        }
        pause();
    }


    private void pause() {
        if (mVodPlayer != null) {
            mVodPlayer.pause();
            mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PAUSE;
            updatePlayState(mCurrentPlayState);
        }

    }

    /**
     * 重置播放器
     */
    public void resetPlayer() {
        if (mDanmuView != null) {
            mDanmuView.release();
            mDanmuView = null;
        }
        stopPlay();
    }

    /**
     * 停止播放
     */
    private void stopPlay() {
        if (mVodPlayer != null) {
            mVodPlayer.setVodListener(null);
            mVodPlayer.stopPlay(false);
        }
        if (mLivePlayer != null) {
            mLivePlayer.setPlayListener(null);
            mLivePlayer.stopPlay(false);
            mTXCloudVideoView.removeVideoView();
        }
        if (mWatcher != null) {
            mWatcher.stop();
        }
        mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PAUSE;
        TXCLog.e(TAG, "stopPlay mCurrentPlayState:" + mCurrentPlayState);
        reportPlayTime();
    }

    /**
     * 上报播放时长
     */
    private void reportPlayTime() {
        if (mReportLiveStartTime != -1) {
            long reportEndTime = System.currentTimeMillis();
            long diff = (reportEndTime - mReportLiveStartTime) / 1000;
            TCLogReport.getInstance().uploadLogs(TCLogReport.ELK_ACTION_LIVE_TIME, diff, 0);
            mReportLiveStartTime = -1;
        }
        if (mReportVodStartTime != -1) {
            long reportEndTime = System.currentTimeMillis();
            long diff = (reportEndTime - mReportVodStartTime) / 1000;
            TCLogReport.getInstance().uploadLogs(TCLogReport.ELK_ACTION_VOD_TIME, diff, mIsPlayWithFileId ? 1 : 0);
            mReportVodStartTime = -1;
        }
    }

    /**
     * 设置超级播放器的回掉
     *
     * @param callback
     */
    public void setPlayerViewCallback(OnSuperPlayerViewCallback callback) {
        mPlayerViewCallback = callback;
    }

    /**
     * 控制是否全屏显示
     */
    private void fullScreen(boolean isFull) {
        Activity activity = (Activity) getContext();
        ViewGroup viewGroup = activity.findViewById(resId);
        if (isFull) {
            oldParent.removeView(this);
            newParent.addView(this);
            viewGroup.addView(newParent, 0);
            for (int i = 1; i < viewGroup.getChildCount(); i++) {
                viewGroup.getChildAt(i).setVisibility(GONE);
            }
        } else {
            viewGroup.removeView(newParent);
            newParent.removeAllViews();
            oldParent.addView(this, 0);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                viewGroup.getChildAt(i).setVisibility(VISIBLE);
            }
        }
    }

    public void setRootId(int rootId) {
        resId = rootId;
        oldParent = (ViewGroup) getParent();
        newParent = new LinearLayout(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        newParent.setLayoutParams(layoutParams);
    }


    /**
     * 初始化controller回调
     */
    private IControllerCallback mControllerCallback = new IControllerCallback() {
        @Override
        public void onSwitchPlayMode(int requestPlayMode) {
            if (mCurrentPlayMode == requestPlayMode) return;
            if (mLockScreen) return;
            if (requestPlayMode == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
                fullScreen(true);
            } else {
                fullScreen(false);
            }
            mControllerFullScreen.hide();
            mControllerWindow.hide();
            mControllerFloat.hide();
            //请求全屏模式
            if (requestPlayMode == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
                if (mLayoutParamFullScreenMode == null)
                    return;
                removeView(mControllerWindow);
                addView(mControllerFullScreen, mVodControllerFullScreenParams);
                setLayoutParams(mLayoutParamFullScreenMode);
                if (buyAntholgyView.isFullVisible())
                    mControllerFullScreen.setVisibility(GONE);
//                rotateScreenOrientation(SuperPlayerConst.ORIENTATION_LANDSCAPE);
                if (mPlayerViewCallback != null) {
                    mPlayerViewCallback.onStartFullScreenPlay();
                }
            }
            // 请求窗口模式
            else if (requestPlayMode == SuperPlayerConst.PLAYMODE_WINDOW) {
                // 当前是悬浮窗
                if (mCurrentPlayMode == SuperPlayerConst.PLAYMODE_FLOAT) {
                    try {
                        Context viewContext = SuperPlayerView.this.getContext();
                        Intent intent = null;
                        if (viewContext instanceof Activity) {
                            intent = new Intent(SuperPlayerView.this.getContext(), viewContext.getClass());
                        } else {
                            Toast.makeText(viewContext, "悬浮播放失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mContext.startActivity(intent);
                        pause();
                        if (mLayoutParamWindowMode == null)
                            return;
                        mWindowManager.removeView(mControllerFloat);

                        if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_VOD) {
                            mVodPlayer.setPlayerView(mTXCloudVideoView);
                        } else {
                            mLivePlayer.setPlayerView(mTXCloudVideoView);
                        }
                        resume();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // 当前是全屏模式
                else if (mCurrentPlayMode == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
                    if (mLayoutParamWindowMode == null)
                        return;
                    removeView(mControllerFullScreen);
                    addView(mControllerWindow, mVodControllerWindowParams);
                    setLayoutParams(mLayoutParamWindowMode);
                    if (buyAntholgyView.isFullVisible())
                        mControllerWindow.setVisibility(GONE);
//                    rotateScreenOrientation(SuperPlayerConst.ORIENTATION_PORTRAIT);
                    if (mPlayerViewCallback != null) {
                        mPlayerViewCallback.onStopFullScreenPlay();
                    }
                }
            }
            //请求悬浮窗模式
            else if (requestPlayMode == SuperPlayerConst.PLAYMODE_FLOAT) {
                TXCLog.i(TAG, "requestPlayMode Float :" + Build.MANUFACTURER);
                SuperPlayerGlobalConfig prefs = SuperPlayerGlobalConfig.getInstance();
                if (!prefs.enableFloatWindow) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 6.0动态申请悬浮窗权限
                    if (!Settings.canDrawOverlays(mContext)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                        mContext.startActivity(intent);
                        return;
                    }
                } else {
                    if (!checkOp(mContext, OP_SYSTEM_ALERT_WINDOW)) {
                        Toast.makeText(mContext, "进入设置页面失败,请手动开启悬浮窗权限", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                pause();

                mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                mWindowParams = new WindowManager.LayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    mWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                }
                mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                mWindowParams.format = PixelFormat.TRANSLUCENT;
                mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;

                SuperPlayerGlobalConfig.TXRect rect = prefs.floatViewRect;
                mWindowParams.x = rect.x;
                mWindowParams.y = rect.y;
                mWindowParams.width = rect.width;
                mWindowParams.height = rect.height;
                try {
                    mWindowManager.addView(mControllerFloat, mWindowParams);
                } catch (Exception e) {
                    Toast.makeText(SuperPlayerView.this.getContext(), "悬浮播放失败", Toast.LENGTH_SHORT).show();
                    return;
                }

                TXCloudVideoView videoView = mControllerFloat.getFloatVideoView();
                if (videoView != null) {
                    if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_VOD) {
                        mVodPlayer.setPlayerView(videoView);
                    } else {
                        mLivePlayer.setPlayerView(videoView);
                    }
                    resume();
                }
                // 悬浮窗上报
                TCLogReport.getInstance().uploadLogs(TCLogReport.ELK_ACTION_FLOATMOE, 0, 0);
            }
            mCurrentPlayMode = requestPlayMode;
        }

        @Override
        public void onBackPressed(int playMode) {
            switch (playMode) {
                case SuperPlayerConst.PLAYMODE_FULLSCREEN:// 当前是全屏模式，返回切换成窗口模式
                    onSwitchPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
                    break;
                case SuperPlayerConst.PLAYMODE_WINDOW:// 当前是窗口模式，返回退出播放器
                    Context context = getContext();
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                    /*if (mPlayerViewCallback != null) {
                        mPlayerViewCallback.onClickSmallReturnBtn();
                    }
                    if (mCurrentPlayState == SuperPlayerConst.PLAYSTATE_PLAYING) {
                        onSwitchPlayMode(SuperPlayerConst.PLAYMODE_FLOAT);
                    }*/
                    break;
                case SuperPlayerConst.PLAYMODE_FLOAT:// 当前是悬浮窗，退出
                    mWindowManager.removeView(mControllerFloat);
                    if (mPlayerViewCallback != null) {
                        mPlayerViewCallback.onClickFloatCloseBtn();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFloatPositionChange(int x, int y) {
            mWindowParams.x = x;
            mWindowParams.y = y;
            mWindowManager.updateViewLayout(mControllerFloat, mWindowParams);
        }

        @Override
        public void onPause() {
            if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_VOD) {
                if (mVodPlayer != null) {
                    mVodPlayer.pause();
                }
            } else {
                if (mLivePlayer != null) {
                    mLivePlayer.pause();
                }
                if (mWatcher != null) {
                    mWatcher.stop();
                }
            }
            updatePlayState(SuperPlayerConst.PLAYSTATE_PAUSE);
        }

        @Override
        public void onResume() {
            if (mCurrentPlayState == SuperPlayerConst.PLAYSTATE_END) { //重播
                if (mCurPlayType == PLAYER_TYPE.PLAYER_TYPE_LIVE) {
                    if (TCUrlUtil.isRTMPPlay(mCurrentPlayVideoURL)) {
                        playLiveURL(mCurrentPlayVideoURL, TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
                    } else if (TCUrlUtil.isFLVPlay(mCurrentPlayVideoURL)) {
                        playTimeShiftLiveURL(mCurrentModel);
                        if (mCurrentModel.multiURLs != null && !mCurrentModel.multiURLs.isEmpty()) {
                            startMultiStreamLiveURL(mCurrentPlayVideoURL);
                        }
                    }
                } else {
                    playVodURL(mCurrentPlayVideoURL);
                }
            } else if (mCurrentPlayState == SuperPlayerConst.PLAYSTATE_PAUSE) { //继续播放
                if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_VOD) {
                    if (mVodPlayer != null) {
                        mVodPlayer.resume();
                    }
                } else {
                    if (mLivePlayer != null) {
                        mLivePlayer.resume();
                    }
                }
            }
            updatePlayState(SuperPlayerConst.PLAYSTATE_PLAYING);
        }

        @Override
        public void onSeekTo(int position) {
            if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_VOD) {
                if (mVodPlayer != null) {
                    mVodPlayer.seek(position);
                }
            } else {
                updatePlayType(SuperPlayerConst.PLAYTYPE_LIVE_SHIFT);
                TCLogReport.getInstance().uploadLogs(TCLogReport.ELK_ACTION_TIMESHIFT, 0, 0);
                if (mLivePlayer != null) {
                    mLivePlayer.seek(position);
                }
                if (mWatcher != null) {
                    mWatcher.stop();
                }
            }
        }

        @Override
        public void onResumeLive() {
            if (mLivePlayer != null) {
                mLivePlayer.resumeLive();
            }
            updatePlayType(SuperPlayerConst.PLAYTYPE_LIVE);
        }

        @Override
        public void onDanmuToggle(boolean isOpen) {
            if (mDanmuView != null) {
                mDanmuView.toggle(isOpen);
            }
        }

        @Override
        public void onSnapshot() {
            if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_VOD) {
                if (mVodPlayer != null) {
                    mVodPlayer.snapshot(new TXLivePlayer.ITXSnapshotListener() {
                        @Override
                        public void onSnapshot(Bitmap bmp) {
                            showSnapshotWindow(bmp);
                        }
                    });
                }
            } else if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_LIVE_SHIFT) {
                Toast.makeText(getContext(), "时移直播时暂不支持截图", Toast.LENGTH_SHORT).show();
            } else {
                if (mLivePlayer != null) {
                    mLivePlayer.snapshot(new TXLivePlayer.ITXSnapshotListener() {
                        @Override
                        public void onSnapshot(Bitmap bmp) {
                            showSnapshotWindow(bmp);
                        }
                    });
                }
            }
        }

        @Override
        public void onQualityChange(TCVideoQuality quality) {
            mControllerFullScreen.updateVideoQuality(quality);
            defaultQualityIndex = quality.index;
            mControllerFullScreen.setDefaultQuality(defaultQualityIndex);
            if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_VOD) {
                if (mVodPlayer != null) {
                    if (quality.url != null) { // br!=0;index=-1;url!=null   //br=0;index!=-1;url!=null
                        // 说明是非多bitrate的m3u8子流，需要手动seek
                        float currentTime = mVodPlayer.getCurrentPlaybackTime();
                        mVodPlayer.stopPlay(true);
                        TXCLog.i(TAG, "onQualitySelect quality.url:" + quality.url);
                        mVodPlayer.setStartTime(currentTime);
                        mVodPlayer.startPlay(quality.url);
                    } else { //br!=0;index!=-1;url=null
                        TXCLog.i(TAG, "setBitrateIndex quality.index:" + quality.index);
                        // 说明是多bitrate的m3u8子流，会自动无缝seek
                        mVodPlayer.setBitrateIndex(quality.index);
                    }
                }
            } else {
                if (mLivePlayer != null && !TextUtils.isEmpty(quality.url)) {
                    int result = mLivePlayer.switchStream(quality.url);
                    if (result < 0) {
                        Toast.makeText(getContext(), "切换" + quality.title + "清晰度失败，请稍候重试", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "正在切换到" + quality.title + "...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            //清晰度上报
            TCLogReport.getInstance().uploadLogs(TCLogReport.ELK_ACTION_CHANGE_RESOLUTION, 0, 0);
        }

        @Override
        public void onSpeedChange(float speedLevel) {
            if (mVodPlayer != null) {
                mVodPlayer.setRate(speedLevel);
            }
            //速度改变上报
            TCLogReport.getInstance().uploadLogs(TCLogReport.ELK_ACTION_CHANGE_SPEED, 0, 0);
        }

        @Override
        public void onMirrorToggle(boolean isMirror) {
            if (mVodPlayer != null) {
                mVodPlayer.setMirror(isMirror);
            }
            if (isMirror) {
                //镜像上报
                TCLogReport.getInstance().uploadLogs(TCLogReport.ELK_ACTION_MIRROR, 0, 0);
            }
        }

        @Override
        public void onHWAccelerationToggle(boolean isAccelerate) {
            if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_VOD) {
                mChangeHWAcceleration = true;
                if (mVodPlayer != null) {
                    mVodPlayer.enableHardwareDecode(isAccelerate);
                    mSeekPos = (int) mVodPlayer.getCurrentPlaybackTime();
                    TXCLog.i(TAG, "save pos:" + mSeekPos);
                    stopPlay();
                    playModeVideo(mCurrentProtocol);
                }
            } else {
                if (mLivePlayer != null) {
                    mLivePlayer.enableHardwareDecode(isAccelerate);
                    playWithModel(mCurrentModel);
                }
            }
            // 硬件加速上报
            if (isAccelerate) {
                TCLogReport.getInstance().uploadLogs(TCLogReport.ELK_ACTION_HW_DECODE, 0, 0);
            } else {
                TCLogReport.getInstance().uploadLogs(TCLogReport.ELK_ACTION_SOFT_DECODE, 0, 0);
            }
        }

        @Override
        public void onAnthologyChange(int position) {
            if (callback != null) callback.onAnthologySelect(position);
        }

        @Override
        public void onSwitchPlayScale(boolean fullScale) {
            if (fullScale) {
                mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
            } else {
                mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
                mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
            }
        }
    };


    /**
     * 显示截图窗口
     *
     * @param bmp
     */
    private void showSnapshotWindow(final Bitmap bmp) {
        if (bmp == null) return;
        final PopupWindow popupWindow = new PopupWindow(mContext);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_new_vod_snap, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_snap);
        imageView.setImageBitmap(bmp);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(mRootView, Gravity.TOP, 1800, 300);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                TCImageUtil.save2MediaStore(mContext, bmp);
            }
        });
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();
            }
        }, 3000);
    }

    /**
     * 旋转屏幕方向
     *
     * @param orientation
     */
    private void rotateScreenOrientation(int orientation) {
        switch (orientation) {
            case SuperPlayerConst.ORIENTATION_LANDSCAPE:
                ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case SuperPlayerConst.ORIENTATION_PORTRAIT:
                ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
        }
    }

    /**
     * 点播播放器回调
     *
     * 具体可参考官网文档：https://cloud.tencent.com/document/product/881/20216
     *
     * @param player
     * @param event  事件id.id类型请参考 {@linkplain TXLiveConstants#PLAY_EVT_CONNECT_SUCC 播放事件列表}.
     * @param param
     */
    /**
     * 点播播放器回调
     * <p>
     * 具体可参考官网文档：https://cloud.tencent.com/document/product/881/20216
     *
     * @param player
     * @param event  事件id.id类型请参考 {@linkplain TXLiveConstants#PLAY_EVT_CONNECT_SUCC 播放事件列表}.
     * @param param
     */
    @Override
    public void onPlayEvent(TXVodPlayer player, int event, Bundle param) {
        if (event != TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            String playEventLog = "TXVodPlayer onPlayEvent event: " + event + ", " + param.getString(TXLiveConstants.EVT_DESCRIPTION);
            TXCLog.d(TAG, playEventLog);
        }
        switch (event) {
            case TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED://视频播放开始
                if (adView.getVisibility() == GONE && isJump && vmStartTime != 0) {
                    mVodPlayer.seek(vmStartTime);
                }
                mControllerWindow.hideBackground();
                updatePlayState(SuperPlayerConst.PLAYSTATE_PLAYING);
                if (mIsMultiBitrateStream) {
                    List<TXBitrateItem> bitrateItems = mVodPlayer.getSupportedBitrates();
                    if (bitrateItems == null || bitrateItems.size() == 0)
                        return;
                    Collections.sort(bitrateItems); //masterPlaylist多清晰度，按照码率排序，从低到高
                    List<TCVideoQuality> videoQualities = new ArrayList<>();
                    int size = bitrateItems.size();
                    List<TCResolutionName> resolutionNames = mCurrentProtocol.getResolutionNameList();
                    for (int i = 0; i < size; i++) {
                        TXBitrateItem bitrateItem = bitrateItems.get(i);
                        TCVideoQuality quality;
                        if (resolutionNames != null) {
                            quality = TCVideoQualityUtil.convertToVideoQuality(bitrateItem, mCurrentProtocol.getResolutionNameList());
                        } else {
                            quality = TCVideoQualityUtil.convertToVideoQuality(bitrateItem, i);
                        }
                        quality.index = i;
                        videoQualities.add(quality);
                    }
                    if (!mDefaultQualitySet) {
                        mVodPlayer.setBitrateIndex(bitrateItems.get(bitrateItems.size() - 1).index); //默认播放码率最高的
                        mControllerFullScreen.updateVideoQuality(videoQualities.get(videoQualities.size() - 1));
                        mDefaultQualitySet = true;
                    }
                    mControllerFullScreen.setVideoQualityList(videoQualities);
                }
                break;
            case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME:
                if (mChangeHWAcceleration) { //切换软硬解码器后，重新seek位置
                    TXCLog.i(TAG, "seek pos:" + mSeekPos);
                    mControllerCallback.onSeekTo(mSeekPos);
                    mChangeHWAcceleration = false;
                }
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_END:
                if (adView.getVisibility() == VISIBLE) {
                    adView.setVisibility(GONE);
                    mControllerWindow.setVisibility(VISIBLE);
                    mControllerFullScreen.setVisibility(VISIBLE);
                    SuperPlayerModel superPlayerModel = new SuperPlayerModel();
                    superPlayerModel.appId = playid;
                    superPlayerModel.videoId = new SuperPlayerVideoId();
                    superPlayerModel.videoId.fileId = v_fileid;
                    if (!TextUtils.isEmpty(title))
                        superPlayerModel.title = title;
                    playWithModel(superPlayerModel);
                    updatePlayState(SuperPlayerConst.PLAYSTATE_PLAYING);
                } else if (videoBeans != null && videoBeans.size() != 0) {
                    if (currentPosition != videoBeans.size() - 1) {
                        currentPosition++;
                        if (playEndCallback != null) {
                            playEndCallback.palyEnd(currentPosition);
                        }
                    }
                } else
                    updatePlayState(SuperPlayerConst.PLAYSTATE_END);
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS_MS);
                int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION_MS);
                if (adView.getVisibility() != VISIBLE && startProgress != 0) {
                    int startPosition = duration * startProgress / 100;
                    if (progress < startPosition) {
                        mVodPlayer.seek(startPosition / 1000);
                        startProgress = 0;
                    }
                }
                updateVideoProgress(progress / 1000, duration / 1000);
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN: {
                updatePlayState(SuperPlayerConst.PLAYSTATE_PLAYING);
                break;
            }
            default:
                break;
        }
        if (event < 0) {// 播放点播文件失败
            mVodPlayer.stopPlay(true);
            updatePlayState(SuperPlayerConst.PLAYSTATE_PAUSE);
            Toast.makeText(mContext, param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onNetStatus(TXVodPlayer player, Bundle status) {

    }

    /**
     * 直播播放器回调
     * <p>
     * 具体可参考官网文档：https://cloud.tencent.com/document/product/881/20217
     *
     * @param event 事件id.id类型请参考 {@linkplain TXLiveConstants#PUSH_EVT_CONNECT_SUCC 播放事件列表}.
     * @param param
     */
    @Override
    public void onPlayEvent(int event, Bundle param) {
        if (event != TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            String playEventLog = "TXLivePlayer onPlayEvent event: " + event + ", " + param.getString(TXLiveConstants.EVT_DESCRIPTION);
            TXCLog.d(TAG, playEventLog);
        }
        switch (event) {
            case TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED: //视频播放开始
                updatePlayState(SuperPlayerConst.PLAYSTATE_PLAYING);
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN:
                updatePlayState(SuperPlayerConst.PLAYSTATE_PLAYING);
                if (mWatcher != null) mWatcher.exitLoading();
                break;
            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
            case TXLiveConstants.PLAY_EVT_PLAY_END:
                if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_LIVE_SHIFT) {  // 直播时移失败，返回直播
                    mControllerCallback.onResumeLive();
                    Toast.makeText(mContext, "时移失败,返回直播", Toast.LENGTH_SHORT).show();
                    updatePlayState(SuperPlayerConst.PLAYSTATE_PLAYING);
                } else {
                    stopPlay();
                    updatePlayState(SuperPlayerConst.PLAYSTATE_END);
                    if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                        Toast.makeText(mContext, "网络不给力,点击重试", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_LOADING:
            case TXLiveConstants.PLAY_WARNING_RECONNECT:
                updatePlayState(SuperPlayerConst.PLAYSTATE_LOADING);
                if (mWatcher != null) mWatcher.enterLoading();
                break;
            case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME:
                break;
            case TXLiveConstants.PLAY_EVT_STREAM_SWITCH_SUCC:
                Toast.makeText(mContext, "清晰度切换成功", Toast.LENGTH_SHORT).show();
                break;
            case TXLiveConstants.PLAY_ERR_STREAM_SWITCH_FAIL:
                Toast.makeText(mContext, "清晰度切换失败", Toast.LENGTH_SHORT).show();
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS_MS);
                mMaxLiveProgressTime = progress > mMaxLiveProgressTime ? progress : mMaxLiveProgressTime;
                updateVideoProgress(progress / 1000, mMaxLiveProgressTime / 1000);
                break;
            default:
                break;
        }
    }


    @Override
    public void onNetStatus(Bundle status) {

    }

    /**
     * 切换播放模式
     *
     * @param playMode
     */
    public void requestPlayMode(int playMode) {
        if (playMode == SuperPlayerConst.PLAYMODE_WINDOW) {
            if (mControllerCallback != null) {
                mControllerCallback.onSwitchPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
            }
        } else if (playMode == SuperPlayerConst.PLAYMODE_FLOAT) {
            if (mPlayerViewCallback != null) {
                mPlayerViewCallback.onStartFloatWindowPlay();
            }
            if (mControllerCallback != null) {
                mControllerCallback.onSwitchPlayMode(SuperPlayerConst.PLAYMODE_FLOAT);
            }
        }
    }

    /**
     * 检查悬浮窗权限
     * <p>
     * API <18，默认有悬浮窗权限，不需要处理。无法接收无法接收触摸和按键事件，不需要权限和无法接受触摸事件的源码分析
     * API >= 19 ，可以接收触摸和按键事件
     * API >=23，需要在manifest中申请权限，并在每次需要用到权限的时候检查是否已有该权限，因为用户随时可以取消掉。
     * API >25，TYPE_TOAST 已经被谷歌制裁了，会出现自动消失的情况
     */
    private boolean checkOp(Context context, int op) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Method method = AppOpsManager.class.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                TXCLog.e(TAG, Log.getStackTraceString(e));
            }
        }
        return true;
    }

    /**
     * 获取当前播放模式
     *
     * @return
     */
    public int getPlayMode() {
        return mCurrentPlayMode;
    }

    /**
     * 获取当前播放状态
     *
     * @return
     */
    public int getPlayState() {
        return mCurrentPlayState;
    }

    /**
     * SuperPlayerView的回调接口
     */
    public interface OnSuperPlayerViewCallback {

        /**
         * 开始全屏播放
         */
        void onStartFullScreenPlay();

        /**
         * 结束全屏播放
         */
        void onStopFullScreenPlay();

        /**
         * 点击悬浮窗模式下的x按钮
         */
        void onClickFloatCloseBtn();

        /**
         * 点击小播放模式的返回按钮
         */
        void onClickSmallReturnBtn();

        /**
         * 开始悬浮窗播放
         */
        void onStartFloatWindowPlay();
    }

    public void release() {
        if (mControllerWindow != null) {
            mControllerWindow.release();
        }
        if (mControllerFullScreen != null) {
            mControllerFullScreen.release();
        }
        if (mControllerFloat != null) {
            mControllerFloat.release();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            release();
        } catch (Exception e) {
            TXCLog.e(TAG, Log.getStackTraceString(e));
        } catch (Error e) {
            TXCLog.e(TAG, Log.getStackTraceString(e));
        }
    }

    private PlayEndCallback playEndCallback;

    public void setPlayEndCallback(PlayEndCallback playEndCallback) {
        this.playEndCallback = playEndCallback;
    }

    public interface PlayEndCallback {
        void palyEnd(int position);
    }

    public void setAdImage(Drawable bitmap) {
        mControllerFullScreen.setAdImage(bitmap);
    }
}
