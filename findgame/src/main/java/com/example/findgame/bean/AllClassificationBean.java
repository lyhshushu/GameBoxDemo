package com.example.findgame.bean;

import java.io.Serializable;
import java.util.List;

public class AllClassificationBean implements Serializable {
    private String name;
    private String picUrl;
    private String collectionNum;
    private List<String> collectionPicUrl;

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

    public String getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(String collectionNum) {
        this.collectionNum = collectionNum;
    }

    public List<String> getCollectionPicUrl() {
        return collectionPicUrl;
    }

    public void setCollectionPicUrl(List<String> collectionPicUrl) {
        this.collectionPicUrl = collectionPicUrl;
    }
}
