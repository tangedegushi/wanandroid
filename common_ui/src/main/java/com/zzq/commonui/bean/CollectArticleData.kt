package com.zzq.commonui.bean

/**
 *@auther tangedegushi
 *@creat 2018/12/6
 *@Decribe
 */
data class CollectArticleData(var curPage: Int = 0,
                              var offset: Int = 0,
                              var over: Boolean = false,
                              var pageCount: Int = 0,
                              var size: Int = 0,
                              var total: Int = 0,
                              var datas: List<DatasBean>? = null) {
    class DatasBean {
        /**
         * author : 郭霖
         * chapterId : 409
         * chapterName : 郭霖
         * courseId : 13
         * desc :
         * envelopePic :
         * id : 35138
         * link : https://mp.weixin.qq.com/s/kcbEto2ljhhCSNknIWtbzA
         * niceDate : 5分钟前
         * origin :
         * originId : 7620
         * publishTime : 1544080736000
         * title : Android Monitor工具详解大全
         * userId : 10271
         * visible : 0
         * zan : 0
         */

        var author: String? = null
        var chapterId: Int = 0
        var chapterName: String? = null
        var courseId: Int = 0
        var desc: String? = null
        var envelopePic: String? = null
        var id: Int = 0
        lateinit var link: String
        var niceDate: String? = null
        var origin: String? = null
        var originId: Int = 0
        var publishTime: Long = 0
        var title: String? = null
        var userId: Int = 0
        var visible: Int = 0
        var zan: Int = 0
    }
}