package com.tangmu.app.TengKuTV.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.bean.VersionBean;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;

import me.jessyan.autosize.utils.AutoSizeUtils;


public class VersionUpdateDialog implements View.OnClickListener {
    private AlertDialog alertDialog;
    private Context context;
    private VersionBean versionBean;

    public void setPayClickListener(VersionUpdateListener versionUpdateListener) {
        this.versionUpdateListener = versionUpdateListener;
    }

    private VersionUpdateListener versionUpdateListener;

    public VersionUpdateDialog(Context context, VersionBean versionBean) {
        this.versionBean = versionBean;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.LoadingDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.version_update_dialog, null);
        view.findViewById(R.id.ic_close).setOnClickListener(this);
        view.findViewById(R.id.tv_version_update).setOnClickListener(this);
        TextView tvVersionUpdateContent = view.findViewById(R.id.tv_version_update_content);
        TextView tv_version_num = view.findViewById(R.id.tv_version_num);
        tv_version_num.setText("V" + versionBean.getV_code());
        if (!PreferenceManager.getInstance().isDefaultLanguage()) {

            //藏文
            tvVersionUpdateContent.setText(versionBean.getV_content_z());
        } else {
            //中文
            tvVersionUpdateContent.setText(versionBean.getV_content());
        }
        alertDialog = builder.setCancelable(false).setView(view).create();
        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER;
        alertDialog.getWindow().setAttributes(attributes);


    }


    public boolean isShowing() {
        return alertDialog.isShowing();
    }

    public void show() {
        alertDialog.show();
        alertDialog.getWindow().setLayout(AutoSizeUtils.dp2px(context, 450), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void dismiss() {
        if (alertDialog.isShowing())
            alertDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_close:
                dismiss();
                break;
            case R.id.tv_version_update:
                if (versionUpdateListener != null) {
                    versionUpdateListener.versionUpdate(v);
                }
                dismiss();
                break;
        }
    }

    public interface VersionUpdateListener {
        void versionUpdate(View view);
    }
}
