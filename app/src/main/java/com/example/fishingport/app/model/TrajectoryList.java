package com.example.fishingport.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lenovo on 2017/4/25.
 * 轨迹列表
 */

public class TrajectoryList implements Serializable {

    /**
     * status : {"code":1000,"message":"success"}
     * result : {"count":"91","page":{"page_no":"1","pages":1},"data":[{"id":"117","start_lat":"40.039532","start_lng":"116.409988","stop_lat":"40.039532","stop_lng":"116.409988","distance_count":"34","time_count":"00:00:00","img":"http://201704yg.alltosun.net/static/upload/2017/04/28/20170428144421000000_1__1.png","update_time":"2017-04-28 14:44:21","add_time":"2017-04-28 14:44:18"}]}
     */

    private StatusBean status;
    private ResultBean result;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class StatusBean implements Serializable{
        /**
         * code : 1000
         * message : success
         */

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ResultBean implements Serializable{
        /**
         * count : 91
         * page : {"page_no":"1","pages":1}
         * data : [{"id":"117","start_lat":"40.039532","start_lng":"116.409988","stop_lat":"40.039532","stop_lng":"116.409988","distance_count":"34","time_count":"00:00:00","img":"http://201704yg.alltosun.net/static/upload/2017/04/28/20170428144421000000_1__1.png","update_time":"2017-04-28 14:44:21","add_time":"2017-04-28 14:44:18"}]
         */

        private String count;
        private PageBean page;
        private List<DataBean> data;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class PageBean implements Serializable{
            /**
             * page_no : 1
             * pages : 1
             */

            private String page_no;
            private int pages;

            public String getPage_no() {
                return page_no;
            }

            public void setPage_no(String page_no) {
                this.page_no = page_no;
            }

            public int getPages() {
                return pages;
            }

            public void setPages(int pages) {
                this.pages = pages;
            }
        }

        public static class DataBean implements Serializable{
            /**
             * id : 117
             * start_lat : 40.039532
             * start_lng : 116.409988
             * stop_lat : 40.039532
             * stop_lng : 116.409988
             * distance_count : 34
             * time_count : 00:00:00
             * img : http://201704yg.alltosun.net/static/upload/2017/04/28/20170428144421000000_1__1.png
             * update_time : 2017-04-28 14:44:21
             * add_time : 2017-04-28 14:44:18
             * start_position;开始的位置
             * stop_position;结束的位置
             */

            private String id;
            private String start_lat;
            private String start_lng;
            private String stop_lat;
            private String stop_lng;
            private String distance_count;
            private String time_count;
            private String img;
            private String update_time;
            private String add_time;
            private String start_position;
            private  String stop_position;

            public String getStart_position() {
                return start_position;
            }

            public void setStart_position(String start_position) {
                this.start_position = start_position;
            }

            public String getStop_position() {
                return stop_position;
            }

            public void setStop_position(String stop_position) {
                this.stop_position = stop_position;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getStart_lat() {
                return start_lat;
            }

            public void setStart_lat(String start_lat) {
                this.start_lat = start_lat;
            }

            public String getStart_lng() {
                return start_lng;
            }

            public void setStart_lng(String start_lng) {
                this.start_lng = start_lng;
            }

            public String getStop_lat() {
                return stop_lat;
            }

            public void setStop_lat(String stop_lat) {
                this.stop_lat = stop_lat;
            }

            public String getStop_lng() {
                return stop_lng;
            }

            public void setStop_lng(String stop_lng) {
                this.stop_lng = stop_lng;
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

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }
        }
    }
}
