package com.example.fishingport.app.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.HomeConstract;
import com.example.fishingport.app.model.NewsPolicyBean;
import com.example.fishingport.app.presenter.HomePresenter;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.ui.activity.NewsPolicyShowActivity;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.animation.AnimationType;
import com.google.gson.Gson;
import com.yuyh.library.imgsel.ImgSelActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wushixin on 2017/4/17.
 * 推荐
 */

public class RecommendFragment extends Fragment implements HomeConstract.view{
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.noview)
    LinearLayout noview;
    private BaseRecyclerAdapter<NewsPolicyBean.ResultBean.DataBean> mAdapter;
    private HomePresenter homePresenter;
    private int page=1;
    private List<NewsPolicyBean.ResultBean.DataBean> list=new ArrayList<>();
    public static RecommendFragment newInstance(String info) {
        Bundle args = new Bundle();
        args.putString("info", info);
        RecommendFragment detailFragment = new RecommendFragment();
        detailFragment.setArguments(args);
        return detailFragment;
    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, null);
        homePresenter=new HomePresenter(this);
        String information_id=getArguments().getString("info");
        Log.i("新闻政策分类",information_id+"");
      // Toast.makeText(getActivity(), information_id, Toast.LENGTH_SHORT).show();
        if (information_id.equals("0")){
            recommendlist(page+"","10",SharedPreferencedUtils.getString(getActivity(),"token"));
        }else {
            initLoading(page+"",information_id);
        }
        ButterKnife.bind(this, view);
        mAdapter=new BaseRecyclerAdapter<NewsPolicyBean.ResultBean.DataBean>(getActivity(),
                null,
                R.layout.item_recommend) {
            @Override
            protected void convert(final BaseViewHolder helper, final NewsPolicyBean.ResultBean.DataBean item) {
                ImageView img_right_recommend= (ImageView) helper.getConvertView().findViewById(R.id.img_right_recommend);
                Log.i("url推荐",item.getCover());
                Glide.with(getActivity()).load(item.getCover()).into(img_right_recommend);
                helper.setText(R.id.txt_title_recommend,item.getIntro());
                helper.setText(R.id.txt_label,item.getTitle());
                helper.setText(R.id.txt_comment_home,item.getComment_num()+"");
                helper.setText(R.id.txt_collection,item.getAppraise()+"");
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到详情页
                        Intent intent = new Intent(getActivity(), NewsPolicyShowActivity.class);
                        intent.putExtra("information_id",item.getRes_id()+"");
                        intent.putExtra("appraise",item.getAppraise()+"");//点赞数
                        intent.putExtra("comment_num",item.getComment_num()+"");//评论数
                        intent.putExtra("is_appraise",item.getIs_appraise()+"");//是否点赞1已经点赞，0没有点赞
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.openLoadAnimation(AnimationType.ALPHA);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(list);
        return view;
    }
    @Override
    public void showInfo(String string) {
        Log.e("新闻政策",string);
        list.clear();
        Gson gson=new Gson();
        NewsPolicyBean newsPolicyBean=gson.fromJson(string,NewsPolicyBean.class);
        list.addAll(newsPolicyBean.getResult().getData());
        if (list.size()>0){
            mRecyclerView.setVisibility(View.VISIBLE);
            noview.setVisibility(View.GONE);
            mAdapter.setData(list);
        }else {
            View vaddview=getActivity().getLayoutInflater().inflate(R.layout.noresultview,null);
            ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            vaddview.setLayoutParams(params);
            mAdapter.addHeaderView(vaddview);
        }
    }

    @Override
    public void showcityid(String ss) {

    }
    @Override
    public void showErrMsg(String msg) {

    }
    /**
     * 取新闻政策信息
     **/
    private void initLoading(String page,String information_id){
        Map<String, String> encryptapMap = Utils.getSignParams(getActivity(), SharedPreferencedUtils.getString(getActivity(),"token"));
        Map<String, String> map = Utils.getMap(getActivity(),SharedPreferencedUtils.getString(getActivity(),"token"));
        map.put("page", page);
        map.put("per_page", "10");
        map.put("category_id", information_id);
        Log.e("category_id",information_id);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        homePresenter.loadNewsList(map);
    }

    /*
    * 取推荐新闻信息
    * */
    private void recommendlist(String page,String per_page,String token){
        //
        Log.i("取推荐新闻信息",token);
        Map<String,String> signmap=Utils.getSignParams(getActivity(),token);
        Map<String,String> map=Utils.getMap(getActivity(),token);
        map.put("page",page);
        map.put("per_page",per_page);
        map.put("sign",Utils.getMd5StringMap(signmap));
        homePresenter.recommendlist(map);
    }
}