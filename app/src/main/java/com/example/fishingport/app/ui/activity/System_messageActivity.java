package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.SOSConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.NoticeList;
import com.example.fishingport.app.presenter.SOSPresenter;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
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
/*
* 系统通知列表
* */
public class System_messageActivity extends BaseAppCompatActivity implements SOSConstract.view{
   @BindView(R.id.recsystem_message)
    RecyclerView recsystem_message;//
    @BindView(R.id.noview)
    LinearLayout noview;
    private SOSPresenter sosPresenter;
    BaseRecyclerAdapter<NoticeList.ResultBean> beanBaseRecyclerAdapter;
    private List<NoticeList.ResultBean> noticelists=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBarTitle("系统消息");
        sosPresenter=new SOSPresenter(this);
        noticelist(SharedPreferencedUtils.getString(System_messageActivity.this,"token"));
        showadapter();

    }

    private void showadapter() {
        beanBaseRecyclerAdapter=new BaseRecyclerAdapter<NoticeList.ResultBean>
                (this,null,R.layout.item_notice_view) {
            @Override
            protected void convert(BaseViewHolder helper, final NoticeList.ResultBean item) {
                helper.setText(R.id.title,item.getTitle());
                helper.setText(R.id.content, Html.fromHtml(item.getContent()));
                helper.setText(R.id.time,item.getAdd_time());
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(System_messageActivity.this,SystemMssageDetailsActivity.class);
                        intent.putExtra("id",item.getNotice_id()+"");
                        startActivity(intent);

                    }
                });
            }
        };
        recsystem_message.setLayoutManager(new LinearLayoutManager(this));
        beanBaseRecyclerAdapter.openLoadAnimation(false);
        recsystem_message.setAdapter(beanBaseRecyclerAdapter);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_system_message;
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
        Log.i("系统通知",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            if (status.optInt("code")==1000){
                Gson gson=new Gson();
                NoticeList noticeList=gson.fromJson(string,NoticeList.class);
                noticelists.addAll(noticeList.getResult());
                if (noticelists.size()>0){
                    recsystem_message.setVisibility(View.VISIBLE);
                    noview.setVisibility(View.GONE);
                    beanBaseRecyclerAdapter.setData(noticelists);
                }else {
                    noview.setVisibility(View.VISIBLE);
                    recsystem_message.setVisibility(View.GONE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showWifi(String string) {

    }

    private  void  noticelist(String token){
        Map<String, String> signmap= Utils.getSignParams(System_messageActivity.this,token);
        Map<String,String> map=Utils.getMap(System_messageActivity.this,token);
        map.put("type","1");
        map.put("sign",Utils.getMd5StringMap(signmap));
        sosPresenter.noticelist(map);
    }
}
