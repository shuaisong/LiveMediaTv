package com.tangmu.app.TengKuTV.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.style.ImageSpan;
import android.view.View;

import androidx.annotation.NonNull;

public class ClickImgSpan extends ImageSpan {
    public ClickImgSpan(@NonNull Context context, @NonNull Bitmap bitmap) {
        super(context, bitmap);
    }

    public void onClick(View v) {
        LogUtil.d("");
    }
}
