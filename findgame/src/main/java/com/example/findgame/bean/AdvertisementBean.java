package com.example.findgame.bean;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class AdvertisementBean implements Serializable {
    private int adType;
    private String adName;
    private String adImgUrl;
    private String adDetail;

    public AdvertisementBean(int type,String name,String imgUrl,String detail){
        this.adType=type;
        this.adName=name;
        this.adImgUrl=imgUrl;
        this.adDetail=detail;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdImgUrl() {
        return adImgUrl;
    }

    public void setAdImgUrl(String adImgUrl) {
        this.adImgUrl = adImgUrl;
    }

    public String getAdDetail() {
        return adDetail;
    }

    public void setAdDetail(String adDetail) {
        this.adDetail = adDetail;
    }
    public static List<AdvertisementBean> getAdInfData() {
        List<AdvertisementBean> data = new LinkedList<>();
        data.add(new AdvertisementBean(0,"游戏天天玩，好玩乐翻天",null,"游戏天天玩1111"));
        data.add(new AdvertisementBean(1,"本周开测游戏一览",null,"游戏天天玩2222"));
        data.add(new AdvertisementBean(2,"球球大作战",null,"游戏天天玩3333"));
        data.add(new AdvertisementBean(3,"造梦西游OL",null,"游戏天天玩4444"));
        return data;
    }
}
