package com.dear.ui.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dear.ui.R
import com.dear.ui.databinding.ActivityMotionlayoutCollapsingLayoutBinding

/**
 * 实践一
 * MotionLayout实现折叠工具栏
 */
class MotionCollapsingActivity : BaseActivity<ActivityMotionlayoutCollapsingLayoutBinding>() {

    override fun initView() {
        val layoutManager = LinearLayoutManager(this)
        mDB.rvView.layoutManager = layoutManager
        mDB.rvView.adapter = createAdapter()
    }

    private fun createAdapter(): RecyclerView.Adapter<ItemHolder> {
        return object : RecyclerView.Adapter<ItemHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
                return ItemHolder(LayoutInflater.from(this@MotionCollapsingActivity).inflate(R.layout.item_text_layout,null))
            }

            override fun getItemCount(): Int {
                return 10
            }

            override fun onBindViewHolder(holder: ItemHolder, position: Int) {
                holder.tvText.text = "hello world $position"
            }
        }
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvText:TextView = view.findViewById(R.id.tv_text)
    }
}