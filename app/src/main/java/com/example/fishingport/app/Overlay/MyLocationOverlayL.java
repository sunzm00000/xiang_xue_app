package com.example.fishingport.app.Overlay;

import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;
import android.view.MotionEvent;

import com.example.fishingport.app.R;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.maps.Texture.UtilTextureBase;
import com.tianditu.maps.Texture.UtilTextureDrawable;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Lenovo on 2017/4/14.
 * 重新MylocationOverlay定位类，修改覆盖默认定位图标
 */
public class MyLocationOverlayL extends MyLocationOverlay {
    private Context context;
    private UtilTextureDrawable mTextureDot;//定位图标
    public MyLocationOverlayL(Context context, MapView mapView) {
        super(context, mapView);
        mTextureDot = new UtilTextureDrawable(context, R.mipmap.smalldot, UtilTextureBase.BoundType.BOUND_TYPE_CENTER);
    }
    @Override
    protected boolean dispatchTap() {
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
        return super.onTouchEvent(event, mapView);
    }
    @Override
    protected void drawMyLocation(GL10 gl, MapView mapView, Location lastFix, GeoPoint myLocation, long when) {
        //重载的MylocationOvelay的方法，
        super.drawMyLocation(gl, mapView, lastFix, myLocation, when);
        if (myLocation!=null){
            Point localPoint = mapView.getProjection().toPixels(myLocation, (Point)null);
            mTextureDot.DrawTexture(gl, localPoint, 0.0F);
        }
    }

    @Override
    public void onLocationChanged( final Location location) {
        super.onLocationChanged(location);
    }
}
