package com.example.fishingport.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.LocationUtils;
import com.example.fishingport.app.Dao.GreenDaoManager;
import com.example.fishingport.app.MyApplication;
import com.example.fishingport.app.api.TrackConstract;
import com.example.fishingport.app.bean.Location;
import com.example.fishingport.app.bean.LocationDao;
import com.example.fishingport.app.presenter.TrackPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.LocationhelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.ui.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lenovo on 2017/4/14.
 *地图定位
 */
public class MapService extends Service implements TrackConstract.view{
    private Timer timer;
    private LocationServices locationServices;
    public MyLocationListenner myListener = new MyLocationListenner();

    private TrackPresenter trackPresenter;
    private  int report_time=1;//定位间隔默认2分钟
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    HelpUtils.pathgps(MapService.this);
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        //第一次开启服务调用
        super.onCreate();
        trackPresenter=new TrackPresenter(this);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启服务后调用此方法，会一直调用
        report_time(SharedPreferencedUtils.getString(this,"token"));
        return super.onStartCommand(intent, flags, startId);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        //停止服务调用
        Log.i("服务","服务停止了");
        if (timer!=null){
            timer.cancel();
        }
        //locationServices.stop();
        super.onDestroy();
    }


    @Override
    public void showErrMsg(String msg) {

    }
    @Override
    public void showInfo(String string) {

    }

    @Override
    public void autoInfo(String string) {
        Log.i("划线间隔",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            JSONObject result=object.optJSONObject("result");
            if (status.optInt("code")==1000){
                report_time=result.optInt("report_time");
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }, 0,report_time*60*1000);//一分钟一次
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void startInfo(String string) {

    }


    //取划线和记录间隔
    public  void report_time(String token){
        Map<String,String> signmap= Utils.getSignParams(this,token);
        Map<String,String> map=Utils.getMap(this,token);
        map.put("sign",Utils.getMd5StringMap(signmap));
        trackPresenter.getreport_time(map);
    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.i("定位执行了-","定位执行了");
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                Log.i("定位执行了=","定位执行了");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
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
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
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
                Log.i("不是自动后台定位百度sdk定位",sb.toString());
                double[] dd=HelpUtils.gcj02towgs84(location.getLatitude(),location.getLongitude());
                Log.i("不是天地图和百度--",location.getLatitude()+"/"+location.getLongitude()+"//"+dd[0]
                        +"//"+dd[1]);
                //存到数据库
                LocationDao locationDao = GreenDaoManager.getInstance().getNewSession().getLocationDao();
                Location location1 = new Location(null, dd[1] + "", dd[0] + "", System.currentTimeMillis(), 0);
                locationDao.insert(location1);
            }
        }

        public void onConnectHotSpotMessage(String s, int i){
        }
    };
    /**
     * 定位SDK监听函数
     */
    class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }


    }


}



