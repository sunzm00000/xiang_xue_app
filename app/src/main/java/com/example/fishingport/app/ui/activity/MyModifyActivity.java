package com.example.fishingport.app.ui.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingport.app.R;
import com.example.fishingport.app.api.UserConstract;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.example.fishingport.app.presenter.UserPresenter;
import com.example.fishingport.app.tools.HelpUtils;
import com.example.fishingport.app.tools.SharedPreferencedUtils;
import com.example.fishingport.app.tools.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wushixin on 2017/4/28.
 * 个人资料--编辑
 */

public class MyModifyActivity extends BaseAppCompatActivity implements UserConstract.view {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_modify_title)
    TextView txtTitle;
    @BindView(R.id.edit_title)
    EditText editTitle;
    UserPresenter userPresenter;
    @BindView(R.id.btn_nan)
    RadioButton btnNan;
    @BindView(R.id.btn_nv)
    RadioButton btnNv;
    @BindView(R.id.radio_gender)
    RadioGroup radioGender;
    @BindView(R.id.txt_birthday)
    TextView txtBirthday;
    @BindView(R.id.layout)
    LinearLayout layout;
    private int genderType;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_modify;
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
        setTextRight("完成").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getIntExtra("type", -1) == 0) {
                   initEditField("nick_name",editTitle.getText().toString());
                } else  if (getIntent().getIntExtra("type", -1) == 1){
                    initEditField("gender",genderType+"");
                } else  if (getIntent().getIntExtra("type", -1) == 2){
                    initEditField("birthday",txtBirthday.getText().toString());
                }else  if (getIntent().getIntExtra("type", -1) == 3){
                    initEditField("city",editTitle.getText().toString());
                }else  if (getIntent().getIntExtra("type", -1) == 4){
                    initEditField("intro",editTitle.getText().toString());
                }else  if (getIntent().getIntExtra("type", -1) == 5){
                    initEditField("user_name",editTitle.getText().toString());
                }else  if (getIntent().getIntExtra("type", -1) == 6){
                    initEditField("id_card",editTitle.getText().toString());
                }else  if (getIntent().getIntExtra("type", -1) == 7){
                    initEditField("mobile",editTitle.getText().toString());
                }else  if (getIntent().getIntExtra("type", -1) == 8){
                    Pattern pattern = Pattern.compile("" +
                            "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(editTitle.getText().toString());
                    if (matcher.matches()){
                        initEditField("email",editTitle.getText().toString());
                    } else {
                        Toast.makeText(MyModifyActivity.this, "请正确输入邮箱", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        if (getIntent().getIntExtra("type", -1) == 0) {
            setToolBarTitle("修改昵称");
            layout.setVisibility(View.VISIBLE);
            txtTitle.setText("昵称");
            editTitle.setHint("请输入昵称");
            editTitle.setText(getIntent().getStringExtra("text"));

        } else if (getIntent().getIntExtra("type", -1) == 1){
            setToolBarTitle("修改性别");
            radioGender.setVisibility(View.VISIBLE);
//            txtTitle.setText("群介绍");
//            editTitle.setHint("群介绍");
//            editTitle.setLines(10);
        }else if (getIntent().getIntExtra("type", -1) == 2){
            setToolBarTitle("修改生日");
            txtBirthday.setVisibility(View.VISIBLE);
            txtBirthday.setText(getIntent().getStringExtra("text"));

        }else if (getIntent().getIntExtra("type", -1) == 3){
            setToolBarTitle("修改地区");
            layout.setVisibility(View.VISIBLE);
            editTitle.setHint("请输入地区");
            txtTitle.setText("地区");
            editTitle.setText(getIntent().getStringExtra("text"));

        }else if (getIntent().getIntExtra("type", -1) == 4){
            setToolBarTitle("修改个人简介");
            layout.setVisibility(View.VISIBLE);
            txtTitle.setText("个人简介");
            editTitle.setHint("请输入个人简介");
            editTitle.setLines(10);
            editTitle.setText(getIntent().getStringExtra("text"));

        } else if (getIntent().getIntExtra("type", -1) == 5){
            setToolBarTitle("修改真实姓名");
            layout.setVisibility(View.VISIBLE);
            txtTitle.setText("真实姓名");
            editTitle.setHint("请输入真实姓名");
            editTitle.setText(getIntent().getStringExtra("text"));

        }else if (getIntent().getIntExtra("type", -1) == 6){
            setToolBarTitle("修改身份证");
            layout.setVisibility(View.VISIBLE);
            txtTitle.setText("身份证");
            editTitle.setHint("请输入身份证");
            editTitle.setText(getIntent().getStringExtra("text"));

        }else if (getIntent().getIntExtra("type", -1) == 7){
            setToolBarTitle("修改手机号");
            layout.setVisibility(View.VISIBLE);
            txtTitle.setText("手机号");
            editTitle.setHint("请输入手机号");
            editTitle.setText(getIntent().getStringExtra("text"));

        }else if (getIntent().getIntExtra("type", -1) == 8){
            setToolBarTitle("修改邮箱");
            layout.setVisibility(View.VISIBLE);
            txtTitle.setText("邮箱");
            editTitle.setHint("请输入邮箱");
            editTitle.setText(getIntent().getStringExtra("text"));

        }


        txtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(txtBirthday);
            }
        });

        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btn_nan:
                         genderType=1;
                        break;
                    case R.id.btn_nv:
                         genderType=2;
                        break;


                }
            }
        });

    }

    @Override
    public void showInfo(String string) {
        Log.e("string",string);
        try {
            JSONObject jsonObject = new JSONObject(string);
            if (jsonObject.getJSONObject("status").getInt("code") == 1000) {
                Intent intent=new Intent();
                setResult(100,intent);
                finish();
            }else {
                HelpUtils.warning(MyModifyActivity.this,""+jsonObject.optJSONObject("status").optString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showsign(String s) {

    }

    @Override
    public void showErrMsg(String msg) {
        HelpUtils.error(MyModifyActivity.this,"修改信息失败!");
    }

    /**
     * 修改个人信息
     */
    private void initEditField(String field,String field_value) {
        Map<String, String> encryptapMap = Utils.getSignParams(this, SharedPreferencedUtils.getString(this, "token"));
        Map<String, String> map = Utils.getMap(this, SharedPreferencedUtils.getString(this, "token"));
        map.put("field", field);
        map.put("field_value",field_value);
        map.put("sign", Utils.getMd5StringMap(encryptapMap));
        userPresenter.loadEditField(map);
    }


    public void pickDate(final TextView txtBirthday) {
        Calendar cal = Calendar.getInstance();
        final DatePickerDialog mDialog = new DatePickerDialog(this, null,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        //手动设置按钮
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
                DatePicker datePicker = mDialog.getDatePicker();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                System.out.println(year + "," + month + "," + day);
                txtBirthday.setText(year+"-"+month+"-"+day);
                mDialog.dismiss();
            }
        });
        //取消按钮，如果不需要直接不设置即可
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }
}
