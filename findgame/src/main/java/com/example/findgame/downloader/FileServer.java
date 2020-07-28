package com.example.findgame.downloader;

import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库操作封装
 *
 * @author 4399 yh.liu
 */
public class FileServer {
    private DownloadIdDateBaseHelper dbHelper;

    public FileServer(Context context) {
        dbHelper = new DownloadIdDateBaseHelper(context);
    }

    /**
     * 获取每条线程目前的下载大小，数据库选择id和下载长度
     *
     * @param path
     * @return
     */
    public Map<Integer, Long> getData(String path) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select threadid, downlength from filedownlog where downpath=?",
                new String[]{path});
        Map<Integer, Long> data = new HashMap<Integer, Long>();
        while (cursor.moveToNext()) {
            data.put(cursor.getInt(0), cursor.getLong(1));
        }
        cursor.close();
        db.close();
        return data;
    }

    /**
     * 保存线程id，path，length
     *
     * @param path
     * @param map
     */
    public void save(String path, Map<Integer, Long> map) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Map.Entry<Integer, Long> entry : map.entrySet()) {
                db.execSQL(
                        "insert into filedownlog(downpath, threadid, downlength) values(?,?,?)",
                        new Object[]{path, entry.getKey(), entry.getValue()});
            }
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    /**
     * 更新database
     *
     * @param path
     * @param threadId
     * @param pos
     */
    public void update(String path, int threadId, long pos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update filedownlog set downlength=? where downpath=? and threadid=?",
                new Object[]{pos, path, threadId});
        db.close();
    }

    /**
     * 提供给下载完成之后的删除
     *
     * @param path
     */
    public void delete(String path) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from filedownlog where downpath=?",
                new Object[]{path});
        db.close();
    }
}
