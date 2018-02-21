package com.example.fishingport.app.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
 * Created by wushixin on 2017/4/25.
 * 好友验证
 */

public class FriendsVerifyActivity extends BaseAppCompatActivity implements FriendConstract.view{
    @BindView(R.id.edit_friends_verify)
    EditText editFriendsVerify;
    private FriendPresenter friendPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friends_verify;
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
        setTextRight("发送").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            initAddFirend(getIntent().getStringExtra("uid"),editFriendsVerify.getText().toString());
            }
        });
        friendPresenter=new FriendPresenter(this);
    }

    /**
     * 添加渔友
     */
    private void initAddFirend(String friend_id,String content) {

        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
         encryptapMap.put("friend_id",friend_id);

        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("friend_id", friend_id);
        map.put("content", content);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadAddFriend(map);
    }

    @Override
    public void showInfo(String string) {
        Log.i("添加好友",string.toString());

        try {
            JSONObject jsonObject=new JSONObject(string);
            if (jsonObject.getJSONObject("status").getInt("code")==1000){
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                FriendInfoActivity.activity.finish();
                finish();
            }else {
                HelpUtils.warning(this,jsonObject.getJSONObject("status").optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showErrMsg(String msg) {

    }
}
