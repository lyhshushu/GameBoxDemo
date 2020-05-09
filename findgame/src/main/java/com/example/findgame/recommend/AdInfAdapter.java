package com.example.findgame.recommend;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;
import com.example.findgame.bean.AdvertisementBean;
import com.example.findgame.bean.GameInfBean;

import java.util.List;

public class AdInfAdapter extends BaseQuickAdapter<AdvertisementBean, BaseViewHolder> {

    public AdInfAdapter(int layoutResId) {
        super(layoutResId);
    }

    public AdInfAdapter(int layoutResId, List<AdvertisementBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdvertisementBean item) {
        helper.setText(R.id.ad_name, item.getAdName())
                .setText(R.id.ad_detail, item.getAdDetail())
                .addOnClickListener(R.id.csl_ad_game );
        switch (item.getAdType()) {
            case 0:
                Glide.with(mContext).load(R.drawable.m4399_png_shop_theme_new_flag).into((ImageView) helper.getView(R.id.ad_type));
                break;
            case 1:
                Glide.with(mContext).load(R.drawable.m4399_png_crack_cover_flow_invincible_game_flag).into((ImageView) helper.getView(R.id.ad_type));
                break;
            case 2:
                Glide.with(mContext).load(R.drawable.m4399_png_tags_mini_game_flag).into((ImageView) helper.getView(R.id.ad_type));
                break;
            case 3:
                Glide.with(mContext).load(R.drawable.m4399_png_crack_cover_flow_original_game_flag).into((ImageView) helper.getView(R.id.ad_type));
                break;
            default:
                break;
        }
    }
}
