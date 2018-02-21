package com.example.fishingport.app.model;

import java.util.List;

/**
 * Created by wushixin on 2017/4/28.
 * 取积分明细
 */

public class ScoreRecordList {


    /**
     * status : {"code":1000,"message":"success"}
     * result : {"count":5,"data":[{"score":"5","score_status":"1","source":"1"},{"score":"5","score_status":"1","source":"1"},{"score":"5","score_status":"1","source":"1"},{"score":"5","score_status":"1","source":"1"},{"score":"5","score_status":"1","source":"1"}]}
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
         * count : 5
         * data : [{"score":"5","score_status":"1","source":"1"},{"score":"5","score_status":"1","source":"1"},{"score":"5","score_status":"1","source":"1"},{"score":"5","score_status":"1","source":"1"},{"score":"5","score_status":"1","source":"1"}]
         */

        private int count;
        private List<DataBean> data;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * score : 5
             * score_status : 1
             * source : 1
             */

            private String score;
            private String score_status;
            private String source;
            private String add_time;

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getScore_status() {
                return score_status;
            }

            public void setScore_status(String score_status) {
                this.score_status = score_status;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }
        }
    }
}
