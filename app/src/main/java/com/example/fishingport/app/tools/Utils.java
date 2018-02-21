package com.example.fishingport.app.tools;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Author: wushixin (wusx@alltosun.com)
 * Date: 2016/11/28
 * Content:
 */
public class Utils {


    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String getMd5StringMap(Map<String, String> map) {
        String sign = "";

        Object[] key = map.keySet().toArray();
        Arrays.sort(key);
        @SuppressWarnings({"unchecked", "rawtypes"})
        List arrayList = new ArrayList(map.entrySet());
        Collections.sort(arrayList, new Comparator() {
            public int compare(Object arg1, Object arg2) {
                @SuppressWarnings("rawtypes")
                Map.Entry obj1 = (Map.Entry) arg1;
                @SuppressWarnings("rawtypes")
                Map.Entry obj2 = (Map.Entry) arg2;
                return (obj1.getKey()).toString().compareTo((String) obj2.getKey());
            }
        });
        for (int i = 0; i < arrayList.size(); i++) {
            Log.i("加密参数", String.valueOf(arrayList.get(i)));
            sign += spliteString(String.valueOf(arrayList.get(i)), "=", "index", "back");
        }
        System.out.println(sign + "加密后sign:" + MD5.getMD5(sign));
        return MD5.getMD5(sign);
    }

    /**
     * 截取子串
     *
     * @param srcStr      源串
     * @param pattern     匹配模式
     * @param indexOrLast
     * @param frontOrBack
     * @return
     */
    public static String spliteString(String srcStr, String pattern,
                                      String indexOrLast, String frontOrBack) {
        String result = "";
        int loc = -1;
        if (indexOrLast.equalsIgnoreCase("index")) {
            loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
        } else {
            loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
        }
        if (frontOrBack.equalsIgnoreCase("front")) {
            if (loc != -1)
                result = srcStr.substring(0, loc); // 截取子串
        } else {
            if (loc != -1)
                result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
        }
        return result;
    }


    //获取手机标示
    public static String telePhony(Context context) {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    String DEVICE_ID = tm.getDeviceId() + "";
        return  DEVICE_ID;
   }
    public static byte[] resultBitmap(Bitmap bit) {

        /*********** 以下开始对选择的图片进行处理  *******/
        int quality = 40;
        File dirFile = Environment.getExternalStorageDirectory();
        //		File dirFile = getExternalCacheDir();  //也可以存储到app的缓存目录
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        //该处将得到的图片存储在压缩图片同一文件夹中  方便进行质量大小等对比
        File yuantu = new File(dirFile, "jjyuantu.jpg");
        File jpegTrueFile = new File(dirFile, "jpegtrue1.jpg");
        File jpegFalseFile = new File(dirFile, "jpegfalse1.jpg");
        //调用工具类   进行压缩 该类 包名和类名 不能改变 否则调用不到so文件中的方法
//        NativeUtil.compressBitmap(bit, quality, jpegTrueFile.getAbsolutePath(), true);
//        NativeUtil.compressBitmap(bit, quality, jpegFalseFile.getAbsolutePath(), false);
        try {
            FileOutputStream fos = new FileOutputStream(jpegTrueFile);

            bit.compress(Bitmap.CompressFormat.JPEG, 40, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //利用Glide框架进行图片展示
//			Glide.with(this).load(jpegTrueFile).into(imv);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 30, stream);

        byte[] b = stream.toByteArray();
        return b;
    }

    /*
*bitmap转base64
*/
    public static String bitmapToBase64(Bitmap bitmap){
        String result="";
        ByteArrayOutputStream bos=null;
        try {
            if(null!=bitmap){
                bos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);//将bitmap放入字节数组流中

                bos.flush();//将bos流缓存在内存中的数据全部输出，清空缓存
                bos.close();

                byte []bitmapByte=bos.toByteArray();
                result= Base64.encodeToString(bitmapByte, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(null!=null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    /**
     * 必传参数的source,time,version
     * */
    public static Map<String,String> getMap(Context context,String token){
        Map<String,String> bimap=new HashMap<>();
        bimap.put("source", Constances.source);
        bimap.put("time",Constances.time);
       // bimap.put("rid", JPushInterface.getRegistrationID(context));
//        Log.i("rid==",getDeviceId(context));
        bimap.put("rid","05043bc3a32");
        bimap.put("token",token);
        bimap.put("version", getAppVersionName(context, "com.example.fishingport.app"));
        return bimap;
    }

    /**
     * 必传加密参数的source,time,version
     * */
    public static Map<String,String> getSignParams(Context context,String token){
        Map<String,String> bimap=new HashMap<>();
        bimap.put("source", Constances.source);
        bimap.put("time",Constances.time);
      //  bimap.put("rid",Utils.telePhony(context));
        bimap.put("key", "alltosun2016");
        bimap.put("token",token);
        bimap.put("version", getAppVersionName(context, "com.example.fishingport.app"));
        return bimap;
    }
    /**
     * 获取App版本号
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本号
     */
    public static String getAppVersionName(Context context, String packageName) {
        if (isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }
    /**
     * 获取android设备唯一标识
     * @param context
     * @return
     */
    public static String getDeviceId(Context context){
        TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();

        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length()%10 +
                Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 +
                Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 +
                Build.HOST.length()%10 +
                Build.ID.length()%10 +
                Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 +
                Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 +
                Build.TYPE.length()%10 +
                Build.USER.length()%10 ; //13 digits

        //String m_szAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        //Log.d(tag,"m_szAndroidID = " + m_szAndroidID);
        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();

        BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String m_szBTMAC = m_BluetoothAdapter.getAddress();

        String m_szLongID = szImei + "_" + m_szDevIDShort
                + "_" + m_szWLANMAC + "_" + m_szBTMAC;
        // compute md5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(),0,m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
        // create a hex string
        String m_szUniqueID = new String();
        for (int i=0;i<p_md5Data.length;i++) {
            int b =  (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF)
                m_szUniqueID+="0";
            // add number to string
            m_szUniqueID+=Integer.toHexString(b);
        }   // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase();
        return m_szUniqueID;
    }


    /**
     * 照片转byte二进制
     * @param imagepath 需要转byte的照片路径
     * @return 已经转成的byte
     * @throws Exception
     */
    public static byte[] readStream(String imagepath)  {

        try {
            FileInputStream fs = new FileInputStream(imagepath);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = fs.read(buffer))) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            fs.close();
            return outStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    /**
     * 以友好的方式显示时间
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if(time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        //判断是否是同一天
        String curDate = dateFormater.get().format(cal.getTime());
        String paramDate = dateFormater.get().format(time);
        if(curDate.equals(paramDate)){
            int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
            if(hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
            else
                ftime = hour+"小时前";
            return ftime;
        }

        long lt = time.getTime()/86400000;
        long ct = cal.getTimeInMillis()/86400000;
        int days = (int)(ct - lt);
        if(days == 0){
            int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
            if(hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
            else
                ftime = hour+"小时前";
        }
        else if(days == 1){
            ftime = "昨天";
        }
        else if(days == 2){
            ftime = "前天";
        }
        else if(days > 2 && days <= 10){
            ftime = days+"天前";
        }
        else if(days > 10){
            ftime = dateFormater.get().format(time);
        }
        return ftime;
    }

    /**
     * 将字符串转位日期类型
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (java.text.ParseException e) {
            return null;
        }
    }
}
