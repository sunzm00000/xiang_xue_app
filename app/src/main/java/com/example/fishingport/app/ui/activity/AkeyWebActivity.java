package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.StatusBarCompat;
import com.example.fishingport.app.tools.WifiAdmin;
import com.example.fishingport.app.widget.CustomVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
/*
* 一键上网页面
*
* */

public class AkeyWebActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.akeybtn)
    TextView akeybtn;//一键上网
    @BindView(R.id.skip)
    TextView skip;//跳过，到登录页面
    WifiAdmin wifiAdmin;
    private CustomVideoView videoview;
    private WifiManager mWifiManager;//wifi管理者，用于获取wifi连接时的信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akey_web);
        ButterKnife.bind(AkeyWebActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarCompat.copat(this);
        }
        akeybtn.setOnClickListener(this);
        skip.setOnClickListener(this);
        wifiAdmin = new WifiAdmin(AkeyWebActivity.this);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(AkeyWebActivity.this.WIFI_SERVICE);

        initView();
    }

    private void initView() {
        if (wifiAdmin.checkState()==1){//当wifi没有打开的时候
            wifiAdmin.openWifi();
        }
        //加载视频资源控件
        videoview = (CustomVideoView) findViewById(R.id.videoview);
        //设置播放加载路径
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vidd));
        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });
    }
    //返回重启加载
    @Override
    protected void onRestart() {
        initView();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //当连接的是港口wifi时跳到登录页
        if (mWifiManager.getConnectionInfo().getSSID().toString().equals("\"APP-TEST\"")) {
            //是港口wifi
            if (SharedPreferencedUtils.getString(AkeyWebActivity.this,"token")==null) {
                startActivity(new Intent(AkeyWebActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(AkeyWebActivity.this, MainActivity.class));
                finish();
            }
        }

    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        videoview.stopPlayback();
        super.onStop();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.akeybtn://一键上网
                Intent connectedIntent=new Intent(AkeyWebActivity.this,ConnectedDeviceActivity.class);
                startActivity(connectedIntent);
                break;
            case  R.id.skip:
                if (SharedPreferencedUtils.getString(AkeyWebActivity.this,"token")!=null){
                    Intent intent=new Intent(AkeyWebActivity.this, MainActivity.class);
                    intent.putExtra("type",0);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(AkeyWebActivity.this, LoginActivity.class));
                }
                finish();
                break;
        }
    }
}
