package com.example.fishingport.app.presenter;

import com.example.fishingport.app.api.HomeConstract;
import com.example.fishingport.app.api.HomeTask;
import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.api.UserTask;
import com.tencent.wxop.stat.MtaSDkException;

import java.util.Map;

import rx.Subscriber;

/**
 * Created by wushixin on 2017/4/21.
 * 首页数据
 */

public class HomePresenter implements HomeConstract.presenter{
    HomeTask mTask;
    HomeConstract.view mView;

    public HomePresenter(HomeConstract.view view) {
        mView = view;
        mTask = new HomeTask();
    }
    /**
     * 取新闻政策信息
     **/
    @Override
    public void loadNewsList(Map<String, String> map) {

        mTask.getNewsList(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showInfo(news);

            }
        }, map);

    }
    /**
     * 取鱼货行情信息
     **/
    @Override
    public void loadMarketList(Map<String, String> map) {
        mTask.getMarketList(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showInfo(news);

            }
        }, map);
    }

    /**
     * 取分类列表
     **/

    @Override
    public void loadCategoryList(Map<String, String> map) {
        mTask.getCategoryList(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showInfo(news);

            }
        }, map);
    }
    /**
     * 取城市Id
     **/

    @Override
    public void loadCityId(Map<String, String> map) {
        mTask.getCityId(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showcityid(news);

            }
        }, map);
    }
/**
 * 取所在城市的天气
 * */
    @Override
    public void loadWeatherList(Map<String, String> map) {
        mTask.getWeatherList(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showcityid(news);

            }
        }, map);
    }


    /*
    * 获取培训列表
    *
    * */
    @Override
    public void getTrainList(Map<String, String> map) {
        mTask.getTrainList(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                mView.showInfo(s);
            }
        },map);
    }


    /*
    * 点赞
    *
    * */
    @Override
    public void add_appraise(Map<String, String> map) {
        mTask.add_appraise(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mView.showcityid(s);

            }
        },map);

    }

    @Override
    public void detailnum(Map<String, String> map) {
        mTask.detailnum(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
              mView.showInfo(s);
            }
        },map);
    }

    @Override
    public void recommendlist(Map<String, String> map) {
        mTask.recommendlist(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
              mView.showInfo(s);
            }
        },map);
    }
}
