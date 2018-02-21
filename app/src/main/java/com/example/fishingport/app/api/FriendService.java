package com.example.fishingport.app.api;

import android.os.Message;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by wushixin on 2017/4/24.
 * 好友管理
 */

public interface FriendService {

    /**
     * 添加好友
     * */
    @FormUrlEncoded
    @POST("/api/1/im/user/add_firend")
    Observable<String> getAddFirend(@FieldMap Map<String,String> map);
    /**
     * 新朋友列表
     * */
    @FormUrlEncoded
    @POST("/api/1/im/user/new_friend_list")
    Observable<String> getNewFirend(@FieldMap Map<String,String> map);
    /**
     * 获取群聊列表
     * */
    @FormUrlEncoded
    @POST("/api/1/im/group/group_list")
    Observable<String> getGroupList(@FieldMap Map<String,String> map);
    /**
     * 群信息
     * */
    @FormUrlEncoded
    @POST("/api/1/im/group/group_info")
    Observable<String> getGroupInfo(@FieldMap Map<String,String> map);
    /**
     * 搜索用户
     * */
    @FormUrlEncoded
    @POST("/api/1/im/user/search_user")
    Observable<String> getSearchUser(@FieldMap Map<String,String> map);
    /**
     * 获取好友列表
     * */
    @FormUrlEncoded
    @POST("/api/1/im/user/friend_list")
    Observable<String> getFriendList(@FieldMap Map<String,String> map);
    /**
     * 同意好友申请
     * */
    @FormUrlEncoded
    @POST("/api/1/im/user/agree_apply")
    Observable<String> getAgreeApply(@FieldMap Map<String,String> map);
    /**
     * 创建群聊
     * */
    @FormUrlEncoded
    @POST("/api/1/im/group/create_group")
    Observable<String> getCreateGroup(@FieldMap Map<String,String> map);
    /**
     * 修改群信息
     * */
    @FormUrlEncoded
    @POST("/api/1/im/group/update_group")
    Observable<String> getUpdateGroup(@FieldMap Map<String,String> map);
    /**
     * 退出/解散群聊
     * */
    @FormUrlEncoded
    @POST("/api/1/im/group/quit_group")
    Observable<String> getQuitGroup(@FieldMap Map<String,String> map);
    /**
     * 发送消息
     * */
    @FormUrlEncoded
    @POST("/api/1/im/msg/send_msg")
    Observable<String> getSendMsg(@FieldMap Map<String,String> map);

    /*
    * 获取用户或者群聊id
    *
    * */
    @FormUrlEncoded
    @POST("/api/1/im/msg/get_info")
    Observable<String> getinfo(@FieldMap Map<String,String> map);

    /*
    * 添加群成员
    * */
    @FormUrlEncoded
    @POST("/api/1/im/group/add_user")
    Observable<String> add_group_user(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("/api/1/im/group/delete_user")
    Observable<String> delete_group_user(@FieldMap Map<String,String> map);

    /*
    * 搜索渔友
    *
    * */
    @FormUrlEncoded
    @POST("/api/1/im/group/im_search")
    Observable<String> im_search(@FieldMap Map<String,String> map);

    /*
    * 修改好友备注
    *
    * */
    @FormUrlEncoded
    @POST("/api/1/im/user/update_friend_name")
    Observable<String> update_name(@FieldMap Map<String,String> map);

    /*
    * 删除好友
    * */
    @FormUrlEncoded
    @POST("/api/1/test/im/user/delete_friend")
    Observable<String> delete_friend(@FieldMap Map<String,String> map);
    /*
    * 新渔友数量
    * 正在申请的没有同意的数量
    * */
    @FormUrlEncoded
    @POST("/api/1/im/user/new_friend_num")
    Observable<String> new_friend(@FieldMap Map<String,String> map);
    /*
    * 回话列表
    *
    * */
    @FormUrlEncoded
    @POST("api/1/colloquy/get_list")
    Observable<String> get_chatlist(@FieldMap Map<String,String> map);
    /*
    * 单个会话
    * */
    @FormUrlEncoded
    @POST("api/1/colloquy/get_info")
    Observable<String> getchatinfo(@FieldMap Map<String,String> map);
    /*
    * 删除会话
    * */
    @FormUrlEncoded
    @POST("api/1/colloquy/delete ")
    Observable<String> deletechat(@FieldMap Map<String,String> map);

    /*
    * 聊天记录
    * */
    @FormUrlEncoded
    @POST("/api/1/im/msg/msg_record")
    Observable<String> msg_recodrlist(@FieldMap Map<String,String> map);
}
