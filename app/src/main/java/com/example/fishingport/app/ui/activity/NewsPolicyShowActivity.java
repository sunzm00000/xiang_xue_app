package com.example.fishingport.app.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FishingCircleConstract;
import com.example.fishingport.app.api.HomeConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.NewsPolicyBean;
import com.example.fishingport.app.presenter.FishingCirclePresenter;
import com.example.fishingport.app.presenter.HomePresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.KeyBoardHelper;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.tools.WeiboDialogUtils;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* 新闻政策详情
* */
public class NewsPolicyShowActivity extends BaseAppCompatActivity implements
        HomeConstract.view, FishingCircleConstract.view,KeyBoardHelper.OnKeyBoardStatusChangeListener{
  private  String information_id;
    @BindView(R.id.webview)
    WebView webView;
    IWXAPI api;
    @BindView(R.id.layout)
    RelativeLayout layout;
    private InputMethodManager imm;
    private String res_id,parent_id="0";
    @BindView(R.id.pinglun)
    LinearLayout pinglun;//默认隐藏
    @BindView(R.id.edit_comment)
    EditText editComment;
    @BindView(R.id.noping)
    RelativeLayout noping;//
    private  int edcomment=0;
    private HomePresenter homePresenter;
    private FishingCirclePresenter friendPresenter;
    private String url="";
    @BindView(R.id.disuss)
     ImageView disuss;//
    @BindView(R.id.zan)
   ImageView appraise;//点赞按钮
    private  String appraise_num;//点赞数
    private  String comment_num;//评论数
    private String  is_appraise;//是否点赞
    private KeyBoardHelper boardHelper;
    @BindView(R.id.discussnember)
            TextView discussnember;
    @BindView(R.id.love_num)
            TextView love_num;
    private Dialog mWeiboDialog;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    pinglun.setVisibility(View.VISIBLE);
                    noping.setVisibility(View.GONE);
                    editComment.setFocusable(true);
                    editComment.setFocusableInTouchMode(true);
                    editComment.requestFocus();
                    imm.showSoftInput(editComment, 0);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
         setToolBarTitle("新闻政策");
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        homePresenter=new HomePresenter(this);
        friendPresenter = new FishingCirclePresenter(this);
        boardHelper = new KeyBoardHelper(this);
        boardHelper.onCreate();
        boardHelper.setOnKeyBoardStatusChangeListener(this);
        information_id=getIntent().getStringExtra("information_id");
        appraise_num=getIntent().getStringExtra("appraise");//点赞数
        comment_num=getIntent().getStringExtra("comment_num");//评论数
        is_appraise=getIntent().getStringExtra("is_appraise");
        detailnum(SharedPreferencedUtils.getString(NewsPolicyShowActivity.this,"token"),
                "information",information_id);
        Log.i("点赞和评论数",comment_num+"?"+appraise_num);
        discussnember.setText(comment_num+"  评论");
        love_num.setText(appraise_num+"  喜欢");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new NewsPolicyShowActivity.JsToJava(), "stub");
        url="http://201704yg.alltosun.net/information/m/detail?information_id="+information_id+"&fromApp=1&user_id="
                +SharedPreferencedUtils.getString(NewsPolicyShowActivity.this, "uid");
        webView.loadUrl(url);
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

        editComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEND
                        ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
                    edcomment=2;
                    Log.i("发送评论",getUrlParams(url).get("information_id")+"/"+parent_id+"/"+url);
                    initCommnetAdd(getUrlParams(url).get("information_id"),parent_id,
                            editComment.getText().toString());
                    return true;
                }

                return false;
            }
        });
    }

    @OnClick({R.id.disuss,R.id.zan})
    public   void  onClick(View view){
        switch (view.getId()){
            case R.id.disuss:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                pinglun.setVisibility(View.VISIBLE);
                noping.setVisibility(View.GONE);
                editComment.setFocusable(true);
                editComment.setFocusableInTouchMode(true);
                editComment.requestFocus();
                imm.showSoftInput(editComment, 0);
                break;
            case R.id.zan://
                add_appraise(SharedPreferencedUtils.getString(NewsPolicyShowActivity.this,"token"),getUrlParams(url).get("information_id"));
                break;
        }
    }

    @Override
    public void OnKeyBoardPop(int keyBoardheight) {

    }
    @Override
    public void OnKeyBoardClose(int oldKeyBoardheight) {
      pinglun.setVisibility(View.GONE);
        noping.setVisibility(View.VISIBLE);
    }
    private class JsToJava {
        @JavascriptInterface
        public void jsMethod(String p) {
            //Log.i("CDH", paramFromJS);
             Message msg = myHandler.obtainMessage();
             msg.what = 1;
             myHandler.sendMessage(msg);
             imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
             System.out.println("js返回结果" +p);//处理返回的结果
                parent_id=p;
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_policy_show;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void showErrMsg(String msg) {

    }
    @Override
    public void showInfo(String string) {
        WeiboDialogUtils.closeDialog(mWeiboDialog);
        Log.i("评论详情的展示",string);
        if (edcomment==2){
            edcomment=0;
            Log.e("gg评论",string);
            //pinglun.setVisibility(View.GONE);
           // noping.setVisibility(View.VISIBLE);
            try {
                JSONObject jsonObject=new JSONObject(string);
                if (jsonObject.getJSONObject("status").getInt("code")==1000){
                    editComment.setText("");
                    imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0); //强制隐藏键盘
                    pinglun.setVisibility(View.GONE);
                    noping.setVisibility(View.VISIBLE);
                    parent_id="";
                    String comment_id=jsonObject.getJSONObject("result").getString("comment_id");
                    webView.loadUrl("javascript:commentSuccess('"+comment_id+"')");
                } else {
                    Toast.makeText(this, jsonObject.getJSONObject("status").getInt("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (edcomment==3){
            try {
                JSONObject object=new JSONObject(string);
                JSONObject status=object.optJSONObject("status");
                JSONObject result=object.optJSONObject("result");
                if (status.optInt("code")==1000){
                    comment_num=Integer.valueOf(result.optString("comment_num"))+"";
                    discussnember.setText(comment_num+"  评论");
                    appraise_num=Integer.valueOf(result.optString("appraise_num"))+"";
                    love_num.setText(appraise_num+"  喜欢");
                    is_appraise=result.optString("is_appraise");
                    if (is_appraise.equals("1")){
                        int width=HelpUtils.dip2px(NewsPolicyShowActivity.this,20);
                        ViewGroup.LayoutParams params =appraise.getLayoutParams();
                        params.height=width;
                        params.width =width;
                        appraise.setLayoutParams(params);
                        appraise.setImageDrawable(getResources().getDrawable(R.mipmap.iszan));
                    }else {
                        //没有点赞
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showendcircle_img(String string) {

    }
    @Override
    public void showcityid(String ss) {
        //点赞
        Log.i("点赞",ss);
        WeiboDialogUtils.closeDialog(mWeiboDialog);
        try {
            JSONObject object=new JSONObject(ss);
            JSONObject status=object.optJSONObject("status");
            JSONObject result=object.optJSONObject("result");
            if (status.optInt("code")==1000){
                if (result.optInt("is_del")==0){
                    HelpUtils.success(NewsPolicyShowActivity.this,"点赞成功");
                    int width=HelpUtils.dip2px(NewsPolicyShowActivity.this,20);
                    ViewGroup.LayoutParams params =appraise.getLayoutParams();
                    params.height=width;
                    params.width =width;
                    appraise.setLayoutParams(params);
                    appraise.setImageDrawable(getResources().getDrawable(R.mipmap.iszan));
                     appraise_num=Integer.valueOf(appraise_num)+1+"";
                    love_num.setText(appraise_num+"  喜欢");
                }else {
                    int width=HelpUtils.dip2px(NewsPolicyShowActivity.this,20);
                    ViewGroup.LayoutParams params =appraise.getLayoutParams();
                    params.height=width;
                    params.width =width;
                    appraise.setLayoutParams(params);
                    appraise.setImageDrawable(getResources().getDrawable(R.mipmap.shape));
                    appraise_num=Integer.valueOf(appraise_num)-1+"";
                    love_num.setText(appraise_num+"  喜欢");
                    HelpUtils.success(NewsPolicyShowActivity.this,"取消点赞");
                }

            }else {
                HelpUtils.warning(NewsPolicyShowActivity.this,status.optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 取新闻政策详情
     **/
    private void initLoading(String information_id){
        Map<String, String> encryptapMap = Utils.getSignParams(NewsPolicyShowActivity.this,
                SharedPreferencedUtils.getString(NewsPolicyShowActivity.this,"token"));
        Map<String, String> map = Utils.getMap(NewsPolicyShowActivity.this,SharedPreferencedUtils.getString(NewsPolicyShowActivity.this,"token"));
        map.put("information_id", information_id);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        homePresenter.loadNewsList(map);
    }



    /**
     * 发评论
     */
    private void initCommnetAdd(String res_id, String parent_id, String content) {
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(NewsPolicyShowActivity.this, "加载中...");
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        encryptapMap.put("res_name", "information");
        encryptapMap.put("res_id", res_id);
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("parent_id", parent_id);
        map.put("content", content);
        map.put("to_user_id","0");
        map.put("res_name", "information");
        map.put("res_id", res_id);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadCommentAdd(map);
    }
    /**
     * 截取url
     */
    private HashMap<String,String> getUrlParams(String url){
        int index=url.indexOf("?");
        String argStr=url.substring(index+1);
        String[] argAry=argStr.split("&");
        HashMap<String ,String >  hashMap=new HashMap<>();
        for (String arg:argAry){
            String[] argAryT=arg.split("=");
            hashMap.put(argAryT[0],argAryT[1]);
        }
        return  hashMap;
    }

    /*
    * 点赞
    * */
    private  void  add_appraise(String token,
                                String res_id){
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(NewsPolicyShowActivity.this, "加载中...");

        Map<String,String> signmap=Utils.getSignParams(NewsPolicyShowActivity.this,token);
        signmap.put("res_name","information");
        signmap.put("res_id",res_id);
        Map<String,String> map=Utils.getMap(NewsPolicyShowActivity.this,token);
        map.put("res_name","information");
        map.put("res_id",res_id);
        map.put("sign",Utils.getMd5StringMap(signmap));
        homePresenter.add_appraise(map);
    }
    /*
    * 点赞数，评论数
    * */
    private void  detailnum(String token,String res_name,String res_id){
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(NewsPolicyShowActivity.this, "加载中...");
        edcomment=3;
        Map<String,String> signmap=Utils.getSignParams(NewsPolicyShowActivity.this,token);
        signmap.put("res_name",res_name);
        signmap.put("res_id",res_id);
        Map<String,String> map=Utils.getMap(NewsPolicyShowActivity.this,token);
        map.put("res_name",res_name);
        map.put("res_id",res_id);
        map.put("sign",Utils.getMd5StringMap(signmap));
        homePresenter.detailnum(map);
    }
}
