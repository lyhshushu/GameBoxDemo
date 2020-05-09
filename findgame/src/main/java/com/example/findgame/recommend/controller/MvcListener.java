package com.example.findgame.recommend.controller;

public interface MvcListener {
    /**
     * 成功
     * @param str
     */
    void onSuccess(String str);

    /**
     * 失败
     */
    void onFailed();
}
