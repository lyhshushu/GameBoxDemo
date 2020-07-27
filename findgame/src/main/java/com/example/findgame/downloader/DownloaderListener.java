package com.example.findgame.downloader;

/**
 * @author 4399 yh.liu
 * 监听接口
 */
public interface DownloaderListener {

    void onProgress(int id, int progress);

    void onSuccess(int id);

    void onFailed(int id);

    void onPaused(int id);

    void onCanceled(int id);
}
