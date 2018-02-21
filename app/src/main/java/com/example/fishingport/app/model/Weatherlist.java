package com.example.fishingport.app.model;

import java.util.List;

/**
 * Created by Lenovo on 2017/5/31.
 */

public class Weatherlist {

    /**
     * status : {"code":1000,"message":"success"}
     * result : {"count":"1","data":[{"city":"朝阳区","date":"2017-05-31","temperature":"18/31","weather":"3","content":"456789123<\/p>"}]}
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
         * count : 1
         * data : [{"city":"朝阳区","date":"2017-05-31","temperature":"18/31","weather":"3","content":"456789123<\/p>"}]
         */

        private String count;
        private List<DataBean> data;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * city : 朝阳区
             * date : 2017-05-31
             * temperature : 18/31
             * weather : 3
             * content : 456789123</p>
             */

            private String city;
            private String date;
            private String temperature;
            private String weather;
            private String content;
            private String now_temp;
            private String week;

            public String getNow_temp() {
                return now_temp;
            }

            public void setNow_temp(String now_temp) {
                this.now_temp = now_temp;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
