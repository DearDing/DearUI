package com.dear.ui

import android.app.Application
import com.github.moduth.blockcanary.BlockCanary

class UIApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        BlockCanary.install(this, AppBlockCanaryContext()).start()
    }
}