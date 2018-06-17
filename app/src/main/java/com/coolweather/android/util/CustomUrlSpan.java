package com.coolweather.android.util;

import android.content.Context;
import android.content.Intent;
import android.text.style.ClickableSpan;
import android.view.View;

import com.coolweather.android.GithubWebViewActivity;

public class CustomUrlSpan extends ClickableSpan {

    private Context context;

    private String url;

    public CustomUrlSpan(Context context,String url){

        this.context = context;

        this.url = url;

    }

    @Override

    public void onClick(View widget) {
        Intent intent = new Intent(context,GithubWebViewActivity.class);
        intent.putExtra("https://github.com/DaoSuper",url);
        context.startActivity(intent);

    }
}