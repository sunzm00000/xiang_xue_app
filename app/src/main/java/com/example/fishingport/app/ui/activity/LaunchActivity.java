package com.example.fishingport.app.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.example.fishingport.app.R;
import com.example.fishingport.app.service.LocationSevice;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.PermissionUtil;
import com.example.fishingport.app.tools.RxManager;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.WifiAdmin;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;

import java.util.HashMap;
import java.util.List;

import rx.functions.Action1;

/**
 * Author: wushixin (wusx@alltosun.com)
 * Date: 2016/12/30
 * Content: 启动页
 *
 */
public class LaunchActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private WifiAdmin wifiAdmin;//wifi管理工具类
    private WifiManager mWifiManager;//wifi管理者，用于获取wifi连接时的信息
    private String[] REQUEST_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE};
    private HashMap<String, String> map;//管理权限的map
    private static final int REQUEST_PERMISSION_CODE_TAKE_PIC = 9; //权限的请求码
    private static final int REQUEST_PERMISSION_SEETING = 8; //去设置界面的请求码
    protected RxManager mRxManager;
    private int iswifi = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        wifiAdmin = new WifiAdmin(LaunchActivity.this);
        if (wifiAdmin.checkState() == 1) {//当wifi没有打开的时候
            wifiAdmin.openWifi();
        }
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(LaunchActivity.this.WIFI_SERVICE);
        Intent intent1 = new Intent(LaunchActivity.this, LocationSevice.class);
        startService(intent1);//开启获取城市
        if (NetUtils.hasNetwork(LaunchActivity.this)){
        if (SharedPreferencedUtils.getString(LaunchActivity.this, "token") != null) {
            //每次启动app的时候都进行环信登陆的操作如果有账号的话
            Log.i("环信账号密码", SharedPreferencedUtils.getString(LaunchActivity.this, "huanxinname")
                    + "/" + SharedPreferencedUtils.getString(LaunchActivity.this, "huanxinpasswd"));
            if (HelpUtils.isNetworkAvailable(LaunchActivity.this)){
            if (SharedPreferencedUtils.getString(LaunchActivity.this, "huanxinname")!=null&&SharedPreferencedUtils.getString(LaunchActivity.this, "huanxinpasswd")!=null){
            EMClient.getInstance().login(SharedPreferencedUtils.getString(LaunchActivity.this, "huanxinname")
                    , SharedPreferencedUtils.getString(LaunchActivity.this, "huanxinpasswd"), new EMCallBack() {
                        //回调
                        @Override
                        public void onSuccess() {
                            EMClient.getInstance().groupManager().loadAllGroups();
                            EMClient.getInstance().chatManager().loadAllConversations();
                            Log.d("main", "登录聊天服务器成功！");
                        }
                        @Override
                        public void onProgress(int progress, String status) {
                        }
                        @Override
                        public void onError(int code, String message) {
                            Log.d("main", "登录聊天服务器失败！");
                        }
                    });
        }

            }
        }
        }else {
            HelpUtils.warning(LaunchActivity.this,"暂无网络");
        }
        initRxBus();
        startToHome();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void init() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (SharedPreferencedUtils.getBoolean(LaunchActivity.this, "firststart", true)) {
                    SharedPreferencedUtils.setBoolean(LaunchActivity.this, "firststart", false);
                    startActivity(new Intent(LaunchActivity.this, GuidePageActivity.class));
                    finish();
                } else {
                    if (HelpUtils.isNetworkAvailable(getApplicationContext())) {
                        if (wifiAdmin.checkState() == 1 && wifiAdmin.checkState() == 2) {
                            //当wifi没有打开的时候,说明当前连接的是数据网络就跳到一键上网的页面
                            startActivity(new Intent(LaunchActivity.this, AkeyWebActivity.class));
                            finish();
                        } else {
                            //wifiAdamin.checkState==3;说明连接的shiwifi
                            //判断wifi连接的是否是渔港指定的wifi,如果不是就跳到一键上网的页面，如果是就可以直接操作app
                            //判断需要根据接口的返回做具体的处理
                            HelpUtils.judgewifi(wifiAdmin, mWifiManager,LaunchActivity.this);//判断
                            finish();
                        }
                    } else {
                        //么有网络的情况下，判断是否有港口wifi，没有跳转到定位页面，有跳转到一键上网页面
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
                            startActivity(new Intent(LaunchActivity.this, AkeyWebActivity.class));
                            finish();
                        }else  if (iswifi==0){
                            //没有网络只显示sos求救
                            Intent sosintent = new Intent(LaunchActivity.this, SOSActivity.class);
                            sosintent.putExtra("type", "0");
                            startActivity(sosintent);
                        }
                        //GuidePageActivity.activity.finish();
                    }
                    //  }
                }
            }
        }, 2000);
    }


    /**
     * 跳转到首页
     */
    private void startToHome() {
        if (Build.VERSION.SDK_INT < 23) {
            //6.0一下直接去主页
//            UiUtils.startIntent(this, LoginActivity.class);
//            finish();
            init();
        } else {
            //6.0以上请求权限
            checkPermiss();
        }
    }

    private void initRxBus() {
        mRxManager = new RxManager();
        mRxManager.on(LaunchActivity.class.getSimpleName(), new Action1<Object>() {
            @Override
            public void call(Object o) {

                finish();
            }
        });

    }

    //singtask 再次进入不会进入oncreate,而是进入到这里
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startToHome();
    }

    /**
     * 请求权限
     */
    private void checkPermiss() {
        PermissionUtil.checkPermission(this, REQUEST_PERMISSIONS, new PermissionUtil.permissionInterface() {
            @Override
            public void success() {
                //请求成功
                //UiUtils.startIntent(WelcomeActivity.this, LoginActivity.class);
                init();
                // Utils.toastUtils(LaunchActivity.this, "请求权限成功,去首页");
            }

            @Override
            public void fail(final List<String> permissions) {
                if (map == null) {
                    map = new HashMap<>();
                    map.put("android.permission.CAMERA", "拍照");
                    map.put("android.permission.ACCESS_COARSE_LOCATION", "位置信息");
                    map.put("android.permission.WRITE_EXTERNAL_STORAGE", "存储空间");
                    map.put("android.permission.CALL_PHONE", "打电话");
                }
                requestPermission(permissions.toArray(new String[permissions.size()]));
            }
        });
    }

    /**
     * 请求权限
     *
     * @param permissions
     */
    private void requestPermission(final String[] permissions) {
//        StringBuilder sb = new StringBuilder();
//        for (String permission : permissions) {
//            if (map != null) {
//                String s = map.get(permission);
//                if (!TextUtils.isEmpty(s)) {
//                    sb.append(s + " ");
//                }
//            }
//        }
////        deleteDialog.setTvcontent("请允许"+sb+"权限请求");
////        deleteDialog.show();
//
//        new AlertDialog.Builder(this).setTitle("请允许"+sb+"权限请求")//设置对话框标题
//
//                .setMessage("请确认所有数据都保存后再推出系统！")//设置显示的内容
//
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
//
//
//                    @Override
//
//                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
//
//                        // TODO Auto-generated method stub

        PermissionUtil.requestContactsPermissions(LaunchActivity.this, permissions, REQUEST_PERMISSION_CODE_TAKE_PIC);
//                    }
//
//                }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮
//
//
//
//            @Override
//
//            public void onClick(DialogInterface dialog, int which) {//响应事件
//
//                finish();
//
//            }
//
//        }).show();//在按键响应事件中显示此对话框
//
//        Utils.toastUtils(LaunchActivity.this, "获取权限失败,开启获取权限的对话框");
    }

    /**
     * 请求权限2
     *
     * @param permissions
     */
    private void requestPermission2(final String[] permissions) {

        StringBuilder sb = new StringBuilder();
        for (String permission : permissions) {
            if (map != null) {
                String s = map.get(permission);
                if (!TextUtils.isEmpty(s)) {
                    sb.append(s + " ");
                }
            }
        }

        new AlertDialog.Builder(this).setTitle("请允许" + sb + "权限请求")//设置对话框标题

                .setMessage("请确认所有数据都保存后再推出系统！")//设置显示的内容

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                        // TODO Auto-generated method stub

                        //   去掉已经请求过的权限
                        List<String> deniedPermissions = PermissionUtil.findDeniedPermissions(LaunchActivity.this, permissions);
                        //请求权限
                        PermissionUtil.requestContactsPermissions(LaunchActivity.this, deniedPermissions.toArray(new String[deniedPermissions.size()]), REQUEST_PERMISSION_CODE_TAKE_PIC);

                    }

                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮


            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件

                finish();

            }

        }).show();//在按键响应事件中显示此对话框


    }

    private void startToSetting() {


        new AlertDialog.Builder(this).setTitle("去设置界面开启权限?")//设置对话框标题

                .setMessage("请确认所有数据都保存后再推出系统！")//设置显示的内容

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                        // TODO Auto-generated method stub

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SEETING);
                    }

                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮


            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件

                finish();

            }

        }).show();//在按键响应事件中显示此对话框

    }


    /**
     * 检测权限的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION_CODE_TAKE_PIC) {
            if (PermissionUtil.verifyPermissions(grantResults)) {//有权限
                //TODO 有权限
                // UiUtils.startIntent(this, LoginActivity.class);
                init();
            } else {
                //没有权限
                if (!PermissionUtil.shouldShowPermissions(this, permissions)) {//这个返回false 表示勾选了不再提示

                    startToSetting();

                } else {
                    //表示没有权限 ,但是没勾选不再提示
                    for (String s : permissions) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(LaunchActivity.this,
                                s)) {
                            //去掉已经允许的
                            if (map != null) {
                                map.remove(s);
                            }
                        }
                    }
                    requestPermission2(permissions);
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //如果是从设置界面返回,就继续判断权限
        if (requestCode == REQUEST_PERMISSION_SEETING) {
            checkPermiss();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (map != null) {
            map.clear();
            map = null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxManager.clear();
    }

}
