package com.zzq.moduletree.bean

/**
 *@auther tangedegushi
 *@creat 2018/12/13
 *@Decribe
 */
data class NavigationData(var cid: Int = 0,
                          var name: String = "",
                          var articles: List<ArticlesBean>? = null) {

    class ArticlesBean: DecorationTitleName() {
        /**
         * apkLink :
         * author : 小编
         * chapterId : 272
         * chapterName : 常用网站
         * collect : false
         * courseId : 13
         * desc :
         * envelopePic :
         * fresh : false
         * id : 1848
         * link : https://developers.google.cn/
         * niceDate : 2018-01-07
         * origin :
         * projectLink :
         * publishTime : 1515322795000
         * superChapterId : 0
         * superChapterName :
         * tags : []
         * title : Google开发者
         * type : 0
         * userId : -1
         * visible : 0
         * zan : 0
         */

        var apkLink: String? = null
        var author: String? = null
        var chapterId: Int = 0
        var chapterName: String? = null
        var isCollect: Boolean = false
        var courseId: Int = 0
        var desc: String? = null
        var envelopePic: String? = null
        var isFresh: Boolean = false
        var id: Int = 0
        lateinit var link: String
        var niceDate: String? = null
        var origin: String? = null
        var projectLink: String? = null
        var publishTime: Long = 0
        var superChapterId: Int = 0
        var superChapterName: String? = null
        var title: String? = null
        var type: Int = 0
        var userId: Int = 0
        var visible: Int = 0
        var zan: Int = 0
        var tags: List<*>? = null
    }

}