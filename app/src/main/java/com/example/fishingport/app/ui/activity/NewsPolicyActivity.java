package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.HomeConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.NewsPolicyBean;
import com.example.fishingport.app.presenter.HomePresenter;
import com.example.fishingport.app.tools.HelpUtils;
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

/**
 * Created by wushixin on 2017/4/18.
 * 新闻政策
 */

public class NewsPolicyActivity extends BaseAppCompatActivity implements HomeConstract.view,SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.noview)
    LinearLayout noview;//没有结果页
    @BindView(R.id.nonet)
    LinearLayout nonetview;//没有网络的页面
    @BindView(R.id.nonet_refresh)
    TextView nonet_refresh;//没有网络的时候点击这个按钮刷新页面
    private BaseRecyclerAdapter<NewsPolicyBean.ResultBean.DataBean> mAdapter;
    private HomePresenter homePresenter;
    private ImageView img_icon;
    RelativeLayout itemone;
    private TextView txt_title,txt_collection,txt_comment_home,txt_come,txt_info;
    private List<NewsPolicyBean.ResultBean.DataBean> list=new ArrayList<>();
    private int page=1;
    private  int pages, page_no;
    @BindView(R.id.mswiperefresh)
    SwipeRefreshLayout mswiperefresh;
    View view;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    page=1;
                    list.clear();
                    initLoading(page+"","");
                    mswiperefresh.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_policy;
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
        homePresenter=new HomePresenter(this);
        mswiperefresh.setOnRefreshListener(this);
           showadaper();//初始化适配器
        if (HelpUtils.isNetworkAvailable(NewsPolicyActivity.this)){
            nonetview.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            yesnet();//有网络

        }else {
            nonetview.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            nonet_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (HelpUtils.isNetworkAvailable(NewsPolicyActivity.this)){
                        nonetview.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        initLoading(page+"","");
                    }else {
                        Toast.makeText(NewsPolicyActivity.this,"请稍后重试",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void showadaper() {
        mAdapter=new BaseRecyclerAdapter<NewsPolicyBean.ResultBean.DataBean>(this, null, R.layout.item_newspolicy) {
            @Override
            protected void convert(final BaseViewHolder helper, final NewsPolicyBean.ResultBean.DataBean item) {
                ImageView img_right_recommend= (ImageView) helper.getConvertView().findViewById(R.id.img_right_recommend);
                helper.setText(R.id.txt_collection,item.getAppraise()+"");
                helper.setText(R.id.txt_comment_home,item.getComment_num()+"");
                helper.setText(R.id.txt_title_recommend,item.getTitle());
                helper.setText(R.id.txt_title_content, item.getIntro());
                helper.setText(R.id.txt_label, Utils.friendly_time(item.getAdd_time()));
                Glide.with(mContext).load(item.getCover()).into(img_right_recommend);
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到详情页
                        Intent intent = new Intent(NewsPolicyActivity.this, NewsPolicyShowActivity.class);
                        intent.putExtra("information_id",item.getRes_id()+"");
                        intent.putExtra("appraise",item.getAppraise()+"");//点赞数
                        intent.putExtra("comment_num",item.getComment_num()+"");//评论数
                        intent.putExtra("is_appraise",item.getIs_appraise()+"");//是否点赞1已经点赞，0没有点赞
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.openLoadAnimation(AnimationType.ALPHA);
        mRecyclerView.setAdapter(mAdapter);
        view=getLayoutInflater().inflate(R.layout.header_news_policy,null);
        img_icon= (ImageView) view.findViewById(R.id.img_icon);

        txt_title= (TextView) view.findViewById(R.id.txt_title);
        txt_come= (TextView) view.findViewById(R.id.txt_come);
        txt_info= (TextView) view.findViewById(R.id.txt_info);
        txt_collection= (TextView) view.findViewById(R.id.txt_collection);
        txt_comment_home= (TextView) view.findViewById(R.id.txt_comment_home);
         itemone= (RelativeLayout) view.findViewById(R.id.itemone);

        mAdapter.openLoadingMore(true);
        mAdapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pages>page_no){
                            initLoading(page+"","");
                            // baseRecyclerAdapter.notifyDataChangeAfterLoadMore(true);
                        }else {
                            mAdapter.notifyDataChangeAfterLoadMore(false);
                            if (pages > 1) {
                                mAdapter.addNoMoreView();
                            }
                        }
                    }
                },2000);
            }
        });
    }

    private void yesnet() {
        setToolBarTitle("新闻政策");
        initLoading(page+"","");
    }

    /**
     * 取新闻政策信息
     **/
    private void initLoading(String page,String information_id){
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this,"token"));
        Map<String, String> map = Utils.getMap(this,SharedPreferencedUtils.getString(this,"token"));
        map.put("page", page);
        map.put("per_page", "10");
        map.put("information_id", information_id);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        homePresenter.loadNewsList(map);
    }
    @Override
    public void showInfo(String string) {
        Log.e("news",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
       if (status.optInt("code")==1000){
           mAdapter.addHeaderView(view);
         Gson gson=new Gson();
         final NewsPolicyBean newsPolicyBean=gson.fromJson(string,NewsPolicyBean.class);
         List<NewsPolicyBean.ResultBean.DataBean> news=newsPolicyBean.getResult().getData();
        if (page==1){
        Glide.with(this).load(newsPolicyBean.getResult().getData().get(0).getCover()).error(R.mipmap.timg).into(img_icon);
        txt_title.setText(newsPolicyBean.getResult().getData().get(0).getTitle());
        txt_collection.setText(newsPolicyBean.getResult().getData().get(0).getAppraise()+"");
        txt_comment_home.setText(newsPolicyBean.getResult().getData().get(0).getComment_num()+"");
            txt_come.setText(newsPolicyBean.getResult().getData().get(0).getAdd_time());
            txt_info.setText(newsPolicyBean.getResult().getData().get(0).getSource());
        itemone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到详情页
                Intent intent = new Intent(NewsPolicyActivity.this, NewsPolicyShowActivity.class);
                intent.putExtra("appraise",newsPolicyBean.getResult().getData().get(0).getAppraise()+"");//点赞数
                intent.putExtra("comment_num",newsPolicyBean.getResult().getData().get(0).getComment_num()+"");//评论数
                intent.putExtra("is_appraise",newsPolicyBean.getResult().getData().get(0).getIs_appraise()+"");//是否点赞1已经点赞，0没有点赞
                intent.putExtra("information_id",newsPolicyBean.getResult().getData().get(0).getRes_id()+"");
                startActivity(intent);
            }
        });
        }
        List<NewsPolicyBean.ResultBean.DataBean> lists=new ArrayList<>();
        for (int i = 1; i <news.size() ; i++) {
           NewsPolicyBean.ResultBean.DataBean n=new NewsPolicyBean.ResultBean.DataBean();
           n.setRes_id(news.get(i).getRes_id());
           n.setTitle(news.get(i).getTitle());
           n.setIntro(news.get(i).getIntro());
           n.setCover(news.get(i).getCover());
           n.setContent(news.get(i).getContent());
           n.setAdd_time(news.get(i).getAdd_time());
           n.setAppraise(news.get(i).getAppraise());
           n.setComment_num(news.get(i).getComment_num());
           lists.add(n);
        }
        page_no=Integer.parseInt(newsPolicyBean.getResult().getPage().getPage_no());
        pages =newsPolicyBean.getResult().getPage().getPages();
        if (page==1){
            list.clear();
            list.addAll(lists);
        }else {
            list.addAll(lists);
        }
        if (list.size()>0) {
            noview.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.setData(list);
            mAdapter.notifyDataChangeAfterLoadMore(true);
            if (page_no == pages) {
                mAdapter.notifyDataChangeAfterLoadMore(false);
                if (pages > 1) {
                    mAdapter.addNoMoreView();
                }
            }
            page++;
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
    public void showcityid(String ss) {

    }

    @Override
    public void showErrMsg(String msg) {

    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);

    }
}
