package com.tangmu.app.TengKuTV.db;

import android.content.ContentValues;

import com.j256.ormlite.dao.Dao;
import com.tangmu.app.TengKuTV.bean.BookDetailDataBean;
import com.tangmu.app.TengKuTV.bean.VideoDetailBean;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * 用户缓存管理类
 */
public class PlayHistoryManager {

    /**
     * 获取所有音视频信息
     *
     * @return
     */
    public static List<PlayHistoryInfo> getAll() {
        Dao<PlayHistoryInfo, Integer> dao = SqliteHelper.getInstance().getPlayDao();
        try {
            return dao.queryBuilder().orderBy(PlayHistoryDao.COLUMN_B_UPDATE_TIME, false).query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 获取所有音频信息
     * 按时间降序
     *
     * @return
     */
    public static List<PlayHistoryInfo> getAllBook() {
        Dao<PlayHistoryInfo, Integer> dao = SqliteHelper.getInstance().getPlayDao();
        try {
            return dao.queryBuilder().orderBy(PlayHistoryDao.COLUMN_B_UPDATE_TIME, false)
                    .where().eq(PlayHistoryDao.COLUMN_B_TYPE, 2).query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 获取所有视频信息
     *
     * @return
     */
    public static List<PlayHistoryInfo> getAllVideo() {
        Dao<PlayHistoryInfo, Integer> dao = SqliteHelper.getInstance().getPlayDao();
        try {
            return dao.queryBuilder().orderBy(PlayHistoryDao.COLUMN_B_UPDATE_TIME, false)
                    .where().eq(PlayHistoryDao.COLUMN_B_TYPE, 1).query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static boolean deleteAll() {
        Dao<PlayHistoryInfo, Integer> dao = SqliteHelper.getInstance().getPlayDao();
        try {
            List<PlayHistoryInfo> bookHistoryInfos = dao.queryForAll();
            return dao.delete(bookHistoryInfos) == bookHistoryInfos.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 音视频是否存在
     *
     * @param b_id
     * @return
     */
    public static boolean isExisted(int b_id, int type) {
        Dao<PlayHistoryInfo, Integer> dao = SqliteHelper.getInstance().getPlayDao();
        try {
            long count = dao.queryBuilder().where().eq(PlayHistoryDao.COLUMN_B_ID, b_id).and().eq(PlayHistoryDao.COLUMN_B_TYPE, type).countOf();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取音视频
     *
     * @param b_id
     * @return
     */
    public static PlayHistoryInfo getHistory(int b_id, int type) {
        if (isExisted(b_id, type)) {
            Dao<PlayHistoryInfo, Integer> dao = SqliteHelper.getInstance().getPlayDao();
            try {
                return dao.queryBuilder().where().eq(PlayHistoryDao.COLUMN_B_ID, b_id).and()
                        .eq(PlayHistoryDao.COLUMN_B_TYPE, type).queryForFirst();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void save(BookDetailDataBean playHistoryInfo, int position) {
        if (isExisted(playHistoryInfo.getB_id(), 2)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PlayHistoryDao.COLUMN_B_PROGRESS, playHistoryInfo.getProgress());
            contentValues.put(PlayHistoryDao.COLUMN_B_POSITION, position);
            contentValues.put(PlayHistoryDao.COLUMN_ID_ONE, playHistoryInfo.getVt_id_one());
            contentValues.put(PlayHistoryDao.COLUMN_B_UPDATE_TIME, System.currentTimeMillis());
            DBManager.getInstance().updatePlayHistory(playHistoryInfo.getB_id(), 2, contentValues);
        } else
            DBManager.getInstance().savePlayHistory(playHistoryInfo, position);
    }

    public static void save(VideoDetailBean videoDetailBean, int position) {
        if (isExisted(videoDetailBean.getVm_id(), 1)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PlayHistoryDao.COLUMN_B_PROGRESS, videoDetailBean.getProgress());
            contentValues.put(PlayHistoryDao.COLUMN_B_POSITION, position);
            contentValues.put(PlayHistoryDao.COLUMN_ID_ONE, videoDetailBean.getVt_id_one());
            contentValues.put(PlayHistoryDao.COLUMN_VM_TYPE, videoDetailBean.getVm_type());
            contentValues.put(PlayHistoryDao.COLUMN_B_UPDATE_TIME, System.currentTimeMillis());
            DBManager.getInstance().updatePlayHistory(videoDetailBean.getVm_id(), 1, contentValues);
        } else
            DBManager.getInstance().savePlayHistory(videoDetailBean, position);
    }
}