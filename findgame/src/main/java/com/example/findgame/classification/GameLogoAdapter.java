package com.example.findgame.classification;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;
import com.example.findgame.bean.AllClassificationBean;

import java.util.List;

public class GameLogoAdapter extends BaseQuickAdapter<AllClassificationBean, BaseViewHolder> {
    public GameLogoAdapter(int layoutResId, @Nullable List<AllClassificationBean> data) {
        super(layoutResId, data);
    }

    public GameLogoAdapter(@Nullable List<AllClassificationBean> data) {
        super(data);
    }

    public GameLogoAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AllClassificationBean item) {
        Glide.with(mContext).load(item.getPicUrl()).into((ImageView) helper.getView(R.id.logo_pic));
        helper.setText(R.id.tv_logo, item.getName())
                .addOnClickListener(R.id.cl_logo);
    }
}
