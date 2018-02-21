package com.example.fishingport.app.presenter;

import android.graphics.Bitmap;

import com.example.fishingport.app.api.DownPicturesConstrack;
import com.example.fishingport.app.api.DownloadPicturesTask;
import com.example.fishingport.app.api.FishingCircleConstract;
import com.example.fishingport.app.api.FishingCircleTask;

import rx.Subscriber;

/**
 * Created by Lenovo on 2017/5/22.
 */

public class DownPicturesPresenter
        implements DownPicturesConstrack.presenter{

    DownloadPicturesTask mTask;
    DownPicturesConstrack.view mView;

    public DownPicturesPresenter(DownPicturesConstrack.view view) {
        mView = view;
        mTask = new DownloadPicturesTask();
    }
    @Override
    public void downpicture(String url) {
        mTask.downpictures(new Subscriber<Bitmap>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Bitmap bitmap) {
            mView.showImager(bitmap);
            }
        },url);

    }
}
