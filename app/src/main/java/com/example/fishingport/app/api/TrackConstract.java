package com.example.fishingport.app.api;

import com.example.fishingport.app.base.BaseView;
import com.example.fishingport.app.model.MaprRePortData;

import java.util.Map;

/**
 * Created by Lenovo on 2017/4/21.
 */

public interface TrackConstract {
    interface  view extends BaseView{
        void showInfo(String string);
        void  autoInfo(String string);
        void startInfo(String string);//点击记录按钮的
    }

    interface presenter {
        //取地图轨迹间隔
        void getreport_time(Map<String,String> map);
        //自动轨迹间隔
        void getauto_port_time(Map<String,String> map);
        void autoRePort(Map<String,String> map);
        void start_report(Map<String,String> map);
        void get_trjectory_list(Map<String,String> map);
        void report(Map<String,String> map);
        void recordlist(Map<String,String> map);
        void  delete_trajectory(Map<String,String> map);


    }
}
