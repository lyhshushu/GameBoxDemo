package com.example.gameboxdemo;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * @author yh.liu
 * 必须public才能生效
 */
public class HomeApplication extends Application {

    private Boolean isDebug = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if (isDebug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(HomeApplication.this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }
}
