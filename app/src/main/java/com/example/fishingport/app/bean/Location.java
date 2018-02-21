package com.example.fishingport.app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by wushixin on 2017/4/11.
 */
@Entity
public class Location {
    @Id(autoincrement = true)
    private Long id;
    private String longitude;//经度
    private String latitude;//纬度
    private Long time;//时间
    private int type;//标示
    @Generated(hash = 1117221280)
    public Location(Long id, String longitude, String latitude, Long time,
                    int type) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
        this.type = type;
    }
    @Generated(hash = 375979639)
    public Location() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLongitude() {
        return this.longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLatitude() {
        return this.latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public Long getTime() {
        return this.time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }


}
