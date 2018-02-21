package com.example.fishingport.app.model;

import java.util.List;

/**
 * Created by wushixin on 2017/4/24.
 * 新渔友列表
 */

public class NewFriendBean {


    /**
     * status : {"code":1000,"message":"success"}
     * result : [{"id":1002,"mobile":"15811511345","nick_name":"","avatar":"http://201704yg.alltosun.net/images/default/avatar/small.png","gender":0,"city":"","intro":"","apply_id":2,"apply_info":"","progress":2}]
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
         * id : 1002
         * mobile : 15811511345
         * nick_name :
         * avatar : http://201704yg.alltosun.net/images/default/avatar/small.png
         * gender : 0
         * city :
         * intro :
         * apply_id : 2
         * apply_info :
         * progress : 2
         */

        private int id;
        private String mobile;
        private String nick_name;
        private String avatar;
        private int gender;
        private String city;
        private String intro;
        private int apply_id;
        private String apply_info;
        private int progress;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getApply_id() {
            return apply_id;
        }

        public void setApply_id(int apply_id) {
            this.apply_id = apply_id;
        }

        public String getApply_info() {
            return apply_info;
        }

        public void setApply_info(String apply_info) {
            this.apply_info = apply_info;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }
    }
}
