package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.SearchUserBean;
import com.example.fishingport.app.presenter.FriendPresenter;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.view.DividerItemDecoration;
import com.example.fishingport.app.view.GlideCircleTransform;
import com.example.fishingport.app.view.IconCenterEditText;
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
 * Created by wushixin on 2017/4/18.
 * 添加渔友
 */

public class AddMemberActivity extends BaseAppCompatActivity implements FriendConstract.view {
    @BindView(R.id.icet_search)
    IconCenterEditText icetSearch;
    FriendPresenter friendPresenter;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<SearchUserBean.ResultBean> mAdapter;
    private List<SearchUserBean.ResultBean> list=new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_addmember;
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
        friendPresenter = new FriendPresenter(this);
        setToolBarTitle("添加渔友");
        setTextRight("搜索").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSearchUser(icetSearch.getText().toString());
            }
        });
        mAdapter=new BaseRecyclerAdapter<SearchUserBean.ResultBean>(this, null, R.layout.item_searchuser) {
            @Override
            protected void convert(final BaseViewHolder helper, final SearchUserBean.ResultBean item) {
                ImageView imgAvatar= (ImageView) helper.getConvertView().findViewById(R.id.img_user);
                Glide.with(AddMemberActivity.this).load(item.getAvatar()).transform(new GlideCircleTransform(AddMemberActivity.this)).into(imgAvatar);
                 helper.setText(R.id.txt_user_name,item.getNick_name());
                 helper.setText(R.id.txt_user_autograph,item.getIntro());
                helper.setOnClickListener(R.id.content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Intent intent=new Intent(AddMemberActivity.this,FriendInfoActivity.class);
                        intent.putExtra("uid",item.getId()+"");
                        intent.putExtra("type",2);
                        intent.putExtra("is_friend",item.isIs_friend());
                        startActivity(intent);
                        finish();
                    }
                });
            }
        };
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.openLoadAnimation(AnimationType.ALPHA);
        mRecyclerView.setAdapter(mAdapter);
        // 实现TextWatcher监听即可
//        icetSearch.setOnSearchClickListener(new IconCenterEditText.OnSearchClickListener() {
//            @Override
//            public void onSearchClick(View view) {
//                Toast.makeText(AddMemberActivity.this, "i‘m going to seach", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    /**
     * 搜索好友
     */
    private void initSearchUser(String search) {
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("search", search);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadSearchUser(map);
    }

    @Override
    public void showInfo(String string) {
        Log.e("dasd",string);
        list.clear();
        Gson gson = new Gson();
        SearchUserBean searchUserBean = gson.fromJson(string, SearchUserBean.class);
        list.addAll(searchUserBean.getResult());
        mAdapter.setData(list);
    }

    @Override
    public void showErrMsg(String msg) {

    }
}
