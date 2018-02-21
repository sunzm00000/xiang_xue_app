package com.example.fishingport.app.model;

import java.util.List;

/**
 * Created by wushixin on 2017/4/27.
 * 群信息
 */

public class GroupInfoBean {


    /**
     * status : {"code":1000,"message":"success"}
     * result : {"id":"8","huanxin_id":"14553630965762","create_user_id":"1024","group_user_id":"1024","title":"新群聊","card":"群聊名片","status":"1","add_time":"2017-04-27 15:23:44","update_time":"2017-04-27 15:23:44","user_total":"3","user_list":[{"id":"1023","token":"9c76469ce540e96d6c5268ba55b99ed5","rid":"05043bc3a32","open_id":"","city_id":"0","mobile":"13521141687","user_name":"","email":"","password":"a745e23fc92b1c1f0e8b3b9aaf6fdf01","nick_name":"哈哈","first_pinyin":"h","avatar":"http://201704yg.alltosun.net/static/upload/2017/04/26/20170426144217000000_1__68.jpg","gender":"0","birthday":"0000-00-00","intro":"","id_card":"","province":"","city":"北京","address":"","hash":"nwAQ","type":"11","huanxin_name":"36723daf1a0871fa8c47b3e1cae0bd67","huanx_passwd":"e10adc3949ba59abbe56e057f20f883e","score":"0","status":"1","add_time":"2017-04-26 14:42:08","update_time":"2017-04-26 14:42:58"},{"id":"1025","token":"b87595e267e63e89844a5e0258ba008d","rid":"05043bc3a32","open_id":"","city_id":"0","mobile":"13521141689","user_name":"","email":"","password":"4d81844e9f23be95abb815b5e1b21a92","nick_name":"lala","first_pinyin":"l","avatar":"http://201704yg.alltosun.net/images/default/avatar/small.png","gender":"0","birthday":"0000-00-00","intro":"","id_card":"","province":"","city":"beijing","address":"","hash":"IPyg","type":"11","huanxin_name":"186a037f27955245691de48eaa7ba4c5","huanx_passwd":"e10adc3949ba59abbe56e057f20f883e","score":"0","status":"1","add_time":"2017-04-27 10:50:39","update_time":"2017-04-27 10:52:18"},{"id":"1024","token":"ad2735efee60f23f258f6edc642f2c16","rid":"05043bc3a32","open_id":"","city_id":"0","mobile":"13521141688","user_name":"","email":"","password":"d08e034be911bdab60643d303f758feb","nick_name":"测试","first_pinyin":"c","avatar":"http://201704yg.alltosun.net/static/upload/2017/04/27/middle_20170427113318000000_1__65.jpg","gender":"0","birthday":"0000-00-00","intro":"","id_card":"","province":"","city":"北京","address":"","hash":"ZF6Y","type":"11","huanxin_name":"0320d03c7dd14982100018247bf3ac58","huanx_passwd":"e10adc3949ba59abbe56e057f20f883e","score":"0","status":"1","add_time":"2017-04-26 15:19:34","update_time":"2017-04-27 11:33:18"}]}
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
         * id : 8
         * huanxin_id : 14553630965762
         * create_user_id : 1024
         * group_user_id : 1024
         * title : 新群聊
         * card : 群聊名片
         * status : 1
         * add_time : 2017-04-27 15:23:44
         * update_time : 2017-04-27 15:23:44
         * user_total : 3
         * user_list : [{"id":"1023","token":"9c76469ce540e96d6c5268ba55b99ed5","rid":"05043bc3a32","open_id":"","city_id":"0","mobile":"13521141687","user_name":"","email":"","password":"a745e23fc92b1c1f0e8b3b9aaf6fdf01","nick_name":"哈哈","first_pinyin":"h","avatar":"http://201704yg.alltosun.net/static/upload/2017/04/26/20170426144217000000_1__68.jpg","gender":"0","birthday":"0000-00-00","intro":"","id_card":"","province":"","city":"北京","address":"","hash":"nwAQ","type":"11","huanxin_name":"36723daf1a0871fa8c47b3e1cae0bd67","huanx_passwd":"e10adc3949ba59abbe56e057f20f883e","score":"0","status":"1","add_time":"2017-04-26 14:42:08","update_time":"2017-04-26 14:42:58"},{"id":"1025","token":"b87595e267e63e89844a5e0258ba008d","rid":"05043bc3a32","open_id":"","city_id":"0","mobile":"13521141689","user_name":"","email":"","password":"4d81844e9f23be95abb815b5e1b21a92","nick_name":"lala","first_pinyin":"l","avatar":"http://201704yg.alltosun.net/images/default/avatar/small.png","gender":"0","birthday":"0000-00-00","intro":"","id_card":"","province":"","city":"beijing","address":"","hash":"IPyg","type":"11","huanxin_name":"186a037f27955245691de48eaa7ba4c5","huanx_passwd":"e10adc3949ba59abbe56e057f20f883e","score":"0","status":"1","add_time":"2017-04-27 10:50:39","update_time":"2017-04-27 10:52:18"},{"id":"1024","token":"ad2735efee60f23f258f6edc642f2c16","rid":"05043bc3a32","open_id":"","city_id":"0","mobile":"13521141688","user_name":"","email":"","password":"d08e034be911bdab60643d303f758feb","nick_name":"测试","first_pinyin":"c","avatar":"http://201704yg.alltosun.net/static/upload/2017/04/27/middle_20170427113318000000_1__65.jpg","gender":"0","birthday":"0000-00-00","intro":"","id_card":"","province":"","city":"北京","address":"","hash":"ZF6Y","type":"11","huanxin_name":"0320d03c7dd14982100018247bf3ac58","huanx_passwd":"e10adc3949ba59abbe56e057f20f883e","score":"0","status":"1","add_time":"2017-04-26 15:19:34","update_time":"2017-04-27 11:33:18"}]
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
        private String user_total;
        private List<UserListBean> user_list;

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

        public String getUser_total() {
            return user_total;
        }

        public void setUser_total(String user_total) {
            this.user_total = user_total;
        }

        public List<UserListBean> getUser_list() {
            return user_list;
        }

        public void setUser_list(List<UserListBean> user_list) {
            this.user_list = user_list;
        }

        public static class UserListBean {
            /**
             * id : 1023
             * token : 9c76469ce540e96d6c5268ba55b99ed5
             * rid : 05043bc3a32
             * open_id :
             * city_id : 0
             * mobile : 13521141687
             * user_name :
             * email :
             * password : a745e23fc92b1c1f0e8b3b9aaf6fdf01
             * nick_name : 哈哈
             * first_pinyin : h
             * avatar : http://201704yg.alltosun.net/static/upload/2017/04/26/20170426144217000000_1__68.jpg
             * gender : 0
             * birthday : 0000-00-00
             * intro :
             * id_card :
             * province :
             * city : 北京
             * address :
             * hash : nwAQ
             * type : 11
             * huanxin_name : 36723daf1a0871fa8c47b3e1cae0bd67
             * huanx_passwd : e10adc3949ba59abbe56e057f20f883e
             * score : 0
             * status : 1
             * add_time : 2017-04-26 14:42:08
             * update_time : 2017-04-26 14:42:58
             */

            private String id;
            private String token;
            private String rid;
            private String open_id;
            private String city_id;
            private String mobile;
            private String user_name;
            private String email;
            private String password;
            private String nick_name;
            private String first_pinyin;
            private String avatar;
            private String gender;
            private String birthday;
            private String intro;
            private String id_card;
            private String province;
            private String city;
            private String address;
            private String hash;
            private String type;
            private String huanxin_name;
            private String huanx_passwd;
            private String score;
            private String status;
            private String add_time;
            private String update_time;
            private int icon;

            public int getIcon() {
                return icon;
            }

            public void setIcon(int icon) {
                this.icon = icon;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getRid() {
                return rid;
            }

            public void setRid(String rid) {
                this.rid = rid;
            }

            public String getOpen_id() {
                return open_id;
            }

            public void setOpen_id(String open_id) {
                this.open_id = open_id;
            }

            public String getCity_id() {
                return city_id;
            }

            public void setCity_id(String city_id) {
                this.city_id = city_id;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
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

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getId_card() {
                return id_card;
            }

            public void setId_card(String id_card) {
                this.id_card = id_card;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getHuanxin_name() {
                return huanxin_name;
            }

            public void setHuanxin_name(String huanxin_name) {
                this.huanxin_name = huanxin_name;
            }

            public String getHuanx_passwd() {
                return huanx_passwd;
            }

            public void setHuanx_passwd(String huanx_passwd) {
                this.huanx_passwd = huanx_passwd;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
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
        }
    }
}
