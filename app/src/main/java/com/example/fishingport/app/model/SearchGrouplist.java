package com.example.fishingport.app.model;

import java.util.List;

/**
 * Created by Lenovo on 2017/6/6.
 */

public class SearchGrouplist {


    /**
     * status : {"code":1000,"message":"success"}
     * result : [{"title":"新群聊","avatar":["http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170518000000_1__38.jpg","http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170306000000_1__2.jpg"],"huanxin_id":"18063639511041"},{"title":"新群聊","avatar":["http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170518000000_1__38.jpg","http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170306000000_1__2.jpg"],"huanxin_id":"18067275972610"},{"title":"新群聊","avatar":["http://201704yg.alltosun.net/static/upload/2017/06/01/middle_20170601183157000000_1__96.jpg","http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170518000000_1__38.jpg"],"huanxin_id":"18070591569921"},{"title":"新群聊","avatar":["http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170518000000_1__38.jpg","http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170306000000_1__2.jpg"],"huanxin_id":"18074436698114"}]
     */

    private StatusBean status;
    private List<ResultBean> result;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
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
         * title : 新群聊
         * avatar : ["http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170518000000_1__38.jpg","http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170306000000_1__2.jpg"]
         * huanxin_id : 18063639511041
         */
        private String id;
        private String title;
        private String huanxin_id;
        private List<String> avatar;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getHuanxin_id() {
            return huanxin_id;
        }

        public void setHuanxin_id(String huanxin_id) {
            this.huanxin_id = huanxin_id;
        }

        public List<String> getAvatar() {
            return avatar;
        }

        public void setAvatar(List<String> avatar) {
            this.avatar = avatar;
        }
    }
}
