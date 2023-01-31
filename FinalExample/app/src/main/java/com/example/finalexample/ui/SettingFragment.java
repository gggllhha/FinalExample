package com.example.finalexample.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalexample.R;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.view.QWeather;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#/newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    private EditText edit_pwd;
    private CheckBox cb;
    private Button btn_setting;
    private SharedPreferences sp_login,sp_city,sp_his;
    private Boolean is_autoLogin;
    private EditText edit_city,edit_his;
    private TextView tv_defaultCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sp_login=getActivity().getSharedPreferences("autoLogin",Context.MODE_PRIVATE);
        sp_city=getActivity().getSharedPreferences("cityInfo",Context.MODE_MULTI_PROCESS);
        sp_his=getActivity().getSharedPreferences("historyCount",Context.MODE_PRIVATE);
        btn_setting=(Button)view.findViewById(R.id.btn_setting);
        edit_pwd=(EditText) view.findViewById(R.id.edit_pwd);
        edit_city=(EditText)view.findViewById(R.id.edit_city);
        edit_his=(EditText)view.findViewById(R.id.edit_his);
        tv_defaultCity=(TextView)view.findViewById(R.id.tv_defaultCity);
        cb=(CheckBox) view.findViewById(R.id.cb);
        cb.setChecked(sp_login.getBoolean("AUTO_ISCHECK",false));
        tv_defaultCity.setText("当前默认城市："+sp_city.getString("city",""));
        String oldCount= String.valueOf(sp_his.getInt("count",4));
        edit_his.setText(oldCount);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=sp_login.edit();
                String newpassword=edit_pwd.getText().toString();
                String defaultcity=edit_city.getText().toString();
                int hisCount= Integer.parseInt(edit_his.getText().toString());
                if(!TextUtils.isEmpty(newpassword)) {
                    save(newpassword);
                    edit_pwd.setText("");
                }
                if(!TextUtils.isEmpty(edit_his.getText().toString())){
                    SharedPreferences.Editor editor_his=sp_his.edit();
                    editor_his.putInt("count",hisCount);
                    editor_his.apply();
                }
                is_autoLogin=cb.isChecked();
                editor.putBoolean("AUTO_ISCHECK",is_autoLogin);
                editor.apply();
                if(!TextUtils.isEmpty(defaultcity)){
                    SharedPreferences.Editor editor_city=sp_city.edit();
                    QWeather.getGeoCityLookup(getContext(), defaultcity, new QWeather.OnResultGeoListener() {
                        @Override
                        public void onError(Throwable throwable) {
                            Toast.makeText(getActivity(),"查询不到该城市天气信息,请检查是否输入有误!",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess(GeoBean geoBean) {
                            editor_city.putString("city",defaultcity);
                            editor_city.apply();
                            tv_defaultCity.setText("当前默认城市："+sp_city.getString("city",""));
                            edit_city.setText("");
                            Toast.makeText(getActivity(),"修改成功！",Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Toast.makeText(getActivity(),"修改成功！",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void save(String inputText){
        FileOutputStream out =null;
        BufferedWriter writer=null;
        try {
            out=getActivity().openFileOutput("password", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(writer!=null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}