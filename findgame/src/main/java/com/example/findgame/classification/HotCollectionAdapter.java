package com.example.findgame.classification;

import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.os.Build;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.androidlib.utils.OutLineSetter;
import com.example.androidlib.view.ChildUnClickableConstraintLayout;
import com.example.androidlib.view.MyHorizontalRecyclerView;
import com.example.findgame.R;
import com.example.findgame.bean.AllClassificationBean;

import java.util.List;

public class HotCollectionAdapter extends BaseQuickAdapter<AllClassificationBean, BaseViewHolder> {
    public HotCollectionAdapter(int layoutResId, @Nullable List<AllClassificationBean> data) {
        super(layoutResId, data);
    }

    public HotCollectionAdapter(@Nullable List<AllClassificationBean> data) {
        super(data);
    }

    public HotCollectionAdapter(int layoutResId) {
        super(layoutResId);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void convert(BaseViewHolder helper, AllClassificationBean item) {
        Glide.with(mContext).load(item.getPicUrl()).into((ImageView) helper.getView(R.id.iv_collection));
        helper.setText(R.id.tv_collection_num, item.getCollectionNum() + mContext.getString(R.string.kind))
                .addOnClickListener(R.id.cl_hot_collection_card);

        ChildUnClickableConstraintLayout constraintLayout = helper.getView(R.id.cl_hot_collection_card);
        OutLineSetter.setOutLine(constraintLayout,30);

        MyHorizontalRecyclerView rvLogo = helper.getView(R.id.rv_list_game);
        MiniLogAdapter miniLogAdapter = new MiniLogAdapter(R.layout.item_logo_pic);
        LinearLayoutManager linearLayoutManagerMiniLogo = new LinearLayoutManager(mContext);
        linearLayoutManagerMiniLogo.setOrientation(RecyclerView.HORIZONTAL);
        rvLogo.setLayoutManager(linearLayoutManagerMiniLogo);
        rvLogo.setAdapter(miniLogAdapter);

        miniLogAdapter.setNewData(item.getCollectionPicUrl());
        miniLogAdapter.notifyDataSetChanged();

    }

}

