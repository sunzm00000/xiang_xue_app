package com.example.fishingport.app.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.blankj.utilcode.util.LocationUtils;
import com.example.fishingport.app.MyApplication;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.SOSConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;

import com.example.fishingport.app.presenter.SOSPresenter;


import com.example.fishingport.app.service.TimerServicer;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.StatusBarCompat;
import com.example.fishingport.app.tools.WeiboDialogUtils;
import com.example.fishingport.app.ui.fragment.FishingCircleFragment;


import com.example.fishingport.app.service.AutoService;

import com.example.fishingport.app.service.LocationSevice;
import com.example.fishingport.app.tools.HelpUtils;

import com.example.fishingport.app.service.LocationServices;
import com.example.fishingport.app.service.LocationSevice;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.LocationhelpUtils;

import com.example.fishingport.app.ui.fragment.CeshiBaidumap;

import com.example.fishingport.app.ui.fragment.Fragment_mapView;
import com.example.fishingport.app.ui.fragment.HomeFragment;
import com.example.fishingport.app.ui.fragment.MessageFragment;
import com.example.fishingport.app.ui.fragment.MineFragment;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.TGeoAddress;
import com.tianditu.android.maps.TGeoDecode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/13.
 */

public class MainActivity extends BaseAppCompatActivity  {
    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.radio_home)
    RadioButton radioHome;
    @BindView(R.id.radio_messages)
    RadioButton radioMessages;
    @BindView(R.id.radio_noun)
    RadioButton radioNoun;
    @BindView(R.id.radio_user)
    RadioButton radioUser;
    @BindView(R.id.tab)
    RelativeLayout tab;
    private List<Fragment> mFragments;
    private List<RadioButton> mViews;
    private int mCurrentIndex = 0;
    private int mOldIndex = 0;
    private FragmentTransaction ft;
    private HomeFragment homeFragment;
    private Fragment_mapView fragment_mapView;
    private MineFragment mineFragment;
    private MessageFragment messageFragment;
    private CeshiBaidumap ceshiBaidumap;
    private  FishingCircleFragment fishingCircleFragment;
    private long mExitTime = 0;
    FragmentManager fragmentManager;
    private Reciver broacastreciver;
     private  int ismapactivity=0;
    private LocationServices locationServices;
   public  static  Activity activity;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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
        broacastreciver=new Reciver();
        activity=MainActivity.this;
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("showfragment");
        registerReceiver(broacastreciver,intentFilter);
        mViews = new ArrayList<RadioButton>();
        mViews.add(radioHome);
        mViews.add(radioMessages);
        mViews.add(radioNoun);
        mViews.add(radioUser);
        mViews.get(0).setSelected(true);
        Log.i("获取imei码",HelpUtils.getIMEI(MainActivity.this));
        autogrenndao();
        initFragments();
        locationServices = ((MyApplication) getApplication()).locationServices;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationServices.registerListener(mListener);
        locationServices.start();// 定位SDK
        if (getIntent().getStringExtra("iserror")!=null){
            //说明在登录页环信么有登录成功这里尝试再次登录,这里无论成功失败不做任何提示和处理，如果登录失败了，在聊天页面也有一次登录
            EMLogin(SharedPreferencedUtils.getString(MainActivity.this,"huanxinname"),
                    SharedPreferencedUtils.getString(MainActivity.this,"huanxinpasswd"));
        }
    }

    /**
     * 初始化用到的Fragment
     */
    private void initFragments() {
        fragmentManager=getSupportFragmentManager();
        homeFragment = new HomeFragment();
        messageFragment = new MessageFragment();
        fishingCircleFragment = new FishingCircleFragment();
        mineFragment = new MineFragment();
        fragment_mapView=new Fragment_mapView();
        ceshiBaidumap=new CeshiBaidumap();
        mFragments = new ArrayList<Fragment>();
        mFragments.add(homeFragment);
        mFragments.add(messageFragment);
        mFragments.add(fishingCircleFragment);
        mFragments.add(mineFragment);
        mFragments.add(fragment_mapView);
        if (homeFragment.isAdded()){
            mCurrentIndex = 0;
            showCurrentFragment(mCurrentIndex);
        }else {
            getSupportFragmentManager().beginTransaction().add(R.id.content, homeFragment).commit();
        }
//        showCurrentFragment(0);
//        if (getIntent().getIntExtra("type", -1) == 0) {
            //默认加载前两个Fragment，其中第一个展示，第二个隐藏
       //} else {

           // getSupportFragmentManager().beginTransaction().add(R.id.content,projectFragment).commit();
//        }


    }

    /**
     * 展示当前选中的Fragment
     *
     * @param currentIndex
     */
    private void showCurrentFragment(int currentIndex) {
        if (currentIndex != mOldIndex) {
            ft =fragmentManager.beginTransaction();
            //  mPublishMenu.setSelected(false);
            if (mOldIndex==4){
                imgAdd.setSelected(false);
            }else {
                mViews.get(mOldIndex).setSelected(false);
            }
            if (currentIndex==4){
                imgAdd.setSelected(true);
            }else {
                mViews.get(currentIndex).setSelected(true);
            }
            ft.hide(mFragments.get(mOldIndex));
            if (mFragments.get(currentIndex).isAdded()) {
                    ft.show(mFragments.get(currentIndex)).commitAllowingStateLoss();
            } else {

                ft.add(R.id.content, mFragments.get(currentIndex)).show(mFragments.get(currentIndex)).commitAllowingStateLoss();
            }
            mOldIndex = currentIndex;
       }
    }
    @Override
    protected void onResume() {
    Log.i("当前展示的是第几个",mCurrentIndex+"/"+mOldIndex);
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (homeFragment.isAdded()) {
            mCurrentIndex = 0;
            showCurrentFragment(mCurrentIndex);
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.content, homeFragment).commit();
        }
        if (intent.getStringExtra("ty") != null) {
            Log.i("mapactivity传值",intent.getStringExtra("ty")+"/");
            if (intent.getStringExtra("ty").equals("0")) {
                mCurrentIndex = 0;
                showCurrentFragment(mCurrentIndex);
            }
            if (intent.getStringExtra("ty").equals("1")) {
                mCurrentIndex = 1;
                showCurrentFragment(mCurrentIndex);
            }
            if (intent.getStringExtra("ty").equals("2")) {
                mCurrentIndex = 2;
                showCurrentFragment(mCurrentIndex);
            }
            if (intent.getStringExtra("ty").equals("3")) {
                mCurrentIndex = 3;
                showCurrentFragment(mCurrentIndex);
            }
        }
    }

    @OnClick({R.id.radio_home, R.id.radio_messages, R.id.radio_noun, R.id.radio_user, R.id.img_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_home:
                mCurrentIndex = 0;
                break;
            case R.id.radio_messages:
                mCurrentIndex = 1;
                break;
            case R.id.radio_noun:
                mCurrentIndex = 2;
                break;
            case R.id.radio_user:
                mCurrentIndex = 3;
                break;
            case R.id.img_add:
                   //mCurrentIndex=4;
                startActivity(new Intent(MainActivity.this,MapActivity.class));
                break;
        }
        if (mCurrentIndex != 4) {
            showCurrentFragment(mCurrentIndex);
        }else {
            showCurrentFragment(mCurrentIndex);
        }
    }
    /*
     * 自动记录轨迹
     *
     * */
    public void autogrenndao(){
        Intent intent=new Intent(this, AutoService.class);
        startService(intent);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            mExitTime = System.currentTimeMillis();
            // Utils.toastUtils(MainActivity.this,"再按一次退出");
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_LONG).show();
            //Snackbar.make(home_layout, "再按一次退出", Snackbar.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }


    class  Reciver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!=null){
                Log.i("刘希亭type====",intent.getStringExtra("type"));
                String type=intent.getStringExtra("type");
                if (type.equals("4")){
                    mCurrentIndex=3;
                    showCurrentFragment(mCurrentIndex);//显示轨迹页面
                }
            }

        }
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
               // sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                SharedPreferencedUtils.setString(MainActivity.this,"city",location.getCity());
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
               // sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
               // logMsg(sb.toString());
                Log.i("百度sdk定位",sb.toString());

            }
        }

        public void onConnectHotSpotMessage(String s, int i){
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationServices.stop();
        unregisterReceiver(broacastreciver);
    }

    //环信登录
    private void EMLogin(final String huanxin_name, final String huanxin_passwd) {

        Log.i("登录环信执行了", "登录环信执行 了" + huanxin_name + "/" + huanxin_passwd);
        EMClient.getInstance().login(huanxin_name, huanxin_passwd, new EMCallBack() {
            /**
             * 登陆成功的回调
             */
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
            /**
             * 登陆错误的回调
             * @param i
             * @param s
             */
            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                break;
                            default:
                                if (i==200){
                                    //说明用户已经登录需要先退出再登录
                                    EMClient.getInstance().logout(true, new EMCallBack() {
                                        @Override
                                        public void onSuccess() {
                                            EMLogin(huanxin_name,huanxin_passwd);//重新登录
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                        }
                                        @Override
                                        public void onProgress(int i, String s) {
                                        }
                                    });
                                }
                                break;
                        }
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
