package com.example.findgame.recommend;

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

public class NewHotAdapter extends BaseQuickAdapter<PlayerRecommendBean, BaseViewHolder> {
    public NewHotAdapter(int layoutResId, @Nullable List<PlayerRecommendBean> data) {
        super(layoutResId, data);
    }

    public NewHotAdapter(@Nullable List<PlayerRecommendBean> data) {
        super(data);
    }

    public NewHotAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlayerRecommendBean item) {
        Glide.with(mContext).load(item.getGamePicUrl()).into((ImageView) helper.getView(R.id.iv_new_hot));

        String dateWithContent = item.getRecommendWord();
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(dateWithContent);
        stringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#54BA3D")), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        helper.setText(R.id.tv_hot_game_inf, stringBuilder)
                .setText(R.id.tv_hot_game_name, item.getGameName())
                .addOnClickListener(R.id.cl_new_hot_game);
    }
}
