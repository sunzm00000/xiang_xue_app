package com.example.fishingport.app.api;

import com.example.fishingport.app.tools.RetrofitFactory;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lenovo on 2017/4/21.
 */

public class TrackTask {
      /**
       * 获取地图轨迹间隔
       * */
    public  void getPortTime(Subscriber<String> subscriber,Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(TrackService.class)
                .get_report_time(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    /**
     * 自动轨迹间隔
     * */
    public  void getautoPortTime(Subscriber<String> subscriber,Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(TrackService.class)
                .get_auto_report_time(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    /**
     * 自动记录轨迹上报接口
     * */
    public  void autoRePort(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(TrackService.class)
                .autoreport(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }


    /**
     * 开始记录接口
     * */
    public  void start_report(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(TrackService.class)
                .start_report(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }


    /**
     *取轨迹列表
     * */
    public  void get_trajectory_list(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(TrackService.class)
                .get_trajectory_list(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }


    /**
     *map 记录上报轨迹
     * */
    public  void report(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(TrackService.class)
                .report(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }


    /**
     *每条轨迹的所有的经纬度坐标点，用来显示在轨迹地图上
     * */
    public  void recordlist(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(TrackService.class)
                .recordlist(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    /**
     *删除轨迹列表
     * */
    public  void delete_trajectory(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(TrackService.class)
                .delete_trajectory(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }
}
