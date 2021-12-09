package com.tencent.liteav.demo.play.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SPUtils {
    private static SharedPreferences.Editor editor;
    private SharedPreferences mMSharedPreferences;
    private static final String PREFERENCE_NAME = "saveInfo";
    private static SPUtils spManager;

    private SPUtils(Context cxt) {
        mMSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mMSharedPreferences.edit();
    }

    public static synchronized SPUtils init(Context cxt) {
        if (spManager == null) {
            spManager = new SPUtils(cxt);
        }
        return spManager;
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value).apply();
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value).apply();
    }

    public void putString(String key, String value) {
        editor.putString(key, value).apply();
    }


    public void putFloat(String key, float value) {
        editor.putFloat(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return mMSharedPreferences.getString(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mMSharedPreferences.getBoolean(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return mMSharedPreferences.getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return mMSharedPreferences.getLong(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return mMSharedPreferences.getFloat(key, defaultValue);
    }


    public void exit() {
        editor.remove("login").apply();
    }


    public void putSearchHistory(String key, List<String> list) {
        editor.putStringSet(key, new HashSet<>(list)).apply();
    }

    public List<String> getSearchHistory(String key) {
        Set<String> stringSet = mMSharedPreferences.getStringSet(key, null);
        if (stringSet != null) {
            return new ArrayList<>(stringSet);
        }
        return new ArrayList<String>();
    }

    public void removeSearchHistory(String key) {
        editor.remove(key).apply();
    }

    public void setAccount(ArrayList<String> accounts) {
        editor.putStringSet("accounts", new HashSet<>(accounts)).apply();
    }

    public ArrayList<String> getAccounts() {
        Set<String> accounts = mMSharedPreferences.getStringSet("accounts", new HashSet<String>());
        return new ArrayList<String>(accounts);
    }
}
