package com.tangmu.app.TengKuTV.db;

/**
 * Created by lenovo on 2020/2/17.
 * auther:lenovo
 * Date：2020/2/17
 */

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tangmu.app.TengKuTV.CustomApp;
import com.tangmu.app.TengKuTV.utils.LogUtil;

/**
 * sqlite数据库辅助类
 */

public class SqliteHelper extends OrmLiteSqliteOpenHelper {

    private final String LOG_TAG = getClass().getSimpleName();

    // 数据库名字
    private static final String DATABASE_NAME = "TengKuTV.db";

    // 版本号
    private static final int DATABASE_VERSION = 2;

    private static SqliteHelper mInstance;

    private Dao<PlayHistoryInfo, Integer> mPlayInfoDao = null;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public synchronized static SqliteHelper getInstance() {
        if (mInstance == null) {
            mInstance = new SqliteHelper(CustomApp.getApp());
        }

        return mInstance;
    }

    /**
     * 创建SQLite数据库
     */
    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, PlayHistoryInfo.class);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Unable to create datbases", e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新SQLite数据库
     */
    @Override
    public void onUpgrade(
            SQLiteDatabase sqliteDatabase,
            ConnectionSource connectionSource,
            int oldVer,
            int newVer) {
        try {
            TableUtils.dropTable(connectionSource, PlayHistoryInfo.class, true);
            onCreate(sqliteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(LOG_TAG,
                    "Unable to upgrade database from version " + oldVer + " to new "
                            + newVer, e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    public Dao<PlayHistoryInfo, Integer> getPlayDao() throws SQLException {
        if (mPlayInfoDao == null) {
            try {
                mPlayInfoDao = getDao(PlayHistoryInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mPlayInfoDao;
    }

}