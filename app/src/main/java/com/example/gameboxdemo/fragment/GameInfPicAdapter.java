package com.example.gameboxdemo.fragment;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView imageView = helper.getView(R.id.iv_pic_list);
        imageView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, imageView.getWidth(), imageView.getHeight(), 30);
            }
        });
        imageView.setClipToOutline(true);
        Glide.with(mContext).load(item).into(imageView);
    }
}
