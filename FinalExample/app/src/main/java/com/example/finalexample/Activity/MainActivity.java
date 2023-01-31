package com.example.finalexample.Activity;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.finalexample.R;
import com.example.finalexample.db.DBHistory;
import com.example.finalexample.ui.HistoryFragment;
import com.example.finalexample.ui.MapFragment;
import com.example.finalexample.ui.SettingFragment;
import com.example.finalexample.ui.WeatherFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.qweather.sdk.view.HeConfig;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    DBHistory mDBHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HeConfig.init("HE2201041818241614", "df8228d8794e436f82e2294f2010096a");
        HeConfig.switchToDevService();
        ViewPager vp = (ViewPager) findViewById(R.id.vp_main);
        BottomNavigationView bv = (BottomNavigationView) findViewById(R.id.bottom_nav_view);

        Toolbar tbMain = (Toolbar) findViewById(R.id.toolbar);
        tbMain.setTitle("");
        setSupportActionBar(tbMain);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MapFragment());
        fragments.add(new WeatherFragment());
        fragments.add(new HistoryFragment());
        fragments.add(new SettingFragment());
        FragmentAdapter adapter = new FragmentAdapter(fragments, getSupportFragmentManager());
        vp.setAdapter(adapter);
        bv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuId = item.getItemId();
                // 跳转指定页面：Fragment
                switch (menuId) {
                    case R.id.tab_map:
                        vp.setCurrentItem(0);
                        return true;
                    case R.id.tab_weather:
                        vp.setCurrentItem(1);
                        return true;
                    case R.id.tab_history:
                        vp.setCurrentItem(2);
                        return true;
                    case R.id.tab_setting:
                        vp.setCurrentItem(3);
                        return true;
                }
                return false;
            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bv.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public final class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;

        public FragmentAdapter(List<Fragment> fragments, FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.mFragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}