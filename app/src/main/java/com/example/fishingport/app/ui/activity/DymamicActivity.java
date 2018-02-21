package com.example.fishingport.app.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.WeiboDialogUtils;
import com.example.fishingport.app.ui.fragment.FishingCircleFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DymamicActivity extends BaseAppCompatActivity {
 @BindView(R.id.webview)
    WebView webview;
    private  String type="";
    private  String url="";
    private  String uid="";
    private  int istiao=0;
    private Dialog mWeiboDialog;
    @BindView(R.id.l1)
    LinearLayout l1;
    private int isempty=-1;
    @BindView(R.id.send_port)
    TextView send_port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        type=getIntent().getStringExtra("type");
        uid=getIntent().getStringExtra("uid");
        if (type.equals("0")){
            //我的渔友圈
            url= "http://201704yg.alltosun.net/fishing_circle/m?user_id="+uid+"&get_type=2"+"&fromApp=1";
        }else {
            url="http://201704yg.alltosun.net/fishing_circle/m?user_id="+SharedPreferencedUtils.getString(DymamicActivity.this,"uid")
                    +"&friend_id="+uid+"&fromApp=1";
        }
        Log.i("显示的url",url);
        setToolBarTitle(getIntent().getStringExtra("isme"));
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        // 方法，特别重要
        webview.addJavascriptInterface(new DymamicActivity.JsToJava(), "picture");
        webview.addJavascriptInterface(new DymamicActivity.JsToJava(), "empty");
        webview.addJavascriptInterface(new DymamicActivity.JsToJava(), "error");
        webview.addJavascriptInterface(new DymamicActivity.onClickUser(), "onclickuser");
        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                // view.loadUrl(url);
                Log.e("url",url);
                istiao=1;
                Intent intent = new Intent(DymamicActivity.this, FishingCircleActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //加载完成
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Log.i("json返回结果","加载完成");
                if (isempty==1){
                    webview.setVisibility(View.GONE);
                    l1.setVisibility(View.VISIBLE);
                    if (type.equals("0")){
                        send_port.setText("发布一条动态");
                        send_port.setClickable(true);
                    }else {
                        send_port.setText("该好友暂无动态");
                        send_port.setClickable(false);
                    }
                }else if (isempty==0){
                    webview.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //加载开始
                 mWeiboDialog = WeiboDialogUtils.createLoadingDialog(DymamicActivity.this, "加载中...");
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //加载错误
            }
        });

        send_port.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                istiao=1;

                startActivity(new Intent(DymamicActivity.this, ReleaseDynamicsActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (istiao==1){
            webview.loadUrl("javascript:deleteFishingCircleSuccess()");//刷新页面
            istiao=0;
            webview.loadUrl(url);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dymamic;
    }
    private class onClickUser {
        @JavascriptInterface
        public void onClickUser(String paramFromJS) {
            System.out.println("js返回结果" + paramFromJS);//处理返回的结果
            Log.e("js返回结果" , paramFromJS);
            Intent i=new Intent(DymamicActivity.this, FriendInfoActivity.class);
            i.putExtra("type",1);
            i.putExtra("uid",paramFromJS.split(",")[0]);
            i.putExtra("huanxin_name",paramFromJS.split(",")[1]);
            DymamicActivity.this.startActivity(i);


        }
    }
    private class JsToJava {
        @JavascriptInterface
        public void onClickPicture(String paramFromJS) {
          /*  Toast.makeText(mActivity,paramFromJS, Toast.LENGTH_SHORT).show();
            System.out.println("js返回结果" + paramFromJS);//处理返回的结果
            Log.e("js返回结果" , paramFromJS);*/
            try {
                JSONObject object=new JSONObject(paramFromJS);
                int position=object.optInt("clickImageIndex");
                List<String> list=new ArrayList<>();
                JSONArray array=object.optJSONArray("imageUrls");
                String [] imageUrls=new String[array.length()];
                for (int i=0;i<array.length();i++){
                    JSONObject arrayobject=array.optJSONObject(i);
                    list.add(arrayobject.optString("url"));
                    Log.i("图片的地址",arrayobject.optString("url"));
                    imageUrls[i]=arrayobject.optString("url");
                }
                if (list.size()>0){
                    Log.i("图片详情==",list.get(0));
                    Intent intent = new Intent();
                    intent.putExtra("imageUrls", imageUrls);
                    intent.putExtra("curImageUrl", imageUrls[position]);
                    intent.setClass(DymamicActivity.this, PhotoBrowserActivity.class);
                    DymamicActivity.this.startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @JavascriptInterface
        public void onError(String paramFromJS) {
            Toast.makeText(DymamicActivity.this,paramFromJS, Toast.LENGTH_SHORT).show();
            System.out.println("js返回结果" + paramFromJS);//处理返回的结果
            Log.e("js返回结果" , paramFromJS);




        }
        @JavascriptInterface
        public void onEmpty(String paramFromJS) {


//            Toast.makeText(mActivity,paramFromJS, Toast.LENGTH_SHORT).show();
//            System.out.println("js返回结果" + paramFromJS);//处理返回的结果
          Log.e("js返回结果" , paramFromJS);

            if (paramFromJS.equals("1")){
                isempty=1;
            } else {
                isempty=0;
            }
        }
    }
    @Override
    protected int getFragmentContentId() {
        return 0;
    }
}
