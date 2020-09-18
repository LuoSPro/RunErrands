package com.example.runerrands.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.runerrands.R
import com.example.runerrands.base.BaseActivity
import com.example.runerrands.memo.UserManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_bottom_home.*

class HomeActivity : BaseActivity() {
    private lateinit var mData: MutableList<Int>
    private lateinit var mBehavior: BottomSheetBehavior<View>
    private lateinit var mLinTake: LinearLayout
    private lateinit var mIvClose: ImageView
    private val mIntent = Intent()

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
        mBehavior = BottomSheetBehavior.from(nes_home)
        //起始状态是隐藏
        mBehavior.isHideable = true
        mBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        mLinTake = nes_home.findViewById(R.id.lin_take_home)
        mIvClose = nes_home.findViewById(R.id.iv_close_home)
    }

    override fun initDate() {
        val user = UserManager.getUser()
        tv_nickName_home.text = user?.name
        tv_money_home.text = user?.money.toString()
        //增加轮播图的数据
        mData.add(R.drawable.ima_51)
        mData.add(R.drawable.ima_51)
        mData.add(R.drawable.ima_51)
    }

    override fun initEvent() {
        tv_up_home.setOnClickListener {
            if (mBehavior.state != BottomSheetBehavior.STATE_EXPANDED){
                mBehavior.state = BottomSheetBehavior.STATE_EXPANDED;
            }
        }
        mIvClose.setOnClickListener {
            if (mBehavior.state != BottomSheetBehavior.STATE_HIDDEN){
                mBehavior.state = BottomSheetBehavior.STATE_HIDDEN;
            }
        }
        lin_take_home.setOnClickListener {
            mIntent.setClass(this,TakeActivity::class.java)
            startActivity(mIntent)
        }
    }

    override fun initPresenter() {

    }
}