package com.example.findgame.recommend;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.gesture.GestureLibraries;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.findgame.bean.TitleAd;
import com.example.findgame.recommend.controller.MvcModelImp;
import com.example.findgame.recommend.controller.OKutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
    private List<MyGameBean> mMyGameData;
    static List<DailyTweetBean> dailyTweetBeans;
    private List<String> adPicData;
    public List<TitleAd> adPicUrl;
    private Context mContext;

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
            GameInfBean gameInfBean=(GameInfBean) mGameInfData.get(position);
            if (id == R.id.csl_game_inf) {
                Toast.makeText(mContext, gameInfBean.getGameName() + "csl_game_inf", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.bt_download) {
                Toast.makeText(mContext, gameInfBean.getGameName() + "bt_download_game_inf", Toast.LENGTH_SHORT).show();
            }else if(id==R.id.tweet){
                Toast.makeText(mContext,  "特推", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void applyData() {
        //测试数据
//        mGameInfData = GameInfBean.getGameInfData();
        mAdInfData = AdvertisementBean.getAdInfData();
        mMyGameData = getAppList();

        mGameInfData = new LinkedList<>();
        dailyTweetBeans= new LinkedList<>();
        adPicUrl = new LinkedList<>();
        getJSON(BASEURL + "/app/android/v4.4.5/game-index.html");

        mAdInfAdapter.setNewData(mAdInfData);
        mMyGameAdapter.setNewData(mMyGameData);

    }


    private void getJSON(String URL) {
        MvcModelImp mvcModelImp = new MvcModelImp();
        mvcModelImp.getModel(URL, new OKutil() {
            @Override
            public void onOk(String json) {
                getGameInf(json);
                getGameAdPic(json);
                getDailyTweet(json);
                getActivity().runOnUiThread(() -> {
                    GameInfBean gameInfBean = new GameInfBean();
                    gameInfBean.setType(2);
                    mGameInfData.add(4, gameInfBean);
                    mGameInfAdapter.setNewData(mGameInfData);
                    Glide.with(mContext).load(adPicUrl.get(0).getPicOneUrl()).into(picOne);
                    Glide.with(mContext).load(adPicUrl.get(1).getPicOneUrl()).into(picTwo);
                });
            }

            @Override
            public void onNo(String message) {

            }
        });

    }
    private void getDailyTweet(String json){
        try {
            JSONObject jsonArray = new JSONObject(json);

            String jsonResult = jsonArray.getString("result");
            JSONObject jsonResultObj = new JSONObject(jsonResult);

            String adListArray = jsonResultObj.getString("adList");
            JSONArray jsonArrayAdList = new JSONArray(adListArray);

            JSONObject adTweet = jsonArrayAdList.getJSONObject(0);

            String ext = adTweet.getString("ext");
            JSONObject jsonObjectExt = new JSONObject(ext);

            String arrayList = jsonObjectExt.getString("list");
            JSONArray jsonArrayAd = new JSONArray(arrayList);

            for (int i = 0; i < jsonArrayAd.length(); i++) {
                JSONObject ad = jsonArrayAd.getJSONObject(i);
                DailyTweetBean dailyTweetBean=new DailyTweetBean();
                dailyTweetBean.setTweetName(ad.getString("appname"));
                dailyTweetBean.setTweetPicUrl(ad.getString("icopath"));
                dailyTweetBean.setTweetTime("昨天");
                dailyTweetBeans.add(dailyTweetBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        try {
            JSONObject jsonArray = new JSONObject(json);
            String jsonResult = jsonArray.getString("result");
            JSONObject jsonResultObj = new JSONObject(jsonResult);
            String jsonRecGame = jsonResultObj.getString("recGame");
            JSONArray jsonArrayRecGame = new JSONArray(jsonRecGame);
            for (int i = 0; i < 5; i++) {
                JSONObject jsonObject = jsonArrayRecGame.getJSONObject(i);
                GameInfBean gameInfBean = new GameInfBean();
                gameInfBean.setGameName(jsonObject.getString("appname"));
                gameInfBean.setGameDownload(jsonObject.getString("num_download"));
                gameInfBean.setGameInf(jsonObject.getString("review"));
                gameInfBean.setGameImgUrl(jsonObject.getString("icopath"));
                gameInfBean.setType(1);
                mGameInfData.add(gameInfBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
