package com.example.gameboxdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.androidlib.BaseActivity;
import com.example.androidlib.view.InterToolbar;
import com.example.androidlib.view.VerticalScrollView;
import com.example.androidlib.view.VideoPlayerIJK;
import com.example.androidlib.view.bean.PlayerVideoBean;
import com.example.findgame.FindGameFragment;
import com.example.findgame.recommend.controller.MvcModelImp;
import com.example.findgame.recommend.controller.OKutil;
import com.example.gameboxdemo.bean.AppPermissionBean;
import com.example.gameboxdemo.bean.GameInfActBean;
import com.example.gameboxdemo.fragment.GameInfIntroduceFragemt;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.testinject.injectutil.InjectView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static com.example.androidlib.baseurl.Common.BASEURL;

@InjectView(R.layout.activity_game_inf)
@Route(path = "/app/GameInfActivity")
public class GameInfActivity extends BaseActivity {
    private static final int MAX_ALPHA = 255;

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
    @BindView(R.id.cl_game_inf)
    ConstraintLayout clGameInf;
    @BindView(R.id.stl_game_inf)
    SlidingTabLayout stlGameInf;
    @BindView(R.id.vp_game_inf)
    ViewPager vpGameInf;
    @BindView(R.id.vs_game_inf)
    VerticalScrollView vsGameInf;
    @BindView(R.id.tb_title)
    InterToolbar tbTitle;
    @BindView(R.id.ctb_info_title)
    CollapsingToolbarLayout ctbInfoTitle;
    @BindView(R.id.abl_layout)
    AppBarLayout ablLayout;
    @BindView(R.id.tv_title_game_name)
    TextView tvTitleGameName;
    @BindView(R.id.cl_game_video)
    ConstraintLayout clGameVideo;

    private String gameId;
    private Handler handler;
    public GameInfActBean gameInfBean;
    private List<PlayerVideoBean> playerVideoBeans;
    private List<String> titles;

    @Override
    public void initView() {
        ARouter.getInstance().inject(this);
//        addVideoView();
        VideoPlayerIJK.getInstance(getApplicationContext()).setVisibility(View.INVISIBLE);
        try {
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
        gameInfBean = new GameInfActBean();

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
        setSupportActionBar(tbTitle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        tbTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tbTitle.getBackground().mutate().setAlpha(0);
        ablLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    tbTitle.getBackground().mutate().setAlpha(0);
                    tvTitleGameName.setText("");
                } else if (Math.abs(verticalOffset) >= MAX_ALPHA) {
                    tvTitleGameName.setText(gameInfBean.getAppName());
                    tbTitle.getBackground().mutate().setAlpha(MAX_ALPHA);
                } else {
                    tbTitle.getBackground().mutate().setAlpha(Math.abs(verticalOffset));
                    tvTitleGameName.setText("");
                }
            }
        });

    }

    private void addVideoView() {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams
                (ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        clGameVideo.addView(VideoPlayerIJK.getInstance(getApplicationContext()), params);
    }

    @Override
    protected void bindListener() {
        ivBtDownload.setOnClickListener(this);
        VideoPlayerIJK.getInstance(getApplicationContext()).setOnClickListener(this);
        ivGameMainView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        addVideoView();
        super.onResume();
    }

    @Override
    protected void onPause() {
        ivGameMainView.setVisibility(View.VISIBLE);
        super.onPause();
        VideoPlayerIJK.getInstance().setLastPosition(VideoPlayerIJK.getInstance().getCurrentPosition());
        clGameVideo.removeView(VideoPlayerIJK.getInstance());
    }

    public void loadVideo(String path) {
//        videoPlayer.setVideoPath(path);
        VideoPlayerIJK.getInstance(getApplicationContext()).setVideoPath(path);
    }

    @Override
    protected void onStop() {
        super.onStop();
        IjkMediaPlayer.native_profileEnd();

    }

    @Override
    protected void onDestroy() {
//        videoPlayer.release();
        VideoPlayerIJK.getInstance().setLastPosition(0);
        VideoPlayerIJK.getInstance().release();
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
                    EventBus.getDefault().post(gameInfBean);
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
            String newGameInf = null;
            int downNum = Integer.parseInt(resultJson.getString("downnum"));
            if (downNum > OVER_10000) {
                downNum = downNum / OVER_10000;
                newGameInf = downNum + getString(com.example.findgame.R.string.over_10000) + getString(com.example.findgame.R.string.download) + "  " + gameSize;
            } else {
                newGameInf = downNum + getString(com.example.findgame.R.string.times) + getString(com.example.findgame.R.string.download) + "  " + gameSize;
            }
            String videos = resultJson.getString("videos");
            JSONArray videosJson = new JSONArray(videos);
            JSONObject videoJson = videosJson.getJSONObject(0);
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
            String dev = resultJson.getString("dev_info");
            JSONObject devJson = new JSONObject(dev);

            String dateLine = resultJson.getString("dateline");
            long date = Long.parseLong(dateLine) * 1000;
            Timestamp timestamp = new Timestamp(date);
            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat(getString(com.example.findgame.R.string.date_with_line));
            String dateString = dateFormat.format(timestamp);

            String permission = resultJson.getString("apk_permission");
            List<AppPermissionBean> permissionBeans = new LinkedList<>();
            JSONArray permissionList = new JSONArray(permission);
            for (int i = 0; i < permissionList.length(); i++) {
                AppPermissionBean permissionBean = new AppPermissionBean();
                JSONObject permissionJson = permissionList.getJSONObject(i);
                permissionBean.setPermissionDes(permissionJson.getString("desc"));
                permissionBean.setPermissionTitle(permissionJson.getString("title"));
                permissionBeans.add(permissionBean);
            }
            String screenPath = resultJson.getString("screenpath");
            JSONArray screenPathJson = new JSONArray(screenPath);
            List<String> screenList = new LinkedList<>();
            for (int i = 0; i < screenPathJson.length(); i++) {
                screenList.add((String) screenPathJson.get(i));
            }
            String tags = resultJson.getString("tags");
            List<String> tagList = new LinkedList<>();
            JSONArray tagsJson = new JSONArray(tags);
            for (int i = 0; i < tagsJson.length(); i++) {
                JSONObject tagJson = tagsJson.getJSONObject(i);
                tagList.add(tagJson.getString("name"));
            }
            gameInfBean.setAppTags(tagList);
            gameInfBean.setAppScreen(screenList);
            gameInfBean.setAppPermissionBeans(permissionBeans);
            gameInfBean.setAppVersion(resultJson.getString("version") + " | " + dateString + "更新");
            gameInfBean.setAppCreator(devJson.getString("name"));
            gameInfBean.setAppSize(gameSize);
            gameInfBean.setAppDownload(newGameInf);
            gameInfBean.setAppIcon(resultJson.getString("icopath"));
            gameInfBean.setAppName(resultJson.getString("appname"));
            gameInfBean.setAppTitlePic(videoJson.getString("img"));
            gameInfBean.setAppVideo(videoJson.getString("url"));
            gameInfBean.setAppInfDetail(resultJson.getString("appinfo"));
            gameInfBean.setAppWarmTip(resultJson.getString("note"));
            gameInfBean.setAppPicList(picList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void widgetClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_Game_main_view:
                loadVideo(gameInfBean.getAppVideo());
                VideoPlayerIJK.getInstance(getApplicationContext()).setVisibility(View.VISIBLE);
                ivGameMainView.setVisibility(View.INVISIBLE);
                ivGameMainView.setFocusable(false);
                break;
            case R.id.iv_bt_download:
//                clGameVideo.removeView(VideoPlayerIJK.getInstance(activity));
//                Intent intent = new Intent(this, VideoPlayerActivity.class);
//                activity.startActivity(intent);
                ARouter.getInstance().build("/app/MainActivity")
                        .withString("gameId", gameId)
                        .navigation();
                Toast.makeText(activity, getString(R.string.download), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


//    @RequiresApi(api = Build.VERSION_CODES.P)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ButterKnife.bind(this);
//    }


    /**
     * 获取屏幕的宽
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
}
