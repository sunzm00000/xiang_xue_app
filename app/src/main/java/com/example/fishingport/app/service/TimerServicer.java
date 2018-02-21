package com.example.fishingport.app.service;

import android.app.Service;
import android.app.usage.NetworkStats;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.fishingport.app.Dao.GreenDaoManager;
import com.example.fishingport.app.MyApplication;
import com.example.fishingport.app.bean.Location;
import com.example.fishingport.app.bean.LocationDao;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.ui.activity.MapActivity;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lenovo on 2017/6/14.
 */

public class TimerServicer extends Service {
    public  Timer timer;
    private  int timing=0;
  private Handler handler=new Handler(){
      @Override
      public void handleMessage(Message msg) {
          super.handleMessage(msg);
          switch (msg.what){
              case 1:
                  int sqltime=0;
                  LocationDao locationDao = GreenDaoManager.getInstance().getNewSession().getLocationDao();
                  List<Location> list = locationDao.loadAll();
                  if (list!=null){
                      if (list.size()>0){
                          Log.i("从数据库取出来的数据",Math.round((list.get(list.size()-1).getTime()-list.get(0).getTime())/1000) +
                                  "/"+list.get(0).getTime()+"/"+list.get(list.size()-1).getTime());
                          sqltime=Math.round((list.get(list.size()-1).getTime()-list.get(0).getTime())/1000);
                          Log.i("从数据库取出来的数据=",timing+"/"+
                                  SharedPreferencedUtils.getInteger(TimerServicer.this,"timetotime",0));
                      }
                  }
                  if (SharedPreferencedUtils.getInteger(TimerServicer.this,"timetotime",-1)!=-1){
                      timing=SharedPreferencedUtils.getInteger(TimerServicer.this,"timetotime",-1);
                      if (sqltime>timing){
                          timing=sqltime;
                      }
                  }
                  timing+=1;
                  SharedPreferencedUtils.setInteger(TimerServicer.this,"timetotime",timing);//异常退出时把正在计时的值存起来
                  Log.i("计时开始",timing+"/"+SharedPreferencedUtils.getInteger(TimerServicer.this,"timetotime",0));

                  sendTimeChangedBroadcast(timing);
                  break;
          }
      }
  };
    @Override
    public void onCreate() {

        super.onCreate();
    }



    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
            Log.i("timer已经取消","time已经取消");
          MyApplication.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
        },0,1000);
        return super.onStartCommand(intent, flags, startId);

    }

    /**
     * 发送广播，通知UI层时间已改变
     */
    private void sendTimeChangedBroadcast(int timing){
        Log.i("计时开始==",timing+"");
        Intent intent=new Intent();
        intent.setAction(MapActivity.TIME_CHANGED_ACTION);
        intent.putExtra("time",timing);
        //发送广播，通知UI层时间改变了
        sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("服务","服务停止了");
        MyApplication.timer.cancel();
    }
}
