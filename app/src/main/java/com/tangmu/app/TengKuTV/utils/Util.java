package com.tangmu.app.TengKuTV.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.JsonParseException;
import com.lzy.okgo.exception.HttpException;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.bean.HomeChildBean;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;
import com.tangmu.app.TengKuTV.bean.HomeDubbingBean;
import com.tangmu.app.TengKuTV.bean.HomeDubbingRecBean;
import com.tangmu.app.TengKuTV.bean.TitleBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Util {
    public static int String_length(String value) {
        float valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 1;
            } else {
                valueLength += 0.5f;
            }
        }
        return (int) valueLength;
    }

    public static File createImgFile(Context context) {
        final String PATTERN = "yyyyMMddHHmmss";
        String timeStamp = new SimpleDateFormat(PATTERN, Locale.CHINA).format(new Date());

        String externalStorageState = Environment.getExternalStorageState();

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "jiangong");

        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return new File(dir, timeStamp + ".jpg");
        } else {
            File cacheDir = context.getCacheDir();
            return new File(cacheDir, timeStamp + ".jpg");
        }

    }

    public static File saveImgFile(Bitmap bitmap) {
        StringBuffer stringBuffer = new StringBuffer("img_").append(System.currentTimeMillis()).append(".jpg");
        File file =
                new File(CustomApp.getApp().getFilesDir().getPath() + File.separator + "img", stringBuffer.toString());
        if (file.exists()) file.delete();
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static File createVideoFile(Context context) {
        final String PATTERN = "yyyyMMddHHmmss";
        String timeStamp = new SimpleDateFormat(PATTERN, Locale.CHINA).format(new Date());

        String externalStorageState = Environment.getExternalStorageState();

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "jiangong");

        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return new File(dir, timeStamp + ".mp4");
        } else {
            File cacheDir = context.getCacheDir();
            return new File(cacheDir, timeStamp + ".mp4");
        }

    }

    public static int string_over_index(String value, int max) {
        int index = 0;
        float valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 1;
            } else {
                valueLength += 0.5f;
            }
            if (valueLength >= max) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static void setTitleAndTime(int textWidth, TextView titleView, String title, TextView timeView, String time) {
        TextPaint paint = titleView.getPaint();
        timeView.setText(time);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) timeView.getLayoutParams();
        if (paint.measureText(title) < textWidth - paint.measureText(time)) {
            layoutParams.topToBottom = -1;
            layoutParams.bottomToBottom = titleView.getId();
            titleView.setText(title);
        } else if (paint.measureText(title) <= textWidth) {
            layoutParams.topToBottom = titleView.getId();
            layoutParams.bottomToBottom = -1;
            titleView.setText(title);
        } else if (paint.measureText(title) < 2 * textWidth - paint.measureText(time)) {
            layoutParams.topToBottom = -1;
            layoutParams.bottomToBottom = titleView.getId();
            titleView.setText(title);
        } else {
            layoutParams.topToBottom = -1;
            layoutParams.bottomToBottom = titleView.getId();
            for (int i = title.length() - 1; i > 0; i--) {
                if (paint.measureText(title, 0, i) <= 2 * textWidth - paint.measureText(time)) {
                    titleView.setText(String.format("%s...", title.substring(0, i - 3)));
                    break;
                }
            }
        }
        timeView.setLayoutParams(layoutParams);
    }

    public static boolean isToday(long aLong) {
        Date date = new Date(aLong);
        Calendar today = Calendar.getInstance();
        Date today_date = new Date(System.currentTimeMillis());
        today.setTime(today_date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (today.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
            int diffDay = today.get(Calendar.DAY_OF_YEAR)
                    - cal.get(Calendar.DAY_OF_YEAR);
            return diffDay == 0;
        }
        return false;
    }

    public static void setIssueTitle(TextView view, String s, int lineWidth, int marginEnd) {
        TextPaint paint = view.getPaint();
        float v = paint.measureText(s);
        if (v > 2 * lineWidth - marginEnd) {
            int endLength = (int) paint.measureText("...全文");
            for (int i = 30; i < s.length(); i++) {
                if (paint.measureText(s, 0, i) >= 2 * lineWidth - marginEnd - endLength) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(s.substring(0, i)).append("...全文");
                    SpannableString spannableString = new SpannableString(stringBuilder);
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
                    ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(Color.parseColor("#2095FB"));
                    spannableString.setSpan(foregroundColorSpan, 0, spannableString.length() - 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(foregroundColorSpan1, spannableString.length() - 2, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    view.setText(spannableString);
                    break;
                }
            }
        } else {
            SpannableString spannableString = new SpannableString(s);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
            spannableString.setSpan(foregroundColorSpan, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            view.setText(spannableString);
        }
    }

    public static void setIssueTitle(TextView view, String s, int maxNum) {
        if (s.length() > maxNum) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(s.substring(0, maxNum - 1)).append("...全文");
            SpannableString spannableString = new SpannableString(stringBuilder);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
            ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(Color.parseColor("#2095FB"));
            spannableString.setSpan(foregroundColorSpan, 0, spannableString.length() - 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(foregroundColorSpan1, spannableString.length() - 2, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            view.setText(spannableString);
        } else {
            SpannableString spannableString = new SpannableString(s);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
            spannableString.setSpan(foregroundColorSpan, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            view.setText(spannableString);
        }
    }

    public static long computeFolderSize(final File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.exists()) return 0;
        long dirSize = 0;
        final File[] files = dir.listFiles();
        if (null != files) {
            for (final File file : files) {
                if (file.isFile()) {
                    dirSize += file.length();
                } else if (file.isDirectory()) {
                    dirSize += computeFolderSize(file);
                }
            }
        }
        return dirSize;
    }

    /**
     * Delete a specified directory.
     *
     * @param dir       the directory to clean.
     * @param removeDir true to remove the {@code dir}.
     */
    public static void deleteDir(final File dir, final boolean removeDir) {
        if (!dir.exists()) return;
        if (dir.isDirectory()) {
            final File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (final File file : files) {
                    if (file.isDirectory()) {
                        deleteDir(file, removeDir);
                    } else {
                        boolean delete = file.delete();
                        if (!delete)
                            Log.d("deleteDir", file.getAbsolutePath() + "删除失败");
                    }
                }
            }
            if (removeDir) {
                dir.delete();
            }
        }
    }

    public static String handleError(Throwable exception) {
        if (exception instanceof UnknownHostException || exception instanceof ConnectException) {
            return "网络连接失败,请检查网络!";
        }
        if (exception instanceof SocketTimeoutException) {
            return "网络请求超时!请重试!";
        }
        if (exception instanceof HttpException) {
            return "服务器无响应";
        }
        if (exception instanceof JsonParseException) {
            return "数据解析失败";
        }
        LogUtil.e(exception.getMessage());
        return "未知错误";
    }

    public static boolean isImg(String s) {
        return s.endsWith(".jpg") || s.endsWith(".png") || s.endsWith(".jpeg");
    }


    public static File saveFile(Bitmap bitmap, String path) {
        File file = new File(path);
        if (file.exists()) file.delete();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


    /**
     * 给图片加水印
     * 右下角
     *
     * @param src       原图
     * @param watermark 水印
     * @return 加水印的原图
     */
    public static Bitmap waterMask(Bitmap src, Bitmap watermark) {
        int w = src.getWidth();
        int h = src.getHeight();
        Log.i("waterMask", "原图宽: " + w);
        Log.i("waterMask", "原图高: " + h);

        //根据bitmap缩放水印图片
        float w1 = w / 15;
        //获取原始水印图片的宽、高
        int w2 = watermark.getWidth();
        int h2 = watermark.getHeight();

        //计算缩放的比例
        float scale = w1 / w2;

        Matrix matrix1 = new Matrix();
        matrix1.postScale(scale, scale);

        watermark = Bitmap.createBitmap(watermark, 0, 0, w2, h2, matrix1, true);
        //获取新的水印图片的宽、高
        w2 = watermark.getWidth();
        h2 = watermark.getHeight();

        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(result);
        //在canvas上绘制原图和新的水印图
        cv.drawBitmap(src, 0, 0, null);
        //水印图绘制在画布的右下角，距离右边和底部都为20
        cv.drawBitmap(watermark, src.getWidth() - w2 - 20, src.getHeight() - h2 - 20, null);
        cv.save();
        cv.restore();

        return result;
    }


    public static String convertBalanceTime(int time) {
        StringBuffer stringBuffer = new StringBuffer();
        long h = time / 3600;
        long m = (time % 3600) / 60;
//        long s = (time % 3600) % 60;
        if (h < 10) {
            stringBuffer.append("0");
        }
        stringBuffer.append(h).append("小时");
        if (m < 10) {
            stringBuffer.append("0");
        }
        stringBuffer.append(m).append("分");
    /*    if (s < 10) {
            stringBuffer.append("0");
        }
        stringBuffer.append(s);*/
        return stringBuffer.toString();
    }

    public static String convertVideoTime(long time) {
        StringBuffer stringBuffer = new StringBuffer();
        time /= 1000;
        long m = time / 60;
        long s = time % 60;
        if (m < 10) {
            stringBuffer.append("0");
        }
        stringBuffer.append(m).append(":");
        if (s < 10) {
            stringBuffer.append("0");
        }
        stringBuffer.append(s);
        return stringBuffer.toString();
    }


    public static String getTime(long messageTime) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - messageTime > 3 * 24 * 60 * 60 * 1000) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            return format.format(new Date(messageTime));
        } else if (currentTimeMillis - messageTime >= 24 * 60 * 60 * 1000) {
            return (currentTimeMillis - messageTime) / (24 * 60 * 60 * 1000) + "天前";
        } else if (currentTimeMillis - messageTime >= 60 * 60 * 1000) {
            return (currentTimeMillis - messageTime) / (60 * 60 * 1000) + "小时前";
        } else if (currentTimeMillis - messageTime >= 60 * 1000) {
            return (currentTimeMillis - messageTime) / (60 * 1000) + "分前";
        } else
            return "刚刚";
    }

    public static boolean isNotificationEnabled(Context context) {
        boolean isOpened = false;
        try {
            isOpened = NotificationManagerCompat.from(context).areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOpened;
    }


    public static void setNodata(View noWork, View noNet, boolean isNoNet) {
        if (isNoNet) {
            noNet.setVisibility(View.VISIBLE);
            noWork.setVisibility(View.GONE);
        } else {
            noNet.setVisibility(View.GONE);
            noWork.setVisibility(View.VISIBLE);
        }
    }

    public static boolean isellipsis(String title, TextView des) {
        TextPaint paint = des.getPaint();
        return paint.measureText(title) > des.getMeasuredWidth();
    }

    public static String convertImgPath(String url) {
        if (url.startsWith("http")) return url;
        return Constant.Pic_IP + url;
    }

    public static String showText(String vm_title, String vm_title_z) {
        if (PreferenceManager.getInstance().isDefaultLanguage()) {
            return vm_title;
        } else return vm_title_z;
    }

    public static List<HomeChildBean> convertHomeRecommendVideo(List<HomeChildRecommendBean> result) {
        ArrayList<HomeChildBean> homeChildBeans = new ArrayList<>();
        if (result.isEmpty()) return homeChildBeans;
        int titleSize = 0;
        for (HomeChildRecommendBean homeChildRecommendBean : result) {
            TitleBean titleBean = new TitleBean();
            titleBean.setS_id(homeChildRecommendBean.getS_id());
            titleBean.setS_title(homeChildRecommendBean.getS_title());
            titleBean.setS_title_z(homeChildRecommendBean.getS_title_z());
            if (titleSize > 1) {
                titleBean.setIsMoreType(true);
            }
            HomeChildBean homeChildBean = new HomeChildBean();
            homeChildBean.setTitleBean(titleBean);
            homeChildBeans.add(homeChildBean);
            titleSize++;
            List<HomeChildRecommendBean.VideoBean> video = homeChildRecommendBean.getVideo();
            if (video != null && !video.isEmpty())
                for (HomeChildRecommendBean.VideoBean videoBean : video) {
                    homeChildBean = new HomeChildBean();
                    videoBean.setS_sorts(titleBean.getS_id());
                    homeChildBean.setMovieBean(videoBean);
                    homeChildBeans.add(homeChildBean);
                }
        }
        return homeChildBeans;
    }

    public static boolean isWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) CustomApp.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static ArrayList<HomeDubbingBean> convertHomeRecommendDubbing(List<HomeDubbingRecBean> result) {
        ArrayList<HomeDubbingBean> homeChildBeans = new ArrayList<>();
        if (result.isEmpty()) return homeChildBeans;
        for (HomeDubbingRecBean homeChildRecommendBean : result) {
            TitleBean titleBean = new TitleBean();
            titleBean.setS_title(homeChildRecommendBean.getS_title());
            titleBean.setS_title_z(homeChildRecommendBean.getS_title_z());
            HomeDubbingBean homeChildBean = new HomeDubbingBean();
            homeChildBean.setTitleBean(titleBean);
            homeChildBeans.add(homeChildBean);
            List<HomeDubbingRecBean.VideoBean> video = homeChildRecommendBean.getVideo();
            if (video != null && !video.isEmpty())
                for (HomeDubbingRecBean.VideoBean videoBean : video) {
                    homeChildBean = new HomeDubbingBean();
                    homeChildBean.setMovieBean(videoBean);
                    homeChildBeans.add(homeChildBean);
                }
        }
        return homeChildBeans;
    }

    public static String convertRecordTime(int currentSeconds) {
        StringBuffer stringBuffer = new StringBuffer();

        if (currentSeconds < 10) {
            stringBuffer.append("00:0").append(currentSeconds);
        } else if (currentSeconds < 60) {
            stringBuffer.append("00:").append(currentSeconds);
        } else {
            int m = currentSeconds / 60;
            int s = currentSeconds % 60;
            if (m < 10) {
                stringBuffer.append("0").append(m).append(":");
            } else {
                stringBuffer.append(m);
            }
            if (s < 10) {
                stringBuffer.append("0").append(s);
            } else stringBuffer.append(s);
        }
        return stringBuffer.toString();
    }

    public static String convertVideoPath(String dv_url_video) {
        if (dv_url_video.startsWith("http")) return dv_url_video;
        else
            return Constant.Video_IP + dv_url_video;
    }

    public static String convertBookTime(String bd_times) {
        try {
            Integer integer = Integer.valueOf(bd_times);
            StringBuffer stringBuffer = new StringBuffer();
            int m = integer / 60;
            int s = integer % 60;
            if (m < 10) {
                stringBuffer.append("0");
            }
            stringBuffer.append(m).append(":");
            if (s < 10) {
                stringBuffer.append("0");
            }
            stringBuffer.append(s);
            return stringBuffer.toString();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "00:00";
    }

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm", Locale.CHINA);

    public static String convertSystemTime(long currentTimeMillis) {
        return simpleDateFormat.format(currentTimeMillis);
    }

    public static void saveLog(int keyCode, String s) throws IOException {
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory() + "/errorLog";
        } else {
            path = CustomApp.getApp().getCacheDir().getPath() + "/errorLog";
        }
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(path + File.separator + "errorLog.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write("keyCode:" + keyCode + " currentFocus:" + s);
        fileWriter.close();
    }
}