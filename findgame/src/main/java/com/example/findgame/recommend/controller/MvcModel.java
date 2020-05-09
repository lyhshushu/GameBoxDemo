package com.example.findgame.recommend.controller;

import java.util.Map;

public interface MvcModel {
    /**
     * 获取data
     * @param params
     * @param url
     * @param mvcListener
     */
    void getHttpInf(Map<String,String> params,String url,MvcListener mvcListener);
}
