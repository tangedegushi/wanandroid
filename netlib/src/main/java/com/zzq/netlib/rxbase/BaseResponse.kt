package com.zzq.netlib.rxbase

import com.google.gson.annotations.SerializedName

/**
 *@auther tangedegushi
 *@creat 2018/11/6
 *@Decribe 根据实际传递的参数这里需要修改，这只是一个模板而已
 */
class BaseResponse<D> {
    /**
     * success : true
     * errCode : 0
     * msg : success
     * data:{"":""}
     */
    @SerializedName("errorCode")
    var errorCode: Int = 0
    @SerializedName("errorMsg")
    var errorMsg: String? = null
    @SerializedName("data")
    var data: D? = null
}