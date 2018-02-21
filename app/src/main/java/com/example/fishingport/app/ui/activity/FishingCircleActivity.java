package com.example.fishingport.app.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FishingCircleConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.presenter.FishingCirclePresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.tools.WechatShareManager;
import com.example.fishingport.app.tools.WeiboDialogUtils;
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

import static com.example.fishingport.app.R.id.item;

/**
 * Created by wushixin on 2017/5/9.
 * 渔友圈详情
 */
public class FishingCircleActivity extends BaseAppCompatActivity implements FishingCircleConstract.view {
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.edit_comment)
    EditText editComment;
    @BindView(R.id.viewpppp)
    LinearLayout viewpppp;
    private FishingCirclePresenter friendPresenter;
    private String res_id,to_user_id,parent_id="";
    private int loatype=0;
    private  int isfa=0;
    IWXAPI api;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
    protected int getLayoutId() {
        return R.layout.activity_fishingcircle;
    }
    private WechatShareManager mShareManager;
    @Override
    protected int getFragmentContentId() {
        return 0;
    }
    private InputMethodManager imm;
    private Dialog mWeiboDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
         imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mShareManager = WechatShareManager.getInstance(this);
        setToolBarTitle("渔友圈");
        Log.i("渔友圈===",getIntent().getStringExtra("url")+"/"+
                SharedPreferencedUtils.getString(FishingCircleActivity.this,"uid"));
       if (getUrlParams(getIntent().getStringExtra("url")).get("to_user_id").
                equals(SharedPreferencedUtils.getString(FishingCircleActivity.this,"uid"))){
            getImgRight().setImageDrawable(getResources().getDrawable(R.mipmap.fishingcircle_shape_icon));
            ViewGroup.LayoutParams params = getImgRight().getLayoutParams();
            params.height=50;
            params.width =70;
            getImgRight().setLayoutParams(params);
            getImgRight().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupWindow(v);
                }
            });
        }else {
            getImgRight().setVisibility(View.GONE);
        }
        friendPresenter = new FishingCirclePresenter(this);
        api = WXAPIFactory.createWXAPI(this, "wx0e5b8dcd46f493b8");
        api.registerApp("wx0e5b8dcd46f493b8");
        //启用支持javascript
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
         webview.setWebChromeClient(new WebChromeClient());
        // 方法，特别重要
        webview.addJavascriptInterface(new FishingCircleActivity.JsToJava(), "picture");
        webview.addJavascriptInterface(new onClickUser(), "onclickuser");
        // 方法，特别重要
        webview.addJavascriptInterface(new FishingCircleActivity.JsToJava(), "stub");
        webview.addJavascriptInterface(new FishingCircleActivity.JsToJava(), "trajectory");
        webview.loadUrl(getIntent().getStringExtra("url")+"&fromApp=1");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //加载完成
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //加载开始
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(FishingCircleActivity.this, "加载中...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                    }
                },10*1000);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //加载错误
            }
        });
        editComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId==EditorInfo.IME_ACTION_SEND
                        ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
                    loatype=1;
                    isfa=1;
                    if (isfa==1){
                        initCommnetAdd(getUrlParams(getIntent().getStringExtra("url")).get("fishing_circle_id"),parent_id,editComment.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void showInfo(String string) {
        Log.e("gg",string);
        if (loatype==1){
            //发评论
        try {
            JSONObject jsonObject=new JSONObject(string);
            if (jsonObject.getJSONObject("status").getInt("code")==1000){
                isfa=1;
                editComment.setText("");
                imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0); //强制隐藏键盘
                parent_id="";
                String comment_id=jsonObject.getJSONObject("result").getString("comment_id");
                webview.loadUrl("javascript:commentSuccess('"+comment_id+"')");
            } else {
                HelpUtils.warning(FishingCircleActivity.this,jsonObject.getJSONObject("status").getInt("message")+"");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        }else {
            //删除
            try {
                JSONObject object=new JSONObject(string);
                JSONObject status=object.optJSONObject("status");
                if (status.optInt("code")==1000){
                    HelpUtils.success(FishingCircleActivity.this,"删除成功");
                    webview.loadUrl("javascript:deleteSuccess("+getUrlParams(getIntent().getStringExtra("url")).get("fishing_circle_id")+")");
                    finish();
                }else {
                    HelpUtils.warning(FishingCircleActivity.this,""+status.optString("message"));
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
    public void showErrMsg(String msg) {

    }
    private class JsToJava {
        @JavascriptInterface
        public void jsMethod(String paramFromJS) {
            //Log.i("CDH", paramFromJS);
            Log.i("js返回结果",paramFromJS);
            Message msg = myHandler.obtainMessage();
            msg.what = 1;
            myHandler.sendMessage(msg);
            System.out.println("js返回结果" + paramFromJS);//处理返回的结果
            parent_id=paramFromJS;
        }
        @JavascriptInterface
        public void jsTrajectory(String paramFromJs){
            Log.i("js返回结果retrun_trajectory",paramFromJs);
            try {
                JSONObject object=new JSONObject(paramFromJs);
                Intent intent=new Intent(FishingCircleActivity.this,TrackMapActivity.class);
                intent.putExtra("id",object.optString("dataId")+"");//轨迹id
                intent.putExtra("add_time",object.optString("time")+"");//添加时间
                intent.putExtra("total_long",object.optString("distance"));//总距离
                intent.putExtra("saling",object.optString("average")+"");//航行时间
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @JavascriptInterface
        public void onClickPicture(String paramFromJS) {
            System.out.println("js返回结果" + paramFromJS);//处理返回的结果
            Log.e("js返回结果" , paramFromJS);
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
                    intent.setClass(FishingCircleActivity.this, PhotoBrowserActivity.class);
                    FishingCircleActivity.this.startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @JavascriptInterface
        public void onClickUser(String paramFromJS) {
            //这个是渔友圈h5页面头像点击事件监听
            Intent i=new Intent(FishingCircleActivity.this, FriendInfoActivity.class);
            i.putExtra("type",1);
            i.putExtra("uid",paramFromJS.split(",")[0]);
            i.putExtra("huanxin_name",paramFromJS.split(",")[1]);
            startActivity(i);
        }
    }

    /*
    * 删除渔友圈
    * */
    private  void  deletefish(String token,String fishing_circle_id){
        Map<String,String> signmap=Utils.getSignParams(FishingCircleActivity.this,token);
        signmap.put("fishing_circle_id",fishing_circle_id);
        Map<String,String> map=Utils.getMap(FishingCircleActivity.this,token);
        map.put("fishing_circle_id",fishing_circle_id);
        map.put("sign",Utils.getMd5StringMap(signmap));
        friendPresenter.deletefish(map);
    }
    /**
     * 发评论
     */
    private void initCommnetAdd(String res_id, String parent_id, String content) {
        isfa=0;
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        encryptapMap.put("res_name", "fishing_circle");
        encryptapMap.put("res_id", res_id);
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("parent_id", parent_id);
        map.put("content", content);
        map.put("res_name", "fishing_circle");
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
    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.layout_fishingcircle, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        contentView.findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        final TextView delete= (TextView) contentView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loatype=2;
                deletefish(SharedPreferencedUtils.getString(FishingCircleActivity.this,"token"),
                        getUrlParams(getIntent().getStringExtra("url")).get("fishing_circle_id")+"");
            }
        });
        contentView.findViewById(R.id.fishingcirle_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isWebchatAvaliable()) {
                    HelpUtils.warning(FishingCircleActivity.this,"请先安装微信");
                    return;
                }
                WXWebpageObject webpage = new WXWebpageObject();
                String url=getIntent().getStringExtra("url");
                Log.i("分享的url",url+"");
                webpage.webpageUrl =url;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title ="渔友圈";
                msg.description = "来自渔网通的分享";
                //  Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
                //    msg.thumbData = Util.bmpToByteArray(thumb, true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction =System.currentTimeMillis()+"";
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                api.sendReq(req);
          /*      WechatShareManager.ShareContentText mShareContentText = (WechatShareManager.ShareContentText) mShareManager.getShareContentText(getIntent().getStringExtra("url"));
                mShareManager.shareByWebchat(mShareContentText, WechatShareManager.WECHAT_SHARE_TYPE_TALK);*/
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.fishingcirle_weixin_pengyouquan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isWebchatAvaliable()) {
                    HelpUtils.warning(FishingCircleActivity.this,"请先安装微信");
                    return;
                }
                WXWebpageObject webpage = new WXWebpageObject();
                String url=getIntent().getStringExtra("url");
                Log.i("分享的url",url+"");
                webpage.webpageUrl =url;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title ="渔友圈";
                msg.description = "来自渔网通的分享";
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = System.currentTimeMillis()+"";
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                api.sendReq(req);
      /*          WechatShareManager.ShareContentText mShareContentText = (WechatShareManager.ShareContentText) mShareManager.getShareContentText(getIntent().getStringExtra("url"));
                mShareManager.shareByWebchat(mShareContentText, WechatShareManager.WECHAT_SHARE_TYPE_FRENDS);*/
                popupWindow.dismiss();
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.showAtLocation(viewpppp, Gravity.TOP,0,0);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
    }
    private boolean isWebchatAvaliable() {
        //检测手机上是否安装了微信
        try {
            getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private class onClickUser {
        @JavascriptInterface
        public void onClickUser(String paramFromJS) {
            Log.e("js返回结果" , paramFromJS);
            Intent i=new Intent(FishingCircleActivity.this, FriendInfoActivity.class);
            i.putExtra("type",1);
            i.putExtra("uid",paramFromJS.split(",")[0]);
            i.putExtra("huanxin_name",paramFromJS.split(",")[1]);
            FishingCircleActivity.this.startActivity(i);
        }
    }
}
