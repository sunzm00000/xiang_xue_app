package com.example.fishingport.app.tools;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.bean.Location;
import com.example.fishingport.app.presenter.UserPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 * @author wu
 */
public class SignDialog extends Dialog implements UserConstract.view{

    ImageView img_del;
    Button btn_sin;
    Context context;
    CardView sign_one;
    CardView sign_two;
    CardView sign_three;
    CardView sign_four;
    CardView sign_five;
    CardView sign_six;
    CardView sign_seven;
    private UserPresenter userPresenter;


    public SignDialog(Context context) {
        super(context);
        this.context=context;
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.layout_sign_dialog, null);
        img_del= (ImageView) mView.findViewById(R.id.img_del);
        btn_sin= (Button) mView.findViewById(R.id.btn_sin);
        sign_one= (CardView) mView.findViewById(R.id.sign_one);
        sign_two= (CardView) mView.findViewById(R.id.sign_two);
        sign_three= (CardView) mView.findViewById(R.id.sign_three);
        sign_four= (CardView) mView.findViewById(R.id.sign_four);
        sign_five= (CardView) mView.findViewById(R.id.sign_five);
        sign_six= (CardView) mView.findViewById(R.id.sign_six);
        sign_seven= (CardView) mView.findViewById(R.id.sign_seven);
        userPresenter=new UserPresenter(this);

        super.setContentView(mView);
        if (HelpUtils.isNetworkAvailable(context.getApplicationContext())){
            sign_info();
        }else {
            Toast.makeText(context,"暂无网络",Toast.LENGTH_LONG).show();
        }



    }

//    public View getEditText(){
//        return editText;
//    }

    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    /**
     * 确定键监听器
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener){
        btn_sin.setOnClickListener(listener);
    }
    /**
     * 取消键监听器
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener){
        img_del.setOnClickListener(listener);
    }

    @Override
    public void showErrMsg(String msg) {

    }

    @Override
    public void showInfo(String string) {


    }

    @Override
    public void showsign(String s) {
        Log.i("获取签到的天数",s);
        try {
            JSONObject object=new JSONObject (s);
            JSONObject status=object.optJSONObject("status");
            JSONObject result=object.optJSONObject("result");
            if (status.optInt("code")==1000){
                String days=result.optString("days");
                String sign_today=result.optString("sign_today");
                if (sign_today.equals("1")){
                    btn_sin.setBackgroundResource(R.drawable.bg_qiandaoed);
                    btn_sin.setText("今天已经签到");
                    btn_sin.setClickable(false);
            }
               int day=Integer.valueOf(days);
                switch (day){
                    case 1:
                        sign_one.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        sign_one.setVisibility(View.VISIBLE);
                        sign_two.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        sign_one.setVisibility(View.VISIBLE);
                        sign_two.setVisibility(View.VISIBLE);
                        sign_three.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        sign_one.setVisibility(View.VISIBLE);
                        sign_two.setVisibility(View.VISIBLE);
                        sign_three.setVisibility(View.VISIBLE);
                        sign_four.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        sign_one.setVisibility(View.VISIBLE);
                        sign_two.setVisibility(View.VISIBLE);
                        sign_three.setVisibility(View.VISIBLE);
                        sign_four.setVisibility(View.VISIBLE);
                        sign_five.setVisibility(View.VISIBLE);
                        break;
                    case  6:
                        sign_one.setVisibility(View.VISIBLE);
                        sign_two.setVisibility(View.VISIBLE);
                        sign_three.setVisibility(View.VISIBLE);
                        sign_four.setVisibility(View.VISIBLE);
                        sign_five.setVisibility(View.VISIBLE);
                        sign_six.setVisibility(View.VISIBLE);
                        break;
                    case  7:
                        sign_one.setVisibility(View.VISIBLE);
                        sign_two.setVisibility(View.VISIBLE);
                        sign_three.setVisibility(View.VISIBLE);
                        sign_four.setVisibility(View.VISIBLE);
                        sign_five.setVisibility(View.VISIBLE);
                        sign_six.setVisibility(View.VISIBLE);
                        sign_seven.setVisibility(View.VISIBLE);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
  public  void sign_info(){
      Map<String,String> signmap=Utils.getSignParams(context,SharedPreferencedUtils.getString(context,"token"));
      Map<String,String> map=Utils.getMap(context,SharedPreferencedUtils.getString(context,"token"));
      map.put("sign",Utils.getMd5StringMap(signmap));
      userPresenter.sign_info(map);
  }
}
