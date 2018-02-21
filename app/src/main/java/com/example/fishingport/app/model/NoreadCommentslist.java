package com.example.fishingport.app.model;

import java.util.List;

/**
 * Created by Lenovo on 2017/5/28.
 * 未读评论列表
 */

public class NoreadCommentslist {

    /**
     * status : {"code":1000,"message":"success"}
     * result : {"count":"4","page":{"page_no":1,"pages":1},"data":[{"id":"94","parent_id":"0","user_id":"1046","res_name":"fishing_circle","res_id":"139","to_user_id":"1045","content":"NND","is_read":"2","status":"1","add_time":"2017-06-01 19:28:54","update_time":"2017-06-01 19:28:54","user_avatar":"http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170306000000_1__2.jpg","user_name":"刘希亭","add_time_past":"14小时前","res_info":{"id":"139","user_id":"1045","trajectory_id":"0","img_exists":"1","content":"","position_name":"","position_latlng":"40.038815,116.407815","status":"1","add_time":"2017-06-01 18:06:25","img_path":["http://201704yg.alltosun.net/static/upload/2017/06/01/20170601180624000000_1__73.jpg"],"trajectory_info":[{"id":"710","user_id":"1038","client_screen":"1080,1920","distance_count":"0","time_count":"00:07","distance_average":"0","status":"2","start_position":"北京市朝阳区","stop_position":"北京市朝阳区","img":"http://201704yg.alltosun.net/static/upload/2017/05/27/20170527145503000000_1__26.png","update_time":"2017-05-27 14:55:03","add_time":"2017-05-27 14:54:56"}],"information_info":[{"id":"9","title":"渔民日常逗逼","cover":""}]}},{"id":"90","parent_id":"0","user_id":"1046","res_name":"fishing_circle","res_id":"139","to_user_id":"1045","content":"vbhhjbh","is_read":"2","status":"1","add_time":"2017-06-01 19:24:29","update_time":"2017-06-01 19:24:29","user_avatar":"http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170306000000_1__2.jpg","user_name":"刘希亭","add_time_past":"14小时前","res_info":{"id":"139","user_id":"1045","trajectory_id":"0","img_exists":"1","content":"","position_name":"","position_latlng":"40.038815,116.407815","status":"1","add_time":"2017-06-01 18:06:25","img_path":["http://201704yg.alltosun.net/static/upload/2017/06/01/20170601180624000000_1__73.jpg"],"trajectory_info":[{"id":"710","user_id":"1038","client_screen":"1080,1920","distance_count":"0","time_count":"00:07","distance_average":"0","status":"2","start_position":"北京市朝阳区","stop_position":"北京市朝阳区","img":"http://201704yg.alltosun.net/static/upload/2017/05/27/20170527145503000000_1__26.png","update_time":"2017-05-27 14:55:03","add_time":"2017-05-27 14:54:56"}],"information_info":[{"id":"9","title":"渔民日常逗逼","cover":""}]}},{"id":"89","parent_id":"0","user_id":"1046","res_name":"fishing_circle","res_id":"139","to_user_id":"1045","content":"vbbv","is_read":"2","status":"1","add_time":"2017-06-01 19:24:00","update_time":"2017-06-01 19:24:00","user_avatar":"http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170306000000_1__2.jpg","user_name":"刘希亭","add_time_past":"15小时前","res_info":{"id":"139","user_id":"1045","trajectory_id":"0","img_exists":"1","content":"","position_name":"","position_latlng":"40.038815,116.407815","status":"1","add_time":"2017-06-01 18:06:25","img_path":["http://201704yg.alltosun.net/static/upload/2017/06/01/20170601180624000000_1__73.jpg"],"trajectory_info":[{"id":"710","user_id":"1038","client_screen":"1080,1920","distance_count":"0","time_count":"00:07","distance_average":"0","status":"2","start_position":"北京市朝阳区","stop_position":"北京市朝阳区","img":"http://201704yg.alltosun.net/static/upload/2017/05/27/20170527145503000000_1__26.png","update_time":"2017-05-27 14:55:03","add_time":"2017-05-27 14:54:56"}],"information_info":[{"id":"9","title":"渔民日常逗逼","cover":""}]}},{"id":"78","parent_id":"77","user_id":"1045","res_name":"fishing_circle","res_id":"131","to_user_id":"1045","content":"不像你息怒息怒吃饱","is_read":"2","status":"1","add_time":"2017-06-01 17:01:36","update_time":"2017-06-01 17:01:36","user_avatar":"http://201704yg.alltosun.net/static/upload/2017/06/01/middle_20170601183157000000_1__96.jpg","user_name":"哈利路亚","add_time_past":"17小时前","parent_comment_content":"别具匠心纠缠你放你那放哪","res_info":{"id":"131","user_id":"1042","trajectory_id":"0","img_exists":"1","content":"一二一二一","position_name":"","position_latlng":"40.038990706145704,116.40863055035415","status":"1","add_time":"2017-05-31 15:44:56","img_path":["http://201704yg.alltosun.net/static/upload/2017/05/31/20170531154452000000_1__48.jpg","http://201704yg.alltosun.net/static/upload/2017/05/31/20170531154453000000_1__84.jpg"],"trajectory_info":[{"id":"710","user_id":"1038","client_screen":"1080,1920","distance_count":"0","time_count":"00:07","distance_average":"0","status":"2","start_position":"北京市朝阳区","stop_position":"北京市朝阳区","img":"http://201704yg.alltosun.net/static/upload/2017/05/27/20170527145503000000_1__26.png","update_time":"2017-05-27 14:55:03","add_time":"2017-05-27 14:54:56"}],"information_info":[{"id":"9","title":"渔民日常逗逼","cover":""}]}}]}
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
         * count : 4
         * page : {"page_no":1,"pages":1}
         * data : [{"id":"94","parent_id":"0","user_id":"1046","res_name":"fishing_circle","res_id":"139","to_user_id":"1045","content":"NND","is_read":"2","status":"1","add_time":"2017-06-01 19:28:54","update_time":"2017-06-01 19:28:54","user_avatar":"http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170306000000_1__2.jpg","user_name":"刘希亭","add_time_past":"14小时前","res_info":{"id":"139","user_id":"1045","trajectory_id":"0","img_exists":"1","content":"","position_name":"","position_latlng":"40.038815,116.407815","status":"1","add_time":"2017-06-01 18:06:25","img_path":["http://201704yg.alltosun.net/static/upload/2017/06/01/20170601180624000000_1__73.jpg"],"trajectory_info":[{"id":"710","user_id":"1038","client_screen":"1080,1920","distance_count":"0","time_count":"00:07","distance_average":"0","status":"2","start_position":"北京市朝阳区","stop_position":"北京市朝阳区","img":"http://201704yg.alltosun.net/static/upload/2017/05/27/20170527145503000000_1__26.png","update_time":"2017-05-27 14:55:03","add_time":"2017-05-27 14:54:56"}],"information_info":[{"id":"9","title":"渔民日常逗逼","cover":""}]}},{"id":"90","parent_id":"0","user_id":"1046","res_name":"fishing_circle","res_id":"139","to_user_id":"1045","content":"vbhhjbh","is_read":"2","status":"1","add_time":"2017-06-01 19:24:29","update_time":"2017-06-01 19:24:29","user_avatar":"http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170306000000_1__2.jpg","user_name":"刘希亭","add_time_past":"14小时前","res_info":{"id":"139","user_id":"1045","trajectory_id":"0","img_exists":"1","content":"","position_name":"","position_latlng":"40.038815,116.407815","status":"1","add_time":"2017-06-01 18:06:25","img_path":["http://201704yg.alltosun.net/static/upload/2017/06/01/20170601180624000000_1__73.jpg"],"trajectory_info":[{"id":"710","user_id":"1038","client_screen":"1080,1920","distance_count":"0","time_count":"00:07","distance_average":"0","status":"2","start_position":"北京市朝阳区","stop_position":"北京市朝阳区","img":"http://201704yg.alltosun.net/static/upload/2017/05/27/20170527145503000000_1__26.png","update_time":"2017-05-27 14:55:03","add_time":"2017-05-27 14:54:56"}],"information_info":[{"id":"9","title":"渔民日常逗逼","cover":""}]}},{"id":"89","parent_id":"0","user_id":"1046","res_name":"fishing_circle","res_id":"139","to_user_id":"1045","content":"vbbv","is_read":"2","status":"1","add_time":"2017-06-01 19:24:00","update_time":"2017-06-01 19:24:00","user_avatar":"http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170306000000_1__2.jpg","user_name":"刘希亭","add_time_past":"15小时前","res_info":{"id":"139","user_id":"1045","trajectory_id":"0","img_exists":"1","content":"","position_name":"","position_latlng":"40.038815,116.407815","status":"1","add_time":"2017-06-01 18:06:25","img_path":["http://201704yg.alltosun.net/static/upload/2017/06/01/20170601180624000000_1__73.jpg"],"trajectory_info":[{"id":"710","user_id":"1038","client_screen":"1080,1920","distance_count":"0","time_count":"00:07","distance_average":"0","status":"2","start_position":"北京市朝阳区","stop_position":"北京市朝阳区","img":"http://201704yg.alltosun.net/static/upload/2017/05/27/20170527145503000000_1__26.png","update_time":"2017-05-27 14:55:03","add_time":"2017-05-27 14:54:56"}],"information_info":[{"id":"9","title":"渔民日常逗逼","cover":""}]}},{"id":"78","parent_id":"77","user_id":"1045","res_name":"fishing_circle","res_id":"131","to_user_id":"1045","content":"不像你息怒息怒吃饱","is_read":"2","status":"1","add_time":"2017-06-01 17:01:36","update_time":"2017-06-01 17:01:36","user_avatar":"http://201704yg.alltosun.net/static/upload/2017/06/01/middle_20170601183157000000_1__96.jpg","user_name":"哈利路亚","add_time_past":"17小时前","parent_comment_content":"别具匠心纠缠你放你那放哪","res_info":{"id":"131","user_id":"1042","trajectory_id":"0","img_exists":"1","content":"一二一二一","position_name":"","position_latlng":"40.038990706145704,116.40863055035415","status":"1","add_time":"2017-05-31 15:44:56","img_path":["http://201704yg.alltosun.net/static/upload/2017/05/31/20170531154452000000_1__48.jpg","http://201704yg.alltosun.net/static/upload/2017/05/31/20170531154453000000_1__84.jpg"],"trajectory_info":[{"id":"710","user_id":"1038","client_screen":"1080,1920","distance_count":"0","time_count":"00:07","distance_average":"0","status":"2","start_position":"北京市朝阳区","stop_position":"北京市朝阳区","img":"http://201704yg.alltosun.net/static/upload/2017/05/27/20170527145503000000_1__26.png","update_time":"2017-05-27 14:55:03","add_time":"2017-05-27 14:54:56"}],"information_info":[{"id":"9","title":"渔民日常逗逼","cover":""}]}}]
         */

        private String count;
        private PageBean page;
        private List<DataBean> data;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
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

        public static class DataBean {
            /**
             * id : 94
             * parent_id : 0
             * user_id : 1046
             * res_name : fishing_circle
             * res_id : 139
             * to_user_id : 1045
             * content : NND
             * is_read : 2
             * status : 1
             * add_time : 2017-06-01 19:28:54
             * update_time : 2017-06-01 19:28:54
             * user_avatar : http://201704yg.alltosun.net/static/upload/2017/06/01/20170601170306000000_1__2.jpg
             * user_name : 刘希亭
             * add_time_past : 14小时前
             * res_info : {"id":"139","user_id":"1045","trajectory_id":"0","img_exists":"1","content":"","position_name":"","position_latlng":"40.038815,116.407815","status":"1","add_time":"2017-06-01 18:06:25","img_path":["http://201704yg.alltosun.net/static/upload/2017/06/01/20170601180624000000_1__73.jpg"],"trajectory_info":[{"id":"710","user_id":"1038","client_screen":"1080,1920","distance_count":"0","time_count":"00:07","distance_average":"0","status":"2","start_position":"北京市朝阳区","stop_position":"北京市朝阳区","img":"http://201704yg.alltosun.net/static/upload/2017/05/27/20170527145503000000_1__26.png","update_time":"2017-05-27 14:55:03","add_time":"2017-05-27 14:54:56"}],"information_info":[{"id":"9","title":"渔民日常逗逼","cover":""}]}
             * parent_comment_content : 别具匠心纠缠你放你那放哪
             */

            private String id;
            private String parent_id;
            private String user_id;
            private String res_name;
            private String res_id;
            private String to_user_id;
            private String content;
            private String is_read;
            private String status;
            private String add_time;
            private String update_time;
            private String user_avatar;
            private String user_name;
            private String add_time_past;
            private ResInfoBean res_info;
            private String parent_comment_content;



            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getRes_name() {
                return res_name;
            }

            public void setRes_name(String res_name) {
                this.res_name = res_name;
            }

            public String getRes_id() {
                return res_id;
            }

            public void setRes_id(String res_id) {
                this.res_id = res_id;
            }

            public String getTo_user_id() {
                return to_user_id;
            }

            public void setTo_user_id(String to_user_id) {
                this.to_user_id = to_user_id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getIs_read() {
                return is_read;
            }

            public void setIs_read(String is_read) {
                this.is_read = is_read;
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

            public String getUser_avatar() {
                return user_avatar;
            }

            public void setUser_avatar(String user_avatar) {
                this.user_avatar = user_avatar;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getAdd_time_past() {
                return add_time_past;
            }

            public void setAdd_time_past(String add_time_past) {
                this.add_time_past = add_time_past;
            }

            public ResInfoBean getRes_info() {
                return res_info;
            }

            public void setRes_info(ResInfoBean res_info) {
                this.res_info = res_info;
            }

            public String getParent_comment_content() {
                return parent_comment_content;
            }

            public void setParent_comment_content(String parent_comment_content) {
                this.parent_comment_content = parent_comment_content;
            }

            public static class ResInfoBean {
                /**
                 * id : 139
                 * user_id : 1045
                 * trajectory_id : 0
                 * img_exists : 1
                 * content :
                 * position_name :
                 * position_latlng : 40.038815,116.407815
                 * status : 1
                 * add_time : 2017-06-01 18:06:25
                 * img_path : ["http://201704yg.alltosun.net/static/upload/2017/06/01/20170601180624000000_1__73.jpg"]
                 * trajectory_info : [{"id":"710","user_id":"1038","client_screen":"1080,1920","distance_count":"0","time_count":"00:07","distance_average":"0","status":"2","start_position":"北京市朝阳区","stop_position":"北京市朝阳区","img":"http://201704yg.alltosun.net/static/upload/2017/05/27/20170527145503000000_1__26.png","update_time":"2017-05-27 14:55:03","add_time":"2017-05-27 14:54:56"}]
                 * information_info : [{"id":"9","title":"渔民日常逗逼","cover":""}]
                 */

                private String id;
                private String user_id;
                private String trajectory_id;
                private String img_exists;
                private String content;
                private String position_name;
                private String position_latlng;
                private String status;
                private String add_time;
                private List<String> img_path;
                private List<TrajectoryInfoBean> trajectory_info;
                private List<InformationInfoBean> information_info;
                private  List<TrainInfoBean> train_info;
                private  List<MarketInfoBean> market_info;
                private List<String> img_thumb_path;

                public List<String> getImg_thumb_path() {
                    return img_thumb_path;
                }

                public void setImg_thumb_path(List<String> img_thumb_path) {
                    this.img_thumb_path = img_thumb_path;
                }

                public List<TrainInfoBean> getTrain_info() {
                    return train_info;
                }

                public void setTrain_info(List<TrainInfoBean> train_info) {
                    this.train_info = train_info;
                }

                public List<MarketInfoBean> getMarket_info() {
                    return market_info;
                }

                public void setMarket_info(List<MarketInfoBean> market_info) {
                    this.market_info = market_info;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getTrajectory_id() {
                    return trajectory_id;
                }

                public void setTrajectory_id(String trajectory_id) {
                    this.trajectory_id = trajectory_id;
                }

                public String getImg_exists() {
                    return img_exists;
                }

                public void setImg_exists(String img_exists) {
                    this.img_exists = img_exists;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getPosition_name() {
                    return position_name;
                }

                public void setPosition_name(String position_name) {
                    this.position_name = position_name;
                }

                public String getPosition_latlng() {
                    return position_latlng;
                }

                public void setPosition_latlng(String position_latlng) {
                    this.position_latlng = position_latlng;
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

                public List<String> getImg_path() {
                    return img_path;
                }

                public void setImg_path(List<String> img_path) {
                    this.img_path = img_path;
                }

                public List<TrajectoryInfoBean> getTrajectory_info() {
                    return trajectory_info;
                }

                public void setTrajectory_info(List<TrajectoryInfoBean> trajectory_info) {
                    this.trajectory_info = trajectory_info;
                }

                public List<InformationInfoBean> getInformation_info() {
                    return information_info;
                }

                public void setInformation_info(List<InformationInfoBean> information_info) {
                    this.information_info = information_info;
                }

                public static class TrajectoryInfoBean {
                    /**
                     * id : 710
                     * user_id : 1038
                     * client_screen : 1080,1920
                     * distance_count : 0
                     * time_count : 00:07
                     * distance_average : 0
                     * status : 2
                     * start_position : 北京市朝阳区
                     * stop_position : 北京市朝阳区
                     * img : http://201704yg.alltosun.net/static/upload/2017/05/27/20170527145503000000_1__26.png
                     * update_time : 2017-05-27 14:55:03
                     * add_time : 2017-05-27 14:54:56
                     */
                    private String id;
                    private String user_id;
                    private String client_screen;
                    private String distance_count;
                    private String time_count;
                    private String distance_average;
                    private String status;
                    private String start_position;
                    private String stop_position;
                    private String img;
                    private String update_time;
                    private String add_time;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getUser_id() {
                        return user_id;
                    }

                    public void setUser_id(String user_id) {
                        this.user_id = user_id;
                    }

                    public String getClient_screen() {
                        return client_screen;
                    }

                    public void setClient_screen(String client_screen) {
                        this.client_screen = client_screen;
                    }

                    public String getDistance_count() {
                        return distance_count;
                    }

                    public void setDistance_count(String distance_count) {
                        this.distance_count = distance_count;
                    }

                    public String getTime_count() {
                        return time_count;
                    }

                    public void setTime_count(String time_count) {
                        this.time_count = time_count;
                    }

                    public String getDistance_average() {
                        return distance_average;
                    }

                    public void setDistance_average(String distance_average) {
                        this.distance_average = distance_average;
                    }

                    public String getStatus() {
                        return status;
                    }

                    public void setStatus(String status) {
                        this.status = status;
                    }

                    public String getStart_position() {
                        return start_position;
                    }

                    public void setStart_position(String start_position) {
                        this.start_position = start_position;
                    }

                    public String getStop_position() {
                        return stop_position;
                    }

                    public void setStop_position(String stop_position) {
                        this.stop_position = stop_position;
                    }

                    public String getImg() {
                        return img;
                    }

                    public void setImg(String img) {
                        this.img = img;
                    }

                    public String getUpdate_time() {
                        return update_time;
                    }

                    public void setUpdate_time(String update_time) {
                        this.update_time = update_time;
                    }

                    public String getAdd_time() {
                        return add_time;
                    }

                    public void setAdd_time(String add_time) {
                        this.add_time = add_time;
                    }
                }
                public static  class TrainInfoBean{
                    private String id;
                    private String title;
                    private String cover;
                    private String path;
                    private String type;

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public String getPath() {
                        return path;
                    }

                    public void setPath(String path) {
                        this.path = path;
                    }

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

                    public String getCover() {
                        return cover;
                    }

                    public void setCover(String cover) {
                        this.cover = cover;
                    }
                }
                public static  class MarketInfoBean{
                    private String id;
                    private String title;
                    private String cover;
                    private String path;

                    public String getPath() {
                        return path;
                    }

                    public void setPath(String path) {
                        this.path = path;
                    }

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

                    public String getCover() {
                        return cover;
                    }

                    public void setCover(String cover) {
                        this.cover = cover;
                    }
                }
                public static class InformationInfoBean {
                    /**
                     * id : 9
                     * title : 渔民日常逗逼
                     * cover :
                     */
                    private String id;
                    private String title;
                    private String cover;
                    private String path;
                    public String getPath() {
                        return path;
                    }
                    public void setPath(String path) {
                        this.path = path;
                    }
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

                    public String getCover() {
                        return cover;
                    }

                    public void setCover(String cover) {
                        this.cover = cover;
                    }
                }
            }
        }
    }
}
