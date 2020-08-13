package com.example.findgame.downloader;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private static final int THREAD_NUM = 5;

    private DownloaderListener listener;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;
    private int id;

    private long downloadSize;
    private Map<Integer, Long> data = new ConcurrentHashMap<>();
    private FileServer fileServer;
    private String downloadUrl;
    private String appName;
    private DownThread[] downThreads;
    private Context context;
    private long fileSize;
    private File saveFile;
    private boolean failConnect = false;
    private long block;
    private boolean exit;


    private void setExit(boolean exit) {
        this.exit = exit;
    }

    public boolean getExit() {
        return this.exit;
    }


    /**
     * 累计已下载大小
     *
     * @param size
     */
    protected synchronized void append(long size) {
        downloadSize += size;
        int progress = (int) (downloadSize * 100 / fileSize);
        publishProgress(progress);
    }

    protected synchronized void update(int threadId, Long downLength) {
        this.data.put(threadId, downLength);
        this.fileServer.update(this.downloadUrl, threadId, downLength);
    }


    public DownLoadFileTask(int id, DownloaderListener listener, Context context, String downloadUrl, String appName) {
        this.id = id;
        this.listener = listener;
        this.context = context;
        this.downloadUrl = downloadUrl;
        this.appName = appName;
        fileServer = new FileServer(context);
        downThreads = new DownThread[THREAD_NUM];
        saveFile = new File(context.getExternalFilesDir("download"), appName + ".apk");
    }

    @Override
    protected Integer doInBackground(String... strings) {
        try {
            RandomAccessFile accessFile = new RandomAccessFile(saveFile, "rw");
            if (fileSize > 0) {
                accessFile.setLength(fileSize);
            }
            fileSize = getContentLength(downloadUrl);
            if (fileSize <= 0) {
                return TYPE_FAILED;
            }
            Map<Integer, Long> logData = fileServer.getData(appName);
            if (logData.size() > 0) {
                for (Map.Entry<Integer, Long> entry : logData.entrySet()) {
                    data.put(entry.getKey(), entry.getValue());
                }
            }
            if (data.size() == THREAD_NUM) {
                for (int i = 0; i < downThreads.length; i++) {
                    downloadSize += data.get(i + 1);
                }
            }
            block = (this.fileSize % THREAD_NUM == 0) ? this.fileSize / THREAD_NUM : this.fileSize / THREAD_NUM + 1;
            accessFile.close();
            if (data.size() != THREAD_NUM) {
                data.clear();
                for (int i = 0; i < THREAD_NUM; i++) {
                    data.put(i + 1, (long) 0);
                }
                downloadSize = 0;
            }
            for (int i = 0; i < THREAD_NUM; i++) {
                long downLength = data.get(i + 1);
                if (downLength < block && downloadSize < fileSize) {
                    downThreads[i] = new DownThread(this, i + 1, block, saveFile, downloadUrl, data.get(i + 1));
                    downThreads[i].setPriority(7);
                    downThreads[i].start();
                } else {
                    downThreads[i] = null;
                }
            }
            //获取下载节点完成重置fileServer;
            fileServer.delete(this.appName);
            fileServer.save(appName, data);
            boolean notFinish = true;
            while (notFinish) {
                Thread.sleep(900);
                notFinish = false;
                for (int i = 0; i < THREAD_NUM; i++) {
                    if (this.downThreads[i] != null && !this.downThreads[i].isFinish()) {
                        notFinish = true;
                        //查询下载错误线程
                        if (this.downThreads[i].getDownLength() == -1) {
                            this.downThreads[i] = new DownThread(this, i + 1, block, saveFile, downloadUrl, data.get(i + 1));
                            this.downThreads[i].setPriority(7);
                            this.downThreads[i].start();
                        }
                    }
                }
                if (isPaused) {
                    setExit(true);
                    fileServer.delete(appName);
                    fileServer.save(appName, data);
                    return TYPE_PAUSED;
                }
            }
            if (downloadSize == fileSize) {
                fileServer.delete(appName);
                return TYPE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TYPE_PAUSED;
    }

    //    @Override
//    protected Integer doInBackground(String... strings) {
//        InputStream is = null;
//        RandomAccessFile accessFile = null;
//        File file = null;
//        long downloadedLength = 0;
//        //下载地址
//        String downloadUrl = strings[0];
////        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
////        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
//        file = new File("sdcard/" + strings[1] + ".apk");
//        //文件已存在获取长度
//        if (file.exists()) {
//            downloadedLength = file.length();
//        }
//        try {
//            long contentLength = getContentLength(downloadUrl);
//            if (contentLength == 0 || failConnect) {
//                return TYPE_FAILED;
//            } else if (contentLength == downloadedLength) {
//                //已经下载完成了
//                return TYPE_SUCCESS;
//            }
//
//            //插入分片线程
//
//            OkHttpClient client = new OkHttpClient();
//            //固定分片下载
//            Request request = new Request.Builder()
//                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
//                    .url(downloadUrl)
//                    .build();
//            //获取响应
//            Response response = client.newCall(request).execute();
//            if (response != null) {
//                assert response.body() != null;
//                is = response.body().byteStream();
//                accessFile = new RandomAccessFile(file, "rw");
//                accessFile.seek(downloadedLength);
//                byte[] b = new byte[1024];
//                long total = 0;
//                int len;
//                while ((len = is.read(b)) != -1) {
//                    if (isCanceled) {
//                        return TYPE_CANCELED;
//                    } else if (isPaused) {
//                        return TYPE_PAUSED;
//                    } else {
//                        total += len;
//                        accessFile.write(b, 0, len);
//                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
//                        publishProgress(progress);
//                    }
//                }
//                response.body().close();
//                return TYPE_SUCCESS;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (is != null) {
//                    is.close();
//                }
//                if (accessFile != null) {
//                    accessFile.close();
//                }
//                if (isCanceled && file != null) {
//                    file.delete();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return TYPE_PAUSED;
//    }

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
            listener.onProgress(id, progress);
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
        if (integer != null) {
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
