package com.example.fishingport.app.api;

import com.example.fishingport.app.tools.RetrofitFactory;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wushixin on 2017/4/21.
 * 首页数据
 */

public class HomeTask {

    /**
     * 取新闻政策信息
     **/
    public  void getNewsList(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(HomeService.class)
                .getNewsList(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
      /**
      * 取推荐新闻
      **/
    public  void recommendlist(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(HomeService.class)
                .recommendlist(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }

    /**
     * 取分类列表
     **/
    public  void getCategoryList(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(HomeService.class)
                .getCategoryList(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     * 取城市Id
     **/
    public  void getCityId(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(HomeService.class)
                .getCityId(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     * 取鱼货行情信息
     **/
    public  void getMarketList(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(HomeService.class)
                .getMarketList(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     * 培训详情
     **/
    public  void getTrainList(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(HomeService.class)
                .getTrainList(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     * 取所在城市的天气
     **/
    public  void getWeatherList(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(HomeService.class)
                .getWeatherList(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }

    /*
    * 点赞
    * */
    public  void add_appraise(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(HomeService.class)
                .add_appraise(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }

    /*
  * 点赞数评论数
  * */
    public  void detailnum(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(HomeService.class)
                .detailnum(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
}
