package com.example.fishingport.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;


import com.example.fishingport.app.R;
import com.example.fishingport.app.tools.HelpUtils;

import java.util.List;

/**
 * Created by Lenovo on 2017/4/5.
 * 根据屏幕坐标绘制轨迹缩率图
 */
public class CustomView extends View {
    private  List<Point> pointList;
    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void  setview(List<Point> list){
        this.pointList=list;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint  paint = new Paint();
        paint.setColor(Color.parseColor("#fbbc48"));// 设置红色
        paint.setStrokeWidth(5);
        Paint paint1=new Paint();
        paint1.setColor(Color.BLUE);
        paint1.setStrokeWidth(5);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        int pxwidth=HelpUtils.dip2px(getContext(), 64);
        float f = width / pxwidth;
        float k=height/ pxwidth;
        Paint  paint2= new Paint();
        paint.setColor(Color.YELLOW);// 设置红色
        paint.setStrokeWidth(5);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.imgdot);
        Bitmap bitmap2= HelpUtils.zoomImg(bitmap1, 30, 40);
        if (pointList!=null){
        for (int i=0;i<pointList.size();i++){
            canvas.drawPoint(pointList.get(i).x / f, pointList.get(i).y / k, paint1);
            if (i==0){
                canvas.drawBitmap(bitmap2, pointList.get(i).x / f-15, pointList.get(i).y / k-40, paint2);
            }
            if (pointList.size()-1==i){
                canvas.drawBitmap(bitmap2,pointList.get(i).x / f-15, pointList.get(i).y / k-40,paint2);
            }

            if (pointList.size()==1){
            }else if (pointList.size()==2){
                canvas.drawLine(pointList.get(0).x / f, pointList.get(0).y / k,pointList.get(1).x / f, pointList.get(1).y / k, paint);
            }else {
                if (i==0){
                    canvas.drawLine(pointList.get(0).x / f, pointList.get(0).y / k,pointList.get(1).x / f, pointList.get(1).y / k, paint);

                }else {
                    canvas.drawLine(pointList.get(i - 1).x / f, pointList.get(i - 1).y / k, pointList.get(i).x / f, pointList.get(i).y / k, paint);
                }

            }

        }
        }
/*        Log.i("啦-", pxwidth + "/" + width + "/" + height);
        canvas.drawLine(546 / f, 879 / k, 567 / f, 930 / k, paint);
        canvas.drawLine(567 / f, 930 / k, 567 / f, 567 / k, paint);
        canvas.drawLine(567 / f, 567 / k, 564 / f, 942 / k, paint);
        canvas.drawLine(564 / f, 942 / k, 561 / f, 942 / k, paint);
        canvas.drawLine(561 / f, 942 / k, 564 / f, 939 / k, paint);
        canvas.drawLine(564 / f, 939 / k, 516 / f, 936 / k, paint);
        canvas.drawLine(516 / f, 936 / k, 546 / f, 963 / k, paint);*/



        //}
    }
}
