package com.example.fishingport.app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

/**
 * Created by Lenovo on 2017/5/24.
 */
@Entity
public class Personal {
    @Id(autoincrement = true)
    private Long id;
    private  String uid;//
    private  String huanxin_id;
    private  String head_img;
    private String nickname;
    @Generated(hash = 1643052688)
    public Personal(Long id, String uid, String huanxin_id, String head_img,
            String nickname) {
        this.id = id;
        this.uid = uid;
        this.huanxin_id = huanxin_id;
        this.head_img = head_img;
        this.nickname = nickname;
    }
    @Generated(hash = 278967090)
    public Personal() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getHuanxin_id() {
        return this.huanxin_id;
    }
    public void setHuanxin_id(String huanxin_id) {
        this.huanxin_id = huanxin_id;
    }
    public String getHead_img() {
        return this.head_img;
    }
    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


}
