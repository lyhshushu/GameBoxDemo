package com.example.findgame.recommend;

import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.androidlib.view.MyHorizontalRecyclerView;
import com.example.findgame.R;
import com.example.findgame.bean.DailyTweetBean;
import com.example.findgame.bean.GameInfBean;
import com.example.findgame.bean.PlayerRecommendBean;

import java.util.LinkedList;
import java.util.List;

public class GameInfAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private List<GameInfBean> bigTitleBeans;
    private List<Integer> flagList;
    private final static int OVER_10000 = 10000;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public GameInfAdapter(List<MultiItemEntity> data) {
        super(data);
        flagList = new LinkedList<>();
        try {
            addItemType(0, R.layout.item_game_inf);
            flagList.add(0);
            addItemType(1, R.layout.item_daily_tweet);
            flagList.add(0);
            addItemType(19, R.layout.item_daily_tweet);
            flagList.add(0);
            addItemType(23, R.layout.item_daily_tweet);
            flagList.add(0);
            addItemType(24, R.layout.item_player_recommend);
            flagList.add(0);
            addItemType(9, R.layout.item_daily_tweet);
            flagList.add(0);
            addItemType(25, R.layout.item_daily_tweet);
            flagList.add(0);
            addItemType(15, R.layout.item_daily_tweet);
            flagList.add(0);
            addItemType(26, R.layout.item_player_recommend);
            flagList.add(0);
            addItemType(18, R.layout.item_ad_inf);
            flagList.add(0);
            addItemType(2, R.layout.item_daily_tweet);
            flagList.add(0);
            addItemType(21, R.layout.item_daily_tweet);
            addItemType(13, R.layout.item_daily_tweet);
            addItemType(22,R.layout.item_daily_tweet);
            addItemType(6, R.layout.item_ad_inf);
            addItemType(4,R.layout.item_ad_inf);
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        int itemType = helper.getItemViewType();
        switch (itemType) {
            //普通游戏信息列表，0
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
            //特推，1
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
            //新游，2
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
            //开测，3
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
            //玩家推，4
            case 24:
                GameInfBean gameInfBeanRecommend = (GameInfBean) item;
                helper.setText(R.id.tv_player_recommend, gameInfBeanRecommend.getTitleName())
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
                List<PlayerRecommendBean> playerRecommendBean = RecommendFragment.playerRecommendBeans;
                recommendAdapter.setNewData(playerRecommendBean);
                recommendAdapter.notifyDataSetChanged();
                break;
            //感兴趣，5
            case 9:
                GameInfBean gameInfBeanInterest = (GameInfBean) item;
                helper.setText(R.id.type_tweet, gameInfBeanInterest.getTitleName())
                        .addOnClickListener(R.id.tweet);
                break;
            //小游戏，6
            case 25:
                GameInfBean gameInfBeanMiniGame = (GameInfBean) item;
                helper.setText(R.id.type_tweet, gameInfBeanMiniGame.getTitleName())
                        .addOnClickListener(R.id.tweet);

                NewGameAdapter miniGameAdapter = new NewGameAdapter(R.layout.item_new_game);
                MyHorizontalRecyclerView rvMiniGame = helper.getView(R.id.rv_daily_tweet);

                LinearLayoutManager miniGameLayoutManager = new LinearLayoutManager(mContext);
                miniGameLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                rvMiniGame.setLayoutManager(miniGameLayoutManager);

                miniGameAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                    int id = view.getId();
                    if (id == R.id.new_game_icon) {
                        Toast.makeText(mContext, position + "", Toast.LENGTH_SHORT).show();
                    }
                });
                rvMiniGame.setAdapter(miniGameAdapter);
                List<DailyTweetBean> miniGameBean = RecommendFragment.miniGameBeans;
                miniGameAdapter.setNewData(miniGameBean);
                miniGameAdapter.notifyDataSetChanged();
                break;
            //近期热点，7
            case 15:
                GameInfBean gameInfBeanHot = (GameInfBean) item;
                helper.setText(R.id.type_tweet, gameInfBeanHot.getTitleName())
                        .addOnClickListener(R.id.tweet);

                HotAdapter hotAdapter = new HotAdapter(R.layout.item_hot_pic);
                MyHorizontalRecyclerView rvHot = helper.getView(R.id.rv_daily_tweet);

                GridLayoutManager hotLayoutManager = new GridLayoutManager(mContext, 2);
                rvHot.setLayoutManager(hotLayoutManager);

                rvHot.setAdapter(hotAdapter);
                List<DailyTweetBean> hotBean = RecommendFragment.hotBeans;
                hotAdapter.setNewData(hotBean);
                hotAdapter.notifyDataSetChanged();
                break;
            //近期热游更新，8
            case 26:
                GameInfBean gameInfBeanNewHot = (GameInfBean) item;
                helper.setText(R.id.tv_player_recommend, gameInfBeanNewHot.getTitleName())
                        .addOnClickListener(R.id.cl_recommend);

                NewHotAdapter newHotAdapter = new NewHotAdapter(R.layout.item_new_hot_game_card);
                MyHorizontalRecyclerView rvNewHot = helper.getView(R.id.rv_play_word);

                LinearLayoutManager linearLayoutManagerNewHot = new LinearLayoutManager(mContext);
                linearLayoutManagerNewHot.setOrientation(RecyclerView.HORIZONTAL);
                rvNewHot.setLayoutManager(linearLayoutManagerNewHot);

                newHotAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                    int id = view.getId();
                    if (id == R.id.cl_new_hot_game) {
                        Toast.makeText(mContext, position + "热游更新", Toast.LENGTH_SHORT).show();
                    }
                });

                rvNewHot.setAdapter(newHotAdapter);
                List<PlayerRecommendBean> newHotBean = RecommendFragment.newHotBeans;
                newHotAdapter.setNewData(newHotBean);
                newHotAdapter.notifyDataSetChanged();
                break;
            //大图标广告，9
            case 18:
            case 6:
            case 4:
                GameInfBean bigTitleGame = (GameInfBean) item;
                bigTitleBeans = RecommendFragment.bigTitleGame;
                GameInfBean bigTitle = bigTitleBeans.get(flagList.get(9));
                flagList.set(9, flagList.get(9) + 1);

                ConstraintLayout constraintLayout = helper.getView(R.id.csl_ad_game);
                constraintLayout.setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, 0, constraintLayout.getWidth(), constraintLayout.getHeight(), 30);
                    }
                });
                constraintLayout.setClipToOutline(true);
                helper.setText(R.id.ad_name, bigTitleGame.getTitleName())
                        .setText(R.id.ad_detail, bigTitle.getGameInf())
                        .addOnClickListener(R.id.csl_ad_game);
                Glide.with(mContext).load(bigTitle.getGameImgUrl()).into((ImageView) helper.getView(R.id.ad_image));
                break;
            //学习合集，10
            case 2:
            case 21:
            case 13:
            case 22:
                GameInfBean gameInfBeanStudyGame = (GameInfBean) item;
                helper.setText(R.id.type_tweet, gameInfBeanStudyGame.getTitleName())
                        .addOnClickListener(R.id.tweet);

                NewGameAdapter studyGameAdapter = new NewGameAdapter(R.layout.item_new_game);
                MyHorizontalRecyclerView rvStudyGame = helper.getView(R.id.rv_daily_tweet);
                LinearLayoutManager linearLayoutManagerStudyGame = new LinearLayoutManager(mContext);
                linearLayoutManagerStudyGame.setOrientation(RecyclerView.HORIZONTAL);

                rvStudyGame.setLayoutManager(linearLayoutManagerStudyGame);
                studyGameAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                    int id = view.getId();
                    if (id == R.id.new_game_icon) {
                        Toast.makeText(mContext, position + "", Toast.LENGTH_SHORT).show();
                    }
                });
                rvStudyGame.setAdapter(studyGameAdapter);
                List<List<DailyTweetBean>> listStudyGameBean = RecommendFragment.listLists;
                List<DailyTweetBean> studyGameBean = listStudyGameBean.get(flagList.get(10));
                flagList.set(10, flagList.get(10) + 1);
                studyGameAdapter.setNewData(studyGameBean);
                studyGameAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

}
