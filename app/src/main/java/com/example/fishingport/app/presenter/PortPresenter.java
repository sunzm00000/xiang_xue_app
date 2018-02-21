package com.example.fishingport.app.presenter;

import android.util.Log;

import com.example.fishingport.app.api.PortConstract;
import com.example.fishingport.app.api.PortTask;

import java.util.Map;

import rx.Subscriber;

/**
 * Created by Lenovo on 2017/5/4.
 * 附近港口以及城市
 */

public class PortPresenter implements PortConstract.presenter {
    PortConstract.view mview;
    PortTask portTask;
    public  PortPresenter(PortConstract.view view){
        mview=view;
        portTask=new PortTask();
    }
    @Override
    public void get_city_id(Map<String, String> map) {
        portTask.get_city_id(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("错误",e.getMessage().toString()+"");

            }

            @Override
            public void onNext(String s) {
                mview.showcityid(s);

            }
        },map);

    }

    @Override
    public void get_port_list(Map<String, String> map) {
    portTask.get_port_list(new Subscriber<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.i("错误",e.getMessage().toString()+"");
        }

        @Override
        public void onNext(String s) {
            mview.showInfo(s);
        }
    },map);
    }

    //意见反馈
    @Override
    public void suggestion(Map<String, String> map) {
        portTask.suggestion(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mview.showInfo(s);

            }
        },map);

    }
}
