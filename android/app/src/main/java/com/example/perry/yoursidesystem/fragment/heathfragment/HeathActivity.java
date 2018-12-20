package com.example.perry.yoursidesystem.fragment.heathfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.perry.yoursidesystem.R;
import com.example.perry.yoursidesystem.test.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by perry on 2017/12/19.
 */

public class HeathActivity extends AppCompatActivity {
    private TextView titleView;
    private WebView webView;
    private Map<Integer, String> urlMap;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_heath);
        Toolbar toolbar = (Toolbar) findViewById(R.id.heath_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.backup);
        }
        titleView = (TextView) findViewById(R.id.heath_title);
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        initMap();
        Intent intent = getIntent();
        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        settings.setSupportZoom(false);
       
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient());
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }


                super.onProgressChanged(view, newProgress);
            }
        });
        String url=urlMap.get(intent.getIntExtra(HeathAdapter.TITLE_ARG, 0));
        LogUtil.i("test", url);
        webView.loadUrl(url);

    }

    private void initMap() {
        urlMap = new HashMap<>();
        urlMap.put(R.string.hTitle1, "http://www.39yst.com/yufang/512135.shtml");
        urlMap.put(R.string.hTitle2, "http://www.39yst.com/yufang/512118.shtml");
        urlMap.put(R.string.hTitle3, "http://www.39yst.com/yufang/512104.shtml");
        urlMap.put(R.string.hTitle4, "http://www.39yst.com/yufang/512098.shtml");
        urlMap.put(R.string.hTitle5, "http://www.39yst.com/yufang/512089.shtml");
        urlMap.put(R.string.hTitle6, "http://www.39yst.com/yufang/512089.shtml");
        urlMap.put(R.string.hTitle7, "http://www.39yst.com/yufang/512082.shtml");
        urlMap.put(R.string.hTitle8, "http://www.39yst.com/yufang/512080.shtml");
        urlMap.put(R.string.hTitle9, "http://www.39yst.com/yufang/512070.shtml");
        urlMap.put(R.string.hTitle10, "http://www.39yst.com/yufang/512069.shtml");
        urlMap.put(R.string.hTitle11, "http://www.39yst.com/yufang/512065.shtml");
        urlMap.put(R.string.hTitle12, "http://www.39yst.com/yufang/512061.shtml");
        urlMap.put(R.string.hTitle13, "http://www.39yst.com/yufang/512045.shtml");
        urlMap.put(R.string.hTitle14, "http://www.39yst.com/yufang/512028.shtml");
        urlMap.put(R.string.hTitle15, "http://www.39yst.com/yufang/511987.shtml");
        urlMap.put(R.string.hTitle16, "http://www.39yst.com/yufang/511985.shtml");
        urlMap.put(R.string.hTitle17, "http://www.39yst.com/yufang/511913.shtml");
        urlMap.put(R.string.hTitle18, "http://www.39yst.com/yufang/512016.shtml");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }

        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
//            AlertDialog.Builder builder=new AlertDialog.Builder(this);
//            builder.setTitle("是否退出？");
//            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    finish();
//                }
//            });
//            builder.setNegativeButton("否",null);
//            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
