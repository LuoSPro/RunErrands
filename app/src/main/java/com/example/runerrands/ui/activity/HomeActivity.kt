package com.example.runerrands.ui.activity

import android.os.Bundle
import android.view.View
import com.example.runerrands.R
import com.example.runerrands.base.BaseActivity
import com.example.runerrands.memo.UserManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {
    private lateinit var mData: MutableList<Int>
    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }

    private fun init() {
        initView()
        initPresenter()
        initDate()
        initEvent()
    }

    override fun initView() {
        behavior = BottomSheetBehavior.from(nes_home)
        //起始状态是隐藏
        behavior.isHideable = true
    }

    override fun initDate() {
        val user = UserManager.getUser()
        tv_nickName_home.text = user?.name
        tv_money_home.text = user?.money.toString()
        mData.add(R.drawable.ima_51)
        mData.add(R.drawable.ima_51)
        mData.add(R.drawable.ima_51)
    }

    override fun initEvent() {

    }

    override fun initPresenter() {

    }
}