package com.example.fishingport.app.api;

import com.example.fishingport.app.tools.RetrofitFactory;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wushixin on 2017/5/4.
 *  渔友圈
 */

public class FishingCircleTask {

    /**
     *发布动态
     **/
    public  void getFishingCircleAdd(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FishingCircleService.class)
                .getFishingCircleAdd(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     *发布评论
     **/
    public  void getCommentAdd(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FishingCircleService.class)
                .getCommentAdd(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }

    /**
     *后去最后一条渔友圈的动态
     *
     **/
    public  void get_end_circleimg(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FishingCircleService.class)
                .get_end_circle_img(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }

    /**
     *删除我的渔友圈的一条动态
     *
     **/
    public  void deletefish(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FishingCircleService.class)
                .deletefish(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }



}
