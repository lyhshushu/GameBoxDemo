package com.example.androidlib.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.androidlib.utils.VideoPlayerListener;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoPlayerIJK extends FrameLayout {

    private IMediaPlayer mMediaPlayer = null;
    private String videoPath = "";
    private SurfaceView surfaceView;
    private VideoPlayerListener listener;
    private Context mContext;


    public VideoPlayerIJK(@NonNull Context context) {
        super(context);
        initVideoPlayer(context);
    }

    public VideoPlayerIJK(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initVideoPlayer(context);
    }

    public VideoPlayerIJK(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoPlayer(context);
    }


    private void initVideoPlayer(Context context) {
        mContext = context;
        setFocusable(true);
    }

    public void setVideoPath(String path) {
        if (TextUtils.equals("", videoPath)) {
            videoPath = path;
            createSurfaceView();
        } else {
            createSurfaceView();
            videoPath = path;
            load();
        }
    }

    private void createSurfaceView() {
        surfaceView = new SurfaceView(mContext);
        surfaceView.getHolder().addCallback(new mySurfaceCallBack());
        //???显示部分设置
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        this.addView(surfaceView);
    }

    public void setFocusable(VideoPlayerListener videoPlayerListener) {

    }


    private class mySurfaceCallBack implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //surfaceView创建成功后，加载视频
            load();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    private void load() {
        createPlayer();
        try {
            mMediaPlayer.setDataSource(videoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setDisplay(surfaceView.getHolder());
        mMediaPlayer.prepareAsync();
    }

    private void createPlayer() {
        if (mMediaPlayer != null) {
            //释放
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
        }
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        //via_class 类实例访问静态成员？？？
        IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

        //硬解码开启（视频流）
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);

        mMediaPlayer = ijkMediaPlayer;
        if (listener != null) {
            mMediaPlayer.setOnPreparedListener(listener);
            mMediaPlayer.setOnInfoListener(listener);
            mMediaPlayer.setOnSeekCompleteListener(listener);
            mMediaPlayer.setOnBufferingUpdateListener(listener);
            mMediaPlayer.setOnErrorListener(listener);
        }
    }

    public void setListener(VideoPlayerListener listener) {
        this.listener = listener;
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnPreparedListener(listener);
        }
    }

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
    }

    public long getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    public long getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(long l) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(l);
        }
    }

}
