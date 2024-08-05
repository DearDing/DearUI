package com.dear.ui.widget.motionlayout

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.motion.widget.MotionLayout

class DragMotionLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val PROGRESS_START = 0f
        private const val PROGRESS_MIDDLE = 0.5f
        private const val PROGRESS_END = 1f
        private const val STATE_MOVE_OFFSET = 50
    }

    private var mTouchStartY = 0f
    private var mTouchEndY = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.actionMasked){
            MotionEvent.ACTION_DOWN->{
                mTouchStartY = event.y
            }
            MotionEvent.ACTION_UP->{
                mTouchEndY = event.y
                if((mTouchStartY - mTouchEndY)>STATE_MOVE_OFFSET){
                    //向上滑动
                    handleProgress(getNextProgress(false))
                }else if((mTouchEndY - mTouchStartY)>STATE_MOVE_OFFSET){
                    //向下滑动
                    handleProgress(getNextProgress(true))
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getNextProgress(isDown: Boolean): Float {
        return if (progress > PROGRESS_MIDDLE) {
            if (isDown) {
                PROGRESS_MIDDLE
            } else {
                PROGRESS_END
            }
        } else if (progress > PROGRESS_START) {
            if (isDown) {
                PROGRESS_START
            } else {
                PROGRESS_MIDDLE
            }
        } else {
            PROGRESS_START
        }
    }

    private fun handleProgress(nextProgress: Float) {
        if (progress == nextProgress) {
            return
        }
        val animator = ValueAnimator.ofFloat(progress, nextProgress)
        animator.duration = 300
        animator.addUpdateListener {
            progress = it.animatedValue as Float
        }
        animator.start()
    }

    fun showMiddle() {
        if (isAttachedToWindow && progress == PROGRESS_START) {
            handleProgress(PROGRESS_MIDDLE)
        }
    }

    fun hide() {
        if (isAttachedToWindow && progress != PROGRESS_START) {
            handleProgress(PROGRESS_START)
        }
    }

}