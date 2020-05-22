package com.tangmu.app.TengKuTV.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tangmu.app.TengKuTV.CustomApp;

public class GlideUtils {
    public static void initImageWithFileCache(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .dontAnimate()
                .centerCrop()
                .into(imageView);
    }

    public static void initImageNoCache(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .skipMemoryCache(true)
                .dontAnimate()
                .centerCrop()
                .into(imageView);
    }

    public static GlideRequest<Drawable> getRequest(Context context, String url) {
        return GlideApp.with(context).load(url).diskCacheStrategy(CustomApp.canCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE);
    }
    public static GlideRequest<Drawable> getRequest(Fragment context, String url) {
        return GlideApp.with(context).load(url).diskCacheStrategy(CustomApp.canCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE);
    }

    public static GlideRequest<Drawable> getRequest(Activity context, String url) {
        return GlideApp.with(context).load(url).diskCacheStrategy(CustomApp.canCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE);
    }
    public static GlideRequest<Drawable> getRequest(View context, String url) {
        return GlideApp.with(context).load(url).diskCacheStrategy(CustomApp.canCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE);
    }

    public static void clearMemoryCache(Context context) {
        GlideApp.get(context).clearMemory();
    }

}