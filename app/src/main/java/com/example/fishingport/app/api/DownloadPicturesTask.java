package com.example.fishingport.app.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.fishingport.app.tools.RetrofitFactory;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Lenovo on 2017/5/22.\
 * 创建Retrofit对象
 */

public class DownloadPicturesTask {
    public  void downpictures(
            Subscriber<Bitmap> subscriber, String url){
        Observable<ResponseBody> observable= RetrofitFactory.getInstances()
                .downPicture().create(DownloadPicturesService.class)
                .downloadPicFromNet(url);
        if (observable!=null){
            observable
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<ResponseBody,Bitmap>(){
                        @Override
                        public Bitmap call(ResponseBody responseBody) {
                            try {
                                byte[] bytes=responseBody.bytes();
                                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            return bitmap;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                           return null;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

    }
}
