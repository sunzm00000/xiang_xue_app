package com.example.fishingport.app.Dao;

import com.example.fishingport.app.MyApplication;
import com.example.fishingport.app.bean.DaoMaster;
import com.example.fishingport.app.bean.DaoSession;


/**
 * Created by wushixin on 2017/4/11.
 *
 * 存经纬度的数据
 */

public class GreenDaoManager {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private  DaoMaster autoDaoMaster;
    private  DaoSession autoDaoSession;
    private static GreenDaoManager mInstance; //单例

    private GreenDaoManager(){
        if (mInstance == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new
                    DaoMaster.DevOpenHelper(MyApplication.getContext(), "location", null);
            //此处为自己需要处理的表

            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
            DaoMaster.DevOpenHelper autoportopenHelper = new
                    DaoMaster.DevOpenHelper(MyApplication.getContext(),
                    "autolocation", null);//此处为自己需要处理的表

            autoDaoMaster = new DaoMaster(autoportopenHelper.getWritableDatabase());
            autoDaoSession = autoDaoMaster.newSession();

        }
    }

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {//保证异步处理安全操作
                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
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
