package com.example.fishingport.app.api;

import com.example.fishingport.app.base.BaseView;
import com.example.fishingport.app.service.MapService;

import java.util.Map;

/**
 * Created by Lenovo on 2017/5/4.
 */

public interface PortConstract {

    interface view extends BaseView {
        void showInfo(String string);
        void showcityid(String s);
    }

    interface presenter {
        //获取城市id
        void get_city_id(Map<String,String> map);
        //获取港口列表
        void get_port_list(Map<String,String> map);
        //意见反馈
        void suggestion(Map<String,String> map);

    }
}
