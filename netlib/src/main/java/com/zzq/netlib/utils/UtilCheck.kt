package com.zzq.netlib.utils

import java.io.File

/**
 *@auther tangedegushi
 *@creat 2018/11/2
 *@Decribe
 */
object UtilCheck {

    fun isNull(o: Any?): Boolean {
        return o == null
    }

    fun isNotNull(o: Any): Boolean {
        return !isNull(o)
    }

    fun isEmpty(str: CharSequence): Boolean {
        return isNull(str) || str.isEmpty()
    }

    fun isNotEmpty(str: CharSequence): Boolean {
        return !isEmpty(str)
    }

    fun isEmpty(file: File): Boolean {
        return isNull(file) || !file.exists()
    }

    fun isNotEmpty(file: File): Boolean {
        return !isEmpty(file)
    }

    fun isEmpty(os: Array<Any>): Boolean {
        return isNull(os) || os.isEmpty()
    }

    fun isEmpty(l: Collection<*>): Boolean {
        return isNull(l) || l.isEmpty()
    }

    fun isNotEmpty(l: Collection<*>): Boolean {
        return !isEmpty(l)
    }

    fun isEmpty(m: Map<*, *>): Boolean {
        return isNull(m) || m.isEmpty()
    }

    fun isNotEmpty(m: Map<*, *>): Boolean {
        return !isEmpty(m)
    }

    fun isNumeric(str: String): Boolean {
        if (UtilCheck.isEmpty(str)) {
            return false
        }
        return str.matches("[0-9]*".toRegex())
    }

    fun <T> checkNotNull(reference: T?): T {
        return if (reference == null) {
            throw NullPointerException()
        } else {
            reference
        }
    }

    fun <T> checkNotNull(reference: T?, errorMessage: Any?): T {
        return reference ?: throw NullPointerException(errorMessage.toString())
    }

    fun <T> checkNotNull(reference: T?, errorMessageTemplate: String?, vararg errorMessageArgs: Any): T {
        return reference
                ?: throw NullPointerException(format(errorMessageTemplate, *errorMessageArgs))
    }

    internal fun format(template: String?, vararg args: Any): String {
        var template = template
        template = template.toString()
        val builder = StringBuilder(template.length + 16 * args.size)
        var templateStart = 0

        var i = 0
        var placeholderStart: Int
        while (i < args.size) {
            placeholderStart = template.indexOf("%s", templateStart)
            if (placeholderStart == -1) {
                break
            }

            builder.append(template.substring(templateStart, placeholderStart))
            builder.append(args[i++])
            templateStart = placeholderStart + 2
        }

        builder.append(template.substring(templateStart))
        if (i < args.size) {
            builder.append(" [")
            builder.append(args[i++])

            while (i < args.size) {
                builder.append(", ")
                builder.append(args[i++])
            }

            builder.append(']')
        }

        return builder.toString()
    }

}