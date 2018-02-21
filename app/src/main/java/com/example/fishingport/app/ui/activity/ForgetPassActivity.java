package com.example.fishingport.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.presenter.UserPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.TimeUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.view.IconCenterEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/13.
 */

public class ForgetPassActivity extends BaseAppCompatActivity implements UserConstract.view {
    @BindView(R.id.edit_mobile)
    IconCenterEditText editMobile;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.txt_code)
    TextView txtCode;
    @BindView(R.id.btn)
    Button btn;
    private UserPresenter presenter;
    private int typeLoad;
    private int issend=0;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_pass;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new UserPresenter(this);
        setToolBarTitle("忘记密码");
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        editMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        editText2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
    }

    @Override
    public void showInfo(String string) {
        Log.i("忘记密码",string);
        try {
            JSONObject jsonObject=new JSONObject(string);
            if (jsonObject.getJSONObject("status").getString("code").equals("1000")){
                if (typeLoad==1){
                    JSONObject result=jsonObject.getJSONObject("result");
                    editText2.setText(result.getString("captcha"));

                    issend=1;
                } else if (typeLoad==2){
                    Intent intent=new Intent(ForgetPassActivity.this,PassWordActivity.class);
                    intent.putExtra("mobile", editMobile.getText().toString());
                    intent.putExtra("captcha", editText2.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }else {
                HelpUtils.warning(ForgetPassActivity.this,
                        ""+jsonObject.getJSONObject("status").getString("message"));
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

    }

    /**
     * 发送验证码
     */
    private void initCheckSend() {
        typeLoad = 1;
        Map<String, String> encryptapMap = Utils.getSignParams(this, "");
        encryptapMap.put("mobile", editMobile.getText().toString());
        Map<String, String> map = Utils.getMap(this, "");
        map.put("mobile", editMobile.getText().toString());
        map.put("type", "2");
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        presenter.loadCheckSend(map);
    }

    /**
     * 验证手机与验证码  编辑
     */
    private void initCheckMobile() {
        typeLoad = 2;
        Map<String, String> encryptapMap = Utils.getSignParams(this, "");
        encryptapMap.put("mobile", editMobile.getText().toString());
        Map<String, String> map = Utils.getMap(this, "");
        map.put("mobile", editMobile.getText().toString());
        map.put("captcha", editText2.getText().toString());
        map.put("type", "2");
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        presenter.loadCheckMobile(map);
    }

    @OnClick({R.id.txt_code, R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_code:
                if (editMobile.getText().equals("")){
                    HelpUtils.warning(ForgetPassActivity.this,"手机号不能为空");
                }else if (HelpUtils.isMobile(editMobile.getText().toString())) {
                    new TimeUtils(txtCode, "获取验证码");
                    initCheckSend();
                } else {
                    HelpUtils.warning(ForgetPassActivity.this,"手机号格式不对!");
                }
                break;
            case R.id.btn:
//                if (btnLogin.getProgress() == 0) {
//                    btnLogin.setProgress(50);
                    //说明已经发送了验证码并且已经成功
                if (editMobile.getText().equals("")){
                    HelpUtils.warning(ForgetPassActivity.this,"手机号不能为空");
                }else if (editText2.getText().equals("")){
                    HelpUtils.warning(ForgetPassActivity.this,"验证码不能为空");
                } else  if (HelpUtils.isMobile(editMobile.getText().toString())) {
                            initCheckMobile();
                    }else {
                        HelpUtils.warning(ForgetPassActivity.this,"手机号格式不对!");
                    }


               // }
                break;
        }
    }
}
