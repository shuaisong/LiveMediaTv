package com.tangmu.app.TengKuTV.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
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
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Referer", "118.24.243.198")
                .build());
        return GlideApp.with(context).load(glideUrl).diskCacheStrategy(CustomApp.canCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE);
    }
    public static GlideRequest<Drawable> getRequest(Fragment context, String url) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Referer", "118.24.243.198")
                .build());
        return GlideApp.with(context).load(glideUrl).diskCacheStrategy(CustomApp.canCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE);
    }

    public static GlideRequest<Drawable> getRequest(Activity context, String url) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Referer", "118.24.243.198")
                .build());
        return GlideApp.with(context).load(glideUrl).diskCacheStrategy(CustomApp.canCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE);
    }
    public static GlideRequest<Drawable> getRequest(View context, String url) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Referer", "118.24.243.198")
                .build());
        return GlideApp.with(context).load(glideUrl).diskCacheStrategy(CustomApp.canCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE);
    }

    public static void clearMemoryCache(Context context) {
        GlideApp.get(context).clearMemory();
    }

}