package com.tencent.liteav.demo.play.utils;

import com.tencent.liteav.demo.play.bean.TCPlayInfoStream;
import com.tencent.liteav.demo.play.bean.TCResolutionName;
import com.tencent.liteav.demo.play.bean.TCVideoQuality;
import com.tencent.rtmp.TXBitrateItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yuejiaoli on 2018/7/6.
 * <p>
 * 清晰度转换工具
 */

public class TCVideoQualityUtil {

    /**
     * 从比特流信息转换为清晰度信息
     *
     * @param bitrateItem
     * @return
     */
    public static TCVideoQuality convertToVideoQuality(TXBitrateItem bitrateItem, int index) {
        TCVideoQuality quality = new TCVideoQuality();
        quality.bitrate = bitrateItem.bitrate;
        quality.index = bitrateItem.index;

        switch (index) {
            case 0:
                quality.name = "FLU";
                quality.title = "流畅";
                break;
            case 1:
                quality.name = "SD";
                quality.title = "标清270P";
                break;
            case 2:
                quality.name = "HD";
                quality.title = "高清480P";
                break;
            case 3:
                quality.name = "FHD";
                quality.title = "超清720P";
                break;
            case 4:
                quality.name = "2K";
                quality.title = "2K";
                break;
            case 5:
                quality.name = "4K";
                quality.title = "4K";
                break;
            case 6:
                quality.name = "8K";
                quality.title = "8K";
                break;
        }
        return quality;
    }

    /**
     * 从源视频信息与视频类别信息转换为清晰度信息
     *
     * @param sourceStream
     * @param classification
     * @return
     */
    public static TCVideoQuality convertToVideoQuality(TCPlayInfoStream sourceStream, String classification) {
        TCVideoQuality quality = new TCVideoQuality();
        quality.bitrate = sourceStream.getBitrate();

        if (classification.equals("FLU")) {
            quality.name = "FLU";
            quality.title = "流畅";
            quality.index = 0;
        } else if (classification.equals("SD")) {
            quality.name = "SD";
            quality.title = "标清270";
            quality.index = 1;
        } else if (classification.equals("HD")) {
            quality.name = "HD";
            quality.title = "高清480";
            quality.index = 2;
        } else if (classification.equals("FHD")) {
            quality.name = "FHD";
            quality.title = "超清720";
            quality.index = 3;
        } else if (classification.equals("2K")) {
            quality.name = "2K";
            quality.title = "2K";
            quality.index = 4;
        } else if (classification.equals("4K")) {
            quality.name = "4K";
            quality.title = "4K";
            quality.index = 5;
        }
        quality.url = sourceStream.url;
        return quality;
    }

    /**
     * 从{@link TCPlayInfoStream}转换为{@link TCVideoQuality}
     *
     * @param stream
     * @return
     */
    public static TCVideoQuality convertToVideoQuality(TCPlayInfoStream stream) {
        TCVideoQuality qulity = new TCVideoQuality();
        qulity.bitrate = stream.getBitrate();
        qulity.name = stream.id;
        qulity.title = stream.name;
        qulity.url = stream.url;
        qulity.index = -1;
        return qulity;
    }

    /**
     * 从转码列表转换为清晰度列表
     *
     * @param transcodeList
     * @return
     */
    public static List<TCVideoQuality> convertToVideoQualityList(HashMap<String, TCPlayInfoStream> transcodeList) {
        List<TCVideoQuality> videoQulities = new ArrayList<>();
        for (String classification : transcodeList.keySet()) {
            TCVideoQuality videoQulity = convertToVideoQuality(transcodeList.get(classification));
            videoQulities.add(videoQulity);
        }
        return videoQulities;
    }

    /**
     * 根据视频清晰度别名表从码率信息转换为视频清晰度
     *
     * @param bitrateItem     码率
     * @param resolutionNames 清晰度别名表
     * @return
     */
    public static TCVideoQuality convertToVideoQuality(TXBitrateItem bitrateItem, List<TCResolutionName> resolutionNames) {
        TCVideoQuality quality = new TCVideoQuality();
        quality.bitrate = bitrateItem.bitrate;
        quality.index = bitrateItem.index;
        int minEdge = bitrateItem.width < bitrateItem.height ? bitrateItem.width : bitrateItem.height;
        for (TCResolutionName resolutionName : resolutionNames) {
            if (resolutionName.minEdgeLength >= minEdge) {
                quality.title = resolutionName.name;
                break;
            }
        }
        return quality;
    }
}
