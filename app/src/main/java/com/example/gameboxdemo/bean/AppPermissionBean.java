package com.example.gameboxdemo.bean;

import java.io.Serializable;

public class AppPermissionBean implements Serializable {
    private String permissionDes;
    private String permissionTitle;

    public String getPermissionDes() {
        return permissionDes;
    }

    public void setPermissionDes(String permissionDes) {
        this.permissionDes = permissionDes;
    }

    public String getPermissionTitle() {
        return permissionTitle;
    }

    public void setPermissionTitle(String permissionTitle) {
        this.permissionTitle = permissionTitle;
    }
}
