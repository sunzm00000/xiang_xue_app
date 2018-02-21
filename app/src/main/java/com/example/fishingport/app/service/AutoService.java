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
import com.baidu.location.Poi;
import com.example.fishingport.app.Dao.GreenDaoManager;
import com.example.fishingport.app.MyApplication;
import com.example.fishingport.app.api.TrackConstract;
import com.example.fishingport.app.bean.Location;
import com.example.fishingport.app.bean.LocationDao;
import com.example.fishingport.app.model.RePortData;
import com.example.fishingport.app.presenter.TrackPresenter;
import com.example.fishingport.app.tools.HelpUtils;

import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Lenovo on 2017/4/24.
 * 自动记录轨迹
 */

public class
AutoService extends Service implements TrackConstract.view{
    private TrackPresenter trackPresenter;
    private LocationServices locationServices;
    private int report_time=1;//请求定位间隔
    private int report_user_time=1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    HelpUtils.gps(AutoService.this);
                    LocationDao locationDao= GreenDaoManager.getInstance().getAutoDaoSession().getLocationDao();
                    Log.i("间隔","执行了"+locationDao.loadAll().size()+"/");

                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencedUtils.setInteger(this,"auto_time",report_time);
        SharedPreferencedUtils.setInteger(this,"user_time",report_user_time);
        trackPresenter=new TrackPresenter(AutoService.this);
        locationServices = ((MyApplication) getApplication()).locationServices;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，
        // 都是通过此种方式获取locationservice实例的
        locationServices.registerListener(mListener);
        locationServices.start();// 定位SDK
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("自动间隔",SharedPreferencedUtils.getInteger(this,"auto_time",-1)+"");
        if (HelpUtils.isNetworkAvailable(getApplicationContext())){
            autoport_time(SharedPreferencedUtils.getString(AutoService.this,"token"));
        }else {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);

                }
            },0,1000);
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //每隔30分钟上报一次
                LocationDao locationDao= GreenDaoManager.getInstance().getAutoDaoSession().getLocationDao();
                List<Location> list=locationDao.loadAll();
                List<RePortData> portDataList=new ArrayList<RePortData>();
                for (int i=0;i<list.size();i++){
                    RePortData rePortData=new RePortData();
                    rePortData.setLat(list.get(i).getLatitude());
                    rePortData.setLng(list.get(i).getLongitude());
                    rePortData.setReport_time(list.get(i).getTime()+"");
                     portDataList.add(rePortData);
                }
                Gson gson=new Gson();
                gson.toJson(portDataList);
                Log.i("json",gson.toJson(portDataList)+"");
                autoport(gson.toJson(portDataList), SharedPreferencedUtils.getString(AutoService.this,"token"));
                //autoport();

            }
        },500,SharedPreferencedUtils.getInteger(this,"auto_time",1)*60*1000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public  void autoport_time(String token){
        Map<String,String> signmap=Utils.getSignParams(AutoService.this,token);
        Map<String,String> map=Utils.getMap(AutoService.this,token);
        map.put("sign",Utils.getMd5StringMap(signmap));
        trackPresenter.getauto_port_time(map);
    }

    public void autoport(String report_data,String token){
        Map<String,String> signmap= Utils.getSignParams(AutoService.this,token);
        Map<String,String> map=Utils.getMap(AutoService.this,token);
        map.put("report_data",report_data);
        map.put("token",token);
        map.put("sign",Utils.getMd5StringMap(signmap));
        trackPresenter.autoRePort(map);
    }

    @Override
    public void showErrMsg(String msg) {

    }

    @Override
    public void showInfo(String string) {
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            JSONObject result=object.optJSONObject("result");
            if (status.optInt("code")==1000){
                report_time=result.optInt("report_time");
                SharedPreferencedUtils.setInteger(this,"auto_time",report_time);
                Log.i("j间隔",report_time+"");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                },0,report_time*60*1000);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void autoInfo(String string) {
        //自动记录上报的接口
        Log.i("自动上报成功",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            JSONObject result=object.optJSONObject("result");
            if (status.optInt("code")==1000){
                report_time=Integer.valueOf(result.optString("report_auto_time"));
                report_user_time=Integer.valueOf(result.optString("report_user_time"));
                SharedPreferencedUtils.setInteger(this,"auto_time",report_time);
                SharedPreferencedUtils.setInteger(this,"user_time",report_user_time);
                LocationDao locationDao= GreenDaoManager.getInstance().getAutoDaoSession().getLocationDao();
                 locationDao.deleteAll();
                Log.i("json==",locationDao.loadAll().size()+"");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startInfo(String string) {

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
//                sb.append(location.getLocTypeDescription());
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
//                sb.append(location.getUserIndoorState());
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
                Log.i("自动后台定位百度sdk定位",sb.toString());
               double[] dd=HelpUtils.gcj02towgs84(location.getLatitude(),location.getLongitude());
                Log.i("天地图和百度--",location.getLatitude()+"/"+location.getLongitude()+"//"+dd[0]
                        +"//"+dd[1]);
            }
        }
        public void onConnectHotSpotMessage(String s, int i){
        }
    };
}
