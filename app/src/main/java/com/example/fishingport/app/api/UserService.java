package com.example.fishingport.app.api;

import com.example.fishingport.app.bean.Message;

import java.util.Map;

import butterknife.BindView;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wushixin on 2017/4/13.
 * 用户信息
 */

public interface UserService {

    /**
     * 登录
     * */
    @FormUrlEncoded
    @POST("/api/1/user/login")
    Observable<String> getLogin(@FieldMap Map<String,String> map);

    /**
     * 验证手机与验证码  编辑
    * */
    @FormUrlEncoded
    @POST("/api/1/user/check_mobile")
    Observable<String> getCheckMobile(@FieldMap Map<String,String> map);
    /**
     * 发送验证码
    * */
    @FormUrlEncoded
    @POST("/api/1/captcha/send")
    Observable<String> getCheckSend(@FieldMap Map<String,String> map);

    /**
     * 注册——提交密码
     * */
    @FormUrlEncoded
    @POST("/api/1/user/register")
    Observable<String> getRegister(@FieldMap Map<String,String> map);

    /**
     * 添加或修改单个字段的用户信息
     * */
    @FormUrlEncoded
    @POST("/api/1/user/edit_field")
    Observable<String> getEditField(@FieldMap Map<String,String> map);

    /**
     * 获取用户信息
     * */
    @FormUrlEncoded
    @POST("/api/1/user/get_user_info")
    Observable<String> getUserInfo(@FieldMap Map<String,String> map);

    /**
     * 修改密码
     * */
    @FormUrlEncoded
    @POST("/api/1/user/password/set")
    Observable<String> getSetPassWord(@FieldMap Map<String,String> map);
    /**
     * 上传图片（单张）
     * */
    @FormUrlEncoded
    @POST("/api/1/file/upload_img")
    Observable<String> getAddAppraise(@FieldMap Map<String,String> map);

    /**
     * 注册完成添加用户信息
     * */
    @FormUrlEncoded
    @POST("/api/1/user/update_user")
    Observable<String> getUpdateUser(@FieldMap Map<String,String> map);
    /**
     * 取积分明细
     * */
    @FormUrlEncoded
    @POST("/api/1/score/score_record/get_list")
    Observable<String> getScoreRecordList(@FieldMap Map<String,String> map);

    /*
    * 取积分完成度
    * /api/1/score/get_list
    * */
    @FormUrlEncoded
    @POST("/api/1/score/get_list")
    Observable<String> isscore(@FieldMap Map<String,String> map);

    /*
    * 领取积分
    * */
    @FormUrlEncoded
    @POST("/api/1/score/prefect_info/get_score")
    Observable<String> get_score(@FieldMap  Map<String,String> map);


    /**
    * 用户签到
    * */
    @FormUrlEncoded
    @POST("/api/1/user_sign/sign")
    Observable<String> sign_in(@FieldMap Map<String,String> map);


    /**
    * 获取签到信息
    * */
    @FormUrlEncoded
    @POST("/api/1/user_sign/get_sign_day")
    Observable<String> sign_info(@FieldMap Map<String,String> map);

}
