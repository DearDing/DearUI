package com.dear.ui.activity

import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dear.ui.R
import com.dear.ui.databinding.ActivityRecyclerViewLayoutBinding
import com.dear.ui.utils.dp2px

class RecyclerViewActivity : BaseActivity<ActivityRecyclerViewLayoutBinding>() {

    override fun initView() {
        mDB.rvView.layoutManager = LinearLayoutManager(this)
        mDB.rvView.adapter = createAdapter()
    }

    /**
     * 代码创建布局
     */
    fun createItemViewByCode(parent: ViewGroup): View {
        val layout = ConstraintLayout(this).apply {
            id = R.id.item_root
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        val ivIcon = ImageView(this@RecyclerViewActivity).apply {
            layoutParams = ConstraintLayout.LayoutParams(dp2px(this@RecyclerViewActivity,50), dp2px(this@RecyclerViewActivity,50))
            id = R.id.item_iv_icon
            setImageResource(R.mipmap.ic_launcher)
            setPadding(0, dp2px(this@RecyclerViewActivity, 12), 0, dp2px(this@RecyclerViewActivity, 12))
        }.also {
            layout.addView(it)
        }

        val tvTitle = TextView(this@RecyclerViewActivity).apply {
            layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            id = R.id.item_tv_title
            text = "啊啊哇哇哇哇哇哇"
            setTextColor(Color.BLACK)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        }.also {
            layout.addView(it)
        }

        val tvDesc = TextView(this@RecyclerViewActivity).apply {
            layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            id = R.id.item_tv_desc
            text = "哈哈哈哈哈哈哈哈哈哈哈哈"
            setTextColor(Color.BLACK)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        }.also {
            layout.addView(it)
        }

        val constraintSet = ConstraintSet()
        constraintSet.clone(layout)

        constraintSet.connect(ivIcon.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(ivIcon.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT,dp2px(this@RecyclerViewActivity, 16))
        constraintSet.connect(ivIcon.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

        constraintSet.connect(tvTitle.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(tvTitle.id, ConstraintSet.LEFT, ivIcon.id, ConstraintSet.RIGHT)
        constraintSet.connect(tvTitle.id, ConstraintSet.BOTTOM, tvDesc.id, ConstraintSet.TOP)
        constraintSet.setVerticalChainStyle(tvTitle.id, ConstraintSet.CHAIN_PACKED)

        constraintSet.connect(tvDesc.id, ConstraintSet.TOP, tvTitle.id, ConstraintSet.BOTTOM)
        constraintSet.connect(tvDesc.id, ConstraintSet.LEFT, ivIcon.id, ConstraintSet.RIGHT)
        constraintSet.connect(tvDesc.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)


        constraintSet.applyTo(layout)
        return layout
    }

    /**
     * xml 创建布局
     */
    fun createItemViewByXml(parent: ViewGroup): View {
        return LayoutInflater.from(this).inflate(R.layout.item_rv_view_layout, parent, false)
    }

    fun createAdapter(): RecyclerView.Adapter<ItemHolder> {
        return object : RecyclerView.Adapter<ItemHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
                return ItemHolder(createItemViewByCode(parent))
            }

            override fun getItemCount(): Int {
                return 10
            }

            override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            }
        }
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

}