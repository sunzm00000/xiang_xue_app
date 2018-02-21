package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.SOSConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.NoreadCommentslist;
import com.example.fishingport.app.presenter.SOSPresenter;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.view.GlideCircleTransform;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.listener.RequestLoadMoreListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
* 点赞
* */
public class ThumbUpActivity extends BaseAppCompatActivity implements SOSConstract.view,SwipeRefreshLayout.OnRefreshListener{
   private SOSPresenter sosPresenter;
    private int page = 1;
    private int page_no, pages;
    @BindView(R.id.Recynoreadcomments)
    RecyclerView Recynoreadcomments;//
    @BindView(R.id.noview)
    LinearLayout noview;//
    @BindView(R.id.mswiperefresh)
    SwipeRefreshLayout mswiperefresh;
    private BaseRecyclerAdapter<NoreadCommentslist.ResultBean.DataBean> beanBaseRecyclerAdapter;
    private List<NoreadCommentslist.ResultBean.DataBean> noreadlist=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    page=1;
                    noreadlist.clear();
                    unappraise_list(SharedPreferencedUtils.getString(ThumbUpActivity.this,"token"),page+"");
                    mswiperefresh.setRefreshing(false);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        sosPresenter = new SOSPresenter(this);
        mswiperefresh.setOnRefreshListener(this);
        setToolBarTitle("点赞");
        unappraise_list(SharedPreferencedUtils.getString(ThumbUpActivity.this, "token"), page + "");
        showAdapter();
    }
    private void showAdapter() {
        beanBaseRecyclerAdapter = new BaseRecyclerAdapter<NoreadCommentslist.ResultBean.DataBean>
                (this, null, R.layout.item_noreadcommnets_view) {
            @Override
            protected void convert(BaseViewHolder helper, final NoreadCommentslist.ResultBean.DataBean item) {
                helper.setText(R.id.dianzan,"点赞了你");
                helper.setText(R.id.user_name, item.getUser_name());
                helper.setText(R.id.add_time, item.getAdd_time_past());
                TextView no_read= (TextView) helper.getConvertView().findViewById(R.id.no_read);
                if (item.getIs_read().equals("1")){
                    //已读
                    no_read.setVisibility(View.GONE);
                }else if (item.getIs_read().equals("0")){
                    //0是未读
                    no_read.setVisibility(View.VISIBLE);
                }
                TextView comment= (TextView) helper.getConvertView().findViewById(R.id.comment_content);
                LinearLayout layout_view= (LinearLayout) helper.getConvertView().findViewById(R.id.layout_view);
                layout_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "http://201704yg.alltosun.net/fishing_circle/m/detail.html?fishing_circle_id=" +
                                "" + item.getRes_id() + "&user_id=" + item.getUser_id() + "&to_user_id="+item.getTo_user_id();
                        Intent intent = new Intent(ThumbUpActivity.this, FishingCircleActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                });
                comment.setVisibility(View.GONE);
                final ImageView img_ping = (ImageView) helper.getConvertView().findViewById(R.id.img_ping);
                img_ping.setVisibility(View.GONE);
                ImageView imgUser = (ImageView) helper.getConvertView().findViewById(R.id.img_avatar);
                RelativeLayout trackview = (RelativeLayout) helper.getConvertView().findViewById(R.id.trackview);
                RelativeLayout informationview = (RelativeLayout) helper.getConvertView().findViewById(R.id.information);
                RecyclerView mRecyclerfishimg = (RecyclerView) helper.getConvertView().findViewById(R.id.mRecyclerfishimg);
                RequestManager glideRequest;
                glideRequest = Glide.with(ThumbUpActivity.this);
                glideRequest.load(item.getUser_avatar()).transform(new GlideCircleTransform(ThumbUpActivity.this)).into(imgUser);
                if (item.getRes_name().equals("fishing_circle")) {
                    informationview.setVisibility(View.GONE);
                    //是渔友圈的评论
                    if (item.getRes_info().getImg_path().size() > 0) {
                        //是图片
                        List<String> imgs=new ArrayList<>();
                        imgs.addAll(item.getRes_info().getImg_path());
                        mRecyclerfishimg.setVisibility(View.VISIBLE);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ThumbUpActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        mRecyclerfishimg.setLayoutManager(linearLayoutManager);
                        BaseRecyclerAdapter<String> imgbaseRecyclerfish=new BaseRecyclerAdapter<String>
                                (ThumbUpActivity.this,null,R.layout.item_noreadimgs_view) {
                            @Override
                            protected void convert(BaseViewHolder helper, String item) {
                                ImageView imageView= (ImageView) helper.getConvertView().findViewById(R.id.img);
                                Glide.with(ThumbUpActivity.this).load(item.toString()).into(imageView);
                            }
                        };
                        imgbaseRecyclerfish.openLoadAnimation(false);
                        mRecyclerfishimg.setAdapter(imgbaseRecyclerfish);

                    }
                    if (item.getRes_info().getTrajectory_info().size() > 0) {
                        trackview.setVisibility(View.VISIBLE);
                        ImageView imageView = (ImageView) helper.getConvertView().findViewById(R.id.img);
                        helper.setText(R.id.time, item.getAdd_time() + "");//添加时间
                        helper.setText(R.id.saling, item.getRes_info().getTrajectory_info().get(0).getTime_count() + "");//航行时间
                        helper.setText(R.id.total_long, item.getRes_info().getTrajectory_info().get(0).getDistance_count() + "");//总距离
                        helper.setText(R.id.didian, item.getRes_info().getTrajectory_info().get(0).getStart_position());
                        helper.setText(R.id.qidian, item.getRes_info().getTrajectory_info().get(0).getStart_position());
                        helper.setText(R.id.zhongdian, item.getRes_info().getTrajectory_info().get(0).getStop_position());
                        Glide.with(ThumbUpActivity.this).load(item.getRes_info().getTrajectory_info().get(0).getImg()).error(R.mipmap.ic_launcher).into(imageView);
                    }
                } else if (item.getRes_name().equals("information")) {
                    //是新闻政策的评论
                    mRecyclerfishimg.setVisibility(View.GONE);
                    trackview.setVisibility(View.GONE);
                    informationview.setVisibility(View.VISIBLE);
                    helper.setText(R.id.title, item.getRes_info().getInformation_info().get(0).getTitle());
                    ImageView img = (ImageView) helper.getConvertView().findViewById(R.id.caver);
                    Glide.with(ThumbUpActivity.this).load(item.getRes_info().getInformation_info().get(0).getCover()).into(img);
                }
            }
        };
        Recynoreadcomments.setLayoutManager(new LinearLayoutManager(this));
        beanBaseRecyclerAdapter.openLoadAnimation(false);
        Recynoreadcomments.setAdapter(beanBaseRecyclerAdapter);
        beanBaseRecyclerAdapter.openLoadingMore(true);
        beanBaseRecyclerAdapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pages > page_no) {
                            noreadlist.clear();
                            unappraise_list(SharedPreferencedUtils.getString(ThumbUpActivity.this,"token"),page+"");
                        } else {
                            beanBaseRecyclerAdapter.notifyDataChangeAfterLoadMore(false);
                            if (pages > 1) {
                                beanBaseRecyclerAdapter.addNoMoreView();
                            }
                        }
                    }
                }, 2000);
            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_thumb_up;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void showErrMsg(String msg) {

    }

    @Override
    public void showInfo(String string) {
        Log.i("未读点赞消息",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            if (status.optInt("code")==1000){
                Gson gson=new Gson();
                NoreadCommentslist noreadCommentslist=gson.fromJson(string,NoreadCommentslist.class);
                if (page==1){
                    noreadlist.clear();
                    noreadlist.addAll(noreadCommentslist.getResult().getData());
                }else {
                    noreadlist.addAll(noreadCommentslist.getResult().getData());
                }
                if (noreadlist.size()>0){
                    Recynoreadcomments.setVisibility(View.VISIBLE);
                    noview.setVisibility(View.GONE);
                    beanBaseRecyclerAdapter.setData(noreadlist);
                    beanBaseRecyclerAdapter.notifyDataChangeAfterLoadMore(true);
                    pages = noreadCommentslist.getResult().getPage().getPages();
                    page_no =Integer.valueOf(noreadCommentslist.getResult().getPage().getPage_no());
                    if (page_no == pages) {
                        beanBaseRecyclerAdapter.notifyDataChangeAfterLoadMore(false);
                        if (pages > 1) {
                            beanBaseRecyclerAdapter.addNoMoreView();
                        }
                    }
                    page++;
                }else {
                    noview.setVisibility(View.VISIBLE);
                    Recynoreadcomments.setVisibility(View.GONE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showWifi(String string) {

    }

    private void  unappraise_list(String token,String page){
        Map<String,String> signmap= Utils.getSignParams(ThumbUpActivity.this,token);
        Map<String,String> map=Utils.getMap(ThumbUpActivity.this,token);
        map.put("page",page);
        map.put("sign",Utils.getMd5StringMap(signmap));
        sosPresenter.unappraise_list(map);
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);
    }
}
