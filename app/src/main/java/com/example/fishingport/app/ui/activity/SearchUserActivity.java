package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.SearchUserlist;
import com.example.fishingport.app.presenter.FriendPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.view.GlideCircleTransform;
import com.example.fishingport.app.view.IconCenterEditText;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* 搜索好友
* */
public class SearchUserActivity extends
     BaseAppCompatActivity implements FriendConstract.view{
    @BindView(R.id.icet_search)
    IconCenterEditText icet_search;
    private FriendPresenter friendPresenter;
    @BindView(R.id.mRecyclerView)
     RecyclerView mRecyclerView;
    @BindView(R.id.noview)
    TextView noview;
    private List<SearchUserlist.ResultBean>  list=new ArrayList<>();
    private BaseRecyclerAdapter<SearchUserlist.ResultBean> beanBaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBarTitle("搜索好友");
        intitview();
        showAdapter();
    }

    private void showAdapter() {
        beanBaseRecyclerAdapter=new BaseRecyclerAdapter<SearchUserlist.ResultBean>
                (this,null,R.layout.item_maillist) {
            @Override
            protected void convert(BaseViewHolder helper, final SearchUserlist.ResultBean item) {
                ImageView image= (ImageView) helper.getConvertView().findViewById(R.id.ivAvatar);
                Glide.with(mContext).load(item.getAvatar()).
                        transform(new GlideCircleTransform(mContext)).into(image);
                helper.setText(R.id.tvCity,item.getTitle());
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            Intent i = new Intent(mContext, FriendInfoActivity.class);
                            i.putExtra("type", 1);
                            i.putExtra("uid", item.getId() + "");
                            i.putExtra("huanxin_name", item.getHuanxin_id());
                            i.putExtra("avatar", item.getAvatar());//头像
                            i.putExtra("nickname", item.getTitle());//昵称
                            mContext.startActivity(i);
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        beanBaseRecyclerAdapter.openLoadAnimation(false);
        mRecyclerView.setAdapter(beanBaseRecyclerAdapter);
    }

    private void intitview() {
        friendPresenter=new FriendPresenter(this);
        icet_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                list.clear();
                im_search(SharedPreferencedUtils.getString(SearchUserActivity.this,"token"),
                        "user",s.toString());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_user;
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
        Log.i("搜索的结果",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            if (status.optInt("code")==1000){
                Gson gson=new Gson();
                SearchUserlist searchlist=gson.fromJson(string,SearchUserlist.class);
                list.addAll(searchlist.getResult());
                if (list.size()>0){
                 noview.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    beanBaseRecyclerAdapter.setData(list);
                }else {
                    noview.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private  void im_search(String token,String res_name,String title){
        Map<String,String> signmap= Utils.getSignParams(SearchUserActivity.this,token);
        Map<String,String> map=Utils.getMap(SearchUserActivity.this,token);
        map.put("res_name",res_name);
        map.put("title",title);
        map.put("sign",Utils.getMd5StringMap(signmap));
        friendPresenter.im_search(map);
    }
}
