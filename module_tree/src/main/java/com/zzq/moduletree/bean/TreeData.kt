package com.zzq.moduletree.bean

import java.io.Serializable

/**
 *@auther tangedegushi
 *@creat 2018/12/7
 *@Decribe
 */
data class TreeData(var courseId: Int = 0,
                    var id: Int = 0,
                    var name: String = "",
                    var order: Int = 0,
                    var parentChapterId: Int = 0,
                    var userControlSetTop: Boolean = false,
                    var visible: Int = 0,
                    var children: List<ChildrenBean>? = null): Serializable {

    class ChildrenBean : DecorationTitleName(),Serializable {
        /**
         * children : []
         * courseId : 13
         * id : 60
         * name : Android Studio相关
         * order : 1000
         * parentChapterId : 150
         * userControlSetTop : false
         * visible : 1
         */

        var courseId: Int = 0
        var id: Int = 0
        var name: String? = null
        var order: Int = 0
        var parentChapterId: Int = 0
        var isUserControlSetTop: Boolean = false
        var visible: Int = 0
        var children: List<*>? = null
    }

}