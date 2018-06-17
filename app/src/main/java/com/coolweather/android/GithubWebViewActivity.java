package com.coolweather.android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class GithubWebViewActivity extends AppCompatActivity {

    private WebView webView;
    private AlertDialog alertDialog;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_web_view);
        initWebView();
    }
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }

        protected void initWebView() {
            progressBar = ProgressDialog.show(GithubWebViewActivity.this, null, "正在进入网页，请稍后...");
            webView = (WebView) this.findViewById(R.id.github_webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("https://github.com/DaoSuper");
            alertDialog = new AlertDialog.Builder(this).create();
            webView.setWebViewClient(new MyWebViewClient());

        }

        class MyWebViewClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(GithubWebViewActivity.this, "网页加载出错", Toast.LENGTH_LONG).show();
                alertDialog.setTitle("ERROR");
                alertDialog.setMessage(description);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"OK",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        }
}
