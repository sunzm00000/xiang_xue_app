package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.presenter.UserPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/5/9.
 * 设置密码
 *
 */

public class PassWordActivity extends BaseAppCompatActivity implements UserConstract.view {
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private UserPresenter presenter;

    @Override
    public void showInfo(String string) {

        try {
            JSONObject jsonObject=new JSONObject(string);
            if (jsonObject.getJSONObject("status").getString("code").equals("1000")){
                HelpUtils.success(PassWordActivity.this,"新密码设置成功!");
                finish();
            }else {
                HelpUtils.warning(PassWordActivity.this,jsonObject.getJSONObject("status").getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showsign(String s) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_password;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void showErrMsg(String msg) {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        setToolBarTitle("重置密码");
        editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        presenter=new UserPresenter(this);
    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        if (HelpUtils.isNetworkAvailable(PassWordActivity.this)){
              if (editPassword.getText().toString().equals("")){
                  HelpUtils.warning(PassWordActivity.this,"密码不能为空!");

              }else if (editPassword.getText().toString().length()<6){
                  HelpUtils.warning(PassWordActivity.this,"密码不能小于6位!");


              }else {
                  initPassWord();
              }
        }else {
            HelpUtils.warning(PassWordActivity.this,"暂无网络!");
        }

    }

    private void  initPassWord(){

        Map<String, String> encryptapMap = Utils.getSignParams(this, "");
        encryptapMap.put("mobile", getIntent().getStringExtra("mobile"));
        encryptapMap.put("password", editPassword.getText().toString());

        Map<String, String> map = Utils.getMap(this, "");
        map.put("mobile", getIntent().getStringExtra("mobile"));
        map.put("password", editPassword.getText().toString());
        map.put("captcha", getIntent().getStringExtra("captcha"));
        map.put("type", "2");
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        presenter.loadSetPassWord(map);
    }
}
