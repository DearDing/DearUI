package com.dear.ui.fragment

import androidx.core.os.bundleOf
import com.dear.ui.R
import com.dear.ui.databinding.ItemFragmentLayoutBinding

class ItemFragment : BaseFragment<ItemFragmentLayoutBinding>() {

    companion object {

        const val PARAM_POS = "param_position"

        @JvmStatic
        fun getInstance(position: Int): ItemFragment {
            val fragment = ItemFragment()
            fragment.arguments = bundleOf(Pair(PARAM_POS, position))
            return fragment
        }
    }

    private var mPosition = -1

    override fun initParams() {
        mPosition = arguments?.getInt(PARAM_POS) ?: -1
    }

    override fun initView() {
        mDB.tvText.text = "$mPosition"
    }

}