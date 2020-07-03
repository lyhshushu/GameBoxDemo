package com.example.findgame.recommend;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;
import com.example.findgame.R2;
import com.example.findgame.bean.GameInfBean;

import java.util.List;

public class GameInfAdapter extends BaseQuickAdapter<GameInfBean, BaseViewHolder> {

    public GameInfAdapter(int layoutResId) {
        super(layoutResId);
    }

    public GameInfAdapter(int layoutResId, List<GameInfBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameInfBean item) {
        Glide.with(mContext).load(item.getGameImgUrl()).into((ImageView) helper.getView(R.id.game_img));
        int downloadNum = Integer.parseInt(item.getGameDownload());
        String download;
        if (downloadNum > 10000) {
            downloadNum = downloadNum / 10000;
            download = downloadNum + mContext.getString(R.string.over_10000);
        } else {
            download = downloadNum + mContext.getString(R.string.times);
        }
        helper.setText(R.id.game_name, item.getGameName())
                .setText(R.id.game_download, download + mContext.getString(R.string.download))
                .setText(R.id.game_inf_detail, item.getGameInf())
                .addOnClickListener(R.id.bt_download)
                .addOnClickListener(R.id.csl_game_inf);

    }
}
