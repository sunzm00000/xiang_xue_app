package com.example.fishingport.app.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.fishingport.app.Dao.GreenDaoManager;
import com.example.fishingport.app.R;
import com.example.fishingport.app.adapter.NoHorizontalScrollerVPAdapter;
import com.example.fishingport.app.api.FishingCircleConstract;
import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.bean.LocationDao;
import com.example.fishingport.app.model.ImageModel;
import com.example.fishingport.app.model.ImageReleaseDynamics;
import com.example.fishingport.app.model.ScoreRecordList;
import com.example.fishingport.app.model.TrajectoryList;
import com.example.fishingport.app.presenter.FishingCirclePresenter;
import com.example.fishingport.app.presenter.UserPresenter;
import com.example.fishingport.app.tools.EmotionUtils;
import com.example.fishingport.app.tools.FragmentFactory;
import com.example.fishingport.app.tools.GlobalOnItemClickManagerUtils;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.LocationhelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.tools.WeiboDialogUtils;
import com.example.fishingport.app.ui.fragment.Fragment1;
import com.example.fishingport.app.view.GlideCircleTransform;
import com.example.fishingport.app.view.emotionkeyboardview.EmotiomComplateFragment;
import com.example.fishingport.app.view.emotionkeyboardview.EmotionKeyboard;
import com.example.fishingport.app.view.emotionkeyboardview.NoHorizontalScrollerViewPager;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.animation.AnimationType;
import com.google.gson.Gson;

import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.luck.picture.lib.model.PictureConfig.resultCallback;

/**
 * Created by wushixin on 2017/4/26.
 * 发布动态
 */

public class ReleaseDynamicsActivity extends BaseAppCompatActivity implements FishingCircleConstract.view, UserConstract.view {
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.img_picture)
    ImageView img_picture;
    @BindView(R.id.content)
    View content;
    @BindView(R.id.img_noun_expressio)
    ImageView imgNounExpressio;
    @BindView(R.id.layout)
    RelativeLayout layout;
    private Dialog mWeiboDialog;
    @BindView(R.id.vp_emotionview_layout)
    NoHorizontalScrollerViewPager vpEmotionviewLayout;
    @BindView(R.id.ll_emotion_layout)
    LinearLayout llEmotionLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.upload_trajectory)
    LinearLayout upload_trajectory;//上传轨迹按钮
    @BindView(R.id.addlist)
    RecyclerView readdlist;//上传轨迹的承载的list;
    @BindView(R.id.btn_txt)
    TextView btn_txt;
    private BaseRecyclerAdapter<TrajectoryList.ResultBean.DataBean> basereaddlist;//上传轨迹承载的list
    private boolean isBindToBarEditText = true;
    List<Fragment> fragments = new ArrayList<>();
    private int CurrentPosition = 0;
    private BaseRecyclerAdapter<ImageReleaseDynamics> mAdapter;
    private int REQUEST_IMAGE = 1;
    List<ImageReleaseDynamics> listImage = new ArrayList<>();
   // private List<String> path = new ArrayList<>();
   // private ArrayList<ImageItem> images=new ArrayList<>();
    private FishingCirclePresenter fishingCirclePresenter;
    private String json;
    JSONArray accountArray;
    private UserPresenter userPresenter;
    private int iType, loadtype;
    List<String> imgList = new ArrayList<>();
    private List<LocalMedia> selectMedia = new ArrayList<>();
    private String trajectory_id="";//轨迹id
    private String imgs="";//图片
    private String mcontent="";//文字内容

    int i = 0;
    Gson gson = new Gson();
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    final JSONArray accountArray = new JSONArray();
                    for (int i = 0; i < imgList.size(); i++) {
                        accountArray.put(imgList.get(i));
                    }
                    Log.i("", accountArray.toString());
                    imgs=accountArray.toString();

                    final Location gps = LocationhelpUtils.getGPSLocation(ReleaseDynamicsActivity.this);
                    if (gps == null) {
                        final LocationClient mLocClient;
                        mLocClient = new LocationClient(ReleaseDynamicsActivity.this);
                        mLocClient.registerLocationListener(new BDLocationListener() {
                            @Override
                            public void onReceiveLocation(BDLocation bdLocation) {
                                // map view 销毁后不在处理新接收的位置
                                if (bdLocation == null) {
                                    return;
                                }
                                double[] dd = HelpUtils.gcj02towgs84(bdLocation.getLongitude(), bdLocation.getLatitude());
                                Log.i("百度gps定位", dd[1] + "/" + dd[0]);
                                initFishingCircle(trajectory_id, dd[1] + "," + dd[0],imgs, editText.getText().toString());
                                mLocClient.stop();
                            }
                            @Override
                            public void onConnectHotSpotMessage(String s, int i) {
                            }
                        });
                        LocationClientOption option = new LocationClientOption();
                        option.setOpenGps(true); // 打开gps
                        option.setCoorType("gcj02"); // 设置坐标类型
                        option.setScanSpan(0);
                        mLocClient.setLocOption(option);
                        mLocClient.start();
                    } else {
                        initFishingCircle(trajectory_id, gps.getLatitude() + "," + gps.getLongitude()
                                ,imgs, editText.getText().toString());
                    }

                    break;
            }
            super.handleMessage(msg);
        }
    };


    //表情面板
    private EmotionKeyboard mEmotionKeyboard;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_releasedynamics;
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
        setToolBarTitle("发布动态");
        initUpload();//发布渔友圈
        initEmotion();
        initDatas();
        ImageReleaseDynamics imageReleaseDynamics = new ImageReleaseDynamics();
       // imageReleaseDynamics.setIcon(R.mipmap.add_member_grpup_iocn);
        imageReleaseDynamics.setIcon(R.mipmap.addimg);

        //

        img_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageimg();
            }
        });
        fishingCirclePresenter = new FishingCirclePresenter(this);
        mAdapter = new BaseRecyclerAdapter<ImageReleaseDynamics>
                (this, null, R.layout.item_imagereleasedynamics) {
            @Override
            protected void convert(final BaseViewHolder helper, final ImageReleaseDynamics item) {
                final ImageView image = (ImageView) helper.getConvertView().findViewById(R.id.image);
                if (selectMedia.size() < 9) {
                    if (helper.getAdapterPosition() == listImage.size() - 1) {
                        Glide.with(mContext).load(item.getIcon()).into(image);
                        helper.setOnClickListener(R.id.content, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pageimg();
                            }
                        });
                    } else {
                        Glide.with(mContext).load(item.getImage()).into(image);
                    }
                } else {
                    Glide.with(mContext).load(item.getImage()).into(image);
                }
            }
        };
        GridLayoutManager mgr = new GridLayoutManager(this, 5);
        mRecyclerView.setLayoutManager(mgr);
        mAdapter.openLoadAnimation(AnimationType.ALPHA);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(listImage);


    }

    private void pageimg() {
        FunctionOptions optionss = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE)
                .setCompress(true)//是否压缩
                .setGrade(Luban.THIRD_GEAR)
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(9)
                .setThemeStyle(ContextCompat.getColor(ReleaseDynamicsActivity.this, R.color.bule))
                .setSelectMedia(selectMedia) // 已选图片，传入在次进去可选中，不能传入网络图片
               // .setStatusBar(R.drawable.bg_gradient_color)//状态栏颜色
                .setImageSpanCount(3) // 每行个数
                .create();
        // PictureConfig.getInstance().init(optionss);
        PictureConfig.getInstance().init(optionss).openPhoto(ReleaseDynamicsActivity.this,
              new PictureConfig.OnSelectResultCallback() {
                  @Override
                  public void onSelectSuccess(List<LocalMedia> list) {
                      listImage.clear();
                      selectMedia = list;
                      Log.i("callBack_result", list.size() + "/"+list.get(0).getPath());
                      for (int i = 0; i < list.size(); i++) {
                          ImageReleaseDynamics imageReleaseDynamics = new ImageReleaseDynamics();
                          imageReleaseDynamics.setImage(list.get(i).getPath());
                          listImage.add(imageReleaseDynamics);
                      }
                      if (listImage.size() < 9) {
                          ImageReleaseDynamics imageReleaseDynamicss = new ImageReleaseDynamics();
                          imageReleaseDynamicss.setIcon(R.mipmap.addimg);
                          listImage.add(imageReleaseDynamicss);
                      }
                      mAdapter.notifyDataSetChanged();
                  }
                  @Override
                  public void onSelectSuccess(LocalMedia localMedia) {

                  }
              });
    }


    private void initUpload() {
        setTextRight("发布").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectMedia.size() > 0) {
                    Bitmap bitmap = HelpUtils.compressBitmap(selectMedia.get(0).getPath(), 600, 1024, 30);
                    initAddAppraise("fishing_circle", "1", Utils.bitmapToBase64(bitmap));
                } else {
                    Location gps = LocationhelpUtils.getGPSLocation(ReleaseDynamicsActivity.this);
                    if (gps == null) {
                        final LocationClient mLocClient;
                        mLocClient = new LocationClient(ReleaseDynamicsActivity.this);
                        mLocClient.registerLocationListener(new BDLocationListener() {
                            @Override
                            public void onReceiveLocation(BDLocation bdLocation) {
                                // map view 销毁后不在处理新接收的位置
                                if (bdLocation == null) {

                                    return;
                                }
                                double[] dd = HelpUtils.gcj02towgs84(bdLocation.getLongitude(), bdLocation.getLatitude());
                                Log.i("百度gps定位", dd[1] + "/" + dd[0]);
                                initFishingCircle(trajectory_id, dd[1] + "," + dd[0], imgs, editText.getText().toString());
                                mLocClient.stop();
                            }
                            @Override
                            public void onConnectHotSpotMessage(String s, int i) {
                            }
                        });
                        LocationClientOption option = new LocationClientOption();
                        option.setOpenGps(true); // 打开gps
                        option.setCoorType("gcj02"); // 设置坐标类型
                        option.setScanSpan(0);
                        mLocClient.setLocOption(option);
                        mLocClient.start();
                    } else {
                        Log.i("百度gps定位===", gps + "");
                        initFishingCircle(trajectory_id, gps.getLatitude() + "," + gps.getLongitude(), imgs, editText.getText().toString());
                    }
                }
            }
        });

        //点击上传轨迹调到轨迹页面选择轨迹
        upload_trajectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //上传轨迹
                Intent intent=new Intent(ReleaseDynamicsActivity.this,TarckActivity.class);
                intent.putExtra("type","upload");//
                startActivityForResult(intent,1);
            }
        });
    }


    /**
     * 初始化表情面板
     */
    public void initEmotion() {
        mEmotionKeyboard = EmotionKeyboard.with(this)
                .setEmotionView(findViewById(R.id.ll_emotion_layout))//绑定表情面板
                .bindToContent(content)//绑定内容view
                .bindToEditText(!isBindToBarEditText ? ((EditText) content) : ((EditText) findViewById(R.id.editText)))//判断绑定那种EditView
                .bindToEmotionButton(findViewById(R.id.img_noun_expressio))//绑定表情按钮
                .build();
        //创建全局监听
        GlobalOnItemClickManagerUtils globalOnItemClickManager = GlobalOnItemClickManagerUtils.getInstance(this);
        if (isBindToBarEditText) {
            //绑定当前Bar的编辑框
            globalOnItemClickManager.attachToEditText(editText);
        } else {
            // false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
            globalOnItemClickManager.attachToEditText((EditText) content);
            mEmotionKeyboard.bindToEditText((EditText) content);
        }
    }

    /**
     * 数据操作,这里是测试数据，请自行更换数据
     */
    protected void initDatas() {
        replaceFragment();
        List<ImageModel> list = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            if (i == 0) {
                ImageModel model1 = new ImageModel();
                model1.icon = getResources().getDrawable(R.mipmap.sos_icon);
                model1.flag = "经典笑脸";
                model1.isSelected = true;
                list.add(model1);
            } else {
                ImageModel model = new ImageModel();
                model.icon = getResources().getDrawable(R.mipmap.sos_icon);
                model.flag = "其他笑脸" + i;
                model.isSelected = false;
                list.add(model);
            }
        }
        //记录底部默认选中第一个
        CurrentPosition = 0;
        // SharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);
    }

    private void replaceFragment() {
        //创建fragment的工厂类
        FragmentFactory factory = FragmentFactory.getSingleFactoryInstance();
        //创建修改实例
        EmotiomComplateFragment f1 = (EmotiomComplateFragment) factory.getFragment(EmotionUtils.EMOTION_CLASSIC_TYPE);
        fragments.add(f1);
        Bundle b = null;
        for (int i = 0; i < 1; i++) {
            b = new Bundle();
            b.putString("Interge", "Fragment-" + i);
            Fragment1 fg = Fragment1.newInstance(Fragment1.class, b);
            fragments.add(fg);
        }

        NoHorizontalScrollerVPAdapter adapter = new NoHorizontalScrollerVPAdapter(getSupportFragmentManager(), fragments);
        vpEmotionviewLayout.setAdapter(adapter);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            if (requestCode==1&&resultCode==1){
              List<TrajectoryList.ResultBean.DataBean> list= (List<TrajectoryList.ResultBean.DataBean>) data.getSerializableExtra("list");

                if (list!=null){
                    if (list.size()>0){
                basereaddlist=new BaseRecyclerAdapter<TrajectoryList.ResultBean.DataBean>
                        (ReleaseDynamicsActivity.this,null, R.layout.item_trackview) {
                    @Override
                    protected void convert(BaseViewHolder helper, TrajectoryList.ResultBean.DataBean item) {
                        ImageView imageView= (ImageView) helper.getConvertView().findViewById(R.id.img);
                        RelativeLayout itemview= (RelativeLayout) helper.getConvertView().findViewById(R.id.track_item);
                         itemview.setBackgroundColor(Color.parseColor("#F7FAFF"));
                        trajectory_id=item.getId()+"";
                        helper.setText(R.id.time,item.getAdd_time() +"");//添加时间
                        helper.setText(R.id.saling,item.getTime_count()+"");//航行时间
                        helper.setText(R.id.total_long,item.getDistance_count()+"");//总距离
                        helper.setText(R.id.didian,item.getStart_position());
                        helper.setText(R.id.qidian,item.getStart_position());
                        helper.setText(R.id.zhongdian,item.getStop_position());
                        Glide.with(ReleaseDynamicsActivity.this).load(item.getImg()).error(R.mipmap.ic_launcher).into(imageView);
                    }
                };
                readdlist.setLayoutManager(new LinearLayoutManager(this));
                basereaddlist.openLoadAnimation(true);
                readdlist.setAdapter(basereaddlist);
                upload_trajectory.setVisibility(View.GONE);
                basereaddlist.setData(list);

                }else {

                }
            }
        }
        }
    }

    @Override
    public void showInfo(String string) {
        Log.i("选择的图片地址=", string);
        WeiboDialogUtils.closeDialog(mWeiboDialog);
        try {
            JSONObject jsonObject = new JSONObject(string);
            if (jsonObject.getJSONObject("status").getString("code").equals("1000")) {
                if (loadtype == 1) {
                    imgList.add(jsonObject.getJSONObject("result").getString("path_all"));
                    i++;
                    if (i < selectMedia.size()) {
                        Bitmap bitmap = HelpUtils.compressBitmap(selectMedia.get(i).getPath(), 600, 1024, 30);
                        initAddAppraise("fishing_circle", "1", Utils.bitmapToBase64(bitmap));
                    } else if (i == selectMedia.size()) {
                        Message msg = myHandler.obtainMessage();
                        msg.what = 1;
                        myHandler.sendMessage(msg);
                    }
                } else {
                    Intent intent = new Intent();
                    intent.setAction("fishingcircle");
                    intent.putExtra("id", jsonObject.getJSONObject("result").getString("fishing_circle_id"));  // 要发送的内容
                    sendBroadcast(intent);
                    finish();
                }
                // Glide.with(this).load(jsonObject.getJSONObject("result").getString("path_all")).transform(new GlideCircleTransform(this)).into(imgAvatar);
            }else {
                HelpUtils.warning(ReleaseDynamicsActivity.this,jsonObject.getJSONObject("status").getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showendcircle_img(String string) {

    }

    @Override
    public void showsign(String s) {

    }

    @Override
    public void showErrMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 发布动态
     */
    private void initFishingCircle(String trajectory_id, String position_latlng,
                                   String imgs, String content) {
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ReleaseDynamicsActivity.this, "加载中...");

        loadtype = 2;
        Log.i("参数",trajectory_id+"/"+position_latlng+"/"+imgs+"/"+content);

        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        encryptapMap.put("position_latlng", position_latlng);
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("trajectory_id", trajectory_id);
        map.put("position_latlng", position_latlng);
        map.put("imgs", imgs);
        map.put("content", content);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        fishingCirclePresenter.loadFishingCircle(map);

    }

    /**
     * 上传图片（单张）
     */
    private void initAddAppraise(String res_name, String type, String image) {
        loadtype = 1;
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        // encryptapMap.put("uid",uid);

        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("res_name", res_name);
        map.put("type", type);
        map.put("image", image);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        userPresenter.loadAddAppraise(map);
    }


}
