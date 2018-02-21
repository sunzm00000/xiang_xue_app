package com.example.fishingport.app.presenter;

import com.example.fishingport.app.api.FishingCircleConstract;
import com.example.fishingport.app.api.FishingCircleTask;
import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.api.FriendTask;

import java.util.Map;

import rx.Subscriber;

/**
 * Created by wushixin on 2017/5/4.
 * 渔友圈
 */

public class FishingCirclePresenter implements FishingCircleConstract.presenter{

    FishingCircleTask mTask;
    FishingCircleConstract.view mView;

    public FishingCirclePresenter(FishingCircleConstract.view view) {
        mView = view;
        mTask = new FishingCircleTask();
    }
    /**
     *发布动态
     **/
    @Override
    public void loadFishingCircle(Map<String, String> map) {
        mTask.getFishingCircleAdd(new Subscriber<String>() {
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
     *发布评论
     **/
    @Override
    public void loadCommentAdd(Map<String, String> map) {
        mTask.getCommentAdd(new Subscriber<String>() {
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

    /*
    * 获取最后一天图片渔友圈动态
    * */

    @Override
    public void get_end_circle_img(Map<String, String> map) {
        mTask.get_end_circleimg(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mView.showendcircle_img(s);

            }
        },map);

    }

    @Override
    public void deletefish(final Map<String, String> map) {
        mTask.deletefish(new Subscriber<String>() {
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
