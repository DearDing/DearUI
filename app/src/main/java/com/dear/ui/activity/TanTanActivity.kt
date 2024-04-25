package com.dear.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dear.ui.R
import com.dear.ui.recyclerview.card.CardLayoutManager
import com.dear.ui.recyclerview.card.CardSetting
import com.dear.ui.recyclerview.card.CardTouchHelperCallback
import com.dear.ui.recyclerview.card.ReItemTouchHelper

class TanTanActivity : AppCompatActivity() {

    private lateinit var mRvView: RecyclerView
    private var mRvAdapter: CardAdapter? = null
    private var mHelperCallback: CardTouchHelperCallback<CardBean>? = null
    private val mOption: RequestOptions =
        RequestOptions().transform(CenterCrop(), RoundedCorners(16))
    private val urlArr = arrayListOf(
        CardBean(
            "https://copyright.bdstatic.com/vcg/creative/cc9c744cf9f7c864889c563cbdeddce6.jpg@h_1280",
            "第一张图"
        ),
        CardBean(
            "https://pic.rmb.bdstatic.com/bjh/914b8c0f9814b14c5fedeec7ec6615df5813.jpeg",
            "第二张图"
        ),
        CardBean(
            "https://i0.hdslb.com/bfs/archive/609b9b5611900fad4b1f505687b7fe6b8a3442ad.jpg",
            "第三张图"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tan_tan)
        mRvView = findViewById(R.id.rv_pic_view)

        val setting = CardSetting()
        mHelperCallback = CardTouchHelperCallback(mRvView, urlArr, setting)
        val mReItemTouchHelper = ReItemTouchHelper(mHelperCallback)
        val layoutManager = CardLayoutManager(mReItemTouchHelper, setting)
        mRvView.layoutManager = layoutManager
        mRvAdapter = CardAdapter()
        mRvView.adapter = mRvAdapter
    }

    private fun onItemClickEvent(bean: CardBean) {
        Toast.makeText(this, bean.title, Toast.LENGTH_SHORT).show()
    }

    inner class CardAdapter : RecyclerView.Adapter<CardHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_tan_tan_layout, parent, false)
            return CardHolder(view)
        }

        override fun onBindViewHolder(
            holder: CardHolder,
            @SuppressLint("RecyclerView") position: Int
        ) {
            val bean = urlArr[position]
            holder.itemView.tag = bean
            bean.let {
                Glide.with(holder.itemView).load(it.url).apply(mOption).into(holder.img)
                holder.tvText.text = it.title
            }
        }

        override fun getItemCount(): Int {
            return urlArr.size
        }
    }

    inner class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.findViewById(R.id.card_img)
        var tvText: TextView = itemView.findViewById(R.id.tv_text)

        init {
            img.setOnClickListener {
                val tag = itemView.tag
                if (tag is CardBean) {
                    onItemClickEvent(tag)
                }
            }
        }
    }
}

data class CardBean(
    val url: String?,
    val title: String?
)