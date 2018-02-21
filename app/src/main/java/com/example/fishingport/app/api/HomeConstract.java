package com.example.fishingport.app.api;

import com.example.fishingport.app.base.BaseView;

import java.util.Map;

/**
 * Created by wushixin on 2017/4/21.
 * 首页数据
 */

public interface HomeConstract {

    interface view extends BaseView {


        void showInfo(String string);
        void showcityid(String ss);

    }

    interface presenter {
        /**
         * 取新闻政策信息
         **/
        void loadNewsList(Map<String, String> map);
        /**
         * 取鱼货行情信息
         **/
        void loadMarketList(Map<String, String> map);
        /**
         * 取分类列表
         **/
        void loadCategoryList(Map<String, String> map);
        /**
         * 取城市iD
         **/
        void loadCityId(Map<String, String> map);
        /**
         * 取所在城市的天气
         **/
        void loadWeatherList(Map<String, String> map);

        /*
        * 取培训列表
        * */
        void getTrainList(Map<String,String> map);

        /*
        * 点赞
        * */
        void add_appraise(Map<String,String> map);

       /*
       * 点赞数评论数
       * */
       void detailnum(Map<String,String> map);

        /*
        * 取推荐新闻
        * */
        void  recommendlist(Map<String,String> map);


    }
}
