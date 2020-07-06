package com.example.findgame.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



/**
 * @author 4399lyh
 */
public class GameInfBean implements Serializable, MultiItemEntity {

    @SerializedName("appname")
    private String gameName;
    @SerializedName("num_download")
    private String gameDownload;
    @SerializedName("review")
    private String gameInf;
    @SerializedName("icon_path")
    private String gameImgUrl;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;


    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    private String titleName;

    public GameInfBean() {

    }

//    public GameInfBean(String name,String download, String inf, String imgUrl) {
//        this.gameName = name;
//        this.gameDownload=download;
//        this.gameInf = inf;
//        this.gameImgUrl = imgUrl;
//    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameInf() {
        return gameInf;
    }

    public void setGameInf(String gameInf) {
        this.gameInf = gameInf;
    }

    public String getGameImgUrl() {
        return gameImgUrl;
    }

    public void setGameImgUrl(String gameImgUrl) {
        this.gameImgUrl = gameImgUrl;
    }

    public String getGameDownload() {
        return gameDownload;
    }

    public void setGameDownload(String gameDownload) {
        this.gameDownload = gameDownload;
    }

    @Override
    public int getItemType() {
        return type;
    }


//    public static List<GameInfBean> getGameInfData() {
//        List<GameInfBean> data = new LinkedList<>();
//        data.add(new GameInfBean("部落冲突","10w+ 下载 107.52M","3D魔幻动作网游，11111", null));
//        data.add(new GameInfBean("水果忍者", "10w+ 下载 107.53M","3D魔幻动作网游，22222", null));
//        data.add(new GameInfBean("战斗吧剑灵","10w+ 下载 107.54M", "3D魔幻动作网游，33333", null));
//        return data;
//    }


}
