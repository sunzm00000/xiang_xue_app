package com.example.fishingport.app.api;

import com.example.fishingport.app.tools.RetrofitFactory;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Part;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wushixin on 2017/4/24.
 * 好友管理
 */

public class FriendTask {

    /**
     * 添加好友
     **/
    public  void getAddFirend(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .getAddFirend(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     * 新渔友
     **/
    public  void getNewFirend(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .getNewFirend(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     * 搜索渔友
     **/
    public  void getSearchUser(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .getSearchUser(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     * 获取群聊列表
     **/
    public  void getGroupList(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .getGroupList(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     * 群信息
     **/
    public  void getGroupInfo(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .getGroupInfo(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     * 获取好友列表
     **/
    public  void getFriendList(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .getFriendList(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     * 同意好友申请
     **/
    public  void getAgreeApply(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .getAgreeApply(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     * 创建群聊
     **/
    public  void getCreateGroup(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .getCreateGroup(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     *修改群信息
     **/
    public  void getUpdateGroup(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .getUpdateGroup(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     *退出/解散群聊
     **/
    public  void getQuitGroup(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .getQuitGroup(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
    /**
     *发送消息
     **/
    public  void getSendMsg(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .getSendMsg(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }

    /**
     *删除群成员
     *
     **/
    public  void delete_group_user(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .delete_group_user(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    /**
     *搜索好友或者群聊
     *
     **/
    public  void im_search(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .im_search(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }


    /**
     *添加群成员
     *
     **/
    public  void add_group_user(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .add_group_user(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }




    /**
     *获取用户或者群聊信息
     *
     **/
    public  void getinfo(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .getinfo(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }



    /**
     *修改好友备注
     **/
    public  void update_name(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .update_name(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    /**
     *删除好友
     **/
    public  void delete_friend(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .delete_friend(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    /**
     *新渔友数量
     *
     **/
    public  void new_friend(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .new_friend(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }


    /**
     *会话列表
     *
     **/
    public  void get_chatlist(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .get_chatlist(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    /**
     *单个会话列表
     **/
    public  void getchatinfo(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .getchatinfo(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }


    /**
     *删除会话
     **/
    public  void deletechat(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .deletechat(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }
  /*
  * 会聊天记录列表
  * */
    public  void msg_recodrlist(Subscriber<String> subscriber, Map<String,String> map){
        Observable<String> observable= RetrofitFactory.getInstances().getCaptchaRetrofit().create(FriendService.class)
                .msg_recodrlist(map);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }


}
