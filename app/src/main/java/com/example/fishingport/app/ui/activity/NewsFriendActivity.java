package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.bumptech.glide.Glide;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.NewFriendBean;
import com.example.fishingport.app.presenter.FriendPresenter;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.view.DividerItemDecoration;
import com.example.fishingport.app.view.GlideCircleTransform;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.animation.AnimationType;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wushixin on 2017/4/19.
 * 新渔友
 */

public class NewsFriendActivity extends BaseAppCompatActivity implements FriendConstract.view{
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.layout)
    LinearLayout layout;
    private BaseRecyclerAdapter<NewFriendBean.ResultBean> mAdapter;
    private FriendPresenter friendPresenter;
    private List<NewFriendBean.ResultBean> list=new ArrayList<>();
    private int postion;
    private int loadType=1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_newsfriend;
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
        setToolBarTitle("新渔友");
        friendPresenter=new FriendPresenter(this);
        initNewFriend();
        Glide.with(this).load(R.mipmap.add_member_icon).into(getImgRight());

        getImgRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
        mAdapter=new BaseRecyclerAdapter<NewFriendBean.ResultBean>(this, null, R.layout.item_newsfriend) {
            @Override
            protected void convert(final BaseViewHolder helper, final NewFriendBean.ResultBean item) {
                RelativeLayout layout_friend= (RelativeLayout) helper.getConvertView().findViewById(R.id.layout_friend);
                ImageView img_avatar= (ImageView) helper.getConvertView().findViewById(R.id.img_avatar);
                TextView txt_friends= (TextView) helper.getConvertView().findViewById(R.id.txt_friends);
                helper.setText(R.id.txt_name,item.getNick_name());

                if (item.getProgress()==1){
                    layout_friend.setVisibility(View.VISIBLE);
                    txt_friends.setVisibility(View.GONE);

                } else if (item.getProgress()==2){
                    layout_friend.setVisibility(View.GONE);
                    txt_friends.setVisibility(View.VISIBLE);

                }
                Glide.with(NewsFriendActivity.this).
                        load(item.getAvatar()).transform(new GlideCircleTransform(NewsFriendActivity.this)).into(img_avatar);
                helper.setOnClickListener(R.id.layout_friend, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postion=helper.getAdapterPosition();
                        initAgreeApply(item.getApply_id()+"");
                    }
                });



            }


        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mAdapter.openLoadAnimation(AnimationType.ALPHA);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void showInfo(String string) {
        Log.e("friend",string);
        if (loadType==1) {
            Gson gson = new Gson();
            NewFriendBean newFriendBean = gson.fromJson(string, NewFriendBean.class);
            list.addAll(newFriendBean.getResult());
            mAdapter.setData(list);
        } else {
            try {
                JSONObject jsonObject=new JSONObject(string);
                if (jsonObject.getJSONObject("status").getInt("code")==1000){
                    list.get(postion).setProgress(2);
                    mAdapter.notifyDataSetChanged();
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
     * 新渔友列表*/
    private void initNewFriend(){
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this,"token"));
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this,"token"));
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadNewFriend(map);
    }
    /**
     * 同意好友申请*/
    private void initAgreeApply(String apply_id){
        loadType=2;
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this,"token"));
        encryptapMap.put("apply_id",apply_id);
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this,"token"));
        map.put("apply_id",apply_id);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadAgreeApply(map);
    }

    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(NewsFriendActivity.this).inflate(
                R.layout.message_pop_window, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        contentView.findViewById(R.id.txt_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发起群聊
                Intent intent=new Intent(NewsFriendActivity.this,ChooseMemberActivity.class);
                intent.putExtra("type","1");//通讯录
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.txt_add_member).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加好友
                startActivity(new Intent(NewsFriendActivity.this, AddMemberActivity.class));
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
}
