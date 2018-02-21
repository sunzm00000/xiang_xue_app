package com.example.fishingport.app.tools;

import android.content.Context;
import android.location.Address;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.blankj.utilcode.util.LocationUtils;
import com.example.fishingport.app.bean.Location;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.PoiSearch;
import com.tianditu.android.maps.TGeoAddress;
import com.tianditu.android.maps.TGeoDecode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.blankj.utilcode.util.LocationUtils.getAddress;

/**
 * Created by Lenovo on 2017/4/25.
 * 定位根据经纬度得到当前的城市
 */

public class LocationCity implements OnGetGeoCoderResultListener {
    private  Context context;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    GeoPoint geoPoint;

    LocationClient mLocClient;
    public LocationCity(Context context){
        this.context=context;
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }
    public  void  setsearch(){
        if (LocationUtils.isGpsEnabled()==false){
            LocationUtils.openGpsSettings();
        }else {
           // android.location.Location location=HelpUtils.locationgps(context);
                //地理反编码
                mLocClient = new LocationClient(context);
                mLocClient.registerLocationListener(new BDLocationListener() {
                    @Override
                    public void onReceiveLocation(BDLocation bdLocation) {
                        Log.i("百度定位",bdLocation.getLongitude()+"/"+bdLocation.getLatitude());

                        if (bdLocation == null) {
                            return;
                        }
                        double[] dd = HelpUtils.gcj02towgs84(bdLocation.getLongitude(), bdLocation.getLatitude());
                        // 反Geo搜索
                        LatLng ptCenter = new LatLng(dd[1], dd[0]);
                        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
                    }
                    @Override
                    public void onConnectHotSpotMessage(String s, int i) {

                    }
                });
                LocationClientOption option = new LocationClientOption();
                option.setOpenGps(true); // 打开gps
                option.setCoorType("gcj02"); // 设置坐标类型
                option.setScanSpan(0);
                mLocClient.setLocOption(option);
                mLocClient.start();

        }
    }
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
    }
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        String address=reverseGeoCodeResult.getAddress();
       // String addrss="山东省聊城市冠县桑阿镇白佛头";
        mLocClient.stop();
        Log.i("地址",address+"");
        if (address!=null){
            String[] citys= HelpUtils.getcityorcity_addr(address);
            Log.i("截取的结果",citys[0]+"/"+citys[1]);
            SharedPreferencedUtils.setString(context,"city",citys[0]);//如北京市，威海市
            SharedPreferencedUtils.setString(context,"city_addr",citys[1]);//县级市or县or区
        }
    }


}
