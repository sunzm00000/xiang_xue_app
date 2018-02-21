package com.example.fishingport.app.ui.fragment;

import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.example.fishingport.app.Dao.GreenDaoManager;
import com.example.fishingport.app.R;
import com.example.fishingport.app.base.BaseFragment;
import com.example.fishingport.app.bean.Location;
import com.example.fishingport.app.bean.LocationDao;
import com.example.fishingport.app.service.MapService;
import com.example.fishingport.app.ui.activity.Ceshibaidumapactivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.fishingport.app.R.id.cache;
import static com.example.fishingport.app.R.id.location;
import static com.example.fishingport.app.R.id.main_mapview;

/**
 * 百度地图测试
 */
public class CeshiBaidumap extends BaseFragment implements BaiduMap.OnMarkerClickListener,View.OnClickListener{
    @BindView(R.id.location)
    LinearLayout btn_location;
    @BindView(R.id.record)
    LinearLayout btn_record;
    @BindView(R.id.jia)
    LinearLayout jia;
    @BindView(R.id.jian)
    LinearLayout jian;
    @BindView(R.id.bmapView)
    MapView bmapview;
    BaiduMap mBaiduMap;
    @BindView(R.id.tiaozhuan)
    Button tiaozhuan;
    private Timer timer;
    @BindView(R.id.btn)
    Button btn;
    Marker mstart;
    Marker mend;
    private  int   amark=0;
    private MyLocationData locData;
    BitmapDescriptor mCurrentMarker;
    // 定位相关
    LocationClient mLocClient;
    Polyline mPolyline;

    private MyLocationConfiguration.LocationMode mCurrentMode;//地图定位的类型（普通，跟随，罗盘）
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
      bmapview.showZoomControls(true);//设置缩放是否显示

        mBaiduMap=bmapview.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(
                new MapStatus.Builder().zoom(18).build()
        ));
        mBaiduMap.setOnMarkerClickListener(this);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;//普通定位可以拖动地图
        location(mCurrentMode);
        // 当不需要定位图层时关闭定位图层
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Ceshibaidumapactivity.class));

            }
        });
        tiaozhuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapservice = new Intent(getActivity(), MapService.class);
                getActivity().startService(mapservice);
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        amark++;
                        pine();

                    }
                },5000,1000);
            }
        });


    }
      /*
      * 定位
      * */
    private void location(final MyLocationConfiguration.LocationMode mCurrentMode) {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                // map view 销毁后不在处理新接收的位置
                if (bdLocation == null ||mBaiduMap == null) {
                    return;
                }
                locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(0).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                BitmapDescriptor mCurrentMarker= BitmapDescriptorFactory
                        .fromResource(R.mipmap.smalldot);
                MyLocationConfiguration config = new MyLocationConfiguration
                        (mCurrentMode, true, mCurrentMarker);
                mBaiduMap.setMyLocationConfiguration(config);

            }
            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
        LocationClientOption options = new LocationClientOption();
        options.setOpenGps(true); // 打开gps
        options.setCoorType("bd09ll"); // 设置坐标类型
        options.setScanSpan(1000);
        mLocClient.setLocOption(options);
        mLocClient.start();
    }

    private void pine() {
       /* if (mstart!=null){
            mstart.remove();
        }*/
       if (mend!=null){
           mend.remove();
       }

        LocationDao locationDao = GreenDaoManager.getInstance().getNewSession().getLocationDao();
        // List<Location> list = locationDao.queryBuilder().where(LocationDao.Properties.Type.eq(0)).list();
         List<Location> list = locationDao.loadAll();
         List<LatLng> portlist=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            // 将GPS设备采集的原始GPS坐标转换成百度坐标
            CoordinateConverter converter  = new CoordinateConverter();
            converter.from(CoordinateConverter.CoordType.GPS);
          // sourceLatLng待转换坐标
            double lat = Double.valueOf(list.get(i).getLatitude());
            double lon = Double.valueOf(list.get(i).getLongitude());
            LatLng laa=new LatLng(lat,lon);
            converter.coord(laa);
            LatLng desLatLng = converter.convert();
            portlist.add(desLatLng);
            Log.i("上报的list--",list.size()+"/"+lon+"/"+lat);
            Log.i("转换为的百度坐标",desLatLng.latitude+"/"+desLatLng.longitude);
        }

        if (portlist.size()>0){
            // 构造折线点坐标
            List<LatLng> points = new ArrayList<LatLng>();
            for (int i=0;i<portlist.size();i++){
                points.add(portlist.get(i));
            }
            if (amark==1){
                //定义Maker坐标点
                LatLng point = new LatLng(points.get(0).latitude, points.get(0).longitude);
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.start);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .title("开始的位置")
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                mstart= (Marker) mBaiduMap.addOverlay(option);
            }

            //定义Maker坐标点
            LatLng pointend = new LatLng(points.get(points.size()-1).latitude, points.get(points.size()-1).longitude);
            //构建Marker图标
            BitmapDescriptor bitmapend = BitmapDescriptorFactory
                    .fromResource(R.mipmap.end);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions optionend = new MarkerOptions()
                    .position(pointend)
                    .title("结束的位置")
                    .icon(bitmapend);
            //在地图上添加Marker，并显示
          mend= (Marker) mBaiduMap.addOverlay(optionend);
           //构建分段颜色索引数组d
            List<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#FBBC48"));
            OverlayOptions ooPolyline = new PolylineOptions().width(5)
                    .colorsValues(colors).points(points);

            //添加在地图中
            mPolyline= (Polyline) mBaiduMap.addOverlay(ooPolyline);


        }

    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ceshi_baidumap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getActivity(),"点击成功"+marker.getTitle(),Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.location://定位:
                mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;//跟随定位
                location(mCurrentMode);
                break;
            case R.id.record://记录

                break;
            case R.id.jia://级别,加

                break;
            case R.id.jian://级别,减

                break;
        }
    }
}
