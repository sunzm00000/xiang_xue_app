package com.example.fishingport.app.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airsaid.pickerviewlibrary.CityPickerView;
import com.airsaid.pickerviewlibrary.listener.OnSimpleCitySelectListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dd.CircularProgressButton;
import com.example.fishingport.app.Dao.PersonalDaoManager;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.SOSConstract;
import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.bean.Personal;
import com.example.fishingport.app.bean.PersonalDao;
import com.example.fishingport.app.presenter.SOSPresenter;
import com.example.fishingport.app.presenter.UserPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.tools.WeiboDialogUtils;
import com.example.fishingport.app.view.GlideCircleTransform;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/24.
 * 注册完成--个人资料编辑
 */

public class RegisterUserDataActivity extends BaseAppCompatActivity implements UserConstract.view,SOSConstract.view{
    @BindView(R.id.txt_nickname)
    EditText txtNickname;
    @BindView(R.id.txt_city)
    TextView txtCity;
    @BindView(R.id.layout_avatar)
    RelativeLayout layoutAvatar;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.img_arrow)
    ImageView imgArrow;
    private Dialog mWeiboDialog;
    private UserPresenter userPresenter;
    private int loadtype=0;
    private int REQUEST_IMAGE=1;
    private int loadType;
    private String path;
    private String chuanpath="";
    private  String huanxin_passwd="";
    private  String huanxin_name="";
    private SOSPresenter sosPresenter;
    private WifiManager mWifiManager;//wifi管理者，用于获取wifi连接时的信息

    private List<LocalMedia> selectMedia = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_registeruserdata;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void showInfo(final String string) {
        Log.e("string",string+loadType);
       WeiboDialogUtils.closeDialog(mWeiboDialog);
        try {
            JSONObject jsonObject=new JSONObject(string);
            if (jsonObject.getJSONObject("status").getString("code").equals("1000")){
                if (loadType==2) {
                    path=jsonObject.getJSONObject("result").getString("path_all");
                    chuanpath=jsonObject.optJSONObject("result").optString("path");
                    huanxin_passwd=  jsonObject.getJSONObject("result").getString("huanxin_passwd");
                    huanxin_name=jsonObject.getJSONObject("result").getString("huanxin_name");
                    Glide.with(this).load(path).transform(new GlideCircleTransform(this)).into(imgArrow);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    txtNickname.setFocusable(true);
                    txtNickname.setFocusableInTouchMode(true);
                    txtNickname.requestFocus();
                    imm.showSoftInput(txtNickname, 0);
                } else if (loadType==1){
                    //注册完成
                    HelpUtils.success(RegisterUserDataActivity.this,"注册完成");
                    JSONObject result=jsonObject.optJSONObject("result");
                   String  token=getIntent().getStringExtra("token");
                   final String avatar=jsonObject.getJSONObject("result").getString("avatar");
                   final String uid=jsonObject.getJSONObject("result").getString("uid");
                    SharedPreferencedUtils.setString(RegisterUserDataActivity.this,"token",token+"");
                    SharedPreferencedUtils.setString(RegisterUserDataActivity.this,"avatar",avatar);
                    SharedPreferencedUtils.setString(RegisterUserDataActivity.this,"uid",uid);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(string);
                                JSONObject result=jsonObject.optJSONObject("result");
                                PersonalDao personalDao = PersonalDaoManager.getInstance().getNewSession().getPersonalDao();
                                List<Personal> list = personalDao.queryBuilder().where(PersonalDao.Properties.Uid.eq(uid)).list();
                                Log.i("list有没有", list.size() + "");
                                if (list.size() == 0) {
                                    Personal personal = new Personal();
                                    personal.setHead_img(avatar);
                                    personal.setHuanxin_id(jsonObject.getJSONObject("result").getString("huanxin_name"));
                                    personal.setUid(uid+ "");
                                    personal.setId(null);
                                    personal.setNickname(jsonObject.getJSONObject("result").getString("nick_name"));
                                    personalDao.insert(personal);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    if (mWifiManager.getConnectionInfo().getSSID().toString().equals("\"APP-TEST\"")) {
                        //是港口wifi
                        sosPresenter.getwifimac();//获取wifimac
                    }else {
                        EMLogin(huanxin_name,huanxin_passwd);
                    }
                }else if (loadType==3){
                    Log.i("ac认证",string);
                    try {
                        JSONObject object=new JSONObject(string);
                        JSONObject status=object.optJSONObject("status");
                        JSONObject result=object.optJSONObject("result");
                        if (status.optInt("code")==1000){
                            Log.i("登录页ac认证成功","登录页ac认证成功");
                            EMLogin(huanxin_name,huanxin_passwd);
                        }
                        else {
                            // Toast.makeText(ConnectedDeviceActivity.this,"")
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
             }else {
                HelpUtils.warning(this,jsonObject.getJSONObject("status").getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showWifi(String string) {
        Log.i("wifimac",string);
        try {
            JSONObject object=new JSONObject(string);
            String userMac=object.optString("userMac");
            String deviceMac=object.optString("deviceMac");
            SharedPreferencedUtils.setString(RegisterUserDataActivity.this,"usermac",userMac+"");
            String token="";
            if (SharedPreferencedUtils.getString(RegisterUserDataActivity.this,"token")==null){
                token="";
            }else {
                token=SharedPreferencedUtils.getString(RegisterUserDataActivity.this,"token");
            }
            permit_client(token,userMac,deviceMac);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showsign(String s) {

    }
    @Override
    public void showErrMsg(String msg) {

    }
    //环信登录
    private void EMLogin(final String huanxin_name, final String huanxin_passwd){
        Log.i("登录环信执行了","登录环信执行 了"+huanxin_name+"/"+huanxin_passwd);
        EMClient.getInstance().login(huanxin_name,huanxin_passwd,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().chatManager().loadAllConversations();
                EMClient.getInstance().groupManager().loadAllGroups();
                Log.d("main登录成功", "登录聊天服务器成功");
                SharedPreferencedUtils.setString(RegisterUserDataActivity.this,"huanxinname",huanxin_name);
                SharedPreferencedUtils.setString(RegisterUserDataActivity.this,"huanxinpasswd",huanxin_passwd);
                startActivity(new Intent(RegisterUserDataActivity.this, MainActivity.class));
                finish();
            }
            @Override
            public void onProgress(int progress, String status) {
                Log.i("main登录中",progress+"/"+status);

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main登录失败", "登录聊天服务器失败！");
                if (code==200){
                    //说明用户已经登录需要先退出再登录
                    EMClient.getInstance().logout(true, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            EMLogin(huanxin_name,huanxin_passwd);//重新登录
                        }
                        @Override
                        public void onError(int i, String s) {
                        }
                        @Override
                        public void onProgress(int i, String s) {
                        }
                    });
                }else{
                   // 如果是其他错误就走这里
                    SharedPreferencedUtils.setString(RegisterUserDataActivity.this,"huanxinname",huanxin_name);
                    SharedPreferencedUtils.setString(RegisterUserDataActivity.this,"huanxinpasswd",huanxin_passwd);
                    startActivity(new Intent(RegisterUserDataActivity.this, MainActivity.class));
                    finish();
                }

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        userPresenter=new UserPresenter(this);
        sosPresenter=new SOSPresenter(this);
        //wifi管理类
        mWifiManager = (WifiManager) getApplicationContext().
                getSystemService(RegisterUserDataActivity.this.WIFI_SERVICE);
    }
    @OnClick({R.id.layout_avatar, R.id.btn_login,R.id.txt_city})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_avatar:
                pickImage();
                break;
            case R.id.btn_login:
                    loadType=1;
                    initUpdateUser(txtNickname.getText().toString(),chuanpath,txtCity.getText().toString());
                break;
            case  R.id.txt_city:
                CityPickerView mCityPickerView = new CityPickerView(this);
                // 设置点击外部是否消失
//        mCityPickerView.setCancelable(true);
                // 设置滚轮字体大小
//        mCityPickerView.setTextSize(18f);
                // 设置标题
//        mCityPickerView.setTitle("我是标题");
                // 设置取消文字
//        mCityPickerView.setCancelText("我是取消文字");
                // 设置取消文字颜色
//        mCityPickerView.setCancelTextColor(Color.GRAY);
                // 设置取消文字大小
//        mCityPickerView.setCancelTextSize(14f);
                // 设置确定文字
//        mCityPickerView.setSubmitText("我是确定文字");
                // 设置确定文字颜色
//        mCityPickerView.setSubmitTextColor(Color.BLACK);
                // 设置确定文字大小
//        mCityPickerView.setSubmitTextSize(14f);
                // 设置头部背景
//        mCityPickerView.setHeadBackgroundColor(Color.RED);
                mCityPickerView.setOnCitySelectListener(new OnSimpleCitySelectListener(){
                    @Override
                    public void onCitySelect(String prov, String city, String area) {
                        // 省、市、区 分开获取
                    }
                    @Override
                    public void onCitySelect(String str) {
                        // 一起获取
                        txtCity.setText(str);
                    }
                });
                mCityPickerView.show();
                break;
        }
    }
    /**
     * 注册完成添加用户信息
     * **/
    private void initUpdateUser(String nick_name,String avatar,String city){
        loadtype=1;
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(RegisterUserDataActivity.this, "加载中...");
        Map<String, String> encryptapMap = Utils.getSignParams(this, getIntent().getStringExtra("token"));
         encryptapMap.put("nick_name",nick_name);
          Map<String, String> map = Utils.getMap(this,getIntent().getStringExtra("token"));
          map.put("nick_name",nick_name);
          map.put("avatar",avatar);
          map.put("city",city);
         map.put("sign", Utils.getMd5StringMap(encryptapMap));
         userPresenter.loadUpdateUser(map);
    }
    private void pickImage() {
        FunctionOptions optionss = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE)
                .setCompress(true)//是否压缩
                .setGrade(Luban.THIRD_GEAR)
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(1)
                .setThemeStyle(ContextCompat.getColor(RegisterUserDataActivity.this, R.color.bule))
                //.setSelectMedia(selectMedia) // 已选图片，传入在次进去可选中，不能传入网络图片
                // .setStatusBar(R.drawable.bg_gradient_color)//状态栏颜色
                .setImageSpanCount(3) // 每行个数
                .create();
        // PictureConfig.getInstance().init(optionss);
        PictureConfig.getInstance().init(optionss).openPhoto(RegisterUserDataActivity.this,
                new PictureConfig.OnSelectResultCallback() {
                    @Override
                    public void onSelectSuccess(List<LocalMedia> list) {
                        selectMedia = list;
                        if (selectMedia.size() > 0) {
                            Bitmap bitmap = HelpUtils.compressBitmap(selectMedia.get(0).getPath(), 600, 1024, 30);
                            loadType = 2;
                            initAddAppraise("user", "2", Utils.bitmapToBase64(bitmap));
                        }
                    }
                    @Override
                    public void onSelectSuccess(LocalMedia localMedia) {

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    /**
     *上传图片（单张）
     */
    private void initAddAppraise(String res_name,String type,String image){
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(RegisterUserDataActivity.this, "加载中...");
        Log.e("img",image);
        Map<String, String> encryptapMap = Utils.getSignParams(this, getIntent().getStringExtra("token"));
        // encryptapMap.put("uid",uid);
        Map<String, String> map = Utils.getMap(this,getIntent().getStringExtra("token"));
        map.put("res_name",res_name);
        map.put("type",type);
        map.put("image",image);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        userPresenter.loadAddAppraise(map);
    }


    public  void  permit_client(String token,String userMac,String deviceMac)
    {
        loadType=3;
        Log.i("ac端认证",token+"/"+userMac+"/"+deviceMac);
        Map<String,String> signmap= Utils.getSignParams(RegisterUserDataActivity.this,token);
        Map<String,String> map=Utils.getMap(RegisterUserDataActivity.this,token);
        map.put("user_mac",userMac);
        map.put("device_mac",deviceMac);
        map.put("sign",Utils.getMd5StringMap(signmap));
        sosPresenter.permit_client(map);
    }

}
