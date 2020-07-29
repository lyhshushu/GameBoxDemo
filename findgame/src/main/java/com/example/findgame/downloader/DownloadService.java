package com.example.findgame.downloader;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.findgame.R;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 4399
 * 下载服务启动绑定
 */
public class DownloadService extends Service {

    private DownLoadFileTask downLoadFileTask;
    private List<DownLoadFileTask> downLoadFileTasks = new LinkedList<>();
    private List<String> urls = new LinkedList<>();

    private String downloadUrl;

    private final DownloaderListener listener = new DownloaderListener() {
        @Override
        public void onProgress(int id, int progress) {
            getNotificationManager().notify(id + 1, getNotification("下载中：进度", progress));
        }

        @Override
        public void onSuccess(int id) {
            downLoadFileTasks.set(id, null);
//            downLoadFileTask = null;
            stopForeground(true);
            getNotificationManager().notify(id + 1, getNotification("下载成功", -1));
        }

        @Override
        public void onFailed(int id) {
            downLoadFileTasks.set(id, null);
//            downLoadFileTask = null;
            stopForeground(true);
            getNotificationManager().notify(id + 1, getNotification("下载失败", -1));
        }

        @Override
        public void onPaused(int id) {
            downLoadFileTasks.set(id, null);
//            downLoadFileTask = null;
            getNotificationManager().notify(id + 1, getNotification("下载暂停", -1));
        }

        @Override
        public void onCanceled(int id) {
            //数组size-1
            downLoadFileTasks.set(id, null);
//            downLoadFileTask = null;
            stopForeground(true);
            getNotificationManager().notify(id + 1, getNotification("下载取消", -1));
        }
    };


    DownloadBinder downloadBinder = new DownloadBinder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return downloadBinder;
    }

    /**
     * 通知栏管理器
     *
     * @return
     */
    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    String CHANNEL_ONE_ID = "CHANNEL_ONE_ID";
    String CHANNEL_ONE_NAME = "CHANNEL_ONE_ID";
    NotificationChannel notificationChannel = null;

    /**
     * 通知栏
     *
     * @param title
     * @param progress
     * @return
     */
    private Notification getNotification(String title, int progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(notificationChannel);
        }
        Intent[] intents = new Intent[]{
                new Intent().setAction("android.intent.action.MAIN")
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        };
        PendingIntent pi = PendingIntent.getActivities(this, 0, intents, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setChannelId(CHANNEL_ONE_ID);
        builder.setSmallIcon(R.drawable.m4399_png_setting_4399_login_or_bind_logo_nor)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.m4399_png_user_homepage_header_refresh_btn))
                .setContentIntent(pi)
                .setContentTitle(title);
        if (progress > 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }


    //id线程绑定下载？？
    public class DownloadBinder extends Binder {

        public void startDownload(String url, String gameName) {
            if (!urls.contains(url)) {
                downloadUrl = url;
                urls.add(url);
                DownLoadFileTask downLoadFileTask1 = new DownLoadFileTask(getId(url), listener, getApplicationContext(), downloadUrl, gameName);
                downLoadFileTasks.add(downLoadFileTask1);
                String[] strings = {url, gameName};
                downLoadFileTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, strings);
                startForeground(getId(url) + 1, getNotification("请求下载链接中", 0));
            } else {
                downloadUrl = url;
                String[] strings = {url, gameName};
                DownLoadFileTask downLoadFileTask1 = new DownLoadFileTask(getId(url), listener, getApplicationContext(), downloadUrl, gameName);
                downLoadFileTasks.set(getId(url), downLoadFileTask1);
                downLoadFileTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, strings);
                startForeground(getId(url) + 1, getNotification("重新请求下载链接中", 0));
            }
//            if (downLoadFileTask == null) {
//                downloadUrl = url;
//                downLoadFileTask = new DownLoadFileTask(listener);
//                String[] strings = {url, gameName};
//                downLoadFileTask.execute(strings);
//                startForeground(1, getNotification("下载中", 0));
//            }
        }

        public int getId(String url) {
            if (urls.contains(url)) {
                //size，由1开始
                return urls.indexOf(url);
            }
            return -1;
        }

        public void pauseDownload(int id) {
            if (downLoadFileTasks.get(id) != null) {
                downLoadFileTasks.get(id).pauseDownload();
            }
        }

        public void pausedAllDownload() {
            for (int i = 0; i < downLoadFileTasks.size(); i++) {
                if (downLoadFileTasks.get(i) != null) {
                    downLoadFileTasks.get(i).pauseDownload();
                }
            }
        }

        public void cancelDownLoad(int id) {
            if (downLoadFileTask != null) {
                downLoadFileTask.cancelDownload();
            } else {
                if (downloadUrl != null) {
                    //先暂停，后取消 取消下载需将文件删除并通知关闭
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    //需要修改添加包名单独处理
                    File file = new File(directory + fileName);
                    if (file.exists()) {
                        file.delete();
                        getNotificationManager().cancel(id);
                        stopForeground(true);
                    }
                }
            }
        }
    }
}
