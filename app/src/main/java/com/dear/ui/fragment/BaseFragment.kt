package com.dear.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.dear.ui.utils.ViewBindingUtil

abstract class BaseFragment<DB : ViewBinding> : Fragment() {

    protected lateinit var mDB: DB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDB = ViewBindingUtil.inflateWithGeneric(this, inflater, container, false)
        return mDB.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initParams()
        initView()
        initEvent()
        initData()
    }

    open fun initParams() {

    }

    open fun initView() {

    }

    open fun initEvent() {

    }

    open fun initData() {

    }
}