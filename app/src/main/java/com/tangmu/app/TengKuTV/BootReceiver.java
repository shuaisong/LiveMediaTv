package com.tangmu.app.TengKuTV;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tangmu.app.TengKuTV.module.main.MainActivity;
import com.tangmu.app.TengKuTV.utils.LogUtil;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtil.d("onReceive: " + action);
//        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
//            Intent intent1 = new Intent(context, MainActivity.class);
//            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent1);
//        }
    }
}
