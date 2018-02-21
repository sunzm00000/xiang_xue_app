package com.example.fishingport.app.ui.activity;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.SOSConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.presenter.SOSPresenter;
import com.example.fishingport.app.tools.Constances;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.tools.WifiAdmin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
/*
* 连接设备
* */

public class ConnectedDeviceActivity extends BaseAppCompatActivity implements SOSConstract.view {
  @BindView(R.id.progress)
    ProgressBar progressBar;
    Timer timer;
    int proressint=1;
    @BindView(R.id.success)
    LinearLayout successed;//连接成功需要显示的，默认隐藏
    @BindView(R.id.successing)
    LinearLayout successing;//正在连接需要显示的图。默认显示
    private SOSPresenter sosPresenter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    timer.cancel();
                    break;
            }
        }
    };
    WifiAdmin wifiAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(ConnectedDeviceActivity.this);
        wifiAdmin = new WifiAdmin(ConnectedDeviceActivity.this);
        sosPresenter=new SOSPresenter(this);
        setToolBarTitle("连接设备");
        if (wifiAdmin.checkState()==1){//当wifi没有打开的时候
            wifiAdmin.openWifi();
        }
       timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                         proressint++;
                        Log.i("lalal",proressint+"");
                        //连接无线网
                        if (proressint>10){
                            timer.cancel();
                            boolean b=false;
                                List<ScanResult> list = wifiAdmin.getWifiList();
                                for (ScanResult scanResult : list) {
                                    Log.i("列表数", scanResult.SSID+"/"+scanResult.BSSID);
                                    if (scanResult.SSID.equals("APP-TEST")) {
                                       b= wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(scanResult.SSID,
                                               Constances.wifimima, 3, scanResult.BSSID),0);
                                    }
                            }
                            if (b==true){
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("是否有网络0-",HelpUtils.isNetworkAvailable(ConnectedDeviceActivity.this)+"");
                                        if (HelpUtils.isNetworkAvailable(ConnectedDeviceActivity.this)){
                                            sosPresenter.getwifimac();
                                        }else {
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.i("是否有网络=",HelpUtils.isNetworkAvailable(ConnectedDeviceActivity.this)+"");

                                                    if (HelpUtils.isNetworkAvailable(ConnectedDeviceActivity.this)){
                                                        sosPresenter.getwifimac();
                                                    }
                                                }
                                            },2000);
                                        }
                                    }
                                },5000);

                            }else {

                            }
                        }
                        progressBar.setProgress(proressint);
                    }

                });
            }
        },0,100);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_connected_device;
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
        Log.i("ac认证",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            JSONObject result=object.optJSONObject("result");
            if (status.optInt("code")==1000){
                ConnectedDeviceActivity.this.successed.setVisibility(View.VISIBLE);
                ConnectedDeviceActivity.this.successing.setVisibility(View.GONE);
                //跳转到正常的页面
                startActivity(new Intent(ConnectedDeviceActivity.this, LoginActivity.class));
                finish();
            }
            else {
               // Toast.makeText(ConnectedDeviceActivity.this,"")
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showWifi(String string) {
        Log.i("wifimac",string);
        try {
            JSONObject object=new JSONObject(string);
            String userMac=object.optString("userMac");
            String deviceMac=object.optString("deviceMac");
            SharedPreferencedUtils.setString(ConnectedDeviceActivity.this,"usermac",userMac+"");
            String token="";
            if (SharedPreferencedUtils.getString(ConnectedDeviceActivity.this,"token")==null){
                token="";
            }else {
                token=SharedPreferencedUtils.getString(ConnectedDeviceActivity.this,"token");
            }
            permit_client(token,userMac,deviceMac);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  void  permit_client(String token,String userMac,String deviceMac)
    {
        Log.i("ac端认证",token+"/"+userMac+"/"+deviceMac);
        Map<String,String> signmap= Utils.getSignParams(ConnectedDeviceActivity.this,token);
        Map<String,String> map=Utils.getMap(ConnectedDeviceActivity.this,token);
        map.put("user_mac",userMac);
        map.put("device_mac",deviceMac);
        map.put("sign",Utils.getMd5StringMap(signmap));
        sosPresenter.permit_client(map);
    }
}
