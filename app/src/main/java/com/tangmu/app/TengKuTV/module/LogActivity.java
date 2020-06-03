package com.tangmu.app.TengKuTV.module;

import android.os.Environment;
import android.text.TextUtils;
import android.widget.TextView;

import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.component.AppComponent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;

public class LogActivity extends BaseActivity {
    @BindView(R.id.content)
    TextView content;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initData() {
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory() + "/errorLog/errorLog.txt";
        } else {
            path = CustomApp.getApp().getCacheDir().getPath() + "/errorLog/errorLog.txt";
        }
        if (!new File(path).exists()) {
            content.setText("file not exist");
            return;
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    readLog(path);
//                } catch (IOException e) {
//                    content.setText(e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    private void readLog(String path) throws IOException {
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputreader = new InputStreamReader(fileInputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        //分行读取
        while ((line = buffreader.readLine()) != null) {
            stringBuffer.append(line).append("\n");
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(stringBuffer)) {
                    content.setText("not log");
                } else
                    content.setText(stringBuffer);
            }
        });
        fileInputStream.close();
        inputreader.close();
        buffreader.close();
    }

    @Override
    protected void initView() {

    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_log_1;
    }


}
