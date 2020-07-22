package com.example.findgame;


import android.graphics.Typeface;
import android.text.style.AlignmentSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.androidlib.BaseFragment;
import com.example.findgame.album.AlbumFragment;
import com.example.findgame.classification.ClassificationFragment;
import com.example.findgame.information.InformationFragment;
import com.example.findgame.rank.RankFragment;
import com.example.findgame.recommend.RecommendFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * @author 4399lyh
 */
public class FindGameFragment extends BaseFragment {

    @BindView(R2.id.find_game_tab)
    SlidingTabLayout xTabLayout;
    @BindView(R2.id.find_game_viewpager)
    ViewPager findGameViewPager;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_find_game;
    }

    @Override
    protected void initView() {
        List<String> titles = new ArrayList<>();
        titles.add("推荐");
        titles.add("分类");
        titles.add("排行");
        titles.add("专辑");
        titles.add("喜欢");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new RecommendFragment());
        fragments.add(new ClassificationFragment());
        fragments.add(new RankFragment());
        fragments.add(new AlbumFragment());
        fragments.add(new InformationFragment());

        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(), fragments, titles);
        findGameViewPager.setOffscreenPageLimit(5);
        findGameViewPager.setAdapter(adapter);
        xTabLayout.setViewPager(findGameViewPager, titles.toArray(new String[0]));
        findGameViewPager.setCurrentItem(0);

        xTabLayout.getTitleView(0).setTypeface(Typeface.DEFAULT_BOLD);
        xTabLayout.getTitleView(0).setTextSize(15f);

        findGameViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < xTabLayout.getTabCount(); i++) {
                    if (position == i) {
                        xTabLayout.getTitleView(position).setTextSize(15f);
                    } else {
                        xTabLayout.getTitleView(i).setTextSize(12f);
                        xTabLayout.getTitleView(0).setTypeface(Typeface.DEFAULT);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public static class FragmentAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragments;
        private List<String> mTitles;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            mFragments = fragments;
            mTitles = titles;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }
}
