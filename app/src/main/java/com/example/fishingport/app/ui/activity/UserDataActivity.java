package com.example.fishingport.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.model.ImageReleaseDynamics;
import com.example.fishingport.app.model.UserInfoBean;
import com.example.fishingport.app.presenter.UserPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.view.GlideCircleTransform;
import com.google.gson.Gson;

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
 * Created by wushixin on 2017/4/14.
 * 个人资料
 */

public class UserDataActivity extends BaseAppCompatActivity implements UserConstract.view {
    @BindView(R.id.img_avatar)
    ImageView imgAvatar; //头像
    @BindView(R.id.txt_nickname)
    TextView txtNickname; //昵称
    @BindView(R.id.layout_nickname)
    RelativeLayout layoutNickname;
    @BindView(R.id.txt_gender)
    TextView txtGender; //性别
    @BindView(R.id.layout_gender)
    RelativeLayout layoutGender;
    @BindView(R.id.txt_birthday)
    TextView txtBirthday; //生日
    @BindView(R.id.layout_birthday)
    RelativeLayout layoutBirthday;
    @BindView(R.id.txt_city)
    TextView txtCity; //地区
    @BindView(R.id.layout_city)
    RelativeLayout layoutCity;
    @BindView(R.id.txt_intro)
    TextView txtIntro; //个人签名
    @BindView(R.id.layout_intro)
    RelativeLayout layoutIntro;
    @BindView(R.id.txt_user_name)
    TextView txtUserName; //真实姓名
    @BindView(R.id.layout_user_name)
    RelativeLayout layoutUserName;
    @BindView(R.id.txt_id_card)
    TextView txtIdCard;//身份证
    @BindView(R.id.layout_id_card)
    RelativeLayout layoutIdCard;
    @BindView(R.id.txt_mobile)
    TextView txtMobile;//手机号
    @BindView(R.id.layout_mobile)
    RelativeLayout layoutMobile;
    @BindView(R.id.txt_emails)
    TextView txtEmails; //邮箱
    @BindView(R.id.layout_emails)
    RelativeLayout layoutEmails;
    private UserPresenter userPresenter;
    private int REQUEST_IMAGE = 1;
    private int loadType = 1;
    String text = "";
    private  int istiao=0;
    private List<LocalMedia> selectMedia = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userdata;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        userPresenter = new UserPresenter(this);
        setToolBarTitle("个人资料");
        // setTextRight("编辑");
        loadType=1;
        initUserInfo("");
    }

    @Override
    public void showInfo(String string) {
        Log.i("打印数据==",string+"/"+loadType);
        if (loadType == 1) {
            Gson gson = new Gson();
            UserInfoBean userInfoBean = gson.fromJson(string, UserInfoBean.class);
            RequestManager glideRequest;
            glideRequest = Glide.with(this);
            glideRequest.load(userInfoBean.getResult().getAvatar()).transform(new
                    GlideCircleTransform(this)).into(imgAvatar);
            txtNickname.setText(userInfoBean.getResult().getNick_name());
            txtBirthday.setText(userInfoBean.getResult().getBirthday());
            txtIdCard.setText(userInfoBean.getResult().getId_card());
            txtIntro.setText(userInfoBean.getResult().getIntro());
            if (Integer.parseInt(userInfoBean.getResult().getGender()) == 1) {
                text = "男";
            } else if (Integer.parseInt(userInfoBean.getResult().getGender()) == 2) {
                text = "女";
            } else {
                text = "";
            }
            txtGender.setText(text);
            txtCity.setText(userInfoBean.getResult().getCity());
            txtUserName.setText(userInfoBean.getResult().getUser_name());
            txtMobile.setText(userInfoBean.getResult().getMobile());
            txtEmails.setText(userInfoBean.getResult().getEmail());
        } else if (loadType==2){
            Log.i("用户头像返回的数据",string);
            try {
                JSONObject jsonObject = new JSONObject(string);
                if (jsonObject.getJSONObject("status").getString("code").equals("1000")) {
                    Glide.with(this).load(jsonObject.getJSONObject("result").getString("path_all")).
                            transform(new GlideCircleTransform(this)).into(imgAvatar);

                    initEditField("avatar",jsonObject.getJSONObject("result").getString("path"));
                } else {
                    HelpUtils.warning(UserDataActivity.this, "" + jsonObject.getJSONObject("status").getString("message"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (loadType==3){
            Log.i("调用修改个人资料的接口",string);
            try {
                JSONObject jsonObject=new JSONObject(string);
                JSONObject status=jsonObject.optJSONObject("status");
                if (status.optInt("code")==1000){
                    HelpUtils.success(UserDataActivity.this,"修改头像成功");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showsign(String s) {

    }

    @Override
    public void showErrMsg(String msg) {

    }

    /**
     * 用户信息
     */
    private void initUserInfo(String uid) {
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        // encryptapMap.put("uid",uid);
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        //  map.put("uid",uid);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        userPresenter.loadUserInfo(map);
    }

    /**
     * 上传图片（单张）
     */
    private void initAddAppraise(String res_name, String type, String image) {
        loadType=2;
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        // encryptapMap.put("uid",uid);
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("res_name", res_name);
        map.put("type", type);
        map.put("image", image);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        userPresenter.loadAddAppraise(map);
    }

    @OnClick({R.id.img_avatar, R.id.layout_nickname, R.id.layout_gender, R.id.layout_birthday, R.id.layout_city, R.id.layout_intro, R.id.layout_user_name, R.id.layout_id_card, R.id.layout_mobile, R.id.layout_emails})
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(UserDataActivity.this, MyModifyActivity.class);
        istiao=1;
        switch (view.getId()) {
            case R.id.img_avatar:
                pickImage();
                break;
            case R.id.layout_nickname:
                intent.putExtra("type", 0); //0 昵称
                intent.putExtra("text", txtNickname.getText().toString());
                startActivityForResult(intent, 0);
                break;
            case R.id.layout_gender:
                intent.putExtra("type", 1); //1 性别
                intent.putExtra("text", text);
                startActivityForResult(intent, 1);
                break;
            case R.id.layout_birthday:
                intent.putExtra("type", 2); //2 生日
                intent.putExtra("text", txtBirthday.getText().toString());
                startActivityForResult(intent, 2);
                break;
            case R.id.layout_city:
                intent.putExtra("type", 3); //3 地区
                intent.putExtra("text", txtCity.getText().toString());
                startActivityForResult(intent, 3);
                break;
            case R.id.layout_intro:
                intent.putExtra("type", 4); //4 个人简介
                intent.putExtra("text", txtIntro.getText().toString());
                startActivityForResult(intent, 4);
                break;
            case R.id.layout_user_name:
                intent.putExtra("type", 5); //5 真实姓名
                intent.putExtra("text", txtUserName.getText().toString());
                startActivityForResult(intent, 5);
                break;
            case R.id.layout_id_card:
                intent.putExtra("type", 6); //6 身份证
                intent.putExtra("text", txtIdCard.getText().toString());
                startActivityForResult(intent, 6);
                break;
            case R.id.layout_mobile:
                intent.putExtra("type", 7); //7 手机号
                intent.putExtra("text", txtMobile.getText().toString());
                startActivityForResult(intent, 7);
                break;
            case R.id.layout_emails:
                intent.putExtra("type", 8); //8 邮箱
                intent.putExtra("text", txtEmails.getText().toString());
                startActivityForResult(intent, 8);
                break;
        }

    }


    private void pickImage() {

        FunctionOptions optionss = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE)
                .setCompress(true)//是否压缩
                .setGrade(Luban.THIRD_GEAR)
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(1)
                .setThemeStyle(ContextCompat.getColor(UserDataActivity.this, R.color.bule))
                //.setSelectMedia(selectMedia) // 已选图片，传入在次进去可选中，不能传入网络图片
                // .setStatusBar(R.drawable.bg_gradient_color)//状态栏颜色
                .setImageSpanCount(3) // 每行个数
                .create();
        // PictureConfig.getInstance().init(optionss);
        PictureConfig.getInstance().init(optionss).openPhoto(UserDataActivity.this,
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
    protected void onResume() {
        super.onResume();
        if (istiao==1){
            initUserInfo("");
            istiao=0;
        }
    }

    /**
     * 修改个人信息
     */
    private void initEditField(String field,String field_value) {
        loadType=3;
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("field", field);
        map.put("field_value",field_value);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        userPresenter.loadEditField(map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            switch (requestCode) {
                case 0:
                    HelpUtils.success(UserDataActivity.this, "修改昵称成功!");
                    break;
                case 1:
                    HelpUtils.success(UserDataActivity.this, "修改性别成功!");
                    break;
                case 2:
                    HelpUtils.success(UserDataActivity.this, "修改生日成功!");
                    break;
                case 3:
                    HelpUtils.success(UserDataActivity.this, "修改地区成功!");
                    break;
                case 4:
                    HelpUtils.success(UserDataActivity.this, "修改个人简介成功!");
                    break;
                case 5:
                    HelpUtils.success(UserDataActivity.this, "修改真实姓名成功!");
                    break;
                case 6:
                    HelpUtils.success(UserDataActivity.this, "修改身份证成功!");
                    break;
                case 7:
                    HelpUtils.success(UserDataActivity.this, "修改手机号成功!");
                    break;

                case 8:
                    HelpUtils.success(UserDataActivity.this, "修改邮箱成功!");

                    break;
            }

        }
    }
}
