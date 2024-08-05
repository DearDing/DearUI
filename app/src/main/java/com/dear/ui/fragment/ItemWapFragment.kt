package com.dear.ui.fragment

import android.webkit.WebSettings
import androidx.core.os.bundleOf
import com.dear.ui.databinding.ItemFragmentLayoutBinding
import com.dear.ui.databinding.ItemWapFragmentLayoutBinding

class ItemWapFragment : BaseFragment<ItemWapFragmentLayoutBinding>() {

    companion object {

        const val PARAM_POS = "param_position"

        @JvmStatic
        fun getInstance(position: Int): ItemWapFragment {
            val fragment = ItemWapFragment()
            fragment.arguments = bundleOf(Pair(PARAM_POS, position))
            return fragment
        }
    }

    private var mPosition = -1

    override fun initParams() {
        mPosition = arguments?.getInt(PARAM_POS) ?: -1
    }

    override fun initView() {
        mDB.webView.settings.javaScriptEnabled = true
        mDB.webView.settings.cacheMode = WebSettings.LOAD_DEFAULT

        mDB.webView.loadUrl("https://www.baidu.com")
    }

}