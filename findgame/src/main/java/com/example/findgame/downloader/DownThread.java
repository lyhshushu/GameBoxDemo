package com.example.findgame.downloader;

import com.example.findgame.recommend.controller.OKutil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 4399
 */
public class DownThread extends Thread {

    private File downFile;
    private String downloadUrl;
    private long block;
    private int threadId;
    private long downLength;
    private DownLoadFileTask downLoadFileTask;
    private boolean finish = false;

    public DownThread(DownLoadFileTask downLoadFileTask, int threadId, long block, File file, String downloadUrl, long downLength) {
        this.downFile = file;
        this.block = block;
        this.downloadUrl = downloadUrl;
        this.threadId = threadId;
        this.downLength = downLength;
        this.downLoadFileTask = downLoadFileTask;
    }

    @Override
    public void run() {
        InputStream is = null;
        RandomAccessFile accessFile = null;
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(32, 5, TimeUnit.MINUTES));
        client = builder.build();
        //屏蔽一字节误差
        while (block > downLength && !isFinish()) {
            try {
                long startPos = block * (threadId - 1) + downLength;
                long endPos = block * threadId - 1;
                Request request = new Request.Builder()
                        .url(downloadUrl)
                        .addHeader("RANGE", "bytes=" + startPos + "-" + endPos)
                        .build();
                Response response = client.newCall(request).execute();
                if (request != null) {
                    assert request.body() != null;
                    is = response.body().byteStream();
                    accessFile = new RandomAccessFile(downFile, "rwd");
                    accessFile.seek(startPos);
                    byte[] b = new byte[1024];
                    int offset = 0;
                    while (!downLoadFileTask.getExit() && (offset = is.read(b, 0, 1024)) != -1) {
                        accessFile.write(b, 0, offset);
                        downLength += offset;
                        downLoadFileTask.update(threadId, downLength);
                        downLoadFileTask.append(offset);
                    }
                    accessFile.close();
                    is.close();
                    response.body().close();
                    this.finish = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 线程下载是否完成
     *
     * @return
     */
    public boolean isFinish() {
        return finish;
    }

    /**
     * 返回已经下载长度
     *
     * @return
     */
    public long getDownLength() {
        return downLength;
    }
}
