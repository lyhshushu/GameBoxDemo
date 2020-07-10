package com.example.findgame.bean;

import java.io.Serializable;

public class AlbumBean implements Serializable {
    private String name;
    private String picUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
