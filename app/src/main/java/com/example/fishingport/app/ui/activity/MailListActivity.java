package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fishingport.app.Dao.PersonalDaoManager;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.bean.Personal;
import com.example.fishingport.app.bean.PersonalDao;
import com.example.fishingport.app.model.MailListBean;
import com.example.fishingport.app.presenter.FriendPresenter;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.view.DividerItemDecoration;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wushixin on 2017/4/18.
 * 通讯录
 */
public class MailListActivity extends BaseAppCompatActivity implements FriendConstract.view{
    private static final String TAG = "zxt";
    private static final String INDEX_STRING_TOP = "↑";
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.indexBar)
    IndexBar indexBar;
    @BindView(R.id.tvSideBarHint)
    TextView tvSideBarHint;
    private MailListAdapter mAdapter;
    private LinearLayoutManager mManager;
    private List<MailListBean> mDatas = new ArrayList<>();
    private SuspensionDecoration mDecoration;
    private FriendPresenter friendPresenter;
    @BindView(R.id.layoutview)
    LinearLayout layoutview;
    @BindView(R.id.btn_search)
    RelativeLayout btn_search;//搜索好友
    private int loadtype=0;
    String num="0";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_maillist;
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
        new_friend(SharedPreferencedUtils.getString(MailListActivity.this,"token"));//新渔友数量
        setToolBarTitle("通讯录");
        Glide.with(this).load(R.mipmap.add_member_icon).into(getImgRight());
       // getImgRight()
        getImgRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //搜索好友
                startActivity(new Intent(MailListActivity.this,SearchUserActivity.class));
            }
        });
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));
        mAdapter = new MailListAdapter(this, mDatas);
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(this, mDatas));
        mDecoration.setColorTitleBg(Color.parseColor("#f7faff"));
        mDecoration.setColorTitleFont(Color.parseColor("#aebace"));
        //如果add两个，那么按照先后顺序，依次渲染。
        mRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        //indexbar初始化
        indexBar.setmPressedShowTextView(tvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
        //模拟线上加载数据
        //initDatas(getResources().getStringArray(R.array.provinces));
    }

    @Override
    protected void onResume() {
        super.onResume();

        new_friend(SharedPreferencedUtils.getString(MailListActivity.this,"token"));//新渔友数量

    }

    /**
     * 获取好友列表
     */
    private void initFirendList() {
        loadtype=1;
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadFriendList(map);
    }

    /*
    * 新渔友数量
    *
    * */
    private void new_friend(String token){
        loadtype=2;
        Map<String,String> signmap=Utils.getSignParams(this,token);
        Map<String,String> map=Utils.getMap(this,token);
        map.put("sign",Utils.getMd5StringMap(signmap));
        friendPresenter.new_friend(map);
    }

    @Override
    public void showInfo(String string) {
        Log.e("s",string);
        try {
            JSONObject jsonObject=new JSONObject(string);
            if (jsonObject.getJSONObject("status").getInt("code")==1000){
                if (loadtype==1){
                    mDatas = new ArrayList<>();
                    MailListBean mailListBean=new MailListBean();
                    mailListBean.setCity("新渔友");
                    mailListBean.setIcon(R.mipmap.news_member_icon);
                    mailListBean.setTop(true);
                    mailListBean.setBaseIndexTag(INDEX_STRING_TOP);
                    mDatas.add(mailListBean);
                    MailListBean mailListBeans=new MailListBean();
                    mailListBeans.setCity("群聊");
                    mailListBeans.setTop(true);
                    mailListBeans.setBaseIndexTag(INDEX_STRING_TOP);
                    mailListBeans.setIcon(R.mipmap.all_group_icon);
                    mDatas.add(mailListBeans);
                    JSONArray result=jsonObject.getJSONArray("result");
                    for (int i = 0; i <result.length() ; i++) {
                        JSONObject json=result.getJSONObject(i);
                        MailListBean cityBean = new MailListBean();
                        cityBean.setId(json.getInt("id"));
                        cityBean.setCity(json.getString("nick_name"));
                        cityBean.setAvatar(json.getString("avatar"));
                        cityBean.setHuanxin_name(json.getString("huanxin_name"));
                        mDatas.add(cityBean);
                    }
                    mAdapter.setDatas(mDatas,num);
                    mAdapter.notifyDataSetChanged();
                    indexBar.setmSourceDatas(mDatas)//设置数据
                            .invalidate();
                    mDecoration.setmDatas(mDatas);
                }else  if (loadtype==2){
                    JSONObject jsonObject1=jsonObject.optJSONObject("result");
                     num=jsonObject1.optString("num");
                    initFirendList();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void showErrMsg(String msg) {

    }
    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(MailListActivity.this).inflate(
                R.layout.message_pop_window, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        contentView.findViewById(R.id.txt_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发起群聊
                Intent intent=new Intent(MailListActivity.this,ChooseMemberActivity.class);
                intent.putExtra("type","1");//通讯录
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.txt_add_member).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加好友
                startActivity(new Intent(MailListActivity.this, AddMemberActivity.class));
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


        popupWindow.showAtLocation(layoutview, Gravity.TOP,0,0);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
    }


}
