package com.example.fishingport.app.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.fishingport.app.R;
import com.example.fishingport.app.base.BaseAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
* 积分规则详情
* */
public class ScoreRulesActivity extends BaseAppCompatActivity {
     @BindView(R.id.webview)
    WebView webView;
    String url="http://201704yg.alltosun.net/score/m/score_rule";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBarTitle("积分规则");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_score_rules;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }
}
