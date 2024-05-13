package com.dear.ui.widget.dropview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.dear.ui.R
import com.dear.ui.utils.convertToInt
import com.dear.ui.utils.formatToDate
import com.dear.ui.utils.showToast
import com.dear.ui.widget.DatePickerView
import java.text.SimpleDateFormat
import java.util.*

/**
 * 下拉筛选
 * 日期筛选view
 */
class DropDateView : FrameLayout, DatePickerView.ISelectTimeCallback {

    companion object {
        private var mTimePatternFrom: TimeStringPatternFromUtils? = null
    }

    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView
    private lateinit var timePicker: DatePickerView
    private lateinit var tvFirstDate: TextView
    private lateinit var resetButton: TextView
    private lateinit var positiveButton: TextView
    private lateinit var viewStartLine: View
    private lateinit var viewEndLine: View

    //最早支持时间
    private var firstDate: String? = null
    private var startDate: Calendar? = null
    private var startDateStr: String? = null
    private var endDate: Calendar? = null
    private var endDateStr: String? = null

    //默认日期
    private var leftYear: Int = 1993
    private var leftMonth: Int = 12
    private var leftDay: Int = 3
    private var rightYear: Int = 1993
    private var rightMonth: Int = 12
    private var rightDay: Int = 3

    //重置的默认日期
    private var defalutLeftYear: Int = 1993
    private var defaluLteftMonth: Int = 12
    private var defalutLeftDay: Int = 3
    private var defaultRightYear: Int = 1993
    private var defaultRightMonth: Int = 12
    private var defaultRightDay: Int = 3

    //选中日期
    private var mSelectStartDate: String? = ""
    private var mSelectEndDate: String? = ""

    private var selectedPos = 0
    private var canClick = true

    private var mListener: DatePickerListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ){
        val view = LayoutInflater.from(context).inflate(getLayoutId(), this, false)
        addView(view)
        initView()
    }

    @LayoutRes
    private fun getLayoutId(): Int {
        return R.layout.layout_drop_date
    }

    private fun initView() {
        viewStartLine = findViewById(R.id.view_start_line)
        viewEndLine = findViewById(R.id.view_end_line)
        tvStartDate = findViewById(R.id.tv_start_date)
        tvEndDate = findViewById(R.id.tv_end_date)
        timePicker = findViewById(R.id.time_picker)
        tvFirstDate = findViewById(R.id.tv_first_date)
        resetButton = findViewById(R.id.resetButton)
        positiveButton = findViewById(R.id.positiveButton)
        initListener()
    }

    private fun initListener() {
        tvStartDate.setOnClickListener {
            updateTvDateSelected(0)
        }
        tvEndDate.setOnClickListener {
            updateTvDateSelected(1)
        }
        resetButton.setOnClickListener {
            selectedPos = -1
            setLeftPicker(defalutLeftYear, defaluLteftMonth, defalutLeftDay)
            setRightPicker(defaultRightYear, defaultRightMonth, defaultRightDay)
            updateTvDateSelected(0)
            canClick = true
        }
        positiveButton.setOnClickListener {
            val startDate = tvStartDate.text.toString().replace("/", "")
            val endDate = tvEndDate.text.toString().replace("/", "")
            if (!canClick) {
                "结束日期应晚于开始日期！".showToast(context)
                return@setOnClickListener
            }
            mSelectStartDate = startDate
            mSelectEndDate = endDate
            val isAllDate = (startDateStr == startDate) && (endDateStr == endDate)
            mListener?.onDoneClick(startDate, endDate,isAllDate)

        }
    }

    private fun updateTvDateSelected(i: Int) {
        selectedPos = i
        when (i) {
            0 -> {
                tvStartDate.isSelected = true
                viewStartLine.isSelected = true
                tvEndDate.isSelected = false
                viewEndLine.isSelected = false
                //设置时间选择器为开始时间
                val date = tvStartDate.text.toString()
                val list: ArrayList<String> = date.split("/") as ArrayList<String>
                if (list.size >= 3) {
                    timePicker.setPicker(
                        list[0].convertToInt(),
                        list[1].convertToInt(),
                        list[2].convertToInt()
                    )
                }
            }
            1 -> {
                tvStartDate.isSelected = false
                viewStartLine.isSelected = false
                tvEndDate.isSelected = true
                viewEndLine.isSelected = true
                //设置时间选择器为结束时间
                val date = tvEndDate.text.toString()
                val list: ArrayList<String> = date.split("/") as ArrayList<String>
                if (list.size >= 3) {
                    timePicker.setPicker(
                        list[0].convertToInt(),
                        list[1].convertToInt(),
                        list[2].convertToInt()
                    )
                }
            }
        }
    }

    /**
     * 恢复选中的日期显示
     */
    fun resetToSelectedDate(){
        if (mSelectStartDate?.length == 8 && mSelectEndDate?.length == 8) {
            mSelectStartDate?.let {
                setLeftPicker(
                    (it.substring(0, 4)).convertToInt(),
                    (it.substring(4, 6)).convertToInt(),
                    (it.substring(6, 8)).convertToInt()
                )
            }
            mSelectEndDate?.let {
                setRightPicker(
                    (it.substring(0, 4)).convertToInt(),
                    (it.substring(4, 6)).convertToInt(),
                    (it.substring(6, 8)).convertToInt()
                )
            }
        }
        updateTvDateSelected(selectedPos)
    }

    /**
     * 最早支持时间
     */
    fun setFirstDate(firstDate: String?) {
        this.firstDate = stringPattern(firstDate ?: "", "yyyyMMdd", "yyyy/MM/dd")
        val showTips = String.format("目前选择最早支持到 %1%s", this.firstDate)
        tvFirstDate.text = showTips
    }

    fun stringPattern(dateString: String, oldPattern: String?, newPattern: String?): String? {
        if (TextUtils.isEmpty(dateString)
            || TextUtils.isEmpty(oldPattern)
            || TextUtils.isEmpty(newPattern)) {
            return ""
        }
        if ("--" == dateString) {
            return dateString
        }
        if (null == mTimePatternFrom) {
            mTimePatternFrom = TimeStringPatternFromUtils()
        }
        return mTimePatternFrom?.formatDate(
            dateString,
            oldPattern ?: "",
            newPattern ?: ""
        )
    }

    /**
     * 设置开始日期和结束日期
     */
    fun setRangDate(startDate: Calendar?, endDate: Calendar?) {
        this.startDate = startDate
        startDateStr = "yyyyMMdd".formatToDate(startDate?.time?: Date())
        mSelectStartDate = startDateStr
        this.endDate = endDate
        endDateStr = "yyyyMMdd".formatToDate(endDate?.time?:Date())
        mSelectEndDate = endDateStr
    }

    /**
     * 设置监听器
     */
    fun setDatePickerListener(listener: DatePickerListener) {
        this.mListener = listener
    }

    fun setPicker(year: Int, month: Int, day: Int){
        timePicker.setPicker(year,month,day)
    }

    /**
     * 设置默认日期
     */
    fun setLeftPicker(year: Int, month: Int, day: Int) {
        this.leftYear = year
        this.leftMonth = month
        this.leftDay = day
        var month2 = "" + leftMonth
        if (leftMonth < 10) {
            month2 = "0$leftMonth"
        }
        var day2 = "$leftDay"
        if (leftDay < 10) {
            day2 = "0$leftDay"
        }
        tvStartDate.text = "$leftYear/$month2/$day2"
    }

    /**
     * 重置的默认值
     */
    fun setDefaultPicker(
        leftYear: Int,
        leftMonth: Int,
        leftDay: Int,
        rightYear: Int,
        rightMonth: Int,
        rightDay: Int
    ) {
        this.defalutLeftYear = leftYear
        this.defaluLteftMonth = leftMonth
        this.defalutLeftDay = leftDay

        this.defaultRightYear = rightYear
        this.defaultRightMonth = rightMonth
        this.defaultRightDay = rightDay
    }

    /**
     * 设置默认日期
     */
    fun setRightPicker(year: Int, month: Int, day: Int) {
        this.rightYear = year
        this.rightMonth = month
        this.rightDay = day
        var month2 = "$rightMonth"
        if (rightMonth < 10) {
            month2 = "0$rightMonth"
        }
        var day2 = "$rightDay"
        if (rightDay < 10) {
            day2 = "0$rightDay"
        }
        tvEndDate.text = "$rightYear/$month2/$day2"
    }

    fun initPickerDate() {
        timePicker.setRangDate(startDate, endDate)
            .setPicker(leftYear, leftMonth, leftDay)
            .setSelectChangeCallback(this)
        updateTvDateSelected(selectedPos)
    }

    override fun onTimeSelectChanged(date: String?) {

    }

    interface DatePickerListener {
        fun onDoneClick(startDate: String?, endDate: String?,isAllDate:Boolean)
    }

    private class TimeStringPatternFromUtils {

        companion object {
            private val mThreadLocal = ThreadLocal<MutableMap<String, SimpleDateFormat>>()
        }

        @Synchronized
        private fun getSimpleDateFormat(
            oldPattern: String,
            newPattern: String
        ): Map<String, SimpleDateFormat> {
            var mDateFormatMap = mThreadLocal.get()
            if (null == mDateFormatMap) {
                mDateFormatMap = HashMap()
            }
            var simpleDateFormat = mDateFormatMap[oldPattern]
            if (simpleDateFormat == null) {
                simpleDateFormat = SimpleDateFormat(oldPattern, Locale.getDefault())
                mDateFormatMap[oldPattern] = simpleDateFormat
                mThreadLocal.set(mDateFormatMap)
            }
            var simpleDateFormat1 = mDateFormatMap[newPattern]
            if (null == simpleDateFormat1) {
                simpleDateFormat1 = SimpleDateFormat(newPattern, Locale.getDefault())
                mDateFormatMap[newPattern] = simpleDateFormat1
                mThreadLocal.set(mDateFormatMap)
            }
            return mDateFormatMap
        }

        fun formatDate(dateString: String?, oldPattern: String, newPattern: String): String? {
            val mFormatMap = getSimpleDateFormat(oldPattern, newPattern)
            var time: String? = ""
            time = try {
                val date = mFormatMap[oldPattern]!!.parse(dateString)
                mFormatMap[newPattern]!!.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                dateString
            }
            return time
        }

    }
}