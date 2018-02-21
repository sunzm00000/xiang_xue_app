package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.SOSConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.presenter.SOSPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.tools.WifiAdmin;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.phoneNumber;

/**
 * Created by wushixin on 2017/4/14.
 */

public class SOSActivity extends BaseAppCompatActivity implements View.OnClickListener,SOSConstract.view

{
    @BindView(R.id.centerImage)
    ImageView centerImage;
    @BindView(R.id.content)
    RippleBackground content;
     private long mExitTime=0;
    @BindView(R.id.locationgps)
    TextView locationgps;//定位的gps
    @BindView(R.id.layout_message)
    RelativeLayout layout_message;//
    private SOSPresenter sosPresenter;
    @BindView(R.id.layout_phone)
    RelativeLayout layout_phone;//拨打电话
    private WifiAdmin wifiAdmin;//wifi管理工具类

    private WifiManager mWifiManager;//wifi管理者，用于获取wifi连接时的信息


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sos;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected boolean isShowBacking() {
        if (getIntent().getStringExtra("type")!=null&&getIntent().getStringExtra("type").equals("0")){
            //说明是当前没有任何网络，只显示sos求救页面，没有返回的按钮
            return false;
        }else{
            //执行这个时说明是从我的页面的sos求救按钮跳转的正常执行
            return true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        content.startRippleAnimation();
        setToolBarTitle("救援");
        sosPresenter=new SOSPresenter(this);
        layout_message.setOnClickListener(this);
        layout_phone.setOnClickListener(this);
        wifiAdmin = new WifiAdmin(SOSActivity.this);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(SOSActivity.this.WIFI_SERVICE);
        initView();

    }

    private void initView() {
        if (getIntent().getStringExtra("type")!=null&&getIntent().getStringExtra("type").equals("0")){
            //当一开始没有网络的时候进入到这个页面，应该一直执行这个计时器监听当有网络连接时及时处理app跳转到其他正常逻辑页面
                final Timer timer=new Timer();
            timer.schedule(new TimerTask() {
            @Override
            public void run() {
             if (HelpUtils.isNetworkAvailable(getApplicationContext())){
                 HelpUtils.judgewifi(wifiAdmin, mWifiManager,SOSActivity.this);//判断
                 finish();
                 timer.cancel();
              }
            }
        },0,1000);
    }
        Location location=HelpUtils.locationgps(SOSActivity.this);
        if (location!=null){
            String slat="";
            String slon="";
            float lat=(float)(location.getLatitude());
            if (lat>0){
                slat=lat+" N";
            }else {
                slat=lat+" S";
            }
            float lon=(float)(location.getLongitude());
            if (lon>0){
                slon=lon+" E";
            }else {
                slon=lon+" W";
            }
            locationgps.setText("当前定位："+slon+" ，"+slat);
        }
    }



    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra("type")!=null&&getIntent().getStringExtra("type").equals("0")) {
            //说明是当前没有任何网络，只显示了sos页面，点击手机自带的返回按键，执行退出逻辑
            if (System.currentTimeMillis() - mExitTime > 2000) {
                mExitTime = System.currentTimeMillis();
                Toast.makeText(SOSActivity.this, "再按一次退出", Toast.LENGTH_LONG).show();
            } else {
                finish();
            }
        }else {
            //执行这个时说明是从我的页面的sos求救按钮跳转的正常执行
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_message:
                if (HelpUtils.isNetworkAvailable(getApplicationContext())){
                    Location location=HelpUtils.locationgps(SOSActivity.this);
                    if (location!=null){
                        double lat=location.getLatitude();
                        double lon=location.getLongitude();
                        sendsos(SharedPreferencedUtils.getString(SOSActivity.this,"token"),"我要求救:我的位置是"+HelpUtils.latlon(lat,lon));
                    }else {
                        sendsos(SharedPreferencedUtils.getString(SOSActivity.this,"token"),"我要求救:我的位置"+"不明");
                    }
                }else {

                }
                break;


            case R.id.layout_phone:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 119));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void showErrMsg(String msg) {

    }

    @Override
    public void showInfo(String string) {
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            if (status.optInt("code")==1000){
                Toast.makeText(SOSActivity.this,"求救成功,请保持冷静，等待救援",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("求救成功",string);

    }

    @Override
    public void showWifi(String string) {

    }

    /*
    * 发送求救信号
    * */
    public void sendsos(String token,String content){
        Map<String,String> signmap= Utils.getSignParams(SOSActivity.this,token);
        Map<String,String> map=Utils.getMap(SOSActivity.this,token);
        map.put("content",content);
        map.put("sign",Utils.getMd5StringMap(signmap));
        sosPresenter.sendsos(map);

    }
}
