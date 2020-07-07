package com.example.findgame.recommend;

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
import com.example.findgame.R;
import com.example.findgame.bean.DailyTweetBean;

import java.util.List;

public class NewGameAdapter extends BaseQuickAdapter<DailyTweetBean, BaseViewHolder> {
    public NewGameAdapter(int layoutResId, @Nullable List<DailyTweetBean> data) {
        super(layoutResId, data);
    }

    public NewGameAdapter(@Nullable List<DailyTweetBean> data) {
        super(data);
    }

    public NewGameAdapter(int layoutResId) {
        super(layoutResId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void convert(BaseViewHolder helper, DailyTweetBean item) {

        ImageView pic = helper.getView(R.id.new_game_icon);

        pic.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, pic.getWidth(), pic.getHeight(), 30);
            }
        });
        pic.setClipToOutline(true);


        Glide.with(mContext).load(item.getTweetPicUrl()).into(pic);
        helper.setText(R.id.new_game_name, item.getTweetName())
                .setText(R.id.tv_update_time, item.getNewGameTime())
                .setText(R.id.tv_weekday, item.getTweetTime())
                .addOnClickListener(R.id.new_game_icon);
        if (item.getNewGameTime() == null) {
            helper.setGone(R.id.tv_update_time, false);
        }
        if (item.getTweetTime() == null) {
            helper.setGone(R.id.tv_weekday, false);
        }
    }
}
