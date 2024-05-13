package com.dear.ui.widget.dropview

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dear.ui.R

class DropListAdapter (private val context: Context) : RecyclerView.Adapter<DropListAdapter.DropListHolder>() {

    /**
     * 记录选中的pos
     */
    var mSelectedPos = 0

    var unEnablePos = -1

    private var mListener:OnMenuItemClickListener?=null

    private val mList:ArrayList<DialogItemBean> = arrayListOf()

    fun refresh(list:List<DialogItemBean>?){
        mList.clear()
        list?.let {
            mList.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DropListHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.drop_filter_list_item, parent, false)
        return DropListHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: DropListHolder, position: Int) {
        if (holder is DropListHolder) {
            val menu = mList[position]
            menu?.run {
                holder.itemView.isEnabled = unEnablePos != position
                if (holder.itemView.isEnabled) {
                    holder.itemView.visibility = View.VISIBLE
                    holder.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                }else{
                    holder.itemView. layoutParams.height = 0
                    holder.itemView.visibility = View.GONE
                }
                if(!TextUtils.isEmpty(icon)){
                    holder.ivIcon.visibility= View.VISIBLE
                    Glide.with(context).load(icon).into(holder.ivIcon)
                }else{
                    holder.ivIcon.visibility= View.GONE
                }
                if(iconRes != 0){
                    holder.ivIcon.visibility= View.VISIBLE
                    holder.ivIcon.setImageResource(iconRes)
                }else{
                    holder.ivIcon.visibility= View.GONE
                }
                //对勾✔️ 显示与隐藏
                holder.ivSelect.visibility = if (isSelected) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
                holder.itemView.isSelected = isSelected
                holder.tvName.text = content
                holder.tvName.setTextColor(ResourcesCompat.getColor(context.resources,R.color.theme_color,null))

                holder.itemView.setOnClickListener {
                    //如果列表数据过大 此方法可优化
                    for (i in 0 until mList.size) {
                        mList[i]?.isSelected = i == position
                    }
                    mSelectedPos = position
                    notifyDataSetChanged()
                    mListener?.onItemMenuClick(position)
                }
            }

        }
    }

    inner class DropListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_content)
        val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
        val ivSelect: ImageView = itemView.findViewById(R.id.iv_selected)

        init {
            ivSelect.setBackgroundResource(R.drawable.svg_selected_mark)
        }
    }

    fun setOnMenuClickListener(listener:OnMenuItemClickListener) {
        this.mListener = listener
    }

    interface OnMenuItemClickListener{
        fun onItemMenuClick(position:Int)
    }
}