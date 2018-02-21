package com.example.fishingport.app.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.ScoreRecordList;
import com.example.fishingport.app.presenter.UserPresenter;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.animation.AnimationType;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wushixin on 2017/4/14.
 * 积分明细
 */

public class IntegralDetailsActivity extends BaseAppCompatActivity implements UserConstract.view{
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<ScoreRecordList.ResultBean.DataBean> mAdapter;
    private UserPresenter userPresenter;
    private List<ScoreRecordList.ResultBean.DataBean> list=new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_integraldetails;
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
        setToolBarTitle("积分明细");
        userPresenter=new UserPresenter(this);
        initScoreRecordList();
        mAdapter=new BaseRecyclerAdapter<ScoreRecordList.ResultBean.DataBean>(this, null, R.layout.item_integral_details) {
            @Override
            protected void convert(final BaseViewHolder helper, final ScoreRecordList.ResultBean.DataBean item) {
                //1 签到 2邮箱验证 3上传头像 4实名验证 5发评论 6发渔友圈
                String title="";
                if (item.getSource().equals("1")){
                    title="签到";
                } else if (item.getSource().equals("2")){
                    title="邮箱验证";
                }else if (item.getSource().equals("3")){
                    title="上传头像";
                }else if (item.getSource().equals("4")){
                    title="实名验证";
                }else if (item.getSource().equals("5")){
                    title="发评论";
                }else if (item.getSource().equals("6")){
                    title="发渔友圈";
                }else if (item.getSource().equals("7")){
                    title="轨迹";
                }
                helper.setText(R.id.txt_title,title);
                helper.setText(R.id.txt_time,item.getAdd_time());
                if (item.getScore_status().equals("2")) {
                    helper.setText(R.id.txt_count, "-"+item.getScore());
                } else {
                    helper.setText(R.id.txt_count, "+"+item.getScore());
                }


            }


         };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.openLoadAnimation(AnimationType.ALPHA);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(list);
    }

    @Override
    public void showInfo(String string) {
        Log.i("积分明细",string);
        Gson gson=new Gson();
        ScoreRecordList scoreRecordList=gson.fromJson(string,ScoreRecordList.class);
        list.addAll(scoreRecordList.getResult().getData());
        mAdapter.setData(list);

    }

    @Override
    public void showsign(String s) {

    }

    @Override
    public void showErrMsg(String msg) {

    }
    /**
     * 取积分明细
     */
    private void initScoreRecordList() {
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        //map.put("")
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        userPresenter.loadScoreRecordList(map);
    }
}
