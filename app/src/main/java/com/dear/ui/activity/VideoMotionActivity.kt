package com.dear.ui.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import com.dear.ui.R

/**
 * 视频-竖屏
 */
class VideoMotionActivity : AbsVideoFeedActivity() {

    companion object{
        private const val SCREEN_ORIENTATION_PARAM = "screen_orientation"

        private const val LANDSCAPE = "landscape"
        private const val PORTRAIT = "portrait"
    }

    private lateinit var mViewLayer: View
    private lateinit var mIvSwitch: View

    private var currentState:String = PORTRAIT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState?.containsKey(SCREEN_ORIENTATION_PARAM) == true){
            currentState = savedInstanceState.getString(SCREEN_ORIENTATION_PARAM,PORTRAIT) ?:PORTRAIT
        }
        Log.i("VideoMotionActivity","VideoMotionActivity -- 执行了 onCreate 方法  currentState = $currentState")
    }

    override fun initViewLayer() {
        mDB.viewStubLayer.layoutResource = R.layout.layout_video_view_layer
        mViewLayer = mDB.viewStubLayer.inflate()
        mIvSwitch = mViewLayer.findViewById(R.id.iv_switch)
        mIvSwitch.setOnClickListener {
            currentState = if(currentState == PORTRAIT){
                LANDSCAPE
            }else{
                PORTRAIT
            }
            rotateScreenOrientation(currentState)
        }
    }

    /**
     * 旋转屏幕方向
     *
     * @param orientation
     */
    private fun rotateScreenOrientation(orientation:String) {
        when (orientation) {
            LANDSCAPE -> requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            PORTRAIT -> requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SCREEN_ORIENTATION_PARAM,currentState)
    }

}