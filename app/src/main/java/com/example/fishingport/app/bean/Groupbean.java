package com.example.fishingport.app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Lenovo on 2017/6/3.
 * 群聊id
 */
@Entity
public class Groupbean {
    @Id(autoincrement = true)
    private Long id;
    private  String group_id;//
    private  String huanxin_id;
    private String nicke_name;
    @Generated(hash = 1522726455)
    public Groupbean(Long id, String group_id, String huanxin_id,
            String nicke_name) {
        this.id = id;
        this.group_id = group_id;
        this.huanxin_id = huanxin_id;
        this.nicke_name = nicke_name;
    }
    @Generated(hash = 258547186)
    public Groupbean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getGroup_id() {
        return this.group_id;
    }
    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }
    public String getHuanxin_id() {
        return this.huanxin_id;
    }
    public void setHuanxin_id(String huanxin_id) {
        this.huanxin_id = huanxin_id;
    }
    public String getNicke_name() {
        return this.nicke_name;
    }
    public void setNicke_name(String nicke_name) {
        this.nicke_name = nicke_name;
    }
}
