package com.example.fishingport.app.model;

import java.util.List;

/**
 * Created by wushixin on 2017/4/25.
 * 搜索渔友
 */

public class SearchUserBean {


    /**
     * status : {"code":1000,"message":"success"}
     * result : [{"id":1007,"mobile":"15811511350",
     *
     * "nick_name":"","intro":"","city":"","first_pinyin":"",
     * "avatar":"","gender":0,"is_friend":false},{"id":1006,
     * "mobile":"15811511349","nick_name":"","intro":"","city":"","first_pinyin":"","avatar":"","gender":0,"is_friend":false},{"id":1004,"mobile":"15811511347","nick_name":"","intro":"","city":"","first_pinyin":"","avatar":"","gender":0,"is_friend":false},{"id":1003,"mobile":"15811511346","nick_name":"","intro":"","city":"","first_pinyin":"","avatar":"","gender":0,"is_friend":false},{"id":1002,"mobile":"15811511345","nick_name":"","intro":"","city":"","first_pinyin":"","avatar":"","gender":0,"is_friend":true}]
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
         * id : 1007
         * mobile : 15811511350
         * nick_name :
         * intro :
         * city :
         * first_pinyin :
         * avatar :
         * gender : 0
         * is_friend : false
         */

        private int id;
        private String mobile;
        private String nick_name;
        private String intro;
        private String city;
        private String first_pinyin;
        private String avatar;
        private int gender;
        private boolean is_friend;

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

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getFirst_pinyin() {
            return first_pinyin;
        }

        public void setFirst_pinyin(String first_pinyin) {
            this.first_pinyin = first_pinyin;
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

        public boolean isIs_friend() {
            return is_friend;
        }

        public void setIs_friend(boolean is_friend) {
            this.is_friend = is_friend;
        }
    }
}
