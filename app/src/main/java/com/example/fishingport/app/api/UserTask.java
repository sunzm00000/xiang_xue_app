package com.example.fishingport.app.api;

import com.example.fishingport.app.tools.RetrofitFactory;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wushixin on 2017/4/21.
 * 用户信息
 */

public class UserTask {

    /**
     * 登录
     **/
    public  void getLogin(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(UserService.class)
                .getLogin(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    /**
     * 验证手机与验证码  编辑
     **/
    public  void getCheckMobile(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(UserService.class)
                .getCheckMobile(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     * 获取验证码
     **/
    public  void getCheckSend(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(UserService.class)
                .getCheckSend(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }

    /**
     * 注册——提交密码
     **/
    public  void getRegister(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(UserService.class)
                .getRegister(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }

    /**
     * 添加或修改单个字段的用户信息
     **/
    public  void getEditField(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(UserService.class)
                .getEditField(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }

    /**
     * 获取用户信息
     **/
    public  void getUserInfo(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(UserService.class)
                .getUserInfo(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    /**
     * 修改密码
     **/
    public  void getSetPassWord(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(UserService.class)
                .getSetPassWord(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }
    /**
     * 上传图片（单张）
     **/
    public  void getAddAppraise(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(UserService.class)
                .getAddAppraise(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }
    /**
     * 注册完成添加用户信息
     **/
    public  void getUpdateUser(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(UserService.class)
                .getUpdateUser(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }
    /**
     * 取积分明细
     **/
    public  void getScoreRecordList(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(UserService.class)
                .getScoreRecordList(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    /**
     * 取积分的完成度
     **/
    public  void isScore(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(UserService.class)
                .isscore(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    /**
     * 领取积分
     **/
    public  void get_score(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(UserService.class)
                .get_score(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }
    /**
     * 用户签到
     **/
    public  void sign_in(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(UserService.class)
                .sign_in(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     * 获取用户签到信息
     **/
    public  void sign_info(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(UserService.class)
                .sign_info(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }

}
