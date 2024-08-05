package com.dear.ui.widget


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import com.dear.ui.R
import kotlin.math.cos
import kotlin.math.sin

/**
 * 水印view
 */
class WatermarkView: View {

    private var mTextPaint: Paint? = null
    private var mTextSize: Float = 28f
    private var mColorId: Int = R.color.theme_color

    private var mSpaceV: Float = 200f //纵向间距
    private var mSpaceH: Float = 160f //横向间距
    private val mStartX: Float = mSpaceV // 第一条水印x位置
    private val mStartY: Float = mSpaceH // 第一条水印y位置
    private var mMarkText: String = ""
    private var mTextWidth: Float = 0f
    private val mAngle30:Double = 30 * Math.PI / 180

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun initPaint() {
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint?.color = context.getColor(mColorId)
        mTextPaint?.style = Paint.Style.FILL
        mTextPaint?.textSize = mTextSize
    }

    fun setMarkText(text: String): WatermarkView {
        mMarkText = text
        return this
    }

    fun setMarkTextSize(textSize:Float): WatermarkView {
        mTextSize = textSize
        getPaint().textSize = mTextSize
        return this
    }

    fun setMarkColor(@ColorRes id:Int): WatermarkView {
        mColorId = id
        getPaint().color = context.getColor(mColorId)
        return this
    }

    fun draw() {
        postInvalidate()
    }

    private fun getPaint(): Paint {
        if (null == mTextPaint) {
            initPaint()
        }
        return mTextPaint!!
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.save()
            canvasRotate(it)
            drawWatermarkText(it)
            it.restore()
        }
    }

    private fun canvasRotate(canvas: Canvas){
        //先平移，再旋转才不会有空白，使整个图片充满
        val dx = sin(mAngle30) * height
        val dy = sin(mAngle30) * dx
        canvas.translate(-dx.toFloat(), dy.toFloat())
        canvas.rotate(-30f)
    }

    private fun drawWatermarkText(canvas: Canvas) {
        if (TextUtils.isEmpty(mMarkText)) {
            return
        }
        mTextWidth = getPaint().measureText(mMarkText)
        //纵向循环次数
        var count = 1
        var x: Float = mStartX
        var y: Float = mStartY
        val maxX = sin(mAngle30) * height + cos(mAngle30) * width
        val maxY = cos(mAngle30) * height + sin(mAngle30) * width
        while (y <= maxY) {
            canvas.drawText(mMarkText, x, y, getPaint())
            if (x > maxX) {
                count += 1
                //重置x位置，并换行
                x = mSpaceH * (count % 2)
                y += mSpaceV
            } else {
                x += mSpaceH + mTextWidth
            }
        }
    }
}