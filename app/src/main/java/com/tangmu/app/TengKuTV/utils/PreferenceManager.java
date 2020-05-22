package com.tangmu.app.TengKuTV.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.tangmu.app.TengKuTV.bean.LoginBean;
import com.tangmu.app.TengKuTV.bean.UserInfoBean;
import com.tangmu.app.TengKuTV.bean.VisitorBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PreferenceManager {
    /**
     * name of preference
     */
    private static final String PREFERENCE_NAME = "saveInfo";
    private static PreferenceManager mPreferencemManager;
    private static SharedPreferences.Editor editor;
    private SharedPreferences mMSharedPreferences;

    private PreferenceManager(Context cxt) {
        mMSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mMSharedPreferences.edit();
    }

    public static synchronized void init(Context cxt) {
        if (mPreferencemManager == null) {
            mPreferencemManager = new PreferenceManager(cxt);
        }
    }

    /**
     * get instance of PreferenceManager
     *
     * @param
     * @return
     */
    public synchronized static PreferenceManager getInstance() {
        if (mPreferencemManager == null) {
            throw new RuntimeException("please init first!");
        }
        return mPreferencemManager;
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

    public void putVisitor(String value) {
        putString("visitor", value);
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

    public void setUnreadCount(int num) {
        putInt("unread", num);
    }

    public int getUnreadCount() {
        return getInt("unread", 0);
    }


    private void putSearchHistory(String key, List<String> list) {
        editor.putStringSet(key, new HashSet<>(list)).apply();
    }

    private List<String> getSearchHistory(String key) {
        Set<String> stringSet = mMSharedPreferences.getStringSet(key, null);
        if (stringSet != null) {
            return new ArrayList<>(stringSet);
        }
        return new ArrayList<String>();
    }

    private void removeSearchHistory(String key) {
        editor.remove(key).apply();
    }

    public void setBadge(int badge) {
        putInt("badge", badge);
    }

    public int reduceBadge(int num) {
        int badge = getInt("badge", 0);
        if (badge > num) {
            badge -= num;
        } else badge = 0;
        putInt("badge", badge);
        return badge;
    }

    public void setRemember(boolean isRemember) {
        putBoolean("remember", isRemember);
    }

    public boolean isRemember() {
        return getBoolean("remember", true);
    }

    public void setPassword(String passwordStr) {
        putString("password", passwordStr);
    }

    public void setAccount(ArrayList<String> accounts) {
        editor.putStringSet("accounts", new HashSet<>(accounts)).apply();
    }

    public ArrayList<String> getAccounts() {
        Set<String> accounts = mMSharedPreferences.getStringSet("accounts", new HashSet<String>());
        return new ArrayList<String>(accounts);
    }

    public String getPassword() {
        return getString("password", "");
    }


    public boolean isDefaultTheme() {
        return getBoolean("isDefault", true);
    }

    public void setTheme(boolean isDefault) {
        putBoolean("isDefault", isDefault);
    }

    public void setLogin(LoginBean login) {
        Gson gson = new Gson();
        String json = gson.toJson(login, LoginBean.class);
        putString("login", json);
    }

    @Nullable
    public LoginBean getLogin() {
        Gson gson = new Gson();
        String login = getString("login", "");
        if (TextUtils.isEmpty(login)) {
            return null;
        } else {
            return gson.fromJson(login, LoginBean.class);
        }
    }

    public void setVisitor(VisitorBean visitor) {
        Gson gson = new Gson();
        String json = gson.toJson(visitor, VisitorBean.class);
        putString("visitor", json);
    }

    @Nullable
    public VisitorBean getVisitor() {
        Gson gson = new Gson();
        String login = getString("visitor", "");
        if (TextUtils.isEmpty(login)) {
            return null;
        } else {
            return gson.fromJson(login, VisitorBean.class);
        }
    }

    public boolean isDefaultLanguage() {
        return getBoolean("isDefaultLanguage", true);
    }

    public void setDefaultLanguage(boolean isDefault) {
        putBoolean("isDefaultLanguage", isDefault);
    }

    public boolean getCanCache() {
        return getBoolean("canCache", false);
    }

    public void setCanCache(boolean canCache) {
        putBoolean("canCache", canCache);
    }

    public List<String> getVideoSearch() {
        return getSearchHistory("video_search");
    }

    public void setVideoSearch(List<String> data) {
        putSearchHistory("video_search", data);
    }

    public void removeVideoHistory() {
        removeSearchHistory("video_search");
    }

    public List<String> getBookSearch() {
        return getSearchHistory("book_search");
    }

    public void setBookSearch(List<String> data) {
        putSearchHistory("book_search", data);
    }

    public void removeBookHistory() {
        removeSearchHistory("book_search");
    }

    public List<String> getLiveSearch() {
        return getSearchHistory("live_search");
    }

    public void setLiveSearch(List<String> data) {
        putSearchHistory("live_search", data);
    }

    public void removeLiveHistory() {
        removeSearchHistory("live_search");
    }

    public void updateLogin(UserInfoBean userInfoBean) {
        LoginBean login = PreferenceManager.getInstance().getLogin();
        if (login != null) {
            login.setU_vip_status(userInfoBean.getU_vip_status());
            if (login.getU_vip_status() == 1) {
                login.setU_vip_expire(userInfoBean.getU_vip_expire());
            }
            setLogin(login);
        }
    }

    public void setDefaultQuality(int indexOfQuality) {
        putInt("DefaultQuality", indexOfQuality);
    }

    public int getDefaultQuality() {
        return getInt("DefaultQuality", 1);
    }

    public void setIsJump(boolean jump) {
        putBoolean("jump", jump);
    }

    public boolean getIsJump() {
        return getBoolean("jump", true);
    }

    public void setDubbingId(int vt_id) {
        putInt("DubbingId", vt_id);
    }

    public int getDubbingId() {
        return
                getInt("DubbingId", 0);
    }
}
