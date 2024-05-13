package com.dear.ui.widget.dropview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.dear.ui.R
import com.dear.ui.utils.convertToInt
import com.dear.ui.utils.formatToDate
import java.util.*
import kotlin.collections.HashMap

/**
 * 下拉筛选view
 */
class DropFilterView : FrameLayout {

    private lateinit var mTitleContainer: LinearLayout
    private lateinit var mViewLine: View
    private var mBottomView: View? = null

    private var mPopupWindow: DropPopupWindow? = null
    private var mTitleMap: HashMap<String, View>? = null
    private var mContentMap: HashMap<String, View>? = null
    private var mCurType: String = ""

    private var mVisibleListener: OnFilterWindowVisibleListener? = null
    private var mTitleListener: OnTitleViewClickListener? = null
    private var mListResultCallback: OnFilterListResultCallback? = null
    private var mDateResultCallback: OnFilterDateResultCallback? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        val view = LayoutInflater.from(context).inflate(getLayoutId(), this, false)
        addView(view)
        initView()
    }

    private fun getLayoutId(): Int {
        return R.layout.layout_drop_filter_view
    }

    private fun initView() {
        mTitleContainer = findViewById(R.id.drop_filter_title_container)
        mViewLine = findViewById(R.id.drop_filter_bottom_line)
    }

    private fun initPopupwindow() {
        mPopupWindow = DropPopupWindow(context)
        mPopupWindow?.setOnDismissListener {
            mTitleMap?.get(mCurType)?.let {
                if (it is DropTitleView) {
                    it.close()
                }
            }
            mVisibleListener?.onFilterVisible(mCurType, false)
        }
    }

    private fun saveViewToMap(type: String, titleView: View, contentView: View) {
        if (null == mTitleMap) {
            mTitleMap = hashMapOf()
        }
        mTitleMap?.put(type, titleView)
        if (null == mContentMap) {
            mContentMap = hashMapOf()
        }
        mContentMap?.put(type, contentView)
    }

    /**
     * 下拉筛选列表 显示/取消 监听
     */
    fun setOnFilterVisibleListener(listener: OnFilterWindowVisibleListener): DropFilterView {
        mVisibleListener = listener
        return this
    }

    /**
     * 筛选titleView点击事件监听
     */
    fun setOnTitleViewClickListener(listener: OnTitleViewClickListener): DropFilterView {
        mTitleListener = listener
        return this
    }

    /**
     * 默认类型-列表样式筛选结果回调
     */
    fun setOnListFilterResultCallback(callback: OnFilterListResultCallback): DropFilterView {
        mListResultCallback = callback
        return this
    }

    /**
     * 默认类型-日期样式筛选结果回调
     */
    fun setOnDateFilterResultCallback(callback: OnFilterDateResultCallback): DropFilterView {
        mDateResultCallback = callback
        return this
    }

    /**
     * 布局里位于最底部的view,用于处理popupwindow的高度
     * 必要性说明：由于Android 手机存在不同屏幕类型，获取到的屏幕高度有偏差，所以用布局里最底部的view，间接获取屏幕显示的高度
     */
    fun setBottomView(bottomView: View): DropFilterView {
        mBottomView = bottomView
        return this
    }

    /**
     * 是否显示titleView底部的分隔线
     */
    fun setLineEnable(isEnable: Boolean): DropFilterView {
        mViewLine.visibility = if (isEnable) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
        return this
    }

    /**
     * 添加自定的titleView 和 自定义的contentView
     */
    fun addCustomView(type: String, titleView: View, contentView: View): DropFilterView {
        val layout = FrameLayout(context)
        val tvParams =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        tvParams.gravity = Gravity.CENTER
        layout.addView(titleView, tvParams)

        val params = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        params.weight = 1f
        layout.tag = type
        layout.setOnClickListener {
            onItemTitleClickEvent(type)
        }
        mTitleContainer.addView(layout, params)
        saveViewToMap(type, titleView, contentView)
        return this
    }

    /**
     * 添加固定样式的titleView 和 自定义样式的contentView
     */
    fun addCustomView(type: String, title: String, contentView: View): DropFilterView {
        val titleView = DropTitleView(context)
        titleView.tag = type
        titleView.setText(title)
        titleView.setOnClickListener {
            onItemTitleClickEvent(type)
        }
        addCustomView(type, titleView, contentView)
        return this
    }

    /**
     * 添加列表筛选样式（单选）
     */
    fun addListView(type: String, title: String, list: List<DialogItemBean>): DropFilterView {
        val titleView = DropTitleView(context)
        titleView.tag = type
        titleView.setText(title)
        titleView.setOnClickListener {
            onItemTitleClickEvent(type)
        }
        val listView = DropListView(context)
        listView.setData(list)
        listView.setItemListClickListener(object : DropListView.OnItemListClickListener {
            override fun onItemClick(bean: DialogItemBean?) {
                mPopupWindow?.dismiss()
                mListResultCallback?.onFilterListResult(type, bean)
            }
        })
        addCustomView(type, titleView, listView)
        return this
    }

    /**
     * 刷新列表样式筛选数据
     */
    fun refreshListView(type: String, list: List<DialogItemBean>) {
        var listView = mContentMap?.get(type)
        if (listView is DropListView) {
            listView.setData(list)
        }
    }

    /**
     * 添加日期类型筛选view
     */
    fun addDateView(type: String, title: String, startTime: String): DropFilterView {
        val titleView = DropTitleView(context)
        titleView.tag = type
        titleView.setText(title)
        titleView.setOnClickListener {
            onItemTitleClickEvent(type)
        }
        val dateView = createDateView(startTime)
        dateView.setDatePickerListener(object : DropDateView.DatePickerListener {
            override fun onDoneClick(startDate: String?, endDate: String?, isAllDate: Boolean) {
                mPopupWindow?.dismiss()
                mDateResultCallback?.onFilterDateResult(mCurType, startDate, endDate, isAllDate)
            }
        })
        addCustomView(type, titleView, dateView)
        return this
    }

    /**
     * 创建日期类型筛选view
     */
    private fun createDateView(startTime: String): DropDateView {
        val dateView = DropDateView(context)
        //开始时间：2023年01月01日
        val startDate = Calendar.getInstance()
        startDate.set(
            (startTime.substring(0, 4)).convertToInt(),
            (startTime.substring(4, 6)).convertToInt() - 1,
            (startTime.substring(6, 8)).convertToInt()
        )
        //结束时间：最新自然日（系统当前时间）
        val endDate = Calendar.getInstance()
        val endTime = "yyyyMMdd".formatToDate(endDate.time)
        //设置日期默认值
        dateView.setDefaultPicker(
            (startTime.substring(0, 4)).convertToInt(),
            (startTime.substring(4, 6)).convertToInt(),
            (startTime.substring(6, 8)).convertToInt(),
            (endTime.substring(0, 4)).convertToInt(),
            (endTime.substring(4, 6)).convertToInt(),
            (endTime.substring(6, 8)).convertToInt()
        )
        dateView.setRangDate(startDate, endDate)
        dateView.setFirstDate(startTime)
        dateView.setLeftPicker(
            (startTime.substring(0, 4)).convertToInt(),
            (startTime.substring(4, 6)).convertToInt(),
            (startTime.substring(6, 8)).convertToInt()
        )
        dateView.setRightPicker(
            (endTime.substring(0, 4)).convertToInt(),
            (endTime.substring(4, 6)).convertToInt(),
            (endTime.substring(6, 8)).convertToInt()
        )
        dateView.setPicker(
            (startTime.substring(0, 4)).convertToInt(),
            (startTime.substring(4, 6)).convertToInt(),
            (startTime.substring(6, 8)).convertToInt()
        )
        dateView.initPickerDate()
        return dateView
    }

    /**
     * TitleView 点击事件统一处理
     */
    private fun onItemTitleClickEvent(type: String) {
        val isInterceptClick: Boolean = mTitleListener?.onTitleViewClickEvent(type) ?: false
        //拦截点击事件后，可以通过主动调用show(type)方法控制筛选框显示时机
        //场景：点击后请求接口，数据回来后再显示筛选框
        if (!isInterceptClick) {
            mBottomView?.let {
                show(type, it)
            }
        }
    }

    /**
     * 设置标题文字显示
     */
    fun setTitleText(type: String, text: String) {
        var titleView = mTitleMap?.get(type)
        if (titleView is DropTitleView) {
            titleView.setText(text)
        }
    }

    /**
     * 显示下拉筛选框
     */
    fun show(type: String, bottomView: View) {
        if (null == mPopupWindow) {
            initPopupwindow()
        }
        if (mPopupWindow?.isAnimRunning() == true) {
            return
        }
        if (mPopupWindow?.isShowing == true) {
            mPopupWindow?.dismiss()
            return
        }
        if (null == mContentMap || mContentMap?.isEmpty() == true) {
            return
        }
        mContentMap?.get(type)?.let { contentView ->
            //处理筛选框显示
            if (mCurType != type && mPopupWindow?.hasContentView() == true) {
                mPopupWindow?.removeContentView()
            }
            if (mPopupWindow?.hasContentView() == false) {
                mPopupWindow?.addContentView(contentView)
            }
            //日期筛选，恢复到选中的日期
            if (contentView is DropDateView) {
                contentView.resetToSelectedDate()
            }
            mPopupWindow?.show(mViewLine, bottomView)
            //处理titleView开启状态
            mTitleMap?.get(type)?.let { titleView ->
                if (titleView is DropTitleView) {
                    titleView.open()
                }
            }
            mVisibleListener?.onFilterVisible(mCurType, true)
        }
        mCurType = type
    }

    /**
     * 筛选框显示/消失监听
     */
    interface OnFilterWindowVisibleListener {

        /**
         * @param type 当前选中的筛选type
         */
        fun onFilterVisible(type: String, isVisible: Boolean)
    }

    /**
     * 头部view点击事件监听
     */
    interface OnTitleViewClickListener {

        /**
         * 拦截点击事件后，可通过主动调用show(type)方法显示弹窗
         * @return true:拦截titleView点击事件；false:不拦截点击事件
         */
        fun onTitleViewClickEvent(type: String): Boolean
    }

    /**
     * 默认的列表样式选择结果回调
     */
    interface OnFilterListResultCallback {

        fun onFilterListResult(type: String, bean: DialogItemBean?)
    }

    /**
     * 默认的日期样式选择结果回调
     */
    interface OnFilterDateResultCallback {

        /**
         * @param isAllDate 选中的是否为全部日期
         */
        fun onFilterDateResult(
            type: String,
            startDate: String?,
            endDate: String?,
            isAllDate: Boolean
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mPopupWindow = null
        mTitleMap?.clear()
        mTitleMap = null
        mContentMap?.clear()
        mContentMap = null
    }

}