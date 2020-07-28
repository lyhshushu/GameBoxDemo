package com.example.findgame.downloader;

import android.content.Context;
import android.telephony.mbms.DownloadProgressListener;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 4399 yh.liu
 */
public class FileDownloader {
    private Context context;
    private FileServer fileServer;

    /**
     * 停止下载
     */
    private boolean exit;
    /**
     * 已经下载长度
     */
    private int downloadSize = 0;
    /**
     * 原始文件长度
     */
    private int fileSize = 0;

    private DownloadThread[] threads;
    private File saveFile;
    /**
     * 缓存各线程的下载长度
     */
    private Map<Integer, Long> data = new ConcurrentHashMap<Integer, Long>();
    private int block;
    private String downloadUrl;

    public int getThreadSize() {
        return threads.length;
    }

    public void exit() {
        this.exit = true;
    }

    public boolean getExit() {
        return this.exit;
    }

    public long getFileSize() {
        return fileSize;
    }

    /**
     * 累计已下载大小
     *
     * @param size
     */
    protected synchronized void append(int size) {
        downloadSize += size;
    }

    protected synchronized void update(int threadId, Long downLength) {
        this.data.put(threadId, downLength);
        this.fileServer.update(this.downloadUrl, threadId, downLength);
    }

    public FileDownloader(Context context, String downloadUrl,
                          File fileSaveDir, int threadNum) {
        try {
            this.context = context;
            this.downloadUrl = downloadUrl;
            fileServer = new FileServer(this.context);
            URL url = new URL(this.downloadUrl);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdir();
            }
            this.threads = new DownloadThread[threadNum];
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                assert response.body() != null;
                this.fileSize = (int) response.body().contentLength();
                if (this.fileSize <= 0) {
                    throw new RuntimeException("Unkown file size ");
                }
                this.saveFile = new File("sdcard/" + "hhh.apk");
                Map<Integer, Long> logData = fileServer.getData(downloadUrl);
                if (logData.size() > 0) {
                    for (Map.Entry<Integer, Long> entry : logData.entrySet()) {
                        //各条线程已下载数据长度放入data
                        data.put(entry.getKey(), entry.getValue());
                    }
                }
                if (this.data.size() == this.threads.length) {
                    for (int i = 0; i < this.threads.length; i++) {
                        this.downloadSize += this.data.get(i + 1);
                    }
                }
                this.block = (this.fileSize % this.threads.length == 0) ? this.fileSize / this.threads.length : this.fileSize / this.threads.length + 1;
            } else {
                throw new RuntimeException("server no response ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int download(DownloadProgressListener listener) throws Exception {
        try {
            RandomAccessFile accessFile = new RandomAccessFile(this.saveFile, "rw");
            if (this.fileSize > 0) {
                accessFile.setLength(this.fileSize);
            }
            accessFile.close();
            URL url = new URL(this.downloadUrl);
            if (this.data.size() != this.threads.length) {
                this.data.clear();
                for (int i = 0; i < this.threads.length; i++) {
                    this.data.put(i + 1, (long) 0);
                }
                this.downloadSize = 0;
            }
            for (int i = 0; i < this.threads.length; i++) {
                Long downLength = this.data.get(i + 1);
                if (downLength < this.block
                        && this.downloadSize < this.fileSize) {
                    this.threads[i] = new DownloadThread(this, url,
                            this.saveFile, this.block, this.data.get(i + 1),
                            i + 1);
                    this.threads[i].setPriority(7);
                    this.threads[i].start();
                } else {
                    this.threads[i] = null;
                }
            }
            fileServer.delete(this.downloadUrl);
            fileServer.save(this.downloadUrl, this.data);
            boolean notFinish = true;
            while (notFinish) {
                Thread.sleep(900);
                notFinish = false;
                for (int i = 0; i < this.threads.length; i++) {
                    if (this.threads[i] != null && !this.threads[i].isFinish()) {
                        notFinish = true;
                        if (this.threads[i].getDownLength() == -1) {
                            this.threads[i] = new DownloadThread(this, url,
                                    this.saveFile, this.block,
                                    this.data.get(i + 1), i + 1);
                            this.threads[i].setPriority(7);
                            this.threads[i].start();
                        }
                    }
                }
                if (listener != null) {

                }
            }
            if (downloadSize == this.fileSize) {
                fileServer.delete(this.downloadUrl);// 下载完成删除记录
            }
        } catch (Exception e) {
            throw new Exception("file download error");
        }
        return this.downloadSize;
    }

    /**
     * 获取Http响应头字段
     *
     * @param http
     * @return
     */
    public static Map<String, String> getHttpResponseHeader(
            HttpURLConnection http) {
        Map<String, String> header = new LinkedHashMap<String, String>();
        for (int i = 0; ; i++) {
            String mine = http.getHeaderField(i);
            if (mine == null) {
                break;
            }
            header.put(http.getHeaderFieldKey(i), mine);
        }
        return header;
    }
}
