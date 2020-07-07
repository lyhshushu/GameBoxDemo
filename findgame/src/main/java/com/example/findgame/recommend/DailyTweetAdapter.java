package com.example.findgame.recommend;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;
import com.example.findgame.bean.DailyTweetBean;

import java.util.List;

public class DailyTweetAdapter extends BaseQuickAdapter<DailyTweetBean, BaseViewHolder> {
    public DailyTweetAdapter(int layoutResId, @Nullable List<DailyTweetBean> data) {
        super(layoutResId, data);
    }

    public DailyTweetAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, DailyTweetBean item) {
        Glide.with(mContext).load(item.getTweetPicUrl()).into((ImageView) helper.getView(R.id.icon_tweet_game));
        helper.setText(R.id.tv_tweet_name, item.getTweetName())
                .setText(R.id.tv_tweet_time, item.getTweetTime())
                .addOnClickListener(R.id.icon_tweet_game);

//        Glide.with(mContext).load(item.getTweetPicUrl()).into((ImageView) helper.getView(R.id.new_game_icon));
//        helper.setText(R.id.new_game_name,item.getTweetName())
//                .setText(R.id.tv_update_time,item.getNewGameTime())
//                .setText(R.id.tv_weekday,item.getTweetTime())
//                .addOnClickListener(R.id.icon_tweet_game);
    }
}
