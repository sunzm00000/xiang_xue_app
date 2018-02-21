package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.hyphenate.chat.EMClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/17.
 * 群聊信息
 */

public class ChatMessageActivity extends BaseAppCompatActivity implements FriendConstract.view {
    @BindView(R.id.layout_ground_title)
    RelativeLayout layoutGroundTitle;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.layout_autograph)
    RelativeLayout layoutAutograph;
    @BindView(R.id.txt_groud_title)
    TextView txtGroudTitle;
    @BindView(R.id.txt_groud_member)
    TextView txtGroudMember;
    @BindView(R.id.txt_groud_intro)
    TextView txtGroudIntro;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private int type;
    private FriendPresenter friendPresenter;
    List <GroupInfoBean.ResultBean.UserListBean> list;
    private String group_id;
    private  String huanxin_id;
    private int loadtype=0;
    private  String create_user_id;//群主
    @BindView(R.id.chatnember)
    RelativeLayout chatnember;//群成员
    @Override
    protected int getLayoutId() {
        return R.layout.activity_chatmessage;
    }
    @Override
    protected int getFragmentContentId() {
        return 0;
    }
    private BaseRecyclerAdapter<GroupInfoBean.ResultBean.UserListBean> mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        setToolBarTitle("群聊信息");
        friendPresenter = new FriendPresenter(this);
        huanxin_id=getIntent().getStringExtra("huanxin_id");
        mAdapter=new BaseRecyclerAdapter<GroupInfoBean.ResultBean.UserListBean>(this, null, R.layout.item_groupavatar) {
            @Override
            protected void convert(final BaseViewHolder helper, final GroupInfoBean.ResultBean.UserListBean item) {
                ImageView img_avatar= (ImageView) helper.getConvertView().findViewById(R.id.img_avatar);
                  Glide.with(ChatMessageActivity.this).load(item.getAvatar()).transform(new GlideCircleTransform(ChatMessageActivity.this)).into(img_avatar);
                    helper.setOnClickListener(R.id.content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(mContext, "111", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ChatMessageActivity.this,ChooseMemberActivity.class));
                        }
                    });
                helper.setText(R.id.txt_name,item.getNick_name());
            }
        };
        GridLayoutManager mgr = new GridLayoutManager(this, 5);
        mRecyclerView.setLayoutManager(mgr);
        mAdapter.openLoadAnimation(AnimationType.ALPHA);
        mRecyclerView.setAdapter(mAdapter);
    }
    @OnClick({R.id.layout_ground_title, R.id.btn_login, R.id.layout_autograph,R.id.chatnember})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.chatnember://群成员
                intent.setClass(ChatMessageActivity.this,ChatNemberActivity.class);
                intent.putExtra("group_id",group_id);
                startActivity(intent);
                break;
            case R.id.layout_ground_title:
                if (create_user_id.equals(SharedPreferencedUtils.getString(ChatMessageActivity.this,"uid"))){
                    HelpUtils.success(ChatMessageActivity.this,"不是群主，没有权限操作");
                }else {
                    intent.setClass(ChatMessageActivity.this, ModifyTitleActivity.class);
                    intent.putExtra("type", 0); //0 群标题 1 群简介
                    intent.putExtra("group_id", group_id);
                    startActivity(intent);
                }
                break;
            case R.id.btn_login:
                if (create_user_id.equals(SharedPreferencedUtils.getString(ChatMessageActivity.this,"uid"))){
                    initQuitGroup(getIntent().getStringExtra("group_id"));
                }else {
                    initQuitGroup(getIntent().getStringExtra("group_id"));
                }
                break;
            case R.id.layout_autograph:
                if (create_user_id.equals(SharedPreferencedUtils.getString(ChatMessageActivity.this,"uid"))){
                     HelpUtils.success(ChatMessageActivity.this,"不是群主，没有权限操作");
                }else {
                    intent.setClass(ChatMessageActivity.this, ModifyTitleActivity.class);
                    intent.putExtra("type", 1); //0 群标题 1 群简介
                    intent.putExtra("group_id", group_id);
                    startActivity(intent);
                }
                break;
        }
    }
    @Override
    public void showInfo(String string) {
        Log.i("获取的群信息",string);
        if (loadtype==1) {
        Gson gson = new Gson();
        GroupInfoBean groupInfoBean = gson.fromJson(string, GroupInfoBean.class);
        group_id=groupInfoBean.getResult().getId();
            create_user_id=groupInfoBean.getResult().getCreate_user_id();
      if (create_user_id.equals(SharedPreferencedUtils.getString(ChatMessageActivity.this,"uid"))){
                btnLogin.setText("解散群");
      }
        txtGroudTitle.setText(groupInfoBean.getResult().getTitle());
        txtGroudIntro.setText(groupInfoBean.getResult().getCard());
        txtGroudMember.setText("共 " + groupInfoBean.getResult().getUser_list().size() + " 人");
        list=groupInfoBean.getResult().getUser_list();
        mAdapter.setData(groupInfoBean.getResult().getUser_list());
        }else if (loadtype==2){
            try {
                JSONObject object=new JSONObject(string);
                JSONObject status=object.optJSONObject("status");
                if (status.optInt("code")==1000){
                    HelpUtils.success(ChatMessageActivity.this,"退群成功");
                  boolean ff= EMClient.getInstance().chatManager().deleteConversation(huanxin_id, true);
                    if (ff==true){
                        startActivity(new Intent(ChatMessageActivity.this, GroupListActivity.class));
                        finish();
                        MessageDetailsActivity.activity.finish();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void showErrMsg(String msg) {

    }
    /**
     * 群信息
     */
    private void initGroupInfo(String group_id) {
        loadtype=1;
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        encryptapMap.put("group_id", group_id);
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("group_id", group_id);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadGroupInfo(map);
    }
    /**
     * 退出/解散群聊
     */
    private void initQuitGroup(String group_id) {
        loadtype=2;
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        encryptapMap.put("group_id", group_id);
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("group_id", group_id);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadQuitGroup(map);
    }
    @Override
    protected void onResume() {
        super.onResume();
        initGroupInfo(getIntent().getStringExtra("group_id"));
    }

}
