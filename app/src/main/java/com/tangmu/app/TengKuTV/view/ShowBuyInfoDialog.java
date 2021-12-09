package com.tangmu.app.TengKuTV.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tangmu.app.TengKuTV.R;

import androidx.appcompat.app.AlertDialog;

public class ShowBuyInfoDialog {

    private AlertDialog alertDialog;
    private TextView vip_price;
    private TextView normal_price;
    private TextView exprie_time;
    private View pay1;
    private View pay2;
    private View pay3;

    public ShowBuyInfoDialog(Activity context) {
        init(context);
    }


    private void init(Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.LoadingDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.buy_antholgy_pop1, null);
        vip_price = (TextView) view.findViewById(R.id.vip_price);
        normal_price = (TextView) view.findViewById(R.id.normal_price);
        exprie_time = (TextView) view.findViewById(R.id.exprie_time);

        pay1 = view.findViewById(R.id.pay1);
        pay2 = view.findViewById(R.id.pay2);
        pay3 = view.findViewById(R.id.pay3);

        alertDialog = builder.setCancelable(true).setView(view).create();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置背景透明度 背景透明
        lp.alpha = 0.9f;//参数为0到1之间。0表示完全透明，1就是不透明。按需求调整参数
        window.setAttributes(lp);
    }

    public void setDismissListener(AlertDialog.OnDismissListener dismissListener) {
        alertDialog.setOnDismissListener(dismissListener);
    }


    public boolean isShowing() {
        return alertDialog.isShowing();
    }


    public void show() {
        if (alertDialog.isShowing()) return;
        alertDialog.show();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置背景透明度 背景透明
        lp.alpha = 0.9f;//参数为0到1之间。0表示完全透明，1就是不透明。按需求调整参数
        window.setAttributes(lp);
    }

    public void dismiss() {
        if (alertDialog.isShowing())
            alertDialog.dismiss();
    }

    public void setPrice(String vm_vip_price, String vm_price, String vm_expire_time) {
        vip_price.setText("VIP特惠价："+vm_vip_price+"元");
        normal_price.setText("非会员价："+vm_price+"元");
        exprie_time.setText("观 看 有 效 期 至："+vm_expire_time);
    }
    public void setOnClickListener(View.OnClickListener clickListener){
        pay1.setOnClickListener(clickListener);
        pay2.setOnClickListener(clickListener);
        pay3.setOnClickListener(clickListener);
    }

}
