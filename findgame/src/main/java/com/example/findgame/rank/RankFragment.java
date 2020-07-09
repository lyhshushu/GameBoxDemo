package com.example.findgame.rank;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.androidlib.BaseFragment;
import com.example.findgame.R;
import com.example.findgame.R2;
import com.example.findgame.information.InformationFragment;
import com.example.findgame.rank.fragments.RankNewGameFragment;
import com.example.findgame.recommend.controller.MvcModelImp;
import com.example.findgame.recommend.controller.OKutil;
import com.flyco.tablayout.SlidingTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

import static com.example.androidlib.baseurl.Common.BASEURL;

public class RankFragment extends BaseFragment {

    @BindView(R2.id.vp_rank_type)
    ViewPager vpRankType;
    @BindView(R2.id.xtl_rank_type)
    SlidingTabLayout xtlRankType;

    private Context mContext;
    private List<String> titles;
    private Handler handler;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_rank;
    }

    @Override
    protected void initView() {
        mContext = getContext();

//        titles = new ArrayList<>();
//        titles.add("推荐");
//        titles.add("分类");
//        titles.add("排行");
//        titles.add("专辑");
//        titles.add("资讯");


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new RankNewGameFragment());
        fragments.add(new InformationFragment());
        fragments.add(new RankNewGameFragment());
        fragments.add(new RankNewGameFragment());
        fragments.add(new RankNewGameFragment());

        RankFragmentAdapter rankFragmentAdapter = new RankFragmentAdapter(getChildFragmentManager(), fragments, titles);
        vpRankType.setOffscreenPageLimit(5);
        vpRankType.setAdapter(rankFragmentAdapter);


    }

    @Override
    protected void applyData() {
        titles = new LinkedList<>();
        getJson(BASEURL + "//app/android/v4.2.2/game-top-p-1-startKey--n-20.html");
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    xtlRankType.setViewPager(vpRankType, titles.toArray(new String[0]));
                }
            }
        };
    }

    private void getJson(String url) {
        MvcModelImp mvcModelImp = new MvcModelImp();
        mvcModelImp.getModel(url, new OKutil() {
            @Override
            public void onOk(String json) {
                getTitles(json);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onNo(String message) {

            }
        });
    }

    private void getTitles(String json) {
        try {
            JSONObject allJson = new JSONObject(json);
            String result = allJson.getString("result");
            JSONObject resultJson = new JSONObject(result);
            String types = resultJson.getString("types");
            JSONArray typesJson = new JSONArray(types);
            for (int i = 0; i < typesJson.length(); i++) {
                JSONObject typeJson = typesJson.getJSONObject(i);
                titles.add(typeJson.getString("title"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class RankFragmentAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragmentList;
        private List<String> titleList;

        public RankFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        public RankFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public RankFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            fragmentList = fragments;
            titleList = titles;
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

    @Override
    protected void bindListener() {

    }

}
