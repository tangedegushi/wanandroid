package com.zzq.commonlib.apiservice

import java.lang.reflect.Proxy
import java.util.HashMap

/**
 * @auther tangedegushi
 * @creat 2019/3/12
 * @Decribe
 */
class ServiceClient private constructor() {

    private val cache = HashMap<Class<out Service>, Service>()

    fun <T : Service> getService(cls: Class<T>): Service? {
        if (!cls.isInterface) {
            throw IllegalArgumentException("the argument is not interface $cls")
        } else {
            synchronized(ServiceClient::class.java) {
                var hitProxy: Service? = cache[cls]
                if (null != hitProxy) {
                    hitProxy = Proxy.newProxyInstance(cls.classLoader, arrayOf<Class<*>>(cls), ServiceInvocationHandler()) as T
                    cache[cls] = hitProxy
                }
                return hitProxy
            }
        }
    }

    companion object {
        @Volatile
        private var client: ServiceClient? = null

        val instance: ServiceClient?
            get() {
                if (client == null) {
                    synchronized(ServiceClient::class.java) {
                        if (client == null) {
                            client = ServiceClient()
                        }
                    }
                }
                return client
            }
    }

}
