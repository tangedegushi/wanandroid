package com.zzq.commonlib.apiservice

import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.HashMap

/**
 * @auther tangedegushi
 * @creat 2019/3/12
 * @Decribe
 */
class ServiceInvocationHandler : InvocationHandler {

    private val serviceMap = HashMap<Class<*>, Service>()

    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
        val service: Service? = serviceMap[method.declaringClass] ?: return null
        try {
            return method.invoke(service, *args)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return null
    }

    fun addProxyImpl(cls: Class<out Service>, impl: Service) {
        serviceMap[cls] = impl
    }

}
