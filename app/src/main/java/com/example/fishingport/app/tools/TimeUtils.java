package com.example.fishingport.app.tools;

import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: wushixin (wusx@alltosun.com)
 * Date: 2016/12/29
 * Content: 倒计时
 */
public class TimeUtils {
    private int time=60;

    private Timer timer;

    private TextView btnSure;

    private String btnText;

    public TimeUtils(TextView btnSure, String btnText) {
        super();
        this.btnSure = btnSure;
        this.btnText = btnText;
        RunTimer();
    }





    public void RunTimer(){
        timer=new Timer();
        TimerTask task=new TimerTask() {

            @Override
            public void run(){
                time--;
                Message msg=handler.obtainMessage();
                msg.what=1;
                handler.sendMessage(msg);

            }
        };


        timer.schedule(task, 100, 1000);
    }


    private Handler handler =new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    if(time>0){
                        btnSure.setEnabled(false);
                        btnSure.setText(time + "s重新发送");
                        btnSure.setTextSize(14);
                        btnSure.setClickable(false);
                       // btnSure.setBackgroundResource(R.drawable.textviewhuishape);
                    }else{

                        timer.cancel();
                        btnSure.setText(btnText);
                      //  btnSure.setBackgroundResource(R.drawable.textviewshape);

                        btnSure.setClickable(true);
                        btnSure.setEnabled(true);
                        btnSure.setTextSize(14);
                    }

                    break;


                default:
                    break;
            }

        };
    };



}
