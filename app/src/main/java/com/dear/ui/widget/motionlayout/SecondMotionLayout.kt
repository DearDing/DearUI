package com.dear.ui.widget.motionlayout

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.motion.widget.MotionLayout

class SecondMotionLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val PROGRESS_START = 0.0f
        private const val PROGRESS_MIDDLE = 0.4f
        private const val PROGRESS_END = 1.0f
        private const val SCROLL_OFFSET = 50
    }

    private var mStartY = 0f
    private var mEndY = 0f

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return (progress > PROGRESS_START) && (progress < PROGRESS_END)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (progress <= PROGRESS_START) {
            return false
        }
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mStartY = event.y
            }
            MotionEvent.ACTION_UP -> {
                mEndY = event.y
                if (mEndY - mStartY > SCROLL_OFFSET) {
                    //向下滑动
                    handleProgress(getNextStateValue(true))
                    return true
                }
                if (mStartY - mEndY > SCROLL_OFFSET) {
                    //向上滑动
                    handleProgress(getNextStateValue(false))
                    return true
                }
                //判定为点击事件，不作处理
                return false
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getNextStateValue(isDown: Boolean): Float {
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