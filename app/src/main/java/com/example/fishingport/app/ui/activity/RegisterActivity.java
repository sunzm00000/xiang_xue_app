package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.presenter.UserPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.KeyBoardHelper;
import com.example.fishingport.app.tools.StatusBarCompat;
import com.example.fishingport.app.tools.TimeUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.widget.CustomVideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/13.
 * 注册
 */

public class RegisterActivity extends AppCompatActivity implements UserConstract.view{
    @BindView(R.id.edit_mobile)
    EditText editMobile;
    @BindView(R.id.edit_check)
    EditText editCheck;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.txt_check)
    TextView txtCheck;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.layout_bottom)
    View layoutBottom;
    @BindView(R.id.layout_content)
    View layoutContent;
    @BindView(R.id.tologin)
    LinearLayout tologin;
    private KeyBoardHelper boardHelper;
    private int bottomHeight;
    private int type = 1;
    private int typeLoad = 1; //接口请求 1 获取验证码 2验证验证码  3 注册--提交密码
    private UserPresenter userPresenter;
    String mobile="";//手机号
    private CustomVideoView videoview;
    private int issend=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarCompat.copat(this);
        }
        showviedo();//展示视频
        editMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        editCheck.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        userPresenter=new UserPresenter(this);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = 1;
                } else {
                    type = 0;
                }
            }
        });
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

    private void showviedo() {
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

    private KeyBoardHelper.OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener = new KeyBoardHelper.OnKeyBoardStatusChangeListener() {

        @Override
        public void OnKeyBoardPop(int keyBoardheight) {
            final int height = keyBoardheight;
            Log.i("height",height+"");
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

    @OnClick({R.id.txt_check, R.id.btn_register,R.id.tologin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_check:
                mobile=editMobile.getText()+"";
                if (HelpUtils.isMobile(mobile)) {
                    typeLoad=1;
                    initCheckSend();
                    new TimeUtils(txtCheck, "获取验证码");
                } else {
                    HelpUtils.warning(this,"手机号格式不对!");
                }
                break;
            case R.id.btn_register:
                mobile=editMobile.getText()+"";
            if (mobile.equals("")){
                 HelpUtils.warning(this,"手机号不能为空");
            }
            else if (editCheck.getText().toString().equals("")){
                   HelpUtils.warning(this,"验证码不能为空!");
               }else if (editPassword.getText().toString().equals("")){
                   HelpUtils.warning(this,"密码不能为空!");
               } else{
                    if (type==1){
                        if (HelpUtils.isNetworkAvailable(this)){
                            initCheckMobile();
                        }else {
                            HelpUtils.warning(this,"暂无网络!");
                        }
                    } else {

                    }
               }
                break;
            case  R.id.tologin://已有账号，直接登录
                finish();
                break;
        }
    }

    /**
     * 发送验证码
     * */
    private void initCheckSend(){
        Map<String, String> encryptapMap = Utils.getSignParams(this,"");
        encryptapMap.put("mobile",editMobile.getText().toString());
        Map<String, String> map = Utils.getMap(this,"");
        map.put("mobile",editMobile.getText().toString());
        map.put("type","1");
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        userPresenter.loadCheckSend(map);
    }
    /**
     * 验证手机与验证码  编辑
     * */
    private void initCheckMobile(){
        typeLoad=2;
        Map<String, String> encryptapMap = Utils.getSignParams(this,"");
        encryptapMap.put("mobile",editMobile.getText().toString());
        Map<String, String> map = Utils.getMap(this,"");
        map.put("mobile",editMobile.getText().toString());
        map.put("captcha",editCheck.getText().toString());
        map.put("type","1");
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        userPresenter.loadCheckMobile(map);
    }
    /**
     * 注册——提交密码
     * */
    private void initRegister(){
        typeLoad=3;
        Map<String, String> encryptapMap = Utils.getSignParams(this,"");
        encryptapMap.put("mobile",editMobile.getText().toString());
        encryptapMap.put("password",editPassword.getText().toString());
        Map<String, String> map = Utils.getMap(this,"");
        map.put("mobile",editMobile.getText().toString());
        map.put("captcha",editCheck.getText().toString());
        map.put("password",editPassword.getText().toString());
        map.put("type","1");
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        userPresenter.loadRegister(map);
    }

    @Override
    public void showInfo(String string) {
        Log.e("load",string+"/"+typeLoad);
        try {
            JSONObject jsonObject=new JSONObject(string);
            if (jsonObject.getJSONObject("status").getString("code").equals("1000")){
                if (typeLoad==1){
                    JSONObject result=jsonObject.getJSONObject("result");
                    editCheck.setText(result.getString("captcha"));

               } else if (typeLoad==2){
                 initRegister();
               } else {
                    JSONObject result=jsonObject.getJSONObject("result");
                    Intent intent=new Intent(this,RegisterUserDataActivity.class);
                    intent.putExtra("token",result.getString("token"));
                    startActivity(intent);
                    finish();
                }
            } else {
                HelpUtils.warning(this,jsonObject.getJSONObject("status").getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showsign(String s) {

    }

    @Override
    public void showErrMsg(String msg) {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();

    }

    //返回重启加载
    @Override
    protected void onRestart() {
        showviedo();//展示动态视频
        super.onRestart();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        videoview.stopPlayback();//停止加载
        super.onStop();
    }
}
