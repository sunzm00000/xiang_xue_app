package com.example.fishingport.app.tools;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/22.
 */

public class RetrofitFactory
{

    private static RetrofitFactory instances;
    /**
     * 单例模式
     * @return
     */
    public static synchronized RetrofitFactory getInstances()
    {
        if (instances == null)
        {
            synchronized (RetrofitFactory.class)
            {
                if (instances == null)
                {
                    instances = new RetrofitFactory();
                }
            }
        }
        return instances;
    }


    //新闻请求
    public  Retrofit getNewsRetrofit()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constances.HOSTURL)
                .build();
        return retrofit;
    }

    public  Retrofit getCaptchaRetrofit()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constances.HOSTURL)
                .build();
        return retrofit;
    }

    public Retrofit getwifi(){
        Retrofit retrofit=new Retrofit.Builder()
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://dev.qts:81/api/")
                .build();
        return retrofit;
    }
    /*
* 下载环信图片
* */
    public  Retrofit downPicture(){
        Retrofit retrofit=new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(" https://a1.easemob.com/")
                .build();
        return  retrofit;
    }
    public  Retrofit getRetrofit()
    {
        Retrofit retrofit = new Retrofit.Builder()

                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constances.HOSTURL)
                .build();
        return retrofit;
    }


    }

