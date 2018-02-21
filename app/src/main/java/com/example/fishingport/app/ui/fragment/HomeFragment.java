package com.example.fishingport.app.ui.fragment;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.HomeConstract;
import com.example.fishingport.app.base.BaseFragment;
import com.example.fishingport.app.model.CategoryBean;
import com.example.fishingport.app.presenter.HomePresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.ui.activity.LaunchActivity;
import com.example.fishingport.app.ui.activity.NewsPolicyActivity;
import com.example.fishingport.app.ui.activity.QuotationActivity;
import com.example.fishingport.app.ui.activity.TrainingActivity;
import com.example.fishingport.app.ui.activity.WeatherDetailsaActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/13.
 * 首页
 */

public class HomeFragment extends BaseFragment implements HomeConstract.view {

    @BindView(R.id.layout_news_policy)
    RelativeLayout layoutNewsPolicy;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.txt_seaport)
    TextView txtSeaport;
    @BindView(R.id.img_sun_home)
    ImageView imgSunHome;
    @BindView(R.id.txt_temperature)
    TextView txtTemperature;
    @BindView(R.id.txt_wearth)
    TextView txtWearth;
    @BindView(R.id.btn_video)
    RelativeLayout btn_video;
    @BindView(R.id.tianqi)
    RelativeLayout tianqi;
    private ViewPager mVpBody;
    private ArrayList<Fragment> fragmentsList;
    private int currIndex;
    private List<String> titles;
    private HomePresenter homePresenter;
    private TabLayout mTabLayout;
    private Fragment recommendFragment;
    private int loadType;
    private int loadtypelist=0;
    private  String weather;
    @BindView(R.id.txt_network)
    TextView txt_network;//是否显示安全网络连接成功
    private WifiManager mWifiManager;//wifi管理者，用于获取wifi连接时的信息
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initViewPager(view);
        initCategory();
        mWifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(getActivity().WIFI_SERVICE);
        if (mWifiManager.getConnectionInfo().getSSID().equals("\"APP-TEST\"")) {
            txt_network.setVisibility(View.VISIBLE);
            txt_network.setText("安全网络已经连接成功");
        }else {
            txt_network.setVisibility(View.GONE);
        }
        String city=SharedPreferencedUtils.getString(getActivity(),"city_addr");
         //Log.i("定位的成功",city);
        if (city!=null){
            txtSeaport.setText(city);
            initCityId(city);
        }else {
            initCityId("朝阳区");
            txtSeaport.setText("朝阳区");
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    private void initViewPager(View view) {
        try {
            homePresenter = new HomePresenter(this);
            mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
            mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            titles = new ArrayList<>();
            mVpBody = (ViewPager) view.findViewById(R.id.vp_body);
            fragmentsList = new ArrayList<Fragment>();
        } catch (Exception e) {

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
    @OnClick({R.id.layout_news_policy, R.id.toolbar, R.id.layout_quotation,R.id.btn_video,R.id.btn_training})
    public void onClick(View view) {
        switch (view.getId()) {
            case  R.id.btn_training://知识培训
                startActivity(new Intent(getActivity(),TrainingActivity.class));
                break;
            case R.id.layout_news_policy://新闻政策
                startActivity(new Intent(getActivity(), NewsPolicyActivity.class));
                break;
            case R.id.layout_quotation://渔货行情
                startActivity(new Intent(getActivity(), QuotationActivity.class));
                break;
            case R.id.toolbar:
                break;
            case R.id.btn_video://视频
                Toast.makeText(getActivity(),"敬请期待",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    /**
     * 取分类列表
     */
    private void initCategory() {
        loadtypelist = 1;
        Map<String, String> encryptapMap = Utils.getSignParams(getActivity(), SharedPreferencedUtils.getString(getActivity(), "token"));
        encryptapMap.put("res_name", "news");
        Map<String, String> map = Utils.getMap(getActivity(), SharedPreferencedUtils.getString(getActivity(), "token"));
        map.put("page", "1");
        map.put("per_page", "10");
        map.put("res_name", "news");
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        homePresenter.loadCategoryList(map);
    }
    /**
     * 取所在城市的天气
     */
    private void initWeatherList(String city_id) {
        loadType = 3;
        Log.i("执行了取天气的接口",city_id+"");
        Map<String, String> encryptapMap = Utils.getSignParams(getActivity(), SharedPreferencedUtils.getString(getActivity(), "token"));
        encryptapMap.put("city_id", city_id);
        Map<String, String> map = Utils.getMap(getActivity(), SharedPreferencedUtils.getString(getActivity(), "token"));
        map.put("city_id", city_id);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        homePresenter.loadWeatherList(map);
    }
    /**
     * 取城市id
     */
    private void initCityId(String city) {
        Map<String, String> encryptapMap = Utils.getSignParams(getActivity(), SharedPreferencedUtils.getString(getActivity(), "token"));
        encryptapMap.put("city", city);
        Map<String, String> map = Utils.getMap(getActivity(), SharedPreferencedUtils.getString(getActivity(), "token"));
        map.put("city", city);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        homePresenter.loadCityId(map);
    }
    @Override
    public void showInfo(String string) {
        if (loadtypelist == 1) {
            Log.i("列表",string);
            Gson gson = new Gson();
            CategoryBean categoryBean = gson.fromJson(string, CategoryBean.class);
            List<CategoryBean.ResultBean.DataBean> list=new ArrayList<>();
            titles.add("推荐");
            recommendFragment = RecommendFragment.newInstance
                    (0+ "");
            fragmentsList.add(recommendFragment);
            for (int i = 0; i < categoryBean.getResult().getData().size(); i++) {
                titles.add(categoryBean.getResult().getData().get(i).getCategory_title());
                recommendFragment = RecommendFragment.newInstance
                        (categoryBean.getResult().getData().get(i).getCategory_id() + "");
                fragmentsList.add(recommendFragment);
            }
            TabFragmentPagerAdapter tabFragmentPagerAdapter = new TabFragmentPagerAdapter(
                    getChildFragmentManager(), fragmentsList, titles);
            mVpBody.setAdapter(tabFragmentPagerAdapter);
            mVpBody.setOnPageChangeListener(new MyOnPageChangeListener());
            mTabLayout.setupWithViewPager(mVpBody);
        }
    }
    @Override
    public void showcityid(final String ss) {
        Log.e("listjjj", ss);
      if (loadType == 3) {
            Log.i("天气情况",ss);
            try {
                JSONObject jsonObject = new JSONObject(ss);
                if (jsonObject.getJSONObject("status").getInt("code") == 1000) {
                    JSONObject result=jsonObject.getJSONObject("result");
                    JSONArray data=result.optJSONArray("data");
                    JSONObject oneobject=data.optJSONObject(0);
                    String temperature=oneobject.getString("temperature");
                    String weather=oneobject.getString("weather");
                    if (weather.equals("1")){
                        txtWearth.setText("多云");
                        Glide.with(getActivity()).load(R.mipmap.cloudy_icon).into(imgSunHome);
                    } else  if (weather.equals("2")){
                        txtWearth.setText("海风");
                        Glide.with(getActivity()).load(R.mipmap.sea_breeze_icon).into(imgSunHome);
                    }else  if (weather.equals("3")){
                        txtWearth.setText("晴");
                        Glide.with(getActivity()).load(R.mipmap.sun_home_icon).into(imgSunHome);
                    }else  if (weather.equals("4")){
                        txtWearth.setText("雨转晴");
                        Glide.with(getActivity()).load(R.mipmap.rain_cleared_up_icon).into(imgSunHome);
                    }else  if (weather.equals("5")){
                        txtWearth.setText("雪");
                        Glide.with(getActivity()).load(R.mipmap.snow_icon).into(imgSunHome);
                    }else  if (weather.equals("6")){
                        txtWearth.setText("雨");
                        Glide.with(getActivity()).load(R.mipmap.rain_icon).into(imgSunHome);
                    }else  if (weather.equals("7")){
                        txtWearth.setText("雨加雪");
                        Glide.with(getActivity()).load(R.mipmap.rain_snow_icon).into(imgSunHome);
                    }else  if (weather.equals("8")){
                        txtWearth.setText("转晴");
                        Glide.with(getActivity()).load(R.mipmap.sunny_icon).into(imgSunHome);
                    }else  if (weather.equals("0")){
                        txtWearth.setText("未知");
                    }
                    txtTemperature.setText(temperature +"℃");
                    tianqi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //跳转到天气页面
                            Intent intent=new Intent(getActivity(),WeatherDetailsaActivity.class);
                            intent.putExtra("string",ss+"");
                            startActivity(intent);
                        }
                    });
                } else {
                    HelpUtils.warning(mActivity, jsonObject.getJSONObject("status").getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
        try {
            JSONObject jsonObject = new JSONObject(ss);
            if (jsonObject.getJSONObject("status").getInt("code") == 1000) {
                weather=jsonObject.getString("result");
                initWeatherList(weather);
            } else {
                HelpUtils.warning(mActivity,jsonObject.getJSONObject("status").getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
      }
    }

    @Override
    public void showErrMsg(String msg) {

    }
    public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> mFragmentsList;
        List<String> list;
        public TabFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentsList, List<String> list) {
            super(fm);
            this.mFragmentsList = fragmentsList;
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position);
        }
    }

    public class TabOnClickListener implements View.OnClickListener {
        private int index = 0;

        public TabOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mVpBody.setCurrentItem(index);
        }
    }



    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {

            switch (arg0) {
                case 0:


                    break;
                case 1:

                    break;
                case 2:

                    break;

            }
            currIndex = arg0;

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}