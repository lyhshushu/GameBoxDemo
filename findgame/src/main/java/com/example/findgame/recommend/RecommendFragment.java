package com.example.findgame.recommend;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Outline;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.KeyEventDispatcher;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.androidlib.BaseFragment;
import com.example.androidlib.view.ImageButtonWithText;
import com.example.findgame.R;
import com.example.findgame.R2;
import com.example.findgame.bean.AdvertisementBean;
import com.example.findgame.bean.DailyTweetBean;
import com.example.findgame.bean.GameInfBean;
import com.example.findgame.bean.MyGameBean;
import com.example.findgame.bean.PlayerRecommendBean;
import com.example.findgame.bean.TitleAd;
import com.example.findgame.recommend.controller.MvcModelImp;
import com.example.findgame.recommend.controller.OKutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

import static com.example.androidlib.baseurl.Common.BASEURL;


public class RecommendFragment extends BaseFragment {

    public static final int OK_MODEl = 0;
    public static final int NO_MODEL = 1;
    public static final int GAME_INF = 0;
    public static final int GAME_AD_PIC = 1;


    @BindView(R2.id.pic_one)
    ImageView picOne;
    @BindView(R2.id.pic_two)
    ImageView picTwo;
    @BindView(R2.id.ll_image)
    LinearLayout llImage;
    @BindView(R2.id.ibw_new_game)
    ImageButtonWithText ibwNewGame;
    @BindView(R2.id.ibw_invincible)
    ImageButtonWithText ibwInvincible;
    @BindView(R2.id.ibw_online_game)
    ImageButtonWithText ibwOnlineGame;
    @BindView(R2.id.ibw_necessary)
    ImageButtonWithText ibwNecessary;
    @BindView(R2.id.ibw_money)
    ImageButtonWithText ibwMoney;
    @BindView(R2.id.ll_tags)
    LinearLayout llTags;
    @BindView(R2.id.rv_game_inf)
    RecyclerView rvGameInf;
    @BindView(R2.id.rv_advertisement)
    RecyclerView rvAdvertisement;
    @BindView(R2.id.rv_my_game)
    RecyclerView rvMyGame;
    @BindView(R2.id.tv_my_game)
    TextView tvMyGame;
    @BindView(R2.id.delete_my_game)
    ImageView deleteMyGame;
    @BindView(R2.id.my_game)
    ConstraintLayout myGame;
    @BindView(R2.id.update_apk_img)
    ImageView updateApkImg;
    @BindView(R2.id.update_apk_name)
    TextView updateApkName;
    @BindView(R2.id.update_apk_detail)
    TextView updateApkDetail;
    @BindView(R2.id.apk_star)
    RatingBar apkStar;
    @BindView(R2.id.update_apk_edition)
    TextView updateApkEdition;
    @BindView(R2.id.update_apk_size)
    TextView updateApkSize;
    @BindView(R2.id.update_apk_delete)
    ImageView updateApkDelete;
    @BindView(R2.id.update_apk_button)
    Button updateApkButton;
    @BindView(R2.id.update_apk)
    ConstraintLayout updateApk;
    @BindView(R2.id.my_game_more)
    Button myGameMore;


    private GameInfAdapter mGameInfAdapter;
    private AdInfAdapter mAdInfAdapter;
    private MyGameAdapter mMyGameAdapter;
    private List<MultiItemEntity> mGameInfData;
    private List<AdvertisementBean> mAdInfData;

    static List<PlayerRecommendBean> playerRecommendBeans;
    static List<DailyTweetBean> hotBeans;
    static List<PlayerRecommendBean> newHotBeans;
    static List<DailyTweetBean> dailyTweetBeans;
    static List<DailyTweetBean> newGameBeans;
    static List<DailyTweetBean> miniGameBeans;
    static List<DailyTweetBean> testGameBeans;
    static List<List<DailyTweetBean>> listLists;
    static List<GameInfBean> bigTitleGame;


    private List<String> adPicData;
    public List<TitleAd> adPicUrl;
    private Context mContext;
    private Handler handler;

    private List<Integer> types;
    private List<String> titles;

    public RecommendFragment() {
    }


    @Override
    protected int bindLayout() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView() {
        mContext = getContext();
        mGameInfAdapter = new GameInfAdapter(mGameInfData);


        rvGameInf.setLayoutManager(new LinearLayoutManager(mContext));
        rvGameInf.addItemDecoration(new RecyclerDivider(mContext));
        rvGameInf.setAdapter(mGameInfAdapter);

        mAdInfAdapter = new AdInfAdapter(R.layout.item_ad_inf);
        rvAdvertisement.setLayoutManager(new LinearLayoutManager(mContext));
        rvAdvertisement.addItemDecoration(new RecyclerDivider(mContext));
        rvAdvertisement.setAdapter(mAdInfAdapter);

        mMyGameAdapter = new MyGameAdapter(R.layout.item_my_game);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvMyGame.setLayoutManager(linearLayoutManager);
        rvMyGame.setAdapter(mMyGameAdapter);

    }

    @Override
    protected void bindListener() {
        //推荐预览图
        picOne.setOnClickListener(this);
        picTwo.setOnClickListener(this);
        //5大推荐板块
        ibwNewGame.setOnClickListener(this);
        ibwInvincible.setOnClickListener(this);
        ibwOnlineGame.setOnClickListener(this);
        ibwOnlineGame.setOnClickListener(this);
        ibwMoney.setOnClickListener(this);
        //我的游戏板块
        deleteMyGame.setOnClickListener(this);
        myGameMore.setOnClickListener(this);
        //游戏盒更新模块
        updateApkDelete.setOnClickListener(this);
        updateApkButton.setOnClickListener(this);

        mMyGameAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            if (id == R.id.icon_my_game) {
                Toast.makeText(mContext, position + "", Toast.LENGTH_SHORT).show();
            }
        });
        mGameInfAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            GameInfBean gameInfBean = (GameInfBean) mGameInfData.get(position);
            if (id == R.id.csl_game_inf) {
                Toast.makeText(mContext, gameInfBean.getGameName() + "csl_game_inf", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                //component启动（都可实现）
//                ComponentName componentName=new ComponentName("com.example.gameboxdemo","com.example.gameboxdemo.GameInfActivity");
//                intent.setComponent(componentName);
                //隐式启动
                intent.setAction("open_game_inf_activity");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("gameId",gameInfBean.getGameId());
                this.startActivity(intent);
            } else if (id == R.id.bt_download) {
                Toast.makeText(mContext, gameInfBean.getGameName() + "bt_download_game_inf", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.tweet) {
                Toast.makeText(mContext, "特推", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.cl_recommend) {
                Toast.makeText(mContext, "玩家推", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.csl_ad_game) {
                Toast.makeText(mContext, "bigtitle", Toast.LENGTH_SHORT).show();
            }
        });

        mAdInfAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            if (id == R.id.csl_ad_game) {
                Toast.makeText(mContext, mAdInfData.get(position).getAdName() + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void widgetClick(View v) {
        int id = v.getId();
        //library中的id不是final的不可使用switch
        //验证点击功能
        if (id == R.id.pic_one) {
            Toast.makeText(mContext, "pic_one", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.pic_two) {
            Toast.makeText(mContext, "pic_two", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ibw_new_game) {
            Toast.makeText(mContext, "ibw_new_game", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ibw_invincible) {
            Toast.makeText(mContext, "ibw_invincible", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ibw_online_game) {
            Toast.makeText(mContext, "ibw_online_game", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ibw_necessary) {
            Toast.makeText(mContext, "ibw_necessary", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ibw_money) {
            Toast.makeText(mContext, "ibw_money", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.delete_my_game) {
            Toast.makeText(mContext, "delete_my_game", Toast.LENGTH_SHORT).show();
            myGame.setVisibility(View.GONE);
        } else if (id == R.id.my_game_more) {
            Toast.makeText(mContext, "my_game_more", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.update_apk_delete) {
            Toast.makeText(mContext, "update_apk_delete", Toast.LENGTH_SHORT).show();
            updateApk.setVisibility(View.GONE);
        } else if (id == R.id.update_apk_button) {
            Toast.makeText(mContext, "update_apk_button", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("HandlerLeak")
    @Override
    protected void applyData() {
        //测试数据
//        mGameInfData = GameInfBean.getGameInfData();
        mAdInfData = AdvertisementBean.getAdInfData();
        List<MyGameBean> mMyGameData = getAppList();

        mGameInfData = new LinkedList<>();
        dailyTweetBeans = new LinkedList<>();
        newHotBeans = new LinkedList<>();
        playerRecommendBeans = new LinkedList<>();
        newGameBeans = new LinkedList<>();
        testGameBeans = new LinkedList<>();
        listLists = new LinkedList<>();
        miniGameBeans = new LinkedList<>();
        bigTitleGame = new LinkedList<>();
        types = new LinkedList<>();
        titles = new LinkedList<>();
        adPicUrl = new LinkedList<>();
        hotBeans = new LinkedList<>();
        getJSON(BASEURL + "/app/android/v4.4.5/game-index.html");
        handler = new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    for (int i = types.size() - 1; i >= 0; i--) {
                        GameInfBean gameInfBean = new GameInfBean();
                        gameInfBean.setType(types.get(i));
                        gameInfBean.setTitleName(titles.get(i));
                        mGameInfData.add(4 * (i + 1), gameInfBean);
                        mGameInfAdapter.setNewData(mGameInfData);
                    }
                    picOne.setOutlineProvider(new ViewOutlineProvider() {
                        @Override
                        public void getOutline(View view, Outline outline) {
                            outline.setRoundRect(0,0,picOne.getWidth(),picOne.getHeight(),30);
                        }
                    });
                    picTwo.setOutlineProvider(new ViewOutlineProvider() {
                        @Override
                        public void getOutline(View view, Outline outline) {
                            outline.setRoundRect(0,0,picTwo.getWidth(),picTwo.getHeight(),30);
                        }
                    });
                    picOne.setClipToOutline(true);
                    picTwo.setClipToOutline(true);
                    Glide.with(mContext).load(adPicUrl.get(0).getPicOneUrl()).into(picOne);
                    Glide.with(mContext).load(adPicUrl.get(1).getPicOneUrl()).into(picTwo);
                }
            }
        };
        mAdInfAdapter.setNewData(mAdInfData);
        mMyGameAdapter.setNewData(mMyGameData);
    }


    private int adListLength;

    private void getJSON(String url) {
        MvcModelImp mvcModelImp = new MvcModelImp();
        mvcModelImp.getModel(url, new OKutil() {
            @Override
            public void onOk(String json) {
                getGameInf(json);
                getGameAdPic(json);
                //List
                for (int i = 0; i <adListLength ; i++) {
                    getListInf(i, json);
                }


                handler.sendEmptyMessage(1);
            }

            @Override
            public void onNo(String message) {

            }
        });
    }

    private void getListInf(int i, String json) {
        try {
            JSONArray jsonArrayAdList = getJsonList(json);
            JSONObject adTweet = jsonArrayAdList.getJSONObject(i);
            titles.add(adTweet.getString("title"));
            types.add(Integer.parseInt(adTweet.getString("type")));
            String ext;
            JSONObject jsonObjectExt;
            String arrayList;
            JSONArray jsonArrayAd;

            switch (Integer.parseInt(adTweet.getString("type"))) {
                case 1:
                    ext = adTweet.getString("ext");
                    jsonObjectExt = new JSONObject(ext);
                    arrayList = jsonObjectExt.getString("list");
                    jsonArrayAd = new JSONArray(arrayList);
                    for (int j = 0; j < jsonArrayAd.length(); j++) {
                        JSONObject ad = jsonArrayAd.getJSONObject(j);

                        String tweetTime = ad.getString("ext");
                        JSONObject jsonTweetTime = new JSONObject(tweetTime);

                        String dateLineS = jsonTweetTime.getString("dateline");
                        int dateline = Integer.parseInt(dateLineS);

                        long temp = (long) dateline * 1000;
                        Timestamp ts = new Timestamp(temp);
                        @SuppressLint("SimpleDateFormat")
                        DateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format));

                        String tsStr = dateFormat.format(ts);

                        DailyTweetBean dailyTweetBean = new DailyTweetBean();
                        dailyTweetBean.setTweetName(ad.getString("appname"));
                        dailyTweetBean.setTweetPicUrl(ad.getString("icopath"));
                        dailyTweetBean.setTweetTime(tsStr);
                        dailyTweetBeans.add(dailyTweetBean);
                    }
                    break;
                case 19:
                    ext = adTweet.getString("ext");
                    jsonObjectExt = new JSONObject(ext);
                    arrayList = jsonObjectExt.getString("list");
                    jsonArrayAd = new JSONArray(arrayList);
                    for (int j = 0; j < jsonArrayAd.length(); j++) {
                        JSONObject ad = jsonArrayAd.getJSONObject(j);
                        DailyTweetBean newGameBean = new DailyTweetBean();

                        String tweetTime = ad.getString("ext");
                        JSONObject jsonTweetTime = new JSONObject(tweetTime);

                        String updateTime = jsonTweetTime.getString("desc");

                        int dateLine = Integer.parseInt(jsonTweetTime.getString("dateline"));

                        long temp = (long) dateLine * 1000;
                        Timestamp ts = new Timestamp(temp);
                        @SuppressLint("SimpleDateFormat")
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String[] weekDay = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
                        Calendar calendar = Calendar.getInstance();
                        String datetime = dateFormat.format(ts);
                        Date date = null;
                        try {
                            date = dateFormat.parse(datetime);
                            calendar.setTime(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                        if (w < 0) {
                            w = 0;
                        }

                        newGameBean.setNewGameTime(updateTime);
                        newGameBean.setTweetName(ad.getString("appname"));
                        newGameBean.setTweetPicUrl(ad.getString("icopath"));
                        newGameBean.setTweetTime(weekDay[w]);
                        newGameBeans.add(newGameBean);
                    }
                    break;
                case 23:
                    ext = adTweet.getString("ext");
                    jsonObjectExt = new JSONObject(ext);
                    arrayList = jsonObjectExt.getString("list");
                    jsonArrayAd = new JSONArray(arrayList);
                    for (int j = 0; j < jsonArrayAd.length(); j++) {

                        JSONObject testGame = jsonArrayAd.getJSONObject(j);
                        DailyTweetBean testGameBean = new DailyTweetBean();
                        testGameBean.setTweetName(testGame.getString("appname"));
                        testGameBean.setTweetPicUrl(testGame.getString("icopath"));
                        testGameBeans.add(testGameBean);
                    }
                    break;
                case 24:
                    ext = adTweet.getString("ext");
                    jsonObjectExt = new JSONObject(ext);
                    arrayList = jsonObjectExt.getString("list");
                    jsonArrayAd = new JSONArray(arrayList);
                    for (int j = 0; j < jsonArrayAd.length(); j++) {
                        JSONObject recommend = jsonArrayAd.getJSONObject(j);
                        String gamePart = recommend.getString("game");
                        JSONObject gamePartJson = new JSONObject(gamePart);

                        PlayerRecommendBean playerRecommendBean = new PlayerRecommendBean();
                        playerRecommendBean.setRecommendWord(recommend.getString("content"));
                        playerRecommendBean.setPlayerName(recommend.getString("nick"));
                        playerRecommendBean.setRecommendNum(recommend.getString("recommend_num"));
                        playerRecommendBean.setHeadPic(recommend.getString("sface"));

                        playerRecommendBean.setGameName(gamePartJson.getString("appname"));
                        playerRecommendBean.setGamePicUrl(gamePartJson.getString("icopath"));

                        playerRecommendBeans.add(playerRecommendBean);

                    }
                    break;
                case 25:
                    ext = adTweet.getString("ext");
                    jsonObjectExt = new JSONObject(ext);
                    arrayList = jsonObjectExt.getString("list");
                    jsonArrayAd = new JSONArray(arrayList);
                    for (int j = 0; j < jsonArrayAd.length(); j++) {
                        JSONObject miniGameJson = jsonArrayAd.getJSONObject(j);
                        DailyTweetBean miniGameBean = new DailyTweetBean();
                        String share = miniGameJson.getString("share");
                        JSONObject shareJson = new JSONObject(share);
                        miniGameBean.setTweetName(miniGameJson.getString("name"));
                        miniGameBean.setTweetPicUrl(shareJson.getString("cover"));
                        miniGameBeans.add(miniGameBean);

                    }
                    break;
                case 15:
                    ext = adTweet.getString("ext");
                    jsonObjectExt = new JSONObject(ext);
                    arrayList = jsonObjectExt.getString("list");
                    jsonArrayAd = new JSONArray(arrayList);
                    for (int j = 0; j < jsonArrayAd.length() - 1; j++) {
                        JSONObject hotImg = jsonArrayAd.getJSONObject(j);
                        DailyTweetBean hotBean = new DailyTweetBean();
                        hotBean.setTweetPicUrl(hotImg.getString("img_v50"));
                        hotBeans.add(hotBean);
                    }
                case 26:
                    ext = adTweet.getString("ext");
                    jsonObjectExt = new JSONObject(ext);
                    arrayList = jsonObjectExt.getString("list");
                    jsonArrayAd = new JSONArray(arrayList);
                    for (int j = 0; j < jsonArrayAd.length(); j++) {
                        JSONObject newHot = jsonArrayAd.getJSONObject(j);
                        PlayerRecommendBean newHotBean = new PlayerRecommendBean();
                        newHotBean.setGameName(newHot.getString("appname"));
                        newHotBean.setGamePicUrl(newHot.getString("icopath"));

                        String eventRecord = newHot.getString("event_record");
                        JSONObject eventRecordJson = new JSONObject(eventRecord);

                        String dateLineS = eventRecordJson.getString("dateline");
                        long dateline = (long) Integer.parseInt(dateLineS) * 1000;
                        Timestamp timestamp = new Timestamp(dateline);
                        @SuppressLint("SimpleDateFormat")
                        DateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_with_line));
                        String date = dateFormat.format(timestamp);
                        newHotBean.setRecommendWord(date + "  " + eventRecordJson.getString("content"));
                        newHotBeans.add(newHotBean);

                    }
                    break;
                case 18:
                case 6:
                case 4:
                case 5:
                    GameInfBean bigTitle = new GameInfBean();
                    bigTitle.setGameInf(adTweet.getString("desc"));
                    bigTitle.setGameImgUrl(adTweet.getString("img"));
                    bigTitleGame.add(bigTitle);
                    break;
                case 2:
                case 21:
                case 13:
                    ext = adTweet.getString("ext");
                    jsonObjectExt = new JSONObject(ext);
                    arrayList = jsonObjectExt.getString("list");
                    jsonArrayAd = new JSONArray(arrayList);
                    List<DailyTweetBean> studyGameBeans = new LinkedList<>();
                    for (int j = 0; j < jsonArrayAd.length(); j++) {
                        JSONObject studyGame = jsonArrayAd.getJSONObject(j);
                        DailyTweetBean studyGameBean = new DailyTweetBean();
                        studyGameBean.setTweetPicUrl(studyGame.getString("icopath"));
                        studyGameBean.setTweetName(studyGame.getString("appname"));
                        studyGameBeans.add(studyGameBean);
                    }
                    listLists.add(studyGameBeans);
                    break;
                case 22:
                    ext = adTweet.getString("ext");
                    jsonObjectExt = new JSONObject(ext);
                    arrayList = jsonObjectExt.getString("list");
                    jsonArrayAd = new JSONArray(arrayList);
                    List<DailyTweetBean> onlineGameBeans = new LinkedList<>();
                    for (int j = 0; j < jsonArrayAd.length(); j++) {
                        JSONObject studyGame = jsonArrayAd.getJSONObject(j);
                        DailyTweetBean studyGameBean = new DailyTweetBean();
                        studyGameBean.setTweetPicUrl(studyGame.getString("logo"));
                        studyGameBean.setTweetName(studyGame.getString("name"));
                        onlineGameBeans.add(studyGameBean);
                    }
                    listLists.add(onlineGameBeans);
                    break;
                default:
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private JSONArray getJsonList(String json) {
        try {
            JSONObject jsonArray = new JSONObject(json);
            String jsonResult = jsonArray.getString("result");
            JSONObject jsonResultObj = new JSONObject(jsonResult);
            String adListArray = jsonResultObj.getString("adList");
            JSONArray jsonArrayAdList = new JSONArray(adListArray);
            return jsonArrayAdList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    private void getGameAdPic(String json) {
        try {
            JSONObject jsonArray = new JSONObject(json);

            String jsonResult = jsonArray.getString("result");
            JSONObject jsonResultObj = new JSONObject(jsonResult);

            String jsonRecPosterList = jsonResultObj.getString("recPosterList");
            JSONArray jsonArrayAd = new JSONArray(jsonRecPosterList);

            JSONObject listAd = jsonArrayAd.getJSONObject(0);
            String jsonData = listAd.getString("data");

            JSONArray adPic = new JSONArray(jsonData);

            for (int i = 0; i < 2; i++) {
                JSONObject picUrl = adPic.getJSONObject(i);
                TitleAd titleAd = new TitleAd();
                titleAd.setPicOneUrl(picUrl.getString("poster"));
                adPicUrl.add(titleAd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getGameInf(String json) {
        JSONArray jsonArrayAdList = null;
        try {
            JSONObject jsonArray = new JSONObject(json);
            String jsonResult = jsonArray.getString("result");
            JSONObject jsonResultObj = new JSONObject(jsonResult);
            String adListArray = jsonResultObj.getString("adList");
            jsonArrayAdList = new JSONArray(adListArray);

            String jsonRecGame = jsonResultObj.getString("recGame");
            JSONArray jsonArrayRecGame = new JSONArray(jsonRecGame);
            for (int i = 0; i < jsonArrayRecGame.length(); i++) {
                JSONObject jsonObject = jsonArrayRecGame.getJSONObject(i);
                GameInfBean gameInfBean = new GameInfBean();
                gameInfBean.setGameName(jsonObject.getString("appname"));
                gameInfBean.setGameDownload(jsonObject.getString("num_download"));
                gameInfBean.setGameInf(jsonObject.getString("review"));
                gameInfBean.setGameImgUrl(jsonObject.getString("icopath"));
                gameInfBean.setGameId(jsonObject.getString("id"));
                gameInfBean.setType(0);
                mGameInfData.add(gameInfBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adListLength=jsonArrayAdList.length();
    }

    private List<MyGameBean> getAppList() {
        List<MyGameBean> appList = new LinkedList<>();
        List<PackageInfo> packages = getActivity().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            MyGameBean myGameBean = new MyGameBean();
            myGameBean.appIcon = packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager());
            if (((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) && packageInfo.applicationInfo.packageName.contains("game")) {
                appList.add(myGameBean);
            } else {

            }
        }
        return appList;
//        PackageManager packageManager=getActivity().getPackageManager();
    }

}
