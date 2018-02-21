package com.example.fishingport.app.api;

import com.example.fishingport.app.base.BaseView;

import java.util.Map;

/**
 * Created by wushixin on 2017/4/21.
 * 用户信息
 */

public interface UserConstract {

    interface view extends BaseView {


        void showInfo(String string);
        void showsign(String s);//签到

    }

    interface presenter {
        /**
         * 登录
         * **/
        void loadLogin(Map<String,String> map);
        /**
         * 验证手机与验证码  编辑
         * **/
        void loadCheckMobile(Map<String,String> map);
        /**
         * 获取验证码
         * **/
        void loadCheckSend(Map<String,String> map);
        /**
         * 注册——提交密码
         * **/
        void loadRegister(Map<String,String> map);
        /**
         * 添加或修改单个字段的用户信息
         * **/
        void loadEditField(Map<String,String> map);
        /**
         * 获取用户信息
         * **/
        void loadUserInfo(Map<String,String> map);
        /**
         * 修改密码
         * **/
        void loadSetPassWord(Map<String,String> map);
        /**
         * 上传图片（单张）
         * **/
        void loadAddAppraise(Map<String,String> map);

        /**
         * 注册完成添加用户信息
         * **/
        void loadUpdateUser(Map<String,String> map);
        /**
         * 取积分明细
         * **/
        void loadScoreRecordList(Map<String,String> map);

        /*
        * 取积分的完成度
        *
        * */
        void  isScore(Map<String,String> map);

         /**
         * 用户签到
         * */
         void sign_in(Map<String,String> map);
        /**
        * 获取用户签到信息
        * */
        void sign_info(Map<String,String>  map);

        /*
        * 领取积分
        * */
        void get_score(Map<String,String> map);

        /*
        * 取
        * */

    }

}
