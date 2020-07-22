package com.example.androidlib.view.adapter;

import android.gesture.GestureLibraries;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidlib.R;
import com.example.androidlib.utils.OutLineSetter;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void convert(BaseViewHolder helper, PlayerVideoBean item) {
        final ImageView imageView = helper.getView(R.id.iv_player_video);
        OutLineSetter.setOutLine(imageView,20);

        Glide.with(mContext).load(item.getPlayerPic()).into(imageView);
        helper.addOnClickListener(R.id.iv_player_video);
    }
}
