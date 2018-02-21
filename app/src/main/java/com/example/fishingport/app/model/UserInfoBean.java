package com.example.fishingport.app.model;

/**
 * Created by wushixin on 2017/4/24.
 * 用户信息
 */

public class UserInfoBean {


    /**
     * status : {"code":1000,"message":"success"}
     * result : {"uid":"1001","mobile":"18910424482",
     * "nick_name":"我是大好人",
     * "email":"",
     * "avatar":"http://ace.alltosun.com/static/upload/avatar/1/3/small_1_3_avatar.jpg","gender":"0",
     * "id_card":"",
     * "birthday":"0000-00-00",
     * "city":"",
     * "intro":"","user_name":"","score":"25","appraise_num":"0",
     * "comment_num":"0"
     *
     * }
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
         * uid : 1001
         * mobile : 18910424482
         * nick_name : 我是大好人
         * email :
         * avatar : http://ace.alltosun.com/static/upload/avatar/1/3/small_1_3_avatar.jpg
         * gender : 0
         * id_card :
         * birthday : 0000-00-00
         * city :
         * intro :
         * user_name :
         * score : 25
         * appraise_num : 0
         * comment_num : 0
         */

        private String uid;
        private String mobile;
        private String nick_name;
        private String email;
        private String avatar;
        private String gender;
        private String id_card;
        private String birthday;
        private String city;
        private String intro;
        private String user_name;
        private String score;
        private String appraise_num;
        private String comment_num;
        private String is_friend;

        public String getIs_friend() {
            return is_friend;
        }

        public void setIs_friend(String is_friend) {
            this.is_friend = is_friend;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getId_card() {
            return id_card;
        }

        public void setId_card(String id_card) {
            this.id_card = id_card;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
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

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getAppraise_num() {
            return appraise_num;
        }

        public void setAppraise_num(String appraise_num) {
            this.appraise_num = appraise_num;
        }

        public String getComment_num() {
            return comment_num;
        }

        public void setComment_num(String comment_num) {
            this.comment_num = comment_num;
        }
    }
}
