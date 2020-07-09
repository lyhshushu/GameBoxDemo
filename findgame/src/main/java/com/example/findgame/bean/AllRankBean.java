package com.example.findgame.bean;

import java.io.Serializable;

public class AllRankBean implements Serializable {
    private String gameName;
    private String gamePic;
    private String gameInf;
    private int rank;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGamePic() {
        return gamePic;
    }

    public void setGamePic(String gamePic) {
        this.gamePic = gamePic;
    }

    public String getGameInf() {
        return gameInf;
    }

    public void setGameInf(String gameInf) {
        this.gameInf = gameInf;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
