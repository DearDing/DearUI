package com.dear.ui.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dear.ui.R
import com.dear.ui.databinding.ActivityViewpagerScroll2Binding
import com.dear.ui.fragment.ItemFragment

/**
 * 上下两个viewpager 联动
 */
class ViewPagerScroll2Activity : BaseActivity<ActivityViewpagerScroll2Binding>() {

    companion object {
        const val OFFSET_X = 100f
    }

    private val mListTop: ArrayList<View> = arrayListOf()
    private val mListBottom: ArrayList<Fragment> = arrayListOf()
    private var mCurrentPos = 0

    override fun initView() {
        initListData()
        mDB.viewPagerTop.setPageTransformer(false) { page, position ->
            val vpWidth = mDB.viewPagerTop.measuredWidth
            val scrollX = page.left - mDB.viewPagerTop.scrollX
            val rate = (scrollX + page.measuredWidth / 2 - vpWidth / 2) * 0.8f / vpWidth
            page.translationX = -rate * OFFSET_X
        }
        mDB.viewPagerTop.offscreenPageLimit = mListTop.size
        mDB.viewPagerTop.adapter = createTopAdapter()
        mDB.viewPagerTop.currentItem = mCurrentPos

        mDB.viewPagerBottom.offscreenPageLimit = mListBottom.size
        mDB.viewPagerBottom.adapter = createBottomAdapter()
        mDB.viewPagerBottom.currentItem = mCurrentPos
    }

    override fun initListener() {
        mDB.viewPagerTop.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val marginX: Int = getOffsetX(position, positionOffsetPixels)
                if (mDB.viewPagerBottom.scrollX != marginX) {
                    mDB.viewPagerBottom.scrollTo(marginX, 0)
                }
            }

            override fun onPageSelected(position: Int) {
                mCurrentPos = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mDB.viewPagerBottom.currentItem = mCurrentPos
                }
            }
        })
    }

    fun initListData() {
        val view1 = LayoutInflater.from(this).inflate(R.layout.item_viewpager_top_layout,null)
        view1.findViewById<TextView>(R.id.tv_text).setBackgroundResource(R.drawable.shape_blue_r10)
        mListTop.add(view1)
        val view2 = LayoutInflater.from(this).inflate(R.layout.item_viewpager_top_layout,null)
        view2.findViewById<TextView>(R.id.tv_text).setBackgroundResource(R.drawable.shape_green_r10)
        mListTop.add(view2)
        val view3 = LayoutInflater.from(this).inflate(R.layout.item_viewpager_top_layout,null)
        view3.findViewById<TextView>(R.id.tv_text).setBackgroundResource(R.drawable.shape_orange_r10)
        mListTop.add(view3)

        mListBottom.add(ItemFragment.getInstance(0))
        mListBottom.add(ItemFragment.getInstance(2))
        mListBottom.add(ItemFragment.getInstance(3))
    }

    /**
     * 获取偏移量
     */
    private fun getOffsetX(position: Int, positionOffsetPixels: Int): Int {
        return ((mDB.viewPagerTop.width + mDB.viewPagerTop.pageMargin) * position
                + positionOffsetPixels) * (mDB.viewPagerBottom.width + mDB.viewPagerBottom.pageMargin) / (mDB.viewPagerTop.width
                + mDB.viewPagerTop.pageMargin)
    }

    private fun createTopAdapter(): PagerAdapter {
        return object : PagerAdapter() {

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }

            override fun getCount(): Int {
                return mListTop.size
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val view = mListTop[position]
                container.addView(view)
                return view
            }
        }
    }

    private fun createBottomAdapter(): FragmentStatePagerAdapter {
        return object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return mListBottom.size
            }

            override fun getItem(position: Int): Fragment {
                return mListBottom[position]
            }
        }
    }

}