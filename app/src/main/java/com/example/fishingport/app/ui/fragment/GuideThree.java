package com.example.fishingport.app.ui.fragment;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fishingport.app.R;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.WifiAdmin;
import com.example.fishingport.app.ui.activity.AkeyWebActivity;
import com.example.fishingport.app.ui.activity.GuidePageActivity;
import com.example.fishingport.app.ui.activity.LaunchActivity;
import com.example.fishingport.app.ui.activity.LoginActivity;
import com.example.fishingport.app.ui.activity.MainActivity;
import com.example.fishingport.app.ui.activity.SOSActivity;

import java.util.List;


/**
 * Created by Administrator on 2016/4/29.
 */
public class GuideThree extends Fragment {
    View view;
    Button button;
    private WifiAdmin wifiAdmin;//wifi管理工具类
    private WifiManager mWifiManager;//wifi管理者，用于获取wifi连接时的信息
    private int iswifi = 0;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.item_guide_one,container,false);
        ImageView guide_image= (ImageView) view.findViewById(R.id.guide_image);
        TextView txt_top= (TextView) view.findViewById(R.id.txt_top);
        TextView txt_down= (TextView) view.findViewById(R.id.txt_down);
        txt_top.setText("记录轨迹");
        txt_down.setText("记录运动轨迹，我们伴你而行");
        guide_image.setImageResource(R.mipmap.three);
        TextView button= (TextView) view.findViewById(R.id.button);
        button.setText("开启渔网通");
        wifiAdmin = new WifiAdmin(getActivity());
        if (wifiAdmin.checkState() == 1) {//当wifi没有打开的时候
            wifiAdmin.openWifi();
        }
        mWifiManager = (WifiManager) getActivity().getApplicationContext().
                getSystemService(getActivity().WIFI_SERVICE);
        button.setBackgroundResource(R.drawable.bg_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelpUtils.isNetworkAvailable(getActivity().getApplicationContext())) {
                    if (wifiAdmin.checkState() == 1 && wifiAdmin.checkState() == 2) {
                        //当wifi没有打开的时候,说明当前连接的是数据网络就跳到一键上网的页面
                        startActivity(new Intent(getActivity(), AkeyWebActivity.class));
                        getActivity().finish();
                    } else {
                        //wifiAdamin.checkState==3;说明连接的shiwifi
                        //判断wifi连接的是否是渔港指定的wifi,如果不是就跳到一键上网的页面，如果是就可以直接操作app
                        //判断需要根据接口的返回做具体的处理
                        HelpUtils.judgewifi(wifiAdmin, mWifiManager,getActivity());//判断
                        GuidePageActivity.activity.finish();//

                    }
                } else {
                    //没有网络只显示sos求救
                    Intent sosintent = new Intent(getActivity(), SOSActivity.class);
                    sosintent.putExtra("type", "0");
                    startActivity(sosintent);
                    GuidePageActivity.activity.finish();//
                }
                //  }


            }
        });

        return view;

    }

    public void judgewifi(WifiAdmin wifiAdmin,WifiManager mWifiManager) {
        int iswifi = 0;
        if (mWifiManager.getConnectionInfo().getSSID().toString().equals("\"APP-TEST\"")) {
            //是港口wifi
            if (SharedPreferencedUtils.getString(getActivity(),"token")==null) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        } else {
            //有网不是港口wifi
            List<ScanResult> list = wifiAdmin.getWifiList();
            for (ScanResult scanResult : list) {
                Log.i("列表数", scanResult.SSID + "/" + scanResult.BSSID);
                if (scanResult.SSID.equals("APP-TEST")) {
                    //说明有港口wifi
                    iswifi++;
                }
            }
            if (iswifi == 1) {
                //说明有港口wifi
                startActivity(new Intent(getActivity(), AkeyWebActivity.class));
                getActivity().finish();
            } else if (iswifi == 0) {
                //说明没有港口wifi
                if (SharedPreferencedUtils.getString(getActivity(),"token")==null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                } else {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
            }
        }
    }


}
