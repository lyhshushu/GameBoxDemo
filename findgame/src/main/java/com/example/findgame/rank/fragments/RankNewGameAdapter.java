package com.example.findgame.rank.fragments;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;
import com.example.findgame.bean.AllRankBean;

import java.util.List;

public class RankNewGameAdapter extends BaseQuickAdapter<AllRankBean, BaseViewHolder> {
    public RankNewGameAdapter(int layoutResId, @Nullable List<AllRankBean> data) {
        super(layoutResId, data);
    }

    public RankNewGameAdapter(@Nullable List<AllRankBean> data) {
        super(data);
    }

    public RankNewGameAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AllRankBean item) {
        Glide.with(mContext).load(item.getGamePic()).into((ImageView) helper.getView(R.id.iv_rank_new_game));
        helper.setText(R.id.tv_rank_new_game_name, item.getGameName())
                .setText(R.id.tv_rank_new_game_inf, item.getGameInf())
                .addOnClickListener(R.id.cl_rank_new_game)
                .addOnClickListener(R.id.bt_rank_new_game_download);
    }
}
