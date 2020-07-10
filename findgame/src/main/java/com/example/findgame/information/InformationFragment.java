package com.example.findgame.information;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidlib.BaseFragment;
import com.example.findgame.R;
import com.example.findgame.R2;
import com.example.findgame.album.AlbumAdapter;
import com.example.findgame.bean.AlbumBean;
import com.example.findgame.bean.LikeGameBean;
import com.example.findgame.recommend.NewGameAdapter;
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

public class InformationFragment extends BaseFragment {

    private final static int OVER_10000 = 10000;

    @BindView(R2.id.iv_like)
    ImageView ivLike;
    @BindView(R2.id.rv_like)
    RecyclerView rvLike;

    private List<LikeGameBean> likeGameBeans;
    private LikeGameAdapter likeAdapter;
    private Context mContext;
    private Handler handler;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_information;
    }

    @Override
    protected void initView() {
        likeAdapter = new LikeGameAdapter(R.layout.item_game_inf);
        rvLike.setLayoutManager(new LinearLayoutManager(mContext));
        rvLike.setAdapter(likeAdapter);
    }

    @Override
    protected void applyData() {
        likeGameBeans = new LinkedList<>();
        getJson(BASEURL + "/android/box/player/v4.2.1/gameUser-guessLike-startKey--n-20.html");
        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    likeAdapter.setNewData(likeGameBeans);
                    likeAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    private void getJson(String url) {
        MvcModelImp mvcModelImp = new MvcModelImp();
        mvcModelImp.getModel(url, new OKutil() {
            @Override
            public void onOk(String json) {
                getLikeBean(json);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onNo(String message) {

            }
        });
    }

    private void getLikeBean(String json) {
        try {
            JSONObject allJson = new JSONObject(json);
            String result = allJson.getString("result");
            JSONObject resultJson = new JSONObject(result);
            String data = resultJson.getString("data");
            JSONArray dataSJson = new JSONArray(data);

            for (int i = 0; i < dataSJson.length(); i++) {
                JSONObject dataJson = dataSJson.getJSONObject(i);
                LikeGameBean likeGameBean = new LikeGameBean();
                likeGameBean.setName(dataJson.getString("appname"));
                likeGameBean.setGamePic(dataJson.getString("icopath"));
                String newGameInf = null;
                int downNum = Integer.parseInt(dataJson.getString("downnum"));
                float size = Float.parseFloat(dataJson.getString("size"));
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
                    newGameInf = String.valueOf(downNum) + getString(R.string.over_10000) + getString(R.string.download) + "  " + gameSize;
                } else {
                    newGameInf = String.valueOf(downNum) + getString(R.string.times) + getString(R.string.download) + "  " + gameSize;
                }
                likeGameBean.setGameInf(newGameInf);
                likeGameBean.setGameContext(dataJson.getString("review"));
                likeGameBeans.add(likeGameBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
