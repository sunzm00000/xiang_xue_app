package com.example.fishingport.app.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.FishingCircleConstract;
import com.example.fishingport.app.api.HomeConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.presenter.FishingCirclePresenter;
import com.example.fishingport.app.presenter.HomePresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.KeyBoardHelper;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;
import com.example.fishingport.app.tools.WeiboDialogUtils;
import com.example.fishingport.app.widget.DensityUtil;
import com.example.fishingport.app.widget.FullScreenVideoView;
import com.example.fishingport.app.widget.LightnessController;
import com.example.fishingport.app.widget.VolumnController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrainingDetails extends BaseAppCompatActivity implements
        HomeConstract.view, FishingCircleConstract.view ,KeyBoardHelper.OnKeyBoardStatusChangeListener{
   @BindView(R.id.webview)
    WebView webView;//培训
    private  String url="";
    // 自定义VideoView
    @BindView(R.id.videoview)
    FullScreenVideoView mVideo;//视频播放窗口
    @BindView(R.id.play_time)
    TextView mPlayTime;//播放进度时间
    @BindView(R.id.play_btn)
    ImageView play_btn;//暂停播放按钮
    @BindView(R.id.seekbar)
    SeekBar mSeekBar;//进度条
    private String videoUrl="";
    @BindView(R.id.top_layout)
    View mTopView;
    @BindView(R.id.bottom_layout)
    View mBottomView;
    // 自动隐藏顶部和底部View的时间
    private static final int HIDE_TIME = 5000;
    private int playTime;
    @BindView(R.id.total_time)
     TextView mDurationTime; //播放的总时间
    private InputMethodManager imm;
    private String res_id,parent_id="0";
    @BindView(R.id.pinglun)
    LinearLayout pinglun;//默认隐藏
    @BindView(R.id.edit_comment)
    EditText editComment;
    private  int edcomment=0;
    @BindView(R.id.noping)
    RelativeLayout noping;//
    private HomePresenter homePresenter;
    private FishingCirclePresenter friendPresenter;
    @BindView(R.id.disuss)
    ImageView disuss;//
    @BindView(R.id.zan)
    ImageView appraise;//点赞按钮
    private  String appraise_num;//点赞数
    private  String comment_num;//评论数
    @BindView(R.id.discussnember)
    TextView discussnember;
    @BindView(R.id.love_num)
    TextView love_num;
    private Dialog mWeiboDialog;
    private String  is_appraise;//是否点赞
    @BindView(R.id.layout_video)
    RelativeLayout layout_video;//视频view
    private String type="-1";
    private KeyBoardHelper boardHelper;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    pinglun.setVisibility(View.VISIBLE);
                    noping.setVisibility(View.GONE);
                    editComment.setFocusable(true);
                    editComment.setFocusableInTouchMode(true);
                    editComment.requestFocus();
                    imm.showSoftInput(editComment, 0);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    // 音频管理器
    private AudioManager mAudioManager;
    // 声音调节Toast
    private VolumnController volumnController;
    // 屏幕宽高
    private float width;
    private float height;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mVideo.getCurrentPosition() > 0) {
                        mPlayTime.setText(formatTime(mVideo.getCurrentPosition()));
                        int progress = mVideo.getCurrentPosition() * 100 / mVideo.getDuration();
                        mSeekBar.setProgress(progress);
                        if (mVideo.getCurrentPosition() > mVideo.getDuration() - 100) {
                            mPlayTime.setText("00:00");
                            mSeekBar.setProgress(0);
                        }
                        mSeekBar.setSecondaryProgress(mVideo.getBufferPercentage());
                    } else {
                        mPlayTime.setText("00:00");
                        mSeekBar.setProgress(0);
                    }
                    break;
                case 2:
                    showOrHide();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBarTitle("知识培训");
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        homePresenter=new HomePresenter(this);
        friendPresenter = new FishingCirclePresenter(this);
        boardHelper = new KeyBoardHelper(this);
        boardHelper.onCreate();
        boardHelper.setOnKeyBoardStatusChangeListener(this);

        url=getIntent().getStringExtra("url");
        intiview ();
        initVedio();//视频
        initwebview();//html5页面
    }

    private void intiview() {
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        volumnController = new VolumnController(this);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        width = DensityUtil.getWidthInPx(this);
        height = DensityUtil.getHeightInPx(this);
        threshold = DensityUtil.dip2px(this, 18);
        appraise_num=getIntent().getStringExtra("appraise");//点赞数
        comment_num=getIntent().getStringExtra("comment_num");//评论数
        is_appraise=getIntent().getStringExtra("is_appraise");
        detailnum(SharedPreferencedUtils.getString(TrainingDetails.this,"token"),"train",getUrlParams(url).get("id"));
    }

    private void initVedio() {
        videoUrl=getIntent().getStringExtra("path");
        Log.i("视频",videoUrl);
        mVideo.setVideoPath(videoUrl);
        mVideo.requestFocus();
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideo.setVideoWidth(mp.getVideoWidth());
                mVideo.setVideoHeight(mp.getVideoHeight());
                mVideo.start();
                try {
                    Thread.sleep(500);
                    mVideo.setBackgroundColor(Color.parseColor("#00000000"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if (playTime != 0) {

                    mVideo.seekTo(playTime);
                }
                mHandler.removeCallbacks(hideRunnable);
                mHandler.postDelayed(hideRunnable, HIDE_TIME);
                mDurationTime.setText(formatTime(mVideo.getDuration()));
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(1);
                    }
                }, 0, 1000);

            }
        });


        mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play_btn.setImageResource(R.mipmap.video_btn_down);
                mPlayTime.setText("00:00");
                mSeekBar.setProgress(0);
            }
        });
        mVideo.setOnTouchListener(mTouchListener);
    }
    private void initwebview() {
        Log.i("知识培训",url+"");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new TrainingDetails.JsToJava(), "stub");
        webView.loadUrl(url+"&fromApp=1");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                // view.loadUrl(url);
                Log.e("url",url);
                return true;
            }
        });
        editComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEND
                        ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
                    edcomment=2;
                    Log.i("发送评论",getUrlParams(url).get("id")+"/"+parent_id+"/"+url);
                    initCommnetAdd(getUrlParams(url).get("id"),parent_id,editComment.getText().toString());
                    return true;
                }

                return false;
            }
        });



    }

    @Override
    public void OnKeyBoardPop(int keyBoardheight) {

    }

    @Override
    public void OnKeyBoardClose(int oldKeyBoardheight) {
        pinglun.setVisibility(View.GONE);
        noping.setVisibility(View.VISIBLE);
    }

    private class JsToJava {
        @JavascriptInterface
        public void jsMethod(String p) {
            Log.i("返回的结果", p);
            Message msg = myHandler.obtainMessage();
            msg.what = 1;
            myHandler.sendMessage(msg);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            System.out.println("js返回结果" +p);//处理返回的结果
            parent_id=p;
        }

    }
  @OnClick({R.id.play_btn,R.id.disuss,R.id.zan})
  public  void  onclick(View v){
      switch (v.getId()){
          case R.id.play_btn://暂停播放按钮
              if (mVideo.isPlaying()) {
                  mVideo.pause();
                  play_btn.setImageResource(R.mipmap.video_btn_down);
              } else {
                  mVideo.start();
                  play_btn.setImageResource(R.mipmap.video_btn_on);
              }
              break;
          case R.id.disuss:
              InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
              pinglun.setVisibility(View.VISIBLE);
              noping.setVisibility(View.GONE);
              editComment.setFocusable(true);
              editComment.setFocusableInTouchMode(true);
              editComment.requestFocus();
              imm.showSoftInput(editComment, 0);
              break;

          case R.id.zan://
              add_appraise(SharedPreferencedUtils.getString(TrainingDetails.this,"token"),getUrlParams(url).get("id"));
              break;
      }

  }
    @SuppressLint("SimpleDateFormat")
    private String formatTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }
    @Override
    protected int getLayoutId() {
        if (getIntent().getStringExtra("type")!=null){
            type=getIntent().getStringExtra("type");
            Log.i("type打印==",type+"");
            if (type.equals("1")){
                return R.layout.activity_training_novidieo;
            }else if (type.equals("2")){
                return R.layout.activity_training_details;
            }
        }else {
            return R.layout.activity_training_details;
        }
        return R.layout.activity_training_details;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
        mHandler.removeCallbacksAndMessages(null);
    }

    private Runnable hideRunnable = new Runnable() {

        @Override
        public void run() {
            showOrHide();
        }
    };
    private void showOrHide() {
        if (mTopView.getVisibility() == View.VISIBLE) {
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.option_leave_from_top);
            animation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mTopView.setVisibility(View.GONE);
                }
            });
            mTopView.startAnimation(animation);

            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.option_leave_from_bottom);
            animation1.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mBottomView.setVisibility(View.GONE);
                }
            });
            mBottomView.startAnimation(animation1);
        } else {
            mTopView.setVisibility(View.VISIBLE);
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.option_entry_from_top);
            mTopView.startAnimation(animation);

            mBottomView.setVisibility(View.VISIBLE);
            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.option_entry_from_bottom);
            mBottomView.startAnimation(animation1);
            mHandler.removeCallbacks(hideRunnable);
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }
    }
    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandler.removeCallbacks(hideRunnable);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                int time = progress * mVideo.getDuration() / 100;
                mVideo.seekTo(time);
            }
        }
    };

    @Override
    public void showErrMsg(String msg) {

    }

    @Override
    public void showInfo(String string) {
        WeiboDialogUtils.closeDialog(mWeiboDialog);
        if (edcomment==2){
            edcomment=0;
            Log.e("gg评论",string);
            //pinglun.setVisibility(View.GONE);
            // noping.setVisibility(View.VISIBLE);
            try {
                JSONObject jsonObject=new JSONObject(string);
                if (jsonObject.getJSONObject("status").getInt("code")==1000){
                    editComment.setText("");
                    imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0); //强制隐藏键盘
                    pinglun.setVisibility(View.GONE);
                    noping.setVisibility(View.VISIBLE);
                    parent_id="";
                    String comment_id=jsonObject.getJSONObject("result").getString("comment_id");

                    webView.loadUrl("javascript:commentSuccess('"+comment_id+"')");
                } else {
                    Toast.makeText(this, jsonObject.getJSONObject("status").getInt("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            try {
                JSONObject object=new JSONObject(string);
                JSONObject status=object.optJSONObject("status");
                JSONObject result=object.optJSONObject("result");
                if (status.optInt("code")==1000){
                    comment_num=Integer.valueOf(result.optString("comment_num"))+"";
                    discussnember.setText(comment_num+"  评论");
                    appraise_num=Integer.valueOf(result.optString("appraise_num"))+"";
                    love_num.setText(appraise_num+"  喜欢");
                    is_appraise=result.optString("is_appraise");
                    if (is_appraise.equals("1")){
                        int width=HelpUtils.dip2px(TrainingDetails.this,20);
                        ViewGroup.LayoutParams params =appraise.getLayoutParams();
                        params.height=width;
                        params.width =width;
                        appraise.setLayoutParams(params);
                        appraise.setImageDrawable(getResources().getDrawable(R.mipmap.iszan));
                    }else {
                        //没有点赞
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showendcircle_img(String string) {

    }

    @Override
    public void showcityid(String ss) {
        Log.i("点赞",ss);
        WeiboDialogUtils.closeDialog(mWeiboDialog);
        try {
            JSONObject object=new JSONObject(ss);
            JSONObject status=object.optJSONObject("status");
            JSONObject result=object.optJSONObject("result");
            if (status.optInt("code")==1000){
                if (result.optInt("is_del")==0){
                    HelpUtils.success(TrainingDetails.this,"点赞成功");
                    int width=HelpUtils.dip2px(TrainingDetails.this,20);
                    ViewGroup.LayoutParams params =appraise.getLayoutParams();
                    params.height=width;
                    params.width =width;
                    appraise.setLayoutParams(params);
                    appraise.setImageDrawable(getResources().getDrawable(R.mipmap.iszan));
                    appraise_num=Integer.valueOf(appraise_num)+1+"";
                    love_num.setText(appraise_num+"  喜欢");
                }else {
                    HelpUtils.success(TrainingDetails.this,"取消点赞");
                    int width=HelpUtils.dip2px(TrainingDetails.this,20);
                    ViewGroup.LayoutParams params =appraise.getLayoutParams();
                    params.height=width;
                    params.width =width;
                    appraise.setLayoutParams(params);
                    appraise.setImageDrawable(getResources().getDrawable(R.mipmap.shape));
                    appraise_num=Integer.valueOf(appraise_num)-1+"";
                    love_num.setText(appraise_num+"  喜欢");
                }
            }else {
                HelpUtils.warning(TrainingDetails.this,status.optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private class AnimationImp implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

    }




    private float mLastMotionX;
    private float mLastMotionY;
    private int startX;
    private int startY;
    private int threshold;
    private boolean isClick = true;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final float x = event.getX();
            final float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    mLastMotionY = y;
                    startX = (int) x;
                    startY = (int) y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - mLastMotionX;
                    float deltaY = y - mLastMotionY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    // 声音调节标识
                    boolean isAdjustAudio = false;
                    if (absDeltaX > threshold && absDeltaY > threshold) {
                        if (absDeltaX < absDeltaY) {
                            isAdjustAudio = true;
                        } else {
                            isAdjustAudio = false;
                        }
                    } else if (absDeltaX < threshold && absDeltaY > threshold) {
                        isAdjustAudio = true;
                    } else if (absDeltaX > threshold && absDeltaY < threshold) {
                        isAdjustAudio = false;
                    } else {
                        return true;
                    }
                    if (isAdjustAudio) {
                        if (x < width / 2) {
                            if (deltaY > 0) {
                                lightDown(absDeltaY);
                            } else if (deltaY < 0) {
                                lightUp(absDeltaY);
                            }
                        } else {
                            if (deltaY > 0) {
                                volumeDown(absDeltaY);
                            } else if (deltaY < 0) {
                                volumeUp(absDeltaY);
                            }
                        }

                    } else {
                        if (deltaX > 0) {
                            forward(absDeltaX);
                        } else if (deltaX < 0) {
                            backward(absDeltaX);
                        }
                    }
                    mLastMotionX = x;
                    mLastMotionY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(x - startX) > threshold || Math.abs(y - startY) > threshold) {
                        isClick = false;
                    }
                    mLastMotionX = 0;
                    mLastMotionY = 0;
                    startX = (int) 0;
                    if (isClick) {
                        showOrHide();
                    }
                    isClick = true;
                    break;

                default:
                    break;
            }
            return true;
        }

    };

    private void backward(float delataX) {
        int current = mVideo.getCurrentPosition();
        int backwardTime = (int) (delataX / width * mVideo.getDuration());
        int currentTime = current - backwardTime;
        mVideo.seekTo(currentTime);
        mSeekBar.setProgress(currentTime * 100 / mVideo.getDuration());
        mPlayTime.setText(formatTime(currentTime));
    }

    private void forward(float delataX) {
        int current = mVideo.getCurrentPosition();
        int forwardTime = (int) (delataX / width * mVideo.getDuration());
        int currentTime = current + forwardTime;
        mVideo.seekTo(currentTime);
        mSeekBar.setProgress(currentTime * 100 / mVideo.getDuration());
        mPlayTime.setText(formatTime(currentTime));
    }

    private void volumeDown(float delatY) {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int down = (int) (delatY / height * max * 3);
        int volume = Math.max(current - down, 0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int transformatVolume = volume * 100 / max;
        volumnController.show(transformatVolume);
    }

    private void volumeUp(float delatY) {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int up = (int) ((delatY / height) * max * 3);
        int volume = Math.min(current + up, max);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int transformatVolume = volume * 100 / max;
        volumnController.show(transformatVolume);
    }

    private void lightDown(float delatY) {
        int down = (int) (delatY / height * 255 * 3);
        int transformatLight = LightnessController.getLightness(this) - down;
        LightnessController.setLightness(this, transformatLight);
    }

    private void lightUp(float delatY) {
        int up = (int) (delatY / height * 255 * 3);
        int transformatLight = LightnessController.getLightness(this) + up;
        LightnessController.setLightness(this, transformatLight);
    }






    ///


    /**
     * 发评论
     */
    private void initCommnetAdd(String res_id, String parent_id, String content) {
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(TrainingDetails.this, "加载中...");

        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        encryptapMap.put("res_name", "train");
        encryptapMap.put("res_id", res_id);
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("parent_id", parent_id);
        map.put("content", content);
        map.put("to_user_id","0");
        map.put("res_name", "train");
        map.put("res_id", res_id);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        friendPresenter.loadCommentAdd(map);
    }
    /**
     * 截取url
     */
    private HashMap<String,String> getUrlParams(String url){
        int index=url.indexOf("?");
        String argStr=url.substring(index+1);
        String[] argAry=argStr.split("&");
        HashMap<String ,String >  hashMap=new HashMap<>();
        for (String arg:argAry){
            String[] argAryT=arg.split("=");
            hashMap.put(argAryT[0],argAryT[1]);
        }
        return  hashMap;
    }

    /*
    * 点赞
    * */
    private  void  add_appraise(String token,
                                String res_id){
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(TrainingDetails.this, "加载中...");

        Map<String,String> signmap=Utils.getSignParams(TrainingDetails.this,token);
        signmap.put("res_name","train");
        signmap.put("res_id",res_id);
        Map<String,String> map=Utils.getMap(TrainingDetails.this,token);
        map.put("res_name","train");
        map.put("res_id",res_id);
        map.put("sign",Utils.getMd5StringMap(signmap));
        homePresenter.add_appraise(map);
    }
    /*
* 点赞数，评论数
* */
    private void  detailnum(String token,String res_name,String res_id){
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(TrainingDetails.this, "加载中...");

        edcomment=3;
        Map<String,String> signmap=Utils.getSignParams(TrainingDetails.this,token);
        signmap.put("res_name",res_name);
        signmap.put("res_id",res_id);
        Map<String,String> map=Utils.getMap(TrainingDetails.this,token);
        map.put("res_name",res_name);
        map.put("res_id",res_id);
        map.put("sign",Utils.getMd5StringMap(signmap));
        homePresenter.detailnum(map);
    }



}
