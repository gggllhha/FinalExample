package com.example.finalexample.ui.Weather;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalexample.R;
import com.example.finalexample.adapter.FutureIcon;
import com.example.finalexample.adapter.FutureInfoAdapter;
import com.example.finalexample.adapter.Icon;
import com.example.finalexample.adapter.WeatherInfoAdapter;
import com.google.gson.Gson;
import com.qweather.sdk.bean.Basic;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Weather1Fragment#/newInstance} factory method to
 * create an instance of this fragment.
 */
public class Weather1Fragment extends Fragment {
    private RecyclerView mRecyclerView,fRecyclerView;
    private LinearLayoutManager mLayoutManager,fLayoutManager;
    private WeatherInfoAdapter mAdapter;
    private FutureInfoAdapter fAdapter;
    private List<Icon> mPhotoList;
    private List<FutureIcon> fList;
    private int[] indexs = new int[]{R.drawable.ic_1, R.drawable.ic_2,
            R.drawable.ic_3,R.drawable.ic_4, R.drawable.ic_5, R.drawable.ic_6, R.drawable.ic_7,R.drawable.ic_8};
    private String[] title=new String[]{"????????????","????????????","??????","??????","????????????","????????????","?????????","??????"};
    private String[] info;
    private String[] date;
    private int[] icon=new int[]{R.drawable.ic_date,R.drawable.ic_date,R.drawable.ic_date};
    private String[] future_info;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.black,null));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("cityInfo", Context.MODE_MULTI_PROCESS);
                        String locationID=sharedPreferences.getString("ID","");
                        TextView city_weather_info=(TextView)view.findViewById(R.id.city_weather_info);
                        TextView updatetime=(TextView)view.findViewById(R.id.updateTime);
                        ImageView imgCity=(ImageView)view.findViewById(R.id.cityImg);
                        QWeather.getWeatherNow(getContext(), locationID, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
                            @Override
                            public void onError(Throwable e) {
                                Log.i(TAG, "getWeather onError: " + e);
                            }

                            @Override
                            public void onSuccess(WeatherNowBean weatherBean) {
                                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherBean));
                                //??????????????????status??????????????????status???????????????????????????status?????????????????????status?????????Code???????????????
                                if (Code.OK == weatherBean.getCode()) {
                                    WeatherNowBean.NowBaseBean now = weatherBean.getNow();
                                    Basic basic=weatherBean.getBasic();
                                    String updateTime=basic.getUpdateTime();
                                    String temp=now.getTemp()+"???";
                                    String feelslike=now.getFeelsLike()+"???";
                                    String weather=now.getText();
                                    String windDir=now.getWindDir();
                                    String wind=now.getWindSpeed()+"km/h";
                                    String humidity=now.getHumidity()+"%";
                                    String pressure=now.getPressure()+"hpa";
                                    String vis=now.getVis()+"km";
                                    String cloud=now.getCloud()+"%";
                                    info=new String[]{feelslike,weather,windDir,wind,humidity,pressure,vis,cloud};
                                    initData();
                                    mRecyclerView = view.findViewById(R.id.recycler_wt);
                                    mLayoutManager = new LinearLayoutManager(getActivity());
                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                    mAdapter =new WeatherInfoAdapter(mPhotoList);
                                    mRecyclerView.setAdapter(mAdapter);
                                    updatetime.setText("???????????????"+updateTime);
                                    String city=sharedPreferences.getString("city","");
                                    city_weather_info.setText(city+" "+temp);
                                    if(city.equals("??????")||city.equals("?????????")||city.equals("beijing")){
                                        imgCity.setBackgroundResource(R.drawable.ic_beijing);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")||city.equals("shanghai")){
                                        imgCity.setBackgroundResource(R.drawable.ic_shanghai);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")||city.equals("guangzhou")){
                                        imgCity.setBackgroundResource(R.drawable.ic_guangzhou);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")){
                                        imgCity.setBackgroundResource(R.drawable.ic_shenzheng);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")){
                                        imgCity.setBackgroundResource(R.drawable.ic_nanjing);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")){
                                        imgCity.setBackgroundResource(R.drawable.ic_chongqing);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")){
                                        imgCity.setBackgroundResource(R.drawable.ic_chengdu);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")||city.equals("hangzhou")){
                                        imgCity.setBackgroundResource(R.drawable.ic_hangzhou);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")){
                                        imgCity.setBackgroundResource(R.drawable.ic_shenyang);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")){
                                        imgCity.setBackgroundResource(R.drawable.ic_suzhou);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")){
                                        imgCity.setBackgroundResource(R.drawable.ic_tianjin);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")){
                                        imgCity.setBackgroundResource(R.drawable.ic_wuhan);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")){
                                        imgCity.setBackgroundResource(R.drawable.ic_zhengzhou);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")){
                                        imgCity.setBackgroundResource(R.drawable.ic_xian);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")){
                                        imgCity.setBackgroundResource(R.drawable.ic_xianggang);
                                    }
                                    else if(city.equals("??????")||city.equals("?????????")){
                                        imgCity.setBackgroundResource(R.drawable.ic_shengzhou);
                                    }
                                    else {
                                        imgCity.setBackgroundResource(R.drawable.ic_other);
                                    }
                                } else {
                                    //???????????????????????????????????????
                                    Code code = weatherBean.getCode();
                                    Log.i(TAG, "failed code: " + code);
                                }
                            }
                        });
                        QWeather.getWeather3D(getContext(), locationID, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherDailyListener() {
                            @Override
                            public void onError(Throwable throwable) {
                                Log.i(TAG, "getWeather onError: " + throwable);
                            }

                            @Override
                            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherDailyBean));
                                if(Code.OK == weatherDailyBean.getCode()) {
                                    List<WeatherDailyBean.DailyBean> daily=weatherDailyBean.getDaily();
                                    String date1= daily.get(0).getFxDate();
                                    String date2=daily.get(1).getFxDate();
                                    String date3=daily.get(2).getFxDate();
                                    date=new String[]{date1,date2,date3};
                                    String info1=daily.get(0).getTextDay()+"/"+daily.get(0).getTextNight()+"  "+daily.get(0).getTempMin()+"???"+"/"+daily.get(0).getTempMax()+"???";
                                    String info2=daily.get(1).getTextDay()+"/"+daily.get(1).getTextNight()+"  "+daily.get(1).getTempMin()+"???"+"/"+daily.get(1).getTempMax()+"???";
                                    String info3=daily.get(2).getTextDay()+"/"+daily.get(2).getTextNight()+"  "+daily.get(2).getTempMin()+"???"+"/"+daily.get(2).getTempMax()+"???";
                                    future_info=new String[]{info1,info2,info3};
                                    initData_future();
                                    fRecyclerView = view.findViewById(R.id.recycler_ft);
                                    fLayoutManager = new LinearLayoutManager(getActivity());
                                    fRecyclerView.setLayoutManager(fLayoutManager);
                                    fAdapter =new FutureInfoAdapter(fList);
                                    fRecyclerView.setAdapter(fAdapter);
                                } else {
                                    //???????????????????????????????????????
                                    Code code = weatherDailyBean.getCode();
                                    Log.i(TAG, "failed code: " + code);
                                }
                            }
                        });
                    }
                },1000);
            }
        });
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("cityInfo", Context.MODE_MULTI_PROCESS);
        //String city=sharedPreferences.getString("city","");
        String locationID=sharedPreferences.getString("ID","");
        TextView city_weather_info=(TextView)view.findViewById(R.id.city_weather_info);
        TextView updatetime=(TextView)view.findViewById(R.id.updateTime);
        ImageView imgCity=(ImageView)view.findViewById(R.id.cityImg);
        QWeather.getWeatherNow(getContext(), locationID, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "getWeather onError: " + e);
            }

            @Override
            public void onSuccess(WeatherNowBean weatherBean) {
                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherBean));
                //??????????????????status??????????????????status???????????????????????????status?????????????????????status?????????Code???????????????
                if (Code.OK == weatherBean.getCode()) {
                    WeatherNowBean.NowBaseBean now = weatherBean.getNow();
                    Basic basic=weatherBean.getBasic();
                    String updateTime=basic.getUpdateTime();
                    String temp=now.getTemp()+"???";
                    String feelslike=now.getFeelsLike()+"???";
                    String weather=now.getText();
                    String windDir=now.getWindDir();
                    String wind=now.getWindSpeed()+"km/h";
                    String humidity=now.getHumidity()+"%";
                    String pressure=now.getPressure()+"hpa";
                    String vis=now.getVis()+"km";
                    String cloud=now.getCloud()+"%";
                    info=new String[]{feelslike,weather,windDir,wind,humidity,pressure,vis,cloud};
                    initData();
                    mRecyclerView = view.findViewById(R.id.recycler_wt);
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter =new WeatherInfoAdapter(mPhotoList);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.addItemDecoration(new SpacesItemDecoration(30));
                    updatetime.setText("???????????????"+updateTime);
                    String city=sharedPreferences.getString("city","");
                    city_weather_info.setText(city+" "+temp);
                    if(city.equals("??????")||city.equals("?????????")||city.equals("beijing")){
                        imgCity.setBackgroundResource(R.drawable.ic_beijing);
                    }
                    else if(city.equals("??????")||city.equals("?????????")||city.equals("shanghai")){
                        imgCity.setBackgroundResource(R.drawable.ic_shanghai);
                    }
                    else if(city.equals("??????")||city.equals("?????????")||city.equals("guangzhou")){
                        imgCity.setBackgroundResource(R.drawable.ic_guangzhou);
                    }
                    else if(city.equals("??????")||city.equals("?????????")){
                        imgCity.setBackgroundResource(R.drawable.ic_shenzheng);
                    }
                    else if(city.equals("??????")||city.equals("?????????")){
                        imgCity.setBackgroundResource(R.drawable.ic_nanjing);
                    }
                    else if(city.equals("??????")||city.equals("?????????")){
                        imgCity.setBackgroundResource(R.drawable.ic_chongqing);
                    }
                    else if(city.equals("??????")||city.equals("?????????")){
                        imgCity.setBackgroundResource(R.drawable.ic_chengdu);
                    }
                    else if(city.equals("??????")||city.equals("?????????")||city.equals("hangzhou")){
                        imgCity.setBackgroundResource(R.drawable.ic_hangzhou);
                    }
                    else if(city.equals("??????")||city.equals("?????????")){
                        imgCity.setBackgroundResource(R.drawable.ic_shenyang);
                    }
                    else if(city.equals("??????")||city.equals("?????????")){
                        imgCity.setBackgroundResource(R.drawable.ic_suzhou);
                    }
                    else if(city.equals("??????")||city.equals("?????????")){
                        imgCity.setBackgroundResource(R.drawable.ic_tianjin);
                    }
                    else if(city.equals("??????")||city.equals("?????????")){
                        imgCity.setBackgroundResource(R.drawable.ic_wuhan);
                    }
                    else if(city.equals("??????")||city.equals("?????????")){
                        imgCity.setBackgroundResource(R.drawable.ic_zhengzhou);
                    }
                    else if(city.equals("??????")||city.equals("?????????")){
                        imgCity.setBackgroundResource(R.drawable.ic_xian);
                    }
                    else if(city.equals("??????")||city.equals("?????????")){
                        imgCity.setBackgroundResource(R.drawable.ic_xianggang);
                    }
                    else if(city.equals("??????")||city.equals("?????????")){
                        imgCity.setBackgroundResource(R.drawable.ic_shengzhou);
                    }
                    else {
                        imgCity.setBackgroundResource(R.drawable.ic_other);
                    }
                } else {
                    //???????????????????????????????????????
                    Code code = weatherBean.getCode();
                    Log.i(TAG, "failed code: " + code);
                    Toast.makeText(getActivity(),"????????????????????????????????????????????????",Toast.LENGTH_LONG).show();
                }
            }
        });
        QWeather.getWeather3D(getContext(), locationID, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "getWeather onError: " + throwable);
            }

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherDailyBean));
                if(Code.OK == weatherDailyBean.getCode()) {
                    List<WeatherDailyBean.DailyBean> daily=weatherDailyBean.getDaily();
                    String date1= daily.get(0).getFxDate();
                    String date2=daily.get(1).getFxDate();
                    String date3=daily.get(2).getFxDate();
                    date=new String[]{date1,date2,date3};
                    String info1=daily.get(0).getTextDay()+"/"+daily.get(0).getTextNight()+"  "+daily.get(0).getTempMin()+"???"+"/"+daily.get(0).getTempMax()+"???";
                    String info2=daily.get(1).getTextDay()+"/"+daily.get(1).getTextNight()+"  "+daily.get(1).getTempMin()+"???"+"/"+daily.get(1).getTempMax()+"???";
                    String info3=daily.get(2).getTextDay()+"/"+daily.get(2).getTextNight()+"  "+daily.get(2).getTempMin()+"???"+"/"+daily.get(2).getTempMax()+"???";
                    future_info=new String[]{info1,info2,info3};
                    initData_future();
                    fRecyclerView = view.findViewById(R.id.recycler_ft);
                    fLayoutManager = new LinearLayoutManager(getActivity());
                    fRecyclerView.setLayoutManager(fLayoutManager);
                    fAdapter =new FutureInfoAdapter(fList);
                    fRecyclerView.setAdapter(fAdapter);
                    fRecyclerView.addItemDecoration(new SpacesItemDecoration(30));
                } else {
                    //???????????????????????????????????????
                    Code code = weatherDailyBean.getCode();
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });
    }
    private void initData() {
        mPhotoList = new ArrayList<>();
        for (int i = 0; i < indexs.length; i++) {
            Icon icon = new Icon();
            icon.setIcon(indexs[i]);
            icon.setTitle(title[i]);
            icon.setInfo(info[i]);
            mPhotoList.add(icon);
        }
    }
    private void initData_future(){
        fList =new ArrayList<>();
        for(int i=0;i<3;i++){
            FutureIcon fi=new FutureIcon();
            fi.setFuture_icon(icon[i]);
            fi.setDate(date[i]);
            fi.setFuture_info(future_info[i]);
            fList.add(fi);
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
           outRect.left = space-20;
//            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildPosition(view) == 0)
                outRect.top = space-10;
        }
    }
}