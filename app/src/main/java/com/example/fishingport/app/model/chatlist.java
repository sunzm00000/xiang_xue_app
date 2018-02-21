package com.example.fishingport.app.model;

import java.util.List;

/**
 * Created by Lenovo on 2017/6/8.
 * 消息回话列表
 */

public class chatlist {


    /**
     * status : {"code":1000,"message":"success"}
     * result : [{"title":"谁的青春不迷茫","type":"user","huanxin_id":"","user_list":[{"id":"1047","huanxin_name":"79474d8a60e6cf587aa94087a860d5b1","avatar":"/static/upload/2017/06/06/small_20170606194826000000_1__57.jpg"},{"id":"1055","huanxin_name":"72caf411bfc606344280a0156e766d78","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/07/20170607164540000000_1__79.jpg"}],"msg":"[图片]"}]
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
         * title : 谁的青春不迷茫
         * type : user
         * huanxin_id :
         * user_list : [{"id":"1047","huanxin_name":"79474d8a60e6cf587aa94087a860d5b1","avatar":"/static/upload/2017/06/06/small_20170606194826000000_1__57.jpg"},{"id":"1055","huanxin_name":"72caf411bfc606344280a0156e766d78","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/07/20170607164540000000_1__79.jpg"}]
         * msg : [图片]
         */
        private String id;
        private String title;
        private String type;
        private String huanxin_id;
        private String msg;
        private String group_id;
        private String msg_time;
        private List<UserListBean> user_list;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMsg_time() {
            return msg_time;
        }

        public void setMsg_time(String msg_time) {
            this.msg_time = msg_time;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getHuanxin_id() {
            return huanxin_id;
        }

        public void setHuanxin_id(String huanxin_id) {
            this.huanxin_id = huanxin_id;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<UserListBean> getUser_list() {
            return user_list;
        }

        public void setUser_list(List<UserListBean> user_list) {
            this.user_list = user_list;
        }

        public static class UserListBean {
            /**
             * id : 1047
             * huanxin_name : 79474d8a60e6cf587aa94087a860d5b1
             * avatar : /static/upload/2017/06/06/small_20170606194826000000_1__57.jpg
             */

            private String id;
            private String huanxin_name;
            private String avatar;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getHuanxin_name() {
                return huanxin_name;
            }

            public void setHuanxin_name(String huanxin_name) {
                this.huanxin_name = huanxin_name;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }
}
