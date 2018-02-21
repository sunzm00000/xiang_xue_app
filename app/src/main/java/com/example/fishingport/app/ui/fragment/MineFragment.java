package com.example.fishingport.app.ui.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.fishingport.app.R;
import com.example.fishingport.app.api.SOSConstract;
import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.base.BaseFragment;
import com.example.fishingport.app.model.UserInfoBean;
import com.example.fishingport.app.presenter.SOSPresenter;
import com.example.fishingport.app.presenter.UserPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.IntegralDialog;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.SignDialog;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.ui.activity.Common_ProblemsActivity;
import com.example.fishingport.app.ui.activity.DymamicActivity;
import com.example.fishingport.app.ui.activity.FeedbackActivity;
import com.example.fishingport.app.ui.activity.IntegralActivity;
import com.example.fishingport.app.ui.activity.MailListActivity;
import com.example.fishingport.app.ui.activity.MainActivity;
import com.example.fishingport.app.ui.activity.MyNoticeActivity;
import com.example.fishingport.app.ui.activity.NearbyPortActivity;
import com.example.fishingport.app.ui.activity.SOSActivity;
import com.example.fishingport.app.ui.activity.SettingActivity;
import com.example.fishingport.app.ui.activity.TarckActivity;
import com.example.fishingport.app.ui.activity.UserDataActivity;
import com.example.fishingport.app.view.GlideCircleTransform;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wushixin on 2017/4/13.
 */

public class MineFragment extends BaseFragment implements UserConstract.view,SOSConstract.view{
    @BindView(R.id.txt_mine_title)
    TextView txtMineTitle;
    @BindView(R.id.img_user)
    ImageView imgUser;
    @BindView(R.id.txt_user_name)
    TextView txtUserName;
    @BindView(R.id.txt_user_autograph)
    TextView txtUserAutograph;
    @BindView(R.id.layout_user_info)
    RelativeLayout layoutUserInfo;
    @BindView(R.id.txt_user_integral)
    TextView txtUserIntegral;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.layout_friends)
    RelativeLayout layoutFriends;
    @BindView(R.id.layout_dynamic)
    RelativeLayout layoutDynamic;
    @BindView(R.id.layout_trajectory)
    RelativeLayout layoutTrajectory;
    @BindView(R.id.layout_port)
    RelativeLayout layoutPort;
    @BindView(R.id.layout_sign)
    RelativeLayout layoutSign;
    @BindView(R.id.layout_sos)
    RelativeLayout layoutSos;
    @BindView(R.id.img_info)
    ImageView imgInfo;
    @BindView(R.id.layout_info)
    RelativeLayout layoutInfo;
    @BindView(R.id.img_edit)
    ImageView imgEdit;
    @BindView(R.id.layout_edit)
    RelativeLayout layoutEdit;
    @BindView(R.id.img_setting)
    ImageView imgSetting;
    @BindView(R.id.layout_setting)
    RelativeLayout layoutSetting;
    @BindView(R.id.txt_number)
    TextView txtNumber;
    private SignDialog dialog;
    private IntegralDialog integralDialog;
    private UserPresenter userPresenter;
    private SOSPresenter sosPresenter;
    private  int islayout_notice=0;
    String noreadcomment_num=0+"";//未读评论数
    String noreadappraise_num=0+"";//未读点赞数
    private String gscore="0";
    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        userPresenter = new UserPresenter(this);
        sosPresenter=new SOSPresenter(this);
        initUserInfo("");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initUserInfo("");
        getappcom(SharedPreferencedUtils.getString(getActivity(),"token"));
    }

    @OnClick({R.id.layout_user_info, R.id.layout_friends, R.id.layout_dynamic, R.id.layout_trajectory, R.id.layout_port, R.id.layout_sign, R.id.layout_sos, R.id.layout_info, R.id.layout_edit, R.id.layout_setting, R.id.linear_integral, R.id.layout_notice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_user_info://个人资料详情
                startActivity(new Intent(getActivity(), UserDataActivity.class));
                break;
            case R.id.layout_friends://通讯录
                startActivity(new Intent(getActivity(), MailListActivity.class));
                break;
            case R.id.layout_dynamic://动态--我的渔友圈
                Intent intents=new Intent(getActivity(),DymamicActivity.class);
                intents.putExtra("type","0");
                intents.putExtra("uid",SharedPreferencedUtils.getString(getActivity(), "uid"));
                intents.putExtra("isme","我的");
                startActivity(intents);
                break;
            case R.id.layout_trajectory:
                //跳转到我的轨迹页面
                Intent intent = new Intent(getActivity(), TarckActivity.class);
                intent.putExtra("type", 1 + "");
                startActivity(intent);
                break;
            case R.id.layout_port:
                Intent Nearintent=new Intent(getActivity(),NearbyPortActivity.class);
                Nearintent.putExtra("type",1+"");
                startActivity(Nearintent);
                break;
            case R.id.layout_sign:
                dialog();
                break;
            case R.id.layout_sos:
                startActivity(new Intent(getActivity(), SOSActivity.class));
                break;
            case R.id.layout_info://常见问题列表
                startActivity(new Intent(getActivity(), Common_ProblemsActivity.class));
                break;
            case R.id.layout_edit://意见反馈
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.layout_setting://设置
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.linear_integral://积分
                Intent intent1=new Intent(getActivity(),IntegralActivity.class);
                intent1.putExtra("socre",gscore);
                startActivity(intent1);
                break;
            case R.id.layout_notice://我的消息
                islayout_notice=1;
                Intent  noticeintent=new Intent(getActivity(), MyNoticeActivity.class);
                noticeintent.putExtra("appraise_num",noreadappraise_num);//未读点赞
                noticeintent.putExtra("comment_num",noreadcomment_num);//未读评论数
                startActivity(noticeintent);
                break;
        }
    }
    private void IntegralDialog(String days, String score) {
        integralDialog = new IntegralDialog(getActivity());
        integralDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击我知道了的按钮
                integralDialog.dismiss();//取消弹框
            }
        });
        integralDialog.setviewtext("恭喜获得" + score + "个积分", "连续签到第" + days + "天");
        integralDialog.show();
    }

    // 弹窗
    private void dialog() {
        dialog = new SignDialog(getActivity());
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dosomething youself
                if (HelpUtils.isNetworkAvailable(getActivity().getApplicationContext())) {
                    sign_in();
                } else {
                    HelpUtils.warning(getActivity(),"暂无网络!");
                }
                //dialog.dismiss();
            }
        });
        dialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showInfo(String string) {
        Log.e("个人信息", string);
        Gson gson = new Gson();
        UserInfoBean userInfoBean = gson.fromJson(string, UserInfoBean.class);
        txtUserName.setText(userInfoBean.getResult().getNick_name());
        if (userInfoBean.getResult().getIntro().equals("")){
            txtUserAutograph.setVisibility(View.GONE);
        }else {
            txtUserAutograph.setText(userInfoBean.getResult().getIntro());
        }
        txtUserIntegral.setText("我的积分：" + userInfoBean.getResult().getScore());
        gscore=userInfoBean.getResult().getScore();
        noreadappraise_num =userInfoBean.getResult().getAppraise_num();//未读点赞
        noreadcomment_num=userInfoBean.getResult().getComment_num();//未读评论
        //总的未读数
        int zong=(Integer.parseInt(userInfoBean.getResult().getAppraise_num())+
                (Integer.parseInt(userInfoBean.getResult().getComment_num())));
        if (zong==0){
            txtNumber.setVisibility(View.GONE);
        }else {
            txtNumber.setVisibility(View.VISIBLE);
            txtNumber.setText(zong+"");
        }
        RequestManager glideRequest;
        glideRequest = Glide.with(this);
        glideRequest.load(userInfoBean.getResult().getAvatar()).
                transform(new GlideCircleTransform(getActivity())).into(imgUser);
    }

    @Override
    public void showWifi(String string) {
        Log.i("点赞数，评论数",string);
        try {
            JSONObject jsonObject=new JSONObject(string);
            JSONObject status=jsonObject.optJSONObject("status");
            JSONObject result=jsonObject.optJSONObject("result");
            if (status.optInt("code")==1000){
                noreadappraise_num=result.optString("appraise_num");
                noreadcomment_num=result.optString("comment_num");
                //总的未读数
                int zong=(Integer.parseInt(noreadappraise_num)+
                        (Integer.parseInt(noreadcomment_num)));
                if (zong==0){
                    txtNumber.setVisibility(View.GONE);
                }else {
                    txtNumber.setVisibility(View.VISIBLE);
                    txtNumber.setText(zong+"");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showsign(String s) {
        Log.i("签到", s.toString());
        try {
            JSONObject object = new JSONObject(s);
            JSONObject status = object.optJSONObject("status");
            JSONObject result = object.optJSONObject("result");
            if (status.optInt("code") == 1000) {
                dialog.dismiss();
                String days = result.optString("days");
                String score = result.optString("score");//每次几个积分
                SharedPreferencedUtils.setString(getActivity(),"qiandao",days);
                IntegralDialog(days, score);
            } else {
                HelpUtils.warning(getActivity(),status.optString("message")+"");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showErrMsg(String msg) {

    }
    /**
     * 用户信息
     */
    private void initUserInfo(String uid) {
        Map<String, String> encryptapMap = Utils.getSignParams(getActivity(), SharedPreferencedUtils.getString(getActivity(), "token"));
        // encryptapMap.put("uid",uid);
        Map<String, String> map = Utils.getMap(getActivity(), SharedPreferencedUtils.getString(getActivity(), "token"));
        //  map.put("uid",uid);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        userPresenter.loadUserInfo(map);
    }

    /**
     * 用户签到
     */
    public void sign_in() {
        Map<String, String> signmap = Utils.getSignParams(getActivity(), SharedPreferencedUtils.getString(getActivity(), "token"));
        Map<String, String> map = Utils.getMap(getActivity(), SharedPreferencedUtils.getString(getActivity(), "token"));

        map.put("sign", Utils.getMd5StringMap(signmap));
        userPresenter.sign_in(map);
    }

    /*
    * 未读点赞和评论列表
    * */
    public  void getappcom(String token){
        Map<String,String> signmap=Utils.getSignParams(getActivity(),token);
        Map<String,String> map=Utils.getMap(getActivity(),token);
        map.put("sign",Utils.getMd5StringMap(signmap));
        sosPresenter.getappcom(map);
    }
}