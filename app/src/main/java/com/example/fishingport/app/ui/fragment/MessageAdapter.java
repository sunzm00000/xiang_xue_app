package com.example.fishingport.app.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.fishingport.app.Dao.GroupbeanDaoManager;
import com.example.fishingport.app.Dao.PersonalDaoManager;
import com.example.fishingport.app.R;

import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.bean.Groupbean;
import com.example.fishingport.app.bean.GroupbeanDao;
import com.example.fishingport.app.bean.Personal;
import com.example.fishingport.app.bean.PersonalDao;
import com.example.fishingport.app.presenter.FriendPresenter;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.ui.activity.MessageDetailsActivity;
import com.example.fishingport.app.view.circularavatar.CircularImageView;
import com.example.fishingport.app.view.dragindicator.DragIndicatorView;
import com.gxz.library.SwapWrapperUtils;
import com.gxz.library.SwipeMenuBuilder;
import com.gxz.library.view.SwipeMenuLayout;
import com.gxz.library.view.SwipeMenuView;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import retrofit2.http.FormUrlEncoded;


/**
 * Created by wushixin on 2017/4/17.
 * 消息adapter
 */

public class MessageAdapter extends RecyclerView.Adapter  {
    private List<EMConversation> mData;
    private Context mContext;
    private SwipeMenuBuilder swipeMenuBuilder;

    public MessageAdapter(List<EMConversation> mData, SwipeMenuBuilder swipeMenuBuilder, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        this.swipeMenuBuilder = swipeMenuBuilder;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据数据创建右边的操作view
        SwipeMenuView menuView = swipeMenuBuilder.create();
        //包装用户的item布局
        SwipeMenuLayout swipeMenuLayout = SwapWrapperUtils.wrap(parent,
                R.layout.item_message, menuView, new BounceInterpolator(), new LinearInterpolator());
        MyViewHolder holder = new MyViewHolder(swipeMenuLayout);
        setListener(parent, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final EMMessage message = mData.get(position).getLastMessage();//最后一条消息
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(message.getUserName());
        if (conversation!=null){
        String sss = String.valueOf(conversation.getUnreadMsgCount());
        if (sss!=null){
        if (sss.equals("0")) {
            viewHolder.txt_indicatorview.setVisibility(View.GONE);
        } else {
            viewHolder.txt_indicatorview.setVisibility(View.VISIBLE);
            viewHolder.txt_indicatorview.setText(sss);
        }
        }else {
            viewHolder.txt_indicatorview.setVisibility(View.GONE);
        }
            Log.i("未读消息数", sss + "");
        }else {
            viewHolder.txt_indicatorview.setVisibility(View.GONE);
        }
        if (message.getChatType() == EMMessage.ChatType.Chat) {
            //单聊
            Log.i("单聊", message.getFrom() + "/" + message.getUserName() + "/" + message.getTo() + message.direct());
            PersonalDao personalDao = PersonalDaoManager.getInstance().getNewSession().getPersonalDao();
            final List<Personal> personalList = personalDao.queryBuilder().
                    where(PersonalDao.Properties.Huanxin_id.eq(message.getUserName())).list();
            viewHolder.textView.setText(personalList.get(0).getNickname());
            final ArrayList<Bitmap> mBmps = new ArrayList<Bitmap>();
            Glide.with(mContext).load(personalList.get(0).getHead_img())
                    .asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mBmps.add(resource);
                    viewHolder.img_avatar.setImageBitmaps(mBmps);
                }
            });
            if (message.getType() == EMMessage.Type.IMAGE) {//
                viewHolder.text_info.setText("图片");
            } else if (message.getType() == EMMessage.Type.TXT) {//文字消息
                EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                viewHolder.text_info.setText(body.getMessage());
            }
            viewHolder.btn_itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MessageDetailsActivity.class);
                    intent.putExtra("uid", personalList.get(0).getUid() + "");
                    intent.putExtra("chattype", "user");
                    intent.putExtra("avatar", "");
                    intent.putExtra("nickname", "");
                    intent.putExtra("huanxin_name", personalList.get(0).getHuanxin_id());//
                    mContext.startActivity(intent);
                }
            });
        } else if (message.getChatType() == EMMessage.ChatType.GroupChat) {
            Log.i("群聊", message.getFrom() + "/" + message.getUserName() + "/" + message.getTo() + message.direct());
            //群聊
            final List<String> imglist = new ArrayList<>();
            final String groupId = message.getTo();
            if (message.getType() == EMMessage.Type.IMAGE) {//
                viewHolder.text_info.setText("图片");
            } else if (message.getType() == EMMessage.Type.TXT) {//文字消息
                EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                viewHolder.text_info.setText(body.getMessage());
            }
            GroupbeanDao groupbeanDao = GroupbeanDaoManager.getInstance()
                    .getNewSession().getGroupbeanDao();
            final List<Groupbean> list = groupbeanDao.queryBuilder().
                    where(GroupbeanDao.Properties.Huanxin_id.eq(groupId+"")).list();
            viewHolder.btn_itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MessageDetailsActivity.class);
                    intent.putExtra("uid", list.get(0).getGroup_id() + "");
                    intent.putExtra("chattype", "group");
                    intent.putExtra("avatar", "");
                    intent.putExtra("nickname", "");
                    intent.putExtra("huanxin_name", list.get(0).getHuanxin_id());//
                    mContext.startActivity(intent);
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    EMGroup group = null;
                    try {
                        PersonalDao personalDao = PersonalDaoManager.getInstance().getNewSession().getPersonalDao();
                        group = EMClient.getInstance().groupManager().getGroupFromServer(groupId, true);
                        Log.i("群信息",group.getMembers().size()+"/"+group.getGroupName());
                        List<String> grouplist = group.getMembers();
                        int ddd = grouplist.size();
                        if (ddd > 5) {
                            ddd = 5;
                        }
                        for (int i = 0; i < ddd; i++) {
                            final List<Personal> personalList = personalDao.queryBuilder().
                                    where(PersonalDao.Properties.Huanxin_id.eq(grouplist.get(i))).list();
                            imglist.add(personalList.get(0).getHead_img());
                            Log.i("群组信息", group.getMembers().size() + "/"
                                    + grouplist.get(i) + "/" + personalList.get(0).getHead_img());
                }
                        Log.i("头像", imglist.size() + "");
                        imglist.add(SharedPreferencedUtils.getString(mContext, "avatar"));
                        if (imglist.size() > 0) {
                            final EMGroup finalGroup = group;
                            new android.os.Handler(mContext.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    viewHolder.textView.setText(finalGroup.getGroupName());
                                    final ArrayList<Bitmap> mBmps = new ArrayList<Bitmap>();
                                    for (int i = 0; i < imglist.size(); i++) {
                                        Glide.with(mContext).load(imglist.get(i)).asBitmap().into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                mBmps.add(resource);
                                                viewHolder.img_avatar.setImageBitmaps(mBmps);
                                            }
                                        });
                                    }
                                }
                            }, 0);
                        }

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        viewHolder.txt_indicatorview.setOnDismissAction(new DragIndicatorView.OnIndicatorDismiss() {
            @Override
            public void OnDismiss(DragIndicatorView view) {
                //TODO  当红点因拖拽或调用dismissView消失时
                //清除未读消息数
                EMConversation conversation = EMClient.getInstance
                        ().chatManager().getConversation(message.getUserName());
                EMClient.getInstance().chatManager().markAllConversationsAsRead();
                // Toast.makeText(mContext, "textView-onClick->>>" + mData.get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void remove(int pos) {
        this.notifyItemRemoved(pos);
    }

    protected void setListener(final ViewGroup parent, final MyViewHolder viewHolder, int viewype) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, mData.get(position), position);
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, mData.get(position), position);
                }
                return false;
            }
        });
    }
    public OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, com.hyphenate.chat.EMConversation o, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, com.hyphenate.chat.EMConversation o, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        CircularImageView img_avatar;
        DragIndicatorView txt_indicatorview;
        TextView text_info;
        RelativeLayout btn_itemview;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_info = (TextView) itemView.findViewById(R.id.text_info);
            textView = (TextView) itemView.findViewById(R.id.txt_message_name);
            img_avatar = (CircularImageView) itemView.findViewById(R.id.img_avatar);
            txt_indicatorview = (DragIndicatorView) itemView.findViewById(R.id.txt_indicatorview);
            btn_itemview = (RelativeLayout) itemView.findViewById(R.id.btn_itemview);


        }


    }
}