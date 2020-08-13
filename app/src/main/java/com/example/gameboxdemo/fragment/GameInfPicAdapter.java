package com.example.gameboxdemo.fragment;

import android.app.DownloadManager;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidlib.utils.OutLineSetter;
import com.example.gameboxdemo.R;

import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class GameInfPicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public GameInfPicAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    public GameInfPicAdapter(@Nullable List<String> data) {
        super(data);
    }

    public GameInfPicAdapter(int layoutResId) {
        super(layoutResId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView imageView = helper.getView(R.id.iv_pic_list);
        OutLineSetter.setOutLine(imageView, 30);
        Glide.with(mContext)
                .load(item)
                .thumbnail(0.0000001f)
                .placeholder(R.drawable.m4399_png_crack_game_carouse_bg)
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(item + "item");
            }
        });
    }
}
