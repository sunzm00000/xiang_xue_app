package com.example.fishingport.app.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.*;
import com.example.fishingport.app.Dao.GreenDaoManager;
import com.example.fishingport.app.R;
import com.example.fishingport.app.bean.Location;
import com.example.fishingport.app.bean.LocationDao;
import com.example.fishingport.app.service.LocationServices;
import com.example.fishingport.app.service.MapService;
import com.example.fishingport.app.ui.activity.AkeyWebActivity;
import com.example.fishingport.app.ui.activity.LaunchActivity;
import com.example.fishingport.app.ui.activity.LoginActivity;
import com.example.fishingport.app.ui.activity.MainActivity;
import com.tianditu.android.maps.overlay.MarkerOverlay;
import com.trycatch.mysnackbar.TSnackbar;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.blankj.utilcode.util.LocationUtils.getAddress;
import static com.blankj.utilcode.util.LocationUtils.register;


/**
 * Created by Lenovo on 2017/4/14.
 */
public class HelpUtils {

    public final static String CoorType_GCJ02 = "gcj02";
    public final static String CoorType_BD09LL= "bd09ll";
    public final static String CoorType_BD09MC= "bd09";
    final static int mark=0;
    /***
     *61 ： GPS定位结果，GPS定位成功。
     *62 ： 无法获取有效定位依据，定位失败，请检查运营商网络或者wifi网络是否正常开启，尝试重新请求定位。
     *63 ： 网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位。
     *65 ： 定位缓存的结果。
     *66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果。
     *67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果。
     *68 ： 网络连接失败时，查找本地离线定位时对应的返回结果。
     *161： 网络定位结果，网络定位定位成功。
     *162： 请求串密文解析失败。
     *167： 服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位。
     *502： key参数错误，请按照说明文档重新申请KEY。
     *505： key不存在或者非法，请按照说明文档重新申请KEY。
     *601： key服务被开发者自己禁用，请按照说明文档重新申请KEY。
     *602： key mcode不匹配，您的ak配置过程中安全码设置有问题，请确保：sha1正确，“;”分号是英文状态；且包名是您当前运行应用的包名，请按照说明文档重新申请KEY。
     *501～700：key验证失败，请按照说明文档重新申请KEY。
     */

    public static float[] EARTH_WEIGHT = {0.1f,0.2f,0.4f,0.6f,0.8f}; // 推算计算权重_地球
    //public static float[] MOON_WEIGHT = {0.0167f,0.033f,0.067f,0.1f,0.133f};
    //public static float[] MARS_WEIGHT = {0.034f,0.068f,0.152f,0.228f,0.304f};
    /**
     * 获取wifimac地址
     */
    public static String getMac() {
        String macSerial = "";
        try {
            Process pp = null;
            try {
                pp = Runtime.getRuntime().exec(
                        "cat /sys/class/net/wlan0/address");
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null) {
                macSerial += line.trim();
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return macSerial;
    }

    /*
   * LocationhelpUtils gps定位
   * */
    public  static  void  pathgps(final Context context){

        if (LocationUtils.isGpsEnabled()==false){
            LocationUtils.openGpsSettings();
        }else {
            android.location.Location gps = LocationhelpUtils.getGPSLocation(context);
            Log.i("gps",gps+"");
            if (gps!=null){
                //存到数据库
                LocationDao locationDao = GreenDaoManager.getInstance().getNewSession().getLocationDao();
                Location location = new Location(null, gps.getLongitude() + "", gps.getLatitude() + "", System.currentTimeMillis(), 0);
                Log.i("mapgps定位", gps.getLatitude() + "/" + gps.getLongitude());
                locationDao.insert(location);
            }else {
                final LocationClient mLocClient;
                mLocClient = new LocationClient(context);
                mLocClient.registerLocationListener(new BDLocationListener() {
                    @Override
                    public void onReceiveLocation(BDLocation bdLocation) {
                        // map view 销毁后不在处理新接收的位置
                        if (bdLocation == null) {
                            return;
                        }
                        double[] dd = HelpUtils.gcj02towgs84(bdLocation.getLongitude(), bdLocation.getLatitude());
                        LocationDao locationDao = GreenDaoManager.getInstance().getNewSession().getLocationDao();
                        Location location1 = new Location(null, dd[0] + "", dd[1] + "", System.currentTimeMillis(), 0);
                        locationDao.insert(location1);
                        Log.i("map不是自动后台定位百度sdk定位", bdLocation.getLatitude() + "/" + bdLocation.getLongitude() + "//" + dd[1] + "//" + dd[0]);
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



    }
    /*
    * 自动记录 gps定位
    * */
    public  static  void  gps(final Context context){
        if (LocationUtils.isGpsEnabled()==false){
            LocationUtils.openGpsSettings();
        }else {
            android.location.Location gps = LocationhelpUtils.getGPSLocation(context);
            if (gps == null) {
                LocationClient mLocClient;
                mLocClient = new LocationClient(context);
                mLocClient.registerLocationListener(new BDLocationListener() {
                    @Override
                    public void onReceiveLocation(BDLocation bdLocation) {
                        // map view 销毁后不在处理新接收的位置
                        if (bdLocation == null) {

                            return;
                        }
                        double[] dd=HelpUtils.gcj02towgs84(bdLocation.getLongitude(),bdLocation.getLatitude());
                        LocationDao locationDao = GreenDaoManager.getInstance().getAutoDaoSession().getLocationDao();
                        Location location1 = new Location(null, dd[0] + "", dd[1] + "", System.currentTimeMillis(), 0);
                        locationDao.insert(location1);
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
            } else {
                //存到数据库
                LocationDao locationDao = GreenDaoManager.getInstance().getAutoDaoSession().getLocationDao();
                Location location = new Location(null, gps.getLongitude() + "",
                        gps.getLatitude() + "", System.currentTimeMillis(), 0);
                locationDao.insert(location);
            }

        }
    }
       /*
       * 获取当前的经纬度的方法
       * */
    public static android.location.Location locationgps(Context context){
        if (LocationUtils.isGpsEnabled()==false){
            LocationUtils.openGpsSettings();
        }else {
            android.location.Location gps = LocationhelpUtils.getGPSLocation(context);
            if (gps!=null){
                return gps;
            }else {

                return null;
            }
        }
        return null;
    }



    /**
     *  处理图片
     * @param bm 所要转换的bitmap
     * @param
     * @return 指定宽高的bitmap
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 生成视图的预览
     * @param activity
     * @param v
     * @return  视图生成失败返回null
     *          视图生成成功返回视图的绝对路径
     */
    public  static Bitmap savebitmap(Activity activity,View v){
        Bitmap bitmap;
        String path =  Environment.getExternalStorageDirectory().getAbsolutePath()  + "preview.png";
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        bitmap = view.getDrawingCache();//到这里截取的是全屏
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //这里就是需要截取的控件
        bitmap = Bitmap.createBitmap(bitmap, location[0], location[1], v.getWidth(), v.getHeight());

        return  bitmap;
    }
    public static boolean isNetworkAvailable(Context context)
    {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

/**
* 秒转为时分秒
* */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /*
   *  根据经纬度获取所在地
   * */
    public static String getcity(final Context context) {
        if (LocationUtils.isGpsEnabled() == false) {
            LocationUtils.openGpsSettings();
        } else {
            android.location.Location location = LocationhelpUtils.getGPSLocation(context);
            if (location==null){
                Log.i("loca",location+"");
            }else {
                final double lat =36.45;
                final double lon =115.97;
                Log.i("经纬度",lat+"/"+lon);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Geocoder geocoder = new Geocoder(context);
                            try {
                                List<Address>  addresses = geocoder.getFromLocation(lat, lon, 1);
                                Log.i("定位地址", addresses.get(0).getLocality()+"" );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();

                // Address address = getAddress(lat, lon);
                //
                    return "" ;

            }
        }
        return "";

    }


    public  static  String getcitys(final Context context){
        android.location.Location location = LocationhelpUtils.getGPSLocation(context);
        final double lat = location.getLatitude();
        final double lon = location.getLongitude();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context);
                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(lat, lon, 1);
                    Log.i("定位地址", addresses.get(0).getLocality() + "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }}).start();
        return  "";
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num ="^1[3|4|5|7|8][0-9]{9}$";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    //判断是东西经及南北纬
    public static String latlon( double lat, double lon) {
        String lats="";
        String lons="";
        if (lat>0){
            //n
            lats=lat+"N";
        }else {
            lats=lat+"S";
        }
        if (lon>0){
            lons=lon+"E";
        }else {
            lons=lon+"W";
            lons=lons.split("-")[1];
        }
        return lats+","+lons;

    }


    /**
     * GCJ02(火星坐标系)转GPS84
     *
     * @param lng 火星坐标系的经度
     * @param lat 火星坐标系纬度
     * @return WGS84坐标数组
     */
    static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    // π
    static double pi = 3.1415926535897932384626;
    // 长半轴
    static double a = 6378245.0;
    // 扁率
    static double ee = 0.00669342162296594323;

    public static double[] gcj02towgs84(double lng, double lat) {
        if (out_of_china(lng, lat)) {
            return new double[] { lng, lat };
        }
        double dlat = transformlat(lng - 105.0, lat - 35.0);
        double dlng = transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * pi;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * pi);
        double mglat = lat + dlat;
        double mglng = lng + dlng;
        return new double[] { lng * 2 - mglng, lat * 2 - mglat };
    }

    /**
     * 纬度转换
     *
     * @param lng
     * @param lat
     * @return
     */
    public static double transformlat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * pi) + 20.0 * Math.sin(2.0 * lng * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * pi) + 320 * Math.sin(lat * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 经度转换
     *
     * @param lng
     * @param lat
     * @return
     */
    public static double transformlng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * pi) + 20.0 * Math.sin(2.0 * lng * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * pi) + 40.0 * Math.sin(lng / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * pi) + 300.0 * Math.sin(lng / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 判断是否在国内，不在国内不做偏移
     *
     * @param lng
     * @param lat
     * @return
     */
    public static boolean out_of_china(double lng, double lat) {
        if (lng < 72.004 || lng > 137.8347) {
            return true;
        } else if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }

    /**
    * 把传过来的详细的地址截取
     * strs[0]-->：
     * 直辖市市名或者地级市比如北京市朝阳区---<北京市；
     * 山东省聊城市冠县---》聊城市
     * 山东省聊城市临清市-->聊城市
     * sts[1]--->县级市或县区 比如 朝阳区  冠县   临清市
    * */

    public static String[] getcityorcity_addr(String address) {
        String []  strs=new String[2];
        String ss="";//市
        String city_ddr="";//区/县
        if (address!=null) {
            if (address.indexOf("省") == -1) {
                //没有省是自治区或者是直辖市
                if (address.indexOf("广西")!=-1||
                        address.indexOf("内蒙")!=-1||address.indexOf("西藏")!=-1||
                        address.indexOf("新疆")!=-1||address.indexOf("宁夏")!=-1){
                    //是中的五大直辖市
                    String str=address.substring(address.indexOf("区")+1);
                    Log.i("截取记录",str);
                    if (str.indexOf("州")!=-1){
                        //有州字比如 黔西南布依族自治州西林县xx镇
                        //取州
                        ss=str.substring(0,str.indexOf("州")+1);//取的是黔西南布依族自治
                        //判断是否是县还是县级市
                        if (str.indexOf("市")==-1){//不是县级市
                            //是县
                            city_ddr=str.substring(str.indexOf("州")+1,str.indexOf("县")+1);//取的是西林
                        }else {
                            city_ddr=str.substring(str.indexOf("州")+1,str.indexOf("市")+1);
                        }
                    }else {
                        //比如聊城市冠县贾镇、聊城市临清市xxx镇
                        String xianorshi=str.substring(str.indexOf("市")+1);//冠县贾镇or临清市xx镇
                        ss=str.substring(0,str.indexOf("市")+1);//聊城
                        //判断是否是县级市
                        if (xianorshi.indexOf("县")!=-1){//县
                            //县
                            city_ddr=xianorshi.substring(0,xianorshi.indexOf("县")+1);//取的是临清市
                        }else if (xianorshi.indexOf("市")!=-1){
                            //市
                            city_ddr=xianorshi.substring(0,xianorshi.indexOf("市")+1);//取的是冠县
                        }else if (xianorshi.indexOf("区")!=-1){
                            city_ddr=xianorshi.substring(0,xianorshi.indexOf("区")+1);//取的是冠县
                        }
                    }

                }else{
                    ss = address.substring(0, address.indexOf("市")+1);//北京市
                    city_ddr=address.substring(address.indexOf("市")+1,address.indexOf("区")+1);
                }
            } else {
                String str=address.substring(address.indexOf("省")+1);
                Log.i("截取记录",str);
                if (str.indexOf("州")!=-1){
                    //有州字比如 黔西南布依族自治州西林县xx镇
                    //取州
                    ss=str.substring(0,str.indexOf("州")+1);//取的是黔西南布依族自治
                    //判断是否是县还是县级市
                    if (str.indexOf("市")==-1){//不是县级市
                        //是县
                        city_ddr=str.substring(str.indexOf("州")+1,str.indexOf("县")+1);//取的是西林
                    }else {
                        city_ddr=str.substring(str.indexOf("州")+1,str.indexOf("市")+1);
                    }
                }else {
                    //比如聊城市冠县贾镇、聊城市临清市xxx镇
                    String xianorshi=str.substring(str.indexOf("市")+1);//冠县贾镇or临清市xx镇
                    ss=str.substring(0,str.indexOf("市")+1);//聊城
                    //判断是否是县级市
                    if (xianorshi.indexOf("县")!=-1){//县
                        //县
                        city_ddr=xianorshi.substring(0,xianorshi.indexOf("县")+1);//取的是临清市
                    }else if (xianorshi.indexOf("市")!=-1){
                        //市
                        city_ddr=xianorshi.substring(0,xianorshi.indexOf("市")+1);//取的是冠县
                    }else if (xianorshi.indexOf("区")!=-1){
                        city_ddr=xianorshi.substring(0,xianorshi.indexOf("区")+1);//取的是冠县
                    }
                }
            }
            Log.i("city--",ss+"/"+"/"+address+"/"+city_ddr);
            strs[0]=ss;
            strs[1]=city_ddr;
            return  strs;
        }

        return null;

    }


    /* 二次压缩，先按照像素压缩再按照质量压缩
    * @param imgUrl 图片路径
    * @param reqWidth 期望宽度 可以根据市面上的常用分辨率来设置
    * @param size 期望图片的大小，单位为kb
    * @param quality 图片压缩的质量，取值1-100，越小表示压缩的越厉害，如输入30，表示压缩70%
            * @return Bitmap 压缩后得到的图片
    */
    public static Bitmap compressBitmap(String imgUrl,int reqWidth,int size,int quality){
        // 创建bitMap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgUrl, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int reqHeight;
        reqHeight = (reqWidth * height) / width;
        // 在内存中创建bitmap对象，这个对象按照缩放比例创建的
        options.inSampleSize = calculateInSampleSize(
                options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(
                imgUrl, options);
        Bitmap mBitmap = compressImage(Bitmap.createScaledBitmap(
                bm, 600, reqHeight, false),size,quality);
        return  mBitmap;
    }


    private static Bitmap compressImage(Bitmap image,int size,int options) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > size) {
            options -= 10;// 每次都减少10
            baos.reset();// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * 计算像素压缩的缩放比例
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;


        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();

        return imei;
    }
  /*
  * 顶部弹框警告框
  * */
    public  static  void warning(Activity context,String str){
         TSnackbar snackBar;
         int APP_DOWn = TSnackbar.APPEAR_FROM_TOP_TO_DOWN;
        final ViewGroup viewGroup = (ViewGroup) context.findViewById(android.R.id.content).getRootView();
        snackBar = TSnackbar.make(viewGroup, str, TSnackbar.LENGTH_SHORT, APP_DOWn);
        // snackBar.setPromptThemBackground(Prompt.SUCCESS);自带的
        snackBar.setBackgroundColor(Color.parseColor("#ffffff"));
        snackBar.addIcon(R.mipmap.warn);
        snackBar.setTextColor(Color.parseColor("#132234"));
        snackBar.setMessageTextSize(17);
        snackBar.show();
    }

    /*
    * 顶部弹框成功框
    * */
    public  static  void  success(Activity context,String str){
        TSnackbar snackBar;
        int APP_DOWn = TSnackbar.APPEAR_FROM_TOP_TO_DOWN;
        final ViewGroup viewGroup = (ViewGroup) context.findViewById(android.R.id.content).getRootView();
        snackBar = TSnackbar.make(viewGroup, str, TSnackbar.LENGTH_SHORT, APP_DOWn);
        // snackBar.setPromptThemBackground(Prompt.SUCCESS);自带的
        snackBar.setBackgroundColor(Color.parseColor("#ffffff"));
        snackBar.addIcon(R.mipmap.successs);
        snackBar.setTextColor(Color.parseColor("#132234"));
        snackBar.setMessageTextSize(17);
        snackBar.show();
    }

    /*
    * 顶部弹框错误框
    * */
    public static  void error(Activity context,String str){
        TSnackbar snackBar;
        int APP_DOWn = TSnackbar.APPEAR_FROM_TOP_TO_DOWN;
        final ViewGroup viewGroup = (ViewGroup) context.findViewById(android.R.id.content).getRootView();
        snackBar = TSnackbar.make(viewGroup, str, TSnackbar.LENGTH_SHORT, APP_DOWn);
        // snackBar.setPromptThemBackground(Prompt.SUCCESS);自带的
        snackBar.setBackgroundColor(Color.parseColor("#ffffff"));
        snackBar.addIcon(R.mipmap.error);
        snackBar.setTextColor(Color.parseColor("#132234"));
        snackBar.setMessageTextSize(17);
        snackBar.show();
    }

    /*
    * 判断wifi
    * */
    public static  void judgewifi(WifiAdmin wifiAdmin,WifiManager mWifiManager,Context context) {
        int iswifi = 0;
        if (mWifiManager.getConnectionInfo().getSSID().toString().equals("\"APP-TEST\"")) {
            //是港口wifi
            if (SharedPreferencedUtils.getString(context,"token")==null) {
                context.startActivity(new Intent(context, LoginActivity.class));
            } else {
                context.startActivity(new Intent(context, MainActivity.class));

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
                context.startActivity(new Intent(context, AkeyWebActivity.class));
            } else if (iswifi == 0) {
                //说明没有港口wifi
                if (SharedPreferencedUtils.getString(context,"token")==null) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                } else {
                    context.startActivity(new Intent(context, MainActivity.class));
                }
            }
        }
    }
}
