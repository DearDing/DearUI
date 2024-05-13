package com.dear.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.dear.ui.R
import java.util.*
import kotlin.collections.ArrayList
import com.aigestudio.wheelpicker.WheelPicker

/**
 * 日期选择
 */
class DatePickerView : FrameLayout {

    private val TAG : String = "DatePickerView"
    protected var mYearWheel: WheelPicker? = null
    protected var mMonthWheel: WheelPicker? = null
    protected var mDayWheel: WheelPicker? = null
    protected var mFirstData: ArrayList<String> = ArrayList()
    protected var mSecondData: ArrayList<String> = ArrayList()
    protected var mThirdData: ArrayList<String> = ArrayList()
    protected var mFirstChoice: CharSequence? = null
    protected var mSecondChoice: CharSequence? = null
    protected var mThirdChoice: CharSequence? = null

    private val DEFAULT_START_YEAR = 1900
    private val DEFAULT_END_YEAR = 2099
    private val DEFAULT_START_MONTH = 3
    private val DEFAULT_END_MONTH = 9
    private val DEFAULT_START_DAY = 5
    private val DEFAULT_END_DAY = 10

    private var startYear: Int = DEFAULT_START_YEAR
    private var endYear: Int = DEFAULT_END_YEAR
    private var startMonth: Int = DEFAULT_START_MONTH
    private var endMonth: Int = DEFAULT_END_MONTH
    private var startDay: Int = DEFAULT_START_DAY
    private var endDay: Int = DEFAULT_END_DAY //表示31天的
    private var currentYear = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.view_date_picker_layout, this, true)
        init()
    }

    private fun init() {
        initView()
        initPicker(startYear,startMonth,startDay)
    }

    /**
     * 初始化控件
     */
    private fun initView() {
        this.mYearWheel = findViewById<View>(R.id.wheel_first) as WheelPicker
        this.mYearWheel?.setOnItemSelectedListener(WheelPicker.OnItemSelectedListener { picker, data, position ->
            this.mFirstChoice = data.toString()
        })
        this.mMonthWheel = findViewById<View>(R.id.wheel_second) as WheelPicker
        this.mMonthWheel?.setOnItemSelectedListener(WheelPicker.OnItemSelectedListener { picker, data, position ->
            this.mSecondChoice = data.toString()
        })
        this.mDayWheel = findViewById<View>(R.id.wheel_third) as WheelPicker
        this.mDayWheel?.setOnItemSelectedListener(WheelPicker.OnItemSelectedListener { picker, data, position ->
            this.mThirdChoice = data.toString()
        })
    }

    /**
     * 初始化日期
     */
    private fun initPicker(year:Int,month: Int,day:Int){
        // 添加大小月月份并将其转换为list,方便之后的判断
        val months_big = arrayOf("1", "3", "5", "7", "8", "10", "12")
        val months_little = arrayOf("4", "6", "9", "11")

        val list_big = months_big
        val list_little = months_little

        var i = startYear
        // 年
        mFirstData.clear()
        while (i < endYear+1) {
            mFirstData.add(i.toString() + "年")
            ++i
        }
        mYearWheel?.data = mFirstData
        mYearWheel?.selectedItemPosition = year - startYear

        // 月
        if (startYear == endYear) { //开始年等于终止年
            setMonth(startMonth,endMonth)
            mMonthWheel?.selectedItemPosition = month - startMonth
        } else if (year == startYear) {
            //起始日期的月份控制
            setMonth(startMonth,12)
            mMonthWheel?.selectedItemPosition = month - startMonth
        } else if (year == endYear) {
            //终止日期的月份控制
            setMonth(1,endMonth)
            mMonthWheel?.selectedItemPosition = month - 1
        } else {
            setMonth(1,12)
            mMonthWheel?.selectedItemPosition = month - 1
        }
        val leapYear = isRunYear(year)

        currentYear = year

        val dayList: ArrayList<String> = ArrayList()
        if (startYear == endYear && startMonth == endMonth) {
            if (list_big.contains((month).toString())) {
                if (endDay > 31) {
                    endDay = 31
                }
                setDayData(1, endDay, dayList)
            } else if (list_little.contains((month).toString())) {
                if (endDay > 30) {
                    endDay = 30
                }
                setDayData(1, endDay, dayList)
            } else {
                // 闰年
                if (isRunYear(year)) {
                    if (endDay > 29) {
                        endDay = 29
                    }
                    setDayData(1, endDay, dayList)
                } else {
                    if (endDay > 28) {
                        endDay = 28
                    }
                    setDayData(1, endDay, dayList)
                }
            }
            mDayWheel?.selectedItemPosition = day - startDay
        } else if (year == startYear && month == startMonth) {
            // 起始日期的天数控制
            if (list_big.contains((month).toString())) {
                setDayData(startDay, 31, dayList)
            } else if (list_little.contains((month).toString())) {
                setDayData(startDay, 30, dayList)
            } else {
                // 闰年 29，平年 28
                setDayData(startDay, if (leapYear) 29 else 28, dayList)
            }
            mDayWheel?.selectedItemPosition = day - startDay
        } else if (year == endYear && month == endMonth) {
            // 终止日期的天数控制
            if (list_big.contains((month).toString())) {
                if (endDay > 31) {
                    endDay = 31
                }
                setDayData(1, endDay, dayList)
            } else if (list_little.contains((month).toString())) {
                if (endDay > 30) {
                    endDay = 30
                }
                setDayData(1, endDay, dayList)
            } else {
                // 闰年
                if (leapYear) {
                    if (endDay > 29) {
                        endDay = 29
                    }
                    setDayData(1, endDay, dayList)
                } else {
                    if (endDay > 28) {
                        endDay = 28
                    }
                    setDayData(1, endDay, dayList)
                }
            }
            mDayWheel?.selectedItemPosition = day - 1
        } else {
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains((month).toString())) {
                setDayData(1, 31, dayList)
            } else if (list_little.contains((month).toString())) {
                setDayData(1, 30, dayList)
            } else {
                // 闰年 29，平年 28
                setDayData(1, if (leapYear) 29 else 28, dayList)
            }
            mDayWheel?.selectedItemPosition = day - 1
        }

        // 添加"年"监听
        mYearWheel?.setOnWheelChangeListener(object : WheelPicker.OnWheelChangeListener {
            override fun onWheelScrolled(i: Int) {}
            override fun onWheelSelected(index: Int) {

                val year_num: Int = index + startYear
                currentYear = year_num
                var currentMonthItem: Int = mMonthWheel?.currentItemPosition?:0 //记录上一次的item位置
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (startYear == endYear) {
                    //重新设置月份
                    setMonth(startMonth,endMonth)
                    if (currentMonthItem > (mMonthWheel?.data?.size?:0) - 1) {
                        currentMonthItem = (mMonthWheel?.data?.size?:0) - 1
                        mMonthWheel?.selectedItemPosition = currentMonthItem
                    }
                    val monthNum: Int = currentMonthItem + startMonth
                    if (startMonth == endMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, startDay, endDay, list_big, list_little)
                    } else if (monthNum == startMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, startDay, 31, list_big, list_little)
                    } else if (monthNum == endMonth) {
                        setReDay(year_num, monthNum, 1, endDay, list_big, list_little)
                    } else { //重新设置日
                        setReDay(year_num, monthNum, 1, 31, list_big, list_little)
                    }
                } else if (year_num == startYear) { //等于开始的年
                    //重新设置月份
                    setMonth(startMonth,12)
                    if (currentMonthItem > (mMonthWheel?.data?.size?:0) - 1) {
                        currentMonthItem = (mMonthWheel?.data?.size?:0) - 1
                        mMonthWheel?.selectedItemPosition = currentMonthItem
                    }
                    val month: Int = currentMonthItem + startMonth
                    if (month == startMonth) {
                        //重新设置日
                        setReDay(year_num, month, startDay, 31, list_big, list_little)
                    } else {
                        //重新设置日
                        setReDay(year_num, month, 1, 31, list_big, list_little)
                    }
                } else if (year_num == endYear) {
                    setMonth(1,endMonth)
                    if (currentMonthItem > (mMonthWheel?.data?.size?:0) - 1) {
                        currentMonthItem = (mMonthWheel?.data?.size?:0) - 1
                        mMonthWheel?.selectedItemPosition = currentMonthItem
                    }
                    val monthNum = currentMonthItem + 1
                    if (monthNum == endMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, 1, endDay, list_big, list_little)
                    } else {
                        //重新设置日
                        setReDay(year_num, monthNum, 1, 31, list_big, list_little)
                    }
                } else {
                    setMonth(1,12)
                    //重新设置日
                    setReDay(year_num, (mMonthWheel?.currentItemPosition?:0) + 1, 1, 31, list_big, list_little)
                }
                mSelectChangeCallback?.onTimeSelectChanged(getSelectedItem())
            }
            override fun onWheelScrollStateChanged(i: Int) {
            }
        })

        // 添加"月"监听
        mMonthWheel?.setOnWheelChangeListener(object : WheelPicker.OnWheelChangeListener {
            override fun onWheelScrolled(i: Int) {}
            override fun onWheelSelected(index: Int) {
                var month_num = index + 1
                if (startYear == endYear) {
                    month_num = month_num + startMonth - 1
                    if (startMonth == endMonth) {
                        //重新设置日
                        setReDay(currentYear, month_num, startDay, endDay, list_big, list_little)
                    } else if (startMonth == month_num) {
                        //重新设置日
                        setReDay(currentYear, month_num, startDay, 31, list_big, list_little)
                    } else if (endMonth == month_num) {
                        setReDay(currentYear, month_num, 1, endDay, list_big, list_little)
                    } else {
                        setReDay(currentYear, month_num, 1, 31, list_big, list_little)
                    }
                } else if (currentYear == startYear) {
                    month_num = month_num + startMonth - 1
                    if (month_num == startMonth) {
                        //重新设置日
                        setReDay(currentYear, month_num, startDay, 31, list_big, list_little)
                    } else {
                        //重新设置日
                        setReDay(currentYear, month_num, 1, 31, list_big, list_little)
                    }
                } else if (currentYear == endYear) {
                    if (month_num == endMonth) {
                        //重新设置日
                        setReDay(currentYear, (mMonthWheel?.currentItemPosition?:0) + 1, 1, endDay, list_big, list_little)
                    } else {
                        setReDay(currentYear, (mMonthWheel?.currentItemPosition?:0) + 1, 1, 31, list_big, list_little)
                    }
                } else {
                    //重新设置日
                    setReDay(currentYear, month_num, 1, 31, list_big, list_little)
                }
                var currentItem: Int = mMonthWheel?.currentItemPosition?:0
                if (currentItem > (mMonthWheel?.data?.size?:0) - 1) {
                    currentItem = (mMonthWheel?.data?.size?:0) - 1
                    mMonthWheel?.selectedItemPosition = currentItem
                }else {
                    mMonthWheel?.selectedItemPosition = currentItem

                }
                mSelectChangeCallback?.onTimeSelectChanged(getSelectedItem())
            }
            override fun onWheelScrollStateChanged(i: Int) {

            }
        })

        //添加"日"监听
        mDayWheel?.setOnWheelChangeListener(object : WheelPicker.OnWheelChangeListener {
            override fun onWheelScrolled(p0: Int) {

            }

            override fun onWheelSelected(p0: Int) {
                var currentItem: Int = mDayWheel?.currentItemPosition?:0
                if (currentItem > (mDayWheel?.data?.size?:0) - 1) {
                    currentItem = (mDayWheel?.data?.size?:0) - 1
                    mDayWheel?.selectedItemPosition = currentItem
                }else {
                    mDayWheel?.selectedItemPosition = currentItem

                }
                mSelectChangeCallback?.onTimeSelectChanged(getSelectedItem())
            }

            override fun onWheelScrollStateChanged(p0: Int) {

            }
        })
    }

    /**
     * 更新月份
     */
    private fun setMonth(startMonth:Int,endMonth:Int) {
        mSecondData.clear()
        var i = startMonth
        while (i < endMonth + 1) {
            mSecondData.add("" + (i) + "月")
            ++i
        }
        mMonthWheel?.data = mSecondData
    }

    /**
     * 设置日期
     */
    private fun setReDay(year_num: Int, monthNum: Int, startD: Int, endD: Int, list_big: Array<String>, list_little: Array<String>) {
        var endD = endD
        var currentItem: Int = mDayWheel?.currentItemPosition?:0

        val dayList: ArrayList<String> = ArrayList()
        if (list_big.contains(monthNum.toString())) {
            if (endD > 31) {
                endD = 31
            }
            setDayData(startD, endD, dayList)
        } else if (list_little.contains(monthNum.toString())) {
            if (endD > 30) {
                endD = 30
            }
            setDayData(startD, endD, dayList)
        } else {
            if (isRunYear(year_num)) {
                if (endD > 29) {
                    endD = 29
                }
                setDayData(startD, endD, dayList)
            } else {
                if (endD > 28) {
                    endD = 28
                }
                setDayData(startD, endD, dayList)
            }
        }
        if (currentItem > (mDayWheel?.data?.size?:0) - 1) {
            currentItem = (mDayWheel?.data?.size?:0) - 1
            mDayWheel?.selectedItemPosition = currentItem
        }
    }

    /**
     * 更新"日"区间
     */
    private fun setDayData(startD: Int, endD: Int, dayList: ArrayList<String>) {
        for (j in startD..endD) {
            dayList.add(j.toString() + "日")
        }
        mThirdData.clear()
        mThirdData.addAll(dayList)
        mDayWheel?.data = mThirdData
    }

    /**
     * 获取当前选中的时间
     */
    fun getSelectedItem(): String? {
        val mChoiceResult = StringBuilder()
        var index: Int
        if (VISIBLE == mYearWheel?.visibility) {
            index = mYearWheel?.currentItemPosition?:0
            if (index < mFirstData.size) {
                mChoiceResult.append(mFirstData[index])
            }
        }
        if (VISIBLE == mMonthWheel?.visibility) {
            index = mMonthWheel?.currentItemPosition?:0
            if (index < mSecondData.size) {
                mChoiceResult.append(mSecondData[index])
            }
        }
        if (VISIBLE == mDayWheel?.visibility) {
            index = mDayWheel?.currentItemPosition?:0
            if (index < mThirdData.size) {
                mChoiceResult.append(mThirdData[index])
            }
        }
        return mChoiceResult.toString()
    }

    /**
     * 判断是否为闰年 2月28/2月29
     */
    protected fun isRunYear(year: Int): Boolean {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
    }

    /**
     * 设置开始日期和结束日期
     */
    fun setRangDate(startDate: Calendar?, endDate: Calendar?):DatePickerView{
        if (startDate == null && endDate != null) {
            val year = endDate[Calendar.YEAR]
            val month = endDate[Calendar.MONTH] + 1
            val day = endDate[Calendar.DAY_OF_MONTH]
            if (year > startYear) {
                endYear = year
                endMonth = month
                endDay = day
            } else if (year == startYear) {
                if (month > startMonth) {
                    endYear = year
                    endMonth = month
                    endDay = day
                } else if (month == startMonth) {
                    if (day > startDay) {
                        endYear = year
                        endMonth = month
                        endDay = day
                    }
                }
            }
        } else if (startDate != null && endDate == null) {
            val year = startDate[Calendar.YEAR]
            val month = startDate[Calendar.MONTH] + 1
            val day = startDate[Calendar.DAY_OF_MONTH]
            if (year < endYear) {
                startMonth = month
                startDay = day
                startYear = year
            } else if (year == endYear) {
                if (month < endMonth) {
                    startMonth = month
                    startDay = day
                    startYear = year
                } else if (month == endMonth) {
                    if (day < endDay) {
                        startMonth = month
                        startDay = day
                        startYear = year
                    }
                }
            }
        } else if (startDate != null && endDate != null) {
            startYear = startDate[Calendar.YEAR]
            endYear = endDate[Calendar.YEAR]
            startMonth = startDate[Calendar.MONTH] + 1
            endMonth = endDate[Calendar.MONTH] + 1
            startDay = startDate[Calendar.DAY_OF_MONTH]
            endDay = endDate[Calendar.DAY_OF_MONTH]
        }
        return this
    }

    fun setSelectChangeCallback(mSelectChangeCallback: ISelectTimeCallback?):DatePickerView{
        this.mSelectChangeCallback = mSelectChangeCallback
        return this
    }

    /**
     * 设置默认日期
     */
    fun setPicker(year: Int, month: Int, day: Int):DatePickerView {
        initPicker(year, month, day)
        return this
    }

    /**
     * 日期变换监听处理
     */
    private var mSelectChangeCallback: ISelectTimeCallback? = null

    interface ISelectTimeCallback {
        fun onTimeSelectChanged(date:String?)
    }

}