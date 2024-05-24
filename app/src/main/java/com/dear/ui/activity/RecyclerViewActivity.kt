package com.dear.ui.activity

import android.graphics.Color
import android.util.Log
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
import com.dear.ui.dsl.*
import com.dear.ui.utils.dp2px

/**
 * 通过动态代码创建 itemView
 */
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
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        val ivIcon = ImageView(this@RecyclerViewActivity).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                dp2px(this@RecyclerViewActivity, 50),
                dp2px(this@RecyclerViewActivity, 50)
            )
            id = R.id.item_iv_icon
            setImageResource(R.mipmap.ic_launcher)
            setPadding(
                0,
                dp2px(this@RecyclerViewActivity, 12),
                0,
                dp2px(this@RecyclerViewActivity, 12)
            )
        }.also {
            layout.addView(it)
        }

        val tvTitle = TextView(this@RecyclerViewActivity).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            id = R.id.item_tv_title
            text = "啊啊哇哇哇哇哇哇"
            setTextColor(Color.BLACK)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        }.also {
            layout.addView(it)
        }

        val tvDesc = TextView(this@RecyclerViewActivity).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            id = R.id.item_tv_desc
            text = "哈哈哈哈哈哈哈哈哈哈哈哈"
            setTextColor(Color.BLACK)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        }.also {
            layout.addView(it)
        }

        val constraintSet = ConstraintSet()
        constraintSet.clone(layout)

        constraintSet.connect(
            ivIcon.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            ivIcon.id,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT,
            dp2px(this@RecyclerViewActivity, 16)
        )
        constraintSet.connect(
            ivIcon.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )

        constraintSet.connect(
            tvTitle.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(tvTitle.id, ConstraintSet.LEFT, ivIcon.id, ConstraintSet.RIGHT)
        constraintSet.connect(tvTitle.id, ConstraintSet.BOTTOM, tvDesc.id, ConstraintSet.TOP)
        constraintSet.setVerticalChainStyle(tvTitle.id, ConstraintSet.CHAIN_PACKED)

        constraintSet.connect(tvDesc.id, ConstraintSet.TOP, tvTitle.id, ConstraintSet.BOTTOM)
        constraintSet.connect(tvDesc.id, ConstraintSet.LEFT, ivIcon.id, ConstraintSet.RIGHT)
        constraintSet.connect(
            tvDesc.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )

        constraintSet.applyTo(layout)
        return layout
    }

    /**
     * 代码创建布局，优化可读性
     */
    fun createItemViewByCodeDSL(parent: ViewGroup):View{
        return ConstraintLayoutDSL{
            layout_width = match_parent
            layout_height = wrap_content

            ImageViewDSL {
                layout_id = "iv_icon"
                top_toTopOf = parent_id
                start_toStartOf = parent_id
                bottom_toBottomOf = parent_id
                layout_width = wrap_content
                layout_height = wrap_content
                padding_bottom = 12
                padding_top = 12
                margin_start = 16
                src = R.mipmap.ic_launcher
            }.also {
                addView(it)
            }

            TextViewDSL {
                layout_id = "tv_title"
                layout_width = wrap_content
                layout_height = wrap_content
                text = "标题标题标题"
                text_color_res = R.color.black
                textSize = 14f
                bottom_toTopOf = "tv_desc"
                start_toEndOf = "iv_icon"
                top_toTopOf = parent_id
                vertical_chain_style = packed
            }.also {
                addView(it)
            }

            TextViewDSL {
                layout_id = "tv_desc"
                layout_width = wrap_content
                layout_height = wrap_content
                text = "描述信息描述"
                margin_start = 10
                textColor = "#666666"
                textSize = 12f
                bottom_toBottomOf = parent_id
                start_toEndOf = "iv_icon"
                top_toBottomOf = "tv_title"
                vertical_chain_style = packed
            }.also {
                addView(it)
            }
        }
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
                val timeStart = System.currentTimeMillis()
                // 平均耗时 4-5ms （最优）
                val view = createItemViewByCode(parent)

                // 平均耗时 7-8ms
                //val view = createItemViewByCodeDSL(parent)

                // 平均耗时 7-8ms
                //val view = createItemViewByXml(parent)

                val timeEnd = System.currentTimeMillis()
                Log.i("RecyclerView create time","use time = ${timeEnd-timeStart}")
                return ItemHolder(view)
            }

            override fun getItemCount(): Int {
                return 20
            }

            override fun onBindViewHolder(holder: ItemHolder, position: Int) {
                holder.tvTitle?.text = "我是标题标题标题 $position"
                holder.tvDesc?.text = "我是简介简介"
            }
        }
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val ivIcon: ImageView = view.findViewById(R.id.item_iv_icon)
//        val tvTitle: TextView = view.findViewById(R.id.item_tv_title)
//        val tvDesc: TextView = view.findViewById(R.id.item_tv_desc)

        var ivIcon: ImageView? = view.find("iv_icon")
        val tvTitle: TextView? = view.find("tv_title")
        val tvDesc: TextView? = view.find("tv_desc")
    }

}