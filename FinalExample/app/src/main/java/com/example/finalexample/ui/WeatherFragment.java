package com.example.finalexample.ui;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalexample.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import com.example.finalexample.ui.Weather.Weather1Fragment;
import com.example.finalexample.ui.Weather.Weather2Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#/newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //HeConfig.init("PublicId", "PrivateKey");
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tl =(TabLayout)view.findViewById(R.id.tl_weather);
        ViewPager vp =(ViewPager)view.findViewById(R.id.vp_weather);

        tl.setTabMode(TabLayout.MODE_FIXED);
        tl.addTab(tl.newTab().setText("今日"));
        tl.addTab(tl.newTab().setText("推荐"));

        vp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()));
        tl.setupWithViewPager(vp);

    }
    public class FragmentPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {
        private String[] titleList={"今日","推荐"};

        public FragmentPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment f;
            switch (position){

                case 0:
                    f=new Weather1Fragment();
                    return f;
                case 1:
                    f=new Weather2Fragment();
                    return f;
            }
            return null;
        }

        @Override
        public int getCount() {
            return titleList.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList[position];
        }
    }
}