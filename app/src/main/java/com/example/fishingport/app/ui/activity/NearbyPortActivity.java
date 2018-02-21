package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.PortConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.PortList;
import com.example.fishingport.app.presenter.PortPresenter;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.animation.AnimationType;
import com.github.library.listener.RequestLoadMoreListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/19.
 * 附近港口
 */

public class NearbyPortActivity extends AppCompatActivity implements PortConstract.view,SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<PortList.ResultBean.DataBean> mAdapter;
    private List<PortList.ResultBean.DataBean> list;
    private PortPresenter portPresenter;
    private String city_id=1+"";
    private  PortList portLists;
    private String city="北京";
    @BindView(R.id.layout_back)
    RelativeLayout layout_back;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.txt_right)
    TextView txt_right;
    @BindView(R.id.noview)
    LinearLayout noview;//
    private int page = 1;
    private int page_no, pages;
    @BindView(R.id.mswiperefresh)
    SwipeRefreshLayout mswiperefresh;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    page=1;
                    list.clear();
                    city=SharedPreferencedUtils.getString(NearbyPortActivity.this,"city_addr");
                    if (city!=null){
                        getcity_id(SharedPreferencedUtils.getString(NearbyPortActivity.this,"token")
                                ,city);
                    }
                    mswiperefresh.setRefreshing(false);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(R.layout.activity_nearbyport);
        ButterKnife.bind(this);
        portPresenter=new PortPresenter(this);
        mswiperefresh.setOnRefreshListener(this);
        txt_title.setText("附近港口");
        Log.i("city==",SharedPreferencedUtils.getString(NearbyPortActivity.this,"city_addr")+"傻逼");
        city=SharedPreferencedUtils.getString(NearbyPortActivity.this,"city_addr");
        if (city!=null){
            getcity_id(SharedPreferencedUtils.getString(NearbyPortActivity.this,"token")
                    ,city);
        }
         list=new ArrayList<>();
        mAdapter=new BaseRecyclerAdapter<PortList.ResultBean.DataBean>(this, null, R.layout.item_nearbyport) {
            @Override
            protected void convert(BaseViewHolder helper, PortList.ResultBean.DataBean item) {
                helper.setText(R.id.portname,item.getTitle());
                helper.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent portmap=new Intent(NearbyPortActivity.this,PortMapActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("sds",portLists);
                        portmap.putExtras(bundle);
                        startActivity(portmap);
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.openLoadAnimation(AnimationType.ALPHA);
        mRecyclerView.setAdapter(mAdapter);
    }
     @OnClick({R.id.layout_back})
     public  void  onClick(View view){
         switch (view.getId()){
             case  R.id.layout_back:
                 if (getIntent().getStringExtra("type")!=null){
                     if (getIntent().getStringExtra("type").equals("1")) {
                         Intent intent=new Intent();
                         intent.setAction("showfragment");
                         intent.putExtra("type","4");
                         sendBroadcast(intent);
                         finish();
                     }
                 } else {
                     startActivity(new Intent(NearbyPortActivity.this,MapActivity.class));
                 }
                 break;
         }
     }
    @Override
    public void showErrMsg(String msg) {

    }
    @Override
    public void showInfo(String string) {
        Log.i("港口列表",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            JSONObject result=object.optJSONObject("result");
            if (status.optInt("code")==1000){
                Gson gson=new Gson();
                PortList portList=gson.fromJson(string,PortList.class);
                portLists=portList;
              list.addAll(portList.getResult().getData());
                Log.i("港口列表-",list.size()+"/");
                if (list.size()>0){
                    noview.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mAdapter.setData(list);

                }else {
                    noview.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showcityid(String s) {
        Log.i("城市id",s+"");
        try {
            JSONObject object=new JSONObject(s);
            JSONObject status=object.optJSONObject("status");
            if (status.optInt("code")==1000){
                city_id=object.optString("result");
                get_port_list(SharedPreferencedUtils.getString(NearbyPortActivity.this,"token"),city_id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*
    * 获取城市id
    * */
    public  void  getcity_id(String token,String city){
        Map<String,String> signmap= Utils.getSignParams(NearbyPortActivity.this,token);
        signmap.put("city",city);
        Map<String,String> map=Utils.getMap(NearbyPortActivity.this,token);
        map.put("city",city);
        map.put("sign",Utils.getMd5StringMap(signmap));
        portPresenter.get_city_id(map);
    }
    /*
    * 获取附近港口list
    *
    * */
    public  void get_port_list(String token,String city_id){
          Map<String,String> signmap=Utils.getSignParams(NearbyPortActivity.this,token);
          signmap.put("city_id",city_id);
          Map<String,String> map=Utils.getMap(NearbyPortActivity.this,token);
          map.put("city_id",city_id);
          map.put("sign",Utils.getMd5StringMap(signmap));
          portPresenter.get_port_list(map);
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra("type")!=null){
            if (getIntent().getStringExtra("type").equals("1")) {
                Intent intent=new Intent();
                intent.setAction("showfragment");
                intent.putExtra("type","4");
                sendBroadcast(intent);
                finish();
            }
        } else {
            startActivity(new Intent(NearbyPortActivity.this,MapActivity.class));
        }
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);

    }
}
