package com.dear.ui.utils

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.dear.ui.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

fun String.showToast(context: Context){
    if(!TextUtils.isEmpty(this)){
        Toast.makeText(context,this,Toast.LENGTH_SHORT).show()
    }
}

fun String.convertToInt():Int{
    var text = this
    if (!TextUtils.isEmpty(text) && !"null".equals(
            text,
            ignoreCase = true
        ) && "--" != text && "- -" != text
    ) {
        text = text.trim { it <= ' ' }
        if (text.indexOf(".") > 0) {
            text = text.substring(0, text.indexOf("."))
        }
        try {
            return this.toInt()
        } catch (e: NumberFormatException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }
    return 0
}

fun String.formatToDate(date:Date):String{
    val sdf = SimpleDateFormat(this, Locale.CHINA)
    sdf.timeZone = TimeZone.getTimeZone("GMT+8")
    return sdf.format(date)
}