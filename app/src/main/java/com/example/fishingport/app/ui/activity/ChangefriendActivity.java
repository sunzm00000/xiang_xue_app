package com.example.fishingport.app.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FishingCircleConstract;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.presenter.FishingCirclePresenter;
import com.example.fishingport.app.presenter.FriendPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangefriendActivity extends BaseAppCompatActivity implements FriendConstract.view {
    private FriendPresenter friendpresenter;
    @BindView(R.id.edit_title)
    EditText edit_title;
    private  String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        friendpresenter=new FriendPresenter(this);
        setToolBarTitle("修改备注");
        initview();
    }
    private void initview() {
        uid=getIntent().getStringExtra("uid");
        setTextRight("完成").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=edit_title.getText()+"";
                if (name.equals("")){
                    HelpUtils.warning(ChangefriendActivity.this,"备注名不能为空");
                }
                else {
                    update_name(SharedPreferencedUtils.getString(ChangefriendActivity.this,"token"),uid,name);
                }
            }
        });
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_changefriend;
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
        Log.i("修改备注",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            if (status.optInt("code")==1000){
                finish();
            }else
            {
                HelpUtils.warning(ChangefriendActivity.this,status.optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public  void  update_name(String token,String friend_id,String name){
        Map<String,String> signmap= Utils.getSignParams(ChangefriendActivity.this,token);
        signmap.put("friend_id",friend_id);
        Map<String,String> map=Utils.getMap(ChangefriendActivity.this,token);
        map.put("friend_id",friend_id);
        map.put("name",name);
        map.put("sign",Utils.getMd5StringMap(signmap));
        friendpresenter.update_name(map);
    }
}
