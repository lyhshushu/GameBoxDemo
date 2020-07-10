package com.example.findgame.rank.fragments;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.androidlib.BaseFragment;
import com.example.findgame.R;
import com.example.findgame.R2;
import com.example.findgame.bean.AllRankBean;
import com.example.findgame.recommend.RecyclerDivider;
import com.example.findgame.recommend.controller.MvcModelImp;
import com.example.findgame.recommend.controller.OKutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

import static com.example.androidlib.baseurl.Common.BASEURL;

public class RankSingleGameFragment extends BaseFragment {
    private final static int OVER_10000 = 10000;

    @BindView(R2.id.rv_rank_new_game)
    RecyclerView rvRankNewGame;
    private List<AllRankBean> rankSingleGameBeans;
    private Context mContext;
    private Handler handler;
    private RankNewGameAdapter rankSingleGameAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_rank_new_game;
    }

    @Override
    protected void initView() {
        mContext = getContext();

        rankSingleGameAdapter = new RankNewGameAdapter(R.layout.item_rank_new_game);
        rvRankNewGame.setLayoutManager(new LinearLayoutManager(mContext));
        rvRankNewGame.addItemDecoration(new RecyclerDivider(mContext));
        rvRankNewGame.setAdapter(rankSingleGameAdapter);
    }

    @Override
    protected void bindListener() {
        rankSingleGameAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            if (id == R.id.cl_rank_new_game) {
                Toast.makeText(mContext, rankSingleGameBeans.get(position).getGameName(), Toast.LENGTH_SHORT).show();
            }
            if (id == R.id.bt_rank_new_game_download) {
                Toast.makeText(mContext, rankSingleGameBeans.get(position).getGameName() + getString(R.string.download), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void applyData() {
        rankSingleGameBeans = new LinkedList<>();
        getJson(BASEURL + "//app/android/v4.2.2/game-top-startKey--type-single-n-20.html");
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    rankSingleGameAdapter.setNewData(rankSingleGameBeans);
                    rankSingleGameAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    private void getJson(String url) {
        MvcModelImp mvcModelImp = new MvcModelImp();
        mvcModelImp.getModel(url, new OKutil() {
            @Override
            public void onOk(String json) {
                getRankSingleGame(json);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onNo(String message) {

            }
        });
    }

    private void getRankSingleGame(String json) {
        try {
            JSONObject allJson = new JSONObject(json);
            String result = allJson.getString("result");
            JSONObject resultJson = new JSONObject(result);
            String data = resultJson.getString("data");
            JSONArray dataJson = new JSONArray(data);
            for (int i = 0; i < dataJson.length(); i++) {
                JSONObject newGameJson = dataJson.getJSONObject(i);
                AllRankBean newGameBean = new AllRankBean();
                newGameBean.setRank(String.valueOf((i + 1)));
                newGameBean.setGameName(newGameJson.getString("appname"));
                newGameBean.setGamePic(newGameJson.getString("icopath"));
                String newGameInf = null;
                int downNum = Integer.parseInt(newGameJson.getString("num_download"));
                float size = Float.parseFloat(newGameJson.getString("size"));
                String gameSize = null;
                int intSize;
                DecimalFormat decimalFormat = new DecimalFormat("####.##");
                if (size > 1024) {
                    float floatSize = Float.parseFloat(decimalFormat.format(size / 1024));
                    if (floatSize == (int) floatSize) {
                        intSize = (int) floatSize;
                        gameSize = intSize + "G";
                    } else {
                        gameSize = floatSize + "G";
                    }
                } else {
                    if (size == (int) size) {
                        intSize = (int) size;
                        gameSize = intSize + "M";
                    } else {
                        gameSize = size + "M";
                    }
                }

                if (downNum > OVER_10000) {
                    downNum = downNum / OVER_10000;
                    newGameInf = downNum + mContext.getString(R.string.over_10000) + mContext.getString(R.string.download) + "  " + gameSize;
                } else {
                    newGameInf = downNum + mContext.getString(R.string.times) + mContext.getString(R.string.download) + "  " + gameSize;
                }
                newGameBean.setGameInf(newGameInf);

                String eventRecord = newGameJson.getString("event_record");
                JSONObject gameContent = new JSONObject(eventRecord);
                if (gameContent.isNull("content")) {
                    newGameBean.setGameContent(null);
                } else {
                    newGameBean.setGameContent(gameContent.getString("content"));
                }
                rankSingleGameBeans.add(newGameBean);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
