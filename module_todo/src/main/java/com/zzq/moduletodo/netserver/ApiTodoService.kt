package com.zzq.moduletodo.netserver

import com.zzq.moduletodo.bean.TodoData
import com.zzq.netlib.rxbase.BaseResponse
import io.reactivex.Observable
import retrofit2.http.*

/**
 *@auther tangedegushi
 *@creat 2018/12/17
 *@Decribe
 */
interface ApiTodoService {

    /**
     * 新增一个TODO
     * 方法：POST
     * 参数：
     * title: 新增标题（必须）
     * content: 新增详情（必须）
     * date: 2018-08-01 预定完成时间（不传默认当天，建议传）
     * type: 大于0的整数（可选）；
     * priority 大于0的整数（可选）priority 主要用于定义优先级，在app 中预定义几个优先级：重要（1），一般（2）等，
     * 查询的时候，传入priority 进行筛选；
     */
    @FormUrlEncoded
    @POST("lg/todo/add/json")
    fun addTodo(@Field("title") title: String,
                @Field("content") content: String?,
                @Field("date") date: String?,
                @Field("type") type: Int,
                @Field("priority") priority: Int): Observable<BaseResponse<TodoData.DatasBean>>

    /**
     * 更新一个TODO
     * 方法：POST
     * 参数：
     * id: 拼接在链接上，为唯一标识，列表数据返回时，每个todo 都会有个id标识 （必须）
     * title: 更新标题 （必须）
     * content: 新增详情（必须）
     * date: 2018-08-01（必须）
     * status: 0 // 0为未完成，1为完成
     * type: ；
     * priority: ；
     * 如果有当前状态没有携带，会被默认值更新，比如当前 todo status=1，更新时没有带上，会认为被重置
     */
    @FormUrlEncoded
    @POST("lg/todo/update/{id}/json")
    fun updateTodo(@Path("id") id: Int,
                   @Field("title") title: String,
                   @Field("content") content: String?,
                   @Field("date") date: String?,
                   @Field("status") status: Int,
                   @Field("type") type: Int,
                   @Field("priority") priority: Int): Observable<BaseResponse<TodoData.DatasBean>>

    /**
     * 仅更新完成状态TODO
     * 方法：POST
     * 参数：
     * id: 拼接在链接上，为唯一标识
     * status: 0或1，传1代表未完成到已完成，反之则反之。
     */
    @FormUrlEncoded
    @POST("lg/todo/done/{id}/json")
    fun updateTodoDone(@Path("id") id: Int,
                       @Field("status") status: Int): Observable<BaseResponse<TodoData.DatasBean>>

    /**
     * 删除一个TODO
     * 方法：POST
     * 参数：
     * id: 拼接在链接上，为唯一标识
     */
    @POST("lg/todo/delete/{id}/json")
    fun removeTodo(@Path("id") id: Int): Observable<BaseResponse<String>>

    /**
     * 获取TODO列表
     * 方法：POST
     * 页码从1开始，拼接在url 上
     * status 状态， 1-完成；0未完成; 默认全部展示；
     * type 创建时传入的类型, 默认全部展示
     * priority 创建时传入的优先级；默认全部展示
     * orderby 1:完成日期顺序；2.完成日期逆序；3.创建日期顺序；4.创建日期逆序(默认)；
     */
    @FormUrlEncoded
    @POST("lg/todo/v2/list/{page}/json")
    fun getTodo(@Path("page") page: Int,
                @Field("status") status: Int,
                @Field("type") type: Int,
                @Field("priority") priority: Int,
                @Field("orderby") orderby: Int): Observable<BaseResponse<TodoData>>


}