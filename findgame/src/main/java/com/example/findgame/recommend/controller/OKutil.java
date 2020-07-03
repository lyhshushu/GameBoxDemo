package com.example.findgame.recommend.controller;

/**
 * @author 4399lyh
 */
public interface OKutil {
    /**
     * okhttp 完成
     * @param json
     */
    void onOk(String json);

    /**
     * okhttp 失败
     * @param message
     */
    void onNo(String message);
}
