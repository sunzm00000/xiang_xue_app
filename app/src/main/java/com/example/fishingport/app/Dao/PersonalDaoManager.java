package com.example.fishingport.app.Dao;

import com.example.fishingport.app.MyApplication;
import com.example.fishingport.app.bean.DaoMaster;
import com.example.fishingport.app.bean.DaoSession;

/**
 * Created by Lenovo on 2017/5/26.
 */

public class PersonalDaoManager {

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private  DaoMaster autoDaoMaster;
    private  DaoSession autoDaoSession;
    private static PersonalDaoManager mInstance; //单例

    private PersonalDaoManager(){
        if (mInstance == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new
                    DaoMaster.DevOpenHelper(MyApplication.getContext(), "personal", null);//此处为自己需要处理的表

            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
        }
    }

    public static PersonalDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (PersonalDaoManager.class) {//保证异步处理安全操作
                if (mInstance == null) {
                    mInstance = new PersonalDaoManager();
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
