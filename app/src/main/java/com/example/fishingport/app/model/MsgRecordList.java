package com.example.fishingport.app.model;

import java.util.List;

/**
 * Created by Lenovo on 2017/6/15.
 * 聊天记录
 */
public class MsgRecordList {
    /**
     * status : {"code":1000,"message":"success"}
     * result : [{"id":"62","u_id":"1047","huanxin_id":"79474d8a60e6cf587aa94087a860d5b1","nick_name":"base123","name":"123456","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/12/middle_20170612124316000000_1__98.jpg","type":"1","content":"ghxh"},{"id":"63","u_id":"1047","huanxin_id":"79474d8a60e6cf587aa94087a860d5b1","nick_name":"base123","name":"123456","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/12/middle_20170612124316000000_1__98.jpg","type":"1","content":"ghxh"},{"id":"64","u_id":"1047","huanxin_id":"79474d8a60e6cf587aa94087a860d5b1","nick_name":"base123","name":"123456","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/12/middle_20170612124316000000_1__98.jpg","type":"1","content":"ggg"},{"id":"65","u_id":"1055","huanxin_id":"72caf411bfc606344280a0156e766d78","nick_name":"谁的青春不迷茫","name":"","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/07/20170607164540000000_1__79.jpg","type":"1","content":"我无所谓"},{"id":"66","u_id":"1047","huanxin_id":"79474d8a60e6cf587aa94087a860d5b1","nick_name":"base123","name":"123456","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/12/middle_20170612124316000000_1__98.jpg","type":"1","content":"vvv"},{"id":"67","u_id":"1055","huanxin_id":"72caf411bfc606344280a0156e766d78","nick_name":"谁的青春不迷茫","name":"","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/07/20170607164540000000_1__79.jpg","type":"1","content":"弄"},{"id":"68","u_id":"1047","huanxin_id":"79474d8a60e6cf587aa94087a860d5b1","nick_name":"base123","name":"123456","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/12/middle_20170612124316000000_1__98.jpg","type":"1","content":"vvgg"},{"id":"69","u_id":"1047","huanxin_id":"79474d8a60e6cf587aa94087a860d5b1","nick_name":"base123","name":"123456","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/12/middle_20170612124316000000_1__98.jpg","type":"1","content":"vvv"},{"id":"70","u_id":"1055","huanxin_id":"72caf411bfc606344280a0156e766d78","nick_name":"谁的青春不迷茫","name":"","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/07/20170607164540000000_1__79.jpg","type":"1","content":"普通"},{"id":"71","u_id":"1055","huanxin_id":"72caf411bfc606344280a0156e766d78","nick_name":"谁的青春不迷茫","name":"","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/07/20170607164540000000_1__79.jpg","type":"1","content":"局"},{"id":"72","u_id":"1047","huanxin_id":"79474d8a60e6cf587aa94087a860d5b1","nick_name":"base123","name":"123456","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/12/middle_20170612124316000000_1__98.jpg","type":"1","content":"cvv"},{"id":"73","u_id":"1047","huanxin_id":"79474d8a60e6cf587aa94087a860d5b1","nick_name":"base123","name":"123456","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/12/middle_20170612124316000000_1__98.jpg","type":"1","content":"vvvg"},{"id":"74","u_id":"1047","huanxin_id":"79474d8a60e6cf587aa94087a860d5b1","nick_name":"base123","name":"123456","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/12/middle_20170612124316000000_1__98.jpg","type":"1","content":"vvvg"},{"id":"75","u_id":"1047","huanxin_id":"79474d8a60e6cf587aa94087a860d5b1","nick_name":"base123","name":"123456","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/12/middle_20170612124316000000_1__98.jpg","type":"1","content":"vvvvv"},{"id":"76","u_id":"1055","huanxin_id":"72caf411bfc606344280a0156e766d78","nick_name":"谁的青春不迷茫","name":"","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/07/20170607164540000000_1__79.jpg","type":"1","content":"介绍"},{"id":"77","u_id":"1055","huanxin_id":"72caf411bfc606344280a0156e766d78","nick_name":"谁的青春不迷茫","name":"","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/07/20170607164540000000_1__79.jpg","type":"1","content":"接口"},{"id":"78","u_id":"1047","huanxin_id":"79474d8a60e6cf587aa94087a860d5b1","nick_name":"base123","name":"123456","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/12/middle_20170612124316000000_1__98.jpg","type":"1","content":"vb"},{"id":"79","u_id":"1047","huanxin_id":"79474d8a60e6cf587aa94087a860d5b1","nick_name":"base123","name":"123456","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/12/middle_20170612124316000000_1__98.jpg","type":"1","content":"ccccg"},{"id":"80","u_id":"1055","huanxin_id":"72caf411bfc606344280a0156e766d78","nick_name":"谁的青春不迷茫","name":"","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/07/20170607164540000000_1__79.jpg","type":"1","content":"[怒]"},{"id":"81","u_id":"1047","huanxin_id":"79474d8a60e6cf587aa94087a860d5b1","nick_name":"base123","name":"123456","avatar":"http://201704yg.alltosun.net/static/upload/2017/06/12/middle_20170612124316000000_1__98.jpg","type":"1","content":"vhhvvv"}]
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
         * id : 62
         * u_id : 1047
         * huanxin_id : 79474d8a60e6cf587aa94087a860d5b1
         * nick_name : base123
         * name : 123456
         * avatar : http://201704yg.alltosun.net/static/upload/2017/06/12/middle_20170612124316000000_1__98.jpg
         * type : 1
         * content : ghxh
         */

        private String id;
        private String u_id;
        private String huanxin_id;
        private String nick_name;
        private String name;
        private String avatar;
        private String type;
        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getU_id() {
            return u_id;
        }

        public void setU_id(String u_id) {
            this.u_id = u_id;
        }

        public String getHuanxin_id() {
            return huanxin_id;
        }

        public void setHuanxin_id(String huanxin_id) {
            this.huanxin_id = huanxin_id;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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
    }
}
