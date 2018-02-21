package com.example.fishingport.app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Lenovo on 2017/6/13.
 */
@Entity
public class ff {
    private String ss;

    @Generated(hash = 1267029299)
    public ff(String ss) {
        this.ss = ss;
    }

    @Generated(hash = 1674275166)
    public ff() {
    }

    public String getSs() {
        return this.ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }
}
