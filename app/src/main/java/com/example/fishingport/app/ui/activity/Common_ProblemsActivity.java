package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.SOSConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.Questionlist;
import com.example.fishingport.app.presenter.SOSPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * 常见问题列表
 *
* */

public class Common_ProblemsActivity extends BaseAppCompatActivity implements View.OnClickListener,SOSConstract.view{

    @BindView(R.id.recyclerviewquestion)
    RecyclerView recyclerviewquestion;//常见问题列表
    private SOSPresenter sosPresenter;
    private List<Questionlist.ResultBean> qusetionlists=new ArrayList<>();
    BaseRecyclerAdapter<Questionlist.ResultBean> beanBaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarTitle("常见问题列表");
        ButterKnife.bind(this);
        sosPresenter=new SOSPresenter(this);
        getquestionlist(SharedPreferencedUtils.getString(Common_ProblemsActivity.this,"token"));
        showadapter();
    }

    private void showadapter() {
        beanBaseRecyclerAdapter=new BaseRecyclerAdapter<Questionlist.ResultBean>
                (this,null,R.layout.item_question_view) {
            @Override
            protected void convert(BaseViewHolder helper, final Questionlist.ResultBean item) {
            helper.setText(R.id.question_info,item.getQuestion());
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Common_ProblemsActivity.this,QuestionDetails.class);
                        intent.putExtra("id",item.getQuestion_id()+"");
                        startActivity(intent);
                    }
                });

            }
        };
        recyclerviewquestion.setLayoutManager(new LinearLayoutManager(this));
        beanBaseRecyclerAdapter.openLoadAnimation(false);
        recyclerviewquestion.setAdapter(beanBaseRecyclerAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common__problems;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void showErrMsg(String msg) {

    }

    @Override
    public void showInfo(String string) {
        Log.i("常见问题列表",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            JSONObject result=object.optJSONObject("result");
            if (status.optInt("code")==1000){
                Gson gson=new Gson();
                Questionlist questionlist=gson.fromJson(string,Questionlist.class);
                qusetionlists.addAll(questionlist.getResult());
                beanBaseRecyclerAdapter.setData(qusetionlists);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void showWifi(String string) {

    }

    private void getquestionlist(String token){
        Map<String,String> signmap= Utils.getSignParams(Common_ProblemsActivity.this,token);
        Map<String,String> map=Utils.getMap(Common_ProblemsActivity.this,token);
        map.put("sign",Utils.getMd5StringMap(signmap));
        sosPresenter.questionlist(map);
    }
}
