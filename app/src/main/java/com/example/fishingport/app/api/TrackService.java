package com.example.fishingport.app.api;

import com.example.fishingport.app.model.MaprRePortData;
import com.example.fishingport.app.service.MapService;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Lenovo on 2017/4/21.
 * 轨迹
 */

public interface TrackService {
    /*
    *
    * 地图轨迹上报间隔
    * */
    @FormUrlEncoded
    @POST("/api/1/trajectory/get_report_time")
    Observable<String> get_report_time(@FieldMap Map<String,String> map);
    /*
    * 自动记录轨迹上报间隔
    *
    * */
    @FormUrlEncoded
    @POST("/api/1/trajectory_auto/get_report_time")
    Observable<String> get_auto_report_time(@FieldMap Map<String,String> map);

    /*
    * 自动记录轨迹上报接口
    * */
    @FormUrlEncoded
    @POST("/api/1/trajectory_auto/report")
    Observable<String> autoreport(@FieldMap Map<String,String> map);

    /*
    * 开始登陆接口
    *
    * */
    @FormUrlEncoded
    @POST("/api/1/trajectory/report/start_report")
    Observable<String> start_report(@FieldMap Map<String,String> map);

    /*
    * 取轨迹列表
    * */
    @FormUrlEncoded
    @POST("/api/1/trajectory/get_trajectory_list")
    Observable<String> get_trajectory_list(@FieldMap Map<String,String> map);
   /*
   * map记录轨迹上报
   * */

   @FormUrlEncoded
    @POST("/api/1/trajectory/report/report")
    Observable<String> report(@FieldMap Map<String,String> map);
    /*
    * 每条轨迹的所有的定位的经纬度，用来显示在轨迹地图上
    * */
    @FormUrlEncoded
    @POST("/api/1/trajectory/get_trajectory_record_list")
    Observable<String>  recordlist(@FieldMap Map<String,String> map);

    /*
    * 删除轨迹
    * */
    @FormUrlEncoded
    @POST("/api/1/trajectory/delete")
    Observable<String> delete_trajectory(@FieldMap Map<String,String> map);

}
