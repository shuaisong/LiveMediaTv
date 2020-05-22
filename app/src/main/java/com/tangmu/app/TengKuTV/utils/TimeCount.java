package com.tangmu.app.TengKuTV.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.tangmu.app.TengKuTV.R;

import java.util.Locale;


/**
 * 验证码发送计时器
 */
public class TimeCount extends CountDownTimer {
    private TextView textView;


    public TimeCount(long millisInFuture, long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setClickable(false);
        textView.setText(String.format(Locale.CHINA, "%d%s", millisUntilFinished / 1000, "s"));
    }

    @Override
    public void onFinish() {
        textView.setText(textView.getContext().getString(R.string.get_verify));
        textView.setClickable(true);
    }

}