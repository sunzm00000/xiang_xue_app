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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.SearchGrouplist;
import com.example.fishingport.app.presenter.FriendPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.view.IconCenterEditText;
import com.example.fishingport.app.view.circularavatar.CircularImageView;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.fishingport.app.R.layout.item_grouplist;
import static com.example.fishingport.app.R.layout.layout_sign_dialog;

public class SearchGroupActiviy extends BaseAppCompatActivity implements FriendConstract.view{
    @BindView(R.id.icet_search)
    IconCenterEditText icet_search;
    private FriendPresenter friendPresenter;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;//
    @BindView(R.id.noview)
    TextView noview;//
    private List<SearchGrouplist.ResultBean> list=new ArrayList<>();
    private BaseRecyclerAdapter<SearchGrouplist.ResultBean> beanBaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBarTitle("搜索群聊");
        intitview();
        showAdapter();
    }

    private void showAdapter() {
        beanBaseRecyclerAdapter=new BaseRecyclerAdapter<SearchGrouplist.ResultBean>
                (this,null,R.layout.item_grouplist) {
            @Override
            protected void convert(BaseViewHolder helper, final SearchGrouplist.ResultBean item) {
                helper.setText(R.id.tvGroup,item.getTitle());
                final CircularImageView img_avatar= (CircularImageView)
                        helper.getConvertView().findViewById(R.id.img_avatar);
                final ArrayList<Bitmap> mBmps=new ArrayList<>();
                for (int i = 0; i <item.getAvatar().size() ; i++) {
                    Glide.with(mContext).load(item.getAvatar().get(i)).asBitmap().
                            into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    mBmps.add(resource);
                                    img_avatar.setImageBitmaps(mBmps);
                                }
                            });
                }

                helper.setOnClickListener(R.id.content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(SearchGroupActiviy.this,MessageDetailsActivity.class);
                        intent.putExtra("type",1);
                        intent.putExtra("uid",item.getId());
                        intent.putExtra("huanxin_name",item.getHuanxin_id());
                        intent.putExtra("chattype","group");
                        intent.putExtra("nickname",item.getTitle()+"");//群聊昵称
                        intent.putExtra("avatar","");
                        startActivity(intent);
                    }
                });


            }
        };
        beanBaseRecyclerAdapter.openLoadAnimation(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                im_search(SharedPreferencedUtils.getString(SearchGroupActiviy.this,"token"),
                        "group",s.toString());

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_group_activiy;
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
        Log.i("群聊搜索",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            if (status.optInt("code")==1000){
                Gson gson=new Gson();
                SearchGrouplist searchGrouplist=gson.fromJson(string,SearchGrouplist.class);
                list.addAll(searchGrouplist.getResult());
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
        Map<String,String> signmap= Utils.getSignParams(SearchGroupActiviy.this,token);
        Map<String,String> map=Utils.getMap(SearchGroupActiviy.this,token);
        map.put("res_name",res_name);
        map.put("title",title);
        map.put("sign",Utils.getMd5StringMap(signmap));
        friendPresenter.im_search(map);
    }
}
