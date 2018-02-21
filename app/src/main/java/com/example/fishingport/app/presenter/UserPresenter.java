package com.example.fishingport.app.presenter;

import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.api.UserTask;

import java.util.Map;

import rx.Subscriber;

/**
 * Created by wushixin on 2017/4/21.
 * 用户信息
 */

public class UserPresenter implements UserConstract.presenter {

    UserTask mTask;
    UserConstract.view mView;

    public UserPresenter(UserConstract.view view) {
        mView = view;
        mTask = new UserTask();
    }

    /**
     * 登录
     */
    @Override
    public void loadLogin(Map<String, String> map) {
        mTask.getLogin(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showInfo(news);

            }
        }, map);

    }

    /**
     * 验证手机与验证码  编辑
     */
    @Override
    public void loadCheckMobile(Map<String, String> map) {
        mTask.getCheckMobile(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showInfo(news);

            }
        }, map);

    }

    /**
     * 获取验证码
     */
    @Override
    public void loadCheckSend(Map<String, String> map) {
        mTask.getCheckSend(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showInfo(news);

            }
        }, map);
    }

    /**
     * 注册——提交密码
     */
    @Override
    public void loadRegister(Map<String, String> map) {
        mTask.getRegister(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showInfo(news);

            }
        }, map);

    }

    /**
     * 添加或修改单个字段的用户信息
     */
    @Override
    public void loadEditField(Map<String, String> map) {
        mTask.getEditField(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showInfo(news);

            }
        }, map);

    }

    /**
     * 用户信息
     */
    @Override
    public void loadUserInfo(Map<String, String> map) {
        mTask.getUserInfo(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showInfo(news);

            }
        }, map);

    }

    /**
     * 修改密码
     */
    @Override
    public void loadSetPassWord(Map<String, String> map) {
        mTask.getSetPassWord(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showInfo(news);

            }
        }, map);

    }

    /**
     * 上传图片（单张）
     **/
    @Override
    public void loadAddAppraise(Map<String, String> map) {
        mTask.getAddAppraise(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showInfo(news);

            }
        }, map);
    }

    /**
     * 注册完成添加用户信息
     **/

    @Override
    public void loadUpdateUser(Map<String, String> map) {
        mTask.getUpdateUser(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showInfo(news);

            }
        }, map);
    }

    /**
     * 取积分明细
     **/
    @Override
    public void loadScoreRecordList(Map<String, String> map) {
        mTask.getScoreRecordList(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showErrMsg("--------------->" + e.getMessage());
            }

            @Override
            public void onNext(String news) {
                mView.showInfo(news);

            }
        }, map);
    }


    /*
    * 取积分的完成度
    * */
    @Override
    public void isScore(Map<String, String> map) {
        mTask.isScore(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mView.showInfo(s);

            }
        }, map);

    }

    /**
     * 签到
     */
    @Override
    public void sign_in(Map<String, String> map) {
        mTask.sign_in(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mView.showsign(s);

            }
        }, map);
    }

    @Override
    public void sign_info(Map<String, String> map) {
        mTask.sign_info(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mView.showsign(s);

            }
        }, map);
    }

    /*
    * 领取积分
    * */
    @Override
    public void get_score(Map<String, String> map) {
        mTask.get_score(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mView.showsign(s);

            }
        },map);
    }

}
