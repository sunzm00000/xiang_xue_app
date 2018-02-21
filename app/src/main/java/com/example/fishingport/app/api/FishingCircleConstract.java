package com.example.fishingport.app.api;

import com.example.fishingport.app.base.BaseView;

import java.util.Map;

/**
 * Created by wushixin on 2017/5/4.
 * 渔友圈
 */

public interface FishingCircleConstract {

    interface view extends BaseView {


        void showInfo(String string);
        void showendcircle_img(String string);

    }

    interface presenter {
        /**
         * 发布动态
         **/
        void loadFishingCircle(Map<String, String> map);
        /**
         * 发布评论
         **/
        void loadCommentAdd(Map<String, String> map);

        /*
        * 获取最后一条渔友图片动态
        * */
        void  get_end_circle_img(Map<String,String> map);

        /*
        * 删除我的渔友圈的一条动态
        *
        * */
        void deletefish(Map<String,String> map);

    }
}
