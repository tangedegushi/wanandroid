package com.zzq.commonui.bean

/**
 *@auther tangedegushi
 *@creat 2018/12/5
 *@Decribe
 */
data class TypeArticleData(var curPage: Int = 0,
                           var offset: Int = 0,
                           var over: Boolean = false,
                           var pageCount: Int = 0,
                           var size: Int = 0,
                           var total: Int = 0,
                           var datas: List<DatasBean>? = null) {

    class DatasBean {
        /**
         * apkLink :
         * author : 张风捷特烈
         * chapterId : 168
         * chapterName : Drawable
         * collect : false
         * courseId : 13
         * desc :
         * envelopePic :
         * fresh : false
         * id : 7445
         * link : https://www.jianshu.com/p/89aa5f9ed17a
         * niceDate : 2018-10-31
         * origin :
         * projectLink :
         * publishTime : 1540987192000
         * superChapterId : 168
         * superChapterName : 基础知识
         * tags : []
         * title : 自己写一个svg转化为安卓xml的工具类
         * type : 0
         * userId : -1
         * visible : 1
         * zan : 0
         */

        var apkLink: String? = null
        var author: String? = null
        var chapterId: Int = 0
        var chapterName: String? = null
        var collect: Boolean = false
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