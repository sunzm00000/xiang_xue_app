package com.example.fishingport.app.model;

import java.util.List;

/**
 * Created by wushixin on 2017/4/24.
 * 分类列表
 */

public class CategoryBean {


    /**
     * status : {"code":1000,"message":"success"}
     * result : {"count":2,"page":{"page_no":1,"pages":1},"data":[{"category_id":2,"category_title":"娱乐"},{"category_id":1,"category_title":"推荐"}]}
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
         * count : 2
         * page : {"page_no":1,"pages":1}
         * data : [{"category_id":2,"category_title":"娱乐"},{"category_id":1,"category_title":"推荐"}]
         */

        private int count;
        private PageBean page;
        private List<DataBean> data;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
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

        public static class PageBean {
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

        public static class DataBean {
            /**
             * category_id : 2
             * category_title : 娱乐
             */

            private int category_id;
            private String category_title;

            public int getCategory_id() {
                return category_id;
            }

            public void setCategory_id(int category_id) {
                this.category_id = category_id;
            }

            public String getCategory_title() {
                return category_title;
            }

            public void setCategory_title(String category_title) {
                this.category_title = category_title;
            }
        }
    }
}
