package com.example.findgame.downloader;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author 4399 yh.liu
 * 三种泛型类型分别代表“启动任务执行的输入参数”、“后台任务执行的进度”、“后台计算结果的类型”
 */
public class DownLoadFileTask extends AsyncTask<String, Integer, Integer> {

    /**
     * 下载状态分类
     */
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private DownloaderListener listener;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;
    private int id;

    public DownLoadFileTask(int id, DownloaderListener listener) {
        this.id = id;
        this.listener = listener;
    }


    @Override
    protected Integer doInBackground(String... strings) {
        InputStream is = null;
        RandomAccessFile accessFile = null;
        File file = null;
        long downloadedLength = 0;
        //下载地址
        String downloadUrl = strings[0];
        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        file = new File("sdcard/" + strings[1] + ".apk");
        //文件已存在获取长度
        if (file.exists()) {
            downloadedLength = file.length();
        }
        try {
            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                return TYPE_FAILED;
            } else if (contentLength == downloadedLength) {
                //已经下载完成了
                return TYPE_SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();
            //固定分片下载
            Request request = new Request.Builder()
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            //获取响应
            Response response = client.newCall(request).execute();
            if (response != null) {
                assert response.body() != null;
                is = response.body().byteStream();
                accessFile = new RandomAccessFile(file, "rw");
                accessFile.seek(downloadedLength);
                byte[] b = new byte[1024];
                long total = 0;
                int len;
                while ((len = is.read(b)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        accessFile.write(b, 0, len);
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (accessFile != null) {
                    accessFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取总长度
     *
     * @param downloadUrl
     * @return
     * @throws IOException
     */
    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            assert response.body() != null;
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }

    /**
     * 刷新下载进度
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(id,progress);
            lastProgress = progress;
        }
    }

    /**
     * 返回最后的下载结果
     *
     * @param integer
     */
    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        switch (integer) {
            case TYPE_SUCCESS:
                listener.onSuccess(id);
                break;
            case TYPE_FAILED:
                listener.onFailed(id);
                break;
            case TYPE_CANCELED:
                listener.onCanceled(id);
                break;
            case TYPE_PAUSED:
                listener.onPaused(id);
                break;
            default:
                break;
        }
    }

    /**
     * 暂停下载
     */
    public void pauseDownload() {
        isPaused = true;
    }

    /**
     * 取消下载
     */
    public void cancelDownload() {
        isCanceled = true;
    }
}
