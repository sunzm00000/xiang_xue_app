package com.example.fishingport.app.presenter;

import android.location.Location;
import android.util.Log;

import com.example.fishingport.app.api.SOSConstract;
import com.example.fishingport.app.api.SOSTask;

import java.util.Map;

import rx.Subscriber;

/**
 * Created by Lenovo on 2017/5/8.
 */

public class SOSPresenter implements SOSConstract.presenter{
    private SOSConstract.view mview;
    private SOSTask sosTask;
    public   SOSPresenter(SOSConstract.view view){
        mview=view;
        sosTask=new SOSTask();
    }

    @Override
    public void sendsos(Map<String, String> map) {
        sosTask.sendsos(new Subscriber<String>() {
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
   * 获取设备的mac
   * */
    @Override
    public void getwifimac() {
        sosTask.getwifimac(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("wifimac--",e.getMessage().toString());

            }

            @Override
            public void onNext(String s) {
                mview.showWifi(s);

            }
        });
    }
   /*
   * ac客户端认证放行
   * */
    @Override
    public void permit_client(Map<String, String> map) {
        sosTask.permit_client(new Subscriber<String>() {
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
    * 未读评论列表
    *
    * */

    @Override
    public void unread_list(Map<String, String> map) {
        sosTask.unread_list(new Subscriber<String>() {
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
    * 未读点赞消息
    * */

    @Override
    public void unappraise_list(Map<String, String> map) {
        sosTask.unappraise_list(new Subscriber<String>() {
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
    * 常见问题列表
    *
    * */

    @Override
    public void questionlist(Map<String, String> map) {
        sosTask.questionlist(new Subscriber<String>() {
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
    * 系统通知列表
    *
    * */
    @Override
    public void noticelist(Map<String, String> map) {
        sosTask.notice(new Subscriber<String>() {
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
   * 取未读评论点赞数量
   * */
    @Override
    public void getappcom(Map<String, String> map) {
        sosTask.getappcom(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(String s) {
                mview.showWifi(s);
            }
        },map);

    }
}
