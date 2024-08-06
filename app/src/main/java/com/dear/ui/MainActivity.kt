package com.dear.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dear.ui.activity.*
import com.dear.ui.databinding.ActivityMainBinding
import com.dear.ui.debug.TraceUtil

class MainActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "MainActivity_TAG"
    }
    private lateinit var binding: ActivityMainBinding
    private val textArr: Array<String> = arrayOf("仿探探卡片", "上下两个ViewPager联动","下拉筛选","RecyclerView优化","MotionLayout实现折叠工具栏","仿高德三段式滑动效果","可折叠的TextView")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycleView()
        //testMuchTime()
    }

    /**
     * 测试 TraceUtil 工具，统计方法耗时
     */
    private fun testMuchTime(){
        val startTime = System.currentTimeMillis()
        Log.i("TraceUtil", "onCreate: startTime= ${startTime}")
        for (index in 0..100000){
            val result = index + 10
            println(result)
        }
        val endTime = System.currentTimeMillis()
        Log.i("TraceUtil", "onCreate: endTime= ${startTime}  total = ${endTime - startTime}")
    }

    private fun initRecycleView() {
        binding.rvView.layoutManager = LinearLayoutManager(this)
        binding.rvView.adapter = createAdapter()
    }

    private fun onItemClickEvent(position: Int) {
        when (position) {
            0 -> {
                startActivity(Intent(this, TanTanActivity::class.java))
            }
            1 -> {
                startActivity(Intent(this, ViewPagerScroll2Activity::class.java))
            }
            2->{
                startActivity(Intent(this, DropFilterActivity::class.java))
            }
            3->{
                startActivity(Intent(this, RecyclerViewActivity::class.java))
            }
            4->{
                startActivity(Intent(this, MotionCollapsingActivity::class.java))
            }
            5->{
                startActivity(Intent(this, VideoMotionActivity::class.java))
            }
            6->{
                startActivity(Intent(this, ExpandableTextActivity::class.java))
            }
        }
    }

    private fun createAdapter(): RecyclerView.Adapter<ItemViewHolder> {
        return object : RecyclerView.Adapter<ItemViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): ItemViewHolder {
                return ItemViewHolder(
                    LayoutInflater.from(this@MainActivity)
                        .inflate(R.layout.item_text_main_layout, parent, false)
                )
            }

            override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
                holder.itemView.tag = position
                holder.tvText.text = textArr[position]
            }

            override fun getItemCount(): Int {
                return textArr.size
            }

        }
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvText = view.findViewById<TextView>(R.id.tv_text)

        init {
            tvText.setOnClickListener {
                if (itemView.tag is Int) {
                    onItemClickEvent(itemView.tag as Int)
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        Log.i(TAG,"onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG,"onResume")
    }

    override fun onPause() {
        super.onPause()
        TraceUtil.stopTrace()
        Log.i(TAG,"onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG,"onDestroy")

    }
}