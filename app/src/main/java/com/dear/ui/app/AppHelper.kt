package com.dear.ui.app

import android.content.Context

object AppHelper {

    var applicationContext: Context? = null

    fun initContext(context:Context){
        applicationContext = context
    }

    fun appContext():Context?{
        return applicationContext
    }
}