package com.example.fishingport.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FishingCircleConstract;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;

import com.example.fishingport.app.model.UserInfoBean;
import com.example.fishingport.app.presenter.FishingCirclePresenter;
import com.example.fishingport.app.presenter.FriendPresenter;
import com.example.fishingport.app.presenter.UserPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.view.GlideCircleTransform;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/18.
 * 好友信息
 */

public class FriendInfoActivity extends BaseAppCompatActivity implements
        UserConstract.view,FishingCircleConstract.view,FriendConstract.view{

    @BindView(R.id.btn_login)
    Button btnLogin;
    UserPresenter userPresenter;
    FriendPresenter friendPersenter;
    @BindView(R.id.img_avatar)
    ImageView imgAvatar;
    @BindView(R.id.txt_user_name)
    TextView txtUserName;
    @BindView(R.id.txt_user_intro)
    TextView txtUserIntro;
    @BindView(R.id.txt_city)
    TextView txtCity;
    private int loadType;
    public static  Activity activity;
    @BindView(R.id.btn_linearlayout)
    LinearLayout btn_linearlayout;//点击这个按钮跳转到好友的渔友圈
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;//展示如果好友有图片显示的最新一条的图片
    private FishingCirclePresenter fishingpresenter;
    private List<String> imglist=new ArrayList<>();
    BaseRecyclerAdapter<String> basrRecycleradapter;
    @BindView(R.id.sex)
    ImageView sex;//男女
    @BindView(R.id.layout)
    LinearLayout layout;
    private  String is_friend="";
    private String uid="";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friendinfo;
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
        activity=this;
        userPresenter = new UserPresenter(this);
        fishingpresenter=new FishingCirclePresenter(this);
        friendPersenter=new FriendPresenter(this);
        uid=getIntent().getStringExtra("uid");
        if (uid.equals("")){
            initUserInfo("",
                    getIntent().getStringExtra("huanxin_name"));
        }else {
            initUserInfo(uid,
                    "");
        }

        setToolBarTitle("详细资料");
        rightimg();
        if (getIntent().getIntExtra("type", -1) == 1) {
            btnLogin.setText("发消息");

        } else {

        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FriendInfoActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        basrRecycleradapter=new BaseRecyclerAdapter<String>
                (this,null,R.layout.item_noreadimgs_view) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                ImageView imageView= (ImageView) helper.getConvertView().findViewById(R.id.img);
                Glide.with(FriendInfoActivity.this).load(item.toString()).into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(FriendInfoActivity.this,DymamicActivity.class);
                        intent.putExtra("type","1");
                        intent.putExtra("uid",uid);
                        intent.putExtra("isme",getIntent().getStringExtra("nickname"));
                        startActivity(intent);
                    }
                });
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(FriendInfoActivity.this,DymamicActivity.class);
                        intent.putExtra("type","1");
                        intent.putExtra("uid",uid);
                        intent.putExtra("isme",getIntent().getStringExtra("nickname"));
                        startActivity(intent);
                    }
                });
            }
        };
        basrRecycleradapter.openLoadAnimation(false);
        mRecyclerView.setAdapter(basrRecycleradapter);


    }

    private void rightimg() {
       if (SharedPreferencedUtils.getString(FriendInfoActivity.this,"uid").
               equals(getIntent().getStringExtra("uid"))){
           getImgRight().setVisibility(View.GONE);
       }else {
           getImgRight().setVisibility(View.VISIBLE);
           getImgRight().setImageDrawable(getResources().getDrawable(R.mipmap.fishingcircle_shape_icon));
           ViewGroup.LayoutParams params = getImgRight().getLayoutParams();
           params.height = 50;
           params.width = 70;
           getImgRight().setLayoutParams(params);
           getImgRight().setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   showPopupWindow(v);
               }
           });

       }
    }


    @OnClick({R.id.btn_linearlayout})
    public  void  OnClick(View view){
        switch (view.getId()){
            case R.id.btn_linearlayout://跳转到该好友的渔友圈
             Intent intent=new Intent(FriendInfoActivity.this,DymamicActivity.class);
                intent.putExtra("type","1");
                intent.putExtra("uid",getIntent().getStringExtra("uid"));
                intent.putExtra("isme",getIntent().getStringExtra("nickname"));
                startActivity(intent);
                break;
        }
    }
    @Override
    public void showInfo(String string) {
       Log.i("用户信息",string);
        if (loadType == 1) {
            Gson gson = new Gson();
            UserInfoBean userInfoBean = gson.fromJson(string, UserInfoBean.class);
            Glide.with(this).load(userInfoBean.getResult().getAvatar()).transform(new GlideCircleTransform(this)).into(imgAvatar);
            txtUserName.setText(userInfoBean.getResult().getNick_name());
            txtUserIntro.setText(userInfoBean.getResult().getIntro());
            txtCity.setText(userInfoBean.getResult().getCity());
            is_friend=userInfoBean.getResult().getIs_friend();
            uid=userInfoBean.getResult().getUid()+"";
            String sexs=userInfoBean.getResult().getGender();

            if (is_friend.equals("true")){
                //是本人好友
                btnLogin.setText("发消息");
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(FriendInfoActivity.this,MessageDetailsActivity.class);
                        Log.i("消息id",getIntent().getStringExtra("uid")+"/"+
                                getIntent().getStringExtra("huanxin_name"));
                        intent.putExtra("uid",getIntent().getStringExtra("uid"));
                        intent.putExtra("huanxin_name",getIntent().getStringExtra("huanxin_name"));
                        intent.putExtra("chattype","user");
                        intent.putExtra("avatar",getIntent().getStringExtra("avatar"));
                        intent.putExtra("nickname",getIntent().getStringExtra("nickname"));
                        startActivity(intent);
                    }
                });
            }else if (is_friend.equals("false")){
                //不是本人好友
                btnLogin.setText("加好友");
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(FriendInfoActivity.this,FriendsVerifyActivity.class);
                        intent.putExtra("uid",getIntent().getStringExtra("uid"));
                        startActivity(intent);
                    }
                });
            }
            if (sexs.equals("0")){
               sex.setVisibility(View.GONE);
            }else if (sexs.equals("1")){
                sex.setImageDrawable(getResources().getDrawable(R.mipmap.nanimg));
            }else if (sexs.equals("2")){
                sex.setImageDrawable(getResources().getDrawable(R.mipmap.nvimg));
            }
            get_end_circle_img(SharedPreferencedUtils.getString(FriendInfoActivity.this,"token"),
                    uid);

        }else  if (loadType==10){
            try {
                JSONObject object=new JSONObject(string);
                JSONObject status=object.optJSONObject("status");
                if (status.optInt("code")==1000){
                    HelpUtils.success(FriendInfoActivity.this,"删除成功");
                    finish();
                }else {
                    HelpUtils.warning(FriendInfoActivity.this,status.optString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showendcircle_img(String string) {
        Log.i("渔友圈图片动态",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            if (status.optInt("code")==1000){
                JSONObject result=object.optJSONObject("result");
                if (result!=null){
                    mRecyclerView.setVisibility(View.VISIBLE);
                JSONArray imgs=result.optJSONArray("imgs_thumb");
                for (int i=0;i<imgs.length();i++){
                    Log.i("图片",imgs.getString(i));
                    imglist.add(imgs.getString(i));
                    basrRecycleradapter.setData(imglist);
                   }
                }else {
                    mRecyclerView.setVisibility(View.GONE);
                }
            }else {
                HelpUtils.warning(FriendInfoActivity.this,""+status.optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void showsign(String s) {
    }
    @Override
    public void showErrMsg(String msg) {
    }
    /**
     * 用户信息
     */
    private void initUserInfo(String uid,String huanxin_id) {
        loadType = 1;
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        // encryptapMap.put("uid",uid);
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("uid", uid);
        map.put("hxid",huanxin_id);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        userPresenter.loadUserInfo(map);
    }

    /*
    * 获取最后一条渔友圈
    *
    * */
    private  void  get_end_circle_img(String token,String user_id){
        Log.i("参数===",token+"/"+user_id);
        Map<String,String> signmap=Utils.getSignParams(FriendInfoActivity.this,token);
        signmap.put("user_id",user_id);
        Map<String,String> map=Utils.getMap(FriendInfoActivity.this,token);
        map.put("user_id",user_id);
        map.put("sign",Utils.getMd5StringMap(signmap));
        fishingpresenter.get_end_circle_img(map);
    }

    /*
    * 删除好友
    * */
    private  void  delete_friend(String token,String friend_id){
        Map<String,String> signmap=Utils.getSignParams(FriendInfoActivity.this,token);
        Map<String,String> map=Utils.getMap(FriendInfoActivity.this,token);
        map.put("friend_id",friend_id);
        map.put("sign",Utils.getMd5StringMap(map));
        friendPersenter.delete_friend(map);
    }


    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(FriendInfoActivity.this).inflate(
                R.layout.friend_popview, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        contentView.findViewById(R.id.txt_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加备注
                Intent intent=new Intent(FriendInfoActivity.this,ChangefriendActivity.class);
                intent.putExtra("uid",getIntent().getStringExtra("uid"));
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.txt_add_member).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除好友
                loadType=10;
                delete_friend(SharedPreferencedUtils.getString(FriendInfoActivity.this,"token"),
                        uid);
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
