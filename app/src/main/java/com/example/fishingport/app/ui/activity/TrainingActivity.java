package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.HomeConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.Traininglist;
import com.example.fishingport.app.presenter.HomePresenter;
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
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainingActivity extends BaseAppCompatActivity implements HomeConstract.view, SwipeRefreshLayout.OnRefreshListener {
  @BindView(R.id.mswiperefresh)
    SwipeRefreshLayout mswiperefresh;//刷新
    @BindView(R.id.noview)
    LinearLayout noview;//没有数据时显示
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;//列表
    private HomePresenter homePresenter;
    private int page=1;
    private  int pages, page_no;


    private List<Traininglist.ResultBean.ListBean> mList=new ArrayList<>();
    private BaseRecyclerAdapter<Traininglist.ResultBean.ListBean> beanBaseRecyclerAdapter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    page=1;
                    mList.clear();
                    getTrainlist(SharedPreferencedUtils.getString(TrainingActivity.this,"token"),page+"","10");
                    mswiperefresh.setRefreshing(false);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBarTitle("知识培训");
        mswiperefresh.setOnRefreshListener(this);
        homePresenter=new HomePresenter(this);
        initview();
    }

    private void initview() {
        getTrainlist(SharedPreferencedUtils.getString(TrainingActivity.this,"token"),page+"","10");
        showadapter();
    }

    private void showadapter() {
        beanBaseRecyclerAdapter=new BaseRecyclerAdapter<Traininglist.ResultBean.ListBean>
                (this,null, R.layout.item_newspolicy) {
            @Override
            protected void convert(BaseViewHolder helper, final Traininglist.ResultBean.ListBean item) {
                ImageView img_right_recommend= (ImageView) helper.getConvertView().findViewById(R.id.img_right_recommend);
                Glide.with(mContext).load(item.getCover()).into(img_right_recommend);
                helper.setText(R.id.txt_title_recommend,item.getTitle());
                helper.setText(R.id.txt_title_content, item.getIntro());//内容
                helper.setText(R.id.add_time,item.getAdd_time());//添加时间
                helper.setText(R.id.txt_comment_home,item.getComment_num());//评论数
                helper.setText(R.id.txt_collection,item.getAppraise_num());//点赞数
                helper.setText(R.id.txt_label,item.getSource());//来自哪里

                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(TrainingActivity.this,TrainingDetails.class);
                        intent.putExtra("path",item.getPath());
                        intent.putExtra("url",item.getUrl());
                        intent.putExtra("type",item.getType()+"");
                        Log.i("type打印",item.getType()+"");
                        intent.putExtra("appraise",item.getAppraise_num()+"");//点赞数
                        intent.putExtra("comment_num",item.getComment_num()+"");//评论数
                        intent.putExtra("is_appraise",item.getIs_appraise()+"");//是否点赞1已经点赞，0没有点赞
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        beanBaseRecyclerAdapter.openLoadAnimation(false);
        mRecyclerView.setAdapter(beanBaseRecyclerAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_training;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);

    }
    @Override
    public void showErrMsg(String msg) {

    }
    @Override
    public void showInfo(String string) {
        Log.i("培训列表",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            if (status.optInt("code")==1000){
                Gson gson=new Gson();
                Traininglist traininglist=gson.fromJson(string,Traininglist.class);
                mList.addAll(traininglist.getResult().getList());
                if (mList.size()>0){
                    noview.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    beanBaseRecyclerAdapter.setData(mList);
                }else {
                    noview.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }else {
                HelpUtils.warning(TrainingActivity.this,status.optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    @Override
    public void showcityid(String ss) {

    }

    private void getTrainlist(String token,String page,String per_oage){
        Map<String,String> singmap= Utils.getSignParams(TrainingActivity.this,token);
        Map<String,String> map=Utils.getMap(TrainingActivity.this,token);
        map.put("page",page);
        map.put("per_page",per_oage);
        map.put("sign",Utils.getMd5StringMap(singmap));
        homePresenter.getTrainList(map);
    }
}
