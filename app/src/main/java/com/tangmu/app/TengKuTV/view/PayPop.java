package com.tangmu.app.TengKuTV.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.adapter.SingleChoiceAdapter;

public class PayPop implements View.OnClickListener {
    private PopupWindow popupWindow;
    private PayClickListener clickListener;
    private ListView payList;
    private Activity context;
    private View to_pay;

    public PayPop(Activity context, PayClickListener clickListener) {
        this.clickListener = clickListener;
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.pay_sure_pop, null);
        to_pay = view.findViewById(R.id.to_pay);
        to_pay.setOnClickListener(this);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1f);
            }
        });
        payList = view.findViewById(R.id.payList);
        payList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        String[] titles = {context.getString(R.string.pay_wechat), context.getString(R.string.pay_alipay)};
        int[] imgs = {R.mipmap.pay_wechat, R.mipmap.pay_alipay};
        SingleChoiceAdapter adapter = new SingleChoiceAdapter(titles, imgs, context);
        payList.setAdapter(adapter);
        payList.setItemChecked(0, true);
    }


    private void setAlpha(float f) {
        WindowManager.LayoutParams params = context.getWindow().getAttributes();
        params.alpha = f;
        context.getWindow().setAttributes(params);
    }

    public void show(View parent) {
        if (popupWindow == null || popupWindow.isShowing()) return;
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        setAlpha(0.5f);
    }


    @Override
    public void onClick(View v) {
        popupWindow.dismiss();
        clickListener.pay(payList.getCheckedItemPosition());
    }


    public interface PayClickListener {
        void pay(int type);
    }
}
