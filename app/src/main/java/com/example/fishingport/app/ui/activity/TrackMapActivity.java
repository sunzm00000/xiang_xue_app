package com.example.fishingport.app.ui.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.Overlay.MyLocationOverlayL;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.TrackConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.TrackRecordlist;
import com.example.fishingport.app.model.TrajectoryList;
import com.example.fishingport.app.presenter.TrackPresenter;
import com.example.fishingport.app.service.MapService;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.ui.fragment.Fragment_mapView;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.overlay.MarkerOverlay;
import com.tianditu.android.maps.overlay.PolylineOverlay;
import com.tianditu.android.maps.renderoption.LineOption;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackMapActivity extends BaseAppCompatActivity implements TrackConstract.view,MarkerOverlay.OnMarkerClickListener{
     @BindView(R.id.main_mapview)
    MapView mapView;
    private MyLocationOverlay myLocationOverlay;
    MapController mapController;
    private  String trajectory_id=null;
    private TrackPresenter trackPresenter;
    private  String speed="0km/h";//航行速度
    private  String time="00:00:00";//航行时间
    private String total="28km";//航行距离
    IWXAPI api;
    @BindView(R.id.speed)
    TextView textspeed;//
    @BindView(R.id.time)
    TextView textime;
    @BindView(R.id.total)
    TextView textotal;//航行距离
    @BindView(R.id.layout)
    RelativeLayout layout;
    MarkerOverlay markerOverlayend;
    MarkerOverlay markerOverlaystart;
    private List<TrackRecordlist.ResultBean.DataBean.ListBean> listbean=new ArrayList<>();
    private View popview;
    private  TextView textpot;//弹出框的名称
    private  TextView textname;//经纬度点的名称
    private  TextView textlatlon;//经纬度
    private String start_position;//开始的起始位置
    private String stop_position;//结束位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBarTitle("轨迹地图");
        api = WXAPIFactory.createWXAPI(this, "wx0e5b8dcd46f493b8");
        api.registerApp("wx0e5b8dcd46f493b8");
        initview();
        initmapView();
    }
    private void initview() {
        trajectory_id=getIntent().getStringExtra("id");
        trackPresenter=new TrackPresenter(this);
        if (trajectory_id!=null){
            recordlist(SharedPreferencedUtils.getString(TrackMapActivity.this,"token"),trajectory_id);
        }
        popview=getLayoutInflater().inflate(R.layout.popview,null);
          textpot= (TextView) popview.findViewById(R.id.text);
        textname= (TextView) popview.findViewById(R.id.text1);
        textlatlon= (TextView) popview.findViewById(R.id.text2);
        mapView.addView(popview, new MapView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                null,
                MapView.LayoutParams.TOP_LEFT
        ));
        popview.setVisibility(View.GONE);
        getImgRight().setImageDrawable(getResources().getDrawable(R.mipmap.fishingcircle_shape_icon));
        ViewGroup.LayoutParams params = getImgRight().getLayoutParams();
        params.height=50;
        params.width =70;
        getImgRight().setLayoutParams(params);
        getImgRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
    }
    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.trackmapshare, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        contentView.findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.fishingcirle_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isWebchatAvaliable()) {
                    HelpUtils.warning(TrackMapActivity.this,"请先安装微信");
                    return;
                }
                WXWebpageObject webpage = new WXWebpageObject();
                String url="http://201704yg.alltosun.net/trajectory/m/trajectory_map?trajectory_id="+trajectory_id+"&fromApp=1";
                Log.i("分享的url",url+"");
                webpage.webpageUrl =url;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title ="渔友圈";
                msg.description = "来自渔网通的分享";
                //  Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
                //    msg.thumbData = Util.bmpToByteArray(thumb, true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction =System.currentTimeMillis()+"";
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                api.sendReq(req);
          /*      WechatShareManager.ShareContentText mShareContentText = (WechatShareManager.ShareContentText) mShareManager.getShareContentText(getIntent().getStringExtra("url"));
                mShareManager.shareByWebchat(mShareContentText, WechatShareManager.WECHAT_SHARE_TYPE_TALK);*/
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.fishingcirle_weixin_pengyouquan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isWebchatAvaliable()) {
                    HelpUtils.warning(TrackMapActivity.this,"请先安装微信");
                    return;}
                WXWebpageObject webpage = new WXWebpageObject();
                String url="http://201704yg.alltosun.net/trajectory/m/trajectory_map?trajectory_id="+trajectory_id+"&fromApp=1";
                Log.i("分享的url",url+"");
                webpage.webpageUrl =url;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title ="渔友圈";
                msg.description = "来自渔网通的分享";
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = System.currentTimeMillis()+"";
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                api.sendReq(req);
      /*          WechatShareManager.ShareContentText mShareContentText = (WechatShareManager.ShareContentText) mShareManager.getShareContentText(getIntent().getStringExtra("url"));
                mShareManager.shareByWebchat(mShareContentText, WechatShareManager.WECHAT_SHARE_TYPE_FRENDS);*/
                popupWindow.dismiss();
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.showAtLocation(layout, Gravity.TOP,0,0);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
    }
    private boolean isWebchatAvaliable() {
        //检测手机上是否安装了微信
        try {
            getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private void initmapView() {
        myLocationOverlay=new MyLocationOverlayL(TrackMapActivity.this,mapView);
        mapView.setCachePath("/storage/emulated/1/tianditu/TrackMap/Map/");
        mapView.setMapType(MapView.TMapType.MAP_TYPE_IMG);
        mapView.preLoad();
        myLocationOverlay = new MyLocationOverlayL(TrackMapActivity.this, mapView);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.getMyLocation();
        mapController = mapView.getController();
        mapController.setZoom(18);//设置地图级别，2比例尺为800km 4比例尺为200km 12比例尺1km
        mapView.getController().setCenter(myLocationOverlay.getMyLocation());
        mapView.addOverlay(myLocationOverlay);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_track_map2;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void showErrMsg(String msg) {
        Log.i("错误",msg.toString()+"");

    }

    @Override
    public void showInfo(String string) {
        //
        Log.i("列表",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            JSONObject result=object.optJSONObject("result");
            if (status.optInt("code")==1000){
             JSONObject dataobject=result.optJSONObject("data");
                JSONObject info=dataobject.optJSONObject("info");
             textime.setText(info.optString("time_count")+"");
             textotal.setText(info.optString("distance_count")+"km");
             textspeed.setText(info.optString("distance_average")+"km/h");
                start_position=info.optString("start_position");
                stop_position=info.optString("stop_position");
                Gson gson=new Gson();
                TrackRecordlist trackRecordlist=gson.fromJson(string, TrackRecordlist.class);
                listbean.addAll(trackRecordlist.getResult().getData().getList());
                ArrayList<GeoPoint> points=new ArrayList<>();
                if (listbean.size()>0){
                    List<Integer> latdouble = new ArrayList<>();
                    List<Integer> londouble = new ArrayList<>();
                    for (int i=0;i<listbean.size();i++){
                        double slat = Double.valueOf(listbean.get(i).getLat());
                        double slon = Double.valueOf(listbean.get(i).getLng());
                        latdouble.add((int) (slat * 1E6));
                        londouble.add((int) (slon * 1E6));
                        double lat=Double.valueOf(listbean.get(i).getLat());
                        double lon=Double.valueOf(listbean.get(i).getLng());
                        GeoPoint geopoint=new GeoPoint((int)(lat*1E6),(int)(lon*1E6));
                        points.add(geopoint);
                    }
                    //根据经纬度跨度缩放地图保证所有的点都呈现在视野内
                    int latspan = max(latdouble) - min(latdouble);
                    int lonspan = max(londouble) - min(londouble);
                    mapView.getController().zoomToSpan(latspan, lonspan);
                    int latsum = max(latdouble) + min(latdouble);
                    int lonsum = max(londouble) + min(londouble);
                    mapView.getController().setCenter(new GeoPoint(latsum / 2, lonsum / 2));
                    if (points.size()>0) {
                        playline(mapView,points,TrackMapActivity.this);
                    }
                }
            }else {
                Toast.makeText(TrackMapActivity.this,status.optString("message"),Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void playline(MapView mMapView, ArrayList<GeoPoint> geoPoints, Context context) {
        //划线的方法
        PolylineOverlay line=new PolylineOverlay();//多边形覆盖物绘制类;//划线类，
        LineOption option = new LineOption();//划线类
        option.setStrokeWidth((int) (2.0f * context.getResources().getDisplayMetrics().density));//粗细
        option.setDottedLine(false);//是否虚线
        option.setStrokeColor(Color.parseColor("#FBBC48"));//线的颜色
        line.setOption(option);//设置绘制参数，颜色，粗细等
        line.setPoints(geoPoints);//设置绘制的坐标参数
        mMapView.addOverlay(line);
        for (int i=0;i<geoPoints.size();i++){
        if (i==0){
            markerOverlaystart= new MarkerOverlay();
            markerOverlaystart.setIcon(context.getResources().getDrawable(R.mipmap.start));
            markerOverlaystart.setPosition(geoPoints.get(0));
            markerOverlaystart.setTitle(start_position);
            markerOverlaystart.setClickListener(TrackMapActivity.this);
            mMapView.addOverlay(markerOverlaystart);
        }
        if (i==geoPoints.size()-1){

            markerOverlayend= new MarkerOverlay();
            markerOverlayend.setIcon(context.getResources().getDrawable(R.mipmap.end));
            markerOverlayend.setPosition(geoPoints.get(i));
            markerOverlayend.setTitle(stop_position);
            markerOverlayend.setClickListener(TrackMapActivity.this);
            mMapView.addOverlay(markerOverlayend);}
        }
    }

    @Override
    public void autoInfo(String string) {

    }

    @Override
    public void startInfo(String string) {

    }

    public void recordlist(String token,String trajectory_id){
        Map<String,String> signmap= Utils.getSignParams(TrackMapActivity.this,token);
        signmap.put("trajectory_id",trajectory_id);
        Map<String,String> map=Utils.getMap(TrackMapActivity.this,token);
        map.put("trajectory_id",trajectory_id);
        map.put("sign",Utils.getMd5StringMap(signmap));
        trackPresenter.recordlist(map);
    }

    /*
 * 求最大值，
 * */
    public  int max(List<Integer> list ){
        int max=list.get(0);
        for (int i=0;i<list.size();i++){
            if (list.get(i)>max){
                max=list.get(i);
            }
        }
        return  max;
    }
    /*
    * 求最小值
    * */
    public  int min(List<Integer> list){
        int min=list.get(0);
        for ( int i=0;i<list.size();i++){
            if (list.get(i)<min){
                min=list.get(i);
            }
        }
        return min;
    }

    @Override
    public boolean onMarkerClick(MarkerOverlay markerOverlay) {
      GeoPoint geopoint=markerOverlay.getPosition();
        double lat=geopoint.getLatitudeE6()/1E6;
        double lon=geopoint.getLongitudeE6()/1E6;
        mapView.updateViewLayout(
                popview, new MapView.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        markerOverlay.getPosition(),
                        MapView.LayoutParams.BOTTOM_CENTER
                )
        );
     /*   if (popview.getVisibility()==View.VISIBLE){
            popview.setVisibility(View.GONE);
        }else {*/
            popview.setVisibility(View.VISIBLE);
            textpot.setText("名称");
            textname.setText(markerOverlay.getTitle());
            textlatlon.setText(HelpUtils.latlon(lat,lon));
       // }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.removeOverlay(markerOverlayend);
        mapView.removeOverlay(markerOverlaystart);
        mapView.removeOverlay(myLocationOverlay);
        mapView.removeAllOverlay();
        mapView.removeCache();
        //mapView.onDestroy();
    }
}
