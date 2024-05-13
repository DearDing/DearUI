package com.dear.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewStub
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.dear.ui.R

/**
 * 从底部弹出的 Dialog base类
 */
abstract class BaseBottomDialog(context: Context, @StyleRes styleId: Int = R.style.BaseBottomDialogStyle) :
    Dialog(context, styleId) {

    protected var mContext: Context = context
    private var mClBaseRootLayout: ConstraintLayout? = null
    private var mTvBaseTitle: TextView? = null
    private var mIvBaseClose: ImageView? = null
    private var mBaseViewStub: ViewStub? = null

    init {
        setContentView(R.layout.dialog_base_bottom_layout)
        mClBaseRootLayout = findViewById(R.id.cl_base_root_layout)
        mTvBaseTitle = findViewById(R.id.tv_base_title)
        mIvBaseClose = findViewById(R.id.iv_base_close)
        mBaseViewStub = findViewById(R.id.vs_base_content)
        initWindow()
        inflateContentView()
        initTitleLayout()
    }

    open fun initWindow(){
        val window = this.window
        if (window != null) {
            window.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
            window.setWindowAnimations(R.style.AnimBottom)
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun initTitleLayout() {
        if (isHasTitle()) {
            mTvBaseTitle?.visibility = View.VISIBLE
            mIvBaseClose?.visibility = View.VISIBLE
            mTvBaseTitle?.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            mIvBaseClose?.setImageResource(R.drawable.svg_common_close)
            mIvBaseClose?.setOnClickListener {
                onCloseClickEvent()
            }
        } else {
            mTvBaseTitle?.visibility = View.GONE
            mIvBaseClose?.visibility = View.GONE
        }
    }

    /**
     * 关闭按钮点击
     */
    open fun onCloseClickEvent() {
        dismiss()
    }

    /**
     * 设置弹窗背景
     */
    fun setBaseBackground(@DrawableRes drawableId: Int): BaseBottomDialog {
        mClBaseRootLayout?.setBackgroundResource(drawableId)
        return this
    }

    fun setBaseBackgroundColor(@ColorInt colorId: Int): BaseBottomDialog {
        mClBaseRootLayout?.setBackgroundColor(colorId)
        return this
    }

    /**
     * 是否显示标题；默认显示
     */
    open fun isHasTitle(): Boolean {
        return true
    }

    fun setTitle(title: String): BaseBottomDialog {
        mTvBaseTitle?.text = title
        return this
    }

    private fun inflateContentView() {
        mBaseViewStub?.layoutResource = getContentLayoutId()
        mBaseViewStub?.let {
            onViewInflated(it.inflate())
        }
    }

    @LayoutRes
    abstract fun getContentLayoutId(): Int

    abstract fun onViewInflated(view: View)

}