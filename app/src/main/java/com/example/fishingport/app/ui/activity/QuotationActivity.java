package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.HomeConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.MarketList;
import com.example.fishingport.app.presenter.HomePresenter;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.animation.AnimationType;
import com.github.library.listener.RequestLoadMoreListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wushixin on 2017/4/19.
 * 鱼货行情
 */

public class QuotationActivity extends BaseAppCompatActivity implements HomeConstract.view,SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.noview)
    LinearLayout noview;//没有结果页
    private BaseRecyclerAdapter<MarketList.ResultBean.DataBean> mAdapter;
    private List<MarketList.ResultBean.DataBean> mList=new ArrayList<>();
    private HomePresenter homePresenter;
    private int page=1;
    private  int pages, page_no;
    @BindView(R.id.mswiperefresh)
    SwipeRefreshLayout mswiperefresh;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    page=1;
                    mList.clear();
                    initMarketList(page+"");
                    mswiperefresh.setRefreshing(false);
                    break;
            }
        }
    };
    @Override
    protected int getLayoutId() {
        return R.layout.activity_nearbyport;
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
        setToolBarTitle("鱼货行情");
        homePresenter=new HomePresenter(this);
        mswiperefresh.setOnRefreshListener(this);
        mAdapter=new BaseRecyclerAdapter<MarketList.ResultBean.DataBean>(this, null, R.layout.item_quotation) {
            @Override
            protected void convert(final BaseViewHolder helper, final MarketList.ResultBean.DataBean item)
            {
                helper.setOnClickListener(R.id.itemview, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(QuotationActivity.this,QuotationDetails.class);
                        intent.putExtra("id",item.getMarket_id()+"");
                        intent.putExtra("appraise",item.getAppraise()+"");//点赞数
                        intent.putExtra("comment_num",item.getComment_num()+"");//评论数
                        intent.putExtra("is_appraise",item.getIs_appraise()+"");//是否点赞1已经点赞，0没有点赞
                        startActivity(intent);
                    }
                });
               helper.setText(R.id.txt_title,item.getTitle());
               helper.setText(R.id.txt_time,Utils.friendly_time(item.getAdd_time()));
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.openLoadAnimation(AnimationType.ALPHA);
        mAdapter.openLoadingMore(true);
        mRecyclerView.setAdapter(mAdapter);
        initMarketList(page+"");
        mAdapter.openLoadingMore(true);
        mAdapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pages>page_no){
                            initMarketList(page+"");
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
    @Override
    public void showInfo(String string) {
        Log.e("string ",string);
        Gson gson=new Gson();
        MarketList m=gson.fromJson(string,MarketList.class);
         page_no=Integer.parseInt(m.getResult().getPage().getPage_no());
        pages =m.getResult().getPage().getPages();
        if (page==1){
            mList.clear();
            mList.addAll(m.getResult().getData());
        }else {
            mList.addAll(m.getResult().getData());
        }
        if (mList.size()>0){
            noview.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.setData(mList);
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

    @Override
    public void showcityid(String ss) {

    }

    @Override
    public void showErrMsg(String msg) {

    }
    /**
     * 取鱼货行情信息
     **/
    private  void initMarketList(String page){
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this,"token"));

        Map<String, String> map = Utils.getMap(this,SharedPreferencedUtils.getString(this,"token"));
        map.put("page", page);
        map.put("per_page", "10");
        map.put("market_id", "");
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        homePresenter.loadMarketList(map);
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);

    }
}
