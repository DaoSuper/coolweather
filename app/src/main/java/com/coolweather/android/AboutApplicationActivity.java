package com.coolweather.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coolweather.android.util.CustomUrlSpan;
import com.coolweather.android.util.NoUnderlineSpan;

public class AboutApplicationActivity extends AppCompatActivity {

    private TextView tv_github;
    private TextView menu_title;
    private Button menu_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);
        menu_title = (TextView) findViewById(R.id.menu_title);
        menu_title.setText("关于我们");
        menu_back = (Button) findViewById(R.id.menu_back);
        menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutApplicationActivity.this,SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tv_github = (TextView) findViewById(R.id.github_address);
        interceptHyperLink(tv_github);
        removeHyperLinkUnderline(tv_github);
    }

    /**
     * 拦截超链接
     * @param tv
     */
    private void interceptHyperLink(TextView tv){
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence txt = tv.getText();
        if (txt instanceof Spannable){
            int end = txt.length();
            Spannable spannable = (Spannable) tv.getText();
            URLSpan [] urlSpans = spannable.getSpans(0,end,URLSpan.class);
            if (urlSpans.length == 0){
                return;
            }

            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(txt);
            // 循环遍历并拦截 所有https://开头的链接
            for (URLSpan uri : urlSpans){
                String url = uri.getURL();
                if (url.indexOf("https://") == 0) {
                    CustomUrlSpan customUrlSpan = new CustomUrlSpan(this,url);
                    spannableStringBuilder.setSpan(customUrlSpan,spannable.getSpanStart(uri),spannable.getSpanEnd(uri), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
            tv.setText(spannableStringBuilder);
        }
    }

    /**
     * 去除超链接下划线
     */

    private void removeHyperLinkUnderline(TextView tv) {
        CharSequence text = tv.getText();
        if(text instanceof Spannable){
            Spannable spannable = (Spannable) tv.getText();
            NoUnderlineSpan noUnderlineSpan = new NoUnderlineSpan();
            spannable.setSpan(noUnderlineSpan,0,text.length(), Spanned.SPAN_MARK_MARK);
        }
    }
}
