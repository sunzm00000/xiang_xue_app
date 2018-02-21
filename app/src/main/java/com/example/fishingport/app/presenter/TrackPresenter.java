package com.example.fishingport.app.presenter;

import android.view.View;

import com.example.fishingport.app.api.TrackConstract;
import com.example.fishingport.app.api.TrackTask;

import java.util.Map;

import rx.Subscriber;

/**
 * Created by Lenovo on 2017/4/21.
 */

public class TrackPresenter implements TrackConstract.presenter {
    TrackTask trackTask;
    TrackConstract.view mview;
    public TrackPresenter(TrackConstract.view view){
        trackTask=new TrackTask();
        this.mview=view;

    }
    /*
    * 取地图轨迹间隔
    * */
    @Override
    public void getreport_time(Map<String, String> map) {
        trackTask.getPortTime(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
              mview.autoInfo(s);
            }
        },map);
    }


   /*
   * 取自动记录轨迹间隔
   * */
   @Override
   public void getauto_port_time(Map<String,String> map) {
       trackTask.getautoPortTime(new Subscriber<String>() {
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

    @Override
    public void autoRePort(Map<String, String> map) {
        trackTask.autoRePort(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mview.autoInfo(s.toString());

            }
        },map);
    }
/*
* 开始记录接口
* */
    @Override
    public void start_report(Map<String, String> map) {
        trackTask.start_report(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
            mview.startInfo(s);
            }
        },map);
    }
      /*
      * 取轨迹列表
      * */
    @Override
    public void get_trjectory_list(Map<String, String> map) {
        trackTask.get_trajectory_list(new Subscriber<String>() {
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

    @Override
    public void report(Map<String, String> map) {
        trackTask.report(new Subscriber<String>() {
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


    /*
    * 每条轨迹的所有的经纬度坐标点
    * */
    @Override
    public void recordlist(Map<String, String> map) {
        trackTask.recordlist(new Subscriber<String>() {
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


    /*
    * 删除轨迹
    * */
    @Override
    public void delete_trajectory(Map<String, String> map) {
        trackTask.delete_trajectory(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                mview.autoInfo(s);
            }
        },map);
    }

}
