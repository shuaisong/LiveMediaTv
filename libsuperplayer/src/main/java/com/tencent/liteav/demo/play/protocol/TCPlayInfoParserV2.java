package com.tencent.liteav.demo.play.protocol;

import android.util.Log;

import com.tencent.liteav.basic.log.TXCLog;
import com.tencent.liteav.demo.play.bean.TCPlayImageSpriteInfo;
import com.tencent.liteav.demo.play.bean.TCPlayInfoStream;
import com.tencent.liteav.demo.play.bean.TCPlayKeyFrameDescInfo;
import com.tencent.liteav.demo.play.bean.TCResolutionName;
import com.tencent.liteav.demo.play.bean.TCVideoClassification;
import com.tencent.liteav.demo.play.utils.TCVideoQualityUtil;
import com.tencent.liteav.demo.play.bean.TCVideoQuality;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * V2视频信息协议解析实现类
 * <p>
 * 负责解析V2视频信息协议请求响应的Json数据
 */
public class TCPlayInfoParserV2 implements IPlayInfoParser {
    private static final String TAG = "TCPlayInfoParserV2";

    private JSONObject mResponse;                      // 协议请求返回的Json数据

    //播放器配置信息
    private String mDefaultVideoClassification;    // 默认视频清晰度名称
    private List<TCVideoClassification> mVideoClassificationList;       // 视频清晰度信息列表

    private TCPlayImageSpriteInfo mImageSpriteInfo;               // 雪碧图信息
    private List<TCPlayKeyFrameDescInfo> mKeyFrameDescInfo;              // 关键帧打点信息
    //视频信息
    private String mName;                          // 视频名称
    private TCPlayInfoStream mSourceStream;                  // 源视频流信息
    private TCPlayInfoStream mMasterPlayList;                // 主播放视频流信息
    private LinkedHashMap<String, TCPlayInfoStream> mTranscodePlayList;             // 转码视频信息列表

    private String mUrl;                           // 视频播放url
    private List<TCVideoQuality> mVideoQualityList;              // 视频画质信息列表
    private TCVideoQuality mDefaultVideoQuality;           // 默认视频画质

    public TCPlayInfoParserV2(JSONObject response) {
        mResponse = response;
        parsePlayInfo();
    }

    /**
     * 从视频信息协议请求响应的Json数据中解析出视频信息
     * <p>
     * 解析流程：
     * <p>
     * 1、解析播放器信息(playerInfo)字段，获取视频清晰度列表{@link #mVideoClassificationList}以及默认清晰度{@link #mDefaultVideoClassification}
     * <p>
     * 2、解析雪碧图信息(imageSpriteInfo)字段，获取雪碧图信息{@link #mImageSpriteInfo}
     * <p>
     * 3、解析关键帧信息(keyFrameDescInfo)字段，获取关键帧信息{@link #mKeyFrameDescInfo}
     * <p>
     * 4、解析视频信息(videoInfo)字段，获取视频名称{@link #mName}、源视频信息{@link #mSourceStream}、
     * 主视频列表{@link #mMasterPlayList}、转码视频列表{@link #mTranscodePlayList}
     * <p>
     * 5、从主视频列表、转码视频列表、源视频信息中解析出视频播放url{@link #mUrl}、画质信息{@link #mVideoQualityList}、
     * 默认画质{@link #mDefaultVideoQuality}
     */
    private void parsePlayInfo() {
        try {
            JSONObject playerInfo = mResponse.optJSONObject("playerInfo");
            if (playerInfo != null) {
//                mDefaultVideoClassification = parseDefaultVideoClassification(playerInfo);
                mDefaultVideoClassification = "HD";
                mVideoClassificationList = parseVideoClassificationList(playerInfo);
            }
            JSONObject imageSpriteInfo = mResponse.optJSONObject("imageSpriteInfo");
            if (imageSpriteInfo != null) {
                mImageSpriteInfo = parseImageSpriteInfo(imageSpriteInfo);
            }
            JSONObject keyFrameDescInfo = mResponse.optJSONObject("keyFrameDescInfo");
            if (keyFrameDescInfo != null) {
                mKeyFrameDescInfo = parseKeyFrameDescInfo(keyFrameDescInfo);
            }
            JSONObject videoInfo = mResponse.optJSONObject("videoInfo");
            if (videoInfo != null) {
                mName = parseName(videoInfo);
                mSourceStream = parseSourceStream(videoInfo);
                mMasterPlayList = parseMasterPlayList(videoInfo);
                mTranscodePlayList = parseTranscodePlayList(videoInfo);
            }
            parseVideoInfo();
        } catch (JSONException e) {
            TXCLog.e(TAG, Log.getStackTraceString(e));
        }
    }

    /**
     * 解析默认视频清晰度信息
     *
     * @param playerInfo 包含默认视频清晰度信息的Json对象
     * @return 默认视频清晰度名称字符串
     */
    private String parseDefaultVideoClassification(JSONObject playerInfo) throws JSONException {
        return playerInfo.getString("defaultVideoClassification");
    }

    /**
     * 解析视频清晰度信息
     *
     * @param playerInfo 包含默认视频类别信息的Json对象
     * @return 视频清晰度信息数组
     */
    private List<TCVideoClassification> parseVideoClassificationList(JSONObject playerInfo) throws JSONException {
        List<TCVideoClassification> arrayList = new ArrayList<>();
        JSONArray videoClassificationArray = playerInfo.getJSONArray("videoClassification");
        if (videoClassificationArray != null) {
            for (int i = 0; i < videoClassificationArray.length(); i++) {
                JSONObject object = videoClassificationArray.getJSONObject(i);

                TCVideoClassification classification = new TCVideoClassification();
                classification.setId(object.getString("id"));
                classification.setName(object.getString("name"));

                List<Integer> definitionList = new ArrayList<>();
                JSONArray array = object.getJSONArray("definitionList");
                if (array != null) {
                    for (int j = 0; j < array.length(); j++) {
                        int definition = array.getInt(j);
                        definitionList.add(definition);
                    }
                }
                classification.setDefinitionList(definitionList);
                arrayList.add(classification);
            }
        }
        return arrayList;
    }

    /**
     * 解析雪碧图信息
     *
     * @param imageSpriteInfo 包含雪碧图信息的Json对象
     * @return 雪碧图信息对象
     */
    private TCPlayImageSpriteInfo parseImageSpriteInfo(JSONObject imageSpriteInfo) throws JSONException {
        JSONArray imageSpriteList = imageSpriteInfo.getJSONArray("imageSpriteList");
        if (imageSpriteList != null) {
            JSONObject spriteJSONObject = imageSpriteList.getJSONObject(imageSpriteList.length() - 1); //获取最后一个来解析
            TCPlayImageSpriteInfo info = new TCPlayImageSpriteInfo();
            info.webVttUrl = spriteJSONObject.getString("webVttUrl");
            JSONArray jsonArray = spriteJSONObject.getJSONArray("imageUrls");
            List<String> imageUrls = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String url = jsonArray.getString(i);
                imageUrls.add(url);
            }
            info.imageUrls = imageUrls;
            return info;
        }
        return null;
    }

    /**
     * 解析关键帧打点信息
     *
     * @param keyFrameDescInfo 包含关键帧信息的Json对象
     * @return 关键帧信息数组
     */
    private List<TCPlayKeyFrameDescInfo> parseKeyFrameDescInfo(JSONObject keyFrameDescInfo) throws JSONException {
        JSONArray jsonArr = keyFrameDescInfo.getJSONArray("keyFrameDescList");
        if (jsonArr != null) {
            List<TCPlayKeyFrameDescInfo> infoList = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {
                String content = jsonArr.getJSONObject(i).getString("content");
                long time = jsonArr.getJSONObject(i).getLong("timeOffset");
                float timeS = (float) (time / 1000.0);//转换为秒
                TCPlayKeyFrameDescInfo info = new TCPlayKeyFrameDescInfo();
                try {
                    info.content = URLDecoder.decode(content, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    info.content = "";
                }
                info.time = timeS;
                infoList.add(info);
            }
            return infoList;
        }
        return null;
    }

    /**
     * 解析视频名称
     *
     * @param videoInfo 包含视频名称信息的Json对象
     * @return 视频名称字符串
     * @throws JSONException
     */
    private String parseName(JSONObject videoInfo) throws JSONException {
        JSONObject basicInfo = videoInfo.getJSONObject("basicInfo");
        if (basicInfo != null) {
            return basicInfo.getString("name");
        }
        return null;
    }

    /**
     * 解析源视频流信息
     *
     * @param videoInfo 包含源视频流信息的Json对象
     * @return 源视频流信息对象
     */
    private TCPlayInfoStream parseSourceStream(JSONObject videoInfo) throws JSONException {
        JSONObject sourceVideo = videoInfo.getJSONObject("sourceVideo");
        if (sourceVideo != null) {
            TCPlayInfoStream stream = new TCPlayInfoStream();
            stream.url = sourceVideo.getString("url");
            stream.duration = sourceVideo.getInt("duration");
            stream.width = sourceVideo.getInt("width");
            stream.height = sourceVideo.getInt("height");
            stream.size = sourceVideo.getInt("size");
            stream.bitrate = sourceVideo.getInt("bitrate");
            return stream;
        }
        return null;
    }

    /**
     * 解析主播放视频流信息
     *
     * @param videoInfo 包含主播放视频流信息的Json对象
     * @return 主播放视频流信息对象
     */
    private TCPlayInfoStream parseMasterPlayList(JSONObject videoInfo) throws JSONException {
        if (!videoInfo.has("masterPlayList"))
            return null;
        JSONObject masterPlayList = videoInfo.getJSONObject("masterPlayList");
        if (masterPlayList != null) {
            TCPlayInfoStream stream = new TCPlayInfoStream();
            stream.url = masterPlayList.getString("url");
            return stream;
        }
        return null;
    }

    /**
     * 解析转码视频流信息
     * <p>
     * 转码视频流信息{@link #mTranscodePlayList}中不包含清晰度名称，需要与视频清晰度信息{@link #mVideoClassificationList}做匹配
     *
     * @param videoInfo 包含转码视频流信息的Json对象
     * @return 转码视频信息列表 key: 清晰度名称 value: 视频流信息
     */
    private LinkedHashMap<String, TCPlayInfoStream> parseTranscodePlayList(JSONObject videoInfo) throws JSONException {
        List<TCPlayInfoStream> transcodeList = parseStreamList(videoInfo);
        if (transcodeList == null) return mTranscodePlayList;
        for (int i = 0; i < transcodeList.size(); i++) {
            TCPlayInfoStream stream = transcodeList.get(i);
            // 匹配清晰度
            if (mVideoClassificationList != null) {
                for (int j = 0; j < mVideoClassificationList.size(); j++) {
                    TCVideoClassification classification = mVideoClassificationList.get(j);
                    List<Integer> definitionList = classification.getDefinitionList();
                    if (definitionList.contains(stream.definition)) {
                        stream.id = classification.getId();
                        stream.name = classification.getName();
                    }
                }
            }
        }
        //清晰度去重
        LinkedHashMap<String, TCPlayInfoStream> idList = new LinkedHashMap<>();
        for (int i = 0; i < transcodeList.size(); i++) {
            TCPlayInfoStream stream = transcodeList.get(i);
            if (!idList.containsKey(stream.id)) {
                idList.put(stream.id, stream);
            } else {
                TCPlayInfoStream copy = idList.get(stream.id);
                if (copy.getUrl().endsWith("mp4")) {  // 列表中url是mp4，则进行下一步
                    continue;
                }
                if (stream.getUrl().endsWith("mp4")) { // 新判断的url是mp4，则替换列表中
                    idList.remove(copy.id);
                    idList.put(stream.id, stream);
                }
            }
        }
        //按清晰度排序
        return idList;
    }

    /**
     * 解析转码视频信息
     *
     * @param videoInfo 包含转码视频信息的Json对象
     * @return 转码视频是信息数组
     */
    private List<TCPlayInfoStream> parseStreamList(JSONObject videoInfo) throws JSONException {
        List<TCPlayInfoStream> streamList = new ArrayList<>();
        JSONArray transcodeList = videoInfo.optJSONArray("transcodeList");
        if (transcodeList != null) {
            for (int i = 0; i < transcodeList.length(); i++) {
                JSONObject transcode = transcodeList.getJSONObject(i);
                TCPlayInfoStream stream = new TCPlayInfoStream();
                stream.url = transcode.getString("url");
                stream.duration = transcode.getInt("duration");
                stream.width = transcode.getInt("width");
                stream.height = transcode.getInt("height");
                stream.size = transcode.getInt("size");
                stream.bitrate = transcode.getInt("bitrate");
                stream.definition = transcode.getInt("definition");
                streamList.add(stream);
            }
        }
        return streamList;
    }

    /**
     * 解析视频播放url、画质列表、默认画质
     * <p>
     * V2协议响应Json数据中可能包含多个视频播放信息：主播放视频信息{@link #mMasterPlayList}、转码视频{@link #mTranscodePlayList}、
     * 源视频{@link #mSourceStream}, 播放优先级依次递减
     * <p>
     * 从优先级最高的视频信息中解析出播放信息
     */
    private void parseVideoInfo() {
        //有主播放视频信息时，从中解析出支持多码率播放的url
        if (mMasterPlayList != null) {
            mUrl = mMasterPlayList.getUrl();
            return;
        }
        //无主播放信息，从转码视频信息中解析出各码流信息
        if (mTranscodePlayList != null && mTranscodePlayList.size() != 0) {
            TCPlayInfoStream stream = mTranscodePlayList.get(mDefaultVideoClassification);
            String videoURL = null;
            if (stream != null) {
                videoURL = stream.getUrl();
            } else {
                for (TCPlayInfoStream stream1 : mTranscodePlayList.values()) {
                    if (stream1 != null && stream1.getUrl() != null) {
                        stream = stream1;
                        videoURL = stream1.getUrl();
                        break;
                    }
                }
            }
            if (videoURL != null) {
                mVideoQualityList = TCVideoQualityUtil.convertToVideoQualityList(mTranscodePlayList);
                mDefaultVideoQuality = TCVideoQualityUtil.convertToVideoQuality(stream);
                mUrl = videoURL;
                return;
            }
        }
        //无主播放信息、转码信息，从源视频信息中解析出播放信息
        if (mSourceStream != null) {
            if (mDefaultVideoClassification != null) {
                mDefaultVideoQuality = TCVideoQualityUtil.convertToVideoQuality(mSourceStream, mDefaultVideoClassification);
                mVideoQualityList = new ArrayList<>();
                mVideoQualityList.add(mDefaultVideoQuality);
            }
            mUrl = mSourceStream.getUrl();
        }
    }

    /**
     * 获取视频播放url
     *
     * @return url字符串
     */
    @Override
    public String getUrl() {
        return mUrl;
    }

    /**
     * 获取视频名称
     *
     * @return 视频名称字符串
     */
    @Override
    public String getName() {
        return mName;
    }

    /**
     * 获取雪碧图信息
     *
     * @return 雪碧图信息对象
     */
    @Override
    public TCPlayImageSpriteInfo getImageSpriteInfo() {
        return mImageSpriteInfo;
    }

    /**
     * 获取关键帧信息
     *
     * @return 关键帧信息数组
     */
    @Override
    public List<TCPlayKeyFrameDescInfo> getKeyFrameDescInfo() {
        return mKeyFrameDescInfo;
    }

    /**
     * 获取画质信息
     *
     * @return 画质信息数组
     */
    @Override
    public List<TCVideoQuality> getVideoQualityList() {
        return mVideoQualityList;
    }

    /**
     * 获取默认画质信息
     *
     * @return 默认画质信息对象
     */
    @Override
    public TCVideoQuality getDefaultVideoQuality() {
        return mDefaultVideoQuality;
    }

    /**
     * 获取视频画质别名列表
     *
     * @return 画质别名数组
     */
    @Override
    public List<TCResolutionName> getResolutionNameList() {
        return null;
    }
}
