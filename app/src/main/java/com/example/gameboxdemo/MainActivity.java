package com.example.gameboxdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.androidlib.BaseActivity;
import com.example.androidlib.view.IndexViewPager;
import com.example.findgame.FindGameFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author lyh
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    IndexViewPager mViewPager;
    @BindView(R.id.nav_view)
    BottomNavigationView bottomNavigationView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
//添加fragment
        adapter.addFragment(new FindGameFragment());
        adapter.addFragment(new Fragment());
        mViewPager.setAdapter(adapter);
    }

    /**
     * 跳转tab
     *
     * @param index int
     */
    public void toTab(int index) {
        if (index >= 0 && index < mViewPager.getChildCount()) {
            mViewPager.setCurrentItem(index);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Integer i) {
        toTab(i);
    }

    @Override
    protected void bindListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.find_game:
                    mViewPager.setCurrentItem(0, false);
                    return true;
                case R.id.dynamic:
                    mViewPager.setCurrentItem(1, false);
                    return true;
                default:
            }
            return false;
        }
        );
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0 || position == 1) {
                    bottomNavigationView.getMenu().getItem(position).setChecked(true);
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    public class MainPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();

        MainPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }
    }
}
