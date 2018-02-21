package com.example.fishingport.app.api;

import com.example.fishingport.app.base.BaseView;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by wushixin on 2017/4/24.
 * 好友管理
 */

public interface FriendConstract {

    interface view extends BaseView {


        void showInfo(String string);

    }

    interface presenter {
        /**
         * 添加好友
         **/
        void loadAddFriend(Map<String, String> map);
        /**
         * 新渔友
         **/
        void loadNewFriend(Map<String, String> map);
        /**
         * 搜索用户
         **/
        void loadSearchUser(Map<String, String> map);
        /**
         * 获取群聊列表
         **/
        void loadGroupList(Map<String, String> map);
        /**
         * 群信息
         **/
        void loadGroupInfo(Map<String, String> map);
        /**
         * 获取好友列表
         **/
        void loadFriendList(Map<String, String> map);
        /**
         * 同意好友申请
         **/
        void loadAgreeApply(Map<String, String> map);
        /**
         * 创建群聊
         **/
        void loadCreateGroup(Map<String, String> map);
        /**
         * 修改群信息
         **/
        void loadUpdateGroup(Map<String, String> map);
        /**
         * 退出/解散群聊
         * */
        void loadQuitGroup(Map<String, String> map);
        /**
         * 发送消息
         * */
        void loadSendMsg(Map<String, String> map);

        /*
        * 获取用户或者群聊信息
        * */
        void getinfo(Map<String,String> map);
        /*
        * 添加群成员
        *
        * */
        void  add_group_user(Map<String,String> map);
        /*
        * 删除群成员
        *
        * */
        void delete_group_user(Map<String,String> map);

        /*
        * 搜索好友或者昵称
        * */
        void  im_search(Map<String,String> map);
        /*
        * 修改好友备注
        * */
        void  update_name(Map<String,String> map);

        /*
        * 删除好友
        * */
        void  delete_friend(Map<String,String> map);

        /*
        * 新渔友数量
        * 正在申请的数量
        * */
        void new_friend(Map<String,String> map);

        /*
        * 会话消息列表
        *
        * */
        void get_chatlist(Map<String,String> map);
        /*
        * 单个会话列表
        * */
        void  getchatinfo(Map<String,String> map);
        /*
        * 删除会话
        * */
        void deletechat(Map<String,String> map);
        /*
        * 获取会话记录
        * */
        void msg_recodrlist(Map<String,String> map);
    }
}
