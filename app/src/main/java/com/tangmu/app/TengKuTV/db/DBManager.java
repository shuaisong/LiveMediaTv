package com.tangmu.app.TengKuTV.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.bean.BookDetailDataBean;
import com.tangmu.app.TengKuTV.bean.VideoDetailBean;

public class DBManager {
    static private DBManager dbMgr = new DBManager();
    private DbOpenHelper dbHelper;

    private DBManager() {
        dbHelper = DbOpenHelper.getInstance(CustomApp.getApp().getApplicationContext());
    }

    public static synchronized DBManager getInstance() {
        if (dbMgr == null) {
            dbMgr = new DBManager();
        }
        return dbMgr;
    }


    /**
     * delete a video
     */
    synchronized public void deletePlayHistory(int id, int type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(PlayHistoryDao.TABLE_NAME, PlayHistoryDao.COLUMN_B_ID + " = ? and " + PlayHistoryDao.COLUMN_B_TYPE + " = ?"
                    , new String[]{String.valueOf(id), String.valueOf(type)});
        }
    }


    /**
     * update video
     *
     * @param id
     * @param values
     */
    synchronized public void updatePlayHistory(int id, int type, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.update(PlayHistoryDao.TABLE_NAME, values, PlayHistoryDao.COLUMN_B_ID + " = ? and " +
                            PlayHistoryDao.COLUMN_B_TYPE + " = ?"
                    , new String[]{String.valueOf(id), String.valueOf(type)});
        }
    }


    /**
     * save a video
     *
     * @param playHistoryInfo
     */
    synchronized public void savePlayHistory(VideoDetailBean playHistoryInfo, int position) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PlayHistoryDao.COLUMN_B_ID, playHistoryInfo.getVm_id());
        values.put(PlayHistoryDao.COLUMN_B_TITLE, playHistoryInfo.getVm_title());
        values.put(PlayHistoryDao.COLUMN_B_TITLE_Z, playHistoryInfo.getVm_title_z());
        values.put(PlayHistoryDao.COLUMN_B_IMG, playHistoryInfo.getVm_img());
        values.put(PlayHistoryDao.COLUMN_B_PROGRESS, playHistoryInfo.getProgress());
        values.put(PlayHistoryDao.COLUMN_B_POSITION, position);
        values.put(PlayHistoryDao.COLUMN_ID_ONE, playHistoryInfo.getVt_id_one());
        values.put(PlayHistoryDao.COLUMN_B_TYPE, 1);
        values.put(PlayHistoryDao.COLUMN_VM_TYPE, playHistoryInfo.getVm_type());
        values.put(PlayHistoryDao.COLUMN_B_UPDATE_TIME, System.currentTimeMillis());
        if (db.isOpen()) {
            db.replace(PlayHistoryDao.TABLE_NAME, null, values);
        }
    }

    /**
     * save a book
     *
     * @param playHistoryInfo
     */
    synchronized public void savePlayHistory(BookDetailDataBean playHistoryInfo, int position) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PlayHistoryDao.COLUMN_B_ID, playHistoryInfo.getB_id());
        values.put(PlayHistoryDao.COLUMN_B_TITLE, playHistoryInfo.getB_title());
        values.put(PlayHistoryDao.COLUMN_B_TITLE_Z, playHistoryInfo.getB_title_z());
        values.put(PlayHistoryDao.COLUMN_B_IMG, playHistoryInfo.getB_img());
        values.put(PlayHistoryDao.COLUMN_B_PROGRESS, playHistoryInfo.getProgress());
        values.put(PlayHistoryDao.COLUMN_B_POSITION, position);
        values.put(PlayHistoryDao.COLUMN_B_TYPE, 2);
        values.put(PlayHistoryDao.COLUMN_ID_ONE, playHistoryInfo.getVt_id_one());
        values.put(PlayHistoryDao.COLUMN_B_UPDATE_TIME, System.currentTimeMillis());
        if (db.isOpen()) {
            db.replace(PlayHistoryDao.TABLE_NAME, null, values);
        }
    }
}
