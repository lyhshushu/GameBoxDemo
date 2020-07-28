package com.example.findgame.downloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadThread extends Thread {
    private File saveFile;
    private URL downUrl;
    private long block;
    /**
     * 下载起始位置
     */
    private int threadId = -1;
    private long downLength;
    private boolean isFinish = false;
    private FileDownloader downloader;

    public DownloadThread(FileDownloader downloader, URL downUrl,
                          File saveFile, long block, long downLength, int threadId) {
        this.downUrl = downUrl;
        this.saveFile = saveFile;
        this.block = block;
        this.downloader = downloader;
        this.threadId = threadId;
        this.downLength = downLength;
    }

    @Override
    public void run() {
        InputStream is = null;
        RandomAccessFile accessFile = null;
        if (downLength < block) {
            try {
                long startPos = block * (threadId - 1) + downLength;
                long endPos = block * threadId - 1;
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .addHeader("RANGE", "bytes=" + startPos + "-" + endPos)
                        .url(downUrl)
                        .build();
                Response response = client.newCall(request).execute();
                if (response != null) {
                    assert response.body() != null;
                    is = response.body().byteStream();
                    accessFile = new RandomAccessFile(this.saveFile, "rwd");
                    accessFile.seek(startPos);
                    byte[] b = new byte[1024];
                    int offset = 0;
                    while (!downloader.getExit() && (offset = is.read(b, 0, 1024)) != -1) {
                        accessFile.write(b, 0, offset);
                        downLength += offset;
                        downloader.update(this.threadId, downLength);
                        downloader.append(offset);
                    }
                    accessFile.close();
                    is.close();
                    response.body().close();
                    this.isFinish = true;
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
        return isFinish;
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
