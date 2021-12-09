package com.tangmu.app.TengKuTV.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tangmu.app.TengKuTV.R;

import androidx.appcompat.app.AlertDialog;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class MiguPay1Dialog implements View.OnClickListener, View.OnFocusChangeListener {

    private AlertDialog alertDialog;
    private TextView vip_price;
    private TextView phone;
    private TextView pay_sure;
    private TextView pay_cancel;
    private PayAgreeListener payAgreeListener;
    private Activity activity;
    public MiguPay1Dialog(Activity context) {
        init(context);
        activity = context;
    }


    private void init(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.LoadingDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.migu_pay1, null);
        vip_price = (TextView) view.findViewById(R.id.price);
        phone = (TextView) view.findViewById(R.id.phone);
        pay_sure = (TextView) view.findViewById(R.id.pay_sure);
        pay_cancel = (TextView) view.findViewById(R.id.pay_cancel);
        pay_sure.setOnClickListener(this);
        pay_cancel.setOnClickListener(this);
        pay_sure.setOnFocusChangeListener(this);
        pay_cancel.setOnFocusChangeListener(this);
        alertDialog = builder.setCancelable(true).setView(view).create();
        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER;
        alertDialog.getWindow().setAttributes(attributes);
    }

    public void setPayAgreeListener(PayAgreeListener payAgreeListener) {
         this.payAgreeListener = payAgreeListener;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pay_cancel){
            dismiss();
        }else {
            dismiss();
            if (payAgreeListener!=null){
                payAgreeListener.payAgree(16);
            }
        }
    }

    /**
     * Called when the focus state of a view has changed.
     *
     * @param v        The view whose state has changed.
     * @param hasFocus The new focus state of v.
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            v.setBackground(v.getResources().getDrawable(R.drawable.pay_foucse));
        }else {
            v.setBackground(v.getResources().getDrawable(R.drawable.pay_no_foucse));
        }
    }

    public interface PayAgreeListener {
        void payAgree(int type);
    }

    public boolean isShowing() {
        return alertDialog.isShowing();
    }


    public void show() {
        if (alertDialog.isShowing()) return;
        alertDialog.show();
        pay_sure.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pay_sure.requestFocus();
                }
            });
            }
        },400);
        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER;
        attributes.width = AutoSizeUtils.dp2px(alertDialog.getContext(), 500);
        alertDialog.getWindow().setAttributes(attributes);
    }

    public void dismiss() {
        if (alertDialog.isShowing())
            alertDialog.dismiss();
        pay_cancel.clearFocus();
        pay_sure.setBackground(alertDialog.getContext().getResources().getDrawable(R.drawable.pay_foucse));
    }

    public void setPrice(String vm_vip_price  ) {
        vip_price.setText("您正在订购腾酷视频月度VIP（￥"+vm_vip_price+"）");
    }
    public void setSinglePrice(String vm_vip_price  ) {
        vip_price.setText("您正在订购腾酷视频（￥"+vm_vip_price+"）");
    }
    public void setPhone(  String sphone ) {
        phone.setText("支付手机号："+sphone);
    }
}
