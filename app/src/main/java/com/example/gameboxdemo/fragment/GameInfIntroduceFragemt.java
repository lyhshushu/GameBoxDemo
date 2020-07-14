package com.example.gameboxdemo.fragment;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidlib.BaseFragment;
import com.example.androidlib.view.MyHorizontalRecyclerView;
import com.example.gameboxdemo.GameInfActivity;
import com.example.gameboxdemo.R;
import com.example.gameboxdemo.bean.GameInfActBean;

import butterknife.BindView;

public class GameInfIntroduceFragemt extends BaseFragment {
    @BindView(R.id.rv_game_int_pic_list)
    MyHorizontalRecyclerView rvGameIntPicList;
    @BindView(R.id.tv_game_introduce)
    TextView tvGameIntroduce;
    @BindView(R.id.tv_game_inf_detail)
    TextView tvGameInfDetail;

    private GameInfActBean gameInfActBean;
    private Context mContext;
    private GameInfPicAdapter gameInfPicAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_game_inf_introduce;
    }

    @Override
    protected void initView() {
        mContext = getContext();
        gameInfPicAdapter = new GameInfPicAdapter(R.layout.item_game_inf_pic);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvGameIntPicList.setLayoutManager(layoutManager);
        rvGameIntPicList.setAdapter(gameInfPicAdapter);

    }

    @Override
    protected void applyData() {
        gameInfActBean = GameInfActivity.gameInfBean;
        gameInfPicAdapter.setNewData(gameInfActBean.getAppPicList());
        gameInfPicAdapter.notifyDataSetChanged();
        tvGameInfDetail.setText(gameInfActBean.getAppInfDetail());
        tvGameInfDetail.setMaxLines(3);
    }

    @Override
    protected void bindListener() {
        tvGameInfDetail.setOnClickListener(this);
    }

    @Override
    protected void widgetClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_game_inf_detail:
                if (tvGameInfDetail.getMaxLines() == 3) {
                    tvGameInfDetail.setMaxLines(100);
                } else {
                    tvGameInfDetail.setMaxLines(3);
                }
                break;
            default:
                break;
        }
    }
}
