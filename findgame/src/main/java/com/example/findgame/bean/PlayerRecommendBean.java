package com.example.findgame.bean;

import java.io.Serializable;

public class PlayerRecommendBean implements Serializable {
    private String gamePicUrl;
    private String gameName;
    private String recommendWord;
    private String recommendNum;
    private String headPic;
    private String playerName;

    public String getGamePicUrl() {
        return gamePicUrl;
    }

    public void setGamePicUrl(String gamePicUrl) {
        this.gamePicUrl = gamePicUrl;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getRecommendWord() {
        return recommendWord;
    }

    public void setRecommendWord(String recommendWord) {
        this.recommendWord = recommendWord;
    }

    public String getRecommendNum() {
        return recommendNum;
    }

    public void setRecommendNum(String recommendNum) {
        this.recommendNum = recommendNum;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
