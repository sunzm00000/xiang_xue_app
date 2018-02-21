package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.chatlist;
import com.example.fishingport.app.presenter.FriendPresenter;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.view.IconCenterEditText;
import com.example.fishingport.app.view.circularavatar.CircularImageView;
import com.example.fishingport.app.view.dragindicator.DragIndicatorView;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.google.gson.Gson;
import com.gxz.library.SwapRecyclerView;
import com.gxz.library.SwapWrapperUtils;
import com.gxz.library.view.SwipeMenuLayout;
import com.gxz.library.view.SwipeMenuView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
/*
*搜索群聊
* */

public class MessagelistActivity extends BaseAppCompatActivity implements FriendConstract.view{

    FriendPresenter friendPresenter;
    @BindView(R.id.icet_search)
    IconCenterEditText icet_search;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.noview)
    TextView noview;
    private  int istiaomessage=0;
    BaseRecyclerAdapter<chatlist.ResultBean> baseadapterlist;
    private List<chatlist.ResultBean> mlist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        friendPresenter=new FriendPresenter(this);
        setToolBarTitle("搜索会话");
        setTextRight("搜索").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initSearchUser(icetSearch.getText().toString());

            }
        });

        icet_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mlist.clear();
                get_chatlist(SharedPreferencedUtils.getString(MessagelistActivity.this,"token"),s.toString());
            }
        });
        showAdapter();
    }

    private void showAdapter() {
        baseadapterlist=new BaseRecyclerAdapter<chatlist.ResultBean>
                (MessagelistActivity.this,null,R.layout.item_message) {
            @Override
            protected void convert(BaseViewHolder helper, final chatlist.ResultBean item) {
                helper.setText(R.id.txt_message_name,item.getTitle());
                helper.setText(R.id.text_info,item.getMsg());
                helper.setText(R.id.txt_time,item.getMsg_time());
                RelativeLayout btn_itemview = (RelativeLayout) helper.getConvertView().findViewById(R.id.btn_itemview);
                final CircularImageView imgavar= (CircularImageView) helper.getConvertView().findViewById(R.id.img_avatar);
                DragIndicatorView txt_indicatorview= (DragIndicatorView) helper.getConvertView().findViewById(R.id.txt_indicatorview);
                if (item.getType().equals("user")){
                    //单聊
                    for (int i=0;i<item.getUser_list().size();i++){
                        if (SharedPreferencedUtils.getString(MessagelistActivity.this,"uid").
                                equals(item.getUser_list().get(i).getId())){

                        }else {
                            EMConversation conversation = EMClient.getInstance().chatManager().
                                    getConversation(item.getUser_list().get(i).getHuanxin_name());
                            if (conversation!=null){
                                String sss = String.valueOf(conversation.getUnreadMsgCount());
                                if (sss!=null){
                                    if (sss.equals("0")) {
                                        txt_indicatorview.setVisibility(View.GONE);
                                    } else {
                                        txt_indicatorview.setVisibility(View.VISIBLE);
                                        txt_indicatorview.setText(sss);
                                    }
                                }else {
                                    txt_indicatorview.setVisibility(View.GONE);
                                }
                                Log.i("未读消息数", sss + "");
                            }else {
                                txt_indicatorview.setVisibility(View.GONE);
                            }
                            final int finalI = i;
                            btn_itemview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    istiaomessage=1;
                                    Intent intent = new Intent(MessagelistActivity.this,
                                            MessageDetailsActivity.class);
                                    intent.putExtra("uid", item.getUser_list().get(finalI).getId() + "");
                                    intent.putExtra("chattype", "user");
                                    intent.putExtra("avatar", "");
                                    intent.putExtra("nickname", "");
                                    intent.putExtra("huanxin_name",item.getUser_list().get(finalI).getHuanxin_name());//
                                    mContext.startActivity(intent);
                                    finish();
                                }
                            });
                            final ArrayList<Bitmap> mBmps = new ArrayList<Bitmap>();
                            Glide.with(mContext).load(item.getUser_list().get(i).getAvatar())
                                    .asBitmap().into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource,
                                                            GlideAnimation<? super Bitmap> glideAnimation) {
                                    mBmps.add(resource);
                                    imgavar.setImageBitmaps(mBmps);
                                }
                            });
                        }
                    }
                }else if (item.getType().equals("group")){
                    EMConversation conversation = EMClient.getInstance().chatManager().
                            getConversation(item.getHuanxin_id());
                    if (conversation!=null){
                        String sss = String.valueOf(conversation.getUnreadMsgCount());
                        if (sss!=null){
                            if (sss.equals("0")) {
                                txt_indicatorview.setVisibility(View.GONE);
                            } else {
                                txt_indicatorview.setVisibility(View.VISIBLE);
                                txt_indicatorview.setText(sss);
                            }
                        }else {
                            txt_indicatorview.setVisibility(View.GONE);
                        }
                        Log.i("未读消息数", sss + "");
                    }else {
                        txt_indicatorview.setVisibility(View.GONE);
                    }
                    btn_itemview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            istiaomessage=1;
                            Intent intent = new Intent(MessagelistActivity.this,
                                    MessageDetailsActivity.class);
                            intent.putExtra("uid", item.getGroup_id() + "");
                            intent.putExtra("chattype", "group");
                            intent.putExtra("avatar", "");
                            intent.putExtra("nickname", "");
                            intent.putExtra("huanxin_name", item.getHuanxin_id());//
                            mContext.startActivity(intent);
                            finish();
                        }
                    });
                    final ArrayList<Bitmap> mBmps = new ArrayList<Bitmap>();
                    int usersize=item.getUser_list().size();
                    if (usersize>5){
                        usersize=5;
                    }
                    for (int i=0;i<usersize;i++){
                        Glide.with(mContext).load(item.getUser_list().get(i).getAvatar())
                                .asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource,
                                                        GlideAnimation<? super Bitmap> glideAnimation) {
                                mBmps.add(resource);
                                imgavar.setImageBitmaps(mBmps);
                            }
                        });
                    }
                }
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MessagelistActivity.this, LinearLayoutManager.VERTICAL, false));
        baseadapterlist.openLoadAnimation(false);
        mRecyclerView.setAdapter(baseadapterlist);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_messagelist;
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
        Log.i("消息会话列表",string+"/");
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(string);
            JSONObject status=jsonObject.optJSONObject("status");
            if (status.optInt("code")==1000) {
                Gson gson = new Gson();
                chatlist chatlist = gson.fromJson(string, chatlist.class);
                mlist.addAll(chatlist.getResult());
                if (mlist.size() > 0) {
                    noview.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    baseadapterlist.setData(mlist);
                } else {
                    noview.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /*
   * 获取会话列表
   * */
    private void get_chatlist(String token,String title){

        Map<String,String> signmap= Utils.getSignParams(MessagelistActivity.this,token);
        Map<String,String> map=Utils.getMap(MessagelistActivity.this,token);
        map.put("title",title);
        map.put("sign",Utils.getMd5StringMap(signmap));
        friendPresenter.get_chatlist(map);
    }
}
