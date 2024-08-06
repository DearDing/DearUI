package com.dear.ui.activity

import android.graphics.Color
import com.dear.ui.R
import com.dear.ui.databinding.ActivityExpandableTextBinding

/**
 * 可以折叠的TextView
 */
class ExpandableTextActivity : BaseActivity<ActivityExpandableTextBinding>() {

    override fun initView() {
        mDB.tv1.setForceExpandEnable(true)
            .setSuffixText("收起", "展开")
            .setSuffixTextColor(Color.BLUE)
            .setMaxExpandableLines(2)
        mDB.tv2.setSuffixText("收起", "展开")
            .setSuffixTextColor(Color.BLUE)
            .setMaxExpandableLines(2)
        mDB.tv3.setSuffixText("收起", "展开")
            .setSuffixTextColor(Color.BLUE)
            .setSuffixImageResource(R.drawable.icon_expand_up, R.drawable.icon_expand_down)
            .setMaxExpandableLines(2)
    }

    override fun initData() {
        mDB.tv1.setOriginText("奥运会，作为全球规模最大、影响力最深远的综合性体育赛事。")
        mDB.tv2.setOriginText("奥运会，作为全球规模最大、影响力最深远的综合性体育赛事，承载着无数人的梦想与荣耀，彰显着人类对体育精神的不懈追求和对和平友好的坚定信念。")
        mDB.tv3.setOriginText("奥运会，作为全球规模最大、影响力最深远的综合性体育赛事，承载着无数人的梦想与荣耀，彰显着人类对体育精神的不懈追求和对和平友好的坚定信念。")
    }

}