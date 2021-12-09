package com.tangmu.app.TengKuTV;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

import com.tangmu.app.TengKuTV.utils.LogUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AppStatusService extends Service {

    private ActivityManager activityManager;
    private String className;
    private Timer timer;

    public AppStatusService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public ComponentName startService(Intent intent) {
        activityManager = (ActivityManager) getSystemService(android.content.Context.ACTIVITY_SERVICE);
            // 通过计时器延迟执行
            if (timer==null)
                timer = new Timer();
            timer.schedule(timerTask, 500, 500);
        return super.startService(intent);
    }

    /**
     * 应用是否在前台运行
     *
     * @return true：在前台运行；false：已经被切到后台了
     */
    private boolean isAppOnForeground() {
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (appProcess.processName.equals(getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * 定义一个timerTask来发通知和弹出Toast
     */
    TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            if (!isAppOnForeground()) {
                LogUtil.e("app is hijacked");
            }
        }
    };

    @Override
    public void onDestroy() {
        if (timer!=null){
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }
}