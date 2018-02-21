package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.Dao.GroupbeanDaoManager;
import com.example.fishingport.app.Dao.PersonalDaoManager;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.bean.Groupbean;
import com.example.fishingport.app.bean.GroupbeanDao;
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
 * Created by wushixin on 2017/4/27.
 * 选择好友
 */

public class ChooseMemberActivity extends BaseAppCompatActivity implements FriendConstract.view{
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.indexBar)
    IndexBar indexBar;
    @BindView(R.id.tvSideBarHint)
    TextView tvSideBarHint;
    private ChooseMemberAdapter mAdapter;
    private LinearLayoutManager mManager;
    private List<MailListBean> mDatas = new ArrayList<>();
    private SuspensionDecoration mDecoration;
    private FriendPresenter friendPresenter;
    private int loadType=1;
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
        initFirendList();
        setToolBarTitle("选择联系人");
        setTextRight("确定").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("type").equals("2")){
                    //是从群成员跳过来的
                    Intent intent=new Intent();
                    intent.putExtra("user_ids",mAdapter.setUser_ids());
                    setResult(3,intent);
                    finish();
                }else {
                    initCreateGroup(mAdapter.setUser_ids());
                }
            }
        });
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));
        mAdapter = new ChooseMemberAdapter(this, mDatas);
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
    }

    @Override
    public void showInfo(String string) {
        Log.i("创建群聊",string);
        try {
            JSONObject jsonObject=new JSONObject(string);
            if (jsonObject.getJSONObject("status").getInt("code")==1000){
                if (loadType==1) {
                    mDatas = new ArrayList<>();
                    JSONArray result = jsonObject.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject json = result.getJSONObject(i);
                        MailListBean cityBean = new MailListBean();
                        cityBean.setId(json.getInt("id"));
                        cityBean.setCity(json.getString("nick_name"));
                        cityBean.setAvatar(json.getString("avatar"));
                        mDatas.add(cityBean);
                    }
                    mAdapter.setDatas(mDatas);
                    mAdapter.notifyDataSetChanged();
                    indexBar.setmSourceDatas(mDatas)//设置数据
                            .invalidate();
                    mDecoration.setmDatas(mDatas);
                } else {
                      JSONObject result=jsonObject.optJSONObject("result");
                      String group_id=result.optString("group_id");
                      String  huanxin_id=result.optString("huanxin_id");
                    String nicke_name=result.optString("nick_name");
                    GroupbeanDao groupbeanDao = GroupbeanDaoManager.getInstance().getNewSession().getGroupbeanDao();
                    List<Groupbean> list =groupbeanDao.queryBuilder().where(GroupbeanDao.Properties.Group_id.eq(group_id)).list();
                    if (list.size()==0){
                        Log.i("执行了","执行了把群聊id和环信群聊id");
                        Groupbean groupbean=new Groupbean();
                        groupbean.setGroup_id(group_id);
                        groupbean.setHuanxin_id(huanxin_id);
                        groupbean.setNicke_name(nicke_name);
                        groupbean.setId(null);
                        groupbeanDao.insert(groupbean);
                    }
                     Intent intent=new Intent(ChooseMemberActivity.this,MessageDetailsActivity.class);
                     intent.putExtra("type",1);
                     intent.putExtra("uid",group_id);
                     intent.putExtra("huanxin_name",huanxin_id);
                     intent.putExtra("chattype","group");
                     intent.putExtra("nickname","新建群聊");//群聊昵称
                     intent.putExtra("avatar","");
                     startActivity(intent);
                     finish();//创建群聊跳到群聊天界面，把这个页面取消

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showErrMsg(String msg) {

    }

    /**
     * 获取好友列表
     */
    private void initFirendList() {
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadFriendList(map);
    }

    /**
     * 创建群聊
     */
    private void initCreateGroup(String user_ids) {
        loadType=2;
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
         encryptapMap.put("user_ids",user_ids);
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("user_ids", user_ids);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadCreateGroup(map);
    }
}
