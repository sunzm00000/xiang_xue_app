package com.example.fishingport.app.api;

import com.example.fishingport.app.base.BaseView;

import java.util.Map;

import rx.internal.util.unsafe.MpmcArrayQueue;

/**
 * Created by Lenovo on 2017/5/8.
 */

public interface SOSConstract {
    interface  view extends BaseView{
        void showInfo(String string);
        void  showWifi(String string);
    }
    interface presenter {

        void sendsos(Map<String,String> map);
        void getwifimac();
        void permit_client(Map<String,String> map);
        void unread_list(Map<String,String> map);
        void unappraise_list(Map<String,String> map);
        void questionlist(Map<String,String> map);
        void  noticelist(Map<String,String> map);
        void getappcom(Map<String,String> map);



    }
}
