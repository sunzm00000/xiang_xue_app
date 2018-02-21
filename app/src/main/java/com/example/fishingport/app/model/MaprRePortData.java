package com.example.fishingport.app.model;

/**
 * Created by Lenovo on 2017/4/25.
 * 地图记录轨迹需要上传的数据
 */

public class MaprRePortData {
    private String lat;//纬度
    private String lng;//经度
    private int x;//x轴坐标
    private int y;//y轴坐标
    private String report_time;
    private int is_end;//是否结束
    private String distance_count;//行驶的总距离
    private String  time_count;//行驶的总时间
    private String distance_average;//行驶的平均速度




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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    public int getIs_end() {
        return is_end;
    }

    public void setIs_end(int is_end) {
        this.is_end = is_end;
    }

    public String getDistance_count() {
        return distance_count;
    }

    public void setDistance_count(String distance_count) {
        this.distance_count = distance_count;
    }

    public String getTime_count() {
        return time_count;
    }

    public void setTime_count(String time_count) {
        this.time_count = time_count;
    }

    public String getDistance_average() {
        return distance_average;
    }

    public void setDistance_average(String distance_average) {
        this.distance_average = distance_average;
    }
}
