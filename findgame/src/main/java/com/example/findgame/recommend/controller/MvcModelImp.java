package com.example.findgame.recommend.controller;


import java.util.HashMap;


public class MvcModelImp implements MvcModel {

    @Override
    public void getModel(String url, OKutil listener) {
        HttpUtil.getInstance().doGet(url, listener);
    }

    @Override
    public void postModel(String url, HashMap<String, String> map, OKutil listener) {
        HttpUtil.getInstance().doPost(url, map, listener);
    }

    @Override
    public void downloadModel(String url, String path, MvcListener listener) {
        HttpUtil.getInstance().doDownload(url, path, listener);
    }

    @Override
    public void uploadModel(String url, String path, String fileName, String Type, MvcListener listener) {
        HttpUtil.getInstance().doUpload(url, path, fileName, Type, listener);
    }
}
