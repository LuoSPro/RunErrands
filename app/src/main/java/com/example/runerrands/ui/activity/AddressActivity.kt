package com.example.runerrands.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runerrands.R
import com.example.runerrands.base.BaseActivity
import com.example.runerrands.model.bean.LiveDataBus
import com.example.runerrands.room.bean.Address
import com.example.runerrands.room.viewmodel.AddressViewModel
import com.example.runerrands.ui.adapter.AddressAdapter
import com.example.runerrands.utils.Constants
import kotlinx.android.synthetic.main.activity_address.*

class AddressActivity: BaseActivity() {
    private val mAddressAdapter: AddressAdapter = AddressAdapter()
    private lateinit var mViewModel: AddressViewModel
    private val mIntent = Intent()
    private var type = 1
    private val mTYPE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        init()
    }

    private fun init() {
        initDate()
        initView()
        initEvent()
        initPresenter()
    }

    override fun initView() {

    }

    override fun initDate() {
        type = intent.getIntExtra(Constants.ACTIVITY_ADDRESS,1)
        val linearLayoutManager = LinearLayoutManager(this)
        rec_address.apply {
            layoutManager = linearLayoutManager
            adapter = mAddressAdapter
        }
        mViewModel = AddressViewModel(this)
        mViewModel.addressLive.observe(this, Observer{
            mAddressAdapter.setData(it as MutableList<Address>)
        })
    }

    override fun initEvent() {
        tv_add_address.setOnClickListener {
            mIntent.setClass(this,SelectActivity::class.java)
            mIntent.putExtra("type",type)
            startActivity(mIntent)
        }
        //选择收获地址
        mAddressAdapter.setOnItemClickListener(object : AddressAdapter.OnItemClickListener{
            override fun onClick(address: Address) {
                if (type == 1){


                }else {
                    address.type = 2
                    LiveDataBus.get().with("TakeActivity").setStickyData(address)
                    finish()
                }
            }
        })
    }
}