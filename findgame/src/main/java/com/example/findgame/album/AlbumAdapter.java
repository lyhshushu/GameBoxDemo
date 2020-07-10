package com.example.findgame.album;

import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.findgame.R;
import com.example.findgame.bean.AlbumBean;

import java.util.List;

public class AlbumAdapter extends BaseQuickAdapter<AlbumBean, BaseViewHolder> {
    public AlbumAdapter(int layoutResId, @Nullable List<AlbumBean> data) {
        super(layoutResId, data);
    }

    public AlbumAdapter(@Nullable List<AlbumBean> data) {
        super(data);
    }

    public AlbumAdapter(int layoutResId) {
        super(layoutResId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void convert(BaseViewHolder helper, AlbumBean item) {

        ImageView imageView = helper.getView(R.id.iv_collection_pic);
        imageView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, imageView.getWidth(), imageView.getHeight(), 30);
            }
        });
        imageView.setClipToOutline(true);

        Glide.with(mContext).load(item.getPicUrl()).into(imageView);
        helper.setText(R.id.tv_collection_name, item.getName());
    }
}
