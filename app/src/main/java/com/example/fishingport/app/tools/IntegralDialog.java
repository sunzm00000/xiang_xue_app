package com.example.fishingport.app.tools;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fishingport.app.R;

/**
 * Created by Lenovo on 2017/5/9.
 * 积分弹层
 */

public class IntegralDialog extends Dialog {
    Context context;
    TextView integral_total;//展示获取了多少积分
    TextView integral_text;//展示为啥获取积分
    Button btn_integral;
    public IntegralDialog(@NonNull Context context) {
        super(context);
        this.context=context;
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.integraldialogview, null);
        integral_total= (TextView) mView.findViewById(R.id.integral_total);
        integral_text= (TextView) mView.findViewById(R.id.integral_text);
        btn_integral= (Button) mView.findViewById(R.id.btn_integral);
       super.setContentView(mView);

    }
    /**
     * 确定键监听器
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener){
        btn_integral.setOnClickListener(listener);
    }
    /**
     * 取消键监听器
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener){

    }
    public void setviewtext(String total,String text){
        integral_text.setText(text);
        integral_total.setText(total);

    }
    @Override
    public void setContentView(@NonNull View view) {
        super.setContentView(view);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

}
