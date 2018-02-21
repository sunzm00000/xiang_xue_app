package com.example.fishingport.app.api;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.fishingport.app.base.BaseView;

import java.util.Map;

/**
 * Created by Lenovo on 2017/5/22.
 */

public class DownPicturesConstrack {
    public interface view extends BaseView {


        void showInfo(String string);
        void showImager(Bitmap imageView);

    }

  public   interface presenter {

        void downpicture(String url);

    }
}
