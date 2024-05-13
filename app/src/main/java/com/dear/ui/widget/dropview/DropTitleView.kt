package com.dear.ui.widget.dropview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.dear.ui.R

class DropTitleView : FrameLayout{

    private lateinit var mTitleView: TextView
    private lateinit var mImageView: ImageView

    private var mIsOpen: Boolean = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ){
        val view = LayoutInflater.from(context).inflate(getLayoutId(), this, false)
        addView(view)
        initView()
    }

    private fun getLayoutId(): Int {
        return R.layout.layout_drop_title_view
    }

    private fun initView() {
        mTitleView = findViewById(R.id.tv_drop_title)
        mImageView = findViewById(R.id.iv_title_arrow)
    }

    fun setText(text: String): DropTitleView {
        mTitleView.text = text
        return this
    }

    fun isOpen():Boolean{
        return mIsOpen
    }

    fun open(){
        mIsOpen = true
        mImageView.setImageResource(R.drawable.svg_arrow_up)
    }

    fun close(){
        mIsOpen = false
        mImageView.setImageResource(R.drawable.svg_arrow_down)
    }

}