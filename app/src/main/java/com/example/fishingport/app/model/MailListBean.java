package com.example.fishingport.app.model;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

/**
 * Created by wushixin on 2017/4/18.
 */

public class MailListBean extends BaseIndexPinyinBean {
    private int id;
    private String huanxin_name;
    private String city;//城市名字
    private String avatar;//头像
    private int icon;

    public String getHuanxin_name() {
        return huanxin_name;
    }

    public void setHuanxin_name(String huanxin_name) {
        this.huanxin_name = huanxin_name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private boolean isTop;//是否是最上面的 不需要被转化成拼音的

    public MailListBean() {
    }

    public MailListBean(String city,String avatar) {
        this.city = city;
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public MailListBean setCity(String city) {
        this.city = city;
        return this;
    }

    public boolean isTop() {
        return isTop;
    }

    public MailListBean setTop(boolean top) {
        isTop = top;
        return this;
    }

    @Override
    public String getTarget() {
        return city;
    }

    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }


    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }
}
