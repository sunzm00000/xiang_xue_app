package com.example.fishingport.app.model;

import java.util.List;

/**
 * Created by Lenovo on 2017/5/23.
 * 积分完成度
 */

public class isScorelist {

    /**
     * status : {"code":1000,"message":"success"}
     * result : {"data":[{"type":"每日签到","remark":"每日首次签到","score":"+5","progress":"已领取"},{"type":"邮箱验证","remark":"首次完成验证获得","score":"+10","progress":"未完成"},{"type":"上传头像","remark":"首次上传获得","score":20,"progress":"已完成"},{"type":"实名验证","remark":"填写真实身份证获得","score":"+30","progress":"未完成"}]}
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
        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * type : 每日签到
             * remark : 每日首次签到
             * score : +5
             * progress : 已领取
             */

            private String type;
            private String remark;
            private String score;
            private String progress;
            private String type_name;

            public String getType_name() {
                return type_name;
            }

            public void setType_name(String type_name) {
                this.type_name = type_name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getProgress() {
                return progress;
            }

            public void setProgress(String progress) {
                this.progress = progress;
            }
        }
    }
}
