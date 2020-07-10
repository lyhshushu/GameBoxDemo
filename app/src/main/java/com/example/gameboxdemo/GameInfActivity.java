package com.example.gameboxdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.androidlib.BaseActivity;
import com.example.androidlib.utils.VideoPlayerListener;
import com.example.androidlib.view.VideoPlayerIJK;
import com.example.findgame.recommend.controller.MvcModelImp;
import com.example.findgame.recommend.controller.OKutil;
import com.example.gameboxdemo.bean.GameInfActBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.danmaku.ijk.media.player.IMediaPlayer;
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

    private String gameId;
    private Handler handler;
    private GameInfActBean gameInfBean;


    @Override
    public int getLayoutId() {
        return R.layout.activity_game_inf;
    }

    @Override
    public void initView() {
        videoPlayer.setVisibility(View.GONE);
        gameInfBean = new GameInfActBean();
        try {
            //错误当前
//            IjkMediaPlayer.loadLibrariesOnce(null);
//            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            this.finish();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }

    @Override
    protected void bindListener() {
        ivBtDownload.setOnClickListener(this);
        videoPlayer.setFocusable(new VideoPlayerListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
                //获取视频宽高
            }

            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {

            }

            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.start();
            }

            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                return false;
            }

            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                return false;
            }

            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.seekTo(0);
                iMediaPlayer.start();
            }

            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

            }
        });

    }


    public void loadVideo(String path) {
        videoPlayer.setVideoPath(path);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //错误
//        IjkMediaPlayer.native_profileEnd();
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void widgetClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_bt_download) {
            Toast.makeText(activity, R.string.download, Toast.LENGTH_SHORT).show();
            videoPlayer.setVisibility(View.VISIBLE);
//            loadVideo("http://v.3304399.net/yxh/media/6897/10a962bdc25a11eab275c81f66c895c6.mp4");
            ivGameMainView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
