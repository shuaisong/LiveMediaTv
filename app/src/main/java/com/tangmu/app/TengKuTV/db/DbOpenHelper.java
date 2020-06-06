/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tangmu.app.TengKuTV.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tangmu.app.TengKuTV.utils.LogUtil;

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static DbOpenHelper instance;

    private static final String PLAY_TABLE_CREATE = "CREATE TABLE "
            + PlayHistoryDao.TABLE_NAME + " ("
            + PlayHistoryDao.COLUMN_B_TITLE + " TEXT, "
            + PlayHistoryDao.COLUMN_B_TITLE_Z + " TEXT, "
            + PlayHistoryDao.COLUMN_B_IMG + " TEXT, "
            + PlayHistoryDao.COLUMN_B_PROGRESS + " INTEGER, "
            + PlayHistoryDao.COLUMN_B_POSITION + " INTEGER, "
            + PlayHistoryDao.COLUMN_B_UPDATE_TIME + " LONG, "
            + PlayHistoryDao.COLUMN_VM_TYPE + " INTEGER, "
            + PlayHistoryDao.COLUMN_B_TYPE + " INTEGER, "
            + PlayHistoryDao.COLUMN_ID_ONE + " INTEGER, "
            + PlayHistoryDao.COLUMN_B_ID + " INTEGER, "
            + PlayHistoryDao.COLUMN_IS_VIP + " INTEGER, "
            + PlayHistoryDao.COLUMN_UPDATE_NUM + " INTEGER, "
            + PlayHistoryDao.COLUMN_UPDATE_STATUS + " INTEGER, "
            + PlayHistoryDao.COLUMN_ID + " INTEGER PRIMARY KEY);";

    private DbOpenHelper(Context context) {
        super(context, getUserDatabaseName(), null, DATABASE_VERSION);
    }

    public static DbOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    private static String getUserDatabaseName() {
        return "TengKuTV.db";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PLAY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }

}
