package com.example.findgame.recommend.controller;

import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;


public class MvcModelImp implements MvcModel {

    @Override
    public void getHttpInf(Map<String, String> params, String url, MvcListener mvcListener) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        OkHttpUtils
                .get()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mvcListener.onFailed();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("response==", response);
                        mvcListener.onSuccess(response);
                    }
                });
    }
}
