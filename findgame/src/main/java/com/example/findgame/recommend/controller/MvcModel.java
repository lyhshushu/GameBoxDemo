package com.example.findgame.recommend.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 4399lyh
 */
public interface MvcModel {

    /**
     * get
     *
     * @param url
     * @param listener
     */
    void getModel(String url, OKutil listener);

    /**
     * post
     *
     * @param url
     * @param map
     * @param listener
     */
    void postModel(String url, HashMap<String, String> map, OKutil listener);

    /**
     * download
     *
     * @param url
     * @param path
     * @param listener
     */
    void downloadModel(String url, String path, MvcListener listener);


    /**
     * upload
     *
     * @param url
     * @param path
     * @param fileName
     * @param Type
     * @param listener
     */
    void uploadModel(String url, String path, String fileName, String Type, MvcListener listener);

}
