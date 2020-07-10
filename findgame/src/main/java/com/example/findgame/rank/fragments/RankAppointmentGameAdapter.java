package com.example.findgame.rank.fragments;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;
import com.example.findgame.bean.AllRankBean;

import java.util.List;

public class RankAppointmentGameAdapter extends BaseQuickAdapter<AllRankBean, BaseViewHolder> {
    public RankAppointmentGameAdapter(int layoutResId, @Nullable List<AllRankBean> data) {
        super(layoutResId, data);
    }

    public RankAppointmentGameAdapter(@Nullable List<AllRankBean> data) {
        super(data);
    }

    public RankAppointmentGameAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AllRankBean item) {
        Glide.with(mContext).load(item.getGamePic()).into((ImageView) helper.getView(R.id.iv_rank_new_game));
        helper.setText(R.id.tv_rank_new_game_name, item.getGameName())
                .setText(R.id.tv_rank_new_game_inf, item.getGameInf())
                .setText(R.id.tv_rank_new_game_rank, item.getRank())
                .addOnClickListener(R.id.cl_rank_new_game)
                .addOnClickListener(R.id.bt_rank_new_game_appointment)
                .setGone(R.id.bt_rank_new_game_download, false);
        if (item.getGameContent() == null) {
            helper.setGone(R.id.tv_rank_new_game_content, false);
        } else {
            helper.setVisible(R.id.tv_rank_new_game_content, true)
                    .setText(R.id.tv_rank_new_game_content, item.getGameContent());
        }
    }
}
