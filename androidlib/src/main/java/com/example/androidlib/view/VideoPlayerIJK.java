package com.example.androidlib.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.androidlib.BaseActivity;
import com.example.androidlib.R;
import com.example.androidlib.utils.VideoPlayerListener;
import com.example.androidlib.view.adapter.VideoPlayerAdapter;
import com.example.androidlib.view.bean.PlayerVideoBean;
import com.google.android.exoplayer.chunk.ChunkExtractorWrapper;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoPlayerIJK extends FrameLayout {
    private static final int PER_SECOND = 1000;
    private static final int PER_MINUTE = 60;
    private IMediaPlayer mMediaPlayer = null;
    private String videoPath = "";
    private SurfaceView surfaceView;
    private VideoPlayerListener listener;
    private Context mContext;
    private ImageView imageView;
    private TextView allTime;
    private TextView currentTime;

    private SeekBar seekBar;
    private RecyclerView rvGameVideos;
    private ImageView allWindow;
    private boolean isAllScreen = false;
    private static List<PlayerVideoBean> playerVideoBeans;
    private WeakReference<BaseActivity> activity;

    private static VideoPlayerIJK videoPlayerIJK = null;

    public static VideoPlayerIJK getInstance(Context context) {
        if (videoPlayerIJK == null) {
            synchronized (VideoPlayerIJK.class) {
                if (videoPlayerIJK == null) {
                    videoPlayerIJK = new VideoPlayerIJK(context);
                }
            }
        }
        return videoPlayerIJK;
    }

    public static VideoPlayerIJK getInstance() {
        return videoPlayerIJK;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    getCurrentPosition();
            }
        }
    };

    public static List<PlayerVideoBean> getPlayerVideoBeans() {
        return playerVideoBeans;
    }

    public static void setPlayerVideoBeans(List<PlayerVideoBean> playerVideoBeans) {
        VideoPlayerIJK.playerVideoBeans = playerVideoBeans;
    }

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
            if (surfaceView == null) {
                createSurfaceView();
            }
        } else {
            if (surfaceView == null) {
                createSurfaceView();
            }
            videoPath = path;
            load();
        }
    }

    public void setActivity(WeakReference<BaseActivity> activity1) {
        activity = activity1;
    }


    private void createSurfaceView() {
        //surfaceView
        surfaceView = new SurfaceView(mContext);
        surfaceView.getHolder().addCallback(new MySurfaceCallBack());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        surfaceView.setLayoutParams(layoutParams);
        this.addView(surfaceView);
        //暂停图片
        imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.m4399_png_share_video_icon_play_gary);
        LayoutParams layoutParamsImage = new LayoutParams(100, 100, Gravity.CENTER);
        imageView.setLayoutParams(layoutParamsImage);
        this.addView(imageView);
        //总时间
        allTime = new TextView(mContext);
        allTime.setTextColor(getResources().getColor(R.color.white));
        LayoutParams layoutParamsAllTime = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END);
        layoutParamsAllTime.rightMargin = 80;
        layoutParamsAllTime.bottomMargin = 20;
        allTime.setLayoutParams(layoutParamsAllTime);
        this.addView(allTime);
        //当前时间
        currentTime = new TextView(mContext);
        currentTime.setTextColor(getResources().getColor(R.color.white));
        LayoutParams layoutParamsCurrentTime = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.START);
        layoutParamsCurrentTime.leftMargin = 80;
        layoutParamsCurrentTime.bottomMargin = 20;
        currentTime.setLayoutParams(layoutParamsCurrentTime);
        this.addView(currentTime);
        //seekBar
        seekBar = new SeekBar(mContext);
        LayoutParams layoutParamsSeekBar = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        seekBar.setLayoutParams(layoutParamsSeekBar);
        layoutParamsSeekBar.leftMargin = 150;
        layoutParamsSeekBar.rightMargin = 150;
        layoutParamsSeekBar.bottomMargin = 20;
        this.addView(seekBar);
        //放大图标
        allWindow = new ImageView(mContext);
        LayoutParams layoutParamsWindow = new LayoutParams(40, 40, Gravity.BOTTOM | Gravity.END);
        layoutParamsWindow.rightMargin = 20;
        layoutParamsWindow.bottomMargin = 20;
        allWindow.setImageResource(R.drawable.m4399_png_screen_icon);
        allWindow.setLayoutParams(layoutParamsWindow);
        this.addView(allWindow);

        //recyclerView
        rvGameVideos = new RecyclerView(mContext);
        LayoutParams layoutParamsRecyclerView = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.START);
        layoutParamsRecyclerView.topMargin = 20;
        layoutParamsRecyclerView.leftMargin = 150;
        rvGameVideos.setLayoutParams(layoutParamsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        playerVideoBeans = getPlayerVideoBeans();
        rvGameVideos.setLayoutManager(layoutManager);
        VideoPlayerAdapter videoPlayerAdapter = new VideoPlayerAdapter(R.layout.item_game_video);


        videoPlayerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.iv_player_video) {
                    videoPath = playerVideoBeans.get(position).getPlayerVideo();
                    load();
                }
            }
        });
        rvGameVideos.setAdapter(videoPlayerAdapter);
        videoPlayerAdapter.setNewData(playerVideoBeans);
        videoPlayerAdapter.notifyDataSetChanged();
        this.addView(rvGameVideos);
        setInvisible();


        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    imageView.setImageResource(R.drawable.m4399_png_share_video_icon_play_gary);
                } else {
                    mMediaPlayer.start();
                    setInvisible();
                }
            }
        });
        allWindow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAllScreen) {
                    Intent intent = new Intent();
                    intent.setAction("open_game_video_activity");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.getApplicationContext().startActivity(intent);
                    isAllScreen = true;
                } else {
                    activity.get().finish();
                    isAllScreen = false;
                }
            }
        });


        imageView.clearFocus();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacksAndMessages(null);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(getDuration() * seekBar.getProgress() / 100);
//                mMediaPlayer.seekTo(mMediaPlayer.getDuration() * seekBar.getProgress() / 100);
                handler.sendEmptyMessage(1);
            }
        });


        surfaceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getVisibility() == INVISIBLE) {
                    setVisible();
                    if (mMediaPlayer.isPlaying()) {
                        imageView.setImageResource(R.drawable.m4399_png_video_pause_pressed);
                    } else {
                        imageView.setImageResource(R.drawable.m4399_png_share_video_icon_play_gary);
                    }
                } else {
                    setInvisible();
                }
            }
        });
        surfaceView.setFocusable(true);
        surfaceView.setFocusableInTouchMode(false);
        surfaceView.requestFocus();
        currentTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void setInvisible() {
        rvGameVideos.setVisibility(INVISIBLE);
        seekBar.setVisibility(INVISIBLE);
        allTime.setVisibility(INVISIBLE);
        currentTime.setVisibility(INVISIBLE);
        imageView.clearFocus();
        surfaceView.requestFocus();
        imageView.setVisibility(INVISIBLE);
        allWindow.setVisibility(INVISIBLE);
    }

    private void setVisible() {
        rvGameVideos.setVisibility(VISIBLE);
        seekBar.setVisibility(VISIBLE);
        allTime.setVisibility(VISIBLE);
        currentTime.setVisibility(VISIBLE);
        imageView.setVisibility(VISIBLE);
        allWindow.setVisibility(VISIBLE);
    }

    public void setFocusable(VideoPlayerListener videoPlayerListener) {

    }

    private class MySurfaceCallBack implements SurfaceHolder.Callback {

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

    long lastPosition = 0;

    public long getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(long lastPosition) {
        this.lastPosition = lastPosition;
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
        setInvisible();
        mMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                getDuration();
                seekTo(getLastPosition());
                new TimeListener().start();
            }
        });

        mMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                imageView.setImageResource(R.drawable.m4399_png_user_homepage_header_refresh_btn);
                imageView.setVisibility(VISIBLE);
            }
        });

        mMediaPlayer.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {

            }
        });
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
        //缓冲区间，解决currentPosition大于duration问题
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 100 * 1024);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 20);

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
            imageView.setImageResource(R.drawable.m4399_png_share_video_icon_play_gary);
//            imageView.setVisibility(INVISIBLE);
            setInvisible();
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
            imageView.setVisibility(VISIBLE);
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            imageView.setVisibility(VISIBLE);
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
            String time;
            float timeInt = (float) mMediaPlayer.getDuration() / (float) PER_SECOND;
            time = (int) timeInt / PER_MINUTE + ":" + (int) (timeInt % PER_MINUTE);
            allTime.setText(time);
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    public long getCurrentPosition() {
        if (mMediaPlayer != null) {
            String time;
            float timeInt;
            if (mMediaPlayer.getCurrentPosition() >= getDuration()) {
                timeInt = (float) mMediaPlayer.getDuration() / (float) PER_SECOND;
            } else {
                timeInt = (float) mMediaPlayer.getCurrentPosition() / (float) PER_SECOND;
            }
            time = (int) (timeInt / PER_MINUTE) + ":" + (int) (timeInt % PER_MINUTE);
            currentTime.setText(time);
            float percent = 0;
            if (getDuration() != 0) {
                percent = (float) mMediaPlayer.getCurrentPosition() / (float) mMediaPlayer.getDuration() * 100;
//                percent = (float) ((mMediaPlayer.getCurrentPosition() * 100) / getDuration());
            }
            seekBar.setProgress((int) percent);
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(long l) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(l);
            if (!isPlaying()) {
                imageView.setImageResource(R.drawable.m4399_png_share_video_icon_play_gary);
            }
        }
    }


    public boolean isPlaying() {
        if (mMediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    public class TimeListener extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(500);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
}
