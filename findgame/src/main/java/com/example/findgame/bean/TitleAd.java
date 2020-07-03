package com.example.findgame.bean;

import com.google.gson.annotations.SerializedName;


import java.io.Serializable;

public class TitleAd implements Serializable {
    @SerializedName("poster")
    private String picOneUrl;

    public TitleAd(){

    }

    public String getPicOneUrl() {
        return picOneUrl;
    }

    public void setPicOneUrl(String picOneUrl) {
        this.picOneUrl = picOneUrl;
    }


}
