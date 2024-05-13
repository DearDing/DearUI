package com.dear.ui.widget.dropview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dear.ui.R

/**
 * 下拉筛选
 * 列表样式的筛选view
 */
class DropListView : FrameLayout {

    private lateinit var mRootLayout: LinearLayout
    private lateinit var mRecyclerView: RecyclerView

    private var mAdapter: DropListAdapter? = null
    private var mListData:ArrayList<DialogItemBean> ?=null
    private var mListener:OnItemListClickListener?=null

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
        return R.layout.layout_drop_list
    }

    private fun initView() {
        mRootLayout = findViewById(R.id.ll_root_layout)
        mRecyclerView = findViewById(R.id.recycler_view)
        initRecyclerView()
        initListener()
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun initRecyclerView() {
        mRecyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager
        mAdapter = DropListAdapter(context)
        mRecyclerView.adapter = mAdapter
    }

    private fun initListener() {
        mAdapter?.setOnMenuClickListener(object :DropListAdapter.OnMenuItemClickListener{
            override fun onItemMenuClick(position: Int) {
                if(position < 0 || position >= (mListData?.size ?: 0)){
                    return
                }
                mListener?.onItemClick(mListData?.get(position))
            }
        })
    }

    fun setItemListClickListener(listener:OnItemListClickListener):DropListView{
        mListener = listener
        return this
    }

    /**
     * 添加数据
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<DialogItemBean>): DropListView {
        if (null == mListData) {
            mListData = arrayListOf()
        }else{
            mListData?.clear()
        }
        if (list.isNotEmpty()) {
            mListData?.addAll(list)
        }
        mAdapter?.refresh(mListData)
        return this
    }

    interface OnItemListClickListener{
        fun onItemClick(bean:DialogItemBean?)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

}