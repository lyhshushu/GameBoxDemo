package com.example.findgame.rank.fragments;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

import static com.example.androidlib.baseurl.Common.BASEURL;

public class RankNewGameFragment extends BaseFragment {
    private final static int OVER_10000 = 10000;

    @BindView(R2.id.rv_rank_new_game)
    RecyclerView rvRankNewGame;
    private List<AllRankBean> rankNewGameBeans;
    private Context mContext;
    private Handler handler;
    private RankNewGameAdapter rankNewGameAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_rank_new_game;
    }

    @Override
    protected void initView() {
        mContext = getContext();

        rankNewGameAdapter = new RankNewGameAdapter(R.layout.item_rank_new_game);
        rvRankNewGame.setLayoutManager(new LinearLayoutManager(mContext));
        rvRankNewGame.addItemDecoration(new RecyclerDivider(mContext));
        rvRankNewGame.setAdapter(rankNewGameAdapter);
    }

    @Override
    protected void applyData() {
        rankNewGameBeans = new LinkedList<>();
        getJson(BASEURL + "//app/android/v4.2.2/game-top-p-1-startKey--n-20.html");
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    rankNewGameAdapter.setNewData(rankNewGameBeans);
                    rankNewGameAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    private void getJson(String url) {
        MvcModelImp mvcModelImp = new MvcModelImp();
        mvcModelImp.getModel(url, new OKutil() {
            @Override
            public void onOk(String json) {
                getRankNewGame(json);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onNo(String message) {

            }
        });
    }

    private void getRankNewGame(String json) {
        try {
            JSONObject allJson = new JSONObject(json);
            String result = allJson.getString("result");
            JSONObject resultJson = new JSONObject(result);
            String data = resultJson.getString("data");
            JSONArray dataJson = new JSONArray(data);
            for (int i = 0; i < dataJson.length(); i++) {
                JSONObject newGameJson = dataJson.getJSONObject(i);
                AllRankBean newGameBean = new AllRankBean();
                newGameBean.setRank(i + 1);
                newGameBean.setGameName(newGameJson.getString("appname"));
                newGameBean.setGamePic(newGameJson.getString("icopath"));
                String newGameInf = null;
                int downNum = Integer.parseInt(newGameJson.getString("downnum"));
                if (downNum > OVER_10000) {
                    downNum = downNum / OVER_10000;
                    newGameInf = downNum + mContext.getString(R.string.over_10000) + mContext.getString(R.string.download);
                } else {
                    newGameInf = downNum + mContext.getString(R.string.times) + mContext.getString(R.string.download);
                }
                newGameBean.setGameInf(newGameInf);
                rankNewGameBeans.add(newGameBean);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
