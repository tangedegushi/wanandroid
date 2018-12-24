package com.zzq.moduletodo.bean

import java.io.Serializable

/**
 *@auther tangedegushi
 *@creat 2018/12/17
 *@Decribe
 */
data class TodoData(var curPage: Int = 0,
                    var offset: Int = 0,
                    var over: Boolean = false,
                    var pageCount: Int = 0,
                    var size: Int = 0,
                    var total: Int = 0,
                    var datas: List<DatasBean>) {

    class DatasBean : TodoDataType(),Serializable {
        /**
         * completeDate : null
         * completeDateStr :
         * content : 学习kotlin关于集合方面的知识
         * date : 1545062400000
         * dateStr : 2018-12-18
         * id : 5145
         * priority : 0
         * status : 0
         * title : kotlin集合
         * type : 2
         * userId : 10271
         */

        var completeDate: Any? = null
        var completeDateStr: String? = null
        var content: String = ""
        var date: Long = 0
        var dateStr: String = ""
        var id: Int = 0
        var priority: Int = 0
        var status: Int = 0
        var title: String = ""
        var type: Int = 0
        var userId: Int = 0
    }
}