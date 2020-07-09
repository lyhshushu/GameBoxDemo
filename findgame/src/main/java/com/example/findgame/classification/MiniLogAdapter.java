package com.example.findgame.classification;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;

import java.util.List;

public class MiniLogAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MiniLogAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    public MiniLogAdapter(@Nullable List<String> data) {
        super(data);
    }

    public MiniLogAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        Glide.with(mContext).load(item).into((ImageView) helper.getView(R.id.iv_mini_logo));
    }
}
