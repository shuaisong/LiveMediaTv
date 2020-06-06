package com.tangmu.app.TengKuTV.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tangmu.app.TengKuTV.R;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class ShowBuyInfoDialog {

    private AlertDialog alertDialog;
    private TextView vip_price;
    private TextView normal_price;
    private TextView exprie_time;
    private ImageView pay_code;

    public ShowBuyInfoDialog(Context context) {
        init(context);
    }


    private void init(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.LoadingDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.buy_antholgy_pop, null);
        vip_price = (TextView) view.findViewById(R.id.vip_price);
        normal_price = (TextView) view.findViewById(R.id.normal_price);
        vip_price = (TextView) view.findViewById(R.id.vip_price);
        exprie_time = (TextView) view.findViewById(R.id.exprie_time);
        pay_code = (ImageView) view.findViewById(R.id.pay_code);

        alertDialog = builder.setCancelable(true).setView(view).create();
        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER;
        alertDialog.getWindow().setAttributes(attributes);
    }

    public void setDismissListener(AlertDialog.OnDismissListener dismissListener) {
        alertDialog.setOnDismissListener(dismissListener);
    }


    public boolean isShowing() {
        return alertDialog.isShowing();
    }


    public void show() {
        if (alertDialog.isShowing()) return;
        pay_code.setImageResource(R.color.white);
        alertDialog.show();
        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER;
        alertDialog.getWindow().setAttributes(attributes);
    }

    public void dismiss() {
        if (alertDialog.isShowing())
            alertDialog.dismiss();
    }

    public void setPrice(String vm_price, String vm_expire_time) {
        vip_price.setText(String.valueOf(Double.valueOf(vm_price) / 2));
        normal_price.setText(vm_price);
        exprie_time.setText(vm_expire_time);
    }

    public void setCode(String code) {
        byte[] decode = Base64.decode(code.substring(code.indexOf(",") + 1).getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        pay_code.setImageBitmap(bitmap);
    }
}
