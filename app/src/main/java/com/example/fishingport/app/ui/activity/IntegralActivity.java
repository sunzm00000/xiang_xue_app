package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.isScorelist;
import com.example.fishingport.app.presenter.UserPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.IntegralDialog;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.animation.AnimationType;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/14.
 * 我的积分
 */

public class IntegralActivity extends BaseAppCompatActivity implements UserConstract.view{
    @BindView(R.id.txt_user_integral)
    TextView txtUserIntegral;
    @BindView(R.id.layout_back)
    RelativeLayout layoutBack;
    @BindView(R.id.layout_exchange)
    RelativeLayout layoutExchange;
    @BindView(R.id.layout_integral_details)
    RelativeLayout layoutIntegralDetails;
    private UserPresenter presenter;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.forrecord)
    RelativeLayout forrecored;//积分兑换的按钮
    private String ggscore="0";
    private BaseRecyclerAdapter<isScorelist.ResultBean.DataBean> baseRecyclerAdapter;
    private List<isScorelist.ResultBean.DataBean> scorelist=new ArrayList<>();
    private isScorelist iScorelist=new isScorelist();
    private TextView rightbtn;
    private IntegralDialog integralDialog;
    private  String type_name="";
    private  String score="0";
     @Override
    protected int getFragmentContentId() {
        return 0;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        presenter=new UserPresenter(this);
        setToolBarTitle("我的积分");
        ggscore=getIntent().getStringExtra("socre");
        txtUserIntegral.setText(ggscore);
      rightbtn=  setTextRight("积分规则");
       rightbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(IntegralActivity.this,ScoreRulesActivity.class));
           }
       });
        isScore(SharedPreferencedUtils.getString(IntegralActivity.this,"token"));
        adapershow();
        intitview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isScore(SharedPreferencedUtils.getString(IntegralActivity.this,"token"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_integral;
    }
    private void adapershow() {
        baseRecyclerAdapter=new BaseRecyclerAdapter<isScorelist.ResultBean.DataBean>
                (this,null,R.layout.item_scoreview) {
            @Override
            protected void convert(BaseViewHolder helper, final isScorelist.ResultBean.DataBean item) {
                helper.setText(R.id.type,item.getType_name());
                helper.setText(R.id.score,item.getScore());
                helper.setText(R.id.remark,item.getRemark());

                TextView prohress= (TextView) helper.getConvertView().findViewById(R.id.progress);
                prohress.setText(item.getProgress());
                if (item.getProgress().equals("领取奖励")){//
                    prohress.setBackgroundResource(R.drawable.bg_noisscore);//已完成
                    prohress.setTextColor(Color.parseColor("#22a6ef"));
                    prohress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //领取积分接口
                                score=item.getScore()+"";
                                type_name=item.getType_name();
                                get_score(SharedPreferencedUtils.getString(IntegralActivity.this,"token"),item.getType());
                        }
                    });
                }else if (item.getProgress().equals("去完成")){
                    prohress.setBackgroundResource(R.drawable.bg_issocreing);//已完成
                    prohress.setTextColor(Color.parseColor("#ffffff"));
                    prohress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (item.getType().equals("1")){
                                finish();
                            }else {
                                startActivity(new Intent(IntegralActivity.this, UserDataActivity.class));
                            }
                        }
                    });
                }else if (item.getProgress().equals("已完成")){
                    prohress.setBackgroundResource(R.drawable.bg_isscored);//已完成
                    prohress.setTextColor(Color.parseColor("#aebace"));
                }
            }
        };
    }

    private void intitview() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        baseRecyclerAdapter.openLoadAnimation(AnimationType.ALPHA);
        mRecyclerView.setAdapter(baseRecyclerAdapter);
    }

    @OnClick({R.id.layout_exchange, R.id.layout_integral_details,R.id.forrecord})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_exchange://积分兑换
                Toast.makeText(IntegralActivity.this,"敬请期待",Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(IntegralActivity.this,ExchangeActivity.class));
                break;
            case R.id.layout_integral_details:
                startActivity(new Intent(IntegralActivity.this,IntegralDetailsActivity.class));
                break;
            case R.id.forrecord:
                Toast.makeText(IntegralActivity.this,"敬请期待",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public void showErrMsg(String msg) {

    }
    @Override
    public void showInfo(String string) {
        Log.i("积分完成度",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            if (status.optInt("code")==1000){
                Gson gson=new Gson();
                iScorelist=gson.fromJson(string,isScorelist.class);
                scorelist=iScorelist.getResult().getData();
                baseRecyclerAdapter.setData(scorelist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showsign(String s) {
        //接受领取积分的接口
        try {
            JSONObject object=new JSONObject(s);
            JSONObject status=object.optJSONObject("status");
            if (status.optInt("code")==1000){
                IntegralDialog(type_name,score);
                isScore(SharedPreferencedUtils.getString(IntegralActivity.this,"token"));

            }else {
                HelpUtils.warning(IntegralActivity.this,""+status.optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
          /*
          * 积分完成度
          * */
    private void  isScore(String token){
        Map<String,String> singmap= Utils.getSignParams(IntegralActivity.this,token);
        Map<String,String> map=Utils.getMap(IntegralActivity.this,token);
        map.put("sign",Utils.getMd5StringMap(singmap));
        presenter.isScore(map);
    }

    /*
    * 领取积分
    * */
    private  void  get_score(String token,String type){
        Map<String,String> signmap=Utils.getSignParams(IntegralActivity.this,token);
        signmap.put("type",type);
        Map<String,String> map=Utils.getMap(IntegralActivity.this,token);
        map.put("type",type);
        map.put("sign",Utils.getMd5StringMap(signmap));
        presenter.get_score(map);
    }
    private void IntegralDialog(String days, String score) {
        integralDialog = new IntegralDialog(IntegralActivity.this);
        integralDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击我知道了的按钮
                integralDialog.dismiss();//取消弹框
            }
        });
        integralDialog.setviewtext("恭喜获得" + score + "个积分", days);
        integralDialog.show();
    }
}
