package com.dear.ui.debug

import android.os.Debug
import android.os.SystemClock
import android.util.Log
import com.dear.ui.app.AppHelper
import java.io.File

/**
 * 统计方法耗时
 */
object TraceUtil {

    fun startTrace() {
        val file = File(AppHelper.appContext()?.getExternalFilesDir("android"), "methods${System.currentTimeMillis()}.trace")
        Log.i("TraceUtil", file.absolutePath)
        Debug.startMethodTracing(file.absolutePath, 100 * 1024 * 1024)
    }

    fun stopTrace() {
        Debug.stopMethodTracing()
    }

}