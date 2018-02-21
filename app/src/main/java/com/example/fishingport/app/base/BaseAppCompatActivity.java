package com.example.fishingport.app.base;

import android.Manifest;
import android.content.Context;

import android.content.pm.ActivityInfo;

import android.content.Intent;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LocationUtils;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.TrackConstract;
import com.example.fishingport.app.presenter.TrackPresenter;
import com.example.fishingport.app.service.AutoService;
import com.example.fishingport.app.service.LocationSevice;
import com.example.fishingport.app.service.MapService;
import com.example.fishingport.app.tools.StatusBarCompat;
import com.example.fishingport.app.ui.activity.MainActivity;
import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;



/**
 * Author: wushixin (wusx@alltosun.com)
 * Date: 2016/12/21
 * Content: Activity基类
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements
        OnPermissionCallback{
    private static final String TAG = BaseAppCompatActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView textRight,mToolbarTitle;//右边的部分
    private ImageView img_right;
    private PermissionHelper mPermissionHelper;
    private final static String[] MULTI_PERMISSIONS = new String[]{
            //需要动态申请的权限
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private RelativeLayout layout_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
//       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//           getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        // 激活状态栏
//        tintManager.setStatusBarTintEnabled(true);
//        // enable navigation bar tint 激活导航栏
//        tintManager.setNavigationBarTintEnabled(true);
//        //设置系统栏设置颜色
//        tintManager.setTintColor(R.drawable.bg_gradient_color);
//        //给状态栏设置颜色
//        tintManager.setStatusBarTintResource(R.drawable.bg_gradient_color);
//        //Apply the specified drawable or color resource to the system navigation bar.
//        //给导航栏设置资源
//        tintManager.setNavigationBarTintResource(R.drawable.bg_gradient_color);
//    }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            StatusBarCompat.compat(this);
        }
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);//竖屏

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        layout_back = (RelativeLayout) findViewById(R.id.layout_back);
        mToolbarTitle= (TextView) findViewById(R.id.txt_title);//标题
        textRight= (TextView) findViewById(R.id.txt_right);//右边
        img_right=(ImageView) findViewById(R.id.img_right);//右边图标

        if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar);
        }
        if (mToolbarTitle != null) {
            //getTitle()的值是activity的android:lable属性值
            mToolbarTitle.setText(getTitle());
            //设置默认的标题不显示
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
         /*权限动态申请*/
        mPermissionHelper = PermissionHelper.getInstance(this);
        mPermissionHelper.request(MULTI_PERMISSIONS);
        //autogrenndao();//启动服务自动记录轨迹

    }


    @Override
    protected void onStart() {
        super.onStart();
        /**
         * 判断是否有Toolbar,并默认显示返回按钮
         */
        if(null != getToolbar() && isShowBacking()){
            layout_back.setVisibility(View.VISIBLE);

            showBack();
        }else {
            layout_back.setVisibility(View.GONE);
        }
    }

    /**
     * 设置头部标题
     * @param title
     */
    public void setToolBarTitle(CharSequence title) {
        if(mToolbarTitle != null){
            mToolbar.setTitle("");
            mToolbarTitle.setText(title);
        }else{
            getToolbar().setTitle(title);
            setSupportActionBar(getToolbar());
        }
    }
    /**
     * 设置右边标题
     * @param title
     */
    public TextView setTextRight(String title) {
        textRight.setVisibility(View.VISIBLE);
        textRight.setText(title);
        return textRight;
    }

    /**
     * 设置右边图标
     *
     */
    public ImageView getImgRight() {
        img_right.setVisibility(View.VISIBLE);
        return img_right;
    }
    /**
     * this Activity of tool bar.
     * 获取头部.
     * @return support.v7.widget.Toolbar.
     */
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    /**
     * 版本号小于21的后退按钮图片
     */
    private void showBack(){
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
//        getToolbar().setNavigationIcon(R.mipmap.back_icon);
//        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     * @return
     */
    protected boolean isShowBacking(){
        return true;
    }

    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     * @return res layout xml id
     */
    protected abstract int getLayoutId();



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy...");
        stopService(new Intent(this,LocationSevice.class));
        stopService(new Intent(this,AutoService.class));
        stopService(new Intent(this,MapService.class));


    }


    //布局中Fragment的ID
    protected abstract int getFragmentContentId();

    //添加fragment
    protected void addFragment(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    //移除fragment
    protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void onPermissionGranted(String[] permissionName) {
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }



    @Override
    public void onPermissionDeclined(String[] permissionName) {

    }

    @Override
    public void onPermissionPreGranted(String permissionsName) {

    }

    @Override
    public void onPermissionNeedExplanation(String permissionName) {

    }

    @Override
    public void onPermissionReallyDeclined(String permissionName) {

    }

    @Override
    public void onNoPermissionNeeded() {

    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



    /*
    * 自动记录轨迹
    *
    * */
    public void autogrenndao(){
        Intent intent=new Intent(BaseAppCompatActivity.this, AutoService.class);
        startService(intent);

    }


}
