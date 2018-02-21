package com.example.fishingport.app.model;

import java.util.List;

/**
 * Created by wushixin on 2017/5/9.
 */

public class MarketList {

    /**
     * status : {"code":1000,"message":"success"}
     * result : {"page":{"page_no":"1","pages":1},"count":1,"data":[{"market_id":1,"title":"1231","intro":"23423","cover":"http://201704yg.alltosun.net/static/upload/2017/04/11/20170411120620000000_1_9492_27.png","content":"顶戴柘城<\/p>","add_time":"2017-04-11 12:06:21","appraise":0,"comment_num":0}]}
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
         * page : {"page_no":"1","pages":1}
         * count : 1
         * data : [{"market_id":1,"title":"1231","intro":"23423","cover":"http://201704yg.alltosun.net/static/upload/2017/04/11/20170411120620000000_1_9492_27.png","content":"顶戴柘城<\/p>","add_time":"2017-04-11 12:06:21","appraise":0,"comment_num":0}]
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

        public static class PageBean {
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

        public static class DataBean {
            /**
             * market_id : 1
             * title : 1231
             * intro : 23423
             * cover : http://201704yg.alltosun.net/static/upload/2017/04/11/20170411120620000000_1_9492_27.png
             * content : 顶戴柘城</p>
             * add_time : 2017-04-11 12:06:21
             * appraise : 0
             * comment_num : 0
             */

            private int market_id;
            private String title;
            private String intro;
            private String cover;
            private String content;
            private String add_time;
            private int appraise;
            private int comment_num;
            private  String source;//来源
            private String is_appraise;

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getIs_appraise() {
                return is_appraise;
            }

            public void setIs_appraise(String is_appraise) {
                this.is_appraise = is_appraise;
            }

            public int getMarket_id() {
                return market_id;
            }

            public void setMarket_id(int market_id) {
                this.market_id = market_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public int getAppraise() {
                return appraise;
            }

            public void setAppraise(int appraise) {
                this.appraise = appraise;
            }

            public int getComment_num() {
                return comment_num;
            }

            public void setComment_num(int comment_num) {
                this.comment_num = comment_num;
            }
        }
    }
}
