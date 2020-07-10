package com.example.findgame.rank.fragments;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

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

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

import static com.example.androidlib.baseurl.Common.BASEURL;

public class RankAppointmentGameFragment extends BaseFragment {
    private final static int OVER_10000 = 10000;

    @BindView(R2.id.rv_rank_new_game)
    RecyclerView rvRankNewGame;

    private List<AllRankBean> rankAppointmentGameBeans;
    private Context mContext;
    private Handler handler;
    private RankAppointmentGameAdapter rankAppointmentGameAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_rank_new_game;
    }

    @Override
    protected void initView() {
        mContext = getContext();

        rankAppointmentGameAdapter = new RankAppointmentGameAdapter(R.layout.item_rank_new_game);
        rvRankNewGame.setLayoutManager(new LinearLayoutManager(mContext));
        rvRankNewGame.addItemDecoration(new RecyclerDivider(mContext));
        rvRankNewGame.setAdapter(rankAppointmentGameAdapter);
    }

    @Override
    protected void bindListener() {
        rankAppointmentGameAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            if (id == R.id.cl_rank_new_game) {
                Toast.makeText(mContext, rankAppointmentGameBeans.get(position).getGameName(), Toast.LENGTH_SHORT).show();
            }
            if (id == R.id.bt_rank_new_game_appointment) {
                Toast.makeText(mContext, rankAppointmentGameBeans.get(position).getGameName() + getString(R.string.download), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void applyData() {
        rankAppointmentGameBeans = new LinkedList<>();
        getJson(BASEURL + "//app/android/v4.2.2/game-top-startKey--type-subscribe-n-20.html");
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    rankAppointmentGameAdapter.setNewData(rankAppointmentGameBeans);
                    rankAppointmentGameAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    private void getJson(String url) {
        MvcModelImp mvcModelImp = new MvcModelImp();
        mvcModelImp.getModel(url, new OKutil() {
            @Override
            public void onOk(String json) {
                getRankAppointmentGame(json);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onNo(String message) {

            }
        });
    }

    private void getRankAppointmentGame(String json) {
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
                int subscribeNum = Integer.parseInt(newGameJson.getString("num_subscribe"));
                DecimalFormat decimalFormat = new DecimalFormat("####.#");
                if (subscribeNum > OVER_10000) {
                    float floatNum = (float) subscribeNum / OVER_10000;
                    newGameInf = decimalFormat.format(floatNum) + getString(R.string.over_10000_appointment);
                } else {
                    newGameInf = subscribeNum + getString(R.string.over_appointment);
                }
                newGameBean.setGameInf(newGameInf);
                newGameBean.setGameContent(newGameJson.getString("preheat_time"));
                rankAppointmentGameBeans.add(newGameBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

