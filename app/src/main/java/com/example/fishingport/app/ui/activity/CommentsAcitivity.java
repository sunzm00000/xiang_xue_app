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
* 评论
* */
public class CommentsAcitivity extends BaseAppCompatActivity implements SOSConstract.view
        ,SwipeRefreshLayout.OnRefreshListener{
  private SOSPresenter sosPresenter;
    @BindView(R.id.Recynoreadcomments)
    RecyclerView Recynoreadcomments;//
    @BindView(R.id.noview)
    LinearLayout noview;//
    private BaseRecyclerAdapter<NoreadCommentslist.ResultBean.DataBean> beanBaseRecyclerAdapter;
    private List<NoreadCommentslist.ResultBean.DataBean> noreadlist=new ArrayList<>();
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
                    noreadlist.clear();
                    unread_list(SharedPreferencedUtils.getString(CommentsAcitivity.this,"token"),page+"","10");
                    mswiperefresh.setRefreshing(false);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        sosPresenter=new SOSPresenter(this);
        setToolBarTitle("评论");

        mswiperefresh.setOnRefreshListener(this);
        unread_list(SharedPreferencedUtils.getString(CommentsAcitivity.this,"token"),page+"","10");
        showAdapter();
    }

    private void showAdapter() {
        beanBaseRecyclerAdapter=new BaseRecyclerAdapter<NoreadCommentslist.ResultBean.DataBean>
                (this,null,R.layout.item_noreadcommnets_view) {
            @Override
            protected void convert(BaseViewHolder helper, final NoreadCommentslist.ResultBean.DataBean item) {
              helper.setText(R.id.user_name,item.getUser_name());
                helper.setText(R.id.add_time,item.getAdd_time_past());
                helper.setText(R.id.comment_content,item.getContent());
                TextView no_read= (TextView) helper.getConvertView().findViewById(R.id.no_read);
                if (item.getIs_read().equals("1")){
                    //已读
                    no_read.setVisibility(View.GONE);
                }else if (item.getIs_read().equals("0")){
                    //0是未读
                    no_read.setVisibility(View.VISIBLE);
                }
                final ImageView img_ping= (ImageView) helper.getConvertView().findViewById(R.id.img_ping);
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("资源类型",item.getRes_name()+item.getUser_id()+"/"+item.getRes_id()+"/"+item.getUser_id()+"/"+item.getTo_user_id());
                        if (item.getRes_name().equals("fishing_circle")){
                            //渔友圈评论
                            String url="http://201704yg.alltosun.net/fishing_circle/m/detail.html?fishing_circle_id=" +
                                    ""+item.getRes_id()+"&user_id="+item.getUser_id()+"&to_user_id="+item.getTo_user_id();
                            Intent intent = new Intent(CommentsAcitivity.this, FishingCircleActivity.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                        }else if (item.getRes_name().equals("information")){
                            //新闻政策评论
                            //跳转到详情页
                            Intent intent = new Intent(CommentsAcitivity.this, NewsPolicyShowActivity.class);
                            intent.putExtra("information_id",item.getRes_info().getInformation_info().get(0).getId()+"");
                            intent.putExtra("appraise",0+"");//点赞数
                            intent.putExtra("comment_num",0+"");//评论数
                            intent.putExtra("is_appraise",0+"");//是否点赞1已经点赞，0没有点赞
                            startActivity(intent);
                        }else if (item.getRes_name().equals("train")){
                            //知识培训
                            String url="http://201704yg.alltosun.net/train/m/detail?id="+item.getRes_info().getTrain_info().get(0).getId()+"";
                            Intent intent = new Intent(CommentsAcitivity.this, TrainingDetails.class);
                            intent.putExtra("id",item.getRes_info().getTrain_info().get(0).getId()+"");
                            intent.putExtra("appraise",0+"");//点赞数
                            intent.putExtra("comment_num",0+"");//评论数
                            intent.putExtra("url",url);
                            intent.putExtra("type",item.getRes_info().getTrain_info().get(0).getType());
                            intent.putExtra("is_appraise",0+"");//是否点赞1已经点赞，0没有点赞
                            intent.putExtra("path",item.getRes_info().getTrain_info().get(0).getPath());
                            startActivity(intent);
                        }else if (item.getRes_name().equals("market")){
                            //渔货行情
                            Intent intent=new Intent(CommentsAcitivity.this,QuotationDetails.class);
                            intent.putExtra("id",item.getRes_info().getMarket_info().get(0).getId()+"");
                            intent.putExtra("appraise",0+"");//点赞数
                            intent.putExtra("comment_num",0+"");//评论数

                            intent.putExtra("is_appraise",0+"");//是否点赞1已经点赞，0没有点赞
                            startActivity(intent);
                        }
                    }
                });
                ImageView imgUser= (ImageView) helper.getConvertView().findViewById(R.id.img_avatar);
                RelativeLayout trackview= (RelativeLayout) helper.getConvertView().findViewById(R.id.trackview);
                RelativeLayout informationview= (RelativeLayout) helper.getConvertView().findViewById(R.id.information);
                RecyclerView mRecyclerfishimg= (RecyclerView) helper.getConvertView().findViewById(R.id.mRecyclerfishimg);
                RequestManager glideRequest;
                glideRequest = Glide.with(CommentsAcitivity.this);
                glideRequest.load(item.getUser_avatar()).transform(new GlideCircleTransform(CommentsAcitivity.this)).into(imgUser);
                if (item.getRes_name().equals("fishing_circle")){
                   informationview.setVisibility(View.GONE);
                    //是渔友圈的评论
                    if (item.getRes_info().getImg_path().size()>0){
                        //是图片
                        List<String> imgs=new ArrayList<>();
                        imgs.addAll(item.getRes_info().getImg_thumb_path());
                        mRecyclerfishimg.setVisibility(View.VISIBLE);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentsAcitivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        mRecyclerfishimg.setLayoutManager(linearLayoutManager);
                        BaseRecyclerAdapter<String> imgbaseRecyclerfish=new BaseRecyclerAdapter<String>
                                (CommentsAcitivity.this,null,R.layout.item_noreadimgs_view) {
                            @Override
                            protected void convert(BaseViewHolder helper, String item) {
                                ImageView imageView= (ImageView) helper.getConvertView().findViewById(R.id.img);
                                Glide.with(CommentsAcitivity.this).load(item.toString()).into(imageView);
                            }
                        };
                        imgbaseRecyclerfish.openLoadAnimation(false);
                        mRecyclerfishimg.setAdapter(imgbaseRecyclerfish);
                        imgbaseRecyclerfish.setData(imgs);
                    }

                    if (item.getRes_info().getTrajectory_info().size()>0){
                        trackview.setVisibility(View.VISIBLE);
                        ImageView imageView= (ImageView) helper.getConvertView().findViewById(R.id.img);
                        helper.setText(R.id.time,item.getAdd_time() +"");//添加时间
                        helper.setText(R.id.saling,item.getRes_info().getTrajectory_info().get(0).getTime_count()+"");//航行时间
                        helper.setText(R.id.total_long,item.getRes_info().getTrajectory_info().get(0).getDistance_count()+"");//总距离
                        helper.setText(R.id.didian,item.getRes_info().getTrajectory_info().get(0).getStart_position());
                        helper.setText(R.id.qidian,item.getRes_info().getTrajectory_info().get(0).getStart_position());
                        helper.setText(R.id.zhongdian,item.getRes_info().getTrajectory_info().get(0).getStop_position());
                        Glide.with(CommentsAcitivity.this).load(item.getRes_info().getTrajectory_info().get(0).getImg()).error(R.mipmap.ic_launcher).into(imageView);
                    }
                }else if (item.getRes_name().equals("information")){
                    //是新闻政策的评论
                    mRecyclerfishimg.setVisibility(View.GONE);
                    trackview.setVisibility(View.GONE);
                    informationview.setVisibility(View.VISIBLE);
                    helper.setText(R.id.title,item.getRes_info().getInformation_info().get(0).getTitle());
                    ImageView img= (ImageView) helper.getConvertView().findViewById(R.id.caver);
                    Glide.with(CommentsAcitivity.this).
                            load(item.getRes_info().getInformation_info().get(0).getCover()).into(img);
                }else if (item.getRes_name().equals("market_info")){
                    mRecyclerfishimg.setVisibility(View.GONE);
                    trackview.setVisibility(View.GONE);
                    informationview.setVisibility(View.VISIBLE);
                    helper.setText(R.id.title,item.getRes_info().getInformation_info().get(0).getTitle());
                    ImageView img= (ImageView) helper.getConvertView().findViewById(R.id.caver);
                    Glide.with(CommentsAcitivity.this).load(item.getRes_info().getInformation_info().get(0).getCover()).into(img);
                }else if (item.getRes_name().equals("train_info")){
                    mRecyclerfishimg.setVisibility(View.GONE);
                    trackview.setVisibility(View.GONE);
                    informationview.setVisibility(View.VISIBLE);
                    helper.setText(R.id.title,item.getRes_info().getInformation_info().get(0).getTitle());
                    ImageView img= (ImageView) helper.getConvertView().findViewById(R.id.caver);
                    Glide.with(CommentsAcitivity.this).load(item.getRes_info().getInformation_info().get(0).getCover()).into(img);
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
                        if (pages>page_no){
                            unread_list(SharedPreferencedUtils.getString(CommentsAcitivity.this,"token"),page+"","10");
                        }else {
                            beanBaseRecyclerAdapter.notifyDataChangeAfterLoadMore(false);
                            if (pages > 1) {
                                beanBaseRecyclerAdapter.addNoMoreView();
                            }
                        }
                    }
                },2000);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comments_acitivity;
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
        Log.i("未读评论列表",string);
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

    private  void  unread_list(String token,String page,String per_page){
        Map<String,String> signmap= Utils.getSignParams(CommentsAcitivity.this,token);
        Map<String,String> map=Utils.getMap(CommentsAcitivity.this,token);
        map.put("page",page);
        map.put("per_page",per_page);
        map.put("sign",Utils.getMd5StringMap(signmap));
        sosPresenter.unread_list(map);
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);
    }
}
