package com.dear.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dear.ui.activity.DropFilterActivity
import com.dear.ui.activity.TanTanActivity
import com.dear.ui.activity.ViewPagerScroll2Activity
import com.dear.ui.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val textArr: Array<String> = arrayOf("仿探探卡片", "上下两个ViewPager联动","下拉筛选")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycleView()
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
}