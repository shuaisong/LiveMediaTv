package com.tangmu.app.TengKuTV.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.utils.GlideApp;

import androidx.appcompat.app.AlertDialog;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class MiguPay2Dialog {

    private AlertDialog alertDialog;
    private TextView vip_price;
    private ImageView pay_code;
    private MiguPay1Dialog.PayAgreeListener payAgreeListener;
    public MiguPay2Dialog(Context context) {
        init(context);
    }


    private void init(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.LoadingDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.migu_pay2, null);
        vip_price = (TextView) view.findViewById(R.id.price);
        pay_code = (ImageView) view.findViewById(R.id.pay_code);

        alertDialog = builder.setCancelable(true).setView(view).create();
        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER;
        alertDialog.getWindow().setAttributes(attributes);
    }

    public void setPayAgreeListener(MiguPay1Dialog.PayAgreeListener payAgreeListener) {
         this.payAgreeListener = payAgreeListener;
    }



    public boolean isShowing() {
        return alertDialog.isShowing();
    }


    public void show() {
        if (alertDialog.isShowing()) return;
        alertDialog.show();
        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER;
        attributes.width = AutoSizeUtils.dp2px(alertDialog.getContext(), 400);
        alertDialog.getWindow().setAttributes(attributes);
    }

    public void dismiss() {
        if (alertDialog.isShowing())
            alertDialog.dismiss();
    }

    public void setPrice(String vm_vip_price  ) {
        vip_price.setText("您正在订购腾酷视频月度VIP（￥"+vm_vip_price+"）");
    }
    public void setSinglePrice(String vm_vip_price  ) {
        vip_price.setText("您正在订购腾酷视频（￥"+vm_vip_price+"）");
    }
    public void setImageBitmap(Bitmap bitmap) {
        GlideApp.with(pay_code).load(bitmap).transform(new RoundedCorners(10)).into(pay_code);
    }
}
