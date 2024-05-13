package com.dear.ui.widget.dropview

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.dear.ui.R

class DropPopupWindow(var mContext: Context) : PopupWindow() {

    companion object{
        private val COLOR_BG:Int = Color.parseColor("#99000000")
    }

    private var mContentLayout: FrameLayout? = null
    private var mContentView: View? = null
    private var onDismissListener: OnDismissListener? = null

    /**
     * 当前是否在执行动画
     */
    private var isAnimRunning = false

    private var params = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

    init {
        mContentLayout = FrameLayout(mContext)
        mContentLayout?.setOnClickListener(View.OnClickListener {
            if (isAnimRunning()) {
                return@OnClickListener
            }
            dismiss()
        })
        animationStyle = 0
        contentView = mContentLayout
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        setBackgroundDrawable(ColorDrawable())
        isOutsideTouchable = true
    }

    override fun setOnDismissListener(onDismissListener: OnDismissListener?) {
        this.onDismissListener = onDismissListener
    }

    fun addContentView(contentView: View) {
        mContentView = contentView
        mContentLayout!!.addView(contentView, params)
    }

    fun removeContentView() {
        mContentLayout?.removeAllViews()
        mContentView = null
    }

    fun hasContentView(): Boolean {
        return mContentView != null
    }

    /**
     * 当前是否在执行动画
     */
    fun isAnimRunning(): Boolean {
        return isAnimRunning
    }

    /**
     * 显示PopupWindow
     * @param anchor 锚点view
     * @param bottomView 布局最底部的view,用于计算popupwindow的高度,不为空
     */
    fun show(anchor: View,bottomView: View) {
        val ps = IntArray(2)
        anchor.getLocationOnScreen(ps)
        val popTop = ps[1] + anchor.height
        val psBottom = IntArray(2)
        val popBottom: Int = run {
            bottomView.getLocationOnScreen(psBottom)
            psBottom[1] + bottomView.height
        }
        val height = popBottom - popTop
        setHeight(height)
        showAtLocation(anchor, Gravity.NO_GRAVITY, 0, popTop + 1)
        if (hasContentView()) {
            val animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.anim_drop_enter
            )
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    isAnimRunning = true
                    mContentLayout?.let {
                        startBackgroundColorAnim(
                            it,
                            300,
                            LinearInterpolator(),
                            mContext.resources.getColor(android.R.color.transparent),
                            COLOR_BG
                        )
                    }
                }

                override fun onAnimationEnd(animation: Animation) {
                    isAnimRunning = false
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            mContentView?.startAnimation(animation)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun startBackgroundColorAnim(view:View, duration:Long, interpolator: Interpolator, startColor:Int, endColor:Int){
        val objectAnimator = ObjectAnimator.ofInt(view, "backgroundColor", startColor, endColor)
        objectAnimator.duration = duration
        objectAnimator.interpolator = interpolator
        objectAnimator.setEvaluator(ArgbEvaluator())
        objectAnimator.start()
    }

    override fun dismiss() {
        if (hasContentView() && isShowing && !isAnimRunning()) {
            //为了保证时序性，所以这里先执行回调onDismiss，然后再执行动画
            onDismissListener?.onDismiss()
            val animation =
                AnimationUtils.loadAnimation(mContext, R.anim.anim_drop_out)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    mContentLayout?.let {
                        startBackgroundColorAnim(
                            it,
                            300,
                            LinearInterpolator(),
                            COLOR_BG,
                            mContext.resources.getColor(android.R.color.transparent),
                        )
                    }
                    isAnimRunning = true
                }

                override fun onAnimationEnd(animation: Animation) {
                    super@DropPopupWindow.dismiss()
                    isAnimRunning = false
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            mContentView?.startAnimation(animation)
        }
    }
}