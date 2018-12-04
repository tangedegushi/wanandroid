package com.zzq.modulehomepage.bean

/**
 *@auther tangedegushi
 *@creat 2018/11/19
 *@Decribe 搜素关键词返回的数据
 */

class SearchKeyData(var offset: Int,
                    var size: Int,
                    var total: Int,
                    var pageCount: Int,
                    var curPage: Int,
                    var over: Boolean,
                    var datas: List<Datas>?) {

    class Datas(
            var id: Int,
            var originId: Int,
            var title: String,
            var chapterId: Int,
            var chapterName: String?,
            var envelopePic: Any,
            var link: String,
            var author: String,
            var origin: Any,
            var publishTime: Long,
            var zan: Any,
            var desc: Any,
            var visible: Int,
            var niceDate: String,
            var courseId: Int,
            var collect: Boolean
    )
}