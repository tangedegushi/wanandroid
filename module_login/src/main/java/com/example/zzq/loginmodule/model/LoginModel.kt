package com.example.zzq.loginmodule.model

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import com.example.zzq.loginmodule.bean.LoginData
import com.zzq.netlib.error.ServerException
import com.zzq.netlib.http.model.BaseModel
import com.zzq.netlib.rxbase.BaseResponse
import com.zzq.netlib.rxbase.CommonObserver
import com.zzq.netlib.utils.Logger
import com.zzq.netlib.utils.UtilApp
import com.zzq.netlib.utils.UtilRx

/**
 *@auther tangedegushi
 *@creat 2018/11/27
 *@Decribe
 */
class LoginModel(private val owner: LifecycleOwner) : BaseModel() {
    val liveLoginData = MutableLiveData<LoginData>()
    val liveRegisterOrLoginState = MutableLiveData<Int>()

    fun register(userName: String, password: String, rePassword: String) {
        getService(ApiServiceLogin::class.java)
                .register(userName, password, rePassword)
                .compose(UtilRx.applySchedulers())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : CommonObserver<BaseResponse<LoginData>>() {
                    override fun onNext(t: BaseResponse<LoginData>) {
                        if (t.errorCode == -1) {
                            UtilApp.showToast(t.errorMsg)
                            liveRegisterOrLoginState.value = ERROR
                        } else if (t.errorCode == 0) {
                            liveRegisterOrLoginState.value = REGISTER
                        } else {
                            onError(ServerException(t.errorCode, t.errorMsg ?: "the error message is undefine"))
                        }
                    }

                })
    }

    fun login(userName: String, password: String) {
        getService(ApiServiceLogin::class.java)
                .login(userName, password)
                .compose(UtilRx.applySchedulers())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : CommonObserver<BaseResponse<LoginData>>() {
                    override fun onNext(t: BaseResponse<LoginData>) {
                        if (t.errorCode != 0) {
                            UtilApp.showToast(t.errorMsg)
                            liveRegisterOrLoginState.value = ERROR
                        } else {
                            liveRegisterOrLoginState.value = LOGIN
                        }
                    }

                })
    }

    fun loginOut() {
        getService(ApiServiceLogin::class.java)
                .loginOut()
                .compose(UtilRx.applySchedulers())
                .`as`(UtilRx.bindLifeCycle(owner))
                .subscribe(object : CommonObserver<BaseResponse<String>>(){
                    override fun onNext(t: BaseResponse<String>) {
                        if (t.errorCode == 0) {
                            UtilApp.showToast("退出登录成功")
                        } else {
                            UtilApp.showToast(t.errorMsg)
                            Logger.zzqLog().d(t?.data?:"nothing come back")
                        }
                    }

                })
    }

    companion object {
        const val LOGIN = 1
        const val REGISTER = 2
        const val ERROR = 3
    }

}