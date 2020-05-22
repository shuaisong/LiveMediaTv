package com.tangmu.app.TengKuTV.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tangmu.app.TengKuTV.R;

import java.util.Locale;

public class LoadingDialog {

    private AlertDialog alertDialog;
    private TextView title, progress;

    public LoadingDialog(Context context) {
        init(context, "");
    }

    public LoadingDialog(Context context, String title_str) {
        init(context, title_str);
    }

    private void init(Context context, String title_str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.LoadingDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null);
        title = view.findViewById(R.id.title);
        progress = view.findViewById(R.id.progress);
        if (!TextUtils.isEmpty(title_str))
            title.setText(title_str);
        alertDialog = builder.setCancelable(false).setView(view).create();
        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER;
        alertDialog.getWindow().setAttributes(attributes);
    }

    public void setCancelAble() {
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
    }

    public void setDismissListener(AlertDialog.OnDismissListener dismissListener) {
        alertDialog.setOnDismissListener(dismissListener);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public boolean isShowing() {
        return alertDialog.isShowing();
    }

    public void show(String title) {
        setTitle(title);
        show();
    }

    public void show() {
        alertDialog.show();
    }

    public void dismiss() {
        if (alertDialog.isShowing())
            alertDialog.dismiss();
    }

    public void setProgress(int fraction) {
        progress.setVisibility(View.VISIBLE);
        progress.setText(String.format(Locale.CHINA, "%d%%", fraction));
    }
}
