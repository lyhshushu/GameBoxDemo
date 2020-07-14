package com.example.gameboxdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.androidlib.BaseActivity;
import com.example.androidlib.view.OnMeasureViewPager;
import com.example.androidlib.view.VerticalScrollView;
import com.example.androidlib.view.VideoPlayerIJK;
import com.example.androidlib.view.bean.PlayerVideoBean;
import com.example.findgame.FindGameFragment;
import com.example.findgame.recommend.controller.MvcModelImp;
import com.example.findgame.recommend.controller.OKutil;
import com.example.gameboxdemo.bean.GameInfActBean;
import com.example.gameboxdemo.fragment.GameInfIntroduceFragemt;
import com.flyco.tablayout.SlidingTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static com.example.androidlib.baseurl.Common.BASEURL;

public class GameInfActivity extends BaseActivity {

    private final static int OVER_10000 = 10000;

    @BindView(R.id.iv_Game_main_view)
    ImageView ivGameMainView;
    @BindView(R.id.iv_game_icon)
    ImageView ivGameIcon;
    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.tv_game_download)
    TextView tvGameDownload;
    @BindView(R.id.iv_bt_download)
    ImageView ivBtDownload;
    @BindView(R.id.tv_game_size)
    TextView tvGameSize;
    @BindView(R.id.video_player)
    VideoPlayerIJK videoPlayer;
    @BindView(R.id.cl_game_inf)
    ConstraintLayout clGameInf;
    @BindView(R.id.stl_game_inf)
    SlidingTabLayout stlGameInf;
    @BindView(R.id.vp_game_inf)
    ViewPager vpGameInf;
    @BindView(R.id.vs_game_inf)
    VerticalScrollView vsGameInf;


    private String gameId;
    private Handler handler;
    public static GameInfActBean gameInfBean;
    private List<PlayerVideoBean> playerVideoBeans;
    private List<String> titles;


    @Override
    public int getLayoutId() {
        return R.layout.activity_game_inf;
    }

    @Override
    public void initView() {
        videoPlayer.setVisibility(View.INVISIBLE);
        gameInfBean = new GameInfActBean();
        try {
            //错误当前
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            this.finish();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        playerVideoBeans = new LinkedList<>();

        titles = new ArrayList<>();
        titles.add("简介");
        titles.add("评论");
        titles.add("攻略");
        titles.add("游戏圈");


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new GameInfIntroduceFragemt());
        fragments.add(new GameInfIntroduceFragemt());
        fragments.add(new GameInfIntroduceFragemt());
        fragments.add(new GameInfIntroduceFragemt());

        FindGameFragment.FragmentAdapter gameInfFragmentAdapter = new FindGameFragment.FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        vpGameInf.setOffscreenPageLimit(4);
        vpGameInf.setAdapter(gameInfFragmentAdapter);
        stlGameInf.setViewPager(vpGameInf, titles.toArray(new String[0]));
        vsGameInf.setNeedScroll(true);
    }

    @Override
    protected void bindListener() {
        ivBtDownload.setOnClickListener(this);
        videoPlayer.setOnClickListener(this);
        ivGameMainView.setOnClickListener(this);
    }


    public void loadVideo(String path) {
        videoPlayer.setVideoPath(path);
    }

    @Override
    protected void onStop() {
        super.onStop();
        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    protected void onDestroy() {
        videoPlayer.release();
        super.onDestroy();
    }

    @Override
    public void initData() {

        Intent intent = getIntent();
        gameId = intent.getStringExtra("gameId");
        getJson(BASEURL + "/app/android/v4.4.9/gameDetail-index-id-" + gameId + ".html");
        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    VideoPlayerIJK.setPlayerVideoBeans(playerVideoBeans);
                    Glide.with(activity).load(gameInfBean.getAppIcon()).into(ivGameIcon);
                    Glide.with(activity).load(gameInfBean.getAppTitlePic()).into(ivGameMainView);
                    tvGameName.setText(gameInfBean.getAppName());
                    tvGameDownload.setText(gameInfBean.getAppDownload());
                    tvGameSize.setText(gameInfBean.getAppSize());
                }
            }
        };
    }


    private void getJson(String url) {
        MvcModelImp mvcModelImp = new MvcModelImp();
        mvcModelImp.getModel(url, new OKutil() {
            @Override
            public void onOk(String json) {
                getAppInf(json);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onNo(String message) {

            }
        });
    }

    private void getAppInf(String json) {
        try {
            JSONObject allJson = new JSONObject(json);
            String result = allJson.getString("result");
            JSONObject resultJson = new JSONObject(result);
            gameInfBean.setAppIcon(resultJson.getString("icopath"));
            gameInfBean.setAppName(resultJson.getString("appname"));

            float size = Float.parseFloat(resultJson.getString("size"));
            int intSize;
            String gameSize = null;
            DecimalFormat decimalFormat = new DecimalFormat("####.##");
            if (size > 1024) {
                float floatSize = Float.parseFloat(decimalFormat.format(size / 1024));
                if (floatSize == (int) floatSize) {
                    intSize = (int) floatSize;
                    gameSize = intSize + "G";
                } else {
                    gameSize = floatSize + "G";
                }
            } else {
                if (size == (int) size) {
                    intSize = (int) size;
                    gameSize = intSize + "M";
                } else {
                    gameSize = size + "M";
                }
            }

            gameInfBean.setAppSize(gameSize);
            String newGameInf = null;
            int downNum = Integer.parseInt(resultJson.getString("downnum"));
            if (downNum > OVER_10000) {
                downNum = downNum / OVER_10000;
                newGameInf = downNum + getString(com.example.findgame.R.string.over_10000) + getString(com.example.findgame.R.string.download) + "  " + gameSize;
            } else {
                newGameInf = downNum + getString(com.example.findgame.R.string.times) + getString(com.example.findgame.R.string.download) + "  " + gameSize;
            }
            gameInfBean.setAppDownload(newGameInf);

            String videos = resultJson.getString("videos");
            JSONArray videosJson = new JSONArray(videos);
            JSONObject videoJson = videosJson.getJSONObject(0);
            gameInfBean.setAppTitlePic(videoJson.getString("img"));
            gameInfBean.setAppVideo(videoJson.getString("url"));
            gameInfBean.setAppInfDetail(resultJson.getString("appinfo"));

            List<String> picList = new LinkedList<>();

            String customVideo = resultJson.getString("custom_video");
            JSONObject customVideoJson = new JSONObject(customVideo);
            String videoList = customVideoJson.getString("list");
            JSONArray videoListJson = new JSONArray(videoList);
            for (int i = 0; i < videoListJson.length(); i++) {
                JSONObject jsonVideo = videoListJson.getJSONObject(i);
                PlayerVideoBean playerVideoBean = new PlayerVideoBean();
                playerVideoBean.setPlayerPic(jsonVideo.getString("logo"));
                picList.add(jsonVideo.getString("logo"));
                playerVideoBean.setPlayerVideo(jsonVideo.getString("video_url"));
                playerVideoBeans.add(playerVideoBean);
            }
            gameInfBean.setAppPicList(picList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void widgetClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.video_player:
                Toast.makeText(activity, "adsadad", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_Game_main_view:
                videoPlayer.setVisibility(View.VISIBLE);
                if (videoPlayer.getCurrentPosition() == 0) {
                    ivGameMainView.setVisibility(View.INVISIBLE);
                    ivGameMainView.setFocusable(false);
                    loadVideo(gameInfBean.getAppVideo());
                }
                break;
            case R.id.iv_bt_download:
                Toast.makeText(activity, getString(R.string.download), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

}
