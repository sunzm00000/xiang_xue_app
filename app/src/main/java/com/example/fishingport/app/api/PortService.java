package com.example.fishingport.app.api;

import com.example.fishingport.app.service.MapService;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Lenovo on 2017/5/4.
 * 取港口取城市id 为了简化我把意见反馈的接口也写到这里了
 */

public interface PortService {
    //取城市id
    @FormUrlEncoded
    @POST("/api/1/city/get_city_id")
    Observable<String> get_city_id(@FieldMap Map<String,String> map);
    //取港口列表
    @FormUrlEncoded
    @POST("/api/1/harbour/get_list")
    Observable<String> get_port_list(@FieldMap Map<String,String> map);
    //意见反馈
    @FormUrlEncoded
    @POST("/api/1/suggestion")
    Observable<String> suggestion(@FieldMap Map<String,String> map);

}
