package com.example.androidlib.view.adapter;

import android.gesture.GestureLibraries;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidlib.R;
import com.example.androidlib.view.bean.PlayerVideoBean;

import java.util.List;

public class VideoPlayerAdapter extends BaseQuickAdapter<PlayerVideoBean, BaseViewHolder> {
    public VideoPlayerAdapter(int layoutResId, @Nullable List<PlayerVideoBean> data) {
        super(layoutResId, data);
    }

    public VideoPlayerAdapter(@Nullable List<PlayerVideoBean> data) {
        super(data);
    }

    public VideoPlayerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlayerVideoBean item) {
        final ImageView imageView = helper.getView(R.id.iv_player_video);
        imageView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, imageView.getWidth(), imageView.getHeight(), 20);
            }
        });
        imageView.setClipToOutline(true);

        Glide.with(mContext).load(item.getPlayerPic()).into(imageView);
        helper.addOnClickListener(R.id.iv_player_video);
    }
}
