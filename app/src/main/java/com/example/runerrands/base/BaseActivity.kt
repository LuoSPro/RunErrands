package com.example.runerrands.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.runerrands.R
import com.xuexiang.xui.XUI
import com.xuexiang.xui.utils.StatusBarUtils

open abstract class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XUI.initTheme(this)
        StatusBarUtils.initStatusBarStyle(this,false,ActivityCompat.getColor(this, R.color.main_blue))
        //init()
    }
    abstract fun initView()
    abstract fun initDate()
    abstract fun initEvent()
    open fun initPresenter(){

    }
}