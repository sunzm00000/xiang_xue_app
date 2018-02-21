package com.example.fishingport.app.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.baidu.mapapi.search.geocode.GeoCoder;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.LocationCity;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.ui.activity.LaunchActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lenovo on 2017/4/26.
 */

public class LocationSevice extends Service {

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    LocationCity locationCity=new LocationCity(LocationSevice.this);
                    locationCity.setsearch();
                   //String city= HelpUtils.getcity(LocationSevice.this);
                  // SharedPreferencedUtils.getString(LocationSevice.this,"city",city);
                   break;
            }

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message message=new Message();
                 message.what=1;
                handler.sendMessage(message);

            }
        },0,60*1000);
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
