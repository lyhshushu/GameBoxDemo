package com.example.findgame.album;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.androidlib.BaseFragment;
import com.example.findgame.R;
import com.example.findgame.R2;
import com.example.findgame.bean.AlbumBean;
import com.example.findgame.bean.AllRankBean;
import com.example.findgame.recommend.controller.MvcModelImp;
import com.example.findgame.recommend.controller.OKutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

import static com.example.androidlib.baseurl.Common.BASEURL;

public class AlbumFragment extends BaseFragment {


    @BindView(R2.id.rv_album)
    RecyclerView rvAlbum;

    private List<AlbumBean> albumBeans;
    private AlbumAdapter albumAdapter;
    private Context mContext;
    private Handler handler;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_album;
    }

    @Override
    protected void initView() {
        mContext = getContext();

        albumAdapter = new AlbumAdapter(R.layout.item_collection);
        rvAlbum.setLayoutManager(new GridLayoutManager(mContext, 2));
        rvAlbum.setAdapter(albumAdapter);

    }

    @Override
    protected void bindListener() {
        albumAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            if (id == R.id.cl_collection) {
                Toast.makeText(mContext, albumBeans.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void applyData() {
        albumBeans = new LinkedList<>();
        getJson(BASEURL + "/app/android/v3.1/album-list-startKey--n-20.html");
        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    albumAdapter.setNewData(albumBeans);
                    albumAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    private void getJson(String url) {
        MvcModelImp mvcModelImp = new MvcModelImp();
        mvcModelImp.getModel(url, new OKutil() {
            @Override
            public void onOk(String json) {
                getAlbumBean(json);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onNo(String message) {

            }
        });
    }

    private void getAlbumBean(String json) {
        try {
            JSONObject allJson = new JSONObject(json);
            String result = allJson.getString("result");
            JSONObject resultJson = new JSONObject(result);
            String data = resultJson.getString("data");
            JSONArray dataSJson = new JSONArray(data);

            for (int i = 0; i < dataSJson.length(); i++) {
                JSONObject dataJson = dataSJson.getJSONObject(i);
                AlbumBean albumBean = new AlbumBean();
                albumBean.setName(dataJson.getString("app_title_3_3"));
                albumBean.setPicUrl(dataJson.getString("appFace3_3"));
                albumBeans.add(albumBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
