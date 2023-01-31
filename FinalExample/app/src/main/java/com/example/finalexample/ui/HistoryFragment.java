package com.example.finalexample.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalexample.R;
import com.example.finalexample.adapter.FutureInfoAdapter;
import com.example.finalexample.adapter.HistoryInfo;
import com.example.finalexample.adapter.HistoryInfoAdapter;
import com.example.finalexample.adapter.Icon;
import com.example.finalexample.adapter.WeatherInfoAdapter;
import com.example.finalexample.db.DBHistory;
import com.example.finalexample.db.History;
import com.example.finalexample.ui.Weather.Weather1Fragment;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.view.QWeather;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#/newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private HistoryInfoAdapter mAdapter;
    private List<HistoryInfo> mHistoryList;
    private String[] city;
    private String[] date;
    private String[] info;
    DBHistory mDBHistory;
    private boolean isCreated=false;
    public static int Count=4;
    SharedPreferences sp_count;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreated=true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.refresh_his);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.black,null));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        initData();
                        mRecyclerView = view.findViewById(R.id.recycler_his);
                        mLayoutManager = new LinearLayoutManager(getActivity());
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mAdapter =new HistoryInfoAdapter(mHistoryList);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                },1000);
            }
        });
        Log.i("History","onViewCreated");
        initData();
        mRecyclerView = view.findViewById(R.id.recycler_his);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter =new HistoryInfoAdapter(mHistoryList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(40));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

    }
    private void initData() {
        sp_count=getActivity().getSharedPreferences("historyCount", Context.MODE_PRIVATE);
        int count=sp_count.getInt("count",Count);
        mDBHistory=DBHistory.getInstance(getContext());
        List<History> list=mDBHistory.getDaoHistory().getAllHistory();
        mHistoryList = new ArrayList<>();
        int Size=Math.min(list.size(), count);
        for(int i=0;i<Size;i++){
            if(list.size()!=0) {
                HistoryInfo hi = new HistoryInfo();
                hi.setCity(list.get(i).getCityName());
                hi.setDate(list.get(i).getDate());
                hi.setInfo(list.get(i).getTextDay() + "/" + list.get(i).getTextNight() + " " + list.get(i).getTempMin() + "℃" + "/" + list.get(i).getTempMax() + "℃");
                mHistoryList.add(hi);
            }
        }
    }
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.top = space;

            // Add top margin only for the first item to avoid double space between items
            //if (parent.getChildPosition(view) == 0)
                //outRect.top = space;
        }
    }
}