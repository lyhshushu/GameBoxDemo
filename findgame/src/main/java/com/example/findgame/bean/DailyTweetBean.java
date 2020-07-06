package com.example.findgame.bean;


import java.io.Serializable;

public class DailyTweetBean implements Serializable {

    public String getTweetName() {
        return tweetName;
    }

    public void setTweetName(String tweetName) {
        this.tweetName = tweetName;
    }

    public String getTweetTime() {
        return tweetTime;
    }

    public void setTweetTime(String tweetTime) {
        this.tweetTime = tweetTime;
    }

    public String getTweetPicUrl() {
        return tweetPicUrl;
    }

    public void setTweetPicUrl(String tweetPicUrl) {
        this.tweetPicUrl = tweetPicUrl;
    }

    private String tweetName;
    private String tweetTime;
    private String tweetPicUrl;
    private String newGameTime;


    public String getNewGameTime() {
        return newGameTime;
    }

    public void setNewGameTime(String newGameTime) {
        this.newGameTime = newGameTime;
    }



//    public DailyTweetBean(String tweetName, String tweetTime) {
//        this.tweetName = tweetName;
//        this.tweetTime = tweetTime;
//    }


//    public static List<DailyTweetBean> getDailyTweetBeans() {
//        List<DailyTweetBean> dailyTweetBeans = new LinkedList<>();
//        dailyTweetBeans.add(new DailyTweetBean("篝火：被遗弃的土地", "1"));
//        dailyTweetBeans.add(new DailyTweetBean("螺旋风暴（全球首创玩法）", "2"));
//        dailyTweetBeans.add(new DailyTweetBean("篝火：被遗弃的土地", "3"));
//        dailyTweetBeans.add(new DailyTweetBean("篝火：被遗弃的土地", "4"));
//        dailyTweetBeans.add(new DailyTweetBean("篝火：被遗弃的土地", "5"));
//        dailyTweetBeans.add(new DailyTweetBean("螺旋风暴（全球首创玩法）", "6"));
//        dailyTweetBeans.add(new DailyTweetBean("篝火：被遗弃的土地", "7"));
//        dailyTweetBeans.add(new DailyTweetBean("螺旋风暴（全球首创玩法）", "8"));
//        dailyTweetBeans.add(new DailyTweetBean("篝火：被遗弃的土地", "9"));
//        dailyTweetBeans.add(new DailyTweetBean("螺旋风暴（全球首创玩法）", "10"));
//        return dailyTweetBeans;
//    }

}
