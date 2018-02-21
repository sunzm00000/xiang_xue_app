package com.example.fishingport.app.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.Overlay.MyLocationOverlayL;
import com.example.fishingport.app.R;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.PortList;
import com.example.fishingport.app.tools.HelpUtils;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.ItemizedOverlay;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;
import com.tianditu.android.maps.TMapLayerManager;
import com.tianditu.android.maps.overlay.MarkerOverlay;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class PortMapActivity extends BaseAppCompatActivity implements
        View.OnFocusChangeListener,MarkerOverlay.OnMarkerClickListener {
    MapView mapView;
    MyLocationOverlayL   myLocationOverlay;
    private TMapLayerManager mLayerMnger;
    MapController mapController;
    private  OverItemT overItemT;
    private  View popview;
    private  List<PortList.ResultBean.DataBean> portlist=new ArrayList<>();
    private  PortList portlietbean;
    private TextView portname;
    private  TextView portlatlon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarTitle("港口地图");
        ButterKnife.bind(this);
        mapView= (MapView) findViewById(R.id.main_mapview);
        mapView.setCachePath("/storage/emulated/1/tianditu/TdtMap/Map/");
        portlietbean= (PortList) getIntent().getSerializableExtra("sds");
        intdata();

    }
    public void intdata(){
        mapView.setMapType(MapView.TMapType.MAP_TYPE_IMG);
        mapView.preLoad();
        mapView.setBuiltInZoomControls(true);
        mapController=mapView.getController();
        mapController.setZoom(12);//设置地图级别，2比例尺为800km 4比例尺为200km  8-12 12比例尺1km
      //  mapController.setCenter(new GeoPoint((int) (38.5908 * 1E6), (int) (117.4205 * 1E6)));
        //myLocationOverlay=new MyLocationOverlayL(this,mapView);
        //myLocationOverlay.disableMyLocation();
       // myLocationOverlay.getMyLocation();
        // mapView.addOverlay(myLocationOverlay);
        //getOverlay().setGpsFollow(true);//默认定位到自己当前的位置
       mLayerMnger = new TMapLayerManager(mapView);
        String[] showlayers=new String[]{"地名","水系","铁路"};
        mLayerMnger.setLayersShow(showlayers);//设置显示全部的，地名，水系，铁路*/
       // Drawable drawable=getResources().getDrawable(R.mipmap.chuan);
       // overItemT=new OverItemT(drawable);
       // mapView.addOverlay(overItemT);
        //创建弹出框view
        popview=getLayoutInflater().inflate(R.layout.popview,null);
         portname= (TextView) popview.findViewById(R.id.text1);
        portlatlon= (TextView) popview.findViewById(R.id.text2);
        mapView.addView(popview, new MapView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                null,
                MapView.LayoutParams.TOP_LEFT
        ));
        popview.setVisibility(View.GONE);
        if (portlietbean!=null){
            portlist.addAll(portlietbean.getResult().getData());
        }
        if (portlist.size()>0){
            List<Integer> latdouble = new ArrayList<>();
            List<Integer> londouble = new ArrayList<>();
            for (int i=0;i<portlist.size();i++){

                double lat=Double.valueOf(portlist.get(i).getLatitude());
                double lon=Double.valueOf(portlist.get(i).getLongitude());
                latdouble.add((int) (lat * 1E6));
                londouble.add((int) (lon * 1E6));
                MarkerOverlay markerOverlay= new MarkerOverlay();
                markerOverlay.setIcon(getResources().getDrawable(R.mipmap.chuan));
                markerOverlay.setPosition(new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6)));
                markerOverlay.setTitle(portlist.get(i).getTitle());
                markerOverlay.setClickListener(this);
                mapView.addOverlay(markerOverlay);
            }
            int latspan = max(latdouble) - min(latdouble);
            int lonspan = max(londouble) - min(londouble);
            mapView.getController().zoomToSpan(latspan, lonspan);
            int latsum = max(latdouble) + min(latdouble);
            int lonsum = max(londouble) + min(londouble);
            Log.i("跨度", latspan + "/" + lonspan + "/" + latsum + "/" + lonsum);
            mapView.getController().setCenter(new GeoPoint(latsum / 2, lonsum / 2));
        }


    }
    MyLocationOverlay getOverlay() {
        return myLocationOverlay;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("缓存路径-", mapView.getCachePath() + "");
         mapView.removeCache();
        //mapView.removeAllOverlay();
        mapView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.removeCache();
        mapView.removeAllOverlay();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_port_map;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //焦点切换监听

    }

    @Override
    public boolean onMarkerClick(MarkerOverlay markerOverlay) {
        mapView.updateViewLayout(
                popview, new MapView.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        markerOverlay.getPosition(),
                        MapView.LayoutParams.BOTTOM_CENTER
                )
        );
        popview.setVisibility(View.VISIBLE);
        double lat=markerOverlay.getPosition().getLatitudeE6()/1E6;
        double lon=markerOverlay.getPosition().getLongitudeE6()/1E6;
        String latlon= HelpUtils.latlon(lat,lon);

        portlatlon.setText(latlon);
        portname.setText(markerOverlay.getTitle());
        return false;
    }



    class  OverItemT extends ItemizedOverlay<OverlayItem> implements Overlay.Snappable{
        private List<OverlayItem> list=new ArrayList<>();
        public OverItemT(Drawable defaultMarker) {
            super(defaultMarker);
            GeoPoint geoPoint=new GeoPoint((int)(38.5908*1E6),(int)(117.4205*1E6));
            GeoPoint geoPoint1=new GeoPoint((int)(37.5908*1E6),(int)(117.4205*1E6));
            OverlayItem overlayItem=new OverlayItem(geoPoint,"天津",1+"");
            OverlayItem overlayItem2=new OverlayItem(geoPoint1,"天津",2+"");
            overlayItem.setMarker(defaultMarker);
            overlayItem2.setMarker(defaultMarker);
            list.add(overlayItem);
            list.add(overlayItem2);
            populate();
        }

        @Override
        public int size() {
            return list.size();
        }

        @Override
        protected OverlayItem createItem(int i) {
            return list.get(i);
        }

        @Override
        protected boolean onTap(int index) {
            Log.i("index", index + "");
            if (index==-1)
                return false;
            mapView.updateViewLayout(
                    popview, new MapView.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            list.get(index).getPoint(),
                            MapView.LayoutParams.BOTTOM_CENTER
                    )
            );
                popview.setVisibility(View.VISIBLE);
            return true;
        }

         @Override
         public boolean onTap(GeoPoint geoPoint, MapView mapView) {
             popview.setVisibility(View.GONE);
             Toast.makeText(PortMapActivity.this,"点击成功",Toast.LENGTH_LONG).show();
             return super.onTap(geoPoint, mapView);
         }
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
}
