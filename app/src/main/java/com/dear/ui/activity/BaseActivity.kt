package com.dear.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.dear.ui.utils.ViewBindingUtil

abstract class BaseActivity<DB : ViewBinding> : AppCompatActivity() {

    protected lateinit var mDB: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDB = ViewBindingUtil.inflateWithGeneric(this,layoutInflater)
        setContentView(mDB.root)
        initView()
        initListener()
        initData()
    }

    open fun initView() {

    }

    open fun initListener() {

    }

    open fun initData() {

    }
}