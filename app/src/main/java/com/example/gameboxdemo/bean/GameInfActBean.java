package com.example.gameboxdemo.bean;

import java.io.Serializable;
import java.util.List;
import java.util.SplittableRandom;

public class GameInfActBean implements Serializable {
    private String appName;
    private String appDownload;
    private String appSize;
    private String appIcon;
    private String appTitlePic;
    private String appVideo;
    private List<String> appPicList;
    private String appInfDetail;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDownload() {
        return appDownload;
    }

    public void setAppDownload(String appDownload) {
        this.appDownload = appDownload;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppTitlePic() {
        return appTitlePic;
    }

    public void setAppTitlePic(String appTitlePic) {
        this.appTitlePic = appTitlePic;
    }

    public String getAppVideo() {
        return appVideo;
    }

    public void setAppVideo(String appVideo) {
        this.appVideo = appVideo;
    }

    public List<String> getAppPicList() {
        return appPicList;
    }

    public void setAppPicList(List<String> appPicList) {
        this.appPicList = appPicList;
    }

    public String getAppInfDetail() {
        return appInfDetail;
    }

    public void setAppInfDetail(String appInfDetail) {
        this.appInfDetail = appInfDetail;
    }
}
