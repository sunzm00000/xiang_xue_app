package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.GroupInfoBean;
import com.example.fishingport.app.presenter.FriendPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.view.GlideCircleTransform;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.animation.AnimationType;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatNemberActivity extends BaseAppCompatActivity  implements FriendConstract.view {
    private FriendPresenter friendPresenter;
    private BaseRecyclerAdapter<GroupInfoBean.ResultBean.UserListBean> mAdapter;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;//群聊成员
    List<GroupInfoBean.ResultBean.UserListBean> list;
    private int loadtype=0;
    private int isshowdelete=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBarTitle("群成员");
        initview();
        showadapter();
    }

    private void showadapter() {
        mAdapter=new BaseRecyclerAdapter<GroupInfoBean.ResultBean.UserListBean>(this,
                null, R.layout.item_groupavatar) {
            @Override
            protected void convert(final BaseViewHolder helper,
                                   final GroupInfoBean.ResultBean.UserListBean item) {
                ImageView img_avatar= (ImageView) helper.getConvertView().findViewById(R.id.img_avatar);
                ImageView delete= (ImageView) helper.getConvertView().findViewById(R.id.delete);
                if (helper.getAdapterPosition()<list.size()-2){
                    //删除
                    if (isshowdelete==1){
                    delete.setVisibility(View.VISIBLE);
                }else {
                        delete.setVisibility(View.GONE);
                    }
                Glide.with(ChatNemberActivity.this).load(item.getAvatar()).
                        transform(new GlideCircleTransform(ChatNemberActivity.this)).into(img_avatar);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SharedPreferencedUtils.getString(ChatNemberActivity.this,"uid").equals(item.getId())){
                            HelpUtils.warning(ChatNemberActivity.this,"不能删除自己!");
                        }else {
                            loadtype=1;
                            delete_group_user(SharedPreferencedUtils.getString(ChatNemberActivity.this,"token"),
                                    getIntent().getStringExtra("group_id"),item.getId());
                        }
                    }
                });
                     } else if (helper.getAdapterPosition()==list.size()-1){
                    delete.setVisibility(View.GONE);
                    helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isshowdelete==1){
                                isshowdelete=0;
                            }else {
                                isshowdelete=1;
                            }

                            notifyDataSetChanged();
                        }
                    });
                    Glide.with(ChatNemberActivity.this).load(R.mipmap.chatdelete).into(img_avatar);

                }else {
                    delete.setVisibility(View.GONE);
                    helper.setOnClickListener(R.id.content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(mContext, "111", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(ChatNemberActivity.this,ChooseMemberActivity.class);
                            intent.putExtra("type",2+"");
                            startActivityForResult(intent,1);
                        }
                    });
                    Glide.with(ChatNemberActivity.this).load(R.mipmap.add_member_grpup_iocn).into(img_avatar);
                }
                helper.setText(R.id.txt_name,item.getNick_name());
            }
        };
        GridLayoutManager mgr = new GridLayoutManager(this, 5);
        mRecyclerView.setLayoutManager(mgr);
        mAdapter.openLoadAnimation(AnimationType.ALPHA);
        mRecyclerView.setAdapter(mAdapter);
    }
    private void initview() {
        friendPresenter = new FriendPresenter(this);
        initGroupInfo(getIntent().getStringExtra("group_id"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            if (requestCode==1&&resultCode==3){
                String user_ids=data.getStringExtra("user_ids");
                add_group_user(SharedPreferencedUtils.getString(ChatNemberActivity.this,"token"),
                        getIntent().getStringExtra("group_id"),user_ids);
            }



        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_nember;
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
        Log.i("打印的信息",string);
        if (loadtype==0){
            //加载的群成员名称和照片
            Gson gson = new Gson();
            GroupInfoBean groupInfoBean = gson.fromJson(string, GroupInfoBean.class);
            list=groupInfoBean.getResult().getUser_list();
            GroupInfoBean.ResultBean.UserListBean userListBean=new GroupInfoBean.ResultBean.UserListBean();
            userListBean.setIcon(R.mipmap.add_member_grpup_iocn);
            userListBean.setNick_name("添加");
            list.add(userListBean);
            GroupInfoBean.ResultBean.UserListBean userListBean1=new GroupInfoBean.ResultBean.UserListBean();
            userListBean1.setIcon(R.mipmap.chatdelete);
            userListBean1.setNick_name("删除");
            list.add(userListBean1);
            mAdapter.setData(list);
        }else if (loadtype==1){
            //删除群成员
            try {
                JSONObject object=new JSONObject(string);
                JSONObject status=object.optJSONObject("status");
                if (status.optInt("code")==1000){
                    HelpUtils.success(ChatNemberActivity.this,"删除成功");
                    isshowdelete=0;
                    initGroupInfo(getIntent().getStringExtra("group_id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if (loadtype==2){
            try {
                JSONObject object=new JSONObject(string);
                JSONObject status=object.optJSONObject("status");
                if (status.optInt("code")==1000){
                    HelpUtils.success(ChatNemberActivity.this,"添加成功");
                    initGroupInfo(getIntent().getStringExtra("group_id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 群信息
     */
    private void initGroupInfo(String group_id) {
        loadtype=0;
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        encryptapMap.put("group_id", group_id);
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("group_id", group_id);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadGroupInfo(map);
    }

    /*
    * 删除群成员
    *
    * */
    private void delete_group_user(String token,String group_id,String user_ids){
        loadtype=1;
         Map<String,String> signmap=Utils.getSignParams(this,token);
        signmap.put("group_id",group_id);
        signmap.put("user_ids",user_ids);
        Map<String,String> map=Utils.getMap(this,token);
        map.put("group_id",group_id);
        map.put("user_ids",user_ids);
        map.put("sign",Utils.getMd5StringMap(signmap));
        friendPresenter.delete_group_user(map);
    }
    /*
  * 添加群成员
  *
  * */
    private void add_group_user(String token,String group_id,String user_ids){
        loadtype=2;
        Map<String,String> signmap=Utils.getSignParams(this,token);
        signmap.put("group_id",group_id);
        signmap.put("user_ids",user_ids);
        Map<String,String> map=Utils.getMap(this,token);
        map.put("group_id",group_id);
        map.put("user_ids",user_ids);
        map.put("sign",Utils.getMd5StringMap(signmap));
        friendPresenter.add_group_user(map);
    }
}
