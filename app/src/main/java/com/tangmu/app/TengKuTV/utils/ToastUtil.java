package com.tangmu.app.TengKuTV.utils;

import android.widget.Toast;

import com.tangmu.app.TengKuTV.CustomApp;


public class ToastUtil {
    private static Toast toast;

    public static void showText(String text) {
        if (toast == null)
            toast = Toast.makeText(CustomApp.getApp(), text, Toast.LENGTH_SHORT);
        else toast.setText(text);
        toast.show();
    }
}
