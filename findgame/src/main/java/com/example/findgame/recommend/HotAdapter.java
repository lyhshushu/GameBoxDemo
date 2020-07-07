package com.example.findgame.recommend;

import android.provider.ContactsContract;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidlib.BaseActivity;
import com.example.findgame.R;
import com.example.findgame.bean.DailyTweetBean;

import java.util.List;

public class HotAdapter extends BaseQuickAdapter<DailyTweetBean, BaseViewHolder> {
    public HotAdapter(int layoutResId, @Nullable List<DailyTweetBean> data) {
        super(layoutResId, data);
    }

    public HotAdapter(@Nullable List<DailyTweetBean> data) {
        super(data);
    }

    public HotAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, DailyTweetBean item) {
        ImageView hotPic=helper.getView(R.id.hot_game);

        Glide.with(mContext).load(item.getTweetPicUrl()).into((ImageView)helper.getView(R.id.hot_game));
        helper.addOnClickListener(R.id.hot_game);
    }
}
