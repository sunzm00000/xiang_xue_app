package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.TrackConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.TrajectoryList;
import com.example.fishingport.app.presenter.TrackPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.StatusBarCompat;
import com.example.fishingport.app.tools.Utils;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.listener.RequestLoadMoreListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/*
* 轨迹列表
* */
public class TarckActivity extends AppCompatActivity
        implements TrackConstract.view,SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{
    private Toolbar mToolbar;
    @BindView(R.id.tarcklist)
    RecyclerView tarcklist;//轨迹列表
    @BindView(R.id.layout_back)
    RelativeLayout layout_back;
    TextView txt_title;
    @BindView(R.id.txt_right)
    TextView txt_right;
    @BindView(R.id.noportview)
    LinearLayout noportview;
    @BindView(R.id.send_port)
    TextView send_port;//如果轨迹为空时，发一条轨迹
    @BindView(R.id.layout)
    CoordinatorLayout layout;
    private List<Bitmap> bitlist=new ArrayList<>();
    private BaseRecyclerAdapter<TrajectoryList.ResultBean.DataBean> baseRecyclerAdapter=null;
    private TrackPresenter trackPresenter;
    private List<TrajectoryList.ResultBean.DataBean> dataBeanList=new ArrayList<>();
    private List<TrajectoryList.ResultBean.DataBean> addlist=new ArrayList<>();
    private  String type="";//判断是从哪个页面跳过来的
    private int page = 1;
    private int page_no, pages;
    private  int loadtype=0;
    @BindView(R.id.mswiperefresh)
    SwipeRefreshLayout mswiperefresh;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                  page=1;
                    dataBeanList.clear();
                    get_trajectory_list(SharedPreferencedUtils.getString(TarckActivity.this,"token"),page,10);
                    mswiperefresh.setRefreshing(false);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarck);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarCompat.compat(this);
        }
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);//竖屏
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        txt_title= (TextView) findViewById(R.id.txt_title);//标题
        if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar);
        }
        txt_title.setText("我的轨迹");
        txt_right.setVisibility(View.VISIBLE);
        txt_right.setText("添加");
        if (getIntent().getStringExtra("type")!=null){
            type=getIntent().getStringExtra("type");
            if (type.equals("upload")){
                txt_right.setText("取消");
            }
        }
        trackPresenter=new TrackPresenter(this);
        mswiperefresh.setOnRefreshListener(this);
        txt_right.setOnClickListener(this);
        layout_back.setOnClickListener(this);
        tarcklist.setLayoutManager(new LinearLayoutManager(this));
        get_trajectory_list(SharedPreferencedUtils.getString(TarckActivity.this,"token"),page,10);
        LoadBasdRecyclerAdapter();
        baseRecyclerAdapter.openLoadAnimation(false);
        tarcklist.setAdapter(baseRecyclerAdapter);
    }
    private void showPopupWindow(View view, final String trajectory_id) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(TarckActivity.this).inflate(
                R.layout.pop_deleteview, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        contentView.findViewById(R.id.pop_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadtype=1;

                popupWindow.dismiss();
                delete_trajectory(SharedPreferencedUtils.getString(TarckActivity.this,"token"),trajectory_id);
            }
        });
        contentView.findViewById(R.id.pop_quxiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

         popupWindow.showAtLocation(layout, Gravity.TOP,0,0);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

    }
    private void LoadBasdRecyclerAdapter() {
        baseRecyclerAdapter = new BaseRecyclerAdapter<TrajectoryList.ResultBean.DataBean>
                (TarckActivity.this,null, R.layout.item_trackview) {
            @Override
            protected void convert(final BaseViewHolder helper, final TrajectoryList.ResultBean.DataBean item) {
                Log.i("item==",item.getAdd_time()+"/"+item.getImg());
                helper.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showPopupWindow(v,item.getId()+"");
                        return true;
                    }
                });

                helper.setOnClickListener(R.id.track_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type.equals("upload")){
                            addlist.add(dataBeanList.get(helper.getPosition()));
                             Intent intent=new Intent();
                             intent.putExtra("list",(Serializable)addlist);
                             setResult(1,intent);
                             finish();
                        }else {
                            Intent intent=new Intent(TarckActivity.this,TrackMapActivity.class);
                            intent.putExtra("id",item.getId()+"");//轨迹id
                            intent.putExtra("add_time",item.getAdd_time()+"");//添加时间
                            intent.putExtra("total_long",item.getDistance_count());//总距离
                            intent.putExtra("saling",item.getTime_count()+"");//航行时间
                            startActivity(intent);
                        }
                    }
                });
                ImageView imageView= (ImageView) helper.getConvertView().findViewById(R.id.img);
                helper.setText(R.id.time,item.getAdd_time() +"");//添加时间
                helper.setText(R.id.saling,item.getTime_count()+"");//航行时间
                helper.setText(R.id.total_long,item.getDistance_count()+"");//总距离
                helper.setText(R.id.didian,item.getStart_position());
                helper.setText(R.id.qidian,item.getStart_position());
                helper.setText(R.id.zhongdian,item.getStop_position());
               Glide.with(TarckActivity.this).load(item.getImg()).error(R.mipmap.ic_launcher).into(imageView);
            }
        };

        baseRecyclerAdapter.openLoadingMore(true);
        baseRecyclerAdapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pages>page_no){
                            get_trajectory_list(SharedPreferencedUtils.getString(TarckActivity.this,"token"),page,10);
                           // baseRecyclerAdapter.notifyDataChangeAfterLoadMore(true);
                        }else {
                            baseRecyclerAdapter.notifyDataChangeAfterLoadMore(false);
                            if (pages > 1) {
                                baseRecyclerAdapter.addNoMoreView();
                            }
                        }
                    }
                },2000);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showErrMsg(String msg) {

    }
    @Override
    public void showInfo(String string) {
        Log.i("轨迹列表",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            if (status.optInt("code")==1000){
                Gson gson=new Gson();
                TrajectoryList trajectoryList=gson.fromJson(string,TrajectoryList.class);
                if (page==1){
                    dataBeanList.clear();
                    dataBeanList.addAll(trajectoryList.getResult().getData());
                }else {
                    dataBeanList.addAll(trajectoryList.getResult().getData());
                }
                 if (dataBeanList.size()>0){
                     noportview.setVisibility(View.GONE);
                     tarcklist.setVisibility(View.VISIBLE);
                     Log.i("上拉加载4",dataBeanList.size()+"/"+trajectoryList.getResult().getCount()+"/");
                     pages = trajectoryList.getResult().getPage().getPages();
                     page_no =Integer.valueOf(trajectoryList.getResult().getPage().getPage_no());
                     baseRecyclerAdapter.setData(dataBeanList);
                     baseRecyclerAdapter.notifyDataChangeAfterLoadMore(true);
                     if (page_no == pages) {
                         baseRecyclerAdapter.notifyDataChangeAfterLoadMore(false);
                         if (pages > 1) {
                             baseRecyclerAdapter.addNoMoreView();
                         }
                     }
                     page++;
                 }else {
                      noportview.setVisibility(View.VISIBLE);
                      tarcklist.setVisibility(View.GONE);
                     send_port.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             startActivity(new Intent(TarckActivity.this,MapActivity.class));
                         }
                     });
                 }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void autoInfo(String string) {
        Log.i("删除轨迹",string);
        try {
            JSONObject jsonObject=new JSONObject(string);
             JSONObject status=jsonObject.optJSONObject("status");
            if (status.optInt("code")==1000){
                HelpUtils.success(TarckActivity.this,"删除成功");
                page=1;
                dataBeanList.clear();
                get_trajectory_list(SharedPreferencedUtils.getString(TarckActivity.this,"token"),page,10);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void startInfo(String string) {

    }

    /*
    * 请求轨迹列表接口
    * */
    public  void  get_trajectory_list(String token,int page_no,int per_age){
        loadtype=2;
        Map<String,String> signmap= Utils.getSignParams(TarckActivity.this,token);
        Map<String,String> map=Utils.getMap(TarckActivity.this,token);
        map.put("token",token);
        map.put("page_no",page_no+"");
        map.put("per_page",per_age+"");
        map.put("sign",Utils.getMd5StringMap(signmap));
        trackPresenter.get_trjectory_list(map);
    }

    /*
    * 删除轨迹
    * */

    private void delete_trajectory(String token,String trajectory_id){
        Map<String,String> signmap=Utils.getSignParams(TarckActivity.this,token);
        signmap.put("trajectory_id",trajectory_id);
        Map<String,String> map=Utils.getMap(TarckActivity.this,token);
        map.put("trajectory_id",trajectory_id);
        map.put("sign",Utils.getMd5StringMap(signmap));
        trackPresenter.delete_trajectory(map);
    }


    @OnClick({R.id.layout_back,R.id.txt_right})
    public  void onClick(View view){
        switch (view.getId()){
            case R.id.layout_back://这个是返回的按钮做的判断
                if (getIntent().getStringExtra("type")!=null){
                    if (getIntent().getStringExtra("type").equals("1")) {
                        Intent intent=new Intent();
                        intent.setAction("showfragment");
                        intent.putExtra("type","4");
                        sendBroadcast(intent);
                        finish();
                    } else if (getIntent().getStringExtra("type").equals("upload")){
                        //这个判断就是左上角的返回按钮
                        Intent intent=new Intent();
                        intent.putExtra("list",(Serializable)addlist);
                        setResult(1,intent);
                        finish();
                    }
                }
                    else {
                    startActivity(new Intent(TarckActivity.this,MapActivity.class));
                }
                break;
            case R.id.txt_right:
                if (getIntent().getStringExtra("type")!=null){
                    if (getIntent().getStringExtra("type").equals("1")) {
                        finish();
                        startActivity(new Intent(TarckActivity.this,MapActivity.class));
                    } else if (getIntent().getStringExtra("type").equals("upload")){
                        //这个是右上角的取消按钮
                        Intent intent=new Intent();
                        intent.putExtra("list",(Serializable)addlist);
                        setResult(1,intent);
                        finish();
                    }
                }else {
                    startActivity(new Intent(TarckActivity.this,MapActivity.class));
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra("type")!=null){
            if (getIntent().getStringExtra("type").equals("1")) {
                finish();
                Intent intent=new Intent();
                intent.setAction("showfragment");
                intent.putExtra("type","4");
                sendBroadcast(intent);
            }else if (getIntent().getStringExtra("type").equals("upload")){
                Intent intent=new Intent();
                intent.putExtra("list",(Serializable)addlist);
                setResult(1,intent);
                finish();
            }
        }else {
            startActivity(new Intent(TarckActivity.this,MapActivity.class));
        }
    }


}
