package com.example.findgame.bean;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyGameBean implements Serializable {

    private String gameIconUrl;
    private String packageName;
    private String appName;
    public Drawable appIcon;

    public MyGameBean(){

    }
    public MyGameBean(Drawable icon) {
        this.appIcon = icon;
    }

    public String getGameIconUrl() {
        return gameIconUrl;
    }

    public void setGameIconUrl(String gameIconUrl) {
        this.gameIconUrl = gameIconUrl;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

//测试数据
//    public static List<MyGameBean> getMyGameData() {
//        List<MyGameBean> data = new LinkedList<>();
//        data.add(new MyGameBean(null));
//        data.add(new MyGameBean(null));
//        data.add(new MyGameBean(null));
//        return data;
//    }

}
