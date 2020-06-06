package com.tangmu.app.TengKuTV;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetReceiver extends BroadcastReceiver {
    private NetChangeListener netChangeListener;

    public void setNetChangeListener(NetChangeListener netChangeListener) {
        this.netChangeListener = netChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (netChangeListener == null) return;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean available;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) available = false;
        else
            available = activeNetworkInfo.isAvailable();
        if (available) {
            netChangeListener.onNetChange(true);
        } else {
            netChangeListener.onNetChange(false);
        }
    }

    public interface NetChangeListener {
        void onNetChange(boolean hasNet);
    }
}
