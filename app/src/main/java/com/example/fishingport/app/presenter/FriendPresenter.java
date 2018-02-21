package com.example.fishingport.app.presenter;

import com.example.fishingport.app.api.FriendConstract;
import com.example.fishingport.app.api.FriendTask;
import com.example.fishingport.app.api.HomeConstract;
import com.example.fishingport.app.api.HomeTask;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * Created by wushixin on 2017/4/24.
 */

public class FriendPresenter implements FriendConstract.presenter{

    FriendTask mTask;
    FriendConstract.view mView;

    public FriendPresenter(FriendConstract.view view) {
        mView = view;
        mTask = new FriendTask();
    }
    /**
     * 添加好友*/
    @Override
    public void loadAddFriend(Map<String, String> map) {
        mTask.getAddFirend(new Subscriber<String>() {
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
     * 新渔友*/
    @Override
    public void loadNewFriend(Map<String, String> map) {
        mTask.getNewFirend(new Subscriber<String>() {
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
     * 搜索渔友
     * */
    @Override
    public void loadSearchUser(Map<String, String> map) {
        mTask.getSearchUser(new Subscriber<String>() {
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
     * 获取群聊列表
     * */

    @Override
    public void loadGroupList(Map<String, String> map) {
        mTask.getGroupList(new Subscriber<String>() {
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
     * 群信息
     **/
    @Override
    public void loadGroupInfo(Map<String, String> map) {
        mTask.getGroupInfo(new Subscriber<String>() {
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
     * 获取好友列表
     * */
    @Override
    public void loadFriendList(Map<String, String> map) {
        mTask.getFriendList(new Subscriber<String>() {
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
     * 同意好友申请
     **/
    @Override
    public void loadAgreeApply(Map<String, String> map) {
        mTask.getAgreeApply(new Subscriber<String>() {
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
     * 创建群聊
     **/
    @Override
    public void loadCreateGroup(Map<String, String> map) {
        mTask.getCreateGroup(new Subscriber<String>() {
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
     * 修改群信息
     **/
    @Override
    public void loadUpdateGroup(Map<String, String> map) {
        mTask.getUpdateGroup(new Subscriber<String>() {
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
     * 退出/解散群聊
     **/
    @Override
    public void loadQuitGroup(Map<String, String> map) {
        mTask.getQuitGroup(new Subscriber<String>() {
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
     * 发送消息
     **/
    @Override
    public void loadSendMsg(Map<String, String> map) {
        mTask.getSendMsg(new Subscriber<String>() {
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
* 获取用户或者群信息
*
* */
    @Override
    public void getinfo(Map<String, String> map) {
          mTask.getinfo(new Subscriber<String>() {
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
          },map);
    }


     /*
     * 添加群成员
     * */
    @Override
    public void add_group_user(Map<String, String> map) {
        mTask.add_group_user(new Subscriber<String>() {
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
        },map);

    }
    /*
    * 删除群成员
    * */
    @Override
    public void delete_group_user(Map<String, String> map) {
        mTask.delete_group_user(new Subscriber<String>() {
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
        },map);

    }


    /*
    * 搜索好友或者群聊
    * */
    @Override
    public void im_search(Map<String, String> map) {
        mTask.im_search(new Subscriber<String>() {
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
        },map);

    }

    @Override
    public void update_name(Map<String, String> map) {
        mTask.update_name(new Subscriber<String>() {
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
        },map);
    }

    @Override
    public void delete_friend(Map<String, String> map) {
        mTask.delete_friend(new Subscriber<String>() {
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
        },map);
    }
     /*
     * 新渔友数量
     * */
    @Override
    public void new_friend(Map<String, String> map) {
       mTask.new_friend(new Subscriber<String>() {
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
       },map);
    }
     /*
     * 会话消息列表
     * */
    @Override
    public void get_chatlist(Map<String, String> map) {
        mTask.get_chatlist(new Subscriber<String>() {
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
        },map);

    }
    /*
    * 单个会话详情
    * */
    @Override
    public void getchatinfo(Map<String, String> map) {
         mTask.getchatinfo(new Subscriber<String>() {
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
         },map);
    }

    @Override
    public void deletechat(Map<String, String> map) {
        mTask.deletechat(new Subscriber<String>() {
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
        },map);
    }

    @Override
    public void msg_recodrlist(Map<String, String> map) {
        mTask.msg_recodrlist(new Subscriber<String>() {
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
        },map);

    }
}
