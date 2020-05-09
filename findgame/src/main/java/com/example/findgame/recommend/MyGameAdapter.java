package com.example.findgame.recommend;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;
import com.example.findgame.bean.MyGameBean;

public class MyGameAdapter extends BaseQuickAdapter<MyGameBean,BaseViewHolder> {


    public MyGameAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyGameBean item) {
        Glide.with(mContext).load(item.appIcon).into((ImageView)helper.getView(R.id.icon_my_game));
        helper.addOnClickListener(R.id.icon_my_game);
    }
}
