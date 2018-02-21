package com.example.fishingport.app.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.presenter.FriendPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wushixin on 2017/4/17.
 * 修改标题
 */

public class ModifyTitleActivity extends BaseAppCompatActivity implements FriendConstract.view{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_modify_title)
    TextView txtTitle;
    @BindView(R.id.edit_title)
    EditText editTitle;
    FriendPresenter friendPresenter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_title;
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
        setTextRight("完成").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getIntExtra("type", -1) == 0) {
                    initUpdateGroup(getIntent().getStringExtra("group_id"), editTitle.getText().toString(), "");
                } else {
                    initUpdateGroup(getIntent().getStringExtra("group_id"), "", editTitle.getText().toString());
                }
            }
        });
        if (getIntent().getIntExtra("type", -1) == 0) {
            setToolBarTitle("群名称");
            txtTitle.setText("群名称");
            editTitle.setHint("群名称");
        } else {
            setToolBarTitle("群介绍");
            txtTitle.setText("群介绍");
            editTitle.setHint("群介绍");
            editTitle.setLines(10);
        }



    }

    @Override
    public void showInfo(String string) {
        Log.i("群简介",string);
        try {
            JSONObject jsonObject=new JSONObject(string);
            if (jsonObject.getJSONObject("status").getInt("code")==1000){
                finish();
            }else {
                HelpUtils.success(ModifyTitleActivity.this,
                        jsonObject.getJSONObject("status").optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showErrMsg(String msg) {

    }

    /**
     * 修改群信息
     */
    private void initUpdateGroup(String group_id,String title,String card) {
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        encryptapMap.put("group_id", group_id);
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("group_id", group_id);
        map.put("title", title);
        map.put("card", card);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadUpdateGroup(map);
    }
}
