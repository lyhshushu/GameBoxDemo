package com.example.findgame.classification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidlib.BaseFragment;
import com.example.androidlib.view.MyHorizontalRecyclerView;
import com.example.findgame.R;
import com.example.findgame.R2;
import com.example.findgame.bean.AllClassificationBean;
import com.example.findgame.recommend.controller.MvcModelImp;
import com.example.findgame.recommend.controller.OKutil;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.LineNumberReader;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

import static com.example.androidlib.baseurl.Common.BASEURL;

public class ClassificationFragment extends BaseFragment {

    @BindView(R2.id.rv_classification_logo)
    RecyclerView rvClassificationLogo;
    @BindView(R2.id.hot_collection_title)
    TextView HotCollectionTitle;
    @BindView(R2.id.hot_collections)
    MyHorizontalRecyclerView rvHotCollections;
    @BindView(R2.id.cl_hot_collection)
    ConstraintLayout clHotCollection;
    @BindView(R2.id.rv_type_list)
    RecyclerView rvTypeList;

    private List<AllClassificationBean> gameLogoBeans;
    private List<AllClassificationBean> hotCollectionBeans;
    private List<AllClassificationBean> tagsBeans;

    private GameLogoAdapter gameLogoAdapter;
    private HotCollectionAdapter hotCollectionAdapter;
    private TagsTypeAdapter tagsTypeAdapter;


    private Context mContext;
    private Handler handler;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_classification;
    }

    @Override
    protected void initView() {
        mContext = getContext();

        gameLogoAdapter = new GameLogoAdapter(R.layout.item_classification_game_logo);
        rvClassificationLogo.setLayoutManager(new GridLayoutManager(mContext, 4));
        rvClassificationLogo.setAdapter(gameLogoAdapter);

        hotCollectionAdapter = new HotCollectionAdapter(R.layout.item_hot_collection);
        LinearLayoutManager layoutManagerHotCollection = new LinearLayoutManager(mContext);
        layoutManagerHotCollection.setOrientation(RecyclerView.HORIZONTAL);
        rvHotCollections.setLayoutManager(layoutManagerHotCollection);
        rvHotCollections.setAdapter(hotCollectionAdapter);

        tagsTypeAdapter = new TagsTypeAdapter(R.layout.item_classification_tags);
        rvTypeList.setLayoutManager(new LinearLayoutManager(mContext));
        rvTypeList.setAdapter(tagsTypeAdapter);

    }

    @Override
    protected void bindListener() {
        clHotCollection.setOnClickListener(this);

        gameLogoAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            AllClassificationBean gameLogoBean = gameLogoBeans.get(position);
            if (id == R.id.cl_logo) {
                Toast.makeText(mContext, gameLogoBean.getName() + "logo", Toast.LENGTH_SHORT).show();
            }
        });

        hotCollectionAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int id = view.getId();
            if (id == R.id.cl_hot_collection_card) {
                Toast.makeText(mContext, position + "专辑card", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void widgetClick(View v) {
        int id = v.getId();
        if (id == R.id.cl_hot_collection) {
            Toast.makeText(mContext, "热门合辑", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void applyData() {

        gameLogoBeans = new LinkedList<>();
        hotCollectionBeans = new LinkedList<>();
        tagsBeans = new LinkedList<>();
        getJSON(BASEURL + "/android/box/game/v3.8/custom-category-startKey--n-20.html");
        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    gameLogoAdapter.setNewData(gameLogoBeans);
                    gameLogoAdapter.notifyDataSetChanged();

                    hotCollectionAdapter.setNewData(hotCollectionBeans);
                    hotCollectionAdapter.notifyDataSetChanged();

                    tagsTypeAdapter.setNewData(tagsBeans);
                    tagsTypeAdapter.notifyDataSetChanged();
                }
            }
        };

    }

    private void getJSON(String url) {
        MvcModelImp mvcModelImp = new MvcModelImp();
        mvcModelImp.getModel(url, new OKutil() {
            @Override
            public void onOk(String json) {
                getLogo(json);
                getHotCollection(json);
                getTags(json);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onNo(String message) {

            }
        });
    }

    private void getTags(String json) {
        try {
            JSONObject allJson = new JSONObject(json);
            String result = allJson.getString("result");
            JSONObject resultJson = new JSONObject(result);
            String data = resultJson.getString("data");
            JSONArray dataJson = new JSONArray(data);
            for (int i = 0; i < dataJson.length(); i++) {
                JSONObject tagsJson = dataJson.getJSONObject(i);
                AllClassificationBean tagsBean = new AllClassificationBean();
                tagsBean.setName(tagsJson.getString("name"));

                String tagsName = tagsJson.getString("tags");
                JSONArray tagsNameJson = new JSONArray(tagsName);
                List<String> listTagName = new LinkedList<>();

                for (int j = 0; j < tagsNameJson.length(); j++) {
                    JSONObject nameJson = tagsNameJson.getJSONObject(j);
                    listTagName.add(nameJson.getString("name"));
                }
                tagsBean.setCollectionPicUrl(listTagName);
                tagsBeans.add(tagsBean);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getHotCollection(String json) {
        try {
            JSONObject allJson = new JSONObject(json);
            String result = allJson.getString("result");
            JSONObject resultJson = new JSONObject(result);
            String albumList = resultJson.getString("album_list");
            JSONArray albumListJson = new JSONArray(albumList);
            for (int i = 0; i < albumListJson.length(); i++) {
                JSONObject albumJson = albumListJson.getJSONObject(i);
                AllClassificationBean collectionBean = new AllClassificationBean();
                collectionBean.setPicUrl(albumJson.getString("face"));

                String gameList = albumJson.getString("game_list");
                JSONObject gameListJson = new JSONObject(gameList);
                collectionBean.setCollectionNum(gameListJson.getString("count"));

                String data = gameListJson.getString("data");
                JSONArray dataJson = new JSONArray(data);
                List<String> collectionPics = new LinkedList<>();
                for (int j = 0; j < dataJson.length(); j++) {
                    JSONObject collectionGame = dataJson.getJSONObject(j);
                    collectionPics.add(collectionGame.getString("icopath"));
                }
                collectionBean.setCollectionPicUrl(collectionPics);
                hotCollectionBeans.add(collectionBean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getLogo(String json) {
        try {
            JSONObject allJSON = new JSONObject(json);
            String result = allJSON.getString("result");
            JSONObject resultJSON = new JSONObject(result);
            String links = resultJSON.getString("links");
            JSONArray linksJSON = new JSONArray(links);

            for (int i = 0; i < linksJSON.length(); i++) {
                JSONObject logoInf = linksJSON.getJSONObject(i);
                AllClassificationBean gameLogoBean = new AllClassificationBean();
                gameLogoBean.setName(logoInf.getString("name"));
                gameLogoBean.setPicUrl(logoInf.getString("icon"));
                gameLogoBeans.add(gameLogoBean);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
