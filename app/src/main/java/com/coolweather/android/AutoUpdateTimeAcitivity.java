package com.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.service.AutoUpdateService;

public class AutoUpdateTimeAcitivity extends AppCompatActivity implements View.OnClickListener{

    private TextView menu_title;
    private Button menu_back;
    private Button btn_three;
    private Button btn_five;
    private Button btn_eight;
    private Button btn_twelve;
    private Button btn_zero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_update_time_acitivity);

        menu_title = (TextView) findViewById(R.id.menu_title);
        menu_title.setText("更新频率");
        menu_back = (Button) findViewById(R.id.menu_back);
        menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AutoUpdateTimeAcitivity.this,SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_three = (Button) findViewById(R.id.hour_three);
        btn_five = (Button) findViewById(R.id.hour_five);
        btn_eight = (Button) findViewById(R.id.hour_eight);
        btn_twelve = (Button) findViewById(R.id.hour_twelve);
        btn_zero = (Button) findViewById(R.id.hour_zero);

        btn_three.setOnClickListener(this);
        btn_five.setOnClickListener(this);
        btn_eight.setOnClickListener(this);
        btn_twelve.setOnClickListener(this);
        btn_zero.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        switch (v.getId()) {
            case R.id.hour_zero:
                editor.putBoolean("isUpdateTime", false);
                break;
            case R.id.hour_three:
                editor.putBoolean("isUpdateTime", true);
                editor.putInt("autoUpdateTime", 180);
                break;
            case R.id.hour_five:
                editor.putBoolean("isUpdateTime", true);
                editor.putInt("autoUpdateTime", 300);
                break;
            case R.id.hour_eight:
                editor.putBoolean("isUpdateTime", true);
                editor.putInt("autoUpdateTime", 480);
                break;
            case R.id.hour_twelve:
                editor.putBoolean("isUpdateTime", true);
                editor.putInt("autoUpdateTime", 720);
                break;
            default:

        }
        editor.apply();
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
        Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}
