package com.example.gameboxdemo;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.androidlib.BaseActivity;
import com.example.androidlib.view.VideoPlayerIJK;
import com.testinject.injectutil.InjectView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

@InjectView(R.layout.activity_video_player)
public class VideoPlayerActivity extends BaseActivity {

    @BindView(R.id.ck_game_act_video)
    ConstraintLayout ckGameActVideo;

    private static WeakReference<BaseActivity> activityWeakReference;

    @Override
    public void initView() {
        addVideoView();
        activityWeakReference = new WeakReference<>(this);
        VideoPlayerIJK.getInstance().setActivity(activityWeakReference);
    }

    @Override
    public void initData() {

    }

    @Override
    protected boolean isHaveBar() {
        return false;
    }

    private void addVideoView() {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams
                (ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        ckGameActVideo.addView(VideoPlayerIJK.getInstance(activity), params);
    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoPlayerIJK.getInstance().setLastPosition(VideoPlayerIJK.getInstance().getCurrentPosition());
        ckGameActVideo.removeView(VideoPlayerIJK.getInstance());
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void bindListener() {
        super.bindListener();
    }
}

