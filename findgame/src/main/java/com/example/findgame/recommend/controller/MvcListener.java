package com.example.findgame.recommend.controller;

public interface MvcListener {

        /**
         * 失败
         * @param message
         */
        void onError(String message);

        /**
         * 完成
         */
        void onFinish();

        /**
         * 进程中
         * @param progress
         */
        void onProgress(int progress);

}
