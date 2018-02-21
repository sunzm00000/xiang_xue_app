package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.SOSConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.presenter.SOSPresenter;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/14.
 * 我的消息
 *
 */

public class MyNoticeActivity extends BaseAppCompatActivity implements SOSConstract.view{
    @BindView(R.id.comments)
    RelativeLayout comments;//跳转到评论页面
    @BindView(R.id.layout_thumb)
    RelativeLayout thumb;//点赞页面
    @BindView(R.id.System_message)
    RelativeLayout System_message;//系统消息
    String comment_num=0+"";//未读评论数
    String appraise_num=0+"";//未读点赞数
    @BindView(R.id.onedian)
    TextView onedian;
    @BindView(R.id.twodian)
    TextView twodian;
    @BindView(R.id.threedian)
    TextView threedian;
    private SOSPresenter sosPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_mynotice;
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
        sosPresenter=new SOSPresenter(this);
        setToolBarTitle("我的消息");
        comment_num=getIntent().getStringExtra("comment_num");
        appraise_num=getIntent().getStringExtra("appraise_num");
        if (comment_num.equals("0")){
            onedian.setVisibility(View.GONE);
        }else {
            onedian.setVisibility(View.VISIBLE);
        }
        if (appraise_num.equals("0")){
            twodian.setVisibility(View.GONE);
        }else {
            twodian.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getappcom(SharedPreferencedUtils.getString(MyNoticeActivity.this,"token"));
    }

    @OnClick({R.id.comments,R.id.layout_thumb,R.id.System_message})
    public  void  OnClick(View view){
        switch (view.getId()){
            case R.id.comments://评论
                startActivity(new Intent(MyNoticeActivity.this,CommentsAcitivity.class));
                break;
            case R.id.layout_thumb://点赞
                startActivity(new Intent(MyNoticeActivity.this,ThumbUpActivity.class));
                break;
            case R.id.System_message://系统消息
                startActivity(new Intent(MyNoticeActivity.this,System_messageActivity.class));
                break;
        }
    }

    @Override
    public void showErrMsg(String msg) {

    }

    @Override
    public void showInfo(String string) {

    }

    @Override
    public void showWifi(String string) {
        Log.i("点赞，评论",string);
        try {
            JSONObject jsonObject=new JSONObject(string);
            JSONObject status=jsonObject.optJSONObject("status");
            JSONObject result=jsonObject.optJSONObject("result");
            if (status.optInt("code")==1000){
                comment_num=result.optString("comment_num");
                appraise_num=result.optString("appraise_num");
                if (comment_num.equals("0")){
                    onedian.setVisibility(View.GONE);
                }else {
                    onedian.setVisibility(View.VISIBLE);
                    onedian.setText(comment_num);
                }
                if (appraise_num.equals("0")){
                    twodian.setVisibility(View.GONE);
                }else {
                    twodian.setText(appraise_num);
                    twodian.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    * 未读点赞和评论列表
  * */
    public  void getappcom(String token){
        Map<String,String> signmap= Utils.getSignParams(MyNoticeActivity.this,token);
        Map<String,String> map=Utils.getMap(MyNoticeActivity.this,token);
        map.put("sign",Utils.getMd5StringMap(signmap));
        sosPresenter.getappcom(map);
    }
}
