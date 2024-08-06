package com.dear.ui.widget.textview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.AlignmentSpan
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.util.SparseBooleanArray
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView


/**
 * 可展开折叠的TextView
 * 支持在recycleView 中复用，调用 setOriginTextWithTag() 方法
 */
class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "ExpandableTextView"
        private const val ELLIPSIS_NORMAL = "\u2026" // (…)
        private const val SUFFIX_SPACE = "\u0020" // 空格
        private const val TAG_INVALID = -1
        private const val DEFAULT_MAX_LINE = 2
    }

    private var mTag: Int = TAG_INVALID
    private val mTagArray: SparseBooleanArray by lazy { SparseBooleanArray() }
    private var mOriginText: String? = null
    private var mOriginSpannableBuilder: SpannableStringBuilder? = null

    private var mSuffixTextColor: Int = -1
    private var mSuffixTextSize: Float = -1f
    private var mOpenSuffixText: String? = null
    private var mSuffixSpaceWidth = 0f //空格宽度
    private var mSuffixSpaceText: String? = null //需要填充的空格
    private var mOpenSuffixDrawable: Drawable? = null
    private var mCloseSuffixText: String? = null
    private var mCloseSuffixDrawable: Drawable? = null
    private var mSuffixSpannableBuilder: SpannableStringBuilder? = null

    /**
     * 默认收起状态
     * false-收起状态；true-展开状态
     */
    private var mExpandableState: Boolean = false
    private var mMaxExpandableLines: Int = DEFAULT_MAX_LINE

    /**
     * 是否一直展示"展开"、"收起"
     */
    private var mIsForceExpand: Boolean = false

    private var mChangedListener: OnTextExpandStateChangedListener? = null

    init {
        //去掉设置ClickSpan后点击背景色
        highlightColor = Color.TRANSPARENT
        movementMethod = LinkMovementMethod()
        //addOnAttachStateChangeListener(this)
    }

    /**
     * 监听折叠展开状态变化
     */
    fun setOnTextExpandChangedListener(listener: OnTextExpandStateChangedListener): ExpandableTextView {
        mChangedListener = listener
        return this
    }

    /**
     * @param color
     */
    fun setSuffixTextColor(@ColorInt color: Int): ExpandableTextView {
        mSuffixTextColor = color
        return this
    }

    /**
     * @param size 单位px
     */
    fun setSuffixTextSize(size: Float): ExpandableTextView {
        mSuffixTextSize = size
        return this
    }

    /**
     * 设置拼接图片
     */
    fun setSuffixImageResource(
        @DrawableRes openSuffixImgId: Int,
        @DrawableRes closeSuffixImgId: Int
    ): ExpandableTextView {
        mOpenSuffixDrawable = getResourceDrawable(openSuffixImgId)
        mCloseSuffixDrawable = getResourceDrawable(closeSuffixImgId)
        return this
    }

    /**
     * 设置拼接图片
     */
    fun setSuffixImageDrawable(
        openSuffixDrawable: Drawable?,
        closeSuffixDrawable: Drawable?
    ): ExpandableTextView {
        mOpenSuffixDrawable = openSuffixDrawable
        mOpenSuffixDrawable?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
        }
        mCloseSuffixDrawable = closeSuffixDrawable
        mCloseSuffixDrawable?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
        }
        return this
    }

    /**
     * 设置拼接文本
     * @param openSuffixText 展示状态下文本
     * @param closeSuffixText 收起状态下文本
     */
    fun setSuffixText(openSuffixText: String, closeSuffixText: String): ExpandableTextView {
        mOpenSuffixText = openSuffixText
        mCloseSuffixText = closeSuffixText
        return this
    }

    /**
     * 设置折叠情况下最大行数
     */
    fun setMaxExpandableLines(lines: Int): ExpandableTextView {
        if (lines <= 0) {
            return this
        }
        mMaxExpandableLines = lines
        maxLines = lines
        return this
    }

    /**
     * 通过tag记录展开折叠状态，避免复用时状态不一致
     * 支持复用
     */
    fun setOriginTextWithTag(txt: String, tag: Int) {
        mTag = tag
        mExpandableState = if (mTagArray.indexOfKey(tag) < 0) {
            mTagArray.put(tag, false)
            false
        } else {
            mTagArray.get(tag)
        }
        setOriginText(txt)
    }

    /**
     * 设置文本
     * @param expandableState : false-收起状态 ；true-展开状态
     */
    fun setOriginText(txt: String, expandableState: Boolean) {
        mExpandableState = expandableState
        setOriginText(txt)
    }

    /**
     * 设置文本
     */
    fun setOriginText(txt: String) {
        if (isAttachedToWindow && width > 0) {
            setRealText(txt)
        } else {
            postSetRealText(txt)
        }
    }

    private fun postSetRealText(txt: String) {
        post {
            setRealText(txt)
        }
    }

    private fun setRealText(txt: String) {
        Log.i(TAG,"width = $width")
        if (TextUtils.isEmpty(txt)) {
            //空文本
            mOriginText = txt
            text = mOriginText
            return
        }
        val isSameText = (mOriginText == txt)
        if (isSameText && null == mOriginSpannableBuilder && null == mSuffixSpannableBuilder) {
            //文本相同,不可折叠
            text = mOriginText
            return
        }
        if (isSameText && null != mOriginSpannableBuilder && null != mSuffixSpannableBuilder) {
            //文本相同，可折叠
            setState(mExpandableState)
            return
        }
        mOriginText = txt
        val layout = createDynamicLayout(mOriginText ?: "")
        val lineCount = layout.lineCount
        val maxExpandableLine = mMaxExpandableLines
        if (!mIsForceExpand && (lineCount <= 0 || maxExpandableLine < 0 || maxExpandableLine >= lineCount)) {
            //不超过 maxLines,显示原文，不折叠
            text = mOriginText
            mOriginSpannableBuilder = null
            mSuffixSpannableBuilder = null
            return
        }
        //展开文本
        mOriginSpannableBuilder = buildOriginSpannable()
        //截取折叠文本
        mSuffixSpannableBuilder = buildExpandableSpannable(layout, lineCount, maxExpandableLine)
        setState(mExpandableState)
    }

    /**
     * 折叠状态
     * 创建 builder
     */
    private fun buildExpandableSpannable(
        layout: DynamicLayout,
        lineCount: Int,
        maxExpandableLine: Int
    ): SpannableStringBuilder {
        val endIndex = calculateEndIndex(
            layout,
            lineCount,
            maxExpandableLine,
            mCloseSuffixText ?: "",
            mCloseSuffixDrawable
        )
        val expandableText = subString(mOriginText, 0, endIndex)
        val builder = SpannableStringBuilder(expandableText)
        if (endIndex < (mOriginText?.length ?: 0)) {
            builder.append(ELLIPSIS_NORMAL)
        }
        mSuffixSpaceText?.let {
            builder.append(it)
        }
        val suffixSpan = if(mCloseSuffixDrawable == null){
            SpannableString("$mCloseSuffixText")
        }else{
            SpannableString(" $mCloseSuffixText")
        }
        //添加图片
        mCloseSuffixDrawable?.let {
            val imageSpan = CustomImageSpan(it)
            suffixSpan.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        suffixSpan.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                openText()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = getSuffixTextColor()
                ds.textSize = getSuffixTextSize()
            }

        }, 0, suffixSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append(suffixSpan)
        return builder
    }

    /**
     * 展开状态
     * 创建 builder
     */
    private fun buildOriginSpannable(): SpannableStringBuilder {
        val builder = SpannableStringBuilder(mOriginText)
        if (TextUtils.isEmpty(mOpenSuffixText)) {
            return builder
        }
        val suffixSpan = SpannableString(" $mOpenSuffixText")
        //添加图片
        mOpenSuffixDrawable?.let {
            val imageSpan = CustomImageSpan(it)
            suffixSpan.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        suffixSpan.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    closeText()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = getSuffixTextColor()
                    ds.textSize = getSuffixTextSize()
                }

            },
            suffixSpan.length - (mOpenSuffixText?.length ?: 0),
            suffixSpan.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        suffixSpan.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE),
            0,
            1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //添加文本
        builder.append("\n").append(suffixSpan)
        return builder
    }

    private fun getSuffixTextColor(): Int {
        return if (mSuffixTextColor == -1) {
            paint.color
        } else {
            mSuffixTextColor
        }
    }

    private fun getSuffixTextSize(): Float {
        return if (mSuffixTextSize == -1f) {
            paint.textSize
        } else {
            mSuffixTextSize
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getResourceDrawable(@DrawableRes drawableId: Int): Drawable? {
        var drawablePic: Drawable? = null
        try {
            drawablePic = context.getDrawable(drawableId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        drawablePic?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
        }
        return drawablePic
    }

    /**
     * 计算最大显示文本位置
     * @return 截取文本最后的位置
     */
    private fun calculateEndIndex(
        layout: DynamicLayout,
        lineCount: Int,
        maxExpandableLine: Int,
        suffixText: String,
        suffixDrawable: Drawable?
    ): Int {
        val maxLength = mOriginText?.length ?: 0
        if (lineCount in 0 until maxExpandableLine) {
            return maxLength
        }
        val suffixWidth =
            calculateSuffixTextWidth(suffixText) + calculateSuffixDrawableWidth(suffixDrawable)
        val lineWidth = layout.getLineWidth(maxExpandableLine - 1)
        if ((lineWidth + suffixWidth) <= width) {
            //正文可以正常展示
            return maxLength
        }
        val suffixEllipsisWidth = calculateSuffixTextWidth(ELLIPSIS_NORMAL)
        val indexStart = layout.getLineStart(maxExpandableLine - 1)
        val indexEnd = layout.getLineEnd(maxExpandableLine - 1)
        var subEndIndex: Int = indexEnd
        var lastLineText = subString(mOriginText, indexStart, subEndIndex)
        var totalWidth: Float = suffixWidth + suffixEllipsisWidth + paint.measureText(lastLineText)
        while (totalWidth > width && subEndIndex > 0) {
            subEndIndex -= 1
            lastLineText = subString(mOriginText, indexStart, subEndIndex)
            totalWidth = suffixWidth + suffixEllipsisWidth + paint.measureText(lastLineText)
        }
        if (subEndIndex <= 0) {
            return indexEnd
        }
        //计算需要填充的空格
        val spaceCount: Int = ((width - totalWidth) / calculateSpaceWidth()).toInt()
        if (spaceCount > 0) {
            val spaceBuilder = StringBuilder()
            repeat(spaceCount) {
                spaceBuilder.append(SUFFIX_SPACE)
            }
            mSuffixSpaceText = spaceBuilder.toString()
        }
        return subEndIndex
    }

    /**
     * 截取字符串
     */
    private fun subString(text: String?, start: Int, end: Int): String {
        if (TextUtils.isEmpty(text) || start > end) {
            return text ?: ""
        }
        val size = text?.length ?: 0
        if (start in 0..size && end in 0..size) {
            return text?.substring(start, end) ?: ""
        }
        return text ?: ""
    }

    /**
     * 计算图标宽度
     */
    private fun calculateSuffixDrawableWidth(suffixDrawable: Drawable?): Int {
        suffixDrawable?.let {
            return it.bounds.width()
        }
        return 0
    }

    /**
     * 计算文本宽度
     */
    private fun calculateSuffixTextWidth(suffixText: String): Float {
        if (TextUtils.isEmpty(suffixText)) {
            return 0f
        }
        val suffixPaint = paint
        suffixPaint.textSize = getSuffixTextSize()
        return suffixPaint.measureText(suffixText)
    }

    /**
     * 计算一个空格的宽度
     */
    private fun calculateSpaceWidth(): Float {
        if (mSuffixSpaceWidth == 0f) {
            mSuffixSpaceWidth = paint.measureText(" ")
        }
        return mSuffixSpaceWidth
    }

    /**
     * 创建 DynamicLayout
     */
    private fun createDynamicLayout(text: String): DynamicLayout {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            createDynamicLayoutAfterP(text)
        } else {
            createDynamicLayoutBeforeP(text)
        }
    }

    private fun createDynamicLayoutBeforeP(text: String): DynamicLayout {
        val textWidth = width - paddingLeft - paddingRight
        return DynamicLayout(
            text,
            paint,
            textWidth,
            Layout.Alignment.ALIGN_NORMAL,
            1.2f,
            0.0f,
            includeFontPadding
        )
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun createDynamicLayoutAfterP(text: String): DynamicLayout {
        val textWidth = width - paddingLeft - paddingRight
        val builder = DynamicLayout.Builder.obtain(text, paint, textWidth)
        builder.setAlignment(Layout.Alignment.ALIGN_NORMAL)
        builder.setIncludePad(includeFontPadding)
        return builder.build()
    }

    /**
     * 不论文本是否可以完全展示
     * 是否一直显示"展开"、"收起"
     */
    fun setForceExpandEnable(isEnable: Boolean): ExpandableTextView {
        mIsForceExpand = isEnable
        return this
    }

    /**
     * 获取展开折叠状态
     * false-收起状态；true-展开状态
     */
    fun getExpandableState(): Boolean {
        return mExpandableState
    }

    /**
     * 切换展开/收起状态
     */
    fun toggleExpandableState() {
        setState(!mExpandableState)
    }

    /**
     * 设置展开收起状态
     *  @param expandable ：false-收起状态；true-展开状态
     */
    fun setState(expandable: Boolean) {
        if (expandable) {
            openText()
        } else {
            closeText()
        }
    }

    private fun openText() {
        if (mTag != TAG_INVALID) {
            mTagArray.put(mTag, true)
        }
        mExpandableState = true
        maxLines = Int.MAX_VALUE
        mOriginSpannableBuilder?.let {
            text = it
        }
        mChangedListener?.onTextExpandStateChanged(true)
    }

    private fun closeText() {
        if (mTag != TAG_INVALID) {
            mTagArray.put(mTag, false)
        }
        mExpandableState = false
        maxLines = mMaxExpandableLines
        mSuffixSpannableBuilder?.let {
            text = it
        }
        mChangedListener?.onTextExpandStateChanged(false)
    }

    override fun hasOverlappingRendering(): Boolean {
        return false
    }

    /**
     * 展开/折叠 状态监听
     */
    interface OnTextExpandStateChangedListener {
        /**
         * @param isExpand true-展开 ； false-折叠
         */
        fun onTextExpandStateChanged(isExpand: Boolean)
    }
}