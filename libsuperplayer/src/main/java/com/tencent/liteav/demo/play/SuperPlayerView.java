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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tencent.liteav.basic.log.TXCLog;
import com.tencent.liteav.demo.play.bean.TCResolutionName;
import com.tencent.liteav.demo.play.bean.TCVideoQuality;
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
import com.tencent.liteav.demo.play.utils.SPUtils;
import com.tencent.liteav.demo.play.utils.TCImageUtil;
import com.tencent.liteav.demo.play.utils.TCNetWatcher;
import com.tencent.liteav.demo.play.utils.TCUrlUtil;
import com.tencent.liteav.demo.play.utils.TCVideoQualityUtil;
import com.tencent.liteav.demo.play.view.AdView;
import com.tencent.liteav.demo.play.view.BuyAntholgyView;
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

import me.jessyan.autosize.utils.LogUtils;

/**
 * Created by liyuejiao on 2018/7/3.
 * <p>
 * ???????????????view
 * <p>
 * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
 * <p>
 * ??????????????????????????????????????????????????????????????????????????????????????????{@link #playWithModel(SuperPlayerModel)}??????{@link SuperPlayerModel}????????????????????????
 * <p>
 * 1???????????????{@link #playWithModel(SuperPlayerModel)}
 * <p>
 * 2???????????????{@link #setPlayerViewCallback(OnSuperPlayerViewCallback)}
 * <p>
 * 3????????????????????????????????????{@link #initVodPlayer(Context)}?????????????????????{@link #onPlayEvent(TXVodPlayer, int, Bundle)}???
 * ??????????????????{@link #onNetStatus(TXVodPlayer, Bundle)}
 * <p>
 * 4????????????????????????????????????{@link #initLivePlayer(Context)}?????????????????????{@link #onPlayEvent(int, Bundle)}???
 * ??????????????????{@link #onNetStatus(Bundle)}
 * <p>
 * 5???controller????????????{@link #mControllerCallback}
 * <p>
 * 5???????????????????????????{@link #resetPlayer()}
 */

public class SuperPlayerView extends RelativeLayout implements ITXVodPlayListener, ITXLivePlayListener {
    private static final String TAG = "SuperPlayerView";
    private boolean isDefaultLanguage;
    private List<VideoBean> videoBeans;
    private int currentPosition;//?????????????????????
    private AdView adView;
    private VipTipView vipTipView;
    private int resId;
    private ViewGroup oldParent;
    private LinearLayout newParent;
    private BuyAntholgyView buyAntholgyView;
    private boolean isBuyViewVisible;
    private View loading_bar;

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
        }else {
            buyAntholgyView.requestFocus();
        }
    }

    public void setIsBuyViewVisible(boolean visible) {
        this.isBuyViewVisible = visible;
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
        if (!mControllerFullScreen.isShowing()) {
            mControllerFullScreen.showMenu();
        } else {
            mControllerFullScreen.hide();
        }
    }

    public void showTVMenu() {
        if (!mControllerFullScreen.isShowing()) {
            mControllerFullScreen.showMenu();
        } else {
//            mControllerFullScreen.hide();
        }
    }

    public void showProgress(int keyCode) {
        if (duration == 0) return;
        if (buyAntholgyView.findViewById(R.id.buy_line).getVisibility() == VISIBLE) return;
        mControllerFullScreen.showProgress(keyCode, current, duration);
    }

    public View getBuyAntholgyView() {
        return buyAntholgyView;
    }

    private int defaultQualityIndex = 2;

    public void setDefaultQualitySet(int defaultQualityIndex) {
        mDefaultQualitySet = true;
        this.defaultQualityIndex = defaultQualityIndex;
        mControllerFullScreen.setDefaultQuality(defaultQualityIndex);
    }

    private int vmStartTime;//????????????
    private int vmEndTime;//????????????
    private boolean isJump;//????????????????????????

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

    public void setRenderMode(int renderModeFullFillScreen) {
        if (mVodPlayer != null)
            mVodPlayer.setRenderMode(renderModeFullFillScreen);
    }

    public void showLoading(boolean visible) {
        loading_bar.setVisibility(visible?VISIBLE:GONE);
    }

    private enum PLAYER_TYPE {
        PLAYER_TYPE_NULL,
        PLAYER_TYPE_VOD,
        PLAYER_TYPE_LIVE;
    }

    private Context mContext;
    // UI
    private ViewGroup mRootView;                      // SuperPlayerView??????view
    private TXCloudVideoView mTXCloudVideoView;              // ?????????????????????view
    private TCControllerFullScreen mControllerFullScreen;          // ??????????????????view
    private TCControllerWindow mControllerWindow;              // ??????????????????view
    private TCControllerFloat mControllerFloat;               // ?????????????????????view
    private ViewGroup.LayoutParams mLayoutParamWindowMode;         // ???????????????SuperPlayerView???????????????
    private ViewGroup.LayoutParams mLayoutParamFullScreenMode;     // ???????????????SuperPlayerView???????????????
    private LayoutParams mVodControllerWindowParams;     // ??????controller???????????????
    private LayoutParams mVodControllerFullScreenParams; // ??????controller???????????????

    private WindowManager mWindowManager;                 // ????????????????????????
    private WindowManager.LayoutParams mWindowParams;                  // ?????????????????????

    private SuperPlayerModel mCurrentModel;                  // ???????????????model
    private IPlayInfoProtocol mCurrentProtocol;               // ???????????????????????????

    private TXVodPlayer mVodPlayer;                     // ???????????????
    private TXVodPlayConfig mVodPlayConfig;                 // ?????????????????????
    private TXLivePlayer mLivePlayer;                    // ???????????????
    private TXLivePlayConfig mLivePlayConfig;                // ?????????????????????

    private OnSuperPlayerViewCallback mPlayerViewCallback;            // SuperPlayerView??????
    private TCNetWatcher mWatcher;                       // ?????????????????????
    private String mCurrentPlayVideoURL;           // ???????????????url
    private int mCurrentPlayType;               // ??????????????????
    private int mCurrentPlayMode = SuperPlayerConst.PLAYMODE_WINDOW;    // ??????????????????
    private int mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PLAYING; // ??????????????????
    private boolean mIsMultiBitrateStream;          // ??????????????????url??????
    private boolean mIsPlayWithFileId;              // ??????????????????fileId??????
    private long mReportLiveStartTime = -1;      // ?????????????????????????????????????????????
    private long mReportVodStartTime = -1;       // ?????????????????????????????????????????????
    private boolean mDefaultQualitySet = true;             // ?????????????????????url??????????????????????????????
    private boolean mLockScreen = false;            // ??????????????????
    private boolean mChangeHWAcceleration;          // ?????????????????????????????????????????????????????????
    private int mSeekPos;                       // ????????????????????????????????????
    private long mMaxLiveProgressTime;           // ???????????????????????????
    private PLAYER_TYPE mCurPlayType = PLAYER_TYPE.PLAYER_TYPE_NULL;    //??????????????????

    private final int OP_SYSTEM_ALERT_WINDOW = 24;    // ??????TYPE_TOAST??????????????????API??????

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
     * ?????????view
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
        loading_bar = mRootView.findViewById(R.id.loading_progress);

        mVodControllerWindowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mVodControllerFullScreenParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        mControllerFullScreen.setCallback(mControllerCallback);
        mControllerWindow.setCallback(mControllerCallback);
        mControllerFloat.setCallback(mControllerCallback);
        adView.setCallback(mControllerCallback);
        removeAllViews();
        mRootView.removeView(loading_bar);
        mRootView.removeView(mTXCloudVideoView);
        mRootView.removeView(mControllerWindow);
        mRootView.removeView(mControllerFullScreen);
        mRootView.removeView(mControllerFloat);

        addView(mTXCloudVideoView);
        mRootView.removeView(vipTipView);
        addView(vipTipView);
        mRootView.removeView(adView);
        addView(adView);
        addView(loading_bar);
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
                    // ????????????Parent???LayoutParam??????????????????????????????fullscreen????????????LayoutParam
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
     * ????????????????????????
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
        } else {
            mVodPlayConfig.setCacheFolderPath(context.getFilesDir().getPath() + "/txcache");
        }
        mVodPlayConfig.setMaxCacheItems(config.maxCacheItem);
        mVodPlayConfig.setMaxBufferSize(2);
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
     * ????????????????????????
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
     * ????????????
     *
     * @param model
     */
    public void playWithModel(final SuperPlayerModel model) {
        showLoading(true);
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
        // ????????????????????????????????????
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
        if (model.videoId != null || model.videoIdV2 != null) { // ??????FileId??????
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
                    Toast.makeText(SuperPlayerView.this.getContext(), "???????????????????????? code = " + errCode + " msg = " + message, Toast.LENGTH_SHORT).show();
                }
            });
        } else { // ??????URL??????
            String videoURL = null;
            List<TCVideoQuality> videoQualities = new ArrayList<>();
            TCVideoQuality defaultVideoQuality = null;
            if (model.multiURLs != null && !model.multiURLs.isEmpty()) {// ?????????URL??????
                int i = 0;
                for (SuperPlayerModel.SuperPlayerURL superPlayerURL : model.multiURLs) {
                    if (i == model.playDefaultIndex) {
                        videoURL = superPlayerURL.url;
                    }
                    videoQualities.add(new TCVideoQuality(i++, superPlayerURL.qualityName, superPlayerURL.url));
                }
                defaultVideoQuality = videoQualities.get(model.playDefaultIndex);
                mControllerFullScreen.setDefaultQuality(defaultQualityIndex);
            } else if (!TextUtils.isEmpty(model.url)) { // ??????URL????????????
//                videoQualities.add(new TCVideoQuality(0, "", model.url));
//                defaultVideoQuality = videoQualities.get(0);
                videoURL = model.url;
            }

            if (TextUtils.isEmpty(videoURL)) {
                Toast.makeText(this.getContext(), "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TCUrlUtil.isRTMPPlay(videoURL)) { // ????????????????????????RTMP?????????
                mReportLiveStartTime = System.currentTimeMillis();
                mLivePlayer.setPlayerView(mTXCloudVideoView);
                playLiveURL(videoURL, TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
            } else if (TCUrlUtil.isFLVPlay(videoURL)) { // ????????????????????????FLV?????????
                mReportLiveStartTime = System.currentTimeMillis();
                mLivePlayer.setPlayerView(mTXCloudVideoView);
                playTimeShiftLiveURL(model);
                if (model.multiURLs != null && !model.multiURLs.isEmpty()) {
                    startMultiStreamLiveURL(videoURL);
                }
            } else { // ????????????????????????????????????
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
     * ??????FileId??????
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
     * ????????????URL
     */
    private void playLiveURL(String url, int playType) {
        mCurrentPlayVideoURL = url;
        if (mLivePlayer != null) {
            mLivePlayer.setPlayListener(this);
            int result = mLivePlayer.startPlay(url, playType); // result????????????0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
            if (result != 0) {
                TXCLog.e(TAG, "playLiveURL videoURL:" + url + ",result:" + result);
            } else {
                mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PLAYING;
                mCurPlayType = PLAYER_TYPE.PLAYER_TYPE_LIVE;
                TXCLog.e(TAG, "playLiveURL mCurrentPlayState:" + mCurrentPlayState);
            }
        }
    }

    private boolean autoPlay = true;

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    /**
     * ????????????url
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
            mVodPlayer.setAutoPlay(autoPlay);
            mVodPlayer.setVodListener(this);
            int ret = mVodPlayer.startPlay(url);
            if (ret == 0) {
                mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PLAYING;
                mCurPlayType = PLAYER_TYPE.PLAYER_TYPE_VOD;
                TXCLog.e(TAG, "playVodURL mCurrentPlayState:" + mCurrentPlayState);
            }
        }
        mIsPlayWithFileId = false;
    }

    /**
     * ??????????????????url
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
            TXCLog.e(TAG, "playTimeShiftLiveURL: bizidNum ?????? = %s " + bizid);
        }
    }

    /**
     * ???????????????url
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
     * ????????????
     *
     * @param title ????????????
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
     * ??????????????????
     *
     * @param current  ??????????????????(???)
     * @param duration ?????????(???)
     */
    private void updateVideoProgress(long current, long duration) {
        this.current = current;
        this.duration = duration;
        mControllerWindow.updateVideoProgress(current, duration);
        mControllerFullScreen.updateVideoProgress(current, duration);
        if (adView.getVisibility() == VISIBLE) {
            adView.setDuration((int) (duration - current));
        }
        if (balanceTimeCallBack != null && duration != 0) {
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
            if (duration!=0&&current!=0&&duration-current <= vmEndTime) {
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
     * ??????????????????
     *
     * @param playType
     */
    private void updatePlayType(int playType) {
        mCurrentPlayType = playType;
        mControllerWindow.updatePlayType(playType);
        mControllerFullScreen.updatePlayType(playType);
    }

    /**
     * ??????????????????
     *
     * @param playState
     */
    private void updatePlayState(int playState) {
        mCurrentPlayState = playState;
        mControllerWindow.updatePlayState(playState);
        mControllerFullScreen.updatePlayState(playState);
    }

    /**
     * resume??????????????????
     */
    public void onResume() {
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
     * pause??????????????????
     */
    public void onPause() {
        pause();
    }


    private void pause() {
        if (mVodPlayer != null) {
            mVodPlayer.pause();
            mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PAUSE;
            updatePlayState(mCurrentPlayState);
        }
        if (mLivePlayer != null) {
            mLivePlayer.pause();
            mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PAUSE;
            updatePlayState(mCurrentPlayState);
        }


    }

    /**
     * ???????????????
     */
    public void resetPlayer() {
        stopPlay();
    }

    /**
     * ????????????
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
     * ??????????????????
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
     * ??????????????????????????????
     *
     * @param callback
     */
    public void setPlayerViewCallback(OnSuperPlayerViewCallback callback) {
        mPlayerViewCallback = callback;
    }

    /**
     * ????????????????????????
     */
    private void fullScreen(boolean isFull) {
        Activity activity = (Activity) getContext();
        ViewGroup viewGroup = activity.findViewById(resId);
        if (vipTipView.getVisibility() == VISIBLE)
            vipTipView.fullScreen(isFull);
        if (buyAntholgyView.getVisibility() == VISIBLE)
            buyAntholgyView.fullScreen(isFull);
        if (isFull) {
            while (getParent() != null) {
                oldParent.removeView(this);
            }
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
     * ?????????controller??????
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
            //??????????????????
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
            // ??????????????????
            else if (requestPlayMode == SuperPlayerConst.PLAYMODE_WINDOW) {
                // ??????????????????
                if (mCurrentPlayMode == SuperPlayerConst.PLAYMODE_FLOAT) {
                    try {
                        Context viewContext = SuperPlayerView.this.getContext();
                        Intent intent = null;
                        if (viewContext instanceof Activity) {
                            intent = new Intent(SuperPlayerView.this.getContext(), viewContext.getClass());
                        } else {
                            Toast.makeText(viewContext, "??????????????????", Toast.LENGTH_SHORT).show();
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
                // ?????????????????????
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
            //?????????????????????
            else if (requestPlayMode == SuperPlayerConst.PLAYMODE_FLOAT) {
                TXCLog.i(TAG, "requestPlayMode Float :" + Build.MANUFACTURER);
                SuperPlayerGlobalConfig prefs = SuperPlayerGlobalConfig.getInstance();
                if (!prefs.enableFloatWindow) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 6.0???????????????????????????
                    if (!Settings.canDrawOverlays(mContext)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                        mContext.startActivity(intent);
                        return;
                    }
                } else {
                    if (!checkOp(mContext, OP_SYSTEM_ALERT_WINDOW)) {
                        Toast.makeText(mContext, "????????????????????????,??????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SuperPlayerView.this.getContext(), "??????????????????", Toast.LENGTH_SHORT).show();
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
                // ???????????????
                TCLogReport.getInstance().uploadLogs(TCLogReport.ELK_ACTION_FLOATMOE, 0, 0);
            }
            mCurrentPlayMode = requestPlayMode;
        }

        @Override
        public void onBackPressed(int playMode) {
            switch (playMode) {
                case SuperPlayerConst.PLAYMODE_FULLSCREEN:// ???????????????????????????????????????????????????
                    onSwitchPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
                    break;
                case SuperPlayerConst.PLAYMODE_WINDOW:// ?????????????????????????????????????????????
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
                case SuperPlayerConst.PLAYMODE_FLOAT:// ???????????????????????????
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
            if (mCurrentPlayState == SuperPlayerConst.PLAYSTATE_END) { //??????
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
            } else if (mCurrentPlayState == SuperPlayerConst.PLAYSTATE_PAUSE) { //????????????
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
                Toast.makeText(getContext(), "?????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                        // ???????????????bitrate???m3u8?????????????????????seek
                        float currentTime = mVodPlayer.getCurrentPlaybackTime();
                        mVodPlayer.stopPlay(true);
                        TXCLog.i(TAG, "onQualitySelect quality.url:" + quality.url);
                        mVodPlayer.setStartTime(currentTime);
                        mVodPlayer.startPlay(quality.url);
                    } else { //br!=0;index!=-1;url=null
                        TXCLog.i(TAG, "setBitrateIndex quality.index:" + quality.index);
                        // ????????????bitrate???m3u8????????????????????????seek
                        mVodPlayer.setBitrateIndex(quality.index);
                    }
                }
            } else {
                if (mLivePlayer != null && !TextUtils.isEmpty(quality.url)) {
                    int result = mLivePlayer.switchStream(quality.url);
                    if (result < 0) {
                        Toast.makeText(getContext(), "??????" + quality.title + "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "???????????????" + quality.title + "...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            //???????????????
            TCLogReport.getInstance().uploadLogs(TCLogReport.ELK_ACTION_CHANGE_RESOLUTION, 0, 0);
        }

        @Override
        public void onSpeedChange(float speedLevel) {
            if (mVodPlayer != null) {
                mVodPlayer.setRate(speedLevel);
            }
            //??????????????????
            TCLogReport.getInstance().uploadLogs(TCLogReport.ELK_ACTION_CHANGE_SPEED, 0, 0);
        }

        @Override
        public void onMirrorToggle(boolean isMirror) {
            if (mVodPlayer != null) {
                mVodPlayer.setMirror(isMirror);
            }
            if (isMirror) {
                //????????????
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
            // ??????????????????
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
     * ??????????????????
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
     * ??????????????????
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
     * ?????????????????????
     *
     * ??????????????????????????????https://cloud.tencent.com/document/product/881/20216
     *
     * @param player
     * @param event  ??????id.id??????????????? {@linkplain TXLiveConstants#PLAY_EVT_CONNECT_SUCC ??????????????????}.
     * @param param
     */
    /**
     * ?????????????????????
     * <p>
     * ??????????????????????????????https://cloud.tencent.com/document/product/881/20216
     *
     * @param player
     * @param event  ??????id.id??????????????? {@linkplain TXLiveConstants#PLAY_EVT_CONNECT_SUCC ??????????????????}.
     * @param param
     */
    @Override
    public void onPlayEvent(TXVodPlayer player, int event, Bundle param) {
        if (event != TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            String playEventLog = "TXVodPlayer onPlayEvent event: " + event + ", " + param.getString(TXLiveConstants.EVT_DESCRIPTION);
            TXCLog.d(TAG, playEventLog);
        }
        switch (event) {
            case TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED://??????????????????
                if (!SPUtils.init(mContext).getBoolean("switch_picture_quality", false) && adView.getVisibility() == GONE && isJump && vmStartTime != 0) {
                    mVodPlayer.seek(vmStartTime);
                }
                SPUtils.init(mContext).putBoolean("switch_picture_quality", false);
                mControllerWindow.hideBackground();
                updatePlayState(SuperPlayerConst.PLAYSTATE_PLAYING);
                if (mIsMultiBitrateStream) {
                    List<TXBitrateItem> bitrateItems = mVodPlayer.getSupportedBitrates();
                    if (bitrateItems == null || bitrateItems.size() == 0)
                        return;
                    Collections.sort(bitrateItems); //masterPlaylist????????????????????????????????????????????????
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
                        mVodPlayer.setBitrateIndex(bitrateItems.get(bitrateItems.size() - 1).index); //???????????????????????????
                        mControllerFullScreen.updateVideoQuality(videoQualities.get(videoQualities.size() - 1));
                        mDefaultQualitySet = true;
                    }
                    mControllerFullScreen.setVideoQualityList(videoQualities);
                }
                break;
            case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME:
                showLoading(false);
                if (mChangeHWAcceleration) { //?????????????????????????????????seek??????
                    TXCLog.i(TAG, "seek pos:" + mSeekPos);
                    mControllerCallback.onSeekTo(mSeekPos);
                    mChangeHWAcceleration = false;
                }
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_END:
                if (adView.getVisibility() == VISIBLE) {
                    adView.setVisibility(GONE);
                    if (isBuyViewVisible) {
                        setBuyViewVisible(true);
                    }
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
        if (event < 0) {// ????????????????????????
            mVodPlayer.stopPlay(true);
            updatePlayState(SuperPlayerConst.PLAYSTATE_PAUSE);
            Toast.makeText(mContext, param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onNetStatus(TXVodPlayer player, Bundle status) {

    }

    /**
     * ?????????????????????
     * <p>
     * ??????????????????????????????https://cloud.tencent.com/document/product/881/20217
     *
     * @param event ??????id.id??????????????? {@linkplain TXLiveConstants#PUSH_EVT_CONNECT_SUCC ??????????????????}.
     * @param param
     */
    @Override
    public void onPlayEvent(int event, Bundle param) {
        if (event != TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            String playEventLog = "TXLivePlayer onPlayEvent event: " + event + ", " + param.getString(TXLiveConstants.EVT_DESCRIPTION);
            TXCLog.d(TAG, playEventLog);
        }
        String playEventLog = "TXLivePlayer onPlayEvent event: " + event + ", " + param.getString(TXLiveConstants.EVT_DESCRIPTION);
        TXCLog.d(TAG+">>>>>>>>>>", playEventLog);
        switch (event) {
            case TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED: //??????????????????
                LogUtils.e("PLAY_EVT_VOD_PLAY_PREPARED");
                updatePlayState(SuperPlayerConst.PLAYSTATE_PLAYING);
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN:
                updatePlayState(SuperPlayerConst.PLAYSTATE_PLAYING);
                if (mWatcher != null) mWatcher.exitLoading();
                break;
            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
            case TXLiveConstants.PLAY_EVT_PLAY_END:
                if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_LIVE_SHIFT) {  // ?????????????????????????????????
                    mControllerCallback.onResumeLive();
                    Toast.makeText(mContext, "????????????,????????????", Toast.LENGTH_SHORT).show();
                    updatePlayState(SuperPlayerConst.PLAYSTATE_PLAYING);
                } else {
                    stopPlay();
                    updatePlayState(SuperPlayerConst.PLAYSTATE_END);
                    if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                        Toast.makeText(mContext, "???????????????,????????????", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, "?????????????????????", Toast.LENGTH_SHORT).show();
                break;
            case TXLiveConstants.PLAY_ERR_STREAM_SWITCH_FAIL:
                Toast.makeText(mContext, "?????????????????????", Toast.LENGTH_SHORT).show();
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
     * ??????????????????
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
     * ?????????????????????
     * <p>
     * API <18?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * API >= 19 ????????????????????????????????????
     * API >=23????????????manifest???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * API >25???TYPE_TOAST ?????????????????????????????????????????????????????????
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

    public AdView getAdView() {
        return adView;
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    public int getPlayMode() {
        return mCurrentPlayMode;
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    public int getPlayState() {
        return mCurrentPlayState;
    }

    /**
     * SuperPlayerView???????????????
     */
    public interface OnSuperPlayerViewCallback {

        /**
         * ??????????????????
         */
        void onStartFullScreenPlay();

        /**
         * ??????????????????
         */
        void onStopFullScreenPlay();

        /**
         * ???????????????????????????x??????
         */
        void onClickFloatCloseBtn();

        /**
         * ????????????????????????????????????
         */
        void onClickSmallReturnBtn();

        /**
         * ?????????????????????
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

    public TCControllerFullScreen getmControllerFullScreen() {
        return mControllerFullScreen;
    }
}
