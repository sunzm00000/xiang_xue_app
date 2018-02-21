package com.example.fishingport.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wushixin on 2017/4/25.
 * 获取群聊列表
 */

public class GroupListBean {


    /**
     * status : {"code":1000,"message":"success"}
     * result : [{"id":"8","huanxin_id":"14553630965762","create_user_id":"1024","group_user_id":"1024","title":"新群聊","card":"群聊名片","status":"1","add_time":"2017-04-27 15:23:44","update_time":"2017-04-27 15:23:44","avatar":["http://201704yg.alltosun.net/static/upload/2017/04/26/20170426144217000000_1__68.jpg","http://201704yg.alltosun.net/static/upload/2017/04/27/middle_20170427113318000000_1__65.jpg"]},{"id":"9","huanxin_id":"14553741066243","create_user_id":"1024","group_user_id":"1024","title":"新群聊","card":"群聊名片","status":"1","add_time":"2017-04-27 15:25:29","update_time":"2017-04-27 15:25:29","avatar":["http://201704yg.alltosun.net/static/upload/2017/04/26/20170426144217000000_1__68.jpg","http://201704yg.alltosun.net/static/upload/2017/04/27/middle_20170427113318000000_1__65.jpg"]}]
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
         * id : 8
         * huanxin_id : 14553630965762
         * create_user_id : 1024
         * group_user_id : 1024
         * title : 新群聊
         * card : 群聊名片
         * status : 1
         * add_time : 2017-04-27 15:23:44
         * update_time : 2017-04-27 15:23:44
         * avatar : ["http://201704yg.alltosun.net/static/upload/2017/04/26/20170426144217000000_1__68.jpg","http://201704yg.alltosun.net/static/upload/2017/04/27/middle_20170427113318000000_1__65.jpg"]
         */

        private String id;
        private String huanxin_id;
        private String create_user_id;
        private String group_user_id;
        private String title;
        private String card;
        private String status;
        private String add_time;
        private String update_time;
        private ArrayList<String> avatar;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHuanxin_id() {
            return huanxin_id;
        }

        public void setHuanxin_id(String huanxin_id) {
            this.huanxin_id = huanxin_id;
        }

        public String getCreate_user_id() {
            return create_user_id;
        }

        public void setCreate_user_id(String create_user_id) {
            this.create_user_id = create_user_id;
        }

        public String getGroup_user_id() {
            return group_user_id;
        }

        public void setGroup_user_id(String group_user_id) {
            this.group_user_id = group_user_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
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

        public ArrayList<String> getAvatar() {
            return avatar;
        }

        public void setAvatar(ArrayList<String> avatar) {
            this.avatar = avatar;
        }
    }
}
