package com.oasys.digihealth.doctor.utils

import android.util.Log

object LogUtils {

    fun logD(tag: String?, msg: String?) {
        if (msg != null) {
            Log.d(tag, msg)
        }
    }

    fun logE(tag: String?, msg: String?) {
        if (msg != null) {
            Log.e(tag, msg)
        }
    }

    fun logI(tag: String?, msg: String?) {
        if (msg != null) {
            Log.i(tag, msg)
        }
    }

    fun logW(tag: String?, msg: String?) {
        if (msg != null) {
            Log.w(tag, msg)
        }
    }

    fun print(msg: String) {
        println(msg + "")
    }
}