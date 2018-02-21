package com.example.fishingport.app.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baidu.platform.comapi.map.B;
import com.example.fishingport.app.R;
import com.example.fishingport.app.base.BaseAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
* 问题详情
*
* */

public class QuestionDetails extends BaseAppCompatActivity {
    @BindView(R.id.webview)
    WebView webView;
    private  String question_id="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBarTitle("常见问题详情");
        question_id=getIntent().getStringExtra("id");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        // http://201704yg.alltosun.net.
        webView.loadUrl("http://201704yg.alltosun.net/questions/m/detail?question_id="+question_id+"");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                // view.loadUrl(url);
                Log.e("url",url);
                return true;
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_question_details;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }
}
