package com.example.finalexample.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalexample.Activity.MainActivity;
import com.example.finalexample.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Button btn1=(Button) findViewById(R.id.btn1);
        EditText edit1=(EditText) findViewById(R.id.password1);
        EditText edit2=(EditText) findViewById(R.id.password2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password1=edit1.getText().toString();
                String password2=edit2.getText().toString();
                if(TextUtils.isEmpty(password1)){
                    Toast.makeText(SettingActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }else if(!password1.equals(password2)){
                    Toast.makeText(SettingActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    intent.putExtra("newPassword",password1);
                    startActivity(intent);
                }
            }
        });
    }
}