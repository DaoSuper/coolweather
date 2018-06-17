package com.coolweather.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.gson.Basic;
import com.coolweather.android.gson.Now;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar_share;
    private RelativeLayout shareView;
    private Button shareButton;
    private ImageView shareImage;
    private TextView menu_title;
    private Button menu_back;

    private TextView shareCity;
    private TextView shareTime;
    private TextView shareTmp;
    private TextView shareWeatherInfo;
    private EditText shareText;

    private String mWeatherId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        toolbar_share = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar_share);
        menu_title = (TextView) findViewById(R.id.menu_title);
        menu_title.setText("分享");
        menu_back = (Button) findViewById(R.id.menu_back);
        menu_back.setOnClickListener(this);
        shareView = (RelativeLayout) findViewById(R.id.rl_share);
        shareImage = (ImageView) findViewById(R.id.share_image);
        shareButton = (Button) findViewById(R.id.btn_share_image);
        shareButton.setOnClickListener(this);

        shareCity = (TextView) findViewById(R.id.share_city);
        shareTime = (TextView) findViewById(R.id.share_time);
        shareTmp = (TextView) findViewById(R.id.share_tmp);
        shareWeatherInfo = (TextView) findViewById(R.id.share_weather_info);
        shareText = (EditText) findViewById(R.id.share_text);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            String cityName = weather.basic.cityName;
            String time = weather.basic.update.updateTime.split(" ")[0];
            String degree = weather.now.temperature + "°";
            String weatherInfo = weather.now.more.info;
            shareCity.setText(cityName);
            shareTime.setText(time);
            shareTmp.setText(degree);
            shareWeatherInfo.setText(weatherInfo);
        } else {
            // 无缓存时去服务器查询天气
            Toast.makeText(ShareActivity.this, "获取天气信息失败,无法分享", Toast.LENGTH_SHORT).show();
        }

        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(shareImage);
        } else {
            loadBingPic();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(toolbar_share != null){
            toolbar_share.setTitle("");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_share_image:
                Bitmap viewBitmap = makeView2Bitmap(shareView);
                saveMypic(viewBitmap);
                break;
            case R.id.menu_back:
                //返回上一个界面
                Intent intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
        }
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ShareActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(ShareActivity.this).load(bingPic).into(shareImage);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 将bitmap保存为JPG格式的图片
     * @param  bitmap
     */
    public void saveMypic(Bitmap bitmap){
        //非空判断
        if (bitmap == null){
            return;
        }
        //保存图片
        try{
            File file = new File(Environment.getExternalStorageDirectory(),
                    SystemClock.currentThreadTimeMillis() + ".jpg");
            String url = file.getAbsolutePath();
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
            shareMsg(null, "分享天气", null, url);
            //Toast.makeText(this, "保存成功，已经保存到"+ url, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 分享功能
     * @param activityTitle
     *            Activity的名字
     * @param msgTitle
     *            消息标题
     * @param msgText
     *            消息内容
     * @param imgPath
     *            图片路径，不分享图片则传null
     */
    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }

    public Bitmap makeView2Bitmap(View view) {
        //View是你需要绘画的View
        int width = view.getWidth();
        int height = view.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //如果不设置canvas画布为白色，则生成透明   							canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 清除edittext光标
     * @param ev
     * @return
     */
    @Override
    public  boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                //清除Edittext的焦点从而让光标消失
                v.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v !=null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
            top = l[1],
            bottom = top + v.getHeight(),
            right = left + v.getWidth();
            if (event.getX() > left &&event.getX() < right && event.getY()> top && event.getY() < bottom) {
                //点击EditText的时候不做隐藏处理
                return false;
            } else {
                return true;
            }
        }
        //如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }
}
