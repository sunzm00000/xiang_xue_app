package com.example.fishingport.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Picture;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.fishingport.app.Dao.GroupbeanDaoManager;
import com.example.fishingport.app.Dao.MessageDaoManager;

import com.example.fishingport.app.Dao.PersonalDaoManager;
import com.example.fishingport.app.R;
import com.example.fishingport.app.adapter.NoHorizontalScrollerVPAdapter;
import com.example.fishingport.app.api.DownPicturesConstrack;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.bean.Groupbean;
import com.example.fishingport.app.bean.GroupbeanDao;
import com.example.fishingport.app.bean.Message;
import com.example.fishingport.app.bean.MessageDao;
import com.example.fishingport.app.bean.Personal;
import com.example.fishingport.app.bean.PersonalDao;
import com.example.fishingport.app.model.ImageModel;
import com.example.fishingport.app.model.MessageList;
import com.example.fishingport.app.model.MsgRecordList;
import com.example.fishingport.app.presenter.DownPicturesPresenter;
import com.example.fishingport.app.presenter.FriendPresenter;
import com.example.fishingport.app.tools.EaseImageCache;
import com.example.fishingport.app.tools.EaseImageUtils;
import com.example.fishingport.app.tools.EmotionUtils;
import com.example.fishingport.app.tools.FragmentFactory;
import com.example.fishingport.app.tools.GlobalOnItemClickManagerUtils;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.tools.WeiboDialogUtils;
import com.example.fishingport.app.ui.fragment.Fragment1;
import com.example.fishingport.app.view.DividerItemDecoration;

import com.example.fishingport.app.view.GlideCircleTransform;
import com.example.fishingport.app.view.emotionkeyboardview.EmotiomComplateFragment;
import com.example.fishingport.app.view.emotionkeyboardview.EmotionKeyboard;
import com.example.fishingport.app.view.emotionkeyboardview.NoHorizontalScrollerViewPager;
import com.example.fishingport.app.widget.GlideRoundTransform;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.animation.AnimationType;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMChatService;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.ImageUtils;
import com.hyphenate.util.NetUtils;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.example.fishingport.app.ui.activity.FriendInfoActivity.activity;

/**
 * Created by wushixin on 2017/4/17.
 * 消息详情
 */

public class MessageDetailsActivity extends BaseAppCompatActivity implements
        DownPicturesConstrack.view,FriendConstract.view ,SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.img_noun_expressio)
    ImageView imgNounExpressio;
    @BindView(R.id.edit_message)
    EditText editMessage;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.layout_view)
    LinearLayout layout_view;//全部布局
    @BindView(R.id.content)
    View content;
    @BindView(R.id.img_picture)
    ImageView imgPicture;
    @BindView(R.id.txt_ok)
    TextView txtOk;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    private BaseRecyclerAdapter<MessageList> mAdapter;
    @BindView(R.id.vp_emotionview_layout)
    NoHorizontalScrollerViewPager viewPager;
    List<Fragment> fragments = new ArrayList<>();
    private int CurrentPosition = 0;
    private boolean isBindToBarEditText = true;
    private FriendPresenter friendPresenter;
    MessageDao mMessageDao;
    private int REQUEST_IMAGE = 1;
    private String pathImage, pathImg = " ";
    LinearLayoutManager linearLayoutManager;
    private EMConversation mConversation;
    private List<MessageList> messageLists = new ArrayList<>();
    private  String uid="";
    private  String huanxin_name="";//群聊或者单聊
    private String avatar;//好友头像
    private String usernickname;//展示的对方聊天的昵称
    private  String nickname;//昵称
    EMMessage messages;
    private DownPicturesPresenter downPicturesPresenter;
    private  String chattype="";
    private  int isme=1;//说明不是自己2是自己
    private List<LocalMedia> selectMedia = new ArrayList<>();
    public static  Activity activity;
    private  List<String> imglist=new ArrayList<>();
    List<Map<String,String>> itemimglist=new ArrayList<>();
    private  int loadtype=0;
    List<MsgRecordList.ResultBean> msglist=new ArrayList<>();
    private int loadmoreid;
    /**
     * 自定义实现Handler，主要用于刷新UI操作
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 4:
                    mAdapter.setData(messageLists);
                    break;
                case 3://拉取聊天记录
                    mAdapter.setData(messageLists);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(messageLists.size()-1);
                    break;
                case 1://刷新聊天列表
                    messageLists.clear();
                    imglist.clear();
                    isme=1;
                    msgrecordmorelist(SharedPreferencedUtils.getString(MessageDetailsActivity.this,"token"),
                            chattype,huanxin_name,msglist.get(0).getId()+"");
                   // initConversation();
                    refresh.setRefreshing(false);
                    break;
                case 2://图片消息
                    //判断是什么类型的消息
                    Log.i("刘希亭的测试执行了","刘习他 的测试执行了");
                      messages = (EMMessage) msg.obj;
                    if (messages.getType()== EMMessage.Type.IMAGE){
                        EMImageMessageBody imageMessageBody= (EMImageMessageBody) messages.getBody();
                        Log.i("接受的消息",imageMessageBody.getThumbnailUrl()
                                +"分割"+imageMessageBody.getLocalUrl()
                           +"分割"+imageMessageBody.thumbnailLocalPath());
                        final MessageList messageList = new MessageList();
                        messageList.setMsgId(uid);
                        messageList.setContent("");
                        messageList.setTalker(SharedPreferencedUtils.getString(MessageDetailsActivity.this, "token"));
                        messageList.setCreateTime(System.currentTimeMillis());
                        messageList.setIsSend(0);
                        messageList.setIsme(isme+"");
                        messageList.setHuanxin_name(messages.getFrom());
                        messageList.setImg(imageMessageBody.thumbnailLocalPath());
                        imglist.add(imageMessageBody.thumbnailLocalPath());
                        messageList.setAdditional(System.currentTimeMillis() + "");
                        messageList.setAvatar(avatar);
                        messageList.setNickname(usernickname);
                        messageLists.add(messageList);
                        mAdapter.setData(messageLists);
                       // mAdapter.notifyDataSetChanged();
                        mRecyclerView.smoothScrollToPosition(messageLists.size()-1);
                    }else if (messages.getType()== EMMessage.Type.TXT){
                        EMTextMessageBody body = (EMTextMessageBody) messages.getBody();
                        tt=body.getMessage();
                        Log.i("接受的消息",body.getMessage());
                        // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                        MessageList messageList = new MessageList();
                        messageList.setMsgId(uid);
                        messageList.setContent(tt);
                        messageList.setTalker(SharedPreferencedUtils.getString(MessageDetailsActivity.this, "token"));
                        messageList.setCreateTime(System.currentTimeMillis());
                        messageList.setIsSend(0);
                        messageList.setImg(" ");
                        messageList.setHuanxin_name(messages.getFrom());
                        messageList.setIsme(isme+"");
                        messageList.setNickname(usernickname);
                        messageList.setAdditional(System.currentTimeMillis() + "");
                            messageList.setAvatar(avatar);
                        messageLists.add(messageList);
                        mAdapter.setData(messageLists);
                       // mAdapter.notifyDataSetChanged();
                        mRecyclerView.smoothScrollToPosition(messageLists.size()-1);
                    } else {
                        // 如果消息不是当前会话的消息发送通知栏通知
                    }
                    break;
            }
        }
    };



    //表情面板
    private EmotionKeyboard mEmotionKeyboard;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_details;
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
        setToolBarTitle("聊天");
        friendPresenter = new FriendPresenter(this);
         downPicturesPresenter=new DownPicturesPresenter(this);
        refresh.setOnRefreshListener(this);
        mMessageDao = MessageDaoManager.getInstance().getNewSession().getMessageDao();
        // mMessageDao.deleteAll();
        initview();
        initEmotion();//初始化表情模板
        initDatas();//数据操作
        //initConversation();
        showadapter();//聊天界面适配器
        linearLayoutManager = new LinearLayoutManager(this);
        // linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mAdapter.openLoadAnimation(AnimationType.ALPHA);
        mRecyclerView.setAdapter(mAdapter);
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                        HelpUtils.warning(MessageDetailsActivity.this,
                                "您的账号已被移除");
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        HelpUtils.warning(MessageDetailsActivity.this,
                                "您的账号在其他设备登录");
                    } else {
                        if (NetUtils.hasNetwork(MessageDetailsActivity.this)){
                            //连接不到聊天服务器
                            EMLogin(SharedPreferencedUtils.getString(MessageDetailsActivity.this,"huanxinname"),
                                    SharedPreferencedUtils.getString(MessageDetailsActivity.this,"huanxinpasswd"));
                        }
                        //当前网络不可用，请检查网络设置
                    }
                }
            });
        }
    }
    //环信登录
    private void EMLogin(final String huanxin_name, final String huanxin_passwd) {
        Log.i("登录环信执行了", "登录环信执行 了" + huanxin_name + "/" + huanxin_passwd);
        EMClient.getInstance().login(huanxin_name, huanxin_passwd, new EMCallBack() {
            /**
             * 登陆成功的回调
             */
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().chatManager().loadAllConversations();
                        EMClient.getInstance().groupManager().loadAllGroups();
                        Log.d("main登录成功", "登录聊天服务器成功"+"/"+nickname);
                    }
                });
            }
            /**
             * 登陆错误的回调
             * @param i
             * @param s
             */
            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("lzan13", "登录失败 Error code:" + i + ", message:" + s);

                        HelpUtils.warning(MessageDetailsActivity.this,"登录异常请重新登录！");
                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                Toast.makeText(MessageDetailsActivity.this, "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                Toast.makeText(MessageDetailsActivity.this, "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                Toast.makeText(MessageDetailsActivity.this, "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                Toast.makeText(MessageDetailsActivity.this, "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                Toast.makeText(MessageDetailsActivity.this, "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                HelpUtils.warning(MessageDetailsActivity.this,"暂无网络访问");
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                HelpUtils.warning(MessageDetailsActivity.this,"暂无网络访问");
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                Toast.makeText(MessageDetailsActivity.this, "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                HelpUtils.warning(MessageDetailsActivity.this,"暂无网络访问");
                                Toast.makeText(MessageDetailsActivity.this, "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(MessageDetailsActivity.this, "ml_sign_in_failed code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
     /*
     * 聊天界面消息适配器
     * */
    private void showadapter() {

        //聊天界面适配器
        mAdapter = new BaseRecyclerAdapter<MessageList>(this, null,
                R.layout.item_messagedeatails) {
            @Override
            protected void convert(final BaseViewHolder helper, final MessageList item) {
                ImageView img_right_avatar = (ImageView) helper.getConvertView().findViewById(R.id.img_right_avatar);
                TextView txt_right_message = (TextView) helper.getConvertView().findViewById(R.id.txt_right_message);
                ProgressBar right_mProgressBar = (ProgressBar) helper.getConvertView().findViewById(R.id.right_mProgressBar);
                RelativeLayout layout_right = (RelativeLayout) helper.getConvertView().findViewById(R.id.layout_right);
                final ImageView img_right_message = (ImageView) helper.getConvertView().findViewById(R.id.img_right_message);
                //左边
                LinearLayout layout_left= (LinearLayout) helper.getConvertView().findViewById(R.id.layout_left_image);//左边的总布局
                TextView txt_left_message= (TextView) helper.getConvertView().findViewById(R.id.txt_left_message);//文字
                ImageView img_left_icon= (ImageView) helper.getConvertView().findViewById(R.id.img_left_icon);//接收承载的图片
                ProgressBar left_mProgressBar = (ProgressBar) helper.getConvertView().findViewById(R.id.left_mProgressBar);//进度圈
                final ImageView img_left_avatar = (ImageView) helper.getConvertView().findViewById(R.id.img_left_avatar);//头像
                //Toast.makeText(mContext, EmotionUtils.getEmotionContent(MessageDetailsActivity.this,txt_right_message,item.getContent()), Toast.LENGTH_SHORT).show();
                //  Glide.with(MessageDetailsActivity.this).load(item.getImg()).override(300, 200).placeholder(R.mipmap.sos_icon).transform(new GlideRoundTransform(MessageDetailsActivity.this,10)).into(img_left_icon);
                if (item.getTalker().equals(SharedPreferencedUtils.getString(MessageDetailsActivity.this, "token"))) {
                    if (item.getIsme().equals("2")){
                        //左边的数据是对方发的
                        //隐藏右边所有的布局
                        Log.i("nickname",item.getNickname()+"");
                        helper.setText(R.id.nickname,item.getNickname());
                        if (item.getAvatar().equals("")){
                            Glide.with(MessageDetailsActivity.this).
                                    load(R.mipmap.user_avatar_icon).transform(new
                                    GlideCircleTransform(MessageDetailsActivity.this)).into(img_left_avatar);
                        }else {
                            Glide.with(MessageDetailsActivity.this).
                                    load(item.getAvatar()).
                                    transform(new GlideCircleTransform(MessageDetailsActivity.this)).into(img_left_avatar);
                        }
                        img_left_avatar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(MessageDetailsActivity.this, FriendInfoActivity.class);
                                i.putExtra("uid","");
                                i.putExtra("huanxin_name",item.getHuanxin_name());
                                i.putExtra("avatar",item.getAvatar());//头像
                                i.putExtra("nickname", item.getNickname());//昵称
                                mContext.startActivity(i);
                            }
                        });
                        txt_left_message.setText(EmotionUtils.getEmotionContent(MessageDetailsActivity.this, txt_right_message, item.getContent()));
                        layout_left.setVisibility(View.VISIBLE);
                        layout_right.setVisibility(View.GONE);
                        if (item.getImg().equals(" ")) {
                            txt_left_message.setVisibility(View.VISIBLE);
                            img_left_icon.setVisibility(View.GONE);
                            txt_left_message.setText(EmotionUtils.getEmotionContent(MessageDetailsActivity.this, txt_right_message, item.getContent()));
                        } else {
                            img_left_icon.setVisibility(View.VISIBLE);
                            txt_left_message.setVisibility(View.GONE);
                           // showImageView(item.getImg(),img_left_icon,imageMessageBody.getLocalUrl(),messages);
                            if (item.getImg().equals("")){
                                Glide.with(MessageDetailsActivity.this).load(R.mipmap.user_avatar_icon)
                                        .override(300, 200).transform(new
                                        GlideRoundTransform(MessageDetailsActivity.this, 10)).into(img_right_message);
                            }else {
                               int ishttp= item.getImg().indexOf("http://201704yg.alltosun.net");
                                if (ishttp!=-1){
                                    Log.i("说明取的是聊天记录","说明取的是聊天记录的图片");
                                    //说明取的是聊天记录展示需要用下面的方法
                                    Glide.with(MessageDetailsActivity.this).load(item.getImg())
                                            .override(300, 200).transform(new
                                            GlideRoundTransform(MessageDetailsActivity.this, 10)).into(img_left_icon);
                                }else {
                                    //说明是取的正在聊天的图片展示的是本地的环信缓存的
                                    Bitmap bitmap=BitmapFactory.
                                            decodeFile(item.getImg());
                                    img_left_icon.setImageBitmap(bitmap);
                                }

                                img_left_icon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String [] imageUrls=new String[imglist.size()];
                                        int postionimg=0;
                                        for (int i=0;i<imglist.size();i++){
                                            imageUrls[i]=imglist.get(i);
                                            if (item.getImg().equals(imageUrls[i])){
                                                postionimg=i;
                                            }
                                        }
                                        Intent intent = new Intent();
                                        intent.putExtra("imageUrls", imageUrls);
                                        intent.putExtra("curImageUrl", imageUrls[postionimg]);
                                        intent.setClass(MessageDetailsActivity.this, PhotoBrowserActivity.class);
                                       startActivity(intent);
                                    }
                                });
                            }
                           // showImageView(item.getImg(),img_left_icon,imageMessageBody.getLocalUrl(),messages);
                            //Glide.with(MessageDetailsActivity.this).load(item.getImg()).override(300, 200).transform(new GlideRoundTransform(MessageDetailsActivity.this, 10)).into(img_left_icon);
                        }
                        if (item.getIsSend() == 1) {
                            left_mProgressBar.setVisibility(View.GONE);
                        }
                    }else {
                        //自己发的消息

                        layout_left.setVisibility(View.GONE);
                        layout_right.setVisibility(View.VISIBLE);
                        if (item.getAvatar().equals("")){
                            Glide.with(MessageDetailsActivity.this).
                                    load(R.mipmap.user_avatar_icon).
                                    transform(new GlideCircleTransform(MessageDetailsActivity.this)).
                                    into(img_right_avatar);
                        }else {
                            Glide.with(MessageDetailsActivity.this).
                                    load(item.getAvatar()).
                                    transform(new GlideCircleTransform(MessageDetailsActivity.this)).
                                    into(img_right_avatar);
                        }
                        img_right_avatar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(MessageDetailsActivity.this, FriendInfoActivity.class);
                                i.putExtra("uid",SharedPreferencedUtils.getString(MessageDetailsActivity.this,"uid"));
                                i.putExtra("huanxin_name","");
                                i.putExtra("avatar",item.getAvatar());//头像
                                i.putExtra("nickname", item.getNickname());//昵称
                                mContext.startActivity(i);
                            }
                        });
                        if (item.getImg().equals(" ")) {
                          txt_right_message.setVisibility(View.VISIBLE);
                          img_right_message.setVisibility(View.GONE);
                          txt_right_message.setText(EmotionUtils.
                                  getEmotionContent(MessageDetailsActivity.this, txt_right_message, item.getContent()));
                    } else {
                            img_right_message.setVisibility(View.VISIBLE);
                             txt_right_message.setVisibility(View.GONE);
                            if (item.getImg().equals("")){
                                Glide.with(MessageDetailsActivity.this).load(R.mipmap.user_avatar_icon)
                                        .override(300, 200).transform(new
                                        GlideRoundTransform(MessageDetailsActivity.this, 10)).into(img_right_message);
                            }else {
                                Glide.with(MessageDetailsActivity.this).load(item.getImg())
                                        .override(300, 200).transform(new
                                        GlideRoundTransform(MessageDetailsActivity.this, 10)).into(img_right_message);
                            }
                            img_right_message.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String [] imageUrls=new String[imglist.size()];
                                    int postionimg=0;
                                    for (int i=0;i<imglist.size();i++){
                                        imageUrls[i]=imglist.get(i);
                                        if (item.getImg().equals(imageUrls[i])&&helper.getAdapterPosition()==i){
                                            postionimg=i;
                                        }
                                    }
                                    Intent intent = new Intent();
                                    intent.putExtra("imageUrls", imageUrls);
                                    intent.putExtra("curImageUrl", imageUrls[postionimg]);
                                    intent.setClass(MessageDetailsActivity.this, PhotoBrowserActivity.class);
                                    startActivity(intent);
                                }
                            });
                    }
                    if (item.getIsSend() == 1) {
                        right_mProgressBar.setVisibility(View.GONE);
                    }
                   // Glide.with(MessageDetailsActivity.this).load(item.getAvatar()).transform(new GlideCircleTransform(MessageDetailsActivity.this)).into(img_right_avatar);
                }
                }
            }
        };
    }

    /*
    * 初始化数据
    * */
    private void initview() {
        uid=getIntent().getStringExtra("uid");
        huanxin_name=getIntent().getStringExtra("huanxin_name");
        chattype=getIntent().getStringExtra("chattype");
        avatar=getIntent().getStringExtra("avatar");//
        nickname=getIntent().getStringExtra("nickname");//昵称，单聊或者群聊
        Log.i("昵称",nickname);
        Log.i("头像",avatar);
        Log.i("huanxin_id",huanxin_name);
        Log.i("uid",uid);
        setToolBarTitle(nickname);
        //拉取咱们公司后台服务器的聊天记录
        msgrecordlist(SharedPreferencedUtils.getString(MessageDetailsActivity.this,"token"),chattype,huanxin_name);

        if (chattype.equals("group")){
            //群聊
            GroupbeanDao groupbeanDao = GroupbeanDaoManager.getInstance().getNewSession().getGroupbeanDao();
            List<Groupbean> list =groupbeanDao.queryBuilder().
                    where(GroupbeanDao.Properties.Group_id.eq(uid)).list();
            if (list.size()==0){
                Groupbean groupbean=new Groupbean();
                groupbean.setGroup_id(uid);
                groupbean.setHuanxin_id(huanxin_name);
                groupbean.setNicke_name(nickname);
                groupbean.setId(null);
                groupbeanDao.insert(groupbean);
                Log.i("执行了","执行了把群聊id和环信群聊id");
            }else {
                Groupbean groupbean=new Groupbean();
                groupbean.setGroup_id(uid);
                groupbean.setHuanxin_id(huanxin_name);
                groupbean.setNicke_name(nickname);
                groupbean.setId(null);
                groupbeanDao.save(groupbean);
            }
        }else {
            //单聊
            PersonalDao personalDao = PersonalDaoManager.getInstance().getNewSession().getPersonalDao();
            List<Personal> list = personalDao.queryBuilder().where(PersonalDao.Properties.Uid.eq(uid)).list();
            Log.i("list有没有", list.size() + "");
            if (list.size() == 0) {
                Personal personal = new Personal();
                personal.setHead_img(avatar);
                personal.setHuanxin_id(huanxin_name);
                personal.setUid(uid);
                personal.setId(null);
                personal.setNickname(nickname);
                personalDao.insert(personal);
            }else {
                Personal personal = new Personal();
                personal.setHead_img(avatar);
                personal.setHuanxin_id(huanxin_name);
                personal.setUid(uid);
                personal.setId(null);
                personal.setNickname(nickname);
                personalDao.insert(personal);
            }
        }
        //初始化数据
        Glide.with(this).load(R.mipmap.group_members_icon).asBitmap().into(new SimpleTarget<Bitmap>() {
            //右上角的图标
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                getImgRight().setImageBitmap(resource);
            }
        });
        getImgRight().setOnClickListener(new View.OnClickListener() {
            //右上角的图片，群聊资料
            @Override
            public void onClick(View v) {
                if (chattype.equals("group")){
                    Intent intent = new Intent(MessageDetailsActivity.this, ChatMessageActivity.class);
                    intent.putExtra("group_id", getIntent().getStringExtra("uid"));
                    intent.putExtra("huanxin_id",huanxin_name);
                    startActivity(intent);
                }else {
                    Intent i = new Intent(MessageDetailsActivity.this, FriendInfoActivity.class);
                    i.putExtra("type", 1);
                    i.putExtra("uid", uid+ "");
                    i.putExtra("huanxin_name",huanxin_name);
                    i.putExtra("avatar", avatar);//头像
                    i.putExtra("nickname", nickname);//昵称
                    startActivity(i);
                }

            }
        });
        editMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            //聊天的对话框
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    Toast.makeText(MessageDetailsActivity.this, editMessage.getText().toString(), Toast.LENGTH_SHORT).show();
                    editMessage.setText("");
                    return true;
                }
                return false;
            }
        });
    }


    private void initConversation() {
        //初始化消息对象，并且根据需要加载更多
        /**
         * 初始化会话对象，这里有三个参数么，
         * 第一个表示会话的当前聊天的 useranme 或者 groupid
         * 第二个是绘画类型可以为空
         * 第三个表示如果会话不存在是否创建
         */
        mConversation = EMClient.getInstance().chatManager().getConversation(huanxin_name, null, true);
         int noread=mConversation.getUnreadMsgCount();
        if (mConversation==null){
            return;
        }else {
            // 设置当前会话未读数为 0
            mConversation.markAllMessagesAsRead();
            int count = mConversation.getAllMessages().size();
            if (count < mConversation.getAllMsgCount() && count < 20) {
                // 获取已经在列表中的最上边的一条消息id
                String msgId = mConversation.getAllMessages().get(0).getMsgId();
                // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
                mConversation.loadMoreMsgFromDB(msgId, 20 - count);
            }
            // 打开聊天界面获取最后一条消息内容并显示
            if (mConversation.getAllMessages().size() > 0) {
                for (int i=0;i<mConversation.getAllMessages().size();i++){
                    EMMessage messge = mConversation.getAllMessages().get(i);
                    Log.i("获取的第一条消息",messge.getStringAttribute("nickname", "")+"/"+messge.getStringAttribute("imgurl",
                            SharedPreferencedUtils.getString(MessageDetailsActivity.this, "avatar"))+messge.direct()+"/"+
                            messge.getFrom()+"/"+messge.getUserName()+"/"
                            +SharedPreferencedUtils.getString(MessageDetailsActivity.this,"huanxinname"));
                    avatar=messge.getStringAttribute("imgurl",
                            SharedPreferencedUtils.getString(MessageDetailsActivity.this, "avatar"));
                     usernickname=messge.getStringAttribute("nickname", "");
                    // EMTextMessageBody body = (EMTextMessageBody) messge.getBody();
                    // 将消息内容和时间显示出来
                    if (messge.direct()==EMMessage.Direct.SEND){//自己发的

                        isme=1;
                    }else {
                        isme=2;
                    }
                    if (messge.getType()== EMMessage.Type.IMAGE){
                        EMImageMessageBody imageMessageBody= (EMImageMessageBody) messge.getBody();
                        Log.i("接受的消息",imageMessageBody.getThumbnailUrl()
                                +"分割"+imageMessageBody.getLocalUrl()
                                +"分割"+imageMessageBody.thumbnailLocalPath());
                        final MessageList messageList = new MessageList();
                        messageList.setMsgId(uid);
                        messageList.setContent("");
                        messageList.setTalker(SharedPreferencedUtils.getString(MessageDetailsActivity.this, "token"));
                        messageList.setCreateTime(System.currentTimeMillis());
                        messageList.setIsSend(0);
                        messageList.setIsme(isme+"");
                        messageList.setNickname(usernickname);
                        messageList.setHuanxin_name(messge.getFrom());
                        messageList.setImg(imageMessageBody.thumbnailLocalPath());
                        imglist.add(imageMessageBody.thumbnailLocalPath());
                        messageList.setAdditional(System.currentTimeMillis() + "");
                        messageList.setAvatar(avatar);
                        messageLists.add(messageList);
                        mRecyclerView.smoothScrollToPosition(messageLists.size()-1);
                    }else if (messge.getType()== EMMessage.Type.TXT){
                        EMTextMessageBody body = (EMTextMessageBody) messge.getBody();
                        tt=body.getMessage();
                        Log.i("接受的消息",body.getMessage());
                        // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                        MessageList messageList = new MessageList();
                        messageList.setMsgId(uid);
                        messageList.setContent(tt);
                        messageList.setTalker(SharedPreferencedUtils.getString(MessageDetailsActivity.this, "token"));
                        messageList.setCreateTime(System.currentTimeMillis());
                        messageList.setIsSend(0);
                        messageList.setImg(" ");
                        messageList.setNickname(usernickname);
                        messageList.setHuanxin_name(messge.getFrom());
                        messageList.setIsme(isme+"");
                        messageList.setAdditional(System.currentTimeMillis() + "");
                        messageList.setAvatar(avatar);
                        messageLists.add(messageList);
                        mRecyclerView.smoothScrollToPosition(messageLists.size()-1);
                    }
                }
                android.os.Message msg = mHandler.obtainMessage();
                msg.what =3;
                mHandler.sendMessage(msg);
                Log.i("消息","他俩是相等的");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        EMClient.getInstance().chatManager().addMessageListener(msgListener);//添加消息监听
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 移除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
    /**
     * 初始化表情面板
     */
    public void initEmotion() {
        mEmotionKeyboard = EmotionKeyboard.with(this)
                .setEmotionView(findViewById(R.id.ll_emotion_layout))//绑定表情面板
                .bindToContent(mRecyclerView)//绑定内容view
                .bindToEditText(!isBindToBarEditText ? ((EditText) content) : ((EditText) findViewById(R.id.edit_message)))//判断绑定那种EditView
                .bindToEmotionButton(findViewById(R.id.img_noun_expressio))//绑定表情按钮
                .build();
        //创建全局监听
        GlobalOnItemClickManagerUtils globalOnItemClickManager = GlobalOnItemClickManagerUtils.getInstance(this);
        if (isBindToBarEditText) {
            //绑定当前Bar的编辑框
            globalOnItemClickManager.attachToEditText(editMessage);
        } else {
            // false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
            globalOnItemClickManager.attachToEditText((EditText) content);
            mEmotionKeyboard.bindToEditText((EditText) content);
        }
    }

    /**
     * 数据操作,这里是测试数据，请自行更换数据
     */
    protected void initDatas() {
        replaceFragment();
        List<ImageModel> list = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            if (i == 0) {
                ImageModel model1 = new ImageModel();
                model1.icon = getResources().getDrawable(R.mipmap.sos_icon);
                model1.flag = "经典笑脸";
                model1.isSelected = true;
                list.add(model1);
            } else {
                ImageModel model = new ImageModel();
                model.icon = getResources().getDrawable(R.mipmap.sos_icon);
                model.flag = "其他笑脸" + i;
                model.isSelected = false;
                list.add(model);
            }
        }
        //记录底部默认选中第一个
        CurrentPosition = 0;
        // SharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);
    }

    private void replaceFragment() {
        //创建fragment的工厂类
        FragmentFactory factory = FragmentFactory.getSingleFactoryInstance();
        //创建修改实例
        EmotiomComplateFragment f1 = (EmotiomComplateFragment) factory.getFragment(EmotionUtils.EMOTION_CLASSIC_TYPE);
        fragments.add(f1);
        Bundle b = null;
        for (int i = 0; i < 1; i++) {
            b = new Bundle();
            b.putString("Interge", "Fragment-" + i);
            Fragment1 fg = Fragment1.newInstance(Fragment1.class, b);
            fragments.add(fg);
        }
        NoHorizontalScrollerVPAdapter adapter = new NoHorizontalScrollerVPAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }


    @Override
    public void showInfo(String string) {
        try {
            Log.e("发送消息", string);
            JSONObject object = new JSONObject(string);
            JSONObject status = object.optJSONObject("status");
            if (status.optInt("code") == 1000) {
                if (loadtype==1){
                    editMessage.setText("");
                    String additional = object.getJSONObject("result").getString("additional");
                    Message message = mMessageDao.queryBuilder()
                            .where(MessageDao.Properties.Additional.eq(additional)).unique();
                    message.setIsSend(1);
                    mMessageDao.update(message);
                }else if (loadtype==2){
                    Log.i("聊天记录",string);
                    Gson gson=new Gson();
                    MsgRecordList msgRecordList=gson.fromJson(string,MsgRecordList.class);
                    msglist.addAll(msgRecordList.getResult());
                    Collections.reverse(msglist);//倒叙
                    for (int i=0;i<msglist.size();i++){
                        Log.i("倒叙",i+"");
                        if (msglist.get(i).getU_id().equals(SharedPreferencedUtils.
                                getString(MessageDetailsActivity.this,"uid"))){//自己发的
                            isme=1;
                        }else {
                            isme=2;
                        }
                        if (msglist.get(i).getType().equals("2")){
                            //图片
                            Log.i("后台消息记录",msglist.get(i).getContent()+"/"
                                    +msglist.get(i).getU_id()+"/"+msglist.get(i).getId());
                            final MessageList messageList = new MessageList();
                            messageList.setMsgId(msglist.get(i).getU_id());
                            messageList.setContent("");
                            messageList.setTalker(SharedPreferencedUtils.getString(MessageDetailsActivity.this, "token"));
                            messageList.setCreateTime(System.currentTimeMillis());
                            messageList.setIsSend(0);
                            messageList.setIsme(isme+"");
                            if (msglist.get(i).getName().equals("")){
                                messageList.setNickname(msglist.get(i).getNick_name());
                            }else {
                                messageList.setNickname(msglist.get(i).getName());
                            }
                            messageList.setHuanxin_name(msglist.get(i).getHuanxin_id());
                            messageList.setImg(msglist.get(i).getContent());
                            imglist.add(msglist.get(i).getContent());
                            messageList.setAdditional(System.currentTimeMillis() + "");
                            messageList.setAvatar(msglist.get(i).getAvatar());
                            messageLists.add(messageList);
                        }else if (msglist.get(i).getType().equals("1")){
                            //文字
                            // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                            MessageList messageList = new MessageList();
                            messageList.setMsgId(msglist.get(i).getU_id());
                            messageList.setContent(msglist.get(i).getContent());
                            messageList.setTalker(SharedPreferencedUtils.getString(MessageDetailsActivity.this, "token"));
                            messageList.setCreateTime(System.currentTimeMillis());
                            messageList.setIsSend(0);
                            messageList.setImg(" ");
                            if (msglist.get(i).getName().equals("")){
                                messageList.setNickname(msglist.get(i).getNick_name());
                            }else {
                                messageList.setNickname(msglist.get(i).getName());
                            }
                            messageList.setHuanxin_name(msglist.get(i).getHuanxin_id());
                            messageList.setIsme(isme+"");
                            messageList.setAdditional(System.currentTimeMillis() + "");
                            messageList.setAvatar(msglist.get(i).getAvatar());
                            messageLists.add(messageList);
                        }
                    }
                    android.os.Message msg = mHandler.obtainMessage();
                    msg.what =3;
                    mHandler.sendMessage(msg);
                    Log.i("消息","他俩是相等的");
                }else if (loadtype==3){
                    Log.i("聊天记录上拉",string);
                    Gson gson=new Gson();
                    MsgRecordList msgRecordList=gson.fromJson(string,MsgRecordList.class);
                    List<MsgRecordList.ResultBean> morelist=new ArrayList<>();
                    morelist.addAll(msgRecordList.getResult());
                    Collections.reverse(morelist);
                    morelist.addAll(msglist);
                    msglist.clear();
                    msglist.addAll(morelist);
                    for (int i=0;i<msglist.size();i++){
                        Log.i("倒叙",i+"");
                        if (msglist.get(i).getU_id().equals(SharedPreferencedUtils.
                                getString(MessageDetailsActivity.this,"uid"))){//自己发的
                            isme=1;
                        }else {
                            isme=2;
                        }
                        if (msglist.get(i).getType().equals("2")){
                            //图片
                            Log.i("后台消息记录",msglist.get(i).getContent()+"/"
                                    +msglist.get(i).getU_id()+"/"+msglist.get(i).getId());
                            final MessageList messageList = new MessageList();
                            messageList.setMsgId(msglist.get(i).getU_id());
                            messageList.setContent("");
                            messageList.setTalker(SharedPreferencedUtils.getString(MessageDetailsActivity.this, "token"));
                            messageList.setCreateTime(System.currentTimeMillis());
                            messageList.setIsSend(0);
                            messageList.setIsme(isme+"");
                            if (msglist.get(i).getName().equals("")){
                                messageList.setNickname(msglist.get(i).getNick_name());
                            }else {
                                messageList.setNickname(msglist.get(i).getName());
                            }
                            messageList.setHuanxin_name(msglist.get(i).getHuanxin_id());
                            messageList.setImg(msglist.get(i).getContent());
                            imglist.add(msglist.get(i).getContent());
                            messageList.setAdditional(System.currentTimeMillis() + "");
                            messageList.setAvatar(msglist.get(i).getAvatar());
                            messageLists.add(messageList);
                        }else if (msglist.get(i).getType().equals("1")){
                            //文字
                            // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                            MessageList messageList = new MessageList();
                            messageList.setMsgId(msglist.get(i).getU_id());
                            messageList.setContent(msglist.get(i).getContent());
                            messageList.setTalker(SharedPreferencedUtils.getString(MessageDetailsActivity.this, "token"));
                            messageList.setCreateTime(System.currentTimeMillis());
                            messageList.setIsSend(0);
                            messageList.setImg(" ");
                            if (msglist.get(i).getName().equals("")){
                                messageList.setNickname(msglist.get(i).getNick_name());
                            }else {
                                messageList.setNickname(msglist.get(i).getName());
                            }
                            messageList.setHuanxin_name(msglist.get(i).getHuanxin_id());
                            messageList.setIsme(isme+"");
                            messageList.setAdditional(System.currentTimeMillis() + "");
                            messageList.setAvatar(msglist.get(i).getAvatar());
                            messageLists.add(messageList);
                        }
                    }
                    android.os.Message msg = mHandler.obtainMessage();
                    msg.what =4;
                    mHandler.sendMessage(msg);
                }

            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showImager(Bitmap imageView) {
        Log.i("必填bitmap",imageView+"");

    }

    @Override
    public void showErrMsg(String msg) {

    }

    /**
     * 发送消息
     */
    private void initSendMsg(String res_name, String res_id, String type, String content, String file_name, String additional) {
        loadtype=1;
        Log.i("res_id",res_id+"/"+res_name+"/"+type+"/"+content);
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        encryptapMap.put("res_name", res_name); //发送给好友时为user，发送到群聊时为group
        encryptapMap.put("res_id", res_id); //发送给好友时为用户ID，发送到群聊时为群聊ID
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("res_name", res_name);
        map.put("res_id", res_id);
        map.put("type", type);//1，为普通消息2为图片消息
        map.put("content", content);
        map.put("file_name", file_name);
        map.put("additional", additional);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadSendMsg(map);
    }
    @OnClick({R.id.img_picture, R.id.txt_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_picture:
                pickImage();
                break;
            case R.id.txt_ok:
                if (editMessage.getText().toString().equals("")){
                    HelpUtils.warning(MessageDetailsActivity.this,"消息不能为空");
                }else {
                 mEmotionKeyboard.hideEmotionLayout(false);//隐藏表情布局
                final long time = System.currentTimeMillis();
                MessageList messageList = new MessageList();
                messageList.setMsgId(uid);
                messageList.setContent(editMessage.getText().toString());
                messageList.setTalker(SharedPreferencedUtils.getString(this, "token"));
                messageList.setCreateTime(time);
                messageList.setIsSend(0);
                messageList.setIsme("1");
                messageList.setImg(" ");
                messageList.setNickname("");
                messageList.setAdditional(time + "");
                 messageList.setHuanxin_name(SharedPreferencedUtils.getString(MessageDetailsActivity.this,"huanxinname"));
                messageList.setAvatar(SharedPreferencedUtils.getString(this, "avatar"));
                messageLists.add(messageList);
                mAdapter.setData(messageLists);
                    mRecyclerView.smoothScrollToPosition(messageLists.size()-1);
                    EMMessage messages = EMMessage.createTxtSendMessage
                            (editMessage.getText().toString(),//需要发送的消息文本
                                    huanxin_name);//对方用户的环信name
                    // 为消息设置回调
                    //如果是群聊，设置chattype，默认是单聊
                    if (chattype.equals("group")){
                        messages.setChatType(EMMessage.ChatType.GroupChat);
                    }
                  // 设置自定义扩展字段
                    messages.setAttribute("imgurl",
                            SharedPreferencedUtils.getString(MessageDetailsActivity.this, "avatar"));
                    messages.setAttribute("nickname",
                            SharedPreferencedUtils.getString(MessageDetailsActivity.this,
                            "nickname"));
                    Log.i("设置扩展字段",SharedPreferencedUtils.getString(MessageDetailsActivity.this,
                            "nickname"));
                    EMClient.getInstance().chatManager().sendMessage(messages);
                    messages.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            // 消息发送成功，打印下日志，正常操作应该去刷新ui
                            Log.i("lzan13", "send message on success");
                            initSendMsg(chattype, uid, "1", editMessage.getText().toString(), pathImg, time + "");
                        }
                        @Override
                        public void onError(int i, String s) {
                            // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                            HelpUtils.warning(MessageDetailsActivity.this,"消息发送失败");
                        }
                        @Override
                        public void onProgress(int i, String s) {
                            // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
                        }
                    });

                }
                break;
        }
    }


    private void pickImage() {
        FunctionOptions optionss = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE)
                .setCompress(true)//是否压缩
                .setGrade(Luban.THIRD_GEAR)
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(1)
                .setThemeStyle(ContextCompat.getColor(MessageDetailsActivity.this,R.color.bule))
                //.setSelectMedia(selectMedia) // 已选图片，传入在次进去可选中，不能传入网络图片
                // .setStatusBar(R.drawable.bg_gradient_color)//状态栏颜色
                .setImageSpanCount(3) // 每行个数
                .create();
        // PictureConfig.getInstance().init(optionss);
        PictureConfig.getInstance().init(optionss).openPhoto(MessageDetailsActivity.this,
                new PictureConfig.OnSelectResultCallback() {
                    @Override
                    public void onSelectSuccess(List<LocalMedia> list) {
                        selectMedia = list;
                        final long time = System.currentTimeMillis();
                        if (selectMedia.size()>0){
                            pathImage = String.valueOf(Utils.readStream(selectMedia.get(0).getPath()));
                            pathImg =selectMedia.get(0).getPath();
                            MessageList messageList = new MessageList();
                            messageList.setMsgId(uid);
                            messageList.setContent(" ");
                            messageList.setTalker(SharedPreferencedUtils.getString(MessageDetailsActivity.this, "token"));
                            messageList.setCreateTime(time);
                            messageList.setIsSend(0);
                            messageList.setNickname("");
                            messageList.setIsme("1");
                            messageList.setImg(pathImg);
                            imglist.add(pathImage);
                            messageList.setAdditional(1 + "");
                            messageList.setHuanxin_name(SharedPreferencedUtils.getString(MessageDetailsActivity.this,"huanxinname"));
                            messageList.setAvatar(SharedPreferencedUtils.getString(MessageDetailsActivity.this, "avatar"));
                            messageLists.add(messageList);
                            //   linearLayoutManager.setStackFromEnd(true);
                            mAdapter.setData(messageLists);
                            mRecyclerView.smoothScrollToPosition(messageLists.size()-1);
                            //mAdapter.notifyDataSetChanged();
                            final File file = new File(selectMedia.get(0).getPath());
                            // Toast.makeText(this, file.getPath(), Toast.LENGTH_SHORT).show();
                            Log.e("sss",selectMedia.get(0).getPath());
                            mRecyclerView.smoothScrollToPosition(messageLists.size() - 1);
                            Bitmap bitmap = HelpUtils.compressBitmap(selectMedia.get(0).getPath(), 600, 1024, 30);
                            final String paths = Utils.bitmapToBase64(bitmap);

                            //走sdk方式发送
                            EMMessage message=EMMessage.createImageSendMessage(selectMedia.get(0).getPath(), false, huanxin_name);
                            message.setAttribute("imgurl",
                                    SharedPreferencedUtils.getString(MessageDetailsActivity.this, "avatar"));
                            message.setAttribute("nickname",
                                    SharedPreferencedUtils.getString(MessageDetailsActivity.this,
                                            "nickname"));
                            //如果是群聊，设置chattype，默认是单聊
                            if (chattype.equals("group")){
                                message.setChatType(EMMessage.ChatType.GroupChat);
                            }
                            EMClient.getInstance().chatManager().sendMessage(message);
                            message.setMessageStatusCallback(new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    Log.i("发送消息==", "send message on success");
                                    initSendMsg(chattype,uid, "2", paths, selectMedia.get(0).getPath(), time + "");
                                }
                                @Override
                                public void onError(int i, String s) {
                                    Log.i("lzan13", "send message on error");
                                    HelpUtils.warning(MessageDetailsActivity.this,"消息发送失败");
                                }
                                @Override
                                public void onProgress(int i, String s) {
                                    Log.i("lzan13", i+"");
                                }
                            });
                            //initAddAppraise("user", "2", Utils.bitmapToBase64(bitmap));
                        }
                    }
                    @Override
                    public void onSelectSuccess(LocalMedia localMedia) {
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    private  String tt="";
    EMMessageListener msgListener=new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            Log.i("收到的消息",list.size()+"");
            // 循环遍历当前收到的消息
            for (EMMessage message : list) {
                mConversation = EMClient.getInstance().chatManager().getConversation(huanxin_name, null, true);

                mConversation.markMessageAsRead(message.getMsgId());
                Log.i("最后一条消息",message.getChatType() + "/" + message.getType()+
                        "/"+SharedPreferencedUtils.getString(MessageDetailsActivity.this,"huanxinname")
                        +"/"+message.getFrom()+"/"+message.getTo());
    /*            PersonalDao personalDao = PersonalDaoManager.getInstance().getNewSession().getPersonalDao();
                List<Personal> llist = personalDao.queryBuilder().where(
                    PersonalDao.Properties.Huanxin_id.eq(message.getFrom())).list();*/
                  avatar=message.getStringAttribute("imgurl",
                          SharedPreferencedUtils.getString(MessageDetailsActivity.this, "avatar"));
                  usernickname=message.getStringAttribute("nickname", "  ");

                // 群组消息
                if (message.direct()==EMMessage.Direct.SEND){
                    //最后一条消息是自己发的展示到右边
                    isme=1;
                }else {
                    isme=2;
                }
                String username="";
                if (message.getChatType() == EMMessage.ChatType.GroupChat ||
                        message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    username = message.getTo();
                } else {
                    // 单聊消息
                    username = message.getFrom();
                }
                if (username.equals(huanxin_name)&&
                        !SharedPreferencedUtils.getString(MessageDetailsActivity.this,"huanxinname")
                                .equals(message.getFrom())) {
                    android.os.Message msg = mHandler.obtainMessage();
                    msg.what = 2;
                    msg.obj = message;
                    mHandler.sendMessage(msg);
                    // 设置消息为已读
                     mConversation.markMessageAsRead(message.getMsgId());
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    private boolean showImageView(final String thumbernailPath,
                                  final ImageView iv,
                                  final String localFullSizePath,
                                  final EMMessage message) {
        // first check if the thumbnail image already loaded into cache
        final EMImageMessageBody imgBody= (EMImageMessageBody) message.getBody();
        Log.i("enen--",localFullSizePath+"?");
        Log.i("enen==",imgBody.thumbnailLocalPath()+"?");
        Bitmap bitmap = EaseImageCache.getInstance().get(thumbernailPath);
        if (bitmap != null) {
            Log.i("enen",thumbernailPath+"?");
            Log.i("enen","0");
            // thumbnail image is already loaded, reuse the drawable
            iv.setImageBitmap(bitmap);
            return true;
        } else {
            Log.i("enen", "1");
            new AsyncTask<Object, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Object... args) {
                    File file = new File(thumbernailPath);
                    Log.i("enen", "2");
                    if (file.exists()) {
                        Log.i("enen", "3");
                        return EaseImageUtils.decodeScaleImage(thumbernailPath, 300, 300);
                    } else if (new File(imgBody.thumbnailLocalPath()).exists()) {
                        Log.i("enen", "4");
                        return EaseImageUtils.decodeScaleImage(imgBody.thumbnailLocalPath(), 300, 300);
                    } else {
                        Log.i("enen", "5");
                        if (message.direct() == EMMessage.Direct.SEND) {
                            if (localFullSizePath != null && new File(localFullSizePath).exists()) {
                                return EaseImageUtils.decodeScaleImage(localFullSizePath, 300, 300);
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                }
                protected void onPostExecute(Bitmap image) {
                    if (image != null) {
                        iv.setImageBitmap(image);
                        Log.i("enen", "6");
                        EaseImageCache.getInstance().put(thumbernailPath, image);
                    } else {
                        if (message.status() == EMMessage.Status.FAIL) {
                            if (HelpUtils.isNetworkAvailable(MessageDetailsActivity.this)) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("enen", "7");
                                        EMClient.getInstance().chatManager().downloadThumbnail(message);
                                    }
                                }).start();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }.execute();

            return true;
        }

    }

    @Override
    public void onRefresh() {
        //下拉刷新
        android.os.Message message=mHandler.obtainMessage();
        message.what=1;
        mHandler.sendMessage(message);
    }

    /*
    * 获取会话列表
    * */
    private void msgrecordlist(String token,//token
                               String res_name,//user 或者 group
                               String res_id//环信id
                               ){
        loadtype=2;
        Map<String,String> signmap=Utils.getSignParams(MessageDetailsActivity.this,token);
        Map<String,String> map=Utils.getMap(MessageDetailsActivity.this,token);
        map.put("res_name",res_name);
        map.put("res_id",res_id);
        map.put("sign",Utils.getMd5StringMap(signmap));
        friendPresenter.msg_recodrlist(map);
    }

    /*
   * 获取更多会话列表
   * */
    private void msgrecordmorelist(String token,//token
                               String res_name,//user 或者 group
                               String res_id,//环信id
                                   String id
    ){
        loadtype=3;
        Map<String,String> signmap=Utils.getSignParams(MessageDetailsActivity.this,token);
        Map<String,String> map=Utils.getMap(MessageDetailsActivity.this,token);
        map.put("res_name",res_name);
        map.put("res_id",res_id);
        map.put("id",id);
        map.put("sign",Utils.getMd5StringMap(signmap));
        friendPresenter.msg_recodrlist(map);
    }

}
