package com.zzq.netlib.utils

import android.content.Context
import android.os.Environment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 *@auther tangedegushi
 *@creat 2018/11/2
 *@Decribe
 */
object UtilFile {

    fun getCacheFile(context: Context): File {
        return when {
            Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) -> {
                var cacheFlie = context.externalCacheDir
                if (cacheFlie == null) {
                    cacheFlie = File(getCacheFilePath(context))
                    makeDirs(cacheFlie)
                }
                cacheFlie
            }
            else -> context.cacheDir
        }
    }

    /**
     * 获取自定义缓存文件地址
     *
     * @param context
     * @return
     */
    private fun getCacheFilePath(context: Context): String {
        val packageName = context.packageName
        return "/mnt/sdcard/$packageName"
    }

    /**
     * 创建未存在的文件夹
     *
     * @param file
     * @return
     */
    fun makeDirs(file: File): File {
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    /**
     * 使用递归获取目录文件大小
     *
     * @param dir
     * @return
     */
    fun getDirSize(dir: File?): Long {
        if (dir == null) {
            return 0
        }
        if (!dir.isDirectory) {
            return 0
        }
        var dirSize: Long = 0
        val files = dir.listFiles()
        for (file in files!!) {
            if (file.isFile) {
                dirSize += file.length()
            } else if (file.isDirectory) {
                dirSize += file.length()
                dirSize += getDirSize(file) // 递归调用继续统计
            }
        }
        return dirSize
    }

    /**
     * 使用递归删除文件夹
     *
     * @param dir
     * @return
     */
    fun deleteDir(dir: File?): Boolean {
        if (dir == null) {
            return false
        }
        if (!dir.isDirectory) {
            return false
        }
        val files = dir.listFiles()
        for (file in files!!) {
            if (file.isFile) {
                file.delete()
            } else if (file.isDirectory) {
                deleteDir(file) // 递归调用继续删除
            }
        }
        return true
    }

    @Throws(IOException::class)
    fun byteToString(`in`: InputStream): String {
        val out = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        var read = -1
        `in`.use { input ->
            out.use { output ->
                while (input.read(buf).also { read =it } != -1) {
                    output.write(buf,0,read)
                }
            }
        }
        return out.toString()
    }


}