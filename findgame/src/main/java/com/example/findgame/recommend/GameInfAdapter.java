package com.example.findgame.recommend;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.androidlib.view.MyHorizontalRecyclerView;
import com.example.findgame.FindGameFragment;
import com.example.findgame.R;
import com.example.findgame.bean.DailyTweetBean;
import com.example.findgame.bean.GameInfBean;
import com.example.findgame.bean.PlayerRecommendBean;

import java.util.List;

import static com.example.androidlib.baseurl.Common.BASEURL;

public class GameInfAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private List<PlayerRecommendBean> playerRecommendBean;
    private final static int OVER_10000 = 10000;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public GameInfAdapter(List<MultiItemEntity> data) {
        super(data);
        try {
            addItemType(0, R.layout.item_game_inf);
            addItemType(1, R.layout.item_daily_tweet);
            addItemType(19, R.layout.item_daily_tweet);
            addItemType(23, R.layout.item_daily_tweet);
            addItemType(24, R.layout.item_player_recommend);
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
            case 0:
                GameInfBean gameInfBean = (GameInfBean) item;
                Glide.with(mContext).load(gameInfBean.getGameImgUrl()).into((ImageView) helper.getView(R.id.game_img));
                int downloadNum = Integer.parseInt(gameInfBean.getGameDownload());
                String download;
                if (downloadNum > OVER_10000) {
                    downloadNum = downloadNum / OVER_10000;
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
            case 1:
                GameInfBean gameInfBeanTweet = (GameInfBean) item;
                helper.setText(R.id.type_tweet, gameInfBeanTweet.getTitleName())
                        .addOnClickListener(R.id.tweet);
                DailyTweetAdapter dailyTweetAdapter = new DailyTweetAdapter(R.layout.item_tweet);
                MyHorizontalRecyclerView rvDailyTweet = helper.getView(R.id.rv_daily_tweet);
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
                List<DailyTweetBean> dailyTweetBeans = RecommendFragment.dailyTweetBeans;
                dailyTweetAdapter.setNewData(dailyTweetBeans);
                dailyTweetAdapter.notifyDataSetChanged();
                break;
            case 19:
                GameInfBean gameInfBeanNewGame = (GameInfBean) item;
                helper.setText(R.id.type_tweet, gameInfBeanNewGame.getTitleName())
                        .addOnClickListener(R.id.tweet);
                NewGameAdapter newGameAdapter = new NewGameAdapter(R.layout.item_new_game);
                MyHorizontalRecyclerView rvNewGame = helper.getView(R.id.rv_daily_tweet);
                LinearLayoutManager linearLayoutManagerNewGame = new LinearLayoutManager(mContext);
                linearLayoutManagerNewGame.setOrientation(RecyclerView.HORIZONTAL);
                rvNewGame.setLayoutManager(linearLayoutManagerNewGame);
                newGameAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                    int id = view.getId();
                    if (id == R.id.new_game_icon) {
                        Toast.makeText(mContext, position + "", Toast.LENGTH_SHORT).show();
                    }
                });

                rvNewGame.setAdapter(newGameAdapter);
                List<DailyTweetBean> newGameBean = RecommendFragment.newGameBeans;
                newGameAdapter.setNewData(newGameBean);
                newGameAdapter.notifyDataSetChanged();
                break;
            case 23:
                GameInfBean gameInfBeanTestGame = (GameInfBean) item;
                helper.setText(R.id.type_tweet, gameInfBeanTestGame.getTitleName())
                        .addOnClickListener(R.id.tweet);

                NewGameAdapter testGameAdapter = new NewGameAdapter(R.layout.item_new_game);
                MyHorizontalRecyclerView rvTestGame = helper.getView(R.id.rv_daily_tweet);
                LinearLayoutManager linearLayoutManagerTestGame = new LinearLayoutManager(mContext);
                linearLayoutManagerTestGame.setOrientation(RecyclerView.HORIZONTAL);

                rvTestGame.setLayoutManager(linearLayoutManagerTestGame);
                testGameAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                    int id = view.getId();
                    if (id == R.id.new_game_icon) {
                        Toast.makeText(mContext, position + "", Toast.LENGTH_SHORT).show();
                    }
                });

                rvTestGame.setAdapter(testGameAdapter);
                List<DailyTweetBean> testGameBean = RecommendFragment.testGameBeans;
                testGameAdapter.setNewData(testGameBean);
                testGameAdapter.notifyDataSetChanged();
                break;
            case 24:
                GameInfBean gameInfBeanRecommend = (GameInfBean) item;
                helper.setText(R.id.tv_player_recommend, ((GameInfBean) item).getTitleName())
                        .addOnClickListener(R.id.cl_recommend);

                RecommendAdapter recommendAdapter = new RecommendAdapter(R.layout.item_player_word_card);
                MyHorizontalRecyclerView rvRecommendCard = helper.getView(R.id.rv_play_word);
                LinearLayoutManager linearLayoutManagerRecommend = new LinearLayoutManager(mContext);
                linearLayoutManagerRecommend.setOrientation(RecyclerView.HORIZONTAL);
                rvRecommendCard.setLayoutManager(linearLayoutManagerRecommend);

                recommendAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                    int id = view.getId();
                    if (id == R.id.icon_recommend_game || id == R.id.tv_player_game || id == R.id.player_num) {
                        Toast.makeText(mContext, position + "game", Toast.LENGTH_SHORT).show();
                    } else if (id == R.id.player_head || id == R.id.player_name) {
                        Toast.makeText(mContext, position + "player", Toast.LENGTH_SHORT).show();
                    } else if (id == R.id.cl_card) {
                        Toast.makeText(mContext, position + "card", Toast.LENGTH_SHORT).show();
                    }
                });

                rvRecommendCard.setAdapter(recommendAdapter);
                playerRecommendBean = RecommendFragment.playerRecommendBeans;
                recommendAdapter.setNewData(playerRecommendBean);
                recommendAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

}
