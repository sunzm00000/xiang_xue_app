package com.example.fishingport.app.Dao;

import com.example.fishingport.app.MyApplication;
import com.example.fishingport.app.bean.DaoMaster;
import com.example.fishingport.app.bean.DaoSession;

/**
 * Created by wushixin on 2017/5/4.
 */

public class MessageDaoManager {

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private  DaoMaster autoDaoMaster;
    private  DaoSession autoDaoSession;
    private static MessageDaoManager mInstance; //单例

    private MessageDaoManager(){
        if (mInstance == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new
                    DaoMaster.DevOpenHelper(MyApplication.getContext(), "message", null);//此处为自己需要处理的表

            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();


        }
    }

    public static MessageDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (MessageDaoManager.class) {//保证异步处理安全操作
                if (mInstance == null) {
                    mInstance = new MessageDaoManager();
                }
            }
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }
    public DaoSession getSession() {
        return mDaoSession;
    }
    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }



    public DaoMaster getAutoDaoMaster() {
        return autoDaoMaster;
    }

    public DaoSession getAutoDaoSession() {
        autoDaoSession=autoDaoMaster.newSession();
        return autoDaoSession;
    }
}
