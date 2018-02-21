package com.example.fishingport.app.Overlay;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MapViewRender;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.renderoption.DrawableOption;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Lenovo on 2017/4/17.
 */
public class MyMarkOverlay extends Overlay {
    private Drawable drawable;
    private GeoPoint mgeoPoint;
    private Context context;
    private DrawableOption mOption;

    public  MyMarkOverlay(Context context,Drawable drawable,GeoPoint geoPoint){
        this.context=context;
        this.drawable=drawable;
        this.mgeoPoint=geoPoint;
        mOption = new DrawableOption();
        mOption.setAnchor(0.5f, 1.0f);
    }

    @Override
    public boolean onTap(GeoPoint p, MapView mapView) {
        //单机事件
        Toast.makeText(context,"点击成功",Toast.LENGTH_LONG).show();
        return super.onTap(p, mapView);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event, MapView mapView) {
        //按键抬起事件
        return super.onKeyUp(keyCode, event, mapView);
    }

    @Override
    public boolean onLongPress(GeoPoint p, MapView mapView) {
        //长按事件
        return super.onLongPress(p, mapView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
        //触摸事件
        return super.onTouchEvent(event, mapView);
    }

    @Override
    public void draw(GL10 gl, MapView mapView, boolean shadow) {
        if (shadow)
            return;
        MapViewRender render = mapView.getMapViewRender();
        render.drawDrawable(gl, mOption, drawable, mgeoPoint);
    }
}
