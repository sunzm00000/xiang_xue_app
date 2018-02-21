package com.example.fishingport.app.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.PortConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.presenter.PortPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wushixin on 2017/4/14.
 * 意见反馈
 */

public class FeedbackActivity extends BaseAppCompatActivity implements PortConstract.view{
    private PortPresenter portPresenter;
    @BindView(R.id.text)
    TextView text;//意见内容
    @BindView(R.id.iphone)
    TextView iphone;//手机号
    @BindView(R.id.btn)
    Button btn;//提交按钮
    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }
    @Override
    protected int getFragmentContentId() {
        return 0;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        portPresenter=new PortPresenter(this);
        setToolBarTitle("意见反馈");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=text.getText()+"";
                String mobile=iphone.getText()+"";
                Log.i("验证方法",HelpUtils.isMobile(mobile)+"/"+isemail(mobile)+"");
                if (content.equals("")){
                    HelpUtils.warning(FeedbackActivity.this,"问题描述不能为空");
                    return;
                }
                else  if (mobile.equals("")){
                    HelpUtils.warning(FeedbackActivity.this,"联系方式不能为空");
                    return;
                }else if (isemail(mobile)==false){
                    HelpUtils.warning(FeedbackActivity.this,"邮箱或者手机号不合法");
                    return;
                }
                else {
                    suggestion(SharedPreferencedUtils.getString(FeedbackActivity.this,"token"),text.getText()+"",iphone.getText()+"");
                }
            }
        });
    }

    @Override
    public void showErrMsg(String msg) {

    }
    private boolean isemail(String str){
        Pattern pattern = Pattern.compile("(^1[3|4|5|7|8][0-9]{9}$)|(^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$)",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    @Override
    public void showInfo(String string) {
        Log.i("意见反馈",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            JSONObject result= object.optJSONObject("result");
            if (status.optInt("code")==1000){
                HelpUtils.success(FeedbackActivity.this,"提交成功");
                text.setText("");
                iphone.setText("");
            }else {
                Toast.makeText(FeedbackActivity.this,""+status.optString("message"), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showcityid(String s) {

    }
    public  void suggestion(String token,String content,String mobile){
        Map<String,String> signmap= Utils.getSignParams(FeedbackActivity.this,token);
        Map<String,String> map=Utils.getMap(FeedbackActivity.this,token);
        map.put("content",content);
        map.put("mobile",mobile);
        map.put("sign",Utils.getMd5StringMap(signmap));
        portPresenter.suggestion(map);
    }
}
