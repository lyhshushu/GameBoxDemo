package com.example.findgame.bean;

import java.io.Serializable;

public class LikeGameBean implements Serializable {
    private String name;
    private String gamePic;
    private String gameInf;
    private String gameContext;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getGameContext() {
        return gameContext;
    }

    public void setGameContext(String gameContext) {
        this.gameContext = gameContext;
    }
}
