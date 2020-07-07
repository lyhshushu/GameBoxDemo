package com.example.findgame.recommend;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;
import com.example.findgame.bean.PlayerRecommendBean;

import java.util.List;

public class RecommendAdapter extends BaseQuickAdapter<PlayerRecommendBean, BaseViewHolder> {

    private final static int RECENT_LENGTH = 3;

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

        String recommendNum = mContext.getString(R.string.recently) + item.getRecommendNum() + mContext.getString(R.string.recommend_people);
        int length = item.getRecommendNum().length();
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(recommendNum);
        stringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF9800")), RECENT_LENGTH, RECENT_LENGTH + length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        helper.setText(R.id.tv_player_game, item.getGameName())
                .setText(R.id.player_num, stringBuilder)
                .setText(R.id.player_word, item.getRecommendWord())
                .setText(R.id.player_name, item.getPlayerName())
                .addOnClickListener(R.id.tv_player_game)
                .addOnClickListener(R.id.player_num)
                .addOnClickListener(R.id.icon_recommend_game)
                .addOnClickListener(R.id.player_head)
                .addOnClickListener(R.id.player_name)
                .addOnClickListener(R.id.cl_card);
    }
}
