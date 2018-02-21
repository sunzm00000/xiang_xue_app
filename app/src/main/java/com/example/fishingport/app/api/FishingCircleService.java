package com.example.fishingport.app.api;

import java.util.Map;

import butterknife.BindView;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by wushixin on 2017/5/4.
 *  渔友圈
 */

public interface FishingCircleService {

    /**
     * 发布动态
     * */
    @FormUrlEncoded
    @POST("/api/1/fishing_circle/add")
    Observable<String> getFishingCircleAdd(@FieldMap Map<String,String> map);
    /**
     * 发评论
     * */
    @FormUrlEncoded
    @POST("/api/1/comment/add")
    Observable<String> getCommentAdd(@FieldMap Map<String,String> map);

    /*
    * 获取最后一条渔友动态
    * */
    @FormUrlEncoded
    @POST("/api/1/fishing_circle/get_fishing_circle_new")
    Observable<String> get_end_circle_img(@FieldMap Map<String,String> map);

    /*
    * 删除一条自己的渔友圈动态
    * */
    @FormUrlEncoded
    @POST("/api/1/fishing_circle/delete")
    Observable<String> deletefish(@FieldMap Map<String,String> map);

    /*
    *
    * */

}
