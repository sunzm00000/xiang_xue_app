package com.example.fishingport.app.ui.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.example.fishingport.app.Dao.GreenDaoManager;
import com.example.fishingport.app.MyApplication;
import com.example.fishingport.app.Overlay.MyLocationOverlayL;
import com.example.fishingport.app.Overlay.MyMarkOverlay;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.TrackConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.bean.LocationDao;
import com.example.fishingport.app.model.MaprRePortData;
import com.example.fishingport.app.presenter.TrackPresenter;
import com.example.fishingport.app.service.MapService;
import com.example.fishingport.app.service.TimerServicer;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.MapDistance;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.tools.WeiboDialogUtils;
import com.example.fishingport.app.ui.fragment.Fragment_mapView;
import com.google.gson.Gson;
import com.hyphenate.util.NetUtils;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.TMapLayerManager;
import com.tianditu.android.maps.overlay.MarkerOverlay;
import com.tianditu.android.maps.overlay.PolylineOverlay;
import com.tianditu.android.maps.renderoption.LineOption;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/*
* 发布轨迹
* */
public class MapActivity extends BaseAppCompatActivity implements OnGetGeoCoderResultListener, MarkerOverlay.OnMarkerClickListener,TrackConstract.view {
    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.radio_home)
    RadioButton radioHome;
    @BindView(R.id.radio_messages)
    RadioButton radioMessages;
    public static String TIME_CHANGED_ACTION ="MapActivity.timing";//广播标识
    @BindView(R.id.radio_noun)
    RadioButton radioNoun;
    @BindView(R.id.radio_user)
    RadioButton radioUser;
    @BindView(R.id.mapview)
    MapView mapVeiw;
    UItimeReceiver receiver;
    private MyLocationOverlay myLocationOverlay;
    private TMapLayerManager mLayerMnger;
    @BindView(R.id.location)
    LinearLayout locaton;//di定位
    MapController mapController;
    @BindView(R.id.jia)
    LinearLayout jia;
    @BindView(R.id.jian)
    LinearLayout jian;
    @BindView(R.id.nearport)
    RelativeLayout nearport;//附近港口
    @BindView(R.id.tarck)
    ImageView tarck;//轨迹
    @BindView(R.id.record)
    LinearLayout record;//开始记录、结束按钮
    @BindView(R.id.text_record)
    TextView text_record;//开始记录、结束text
    private Timer timer;//划线timer
    Timer totaltimer;//计算总距离的timer
    double ditotal = 0;
    private Timer updateTimer;//计时timer
    private int isend = 0;//轨迹是否结束的标记，当正在运行中时值为0，当结束时值为1 ，
    MarkerOverlay ma;//结束定位的MarkerOverlay
    PolylineOverlay line;//划线类，
    private int tiaotarckActivity = 0;
    private View popview;
    @BindView(R.id.totalView)
    RelativeLayout totalView;//总里程，航行时间，航行速度总的布局，默认隐藏，点击记录显示，结束隐藏
    @BindView(R.id.total)
    TextView total;//总里程
    @BindView(R.id.time)
    TextView time;//航行时间
    @BindView(R.id.speed)
    TextView speed;//航行速度
    int timeing = 1;//开始记录到结束记录的时间
    private TrackPresenter trackPresenter;
    private List<MaprRePortData> rePortDataList;
    private int report_time = 1;//划线间隔，默认2分钟map
    private int report_auto_time=1;
    private int addmarker = 0;//判断
    MarkerOverlay markerOverlay;
    private  String start_position;//开始的位置
    private String stop_position;//结束的位置
    private int stsp=0;//
    LocationClient mLocClient;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private int zhixingint=0;
    private  View view;
    private Dialog mWeiboDialog;
    private Timer servicetimer=new Timer();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    timeing += 1;//行驶的总时间 单位秒s
                    Log.i("计时",timeing+"");
                    SharedPreferencedUtils.setInteger(MapActivity.this,"timetotime",timeing);//异常退出时把正在计时的值存起来
                    String stoh = HelpUtils.secToTime(timeing);//把行驶的总时间转为h:dd:ss
                    time.setText(stoh);
                    //计算速度，总距离/总时间
                    break;
                case 1:
                    //计算距离的实时
                    calculatetotal();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Log.i("activity生命周期","onCreate");
        registerBroadcastReceiver();//注册广播，开始计时

        initview();
    }
    private void initview() {

        if (totaltimer==null){
            totaltimer=new Timer();
        }
        if (timer==null){
            timer = new Timer();
        }
        intdata();
        isEnd();
    }
    private void initload() {
        //创建弹出框view
        popview =this.getLayoutInflater().inflate(R.layout.popview, null);
        mapVeiw.addView(popview, new MapView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                null,
                MapView.LayoutParams.TOP_LEFT
        ));
        popview.setVisibility(View.GONE);
        ma = new MarkerOverlay();
    }
    /*
    * 判断是否有未结束的记录，有就继续之前的记录进行记录。
    * */
    private void isEnd() {
        if (SharedPreferencedUtils.getInteger(MapActivity.this, "isend",-1) != -1) {
            Log.i("isend", SharedPreferencedUtils.getInteger(MapActivity.this, "isend",0) + "/");
            if (SharedPreferencedUtils.getInteger(MapActivity.this, "isend",0)==1) {
                //已经结束了,
                text_record.setText("开始记录");
                time.setText("00:00:00");
            } else {
                //正在记录没有点击开始
                totalView.setVisibility(View.VISIBLE);
               // timeing=SharedPreferencedUtils.getInteger(MapActivity.this,"timetotime",1);
                Log.i("正在记录开始",timeing+"");
                MyApplication myApplication= (MyApplication) this.getApplication();
                Log.i("执行这个","执行了"+"/"+myApplication.getTimerservice());
                if (myApplication.getTimerservice()==null){
                     Log.i("执行这个","执行了");
                    myApplication.newTimerService(servicetimer);
                }
                timeing=SharedPreferencedUtils.getInteger(MapActivity.this,"timetotime",0);
                report_time=SharedPreferencedUtils.getInteger(MapActivity.this,"user_time",1);
                start_position=SharedPreferencedUtils.getString(MapActivity.this,"start_position");
                total.setText(SharedPreferencedUtils.getString(MapActivity.this,"ditotal")+"km");
                Log.i("时间",timeing+"");
                totaltimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //计算总距离，需要把当前记录到的所有的坐标点的两点的距离进行汇总
                        Message message = Message.obtain();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                },0,60*1000);
                Intent mapservice = new Intent(MapActivity.this, MapService.class);
                text_record.setText("结束");
                MapActivity.this.startService(mapservice);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ArrayList<GeoPoint> points = new ArrayList<>();//坐标集合全部
                        List<Long> idlist = new ArrayList<Long>();
                        LocationDao locationDao = GreenDaoManager.getInstance().getNewSession().getLocationDao();
                        List<com.example.fishingport.app.bean.Location> list = locationDao.loadAll();
                        for (int i = 0; i < list.size(); i++) {
                            double lat = Double.valueOf(list.get(i).getLatitude());
                            double lon = Double.valueOf(list.get(i).getLongitude());
                            GeoPoint point = new GeoPoint((int) (lat * 1E6),
                                    (int) (lon * 1E6));
                            points.add(point);
                            idlist.add(list.get(i).getId());
                        }
                          addmarker++;
                        if (points.size() > 0) {
                            plinovlery(mapVeiw, points, MapActivity.this, idlist);//划线和起点终点覆盖物
                        }
                    }
                }, 0,report_time*60*1000);
            }
        }
    }
    public void intdata() {
        trackPresenter=new TrackPresenter(this);
        //取划线间隔
        report_time(SharedPreferencedUtils.getString(MapActivity.this,"token"));
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mapVeiw.setMapType(MapView.TMapType.MAP_TYPE_TERRAIN);
        mapVeiw.preLoad();
        myLocationOverlay = new MyLocationOverlayL(MapActivity.this, mapVeiw);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.getMyLocation();
        mapController = mapVeiw.getController();
        mapController.setZoom(18);//设置地图级别，2比例尺为800km 4比例尺为200km 12比例尺1km
        if (myLocationOverlay.getMyLocation()==null){
            Toast.makeText(MapActivity.this,"定位为空",Toast.LENGTH_LONG).show();
        }else {
            mapVeiw.getController().setCenter(myLocationOverlay.getMyLocation());
            mapVeiw.addOverlay(myLocationOverlay);
        }
        mLayerMnger = new TMapLayerManager(mapVeiw);
        // mapVeiw.setBuiltInZoomControls(true);
        String[] showlayers = new String[]{"地名", "水系", "铁路"};
        mLayerMnger.setLayersShow(showlayers);//设置显示全部的，地名，水系，铁路
        initload();
    }
    @OnClick({R.id.record,R.id.location,R.id.tarck ,
            R.id.nearport,R.id.radio_home, R.id.radio_messages,
            R.id.radio_noun, R.id.radio_user, R.id.img_add,R.id.jia,R.id.jian})
    public void onClick(View view) {
        Intent intent=new Intent(MapActivity.this,MainActivity.class);
        switch (view.getId()) {
            case R.id.jia://放大一级
                mapController.zoomIn();
                break;
            case R.id.jian://缩小一级
                mapController.zoomOut();
                break;
            case R.id.radio_home:
                intent.putExtra("ty","0");
                startActivity(intent);
                break;
            case R.id.radio_messages:
                intent.putExtra("ty","1");
                startActivity(intent);
                break;
            case R.id.radio_noun:
                intent.putExtra("ty","2");
                startActivity(intent);
                break;
            case R.id.radio_user:
                intent.putExtra("ty","3");
                startActivity(intent);
                break;
            case R.id.img_add:
                //mCurrentIndex=4;
                break;
            case R.id.nearport://附近港口
                Intent intent1 = new Intent(MapActivity.this, NearbyPortActivity.class);
                startActivity(intent1);
                break;
            case R.id.tarck://轨迹
                Intent intent2= new Intent(MapActivity.this, TarckActivity.class);
                intent.putExtra("type","0");
                startActivity(intent2);
                break;
            case R.id.location://定位按钮
                myLocationOverlay = new MyLocationOverlayL(this, mapVeiw);
                myLocationOverlay.enableMyLocation();
                myLocationOverlay.getMyLocation();
                mapController = mapVeiw.getController();
                mapController.setZoom(18);//设置地图级别，2比例尺为800km 4比例尺为200km 12比例尺1km
                mapVeiw.getController().setCenter(myLocationOverlay.getMyLocation());
                break;
            case R.id.record://开始记录结束轨迹
                start_RePoort();
                break;
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }
    @Override
    protected int getFragmentContentId() {
        return 0;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("activity生命周期","onResume");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("activity生命周期","onStart");
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i("activity生命周期","onSaveInstanceState");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("activity生命周期","onDestroy");
/*        if (updateTimer!=null){
            updateTimer.cancel();
        }
        if (totaltimer!=null){
            totaltimer.cancel();
        }
        if (timer!=null){
            timer.cancel();
        }*/
         servicetimer.cancel();
        unregisterReceiver(receiver);//注销广播
        SharedPreferencedUtils.setString(MapActivity.this,"ditotal",ditotal+"");
        SharedPreferencedUtils.setInteger(this,"timetotime",timeing);//异常退出时把正在计时的值存起来
        Log.i("正在记录结束",timeing+"");
        if (start_position!=null) {
            SharedPreferencedUtils.setString(this, "start_position", start_position);
        }
        if (mLocClient!=null){
            mLocClient.stop();
        }


        Log.i("map结束","map结束了");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("activity生命周期","onRestart");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("activity生命周期","onPause");
    }
    private void calculatetotal() {
        //计算距离
        LocationDao locationDao =GreenDaoManager.getInstance().getNewSession().getLocationDao();
        List<com.example.fishingport.app.bean.Location> list = locationDao.loadAll();//查询所有的
        double ds=0;//距离
        if (list!=null&&list.size()>=2){
            for (int i=0;i<list.size();i++){
                double dd=0;//每个相邻经纬度的距离
                if (i==0){
                    dd= MapDistance.getDistance(
                            list.get(0).getLatitude(),
                            list.get(0).getLongitude(),
                            list.get(1).getLatitude(),
                            list.get(1).getLongitude());
                }else if (i==list.size()-1){

                }else {
                    dd=MapDistance.getDistance(
                            list.get(i).getLatitude(),
                            list.get(i).getLongitude(),
                            list.get(i+1).getLatitude(),
                            list.get(i+1).getLongitude());
                }
                ds+=dd;//总的距离
            }
            Log.i("总的距离",ds+"");
            ditotal=0;
            ditotal=ds;//总的距离
            double td=timeing/60/60;
            DecimalFormat df=new DecimalFormat("#.00");
            double sp=(ds/timeing)*3600;
            BigDecimal bg = new BigDecimal(sp);
            double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
            BigDecimal tobg = new BigDecimal(ds);
            double f2= tobg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
            total.setText(f2+"km");
            speed.setText(f1+"km/h");
        }
    }
    /*
     * 点击记录,分两种情况，一种是开始记录，一种是上报
     * */
    private void start_RePoort() {
        if (text_record.getText().equals("开始记录")) {
            //说明在退出之前已经点击了结束，重新开始新的记录
            WindowManager wm = (WindowManager)this
                    .getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            //点击记录接口，获取到上报id，传递手机x,y轴宽度单位px
            if (LocationUtils.isGpsEnabled()){
                if(LocationUtils.isLocationEnabled()){
                    start_report(SharedPreferencedUtils.getString(this,"token"), width,height);
                }else {
                    HelpUtils.warning(this,"GPS信号不好请稍后重试");
                }
            }else {
                //打开gps页面
                LocationUtils.openGpsSettings();
            }
        } else {
            //当已经开始记录点击按钮走下面的上传轨迹
            if (NetUtils.hasNetwork(MapActivity.this) ){//判断网络是否可见
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(MapActivity.this, "加载中...");
                record.setClickable(false);
                reverseGeoCode();//此方法是定位到当前点击结束的时候的位置，上传轨迹的时候需要上传位置信息比如朝阳区。这个是用百度地图的地理逆编码的方法
                stsp=2;//这个是判断是结束还是开始记录的时候的标志位
            }else {
                HelpUtils.warning(this,"暂无网络请稍后重试");
            }
        }
    }

    private void reverseGeoCode() {
        android.location.Location location=HelpUtils.locationgps(this);
        if (location!=null){
            // 反Geo搜索
            Log.i("定位页面定位",location.getLatitude()+"//"+location.getLongitude());
            LatLng ptCenter = new LatLng(location.getLatitude(),location.getLongitude());
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
        }else {
            //地理反编码
            mLocClient = new LocationClient(this);
            mLocClient.registerLocationListener(new BDLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    // map view 销毁后不在处理新接收的位置
                    if (bdLocation == null) {
                        return;
                    }
                    double[] dd = HelpUtils.gcj02towgs84(bdLocation.getLongitude(), bdLocation.getLatitude());//百度定位的坐标（GCJ02）转wgs84这个天地图用的是wgs84坐标
                    // 反Geo搜索
                    LatLng ptCenter = new LatLng(dd[1], dd[0]);
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
                    mLocClient.stop();
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

    /*
    * 记录轨迹结束，上报轨迹相关方法
    * */
    private void netreport() {
        //结束轨迹录制，跳转到我的轨迹页面
        Intent mapservice = new Intent(this, MapService.class);
        this.stopService(mapservice);
        ArrayList<GeoPoint> points = new ArrayList<>();//坐标集合全部
        final ArrayList<Point> pointlist = new ArrayList<>();
        //从数据库取到经纬度，划线
        HelpUtils.pathgps(this);
        LocationDao locationDao = GreenDaoManager.getInstance().getNewSession().getLocationDao();
        List<com.example.fishingport.app.bean.Location> list = locationDao.loadAll();
        Log.i("执行了---liu","执行a"+"/总的list"+list.size());
        if (list.size() > 0) {
            //数据库中有经纬度
          /*调节地图坐标点全部显示的的开始*/
            List<Integer> latdouble = new ArrayList<>();
            List<Integer> londouble = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getLongitude()!=null&&list.get(i).getLongitude()!=null){
                    double lat = Double.valueOf(list.get(i).getLatitude());
                    double lon = Double.valueOf(list.get(i).getLongitude());
                    latdouble.add((int) (lat * 1E6));
                    londouble.add((int) (lon * 1E6));
                }
            }
            int latspan = max(latdouble) - min(latdouble);
            int lonspan = max(londouble) - min(londouble);
            mapVeiw.getController().zoomToSpan(latspan, lonspan);
            int latsum = max(latdouble) + min(latdouble);
            int lonsum = max(londouble) + min(londouble);
            Log.i("跨度", latspan + "/" + lonspan + "/" + latsum + "/" + lonsum);
            mapVeiw.getController().setCenter(new GeoPoint(latsum / 2, lonsum / 2));
             /*调节地图坐标点全部显示的的结束*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LocationDao locationDao1 = GreenDaoManager.getInstance().getNewSession().getLocationDao();
                    List<com.example.fishingport.app.bean.Location> list1 = locationDao1.loadAll();
                    rePortDataList = new ArrayList<>();
                    for (int i = 0; i < list1.size(); i++) {
                        calculatetotal();
                        Point point = mapVeiw.getProjection().toPixels(new GeoPoint((int) (Double.valueOf(list1.get(i).getLatitude()) * 1E6),
                                (int) (Double.valueOf(list1.get(i).getLongitude()) * 1E6)), null);
                        MaprRePortData rePortData = new MaprRePortData();
                        rePortData.setReport_time(list1.get(i).getTime() + "");
                        rePortData.setLat(list1.get(i).getLatitude());
                        rePortData.setLng(list1.get(i).getLongitude());
                        if (i == list1.size() - 1) {
                            rePortData.setIs_end(1);
                        } else {
                            rePortData.setIs_end(0);
                        }
                        rePortData.setDistance_average("" + speed.getText());//平均速度
                       //int dd= list1.get(list1.size()-1).getTime()- list1.get(0).getTime();
                        rePortData.setTime_count("" + time.getText());//总时间
                        rePortData.setDistance_count("" + total.getText());//总距离
                        rePortData.setX(point.x);
                        rePortData.setY(point.y);
                        rePortDataList.add(rePortData);
                        pointlist.add(point);//转换后的坐标
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            Log.i("上报的数据token", start_position + "/" + stop_position + "/" + "token" + SharedPreferencedUtils.getString(MapActivity.this, "token"));
                            Log.i("上报的数据[]", "[]" + gson.toJson(rePortDataList));
                            Report(SharedPreferencedUtils.getString(MapActivity.this, "token"),
                                    SharedPreferencedUtils.getInteger(MapActivity.this, "trajectory_id", 0) + "",
                                    gson.toJson(rePortDataList), start_position, stop_position);
                        }
                    }, 200);
                }
            }, 500);
        } else {
            HelpUtils.warning(MapActivity.this,"没有数据不能上报");
            clear();
        }
    }

    public void clear(){
        record.setClickable(true);
        WeiboDialogUtils.closeDialog(mWeiboDialog);
        total.setText("0km");
        speed.setText("0km/h");
        time.setText("0");
        text_record.setText("开始记录");
        totalView.setVisibility(View.GONE);
        if (totaltimer!=null){
            totaltimer.cancel();
        }
        if (timer!=null){
            timer.cancel();
        }
        isend = 1;
        timeing=0;
        SharedPreferencedUtils.setInteger(MapActivity.this,"timetotime",timeing);
        SharedPreferencedUtils.setInteger(MapActivity.this, "isend", isend);
        if (timer!=null){
            timer.cancel();
        }
        MyApplication myApplication= (MyApplication) this.getApplication();
        Log.i("timer",myApplication.getTimer()+"/");
        if ( myApplication.getTimer()!=null){
            Log.i("timer结束了","timer结束了");
            myApplication.getTimer().cancel();
        }
        myApplication.stopTimerService();

        Log.i("是否执行了","执行了clear"+"/"+SharedPreferencedUtils.getInteger(MapActivity.this,"timetotime",0));
        //当上传记录成功里清除所画的线，marker标记等
        mapVeiw.removeOverlay(ma);
        mapVeiw.removeOverlay(line);
        if (markerOverlay!=null){
            mapVeiw.removeOverlay(markerOverlay);
        }
        addmarker=0;
        mLocClient.stop();
    }
    private void start_kaishi() {
        Log.i("间隔",SharedPreferencedUtils.getInteger(this,"user_time",0)+"");
        LocationDao locationDao = GreenDaoManager.getInstance().getNewSession().getLocationDao();
        // List<Location> list = locationDao.queryBuilder().where(LocationDao.Properties.Type.eq(0)).list();
        List<com.example.fishingport.app.bean.Location> list = locationDao.loadAll();
        Intent mapservice = new Intent(this, MapService.class);
        this.startService(mapservice);
        updateTimer = new Timer();
        timeing=0;
        //在保证timer长时间存活的情况下只有一个timer对象在运行
        MyApplication myApplication= (MyApplication) this.getApplication();
         myApplication.newTimerService(servicetimer);
      /*  if (updateTimer!=null){
            updateTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = Message.obtain();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }, 0, 1000);
        }*/
        totaltimer=new Timer();
        totaltimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //计算总距离，需要把当前记录到的所有的坐标点的两点的距离进行汇总
                Message message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);
            }
        },5000,60*1000);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //根据从数据库取出来的经纬度划线
                ArrayList<GeoPoint> points = new ArrayList<>();//坐标集合全部
                List<Long> idlist = new ArrayList<Long>();
                LocationDao locationDao = GreenDaoManager.getInstance().getNewSession().getLocationDao();
                // List<Location> list = locationDao.queryBuilder().where(LocationDao.Properties.Type.eq(0)).list();
                List<com.example.fishingport.app.bean.Location> list = locationDao.loadAll();
                for (int i = 0; i < list.size(); i++) {
                    double lat = Double.valueOf(list.get(i).getLatitude());
                    double lon = Double.valueOf(list.get(i).getLongitude());
                    GeoPoint point = new GeoPoint((int) (lat * 1E6),
                            (int) (lon * 1E6));
                    points.add(point);
                    Log.i("上报的list--",list.size()+"/"+lon+"/"+lat);
                    idlist.add(list.get(i).getId());
                }
                addmarker++;
                if (points.size() > 0) {
                    plinovlery(mapVeiw, points, MapActivity.this, idlist);
                }
            }
        },5000, SharedPreferencedUtils.getInteger(this,"user_time",1)*60*1000);
    }
    public  void  plinovlery(MapView mMapView,ArrayList<GeoPoint> geoPoints,Context context,List<Long> idlist) {
        Log.i("执行了=","执行了");
        markoverlay(mapVeiw, geoPoints,MapActivity.this, idlist);
        line = new PolylineOverlay();//多边形覆盖物绘制类
        LineOption option = new LineOption();//划线类
        option.setStrokeWidth((int) (2.0f * context.getResources().getDisplayMetrics().density));//粗细
        option.setDottedLine(false);//是否虚线
        option.setStrokeColor(Color.parseColor("#FBBC48"));//线的颜色
        line.setOption(option);//设置绘制参数，颜色，粗细等
        line.setPoints(geoPoints);//设置绘制的坐标参数
        mMapView.addOverlay(line);
        //getOverlay().setGpsFollow(true);//跟随位置
    }
    public void markoverlay(MapView mMapView,ArrayList<GeoPoint> points,Context context,List<Long> idlist){
        for (int i=0;i<points.size();i++){
            if (i==0&&addmarker==1){
                markerOverlay= new MarkerOverlay();
                markerOverlay.setIcon(context.getResources().getDrawable(R.mipmap.start));
                markerOverlay.setPosition(points.get(0));
                markerOverlay.setClickListener(MapActivity.this);
                mMapView.addOverlay(markerOverlay);
                Log.i("执行了-","执行了");
            }
           if (i==points.size()-1){
                Log.i("执行了","执行了");
                ma.setIcon(context.getResources().getDrawable(R.mipmap.end));
                ma.setPosition(points.get(i));
                ma.setClickListener(MapActivity.this);
                mMapView.addOverlay(ma);
            }
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

    @Override
    public boolean onMarkerClick(MarkerOverlay markerOverlay) {
        mapVeiw.updateViewLayout(
                popview, new MapView.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        markerOverlay.getPosition(),
                        MapView.LayoutParams.BOTTOM_CENTER
                )
        );
        if (popview.getVisibility()==View.VISIBLE){
            popview.setVisibility(View.GONE);
        }else {
            popview.setVisibility(View.VISIBLE);
        }
        return false;
    }

    @Override
    public void showErrMsg(String msg) {
    }

    @Override
    public void showInfo(String string) {
        Log.i("上报记录",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            JSONObject result=object.optJSONObject("result");
            if (status.optInt("code")==1000){
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                record.setClickable(true);
                report_time=Integer.valueOf(result.optString("report_user_time"));
                report_auto_time=Integer.valueOf(result.optString("report_auto_time"));
                SharedPreferencedUtils.setInteger(MapActivity.this,"user_time",report_time);
                SharedPreferencedUtils.setInteger(MapActivity.this,"auto_time",report_auto_time);
                tiaotarckActivity =1;
                Intent intent2 = new Intent(MapActivity.this, TarckActivity.class);
                startActivity(intent2);
                LocationDao locationDao1 = GreenDaoManager.getInstance().getNewSession().getLocationDao();
                Log.i("剩余的数据库的list-",locationDao1.loadAll().size()+"/");
                locationDao1.deleteAll();
                Log.i("剩余的数据库的list",locationDao1.loadAll().size()+"/");
                clear();//初始化数据
                finish();
            }else {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                record.setClickable(true);
                HelpUtils.warning(MapActivity.this,""+status.optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            WeiboDialogUtils.closeDialog(mWeiboDialog);
            record.setClickable(true);
        }
    }

    @Override
    public void autoInfo(String string) {
        //划线间隔
        Log.i("划线间隔",string);
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            JSONObject result=object.optJSONObject("result");
            if (status.optInt("code")==1000){
                report_time=result.optInt("report_time");
                SharedPreferencedUtils.setInteger(MapActivity.this,"user_time",report_time);}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void startInfo(String string) {
        try {
            JSONObject object=new JSONObject(string);
            JSONObject status=object.optJSONObject("status");
            JSONObject result=object.optJSONObject("result");
            if (status.optInt("code")==1000){
                reverseGeoCode();//地理
                stsp=1;
                int trajectory_id=result.optInt("trajectory_id");
                SharedPreferencedUtils.setInteger(MapActivity.this,"trajectory_id",trajectory_id);
                //获取记录id开始记录轨迹
                totalView.setVisibility(View.VISIBLE);
                isend = 0;
                SharedPreferencedUtils.setInteger(MapActivity.this, "isend", isend);
                text_record.setText("结束");
                zhixingint=0;
                Log.i("isend","//"+SharedPreferencedUtils.getInteger(MapActivity.this,"isend",-1));
                start_kaishi();//开始记录
            }else {
                Toast.makeText(MapActivity.this,""+status.optString("message")+"",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        //百度根据
    }
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            String address=reverseGeoCodeResult.getAddress();
            Log.i("地址",address+"");
            if (address!=null) {
               String citys[]=HelpUtils.getcityorcity_addr(address);
                if (stsp==1){
                    start_position=citys[0]+""+citys[1];
                    zhixingint=0;
                }else if(stsp==2){
                    stop_position=citys[0]+""+citys[1];
                    Log.i("执行了---liu","执行b");
                    netreport();//获取地理位置后再进行提交操作数据
                }
            }else {
                if (stsp==1){
                    start_position="未知";
                    zhixingint=0;
                }else if(stsp==2){
                    stop_position="未知";
                    Log.i("执行了---liu","执行b");
                    netreport();//获取地理位置后再进行提交操作数据
                }
            }

    }

    class  myMarkOverlay extends MyMarkOverlay {
        public myMarkOverlay(Context context, Drawable drawable, GeoPoint geoPoint) {
            super(context, drawable, geoPoint);
        }
        @Override
        public boolean onTap(GeoPoint p, MapView mapView) {

            return super.onTap(p, mapView);
        }
    }

    //取划线和记录间隔
    public  void report_time(String token){
        Map<String,String> signmap= Utils.getSignParams(MapActivity.this,token);
        Map<String,String> map=Utils.getMap(MapActivity.this,token);
        map.put("sign",Utils.getMd5StringMap(signmap));
        trackPresenter.getreport_time(map);
    }
    //点击记录按钮接口请求
    public void  start_report(String token,int x,int y){
        Map<String,String> signmap=Utils.getSignParams(MapActivity.this,token);
        signmap.put("client_screen",x+","+y);

        Map<String,String> map=Utils.getMap(MapActivity.this,token);
        map.put("client_screen",x+","+y);

        map.put("sign",Utils.getMd5StringMap(signmap));
        trackPresenter.start_report(map);
    }
    //记录轨迹上报接口
    public void Report(String token,String trajectory_id,String report_data,
                       String start_position,String stop_position){
        Map<String,String> signmap=Utils.getSignParams(MapActivity.this,token);
        signmap.put("trajectory_id",trajectory_id);
        Map<String,String> map=Utils.getMap(MapActivity.this,token);
        map.put("token",token);
        map.put("trajectory_id",trajectory_id);
        map.put("report_data",report_data);
        map.put("start_position",start_position);
        map.put("stop_position",stop_position);
        map.put("sign",Utils.getMd5StringMap(signmap));
        trackPresenter.report(map);
    }

    /**
     * 注册广播
     */
    private void registerBroadcastReceiver(){
        receiver= new UItimeReceiver();
        IntentFilter filter = new IntentFilter(TIME_CHANGED_ACTION);
        registerReceiver(receiver, filter);
    }
    //广播
    public class UItimeReceiver extends BroadcastReceiver{

       @Override
       public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
           Log.i("服务的计时器==","执行了"+action);
           if(TIME_CHANGED_ACTION.equals(action)){
               timeing=intent.getIntExtra("time",0);
               Log.i("服务的计时器",timeing+"");
               Log.i("计时",timeing+"");
               SharedPreferencedUtils.setInteger(MapActivity.this,"timetotime",timeing);//异常退出时把正在计时的值存起来
               String stoh = HelpUtils.secToTime(timeing);//把行驶的总时间转为h:dd:ss
               time.setText(stoh);

           }
       }
   }

}
