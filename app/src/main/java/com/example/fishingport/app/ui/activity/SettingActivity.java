package com.example.fishingport.app.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.tools.CacheDataManager;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.WeiboDialogUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/14.
 */

public class SettingActivity extends BaseAppCompatActivity {
    @BindView(R.id.cache)
    TextView cache;//显示缓存
    @BindView(R.id.rlcache)
    RelativeLayout rlcache;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.about_me)
    RelativeLayout about_me;//关于我们
    private Dialog mWeiboDialog;
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    new Thread(new clearCache()).start();

                 break;
            }
        }

    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBarTitle("设置");
        try {
            if (CacheDataManager.getTotalCacheSize(this).equals("0.0Byte")){
                cache.setText("0KB");
            }else {
                cache.setText(CacheDataManager.getTotalCacheSize(this));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.rlcache, R.id.btn,R.id.about_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case  R.id.about_me://关于我们
                startActivity(new Intent(SettingActivity.this,AboutmeActivity.class));
                break;
            case R.id.rlcache:
                try {
                    if (CacheDataManager.getTotalCacheSize(SettingActivity.this).equals("0.0Byte")){
                          HelpUtils.warning(SettingActivity.this,"暂不需要清理");
                    }else {
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SettingActivity.this, "加载中...");
                        handler.sendEmptyMessageDelayed(1, 2000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn:
                SharedPreferencedUtils.setString(SettingActivity.this,"token",null);
                SharedPreferencedUtils.setString(SettingActivity.this, "mobile","");
                SharedPreferencedUtils.setString(SettingActivity.this, "avatar","");
                SharedPreferencedUtils.setString(SettingActivity.this, "nick_name","");
                SharedPreferencedUtils.setString(SettingActivity.this, "uid", "");
                SharedPreferencedUtils.setString(SettingActivity.this, "CorporationId", "");
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(SettingActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                    }

                    @Override
                    public void onError(int i, String s) {
                    }
                    @Override
                    public void onProgress(int i, String s) {
                    }
                });
                break;
        }
    }
    class clearCache implements Runnable {

        @Override

        public void run() {
            try {
                 CacheDataManager.clearAllCache(SettingActivity.this);
                if (CacheDataManager.getTotalCacheSize(SettingActivity.this).startsWith("0")) {
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    HelpUtils.success(SettingActivity.this,"清理完成");
                        cache.setText(0 + "KB");
                }

            } catch (Exception e) {
                return;

            }
        }
    }

}
