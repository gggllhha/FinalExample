package com.example.finalexample.ui;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalexample.R;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.example.finalexample.db.DBHistory;
import com.example.finalexample.db.History;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.view.QWeather;

import java.util.List;

import com.example.finalexample.ui.Weather.Weather1Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#/newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    private TextureMapView mapView = null;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    private LocationClient mLocationClient;
    private TextView postionText;
    public MyLocationListener myListener = new MyLocationListener();
    DBHistory mDBHistory;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        mLocationClient = new LocationClient(getContext());
        mLocationClient.registerLocationListener(myListener);
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        postionText = (TextView) rootView.findViewById(R.id.position_text_view);
        mapView = (TextureMapView) rootView.findViewById(R.id.map);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);//显示当地位置
        mLocationClient.start();
        initLocation();
        Log.i("test","onCreateView");
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("test","onViewCreated");
    }

    private void navigateTo(BDLocation location) {
        //if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);//缩放级别
            baiduMap.animateMapStatus(update);//传入到经纬度
            isFirstLocate = false;//防止多次调用
        //}
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

    private void initLocation() {//设置更新时间的间隔
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        //option.setScanSpan(5000);//每隔5000毫秒一次更新
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);//强制GPS
        option.setIsNeedAddress(true);//需要精确的信息
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("test","onDestroyView");
        mapView.onDestroy();
        mLocationClient.stop();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("test","onResume");
        mapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("test","onPause");
        mapView.onPause();
    }

    //
    class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType() == bdLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(bdLocation);
            }
            SharedPreferences.Editor editor;
            SharedPreferences sharedPreferences= getActivity().getSharedPreferences("cityInfo", Context.MODE_MULTI_PROCESS);
            StringBuilder currentPostion = new StringBuilder();
            currentPostion.append("纬度：").append(bdLocation.getLatitude()).append("\n");
            currentPostion.append("经度：").append(bdLocation.getLongitude()).append("\n");
            currentPostion.append("国家：").append(bdLocation.getCountry()).append("\n");
            currentPostion.append("省：").append(bdLocation.getProvince()).append("\n");
            currentPostion.append("市：").append(bdLocation.getCity()).append("\n");
            currentPostion.append("区：").append(bdLocation.getDistrict()).append("\n");
            String city=bdLocation.getCity();
            editor=sharedPreferences.edit();
            String defaultcity=sharedPreferences.getString("city","");
            if(TextUtils.isEmpty(defaultcity)) {
                final String[] locationID = new String[1];
                QWeather.getGeoCityLookup(getContext(), city, new QWeather.OnResultGeoListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        Log.i(TAG, "getCity onError: " + throwable);
                        Toast.makeText(getActivity(),"查询不到该城市天气信息,请检查是否输入有误!",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(GeoBean geoBean) {
                        if(Code.OK==geoBean.getCode()){
                            System.out.println("success");
                            List<GeoBean.LocationBean> location=geoBean.getLocationBean();
                            locationID[0] =location.get(0).getId();
                            String oldID=sharedPreferences.getString("ID","777");
                            if(!oldID.equals(locationID[0])) {
                                saveHistory(locationID[0],city);
                                System.out.println("saved");
                            }
                            editor.putString("city", city);
                            editor.putString("ID",locationID[0]);
                            editor.apply();
                        }
                    }
                });

            }else{
                final String[] locationID = new String[1];
                String date;
                QWeather.getGeoCityLookup(getContext(), defaultcity, new QWeather.OnResultGeoListener() {
                    @Override
                    public void onError(Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(GeoBean geoBean) {
                        if(Code.OK==geoBean.getCode()){
                            List<GeoBean.LocationBean> location=geoBean.getLocationBean();
                            locationID[0] =location.get(0).getId();
                            String oldID=sharedPreferences.getString("ID","");
                            if(!oldID.equals(locationID[0])) {
                                saveHistory(locationID[0],defaultcity);
                            }
                            editor.putString("ID",locationID[0]);
                            editor.apply();
                        }
                    }
                });
            }

            if (bdLocation.getLocType() == bdLocation.TypeGpsLocation) {
                currentPostion.append("GPS");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentPostion.append("网络");
            } else {
                currentPostion.append("无");
            }
            postionText.setText(currentPostion);
        }
    }
    public void saveHistory(String locationID,String cityName){
        mDBHistory=DBHistory.getInstance(getContext());
        QWeather.getWeather3D(getContext(), locationID, new QWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                List<WeatherDailyBean.DailyBean> daily=weatherDailyBean.getDaily();
                String date=daily.get(0).getFxDate();
                String TextDay=daily.get(0).getTextDay();
                String TextNight=daily.get(0).getTextNight();
                String TempMin=daily.get(0).getTempMin();
                String TempMax=daily.get(0).getTempMax();
                History h=new History(locationID,cityName,date,TextDay,TextNight,TempMin,TempMax);
                mDBHistory.getDaoHistory().insertHistory(h);
            }
        });
    }

}