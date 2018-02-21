package com.example.fishingport.app.api;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wushixin on 2017/4/21.
 * 首页数据
 */

public interface HomeService {

    /**
     * 取分类列表
     * */
    @FormUrlEncoded
    @POST("/api/1/category/get_list")
    Observable<String> getCategoryList(@FieldMap Map<String,String> map);

    /**
     * 取城市id
     * */
    @FormUrlEncoded
    @POST("/api/1/city/get_city_id")
    Observable<String> getCityId(@FieldMap Map<String,String> map);

    /**
     * 取新闻政策信息
     * */
    @FormUrlEncoded
    @POST("/api/1/information/get_list")
    Observable<String> getNewsList(@FieldMap Map<String,String> map);
    /*
    * 取推荐新闻
    * */
    @FormUrlEncoded
    @POST("/api/1/recommend/get_list")
    Observable<String> recommendlist(@FieldMap Map<String,String> map);

    /**
     * 取鱼货行情信息
     * */
    @FormUrlEncoded
    @POST("/api/1/market/get_list")
    Observable<String> getMarketList(@FieldMap Map<String,String> map);

    /**
     * 获取培训列表
     * */
    @FormUrlEncoded
    @POST("/api/1/train/get_list")
    Observable<String> getTrainList(@FieldMap Map<String,String> map);

    /**
     * 取港口列表
     * */
    @FormUrlEncoded
    @POST("/api/1/test/harbour/get_list")
    Observable<String> getHarbourList(@FieldMap Map<String,String> map);

    /**
     * 取所在城市的天气
     * */
    @FormUrlEncoded
    @POST("/api/1/weather/get_list")
    Observable<String> getWeatherList(@FieldMap Map<String,String> map);

    /*
    *
    * 点赞接口
    * */
    @FormUrlEncoded
    @POST("/api/1/appraise/add_appraise")
    Observable<String> add_appraise(@FieldMap Map<String,String> map);

    /*
    * 取点赞数喜欢数
    * */
    @FormUrlEncoded
    @POST("/api/1/comment/get_comment_num")
    Observable<String> detailnum(@FieldMap Map<String,String> map);

}
