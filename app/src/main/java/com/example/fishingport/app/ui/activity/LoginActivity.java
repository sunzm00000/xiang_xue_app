package com.example.fishingport.app.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.fishingport.app.Dao.PersonalDaoManager;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.SOSConstract;
import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.bean.Personal;
import com.example.fishingport.app.bean.PersonalDao;
import com.example.fishingport.app.presenter.SOSPresenter;
import com.example.fishingport.app.presenter.UserPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.KeyBoardHelper;

import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.StatusBarCompat;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.tools.WeiboDialogUtils;
import com.example.fishingport.app.widget.CustomVideoView;
import com.huawei.android.pushagent.PushReceiver;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMHuaweiPushReceiver;
import com.hyphenate.util.NetUtils;
import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.password;

/**
 * Created by wushixin on 2017/4/13.
 * 登录
 */

public class LoginActivity extends AppCompatActivity implements UserConstract.view,SOSConstract.view {
    @BindView(R.id.txt_forget_pass)
    TextView txtForgetPass;
    @BindView(R.id.txt_register)
    TextView txtRegister;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.layout_bottom)
    View layoutBottom;
    @BindView(R.id.layout_content)
    View layoutContent;
    private UserPresenter presenter;
    private KeyBoardHelper boardHelper;
    private int bottomHeight;
    private String token;
    private String avatar;
    private String uid;
    private  String nickname;
    private  String huanxin_name;
    private String huanxin_passwd;
    @BindView(R.id.Linear_register)
    LinearLayout Linear_register;
    private Dialog mWeiboDialog;
    private CustomVideoView videoview;
    private SOSPresenter sosPresenter;
    private WifiManager mWifiManager;//wifi管理者，用于获取wifi连接时的信息

   private  int loadtype=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sosPresenter=new SOSPresenter(this);
        //wifi管理类
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(LoginActivity.this.WIFI_SERVICE);
        EMHuaweiPushReceiver.getPushState(LoginActivity.this);
        EMHuaweiPushReceiver.getToken(LoginActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarCompat.copat(this);
        }
        shoevideo();
        editName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtils.hasNetwork(LoginActivity.this)){
                String name = editName.getText() + "";
                String password = editPassword.getText() + "";
                if (name.equals("")) {
                    HelpUtils.warning(LoginActivity.this, "手机号不能为空!");
                }else if (password.equals("")) {
                    HelpUtils.warning(LoginActivity.this, "密码不能为空!");
                } else {
                    if (NetUtils.hasNetwork(LoginActivity.this)) {
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(LoginActivity.this, "加载中...");
                        loadtype=1;
                        Map<String, String> encryptapMap = Utils.getSignParams(LoginActivity.this, "");
                        encryptapMap.put("mobile", editName.getText().toString());
                        encryptapMap.put("password", editPassword.getText().toString());
                        Map<String, String> map = Utils.getMap(LoginActivity.this, "");
                        map.put("mobile", editName.getText().toString());
                        map.put("password", editPassword.getText().toString());
                        if (SharedPreferencedUtils.getString(LoginActivity.this,"usermac")!=null){
                            map.put("mac",SharedPreferencedUtils.getString(LoginActivity.this,"usermac"));
                        }else {
                            map.put("mac","");
                        }
                        map.put("sign", Utils.getMd5StringMap(encryptapMap));
                        presenter.loadLogin(map);
                    } else {
                        HelpUtils.warning(LoginActivity.this, "暂无网络!");
                    }
                }
            }else {
                    HelpUtils.warning(LoginActivity.this,"暂无网络");
                }
            }
        });
        presenter = new UserPresenter(this);
        boardHelper = new KeyBoardHelper(this);
        boardHelper.onCreate();
        boardHelper.setOnKeyBoardStatusChangeListener(onKeyBoardStatusChangeListener);
        layoutBottom.post(new Runnable() {
            @Override
            public void run() {
                bottomHeight = layoutBottom.getHeight();
            }
        });
    }

    private void shoevideo() {
        //加载视频资源控件
        videoview = (CustomVideoView) findViewById(R.id.videoview);
        //设置播放加载路径
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vidd));
        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });
    }

    private KeyBoardHelper.OnKeyBoardStatusChangeListener
            onKeyBoardStatusChangeListener = new KeyBoardHelper.OnKeyBoardStatusChangeListener() {
        @Override
        public void OnKeyBoardPop(int keyBoardheight) {
            final int height = keyBoardheight;
            if (bottomHeight > height) {
                layoutBottom.setVisibility(View.GONE);
            } else {
                int offset = bottomHeight - height;
                final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) layoutContent
                        .getLayoutParams();
                lp.topMargin = offset;
                layoutContent.setLayoutParams(lp);
            }
        }
        @Override
        public void OnKeyBoardClose(int oldKeyBoardheight) {
            if (View.VISIBLE != layoutBottom.getVisibility()) {
                layoutBottom.setVisibility(View.VISIBLE);
            }
            final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) layoutContent
                    .getLayoutParams();
            if (lp.topMargin != 0) {
                lp.topMargin = 0;
                layoutContent.setLayoutParams(lp);
            }
        }
    };
    @OnClick({R.id.txt_forget_pass, R.id.Linear_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_forget_pass://忘记密码
                startActivity(new Intent(LoginActivity.this, ForgetPassActivity.class));
                break;
            case R.id.Linear_register://注册页面
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }
    @Override
    public void showInfo(final String string) {
        Log.i("登录", string);
        if (loadtype==1){
            try {
                final JSONObject jsonObject = new JSONObject(string);
                if (jsonObject.getJSONObject("status").getString("code").equals("1000")) {
                    JSONObject result = jsonObject.optJSONObject("result");
                    token = result.optString("token");
                    avatar = jsonObject.getJSONObject("result").getString("avatar");
                    uid = jsonObject.getJSONObject("result").getString("uid");
                    nickname=jsonObject.getJSONObject("result").getString("nick_name");
                    huanxin_name=jsonObject.getJSONObject("result").getString("huanxin_name");
                    huanxin_passwd=jsonObject.getJSONObject("result").getString("huanxin_passwd");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(string);
                                JSONObject result = jsonObject.optJSONObject("result");
                                PersonalDao personalDao = PersonalDaoManager.getInstance().getNewSession().getPersonalDao();
                                List<Personal> list = personalDao.queryBuilder().where(PersonalDao.Properties.Uid.eq(uid)).list();
                                Log.i("list有没有", list.size() + "");
                                if (list.size() == 0) {
                                    Personal personal = new Personal();
                                    personal.setHead_img(avatar);
                                    personal.setHuanxin_id(jsonObject.getJSONObject("result").getString("huanxin_name"));
                                    personal.setUid(uid + "");
                                    personal.setId(null);
                                    personal.setNickname(jsonObject.getJSONObject("result").getString("nick_name"));
                                    personalDao.insert(personal);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    if (mWifiManager.getConnectionInfo().getSSID().toString().equals("\"APP-TEST\"")) {
                        //是港口wifi
                        sosPresenter.getwifimac();//获取wifimac
                    }else {
                        EMLogin(huanxin_name,huanxin_passwd);
                    }
                } else {
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    HelpUtils.warning(this, jsonObject.getJSONObject("status").getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }
        }else if (loadtype==2){
            Log.i("ac认证",string);
            try {
                JSONObject object=new JSONObject(string);
                JSONObject status=object.optJSONObject("status");
                JSONObject result=object.optJSONObject("result");
                if (status.optInt("code")==1000){
                     Log.i("登录页ac认证成功","登录页ac认证成功");
                    EMLogin(huanxin_name,huanxin_passwd);
                }
                else {
                    // Toast.makeText(ConnectedDeviceActivity.this,"")
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void showWifi(String string) {
        Log.i("wifimac",string);
        try {
            JSONObject object=new JSONObject(string);
            String userMac=object.optString("userMac");
            String deviceMac=object.optString("deviceMac");
            SharedPreferencedUtils.setString(LoginActivity.this,"usermac",userMac+"");
            String token="";
            if (SharedPreferencedUtils.getString(LoginActivity.this,"token")==null){
                token="";
            }else {
                token=SharedPreferencedUtils.getString(LoginActivity.this,"token");
            }
            permit_client(token,userMac,deviceMac);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showsign(String s) {

    }

    @Override
    public void showErrMsg(String msg) {

    }

    public  void  permit_client(String token,String userMac,String deviceMac)
    {
        loadtype=2;
        Log.i("ac端认证",token+"/"+userMac+"/"+deviceMac);
        Map<String,String> signmap= Utils.getSignParams(LoginActivity.this,token);
        Map<String,String> map=Utils.getMap(LoginActivity.this,token);
        map.put("user_mac",userMac);
        map.put("device_mac",deviceMac);
        map.put("sign",Utils.getMd5StringMap(signmap));
        sosPresenter.permit_client(map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boardHelper.onDestory();
    }
    //环信登录
    private void EMLogin(final String huanxin_name, final String huanxin_passwd) {
        Log.i("登录环信执行了", "登录环信执行 了" + huanxin_name + "/" + huanxin_passwd);
        EMClient.getInstance().login(huanxin_name, huanxin_passwd, new EMCallBack() {
            /**
             * 登陆成功的回调
             */
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        EMClient.getInstance().chatManager().loadAllConversations();
                        EMClient.getInstance().groupManager().loadAllGroups();
                        SharedPreferencedUtils.setString(LoginActivity.this, "token", token + "");
                        SharedPreferencedUtils.setString(LoginActivity.this, "avatar", avatar);
                        SharedPreferencedUtils.setString(LoginActivity.this, "uid", uid);
                        SharedPreferencedUtils.setString(LoginActivity.this,"nickname",nickname+"");
                        SharedPreferencedUtils.setString(LoginActivity.this, "huanxinname", huanxin_name);
                        SharedPreferencedUtils.setString(LoginActivity.this, "huanxinpasswd", huanxin_passwd);
                          Log.d("main登录成功", "登录聊天服务器成功"+"/"+nickname);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
            /**
             * 登陆错误的回调
             * @param i
             * @param s
             */
            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (i==200){
                            //说明用户已经登录需要先退出再登录
                            EMClient.getInstance().logout(true, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    EMLogin(huanxin_name,huanxin_passwd);//重新登录
                                }

                                @Override
                                public void onError(int i, String s) {
                                }
                                @Override
                                public void onProgress(int i, String s) {
                                }
                            });
                        }else {
                            //如果是其他错误就走这里
                            WeiboDialogUtils.closeDialog(mWeiboDialog);
                            SharedPreferencedUtils.setString(LoginActivity.this, "token", token + "");
                            SharedPreferencedUtils.setString(LoginActivity.this, "avatar", avatar);
                            SharedPreferencedUtils.setString(LoginActivity.this, "uid", uid);
                            SharedPreferencedUtils.setString(LoginActivity.this,"nickname",nickname+"");
                            SharedPreferencedUtils.setString(LoginActivity.this, "huanxinname", huanxin_name);
                            SharedPreferencedUtils.setString(LoginActivity.this, "huanxinpasswd", huanxin_passwd);
                            Log.d("main登录成功", "登录聊天服务器成功"+"/"+nickname);
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("iserror","1");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }


                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    //返回重启加载
    @Override
    protected void onRestart() {
        shoevideo();
        super.onRestart();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        videoview.stopPlayback();
        super.onStop();
    }

}
