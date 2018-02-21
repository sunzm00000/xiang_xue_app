package com.example.fishingport.app.ui.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fishingport.app.R;
import com.example.fishingport.app.base.BaseFragment;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.WeiboDialogUtils;
import com.example.fishingport.app.ui.activity.FishingCircleActivity;
import com.example.fishingport.app.ui.activity.FriendInfoActivity;
import com.example.fishingport.app.ui.activity.PhotoBrowserActivity;
import com.example.fishingport.app.ui.activity.ReleaseDynamicsActivity;
import com.yalantis.ucrop.entity.LocalMedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/26.
 * 渔友圈
 */

public class FishingCircleFragment extends BaseFragment {
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.l1)
    LinearLayout l1;
    @BindView(R.id.tabs)
    TabLayout tabs;
    private Dialog mWeiboDialog;
    private List<LocalMedia> selectMedia = new ArrayList<>();
    private BroadcastReceiver broacastreciver;
    private  int istiao=0;
    private  int tabposition=0;
    private int isemty=-1;
     @BindView(R.id.send_port)
    TextView send_port;
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        registerBoradcastReceiver();
        //启用支持javascript
        tabs.addTab(tabs.newTab().setText("最新"));
        tabs.addTab(tabs.newTab().setText("好友"));
        tabs.addTab(tabs.newTab().setText("我的"));
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        // 方法，特别重要
        webview.addJavascriptInterface(new JsToJava(), "picture");
        webview.addJavascriptInterface(new JsToJava(), "empty");
        webview.addJavascriptInterface(new JsToJava(), "error");
        webview.addJavascriptInterface(new onClickUser(), "onclickuser");
        webview.loadUrl("http://201704yg.alltosun.net/fishing_circle/m?user_id=" + SharedPreferencedUtils.getString(getActivity(), "uid")+"&fromApp=1");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                // view.loadUrl(url);
                Log.e("url",url);
                istiao=1;
                Intent intent = new Intent(getActivity(), FishingCircleActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //加载完成
               WeiboDialogUtils.closeDialog(mWeiboDialog);
                Log.i("json返回结果","加载完成"+url);
                if (isemty==1){
                    webview.setVisibility(View.GONE);
                    l1.setVisibility(View.VISIBLE);
                }else if (isemty==0){
                    webview.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //加载开始
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //加载错误
            }
        });
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i("执行了",tab.getPosition()+"");
                tabposition=tab.getPosition();
                if (tab.getPosition()==0){
                    getActivity().deleteDatabase("webview.db");
                    getActivity().deleteDatabase("webviewCache.db");
                    webview.loadUrl("http://201704yg.alltosun.net/fishing_circle/m?user_id=" + SharedPreferencedUtils.getString(getActivity(), "uid")+"&fromApp=1");
                } else  if (tab.getPosition()==1){
                    getActivity().deleteDatabase("webview.db");
                    getActivity().deleteDatabase("webviewCache.db");
                    webview.loadUrl("http://201704yg.alltosun.net/fishing_circle/m?user_id=" + SharedPreferencedUtils.getString(getActivity(), "uid")+"&get_type=1"+"&fromApp=1");
                }else  if (tab.getPosition()==2){
                    Log.i("我的","我的执行了");
                    getActivity().deleteDatabase("webview.db");
                    getActivity().deleteDatabase("webviewCache.db");
                    webview.loadUrl("http://201704yg.alltosun.net/fishing_circle/m?user_id=" + SharedPreferencedUtils.getString(getActivity(), "uid")+"&get_type=2"+"&fromApp=1");
                }

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fishingcircle;
    }
    @OnClick({R.id.img_right,R.id.send_port})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_right:
                istiao=1;
                startActivity(new Intent(getActivity(), ReleaseDynamicsActivity.class));
                break;
            case R.id.send_port:
                istiao=1;
                startActivity(new Intent(getActivity(), ReleaseDynamicsActivity.class));
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (istiao==1) {
            istiao = 0;
              webview.loadUrl("javascript:deleteFishingCircleSuccess()");//刷新页面
            if (tabposition==0){
                webview.loadUrl("http://201704yg.alltosun.net/fishing_circle/m?user_id=" + SharedPreferencedUtils.getString(getActivity(), "uid")+"&fromApp=1");
            } else  if (tabposition==1){
                webview.loadUrl("http://201704yg.alltosun.net/fishing_circle/m?user_id=" + SharedPreferencedUtils.getString(getActivity(), "uid")+"&get_type=1"+"&fromApp=1");
            }else  if (tabposition==2){
                Log.i("我的","我的执行了");
                webview.loadUrl("http://201704yg.alltosun.net/fishing_circle/m?user_id=" + SharedPreferencedUtils.getString(getActivity(), "uid")+"&get_type=2"+"&fromApp=1");
            }

        }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
    private class JsToJava {
        @JavascriptInterface
        public void onClickPicture(String paramFromJS) {
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
                    intent.setClass(getActivity(), PhotoBrowserActivity.class);
                    getActivity().startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @JavascriptInterface
        public void onError(String paramFromJS) {
            System.out.println("js返回结果" + paramFromJS);//处理返回的结果
            Log.e("js返回结果" , paramFromJS);
        }
        @JavascriptInterface
        public void onEmpty(String paramFromJS) {
            if (paramFromJS.equals("1")){
                //为空
                isemty=1;
                Log.i("json返回结果","为空"+paramFromJS);

            } else {
                isemty=0;
                Log.i("json返回结果","不为空"+paramFromJS);

            }
        }
    }
    private class onClickUser {
        @JavascriptInterface
        public void onClickUser(String paramFromJS) {
            System.out.println("js返回结果" + paramFromJS);//处理返回的结果
            Log.e("js返回结果" , paramFromJS);
            Intent i=new Intent(getActivity(), FriendInfoActivity.class);
            i.putExtra("type",1);
            i.putExtra("uid",paramFromJS.split(",")[0]);
            i.putExtra("huanxin_name",paramFromJS.split(",")[1]);
            getActivity().startActivity(i);
        }
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("fishingcircle")){
                webview.loadUrl("javascript:addFishingCircleSuccess('"+intent.getStringExtra("id")+"')");
            }
        }

    };

    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("fishingcircle");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

}
