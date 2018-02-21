package com.example.fishingport.app.Dao;

import com.example.fishingport.app.MyApplication;
import com.example.fishingport.app.bean.DaoMaster;
import com.example.fishingport.app.bean.DaoSession;

/**
 * Created by Lenovo on 2017/6/4.
 */

public class GroupbeanDaoManager {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private  DaoMaster autoDaoMaster;
    private  DaoSession autoDaoSession;
    private static GroupbeanDaoManager mInstance; //单例
    private GroupbeanDaoManager(){
        if (mInstance == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new
                    DaoMaster.DevOpenHelper(MyApplication.getContext(), "group", null);//此处为自己需要处理的表
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
        }
    }
    public static GroupbeanDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GroupbeanDaoManager.class) {//保证异步处理安全操作
                if (mInstance == null) {
                    mInstance = new GroupbeanDaoManager();
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
