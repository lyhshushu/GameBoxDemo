package com.example.findgame.recommend;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;
import com.example.findgame.bean.PlayerRecommendBean;

import java.util.List;

public class RecommendAdapter extends BaseQuickAdapter<PlayerRecommendBean, BaseViewHolder> {
    public RecommendAdapter(int layoutResId, @Nullable List<PlayerRecommendBean> data) {
        super(layoutResId, data);
    }

    public RecommendAdapter(@Nullable List<PlayerRecommendBean> data) {
        super(data);
    }

    public RecommendAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlayerRecommendBean item) {
        Glide.with(mContext).load(item.getGamePicUrl()).into((ImageView) helper.getView(R.id.icon_recommend_game));
        Glide.with(mContext).load(item.getHeadPic()).into((ImageView) helper.getView(R.id.player_head));
        helper.setText(R.id.tv_player_game, item.getGameName())
                .setText(R.id.player_num, "最近有" + item.getRecommendNum() + "人推荐")
                .setText(R.id.player_word, item.getRecommendWord())
                .setText(R.id.player_name, item.getPlayerName())
                .addOnClickListener(R.id.cl_card);
    }
}
