package com.example.fishingport.app.ui.activity;

import android.content.Context;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fishingport.app.R;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.Traininglist;
import com.example.fishingport.app.model.Weatherlist;
import com.example.fishingport.app.tools.StatusBarCompat;
import com.example.fishingport.app.widget.SpacesItemDecoration;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* 天气详情页面
*
* */
public class WeatherDetailsaActivity extends AppCompatActivity {
   @BindView(R.id.layout_back)
    RelativeLayout layout_back;//返回按钮
    @BindView(R.id.nowimg)
    ImageView nowimg;//当前天气图
    @BindView(R.id.city)
    TextView city;//当前城市
    @BindView(R.id.now)
    TextView now;//当前温度
    @BindView(R.id.content)
    TextView content;//content
    private  String string;//从首页传过来的json
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;//天气列表
    private  List<Weatherlist.ResultBean.DataBean> mlist=new ArrayList<>();
    BaseRecyclerAdapter<Weatherlist.ResultBean.DataBean> beanBaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detailsa);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarCompat.copat(this);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        beanBaseRecyclerAdapter=new BaseRecyclerAdapter<Weatherlist.ResultBean.DataBean>
                (this,null,R.layout.item_weather_view) {
            @Override
            protected void convert(BaseViewHolder helper, Weatherlist.ResultBean.DataBean item) {
                String week=item.getWeek();

                if (week.equals("1")){
                    helper.setText(R.id.week,"周一");
                }else if (week.equals("2")){
                    helper.setText(R.id.week,"周二");
                }else if (week.equals("3")){
                    helper.setText(R.id.week,"周三");
                }else if (week.equals("4")){
                    helper.setText(R.id.week,"周四");
                }else if (week.equals("5")){
                    helper.setText(R.id.week,"周五");
                }else  if (week.equals("6")){
                    helper.setText(R.id.week,"周六");
                }else if (week.equals("0")){
                    helper.setText(R.id.week,"周日");
                }
                helper.setText(R.id.date,item.getDate());

                ImageView img= (ImageView) helper.getConvertView().findViewById(R.id.img);
                TextView tianqi= (TextView) helper.getConvertView().findViewById(R.id.tianqi);
                setWeatherImg(item.getWeather(),img,tianqi);//设置图标
                helper.setText(R.id.wendu,item.getTemperature()+"℃");
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder holder = (BaseViewHolder) super.onCreateViewHolder(parent, viewType);
                View view = holder.getConvertView();
                if (viewType == 0x00002222) {
                    int with = getScreenWidth(WeatherDetailsaActivity.this) / (mData != null ? mData.size() : 1);
                    if (with < dpTopx(WeatherDetailsaActivity.this, 76)) {
                        with = dpTopx(WeatherDetailsaActivity.this, 76);
                    }
                    view.getLayoutParams().width = with;
                }
                return holder;
            }

        };
        beanBaseRecyclerAdapter.openLoadAnimation(false);
        mRecyclerView.setAdapter(beanBaseRecyclerAdapter);
        initdata();

    }
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
    /**
     * 像素单位转换 dp到px
     */
    public static int dpTopx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    /**
     * 将px值转换为dip或dp值
     *
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    private void initdata() {
        string=getIntent().getStringExtra("string");
        Log.i("传过来的天气情况",string);
        try {
            JSONObject object=new JSONObject(string+"");
            JSONObject status=object.optJSONObject("status");
            if (status.optInt("code")==1000){
                Gson gson=new Gson();
                Weatherlist weatherlist=gson.fromJson(string,Weatherlist.class);
                now.setText(weatherlist.getResult().getData().get(0).getNow_temp()+"℃");//当前温度
                city.setText(weatherlist.getResult().getData().get(0).getCity());//当前定位城市
                content.setText(Html.fromHtml(weatherlist.getResult().getData().get(0).getContent()));
                String weather=weatherlist.getResult().getData().get(0).getWeather();//当前的天气标识根据标识设置不同的图标
                /*设置天气图标开始*/
                setWeatherImg(weather,nowimg,null);//设置图标
                /*设置天气图标结束*/
                Log.i("传过来的天气详情",weatherlist.getResult().getData().size()+"");
                mlist.addAll(weatherlist.getResult().getData());
                beanBaseRecyclerAdapter.setData(mlist);



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setWeatherImg(String weather,ImageView nowimg,TextView txtWearth) {
        if (weather.equals("1")){
            if (txtWearth!=null){
                txtWearth.setText("多云");
            }

            Glide.with(this).load(R.mipmap.cloudy_icon).into(nowimg);
        } else  if (weather.equals("2")){
            if (txtWearth!=null){
                txtWearth.setText("海风");
            }

            Glide.with(this).load(R.mipmap.sea_breeze_icon).into(nowimg);
        }else  if (weather.equals("3")){
            if (txtWearth!=null){

                txtWearth.setText("晴");}
            Glide.with(this).load(R.mipmap.sun_home_icon).into(nowimg);
        }else  if (weather.equals("4")){
            if (txtWearth!=null){

                txtWearth.setText("雨转晴");}
            Glide.with(this).load(R.mipmap.rain_cleared_up_icon).into(nowimg);
        }else  if (weather.equals("5")){
            if (txtWearth!=null){

                txtWearth.setText("雪");}
            Glide.with(this).load(R.mipmap.snow_icon).into(nowimg);
        }else  if (weather.equals("6")){
            if (txtWearth!=null){

                txtWearth.setText("雨");}
            Glide.with(this).load(R.mipmap.rain_icon).into(nowimg);
        }else  if (weather.equals("7")){
            if (txtWearth!=null){

                txtWearth.setText("雨加雪");}
            Glide.with(this).load(R.mipmap.rain_snow_icon).into(nowimg);
        }else  if (weather.equals("8")){
            if (txtWearth!=null){

                txtWearth.setText("转晴");}
            Glide.with(this).load(R.mipmap.sunny_icon).into(nowimg);
        }else  if (weather.equals("0")){
            if (txtWearth!=null) {
                txtWearth.setText("未知");
            }

        }
    }

    @OnClick({R.id.layout_back})
   public  void  onClick(View v){
        switch (v.getId()){
            case R.id.layout_back://返回按钮
                finish();
                break;
        }
    }
    public int dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }
}
