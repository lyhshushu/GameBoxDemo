package com.example.findgame.downloader;

import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * @author 4399 yh.liu
 * 下载loader
 */
public class LSSDownloader {
    private static String download(String url, String savePath) {
        String result = null;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            File file = new File("sdcard/cache.apk");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
//                file.getParentFile().createNewFile();
            }
            BufferedSink sink = Okio.buffer((Okio.sink(file)));
            sink.writeAll(response.body().source());
            sink.flush();
            sink.close();
            result = savePath;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }
}
