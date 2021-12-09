package com.tangmu.app.TengKuTV.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tangmu.app.TengKuTV.bean.MiguLoginBean;
import com.tangmu.app.TengKuTV.bean.VisitorBean;
import com.tencent.liteav.demo.play.utils.SPUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PreferenceManager {
    /**
     * name of preference
     */
//    private static final String PREFERENCE_NAME = "saveInfo";
    private static PreferenceManager mPreferencemManager;
    private final SPUtils spUtils;
    //    private static SharedPreferences.Editor editor;
//    private SharedPreferences mMSharedPreferences;

    private PreferenceManager(Context cxt) {
//        mMSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
//        editor = mMSharedPreferences.edit();
        spUtils = SPUtils.init(cxt);
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


    public void putVisitor(String value) {
        spUtils.putString("visitor", value);
    }

    public void setUnreadCount(int num) {
        spUtils.putInt("unread", num);
    }

    public int getUnreadCount() {
        return spUtils.getInt("unread", 0);
    }

    public void setBadge(int badge) {
        spUtils.putInt("badge", badge);
    }

    public int reduceBadge(int num) {
        int badge = spUtils.getInt("badge", 0);
        if (badge > num) {
            badge -= num;
        } else badge = 0;
        spUtils.putInt("badge", badge);
        return badge;
    }

    public void setRemember(boolean isRemember) {
        spUtils.putBoolean("remember", isRemember);
    }

    public boolean isRemember() {
        return spUtils.getBoolean("remember", true);
    }

    public void setPassword(String passwordStr) {
        spUtils.putString("password", passwordStr);
    }

    public String getPassword() {
        return spUtils.getString("password", "");
    }


    public boolean isDefaultTheme() {
        return spUtils.getBoolean("isDefault", true);
    }

    public void setTheme(boolean isDefault) {
        spUtils.putBoolean("isDefault", isDefault);
    }

    public void setLogin(MiguLoginBean login) {
        Gson gson = new Gson();
        String json = gson.toJson(login, MiguLoginBean.class);
        spUtils.putString("login", json);
    }

    @NonNull
    public MiguLoginBean getLogin() {
        Gson gson = new Gson();
        String login = spUtils.getString("login", "");
        return gson.fromJson(login, MiguLoginBean.class);
    }

    public void setVisitor(VisitorBean visitor) {
        Gson gson = new Gson();
        String json = gson.toJson(visitor, VisitorBean.class);
        spUtils.putString("visitor", json);
    }

    @Nullable
    public VisitorBean getVisitor() {
        Gson gson = new Gson();
        String login = spUtils.getString("visitor", "");
        if (TextUtils.isEmpty(login)) {
            return null;
        } else {
            return gson.fromJson(login, VisitorBean.class);
        }
    }

    public boolean isDefaultLanguage() {
        return spUtils.getBoolean("isDefaultLanguage", true);
    }

    public void setDefaultLanguage(boolean isDefault) {
        spUtils.putBoolean("isDefaultLanguage", isDefault);
    }

    public boolean getCanCache() {
        return spUtils.getBoolean("canCache", false);
    }

    public void setCanCache(boolean canCache) {
        spUtils.putBoolean("canCache", canCache);
    }

    public List<String> getVideoSearch() {
        return spUtils.getSearchHistory("video_search");
    }

    public void setVideoSearch(List<String> data) {
        spUtils.putSearchHistory("video_search", data);
    }

    public void removeVideoHistory() {
        spUtils.removeSearchHistory("video_search");
    }

    public List<String> getBookSearch() {
        return spUtils.getSearchHistory("book_search");
    }

    public void setBookSearch(List<String> data) {
        spUtils.putSearchHistory("book_search", data);
    }

    public void removeBookHistory() {
        spUtils.removeSearchHistory("book_search");
    }

    public List<String> getLiveSearch() {
        return spUtils.getSearchHistory("live_search");
    }

    public void setLiveSearch(List<String> data) {
        spUtils.putSearchHistory("live_search", data);
    }

    public void removeLiveHistory() {
        spUtils.removeSearchHistory("live_search");
    }

    public void updateLogin(MiguLoginBean userInfoBean) {
        setLogin(userInfoBean);
    }

    public void setDefaultQuality(int indexOfQuality) {
        spUtils.putInt("DefaultQuality", indexOfQuality);
    }

    public int getDefaultQuality() {
        return spUtils.getInt("DefaultQuality", 1);
    }

    public void setIsJump(boolean jump) {
        spUtils.putBoolean("jump", jump);
    }

    public boolean getIsJump() {
        return spUtils.getBoolean("jump", true);
    }

    public void setDubbingId(int vt_id) {
        spUtils.putInt("DubbingId", vt_id);
    }

    public int getDubbingId() {
        return
                spUtils.getInt("DubbingId", 0);
    }

    public void exit() {
        spUtils.exit();
    }

    public void setTuid(int tu_id) {
        spUtils.putInt("tu_id",tu_id);
    }
    public int getTuid() {
        return spUtils.getInt("tu_id",0);
    }
    public String getToken() {
        return spUtils.getString("token","");
    }
    public void setToken(String epgToken) {
        spUtils.putString("token",epgToken);
    }

    public void setUserName(String epgUserId) {
        spUtils.putString("userName",epgUserId);
    }
    public String getUserName() {
        return spUtils.getString("userName","");
    }
}

