package com.example.fishingport.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lenovo on 2017/5/4.
 * 港口列表
 */

public class PortList implements Serializable{

    /**
     * status : {"code":1000,"message":"success"}
     * result : {"page":{"page_no":1,"pages":1},"count":1,"data":[{"harbour_id":1,"title":"大河","longitude":"111.5","latitude":"111.5"}]}
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

    public static class StatusBean implements Serializable {
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
         * page : {"page_no":1,"pages":1}
         * count : 1
         * data : [{"harbour_id":1,"title":"大河","longitude":"111.5","latitude":"111.5"}]
         */

        private PageBean page;
        private int count;
        private List<DataBean> data;

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class PageBean implements Serializable {
            /**
             * page_no : 1
             * pages : 1
             */

            private int page_no;
            private int pages;

            public int getPage_no() {
                return page_no;
            }

            public void setPage_no(int page_no) {
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
             * harbour_id : 1
             * title : 大河
             * longitude : 111.5
             * latitude : 111.5
             */

            private int harbour_id;
            private String title;
            private String longitude;
            private String latitude;

            public int getHarbour_id() {
                return harbour_id;
            }

            public void setHarbour_id(int harbour_id) {
                this.harbour_id = harbour_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }
        }
    }
}
