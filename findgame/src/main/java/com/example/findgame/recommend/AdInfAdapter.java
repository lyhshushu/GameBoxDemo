package com.example.findgame.recommend;


import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;
import com.example.findgame.bean.AdvertisementBean;

import java.util.List;

public class AdInfAdapter extends BaseQuickAdapter<AdvertisementBean, BaseViewHolder> {

    public AdInfAdapter(int layoutResId) {
        super(layoutResId);
    }

    public AdInfAdapter(int layoutResId, List<AdvertisementBean> data) {
        super(layoutResId, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void convert(BaseViewHolder helper, AdvertisementBean item) {

        ConstraintLayout constraintLayout = helper.getView(R.id.csl_ad_game);

        constraintLayout.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, constraintLayout.getWidth(), constraintLayout.getHeight(), 30);
            }
        });
        constraintLayout.setClipToOutline(true);


        helper.setText(R.id.ad_name, item.getAdName())
                .setText(R.id.ad_detail, item.getAdDetail())
                .addOnClickListener(R.id.csl_ad_game);
        //添加type，已移除
//        switch (item.getAdType()) {
//            case 0:
//                Glide.with(mContext).load(R.drawable.m4399_png_shop_theme_new_flag).into((ImageView) helper.getView(R.id.ad_type));
//                break;
//            case 1:
//                Glide.with(mContext).load(R.drawable.m4399_png_crack_cover_flow_invincible_game_flag).into((ImageView) helper.getView(R.id.ad_type));
//                break;
//            case 2:
//                Glide.with(mContext).load(R.drawable.m4399_png_tags_mini_game_flag).into((ImageView) helper.getView(R.id.ad_type));
//                break;
//            case 3:
//                Glide.with(mContext).load(R.drawable.m4399_png_crack_cover_flow_original_game_flag).into((ImageView) helper.getView(R.id.ad_type));
//                break;
//            default:
//                break;
//        }
    }
}
