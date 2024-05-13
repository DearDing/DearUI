package com.dear.ui.activity

import com.dear.ui.databinding.ActivityDropFilterLayoutBinding
import com.dear.ui.widget.dropview.DialogItemBean

/**
 * 下拉筛选
 */
class DropFilterActivity : BaseActivity<ActivityDropFilterLayoutBinding>() {

    companion object {
        private const val TYPE_DATE = "type_date"
        private const val TYPE_LIST= "type_list_name"
    }

    override fun initView() {
        mDB.filterView.addDateView(TYPE_DATE,"全部日期","20230101")
        val list = arrayListOf<DialogItemBean>()
        list.add(DialogItemBean("校花","xiaohua"))
        list.add(DialogItemBean("校草","xiaocao"))
        list.add(DialogItemBean("校霸","xiaoba"))
        mDB.filterView.addListView(TYPE_LIST,"名称",list)
        mDB.filterView.setBottomView(mDB.bottomView)
    }

}