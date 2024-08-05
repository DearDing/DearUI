package com.dear.ui

import android.app.Application
import com.dear.ui.app.AppHelper
import com.dear.ui.debug.AppBlockCanaryContext
import com.dear.ui.debug.TraceUtil
import com.github.moduth.blockcanary.BlockCanary

class UIApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        BlockCanary.install(this, AppBlockCanaryContext()).start()
        AppHelper.initContext(this)
        TraceUtil.startTrace()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

}