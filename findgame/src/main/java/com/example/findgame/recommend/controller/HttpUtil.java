package com.example.findgame.recommend.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.StaticLayout;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.example.findgame.bean.GameInfBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import okio.Sink;

public class HttpUtil {


    private static final String TAG = "HttpUtil";
    private OkHttpClient okHttpClient;


    private HttpUtil() {

        //okhttp消息拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor interceptor = new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request build = chain.request().newBuilder().header("token", "mkbk").build();
                return chain.proceed(build);
            }
        };

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();

    }

    private static HttpUtil httpUtil = null;

    public static HttpUtil getInstance() {
        if (httpUtil == null) {
            synchronized (Object.class) {
                if (httpUtil == null) {
                    httpUtil = new HttpUtil();
                }
            }
        }
        return httpUtil;
    }

    public void doGet(String url, final OKutil oKutil) {
        Log.i(TAG, "doGet: ");
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                oKutil.onNo(e.getMessage());
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.i(TAG, "onResponse: ----------------------------");
                oKutil.onOk(response.body().string());
            }
        });
    }

    public void doPost(String url, HashMap<String, String> map, OKutil listener) {
        FormBody.Builder builder = new FormBody.Builder();
        Set<Map.Entry<String, String>> entrise = map.entrySet();

        for (Map.Entry<String, String> entry : entrise) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.add(key, value);
        }
        FormBody body = builder.build();

        Request builder1 = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(builder1);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public void doDownload(String url, final String path, final MvcListener linstener) {

        Request builder = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = okHttpClient.newCall(builder);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                linstener.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                long max = body.contentLength();

                InputStream inputStream = body.byteStream();
                FileOutputStream fileOutputStream = new FileOutputStream(path);
                byte[] bytes = new byte[1024];
                long count = 0;
                int len = 0;
                while ((len = inputStream.read(bytes)) != -1) {
                    count += len;
                    fileOutputStream.write(bytes, 0, len);
                    linstener.onProgress((int) (count * 100 / max));
                }
                if (count >= max) {
                    linstener.onFinish();
                }
            }
        });
    }


    public void doUpload(String url, String path, String filename, String type, final MvcListener linstener) {
        MultipartBody builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", filename, RequestBody.create(MediaType.parse(type), new File(path)))
                .build();

        Request request = new Request.Builder().url(url).post(builder).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                linstener.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                linstener.onFinish();
            }
        });
    }
}
