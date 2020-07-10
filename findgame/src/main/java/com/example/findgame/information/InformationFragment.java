package com.example.findgame.information;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.androidlib.BaseFragment;
import com.example.findgame.R;
import com.example.findgame.R2;
import com.example.findgame.album.AlbumAdapter;
import com.example.findgame.bean.AlbumBean;
import com.example.findgame.bean.LikeGameBean;
import com.example.findgame.information.bottomdialog.BottomDialogAdapter;
import com.example.findgame.recommend.NewGameAdapter;
import com.example.findgame.recommend.controller.MvcModelImp;
import com.example.findgame.recommend.controller.OKutil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
    //dialog
    private RecyclerView rvDialog;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior mDialogBehavior;


    private List<String> tagReason;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_information;
    }

    @Override
    protected void initView() {
        mContext = getContext();

        likeAdapter = new LikeGameAdapter(R.layout.item_game_inf);
        rvLike.setLayoutManager(new LinearLayoutManager(mContext));
        rvLike.setAdapter(likeAdapter);

    }

    @Override
    protected void bindListener() {
        likeAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            if (id == R.id.csl_game_inf) {
                Toast.makeText(mContext, likeGameBeans.get(position).getName(), Toast.LENGTH_SHORT).show();
                showSheetDialog();
                bottomSheetDialog.show();
            }
            if (id == R.id.bt_download) {
                Toast.makeText(mContext, likeGameBeans.get(position).getName() + getString(R.string.download), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void applyData() {
        likeGameBeans = new LinkedList<>();
        tagReason = new LinkedList<>();
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
                getReason(json);
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
                    newGameInf = downNum + getString(R.string.over_10000) + getString(R.string.download) + "  " + gameSize;
                } else {
                    newGameInf = downNum + getString(R.string.times) + getString(R.string.download) + "  " + gameSize;
                }
                likeGameBean.setGameInf(newGameInf);
                likeGameBean.setGameContext(dataJson.getString("review"));
                likeGameBeans.add(likeGameBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getReason(String json) {
        try {
            JSONObject allJson = new JSONObject(json);
            String result = allJson.getString("result");
            JSONObject resultJson = new JSONObject(result);
            String config = resultJson.getString("config");
            JSONObject configJson = new JSONObject(config);
            String reason = configJson.getString("reason");
            JSONArray reasonSJson = new JSONArray(reason);
            for (int i = 0; i < reasonSJson.length(); i++) {
                JSONObject reasonJson = reasonSJson.getJSONObject(i);
                tagReason.add(reasonJson.getString("name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void showSheetDialog() {
        View view = View.inflate(mContext, R.layout.dialog_bottom_like, null);
        rvDialog = view.findViewById(R.id.rv_reasons);
        rvDialog.setLayoutManager(new GridLayoutManager(mContext, 2));
        BottomDialogAdapter bottomDialogAdapter = new BottomDialogAdapter(R.layout.item_bottom_dialog_tag);
        rvDialog.setAdapter(bottomDialogAdapter);
        bottomSheetDialog = new BottomSheetDialog(mContext,R.style.BottomDialog);
        bottomSheetDialog.setContentView(view);

        bottomDialogAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            int id=view1.getId();
            if(id == R.id.cl_dialog_tag){
                Toast.makeText(mContext, tagReason.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        bottomDialogAdapter.setNewData(tagReason);
        bottomDialogAdapter.notifyDataSetChanged();

        mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
//        mDialogBehavior.setPeekHeight(getPeekHeight());

    }

//    protected int getPeekHeight() {
//        int peekHeight = getResources().getDisplayMetrics().heightPixels;
//        //设置弹窗高度为屏幕高度的3/4
//        return peekHeight - peekHeight / 2;
//    }

}
