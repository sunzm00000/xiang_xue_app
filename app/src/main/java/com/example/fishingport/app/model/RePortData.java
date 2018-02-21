package com.example.fishingport.app.model;

/**
 * Created by Lenovo on 2017/4/24.
 * 上报的数据
 */

public class RePortData {
    private String lat;//纬度
    private  String lng;//经度
    private String report_time;//上报时间
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }
}
