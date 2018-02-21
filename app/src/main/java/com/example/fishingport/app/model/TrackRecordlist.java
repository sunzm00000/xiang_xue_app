package com.example.fishingport.app.model;

import java.util.List;

/**
 * Created by Lenovo on 2017/4/28.
 * 每条记录的所有的轨迹点
 */

public class TrackRecordlist {

    /**
     * status : {"code":1000,"message":"success"}
     * result : {"count":0,"data":{"info":{"id":"19","start_lat":"21.704247","start_lng":"115.374733","stop_lat":"22.394247","stop_lng":"115.876541","distance_count":"200.78","distance_average":"23","time_count":"34590","img":"/static/upload/2017/04/21/20170421123608000000_1__58.png","update_time":"2017-04-21 12:36:08","add_time":"2017-04-21 12:25:26"},"list":[{"id":"72","trajectory_id":"19","lat":"21.704247","lng":"115.374733","x":"216","y":"123","report_time":"2017-04-21 11:09:20","add_time":"2017-04-21 12:25:38"},{"id":"73","trajectory_id":"19","lat":"21.892527","lng":"115.542145","x":"239","y":"150","report_time":"2017-04-21 11:09:20","add_time":"2017-04-21 12:25:38"}]}}
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

    public static class StatusBean {
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

    public static class ResultBean {
        /**
         * count : 0
         * data : {"info":{"id":"19","start_lat":"21.704247","start_lng":"115.374733","stop_lat":"22.394247","stop_lng":"115.876541","distance_count":"200.78","distance_average":"23","time_count":"34590","img":"/static/upload/2017/04/21/20170421123608000000_1__58.png","update_time":"2017-04-21 12:36:08","add_time":"2017-04-21 12:25:26"},"list":[{"id":"72","trajectory_id":"19","lat":"21.704247","lng":"115.374733","x":"216","y":"123","report_time":"2017-04-21 11:09:20","add_time":"2017-04-21 12:25:38"},{"id":"73","trajectory_id":"19","lat":"21.892527","lng":"115.542145","x":"239","y":"150","report_time":"2017-04-21 11:09:20","add_time":"2017-04-21 12:25:38"}]}
         */

        private int count;
        private DataBean data;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * info :
             * {"id":"19","start_lat":"21.704247","start_lng":"115.374733","stop_lat":"22.394247","stop_lng":"115.876541","distance_count":"200.78","distance_average":"23","time_count":"34590","img":"/static/upload/2017/04/21/20170421123608000000_1__58.png","update_time":"2017-04-21 12:36:08","add_time":"2017-04-21 12:25:26"}
             * list : [{"id":"72","trajectory_id":"19","lat":"21.704247","lng":"115.374733","x":"216","y":"123","report_time":"2017-04-21 11:09:20","add_time":"2017-04-21 12:25:38"},{"id":"73","trajectory_id":"19","lat":"21.892527","lng":"115.542145","x":"239","y":"150","report_time":"2017-04-21 11:09:20","add_time":"2017-04-21 12:25:38"}]
             */

            private InfoBean info;
            private List<ListBean> list;

            public InfoBean getInfo() {
                return info;
            }

            public void setInfo(InfoBean info) {
                this.info = info;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class InfoBean {
                /**
                 * id : 19
                 * start_lat : 21.704247
                 * start_lng : 115.374733
                 * stop_lat : 22.394247
                 * stop_lng : 115.876541
                 * distance_count : 200.78
                 * distance_average : 23
                 *
                 * time_count : 34590
                 * img : /static/upload/2017/04/21/20170421123608000000_1__58.png
                 * update_time : 2017-04-21 12:36:08
                 * add_time : 2017-04-21 12:25:26
                 */

                private String id;
                private String start_lat;
                private String start_lng;
                private String stop_lat;
                private String stop_lng;
                private String distance_count;
                private String distance_average;
                private String time_count;
                private String img;
                private String update_time;
                private String add_time;
                private String start_position;
                private String stop_position;

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

                public String getDistance_average() {
                    return distance_average;
                }

                public void setDistance_average(String distance_average) {
                    this.distance_average = distance_average;
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

            public static class ListBean {
                /**
                 * id : 72
                 * trajectory_id : 19
                 * lat : 21.704247
                 * lng : 115.374733
                 * x : 216
                 * y : 123
                 * report_time : 2017-04-21 11:09:20
                 * add_time : 2017-04-21 12:25:38
                 */

                private String id;
                private String trajectory_id;
                private String lat;
                private String lng;
                private String x;
                private String y;
                private String report_time;
                private String add_time;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getTrajectory_id() {
                    return trajectory_id;
                }

                public void setTrajectory_id(String trajectory_id) {
                    this.trajectory_id = trajectory_id;
                }

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

                public String getX() {
                    return x;
                }

                public void setX(String x) {
                    this.x = x;
                }

                public String getY() {
                    return y;
                }

                public void setY(String y) {
                    this.y = y;
                }

                public String getReport_time() {
                    return report_time;
                }

                public void setReport_time(String report_time) {
                    this.report_time = report_time;
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
}
