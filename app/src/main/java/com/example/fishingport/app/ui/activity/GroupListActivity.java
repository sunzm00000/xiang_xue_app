package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.fishingport.app.Dao.GroupbeanDaoManager;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.bean.Groupbean;
import com.example.fishingport.app.bean.GroupbeanDao;
import com.example.fishingport.app.model.GroupListBean;
import com.example.fishingport.app.model.SearchGrouplist;
import com.example.fishingport.app.presenter.FriendPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.view.DividerItemDecoration;
import com.example.fishingport.app.view.circularavatar.CircularImageView;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.animation.AnimationType;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/25.
 * 获取群聊列表
 */

public class GroupListActivity extends BaseAppCompatActivity implements FriendConstract.view{
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private FriendPresenter friendPresenter;
    private BaseRecyclerAdapter<GroupListBean.ResultBean> mAdapter;
    @BindView(R.id.btn_search)
    RelativeLayout btn_search;//搜索
    @Override
    protected int getLayoutId() {
        return R.layout.activity_grouplist;
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
        friendPresenter=new FriendPresenter(this);
        setToolBarTitle("群聊");
        mAdapter=new BaseRecyclerAdapter<GroupListBean.ResultBean>(this, null, R.layout.item_grouplist) {
            @Override
            protected void convert(final BaseViewHolder helper, final GroupListBean.ResultBean item) {
                helper.setText(R.id.tvGroup,item.getTitle());
                final CircularImageView  img_avatar= (CircularImageView)
                        helper.getConvertView().findViewById(R.id.img_avatar);
                 final ArrayList<Bitmap> mBmps=new ArrayList<>();
                Log.i("mbmps==",""+item.getAvatar().size());
                int numlist=item.getAvatar().size();
                if (numlist>5){
                    numlist=5;
                }
                for (int i = 0; i <numlist ; i++) {
                    Glide.with(mContext).load(item.getAvatar().get(i)).asBitmap().
                            into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            mBmps.add(resource);
                            img_avatar.setImageBitmaps(mBmps);
                        }
                    });
                }
                helper.setOnClickListener(R.id.content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(GroupListActivity.this,MessageDetailsActivity.class);
                        intent.putExtra("type",1);
                        intent.putExtra("uid",item.getId());
                        intent.putExtra("huanxin_name",item.getHuanxin_id());
                        intent.putExtra("chattype","group");
                        intent.putExtra("nickname",item.getTitle()+"");//群聊昵称
                        List<String> avatarlist=item.getAvatar();
                        intent.putExtra("avatar","");
                        startActivity(intent);
                    }
                });
                Log.i("执行了","执行了把群聊id和环信群聊id==========");
                GroupbeanDao groupbeanDao = GroupbeanDaoManager.getInstance().getNewSession().getGroupbeanDao();
                List<Groupbean> list =groupbeanDao.queryBuilder().where(GroupbeanDao.
                        Properties.Group_id.eq(item.getId())).list();
                if (list.size()==0){
                    Log.i("执行了","执行了把群聊id和环信群聊id");
                    Groupbean groupbean=new Groupbean();
                    groupbean.setGroup_id(item.getId());
                    groupbean.setHuanxin_id(item.getHuanxin_id());
                    groupbean.setNicke_name(item.getTitle());
                    groupbean.setId(null);
                    groupbeanDao.insert(groupbean);
                }else {
                    Groupbean groupbean=new Groupbean();
                    groupbean.setGroup_id(item.getId());
                    groupbean.setHuanxin_id(item.getHuanxin_id());
                    groupbean.setNicke_name(item.getTitle());
                    groupbean.setId(null);
                    groupbeanDao.save(groupbean);
                }
            }
        };
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.openLoadAnimation(AnimationType.ALPHA);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @OnClick({R.id.btn_search})
    public void  onClick(View view){
        switch (view.getId()){
            case R.id.btn_search:
                //点击搜索按钮,跳转到群聊搜索页面
                startActivity(new Intent(GroupListActivity.this, SearchGroupActiviy.class));
                break;
        }
    }

    /**
     * 获取群聊列表
     * */
    private void initGroupList(){
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadGroupList(map);
    }

    @Override
    public void showInfo(String string) {
        Log.e("群聊列表",string);
        Gson gson=new Gson();
        GroupListBean groupListBean=gson.fromJson(string,GroupListBean.class);
        if (groupListBean.getStatus().getCode()==1000) {
            mAdapter.setData(groupListBean.getResult());
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        initGroupList();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrMsg(String msg) {

    }
}
