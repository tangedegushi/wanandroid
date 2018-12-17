package com.zzq.netlib.utils

import android.content.Context
import android.content.SharedPreferences
import android.support.v4.util.ArrayMap
import android.text.TextUtils
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 *@auther tangedegushi
 *@creat 2018/11/7
 *@Decribe 目的：提供一个全局操作sp的工具类，便于管理
 *           思路：将所有sp的名称和key值都在这里设置常量，这样便于统一管理
 *           比如这里提供了一个默认的sp文件名：common_data,如果还有其他sp文件名同样以这样的方式设置，key值也是类型
 *
 *           使用：如果加载的sp文件比较耗时（初次），那我们可以先调用 {@link #initSp(String spName)}这样可以先在子线程中去加载sp文件，
 *           如果我们是在使用的时候再去初始化的sp，那么主线程会等待子线程加载完后在进行操作，可能就会引起卡顿，如果已经加载过sp文件，
 *           那么内存中就已经存在，不会再去加载了（sp文件加载过一次，内存中就会一直存在），加载过一次后直接设置值既可以了
 */
object UtilSp {

    /*----------------------sp中存储的key----------------------*/
    const val SP_NAME_LOGIN = "sp_login_name"
    const val KEY_USER_NAME = "userName"
    const val KEY_HAD_LOGIN = "hadLogin"
    const val KEY_PASSWORD = "password"
    const val KEY_COOKIE_USERNAME = "loginUserName"
    const val KEY_COOKIE_TOKEN_PASS = "token_pass"
    const val KEY_COOKIE_JSESSIONID = "JSESSIONID"
    /*---------------------------------------------------------*/

    private val SP_NAME_DEFAULT = "common_data"
    private val spMap = ArrayMap<String, SharedPreferences>()
    private var currentSP: SharedPreferences? = null
    private var defaultSP: SharedPreferences? = null

    /**
     * @param spName 初次加载sp中的内容时可以先调用这个方法，这样是为了先将sp中的内容加载进内存中
     */
    fun initSp(spName: String?) {
        if (TextUtils.isEmpty(spName)) {
            if (defaultSP == null) {
                val context = UtilApp.obtainAppComponent().application()
                currentSP = context.getSharedPreferences(SP_NAME_DEFAULT, Context.MODE_PRIVATE)
                defaultSP = currentSP
                spMap[SP_NAME_DEFAULT] = currentSP
            } else {
                currentSP = defaultSP
            }
            return
        }
        currentSP = spMap[spName]
        if (currentSP == null) {
            val context = UtilApp.obtainAppComponent().application()
            currentSP = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
            spMap[spName] = currentSP
        }

    }

    /**
     * 当有多个值需要设置时，建议获取Editor，然后等所有值设置完后在调用apply（），
     * 这样做是为了减少对象的创建，以及退出界面时阻塞主线程（退出界面时，如果sp中的
     * 内容还没有保存到本地，保存到本地的这个过程就会从后台切换到主线程中来执行，
     * 等到全部保存到本地后才会继续往下执行）
     */
    fun getSpEditor(spName: String?): SharedPreferences.Editor {
        initSp(spName)
        return currentSP!!.edit()
    }

    /**
     * 存储字符串信息到sp中
     */
    fun setString(key: String, value: String) {
        setString(key, value, null)
    }

    fun setString(key: String, value: String, spName: String?) {
        initSp(spName)
        currentSP!!.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return getString(key, null, null)
    }

    fun getString(key: String, defValue: String?, spName: String?): String? {
        initSp(spName)
        return currentSP!!.getString(key, defValue)
    }

    fun setInt(key: String, value: Int) {
        setInt(key, value, null)
    }

    fun setInt(key: String, value: Int, spName: String?) {
        initSp(spName)
        currentSP!!.edit().putInt(key, value).apply()
    }

    fun getInt(key: String): Int {
        return getInt(key, -1, null)
    }

    fun getInt(key: String, defValue: Int, spName: String?): Int {
        initSp(spName)
        return currentSP!!.getInt(key, defValue)
    }

    fun setBoolean(key: String, b: Boolean) {
        setBoolean(key, b, null)
    }

    fun setBoolean(key: String, b: Boolean, spName: String?) {
        initSp(spName)
        currentSP!!.edit().putBoolean(key, b).apply()
    }

    fun getBoolean(key: String): Boolean {
        return getBoolean(key, false, null)
    }

    fun getBoolean(key: String, defValue: Boolean, spName: String?): Boolean {
        initSp(spName)
        return currentSP!!.getBoolean(key, defValue)
    }

    /**
     * @param key 清除SP中的一个key
     */
    fun removeKey(key: String) {
        removeKey(key, null)
    }

    fun removeKey(key: String, spName: String?) {
        initSp(spName)
        currentSP!!.edit().remove(key).apply()
    }

    /**
     * @param spName 清除SP中的所有内容
     */
    fun clearSp(spName: String) {
        val preferences = spMap.remove(spName)
        preferences?.edit()?.clear()?.apply()
    }

    /**
     * 将对象储存到sharepreference
     *
     * @param key
     * @param device
     * @param <T>
    </T> */
    fun <T> saveObjectBase64(key: String, device: T, spName: String): Boolean {
        initSp(spName)
        val baos = ByteArrayOutputStream()
        try {   //Device为自定义类
            // 创建对象输出流，并封装字节流
            val oos = ObjectOutputStream(baos)
            // 将对象写入字节流
            oos.writeObject(device)
            // 将字节流编码成base64的字符串
            val oAuth_Base64 = String(Base64.encode(baos
                    .toByteArray(), Base64.DEFAULT))
            currentSP!!.edit().putString(key, oAuth_Base64).apply()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    /**
     * 将对象从shareprerence中取出来
     *
     * @param key
     * @param <T>
     * @return
    </T> */
    fun <T> getObjectBase64(key: String, spName: String): T? {
        initSp(spName)
        var device: T? = null
        val productBase64 = currentSP!!.getString(key, null) ?: return null

        // 读取字节
        val base64 = Base64.decode(productBase64.toByteArray(), Base64.DEFAULT)

        // 封装到字节流
        val bais = ByteArrayInputStream(base64)
        try {
            // 再次封装
            val bis = ObjectInputStream(bais)

            // 读取对象
            device = bis.readObject() as T

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return device
    }

    //----------------------------------------------------------
    fun hadLogin(): Boolean {
        return getBoolean(KEY_HAD_LOGIN,false,SP_NAME_LOGIN)
    }

}