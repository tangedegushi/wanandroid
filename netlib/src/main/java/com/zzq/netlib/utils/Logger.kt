package com.zzq.netlib.utils

/**
 *@auther tangedegushi
 *@creat 2018/11/2
 *@Decribe
 */
class Logger private constructor(name: String) {
    val tag = "tangedegushi"
    private val logFlag = true
    private val logLevel = android.util.Log.VERBOSE
    private var who = name
    private val MAX_LENGTH = 4000

    companion object {
        private var log: Logger? = null
        private var whoLog: Logger? = null
        private var zzqLog: Logger? = null
        private val DEFAULT = "[default]"
        private val ZZQ = "[zzq]"
        private val WHO = "[who]"

        fun Log(): Logger {
            if (log == null) {
                log = Logger(DEFAULT)
            }
            return log as Logger
        }

        fun zzqLog(): Logger {
            if (zzqLog == null) {
                zzqLog = Logger(ZZQ)
            }
            return zzqLog as Logger
        }

        fun whoLog(): Logger {
            if (whoLog == null) {
                whoLog = Logger(WHO)
            }
            return whoLog as Logger
        }
    }

    /**
     * Get The Current Function Name
     *
     * @return
     */
    private fun getFunctionName(): String? {
        val sts = Thread.currentThread().stackTrace ?: return null
        for (st in sts) {
            if (st.isNativeMethod) {
                continue
            }
            if (st.className == Thread::class.java.name) {
                continue
            }
            if (st.className == this.javaClass.name) {
                continue
            }
            return who + "  " + st.toString() + " :  "
            //            return mClassName + "[ " + Thread.currentThread().getName() + ": " + st.getFileName() + ":"
            //                    + st.getClassName() + ":" + st.getMethodName() + ":" + st.getLineNumber() + " ]";
        }
        return null
    }

    /**
     * The Log Level:i
     *
     * @param str
     */
    fun i(str: Any) {
        if (logFlag) {
            if (logLevel <= android.util.Log.INFO) {
                val name = getFunctionName()
                if (name != null) {
                    // Log.i(tag, name + " - " + str);
                    printLog(android.util.Log.INFO, "$name - $str")
                } else {
                    // Log.i(tag, str.toString());
                    printLog(android.util.Log.INFO, str.toString())
                }
            }
        }

    }

    /**
     * The Log Level:d
     *
     * @param str
     */
    fun d(str: Any) {
        if (logFlag) {
            if (logLevel <= android.util.Log.DEBUG) {
                val name = getFunctionName()
                if (name != null) {
                    // Log.d(tag, name + " - " + str);
                    printLog(android.util.Log.DEBUG, "$name - $str")
                } else {
                    // Log.d(tag, str.toString());
                    printLog(android.util.Log.DEBUG, str.toString())
                }
            }
        }
    }

    /**
     * The Log Level:V
     *
     * @param str
     */
    fun v(str: Any) {
        if (logFlag) {
            if (logLevel <= android.util.Log.VERBOSE) {
                val name = getFunctionName()
                if (name != null) {
                    // Log.v(tag, name + " - " + str);
                    printLog(android.util.Log.VERBOSE, "$name - $str")
                } else {
                    // Log.v(tag, str.toString());
                    printLog(android.util.Log.VERBOSE, str.toString())
                }
            }
        }
    }

    /**
     * The Log Level:w
     *
     * @param str
     */
    fun w(str: Any) {
        if (logFlag) {
            if (logLevel <= android.util.Log.WARN) {
                val name = getFunctionName()
                if (name != null) {
                    // Log.w(tag, name + " - " + str);
                    printLog(android.util.Log.WARN, "$name - $str")
                } else {
                    // Log.w(tag, str.toString());
                    printLog(android.util.Log.WARN, str.toString())
                }
            }
        }
    }

    /**
     * The Log Level:e
     *
     * @param str
     */
    fun e(str: Any) {
        if (logFlag) {
            if (logLevel <= android.util.Log.ERROR) {
                val name = getFunctionName()
                if (name != null) {
                    // Log.e(tag, name + " - " + str);
                    printLog(android.util.Log.ERROR, "$name - $str")
                } else {
                    // Log.e(tag, str.toString());
                    printLog(android.util.Log.ERROR, str.toString())
                }
            }
        }
    }

    /**
     * The Log Level:e
     *
     * @param ex
     */
    fun e(ex: Exception) {
        if (logFlag) {
            if (logLevel <= android.util.Log.ERROR) {
                android.util.Log.e(tag, "error", ex)
            }
        }
    }

    /**
     * The Log Level:e
     *
     * @param log
     * @param tr
     */
    fun e(log: String, tr: Throwable) {
        if (logFlag) {
            val line = getFunctionName()
            android.util.Log.e(tag, "{Thread:" + Thread.currentThread().name + "}" + "[" + who + line
                    + ":] " + log + "\n", tr)
        }
    }

    /***
     *
     * printLog:因为logcat输出log的字符长度是4k，超过长度字符串会被丢弃，所以对超过4k长度的log做分段输出. <br></br>
     *
     * @author mozk
     * @param level
     * @param logText
     */
    fun printLog(level: Int, logText: String) {
        var logText = logText
        var index = 0
        var sub: String

        logText = logText.trim { it <= ' ' }

        while (index < logText.length) {
            // java的字符不允许指定超过总的长度
            if (logText.length <= index + MAX_LENGTH) {
                sub = logText.substring(index)
            } else {
                sub = logText.substring(index, index + MAX_LENGTH)
            }

            index += MAX_LENGTH

            when (level) {
                android.util.Log.INFO -> android.util.Log.i(tag, sub.trim { it <= ' ' })
                android.util.Log.DEBUG -> android.util.Log.d(tag, sub.trim { it <= ' ' })
                android.util.Log.ERROR -> android.util.Log.e(tag, sub.trim { it <= ' ' })
                android.util.Log.WARN -> android.util.Log.w(tag, sub.trim { it <= ' ' })
                android.util.Log.VERBOSE -> android.util.Log.v(tag, sub.trim { it <= ' ' })
                else -> android.util.Log.e(tag, sub.trim { it <= ' ' })
            }
        }
    }

}