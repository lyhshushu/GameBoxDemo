package com.example.findgame.recommend;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.findgame.FindGameFragment;
import com.example.findgame.R;
import com.example.findgame.R2;
import com.example.findgame.bean.DailyTweetBean;
import com.example.findgame.bean.GameInfBean;
import com.example.findgame.recommend.controller.MvcModelImp;
import com.example.findgame.recommend.controller.OKutil;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static com.example.androidlib.baseurl.Common.BASEURL;

public class GameInfAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private List<DailyTweetBean> dailyTweetBeans;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public GameInfAdapter(List<MultiItemEntity> data) {
        super(data);
        try {
            addItemType(1, R.layout.item_game_inf);
            addItemType(2, R.layout.item_daily_tweet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public GameInfAdapter(int layoutResId) {
//        super(layoutResId);
//    }
//
//    public GameInfAdapter(int layoutResId, List<GameInfBean> data) {
//        super(layoutResId, data);
//    }


    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        int itemType = helper.getItemViewType();
        switch (itemType) {
            case 1:
                GameInfBean gameInfBean = (GameInfBean) item;
                Glide.with(mContext).load(gameInfBean.getGameImgUrl()).into((ImageView) helper.getView(R.id.game_img));
                int downloadNum = Integer.parseInt(gameInfBean.getGameDownload());
                String download;
                if (downloadNum > 10000) {
                    downloadNum = downloadNum / 10000;
                    download = downloadNum + mContext.getString(R.string.over_10000);
                } else {
                    download = downloadNum + mContext.getString(R.string.times);
                }
                helper.setText(R.id.game_name, gameInfBean.getGameName())
                        .setText(R.id.game_download, download + mContext.getString(R.string.download))
                        .setText(R.id.game_inf_detail, gameInfBean.getGameInf())
                        .addOnClickListener(R.id.bt_download)
                        .addOnClickListener(R.id.csl_game_inf);
                break;
            case 2:
                helper.addOnClickListener(R.id.tweet);
                DailyTweetAdapter dailyTweetAdapter = new DailyTweetAdapter(R.layout.item_tweet);
                RecyclerView rvDailyTweet = helper.getView(R.id.rv_daily_tweet);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                rvDailyTweet.setLayoutManager(linearLayoutManager);

                dailyTweetAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                    int id = view.getId();
                    if (id == R.id.icon_tweet_game) {
                        Toast.makeText(mContext, position + "", Toast.LENGTH_SHORT).show();
                    }
                });
                rvDailyTweet.setAdapter(dailyTweetAdapter);

                dailyTweetBeans = RecommendFragment.dailyTweetBeans;
                dailyTweetAdapter.setNewData(dailyTweetBeans);
                dailyTweetAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }


//        Glide.with(mContext).load(item.getGameImgUrl()).into((ImageView) helper.getView(R.id.game_img));
//        int downloadNum = Integer.parseInt(item.getGameDownload());
//        String download;
//        if (downloadNum > 10000) {
//            downloadNum = downloadNum / 10000;
//            download = downloadNum + mContext.getString(R.string.over_10000);
//        } else {
//            download = downloadNum + mContext.getString(R.string.times);
//        }
//        helper.setText(R.id.game_name, item.getGameName())
//                .setText(R.id.game_download, download + mContext.getString(R.string.download))
//                .setText(R.id.game_inf_detail, item.getGameInf())
//                .addOnClickListener(R.id.bt_download)
//                .addOnClickListener(R.id.csl_game_inf);

    }

}
