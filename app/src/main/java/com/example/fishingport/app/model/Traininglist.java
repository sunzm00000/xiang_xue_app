package com.example.fishingport.app.model;

import java.util.List;

/**
 * Created by Lenovo on 2017/5/31.
 */

public class Traininglist {


    /**
     * status : {"code":1000,"message":"success"}
     * result : {"total":"4","page":{"page_no":1,"pages":1},"list":[{"id":"4","category_id":"16","source_id":"3","title":"呃呃呃","cover":"","path":"","type":"1","content":"<p>1234<\/p>","status":"1","add_time":"2017-05-31 17:06:29","update_time":"2017-05-31 17:06:29","url":"http://201704yg.alltosun.net/train/m/detail?id=4","appraise_num":"0","is_appraise":0,"comment_num":"0","source":"渔业新闻网"},{"id":"3","category_id":"2","source_id":"0","title":"121","cover":"","path":"","type":"1","content":"<p>s<\/p>","status":"1","add_time":"2017-04-07 11:47:00","update_time":"2017-04-12 10:59:58","url":"http://201704yg.alltosun.net/train/m/detail?id=3","appraise_num":"0","is_appraise":0,"comment_num":"0","source":""},{"id":"2","category_id":"2","source_id":"0","title":"第二","cover":"http://201704yg.alltosun.net/static/upload/2017/04/12/middle_20170412111706000000_1_13917_3.jpg","path":"http://201704yg.alltosun.net/static/upload/2017/04/05/7fbe2db4ed18d13180f73ef9acf0b91887709144.asf","type":"1","content":"<p>s<\/p>","status":"1","add_time":"2017-04-05 18:29:08","update_time":"2017-04-12 11:27:51","url":"http://201704yg.alltosun.net/train/m/detail?id=2","appraise_num":"0","is_appraise":0,"comment_num":"0","source":""},{"id":"1","category_id":"1","source_id":"0","title":"第一","cover":"http://201704yg.alltosun.net/static/upload/2017/04/12/middle_20170412111810000000_1_302342_51.jpg","path":"http://201704yg.alltosun.net/static/upload/2017/04/12/324492cc3438a3fc7d1ef259fabac49f82379102.asf","type":"2","content":"<p>s<\/p>","status":"1","add_time":"2017-04-05 17:59:28","update_time":"2017-04-12 11:18:11","url":"http://201704yg.alltosun.net/train/m/detail?id=1","appraise_num":"0","is_appraise":0,"comment_num":"0","source":""}]}
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
         * total : 4
         * page : {"page_no":1,"pages":1}
         * list : [{"id":"4","category_id":"16","source_id":"3","title":"呃呃呃","cover":"","path":"","type":"1","content":"<p>1234<\/p>","status":"1","add_time":"2017-05-31 17:06:29","update_time":"2017-05-31 17:06:29","url":"http://201704yg.alltosun.net/train/m/detail?id=4","appraise_num":"0","is_appraise":0,"comment_num":"0","source":"渔业新闻网"},{"id":"3","category_id":"2","source_id":"0","title":"121","cover":"","path":"","type":"1","content":"<p>s<\/p>","status":"1","add_time":"2017-04-07 11:47:00","update_time":"2017-04-12 10:59:58","url":"http://201704yg.alltosun.net/train/m/detail?id=3","appraise_num":"0","is_appraise":0,"comment_num":"0","source":""},{"id":"2","category_id":"2","source_id":"0","title":"第二","cover":"http://201704yg.alltosun.net/static/upload/2017/04/12/middle_20170412111706000000_1_13917_3.jpg","path":"http://201704yg.alltosun.net/static/upload/2017/04/05/7fbe2db4ed18d13180f73ef9acf0b91887709144.asf","type":"1","content":"<p>s<\/p>","status":"1","add_time":"2017-04-05 18:29:08","update_time":"2017-04-12 11:27:51","url":"http://201704yg.alltosun.net/train/m/detail?id=2","appraise_num":"0","is_appraise":0,"comment_num":"0","source":""},{"id":"1","category_id":"1","source_id":"0","title":"第一","cover":"http://201704yg.alltosun.net/static/upload/2017/04/12/middle_20170412111810000000_1_302342_51.jpg","path":"http://201704yg.alltosun.net/static/upload/2017/04/12/324492cc3438a3fc7d1ef259fabac49f82379102.asf","type":"2","content":"<p>s<\/p>","status":"1","add_time":"2017-04-05 17:59:28","update_time":"2017-04-12 11:18:11","url":"http://201704yg.alltosun.net/train/m/detail?id=1","appraise_num":"0","is_appraise":0,"comment_num":"0","source":""}]
         */

        private String total;
        private PageBean page;
        private List<ListBean> list;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
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

        public static class ListBean {
            /**
             * id : 4
             * category_id : 16
             * source_id : 3
             * title : 呃呃呃
             * cover :
             * path :
             * type : 1
             * content : <p>1234</p>
             * status : 1
             * add_time : 2017-05-31 17:06:29
             * update_time : 2017-05-31 17:06:29
             * url : http://201704yg.alltosun.net/train/m/detail?id=4
             * appraise_num : 0
             * is_appraise : 0
             * comment_num : 0
             * source : 渔业新闻网
             * intro
             */

            private String id;
            private String category_id;
            private String source_id;
            private String title;
            private String cover;
            private String path;
            private String type;
            private String content;
            private String status;
            private String add_time;
            private String update_time;
            private String url;
            private String appraise_num;
            private int is_appraise;
            private String comment_num;
            private String source;
            private String intro;

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            public String getSource_id() {
                return source_id;
            }

            public void setSource_id(String source_id) {
                this.source_id = source_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getAppraise_num() {
                return appraise_num;
            }

            public void setAppraise_num(String appraise_num) {
                this.appraise_num = appraise_num;
            }

            public int getIs_appraise() {
                return is_appraise;
            }

            public void setIs_appraise(int is_appraise) {
                this.is_appraise = is_appraise;
            }

            public String getComment_num() {
                return comment_num;
            }

            public void setComment_num(String comment_num) {
                this.comment_num = comment_num;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }
        }
    }
}
