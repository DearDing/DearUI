package com.dear.ui.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dear.ui.R
import com.dear.ui.databinding.ActivityAbsVideoLayoutBinding
import com.dear.ui.fragment.ItemContentFragment

abstract class AbsVideoFeedActivity : BaseActivity<ActivityAbsVideoLayoutBinding>() {

    companion object {
        private const val ANIM_DURATION: Long = 500L
    }

    private val mContentFragment: ItemContentFragment by lazy {
        ItemContentFragment.getInstance()
    }
    private var mShowAnimator: TranslateAnimation? = null
    private var mHideAnimator: TranslateAnimation? = null

    override fun initView() {
        mDB.viewPager.offscreenPageLimit = 5
        mDB.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        mDB.viewPager.adapter = createAdapter()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fl_content_layout,mContentFragment)
        transaction.commitAllowingStateLoss()

        initViewLayer()
    }

    open fun initViewLayer() {
    }

    override fun initListener() {
        mShowAnimator = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f,
            TranslateAnimation.RELATIVE_TO_SELF, 1f, TranslateAnimation.RELATIVE_TO_SELF, 0f
        )
        mShowAnimator?.duration = ANIM_DURATION
        mShowAnimator?.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                mContentFragment.showMiddleContent()
            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        mHideAnimator = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f,
            TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 1f
        )
        mHideAnimator?.duration = ANIM_DURATION
    }

    fun setContentLayoutVisible(isVisible: Boolean) {
        mDB.flContentLayout.post {
            if (isVisible) {
                mDB.flContentLayout.visibility = View.VISIBLE
                mDB.flContentLayout.startAnimation(mShowAnimator)
            } else {
                mDB.flContentLayout.visibility = View.INVISIBLE
                mDB.flContentLayout.startAnimation(mHideAnimator)
            }
        }
    }

    private fun createAdapter(): RecyclerView.Adapter<ItemHolder> {
        return object : RecyclerView.Adapter<ItemHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(this@AbsVideoFeedActivity)
                        .inflate(R.layout.item_fragment_layout, parent, false)
                )
            }

            override fun getItemCount(): Int {
                return Int.MAX_VALUE
            }

            override fun onBindViewHolder(holder: ItemHolder, position: Int) {
                holder.tvText.text = "当前位置是：$position"
            }
        }
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvText: TextView = view.findViewById(R.id.tv_text)

        init {
            tvText.setOnClickListener {
                setContentLayoutVisible(true)
            }
        }
    }

}