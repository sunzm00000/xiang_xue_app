package com.example.fishingport.app.api;

import com.example.fishingport.app.ui.activity.MapActivity;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Lenovo on 2017/5/8.
 */

public interface SOSService {
    //发送求救信号
    @FormUrlEncoded
    @POST("/api/1/sos_message/send")
    Observable<String> sendsos(@FieldMap Map<String,String> map);
    //获取设备的mac等相关信息，当一键上网的时候
    @GET("connect_info")//
    Observable<String> wifimac();

    @FormUrlEncoded
    @POST("/api/1/permit_client/permit_client")
    Observable<String> permit_client(@FieldMap Map<String,String> map);


    /*
    * 未读评论列表、
    *
    * */
    @FormUrlEncoded
    @POST("/api/1/comment/get_unread_list")
    Observable<String> unread_list(@FieldMap Map<String,String> map);

    /*
    * 未读点赞列表
    *
    * */
    @FormUrlEncoded
    @POST("/api/1/appraise/get_appraise_list")
    Observable<String> unappraise_list(@FieldMap Map<String,String> map);

    /*
    * 常见问题列表
    * */
    @FormUrlEncoded
    @POST("/api/1/questions")
    Observable<String> questionlist(@FieldMap Map<String,String> map);

    /*
    * 系统通知列表
    * */
    @FormUrlEncoded
    @POST("/api/1/sys_notice")
    Observable<String> notice(@FieldMap Map<String,String> map);

    /*
    * 取未读消息和未读评论数量
    *
    * */
    @FormUrlEncoded
    @POST("/api/1/appraise/get_appraise_num")
    Observable<String> getappcom(@FieldMap Map<String,String> map);



}
