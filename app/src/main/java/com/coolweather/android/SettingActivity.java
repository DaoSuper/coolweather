package com.coolweather.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity  implements View.OnClickListener{

    private Toolbar toolbar_setting;
    private Button aboutApplication;
    private Button autoUpdateTime;
    private TextView menu_title;
    private Button menu_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar_setting = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar_setting);
        aboutApplication = (Button) findViewById(R.id.about_app);
        autoUpdateTime = (Button) findViewById(R.id.auto_update_time);
        menu_title = (TextView) findViewById(R.id.menu_title);
        menu_title.setText("设置");
        menu_back = (Button) findViewById(R.id.menu_back);
        aboutApplication.setOnClickListener(this);
        autoUpdateTime.setOnClickListener(this);
        menu_back.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(toolbar_setting != null){
            toolbar_setting.setTitle("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_app:
                // 跳转到关于天气界面
                Intent intent = new Intent(this,AboutApplicationActivity.class );
                startActivity(intent);
                break;
            case R.id.auto_update_time:
                //跳转到自动更新频率界面
               Intent intent1 = new Intent(this,AutoUpdateTimeAcitivity.class );
                startActivity(intent1);
                break;
            case R.id.menu_back:
                //返回上一个界面
                Intent intent2 = new Intent(this,WeatherActivity.class );
                startActivity(intent2);
                finish();
                break;
            default:
        }
    }
}
