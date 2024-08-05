package com.dear.ui.fragment

import android.util.Log
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.os.bundleOf
import com.dear.ui.activity.AbsVideoFeedActivity
import com.dear.ui.activity.VideoMotionActivity
import com.dear.ui.databinding.FragmentItemContentLayoutBinding

class ItemContentFragment: BaseFragment<FragmentItemContentLayoutBinding>() ,MotionLayout.TransitionListener{

    companion object {

        const val PARAM_POS = "param_position"

        @JvmStatic
        fun getInstance(position: Int=0): ItemContentFragment {
            val fragment = ItemContentFragment()
            fragment.arguments = bundleOf(Pair(PARAM_POS, position))
            return fragment
        }
    }

    private var mPosition = -1

    override fun initParams() {
        mPosition = arguments?.getInt(PARAM_POS) ?: -1
    }

    override fun initView() {
        mDB.viewExit.setOnClickListener {
            hideContentFragment()
        }
        mDB.motionLayout.addTransitionListener(this)
    }

    fun showMiddleContent(){
        mDB.contentLayout.post {
            mDB.motionLayout.showMiddle()
        }
    }

    override fun onTransitionStarted(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int
    ) {

    }

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) {
        Log.i("progress ","onTransitionChange  progress = ${mDB.motionLayout.progress}")
    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
        Log.i("progress ","onTransitionCompleted  progress = ${mDB.motionLayout.progress}")
        if(mDB.motionLayout.progress == 0.0f){
            hideContentFragment()
        }
    }

    override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {
        Log.i("progress ","onTransitionTrigger  progress = ${mDB.motionLayout.progress}")
    }

    fun hideContentFragment(){
        getMotionActivity()?.setContentLayoutVisible(false)
    }

    fun getMotionActivity():AbsVideoFeedActivity?{
        if(activity is AbsVideoFeedActivity){
            return activity as AbsVideoFeedActivity
        }
        return null
    }
}