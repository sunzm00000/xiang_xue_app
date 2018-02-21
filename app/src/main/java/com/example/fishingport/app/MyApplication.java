package com.example.fishingport.app;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.multidex.MultiDexApplication;
import android.util.Log;


import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.Utils;
import com.example.fishingport.app.Dao.GreenDaoManager;
import com.example.fishingport.app.service.LocationServices;
import com.example.fishingport.app.service.TimerServicer;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wushixin on 2017/4/11.
 */

public class MyApplication extends MultiDexApplication {

    private static Context mContext;
    public LocationServices locationServices;
    public Vibrator mVibrator;
    // 记录是否已经初始化
    private boolean isInit = false;
    public  static  Timer  timer=null;
    public  static Intent timerservice;
    Intent timetservice;
    private List<Timer> timerlist=new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        //greenDao全局配置,只希望有一个数据库操作对象
        GreenDaoManager.getInstance();
        Utils.init(getContext());
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationServices = new LocationServices(mContext);
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(mContext);
        // 初始化环信SDK
//        initEasemob();
    }


    public void newTimerService(Timer timer){
        this.timer=timer;
        timetservice=new Intent(this, TimerServicer.class);

        this.startService(timetservice);
    }


    public void stopTimerService(){
        Log.i("服务关闭了","服务关闭了" );
        this.stopService(timerservice);
        Log.i("服务关闭了","服务关闭了"+timerservice);
    }
    public  Intent getTimerservice(){

        return timerservice;
    }
    public Timer getTimer(){
        return timer;
    }
    public   void starttimer(final Handler handler){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
            }
        }, 0, 1000);

    }
    public void stopTimer(){
        if (timer!=null){
            timer.cancel();
        }
    }
    public static Context getContext() {
        return mContext;
    }
    /**
     *初始化环信sdk
     */
    private void initEasemob() {
        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        /**
         * 如果app启用了远程的service，此application:onCreate会被调用2次
         * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
         * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
         */
        if (processAppName == null || !processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return;
        }
        if (isInit) {
            return;
        }

        // 调用初始化方法初始化sdk
        EMClient.getInstance().init(mContext, initOptions());
        // 设置开启debug模式
       // EMClient.getInstance().setDebugMode(true);
        // 设置初始化已经完成
        isInit = true;
    }
    /**
     * SDK初始化的一些配置
     * 关于 EMOptions 可以参考官方的 API 文档
     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html
     */
    private EMOptions initOptions() {
        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);
        //options.setHuaweiPushAppId("100016315");
        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
        // 设置集成小米推送的appid和appkey
        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);

        return options;
    }
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //程序终止的时候执行
         timer.cancel();
    }
}