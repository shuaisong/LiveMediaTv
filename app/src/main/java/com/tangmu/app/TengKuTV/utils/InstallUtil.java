package com.tangmu.app.TengKuTV.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.tangmu.app.TengKuTV.CustomApp;

import java.io.File;

public class InstallUtil {
    public static void installApk(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            File file = (new File(path));
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(CustomApp.getApp(), "com.tangmu.app.TengKuTV.fileProvider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            CustomApp.getApp().startActivity(intent);
        } else {
            intent.setDataAndType(Uri.fromFile(new File(path)),
//            intent.setDataAndType(Uri.parse("file://"+path),
                    "application/vnd.android.package-archive");
            CustomApp.getApp().startActivity(intent);
        }
    }
}
