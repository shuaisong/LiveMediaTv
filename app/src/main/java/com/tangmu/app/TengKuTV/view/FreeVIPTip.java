package com.tangmu.app.TengKuTV.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.tangmu.app.TengKuTV.R;


public class FreeVIPTip {
    private Context context;
    private final PopupWindow popupWindow;

    public FreeVIPTip(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.free_vip_tip, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(false);
    }

    public void show(View parent) {
        if (popupWindow == null || popupWindow.isShowing()) return;
        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.END, 50, 50);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    public boolean isShowing() {
        return popupWindow.isShowing();
    }
}
