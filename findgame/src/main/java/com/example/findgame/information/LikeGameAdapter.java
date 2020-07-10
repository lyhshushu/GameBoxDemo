package com.example.findgame.information;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;
import com.example.findgame.bean.LikeGameBean;

import java.util.List;

public class LikeGameAdapter extends BaseQuickAdapter<LikeGameBean, BaseViewHolder> {
    public LikeGameAdapter(int layoutResId, @Nullable List<LikeGameBean> data) {
        super(layoutResId, data);
    }

    public LikeGameAdapter(@Nullable List<LikeGameBean> data) {
        super(data);
    }

    public LikeGameAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, LikeGameBean item) {
        Glide.with(mContext).load(item.getGamePic()).into((ImageView) helper.getView(R.id.game_img));
        helper.setText(R.id.game_name, item.getName())
                .setText(R.id.game_download, item.getGameInf())
                .setText(R.id.game_inf_detail, item.getGameContext())
                .setGone(R.id.game_video, false);
    }
}
