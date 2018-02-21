package com.example.fishingport.app.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.base.BaseFragment;
import com.example.fishingport.app.model.chatlist;
import com.example.fishingport.app.presenter.FriendPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.tools.WeiboDialogUtils;
import com.example.fishingport.app.ui.activity.AddMemberActivity;
import com.example.fishingport.app.ui.activity.ChooseMemberActivity;
import com.example.fishingport.app.ui.activity.MailListActivity;
import com.example.fishingport.app.ui.activity.MessageDetailsActivity;
import com.example.fishingport.app.ui.activity.MessagelistActivity;
import com.example.fishingport.app.ui.activity.TarckActivity;
import com.example.fishingport.app.view.circularavatar.CircularImageView;
import com.example.fishingport.app.view.dragindicator.DragIndicatorView;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.google.gson.Gson;
import com.gxz.library.SwapRecyclerView;
import com.gxz.library.SwapWrapperUtils;
import com.gxz.library.SwipeMenuBuilder;
import com.gxz.library.bean.SwipeMenu;
import com.gxz.library.bean.SwipeMenuItem;
import com.gxz.library.view.SwipeMenuLayout;
import com.gxz.library.view.SwipeMenuView;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/17.
 * 消息
 */

public class MessageFragment extends BaseFragment implements SwipeMenuBuilder,FriendConstract.view,SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.img_mail_list)
    ImageView imgMailList;
    @BindView(R.id.img_add_member)
    ImageView imgAddMember;
    @BindView(R.id.mswiperefresh)
    SwipeRefreshLayout mswiperefresh;
    View layoutMessage;
    @BindView(R.id.mRecyclerView)
    SwapRecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    @BindView(R.id.noresult)
    LinearLayout  noresult;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.search_message)
    RelativeLayout  search_message;
    @BindView(R.id.nonet)
    LinearLayout nonet;
    @BindView(R.id.nonet_refresh)
    TextView nonet_refresh;//刷新网络
    List<EMConversation> emlist;
    private FriendPresenter friendPresenter;
    private Dialog mWeiboDialog;
    BaseRecyclerAdapter<chatlist.ResultBean> baseadapterlist;
    private  List<chatlist.ResultBean> mlist=new ArrayList<>();
    private Map<String,chatlist.ResultBean> mmap=new HashMap<>();
    private SwipeMenuBuilder swipeMenuBuilder;
    private  int intpos=0;
    private  int loadtype=0;
    private  int istiaomessage=0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    mlist.clear();
                    get_chatlist(SharedPreferencedUtils.getString(getActivity(),"token"));
                    mswiperefresh.setRefreshing(false);
                    break;
            }
        }
    };
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        friendPresenter=new FriendPresenter(this);
        mswiperefresh.setOnRefreshListener(this);
        layoutMessage=view.findViewById(R.id.layout_message);
        if (NetUtils.hasNetwork(getActivity())){
            nonet.setVisibility(View.GONE);
            get_chatlist(SharedPreferencedUtils.getString(getActivity(),"token"));
            showAdapter();
        }else {
            HelpUtils.warning(getActivity(),"暂无网络");
            nonet.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }
    private void showAdapter() {
      baseadapterlist=new BaseRecyclerAdapter<chatlist.ResultBean>
              (getActivity(),null,R.layout.item_message) {
          @Override
          public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
              SwipeMenuView menuView =create();
              SwipeMenuLayout swipeMenuLayout = SwapWrapperUtils.wrap(parent,
                      R.layout.item_message, menuView, new BounceInterpolator(), new LinearInterpolator());
              BaseViewHolder  holder = new BaseViewHolder(swipeMenuLayout);
              return holder;
          }
          @Override
          protected void convert(BaseViewHolder helper, final chatlist.ResultBean item) {
              helper.setText(R.id.txt_message_name,item.getTitle());
              helper.setText(R.id.text_info,item.getMsg());
              helper.setText(R.id.txt_time,item.getMsg_time());
              RelativeLayout  btn_itemview = (RelativeLayout) helper.getConvertView().findViewById(R.id.btn_itemview);
              final CircularImageView imgavar= (CircularImageView) helper.getConvertView().findViewById(R.id.img_avatar);
              DragIndicatorView txt_indicatorview= (DragIndicatorView) helper.getConvertView().findViewById(R.id.txt_indicatorview);
              if (item.getType().equals("user")){
                 //单聊
                 for (int i=0;i<item.getUser_list().size();i++){
                     if (SharedPreferencedUtils.getString(getActivity(),"uid").
                             equals(item.getUser_list().get(i).getId())){
                     }else {
                         final EMConversation conversation = EMClient.getInstance().chatManager().
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
                                 if (conversation!=null){
                                     conversation.markAllMessagesAsRead();
                                 }
                                 istiaomessage=1;
                                 Intent intent = new Intent(getActivity(), MessageDetailsActivity.class);
                                 intent.putExtra("uid", item.getUser_list().get(finalI).getId() + "");
                                 intent.putExtra("chattype", "user");
                                 intent.putExtra("avatar", "");
                                 intent.putExtra("nickname", "");
                                 intent.putExtra("huanxin_name",item.getUser_list().get(finalI).getHuanxin_name());//
                                 mContext.startActivity(intent);
                             }
                         });
                         final ArrayList<Bitmap> mBmps = new ArrayList<Bitmap>();
                         Log.i("头像展示",item.getUser_list().get(i).getAvatar()+"");
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
                 final EMConversation conversation = EMClient.getInstance().chatManager().
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
                         if (conversation!=null){
                             conversation.markAllMessagesAsRead();
                         }
                         istiaomessage=1;
                         Intent intent = new Intent(getActivity(), MessageDetailsActivity.class);
                         intent.putExtra("uid", item.getGroup_id() + "");
                         intent.putExtra("chattype", "group");
                         intent.putExtra("avatar", "");
                         intent.putExtra("nickname", "");
                         intent.putExtra("huanxin_name", item.getHuanxin_id());//
                         mContext.startActivity(intent);
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        baseadapterlist.openLoadAnimation(false);
        mRecyclerView.setAdapter(baseadapterlist);
    }

    @Override
    public void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        if (istiaomessage==1){
            mlist.clear();
            get_chatlist(SharedPreferencedUtils.getString(getActivity(),"token"));
            istiaomessage=0;
        }

    }

    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            Log.i("收到了消息",messages.get(0).getChatType()+"/"+messages.get(0).getUserName()
                    +"/"+messages.get(0).getFrom()+"/"+messages.get(0).getTo()+"/"+messages.get(0).getMsgId());
            HelpUtils.success(getActivity(),"收到了消息");
            if (messages.get(0).getChatType()== EMMessage.ChatType.GroupChat){
                Log.i("收到了消息--","执行了接口");
                //群聊
                getchatinfo(SharedPreferencedUtils.getString(getActivity(),"token"),"group",
                        messages.get(0).getTo());
            }else if (messages.get(0).getChatType()== EMMessage.ChatType.Chat){
                Log.i("收到了消息==","执行了接口");
                getchatinfo(SharedPreferencedUtils.getString(getActivity(),"token"),"user",
                        messages.get(0).getUserName());
            }
        }
        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }
        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }
        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }
        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };
    private void liaotianlistshow() {
        emlist=loadConversationList();
        if (emlist.size()==0){
            noresult.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else {
            noresult.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter=new MessageAdapter(emlist,this,getActivity());
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setOnSwipeListener(new SwapRecyclerView.OnSwipeListener() {
                @Override
                public void onSwipeStart(int position) {
                    // Toast.makeText(getActivity(),"onSwipeStart-"+position,Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onSwipeEnd(int position) {
                    //  Toast.makeText(getActivity(), "onSwipeEnd-" + position, Toast.LENGTH_SHORT).show();
                }
            });
            mAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, EMConversation o, int position) {

                }
                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, EMConversation o, int position) {
                    return false;
                }
            });
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }
    @OnClick({R.id.img_mail_list, R.id.img_add_member,R.id.search_message,R.id.nonet_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nonet_refresh:
                if (NetUtils.hasNetwork(getActivity())){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    nonet.setVisibility(View.GONE);
                    get_chatlist(SharedPreferencedUtils.getString(getActivity(),"token"));
                    showAdapter();
                }else {
                    HelpUtils.warning(getActivity(),"暂无网络");
                    nonet.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
                break;
            case R.id.img_mail_list:
                showPopupWindow(view);
                break;
            case R.id.img_add_member:
                startActivity(new Intent(getActivity(), MailListActivity.class));
                break;
            case R.id.search_message:
                startActivity(new Intent(getActivity(), MessagelistActivity.class));
                break;
        }
    }
    @Override
    public SwipeMenuView create() {
        SwipeMenu menu = new SwipeMenu(getActivity());
        SwipeMenuItem item = new SwipeMenuItem(getActivity());
        item = new SwipeMenuItem(getActivity());
        item.setTitle("删除")
                .setTitleColor(Color.WHITE)
                .setTitleSize(dp2px(6))
                .setBackground(new ColorDrawable(Color.parseColor("#e84a5f")));
        menu.addMenuItem(item);
        SwipeMenuView menuView = new SwipeMenuView(menu);
        menuView.setOnMenuItemClickListener(mOnSwipeItemClickListener);
        return menuView;
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    private SwipeMenuView.OnMenuItemClickListener mOnSwipeItemClickListener =
            new SwipeMenuView.OnMenuItemClickListener() {
        @Override
        public void onMenuItemClick(int pos, SwipeMenu menu, int index) {
            loadtype=6;
            intpos=pos;
            deletechat(SharedPreferencedUtils.getString(getActivity(),"token"),mlist.get(pos).getId());
        }
    };
    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.message_pop_window, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        contentView.findViewById(R.id.txt_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ChooseMemberActivity.class);
                intent.putExtra("type","1");//通讯录
                startActivity(intent);
               popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.txt_add_member).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddMemberActivity.class));
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
        popupWindow.showAtLocation(layout,Gravity.TOP,0,0);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
    }


    /**
     * 获取会话列表
     *
     * @param
     * @return
    +    */
    protected List<EMConversation> loadConversationList(){
        // 获取所有会话，包括陌生人
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    //if(conversation.getType() != EMConversationType.ChatRoom){
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                    //}
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }

        return list;
    }

    /**
 * 根据最后一条消息的时间排序
 *
 * @param
 */
private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
    Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
        @Override
        public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

            if (con1.first == con2.first) {
                return 0;
            } else if (con2.first > con1.first) {
                return 1;
            } else {
                return -1;
            }
        }

    });
}
    @Override
    public void showErrMsg(String msg) {

    }
    @Override
    public void showInfo(String string) {

        //群信息
        Log.i("消息会话列表",string+"/"+loadtype);
        try {
            JSONObject jsonObject=new JSONObject(string);
            JSONObject status=jsonObject.optJSONObject("status");
            if (status.optInt("code")==1000){
                if (loadtype==1){
                Gson gson=new Gson();
                chatlist chatlist=gson.fromJson(string,chatlist.class);
                mlist.addAll(chatlist.getResult());
                if (mlist.size()>0){
                    noresult.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    baseadapterlist.setData(mlist);
                }else {
                    noresult.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
                }else if (loadtype==2){
                    Gson gson=new Gson();
                    chatlist chatlist=gson.fromJson(string,chatlist.class);
                    Log.i("本地是否有--",mlist.size()+"/"+chatlist.getResult().get(0).getId());
                    int isdeng=-1;
                    for (int i=0;i<mlist.size();i++){
                        Log.i("本地是否有==", mlist.get(i).getId() + "/" + chatlist.getResult().get(0).getId());
                        if (mlist.get(i).getId().equals(chatlist.getResult().get(0).getId())) {
                            isdeng =i;
                            Log.i("本地是否有", mlist.get(i).getId() + "/" + chatlist.getResult().get(0).getId());
                            //本地有这个字段
                        }
                    }
                    if (isdeng!=-1){
                        Log.i("是否执行了","执行了"+isdeng);
                        mlist.remove(isdeng);
                        mlist.add(0,chatlist.getResult().get(0));
                        noresult.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        Log.i("是否执行了","执行了"+isdeng+"/"+mlist.size());
                        baseadapterlist.setData(mlist);
                        baseadapterlist.notifyDataSetChanged();
                    }else {
                        Log.i("是否执行了","执行了"+isdeng);
                        mlist.add(0,chatlist.getResult().get(0));
                        noresult.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        baseadapterlist.setData(mlist);
                        baseadapterlist.notifyDataSetChanged();
                    }
                }     else if (loadtype==6){
                    Log.i("删除会话",string);
                    JSONObject object=new JSONObject(string);
                    JSONObject deletestatus=object.optJSONObject("status");
                    if (deletestatus.optInt("code")==1000){
                        mRecyclerView.smoothCloseMenu(intpos);
                        mlist.remove(intpos);
                        baseadapterlist.setData(mlist);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
     /*
     * 获取会话列表
     * */
    private void get_chatlist(String token){
        loadtype=1;
        Map<String,String> signmap= Utils.getSignParams(getActivity(),token);
        Map<String,String> map=Utils.getMap(getActivity(),token);
        map.put("sign",Utils.getMd5StringMap(signmap));
        friendPresenter.get_chatlist(map);
    }
    /*
    * 获取最新的一条消息
    * */
    private  void  getchatinfo(String token,String res_name,String res_id){
        loadtype=2;
        Map<String,String> singmap=Utils.getSignParams(getActivity(),token);
        Map<String,String> map=Utils.getSignParams(getActivity(),token);
        map.put("res_name",res_name);
        map.put("res_id",res_id);
        map.put("sign",Utils.getMd5StringMap(singmap));
        friendPresenter.getchatinfo(map);
    }

    /*
    * 删除会话
    * */
    private void deletechat(String token,String id){
        Map<String,String> signmap=Utils.getSignParams(getActivity(),token);
        Map<String,String> map=Utils.getMap(getActivity(),token);
        map.put("id",id);
        map.put("sign",Utils.getMd5StringMap(signmap));
        friendPresenter.deletechat(map);
    }
    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);
    }
}
