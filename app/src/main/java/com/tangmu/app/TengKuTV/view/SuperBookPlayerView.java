package com.tangmu.app.TengKuTV.view;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.bean.BookDetailDataBean;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tencent.liteav.basic.log.TXCLog;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerGlobalConfig;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerVideoId;
import com.tencent.liteav.demo.play.bean.TCResolutionName;
import com.tencent.liteav.demo.play.bean.TCVideoQuality;
import com.tencent.liteav.demo.play.bean.VideoBean;
import com.tencent.liteav.demo.play.controller.IControllerCallback;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
 * <p>
 * 3????????????????????????????????????{@link #initVodPlayer(Context)}?????????????????????{@link #onPlayEvent(TXVodPlayer, int, Bundle)}???
 * ??????????????????{@link #onNetStatus(TXVodPlayer, Bundle)}
 * <p>
 * 4????????????????????????????????????{@link #initLivePlayer(Context)}?????????????????????{@link #onPlayEvent(int, Bundle)}???
 * ??????????????????{@link #onNetStatus(Bundle)}
 * <p>
 * <p>
 * 5???????????????????????????{@link #resetPlayer()}
 */

public class SuperBookPlayerView extends RelativeLayout implements ITXVodPlayListener, ITXLivePlayListener {
    private static final String TAG = "SuperPlayerView";

    public void setInfo(String title, String author, String des) {
        mControllerWindow.setInfo(title, author, des);
    }

    public int getPosition() {
        return position;
    }


    private long current;
    private long duration;

    public int getProgress() {
        if (duration == 0) return 0;
        return (int) (current * 100 / duration);
    }

    public void setCover(String coverImgPath) {
        mControllerWindow.setCover(coverImgPath);
    }

    private int startProgress = 0;

    public void setCurrent(int progress) {
        startProgress = progress;
    }

    private String adUrl;

    public void setBookAd(String adUrl) {
        if (!TextUtils.isEmpty(adUrl))
            this.adUrl = Util.convertVideoPath(adUrl);
    }

    public void showProgress(int keyCode) {
        if (duration == 0) return;
        mControllerWindow.showProgress(keyCode, current, duration);
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
    private TCControllerWindow mControllerWindow;              // ??????????????????view
    private ViewGroup.LayoutParams mLayoutParamWindowMode;         // ???????????????SuperPlayerView???????????????
    private LayoutParams mVodControllerWindowParams;     // ??????controller???????????????

    private SuperPlayerModel mCurrentModel;                  // ???????????????model
    private IPlayInfoProtocol mCurrentProtocol;               // ???????????????????????????

    private TXVodPlayer mVodPlayer;                     // ???????????????
    private TXVodPlayConfig mVodPlayConfig;                 // ?????????????????????
    private TXLivePlayer mLivePlayer;                    // ???????????????
    private TXLivePlayConfig mLivePlayConfig;                // ?????????????????????

    private TCNetWatcher mWatcher;                       // ?????????????????????
    private String mCurrentPlayVideoURL;           // ???????????????url
    private int mCurrentPlayType;               // ??????????????????
    private int mCurrentPlayMode = SuperPlayerConst.PLAYMODE_WINDOW;    // ??????????????????
    private int mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PLAYING; // ??????????????????
    private boolean mIsMultiBitrateStream;          // ??????????????????url??????
    private boolean mIsPlayWithFileId;              // ??????????????????fileId??????
    private long mReportLiveStartTime = -1;      // ?????????????????????????????????????????????
    private long mReportVodStartTime = -1;       // ?????????????????????????????????????????????
    private boolean mDefaultQualitySet;             // ?????????????????????url??????????????????????????????
    private boolean mLockScreen = false;            // ??????????????????
    private boolean mChangeHWAcceleration;          // ?????????????????????????????????????????????????????????
    private int mSeekPos;                       // ????????????????????????????????????
    private long mMaxLiveProgressTime;           // ???????????????????????????
    private PLAYER_TYPE mCurPlayType = PLAYER_TYPE.PLAYER_TYPE_NULL;    //??????????????????


    public SuperBookPlayerView(Context context) {
        super(context);
        initView(context);
    }

    public SuperBookPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SuperBookPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mRootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.book_player, null);
        mTXCloudVideoView = (TXCloudVideoView) mRootView.findViewById(R.id.cloud_video_view);
        mControllerWindow = (TCControllerWindow) mRootView.findViewById(R.id.controller_small);
        mVodControllerWindowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mControllerWindow.setCallback(mControllerCallback);
        SelectBookCallback selectBookCallback = new SelectBookCallback() {

            @Override
            public void back() {
                if (data != null) {
                    resetPlayer();
                    selectBook(false);
                }
            }

            @Override
            public void forward() {
                if (data != null) {
                    resetPlayer();
                    selectBook(true);
                }
            }
        };
        mControllerWindow.setBookSelectCallback(selectBookCallback);
        removeAllViews();
        mRootView.removeView(mTXCloudVideoView);
        mRootView.removeView(mControllerWindow);

        addView(mTXCloudVideoView);
        addView(mControllerWindow);
        TCLogReport.getInstance().setAppName(context);
        TCLogReport.getInstance().setPackageName(context);
    }

    private void selectBook(boolean isForward) {
        if (isForward) {
            if (position == data.size() - 1) {
                position = 0;
            } else {
                position += 1;
            }
        } else {
            if (position == 0) {
                position = data.size() - 1;
            } else {
                position -= 1;
            }
        }
        SuperPlayerModel model = new SuperPlayerModel();
        model.appId = Constant.PLAYID;// ?????? AppId
        if (TextUtils.isEmpty(adUrl)) {
            model.videoId = new SuperPlayerVideoId();
            model.videoId.fileId = data.get(position).getBd_fileid();
        } else {
            if (!TextUtils.isEmpty(mCurrentPlayVideoURL) && mCurrentPlayVideoURL.equals(adUrl)) {
                model.videoId = new SuperPlayerVideoId();
                model.videoId.fileId = data.get(position).getBd_fileid();
            } else {
                model.url = adUrl;
            }
        }

        playWithModel(model);
        if (bookSelectedCallback != null) {
            bookSelectedCallback.onBookSelected(position);
        }
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
        }
        mVodPlayConfig.setMaxCacheItems(config.maxCacheItem);
        mVodPlayer.setConfig(mVodPlayConfig);
        mVodPlayer.setRenderMode(config.renderMode);
        mVodPlayer.setVodListener(this);
        mVodPlayer.enableHardwareDecode(config.enableHWAcceleration);
    }

    public void disableCache() {
        mVodPlayConfig.setMaxCacheItems(0);
        mVodPlayer.setConfig(mVodPlayConfig);
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

    /**
     * ????????????
     *
     * @param model
     */
    public void playWithModel(final SuperPlayerModel model) {
        mCurrentModel = model;
        stopPlay();
        initLivePlayer(getContext());
        initVodPlayer(getContext());
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
                }

                @Override
                public void onError(int errCode, String message) {
                    TXCLog.i(TAG, "onFail: errorCode = " + errCode + " message = " + message);
                    Toast.makeText(SuperBookPlayerView.this.getContext(), "???????????????????????? code = " + errCode + " msg = " + message, Toast.LENGTH_SHORT).show();
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
        }
    }

    private List<BookDetailDataBean.BookDetailBean> data;
    private int position;

    //??????????????????
    public void setData(List<BookDetailDataBean.BookDetailBean> data, int position) {
        this.data = data;
        this.position = position;
    }


    private SelectBookCallback selectBookCallback;

    public interface SelectBookCallback {
        void back();

        void forward();

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
            mIsMultiBitrateStream = false;
        } else {
            mIsMultiBitrateStream = true;
        }

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
            mVodPlayer.setAutoPlay(true);
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
    }

    /**
     * ??????????????????
     *
     * @param playType
     */
    private void updatePlayType(int playType) {
        mCurrentPlayType = playType;
        mControllerWindow.updatePlayType(playType);
    }

    /**
     * ??????????????????
     *
     * @param playState
     */
    private void updatePlayState(int playState) {
        mCurrentPlayState = playState;
        mControllerWindow.updatePlayState(playState);
    }

    /**
     * resume??????????????????
     */
    public void onResume() {
        resume();
    }

    private void resume() {
        if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_VOD && mVodPlayer != null) {
            mVodPlayer.resume();
            mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PLAYING;
        }
    }

    /**
     * pause??????????????????
     */
    public void onPause() {
        pause();
    }


    private void pause() {
        if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_VOD && mVodPlayer != null) {
            mVodPlayer.pause();
            mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PAUSE;
        }
    }

    public int getPlayState() {
        return mCurrentPlayState;
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
     * ????????????????????????
     */
    private void fullScreen(boolean isFull) {
        if (getContext() instanceof Activity) {
            Activity activity = (Activity) getContext();
            if (isFull) {
                //?????????????????????????????????
                View decorView = activity.getWindow().getDecorView();
                if (decorView == null) return;
                if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
                    decorView.setSystemUiVisibility(View.GONE);
                } else if (Build.VERSION.SDK_INT >= 19) {
                    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                }
            } else {
                View decorView = activity.getWindow().getDecorView();
          /*        if (decorView == null) return;
                if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
                    decorView.setSystemUiVisibility(View.VISIBLE);
                } else if (Build.VERSION.SDK_INT >= 19) {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }*/
                int systemUiVisibility = decorView.getSystemUiVisibility();
                int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                systemUiVisibility |= flags;
                decorView.setSystemUiVisibility(systemUiVisibility);
            }
        }
    }


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
        View view = LayoutInflater.from(mContext).inflate(com.tencent.liteav.demo.play.R.layout.layout_new_vod_snap, null);
        ImageView imageView = (ImageView) view.findViewById(com.tencent.liteav.demo.play.R.id.iv_snap);
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
//                mControllerWindow.hideBackground();
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
                        videoQualities.add(quality);
                    }
                    if (!mDefaultQualitySet) {
                        mVodPlayer.setBitrateIndex(bitrateItems.get(bitrateItems.size() - 1).index); //???????????????????????????
                        mDefaultQualitySet = true;
                    }
                }
                break;
            case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME:
                if (mChangeHWAcceleration) { //?????????????????????????????????seek??????
                    TXCLog.i(TAG, "seek pos:" + mSeekPos);
                    mChangeHWAcceleration = false;
                }
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_END:
                selectBook(true);
                updatePlayState(SuperPlayerConst.PLAYSTATE_PLAYING);
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS_MS);
                int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION_MS);
                if (!mCurrentPlayVideoURL.equals(adUrl) && startProgress != 0) {
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
        switch (event) {
            case TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED: //??????????????????
                updatePlayState(SuperPlayerConst.PLAYSTATE_PLAYING);
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN:
                updatePlayState(SuperPlayerConst.PLAYSTATE_PLAYING);
                if (mWatcher != null) mWatcher.exitLoading();
                break;
            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
            case TXLiveConstants.PLAY_EVT_PLAY_END:
                if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_LIVE_SHIFT) {  // ?????????????????????????????????
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
     * ????????????????????????
     *
     * @return
     */
    public int getPlayMode() {
        return mCurrentPlayMode;
    }


    public void release() {
        if (mControllerWindow != null) {
            mControllerWindow.release();
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

    private IControllerCallback mControllerCallback = new IControllerCallback() {
        @Override
        public void onSwitchPlayMode(int requestPlayMode) {
        }

        @Override
        public void onBackPressed(int playMode) {
        }

        @Override
        public void onFloatPositionChange(int x, int y) {

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
                playVodURL(mCurrentPlayVideoURL);
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

        }

        @Override
        public void onQualityChange(TCVideoQuality quality) {

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

        }

        @Override
        public void onSwitchPlayScale(boolean fullScale) {

        }
    };
    private BookSelectedCallback bookSelectedCallback;

    public void setBookSelectedCallback(BookSelectedCallback bookSelectedCallback) {
        this.bookSelectedCallback = bookSelectedCallback;
    }

    public interface BookSelectedCallback {
        void onBookSelected(int psotion);
    }
}
