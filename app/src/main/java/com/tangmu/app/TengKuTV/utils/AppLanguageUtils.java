package com.tangmu.app.TengKuTV.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Locale;

public class AppLanguageUtils {
    private static HashMap<String, Locale> mAllLanguages = new HashMap<>(7);

    public static Context attachBaseContext(Context paramContext, String paramString) {
        if (Build.VERSION.SDK_INT >= 24) {
            return updateResources(paramContext, paramString);
        } else {
            Resources res = paramContext.getResources();
            Locale localeByLanguage = getLocaleByLanguage(paramString);
            Configuration conf = res.getConfiguration();
            conf.setLocale(localeByLanguage);
            //更新配置
            return paramContext.createConfigurationContext(conf);
        }
//        return paramContext;
    }

    /**
     * 获取当前系统支持的语言集
     */
    public static void getLanguages() {
        Locale[] lg = Locale.getAvailableLocales();
        for (Locale locale : lg) {
            String language = locale.getLanguage();
            //去掉重复的语言
            //去掉国家为空
            if (!TextUtils.isEmpty(locale.getCountry()) && !AppLanguageUtils.mAllLanguages
                    .containsKey(language)) {
                AppLanguageUtils.mAllLanguages.put(language, locale);
            }
        }
    }

    public static void changeAppLanguage(Context paramContext, String paramString) {
        getLanguages();
        Resources resources = paramContext.getResources();
        Configuration localConfiguration = resources.getConfiguration();
        Locale localeByLanguage = getLocaleByLanguage(paramString);
        if (Build.VERSION.SDK_INT >= 24) {
            localConfiguration.setLocale(localeByLanguage);
            LocaleList localeList = new LocaleList(localeByLanguage);
            Locale.setDefault(localeByLanguage);
            LocaleList.setDefault(localeList);
            localConfiguration.setLocales(localeList);
        } else {
            localConfiguration.setLocale(localeByLanguage);
        }
        resources.updateConfiguration(localConfiguration, resources.getDisplayMetrics());
    }

    private static Locale getLocaleByLanguage(String paramString) {
        if (isSupportLanguage(paramString)) {
            return mAllLanguages.get(paramString);
        }
        return Locale.CHINESE;
    }

    public static String getSupportLanguage(String paramString) {
        if (isSupportLanguage(paramString)) {
            return paramString;
        }
        return "zh";
    }

    private static boolean isSupportLanguage(String paramString) {
        return mAllLanguages.containsKey(paramString);
    }

    @TargetApi(24)
    private static Context updateResources(Context paramContext, String paramString) {
        Resources resources = paramContext.getResources();
        Locale locale = getLocaleByLanguage(paramString);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        Locale.setDefault(locale);
        configuration.setLocales(new LocaleList(locale));
        return paramContext.createConfigurationContext(configuration);
    }

    private static Context updateResourcesM(Context paramContext, String paramString) {
        Resources resources = paramContext.getResources();
        Locale locale = getLocaleByLanguage(paramString);
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        return paramContext.createConfigurationContext(configuration);
    }
}