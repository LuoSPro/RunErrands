package com.example.runerrands.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.baidu.mapapi.model.LatLng
import com.example.runerrands.R
import com.example.runerrands.base.BaseActivity
import com.example.runerrands.databinding.ActivityEditBinding
import com.example.runerrands.room.bean.Address
import com.example.runerrands.model.bean.LiveDataBus
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.activity_edit.*
import kotlin.collections.HashMap

class EditActivity: BaseActivity() {
    private lateinit var mBinding: ActivityEditBinding
    private val mIntent = Intent()
    private var mSex: String = "男士"
    private lateinit var mLatLng: LatLng
    private var type: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        init()
    }

    private fun init() {
        initView()
        initDate()
        initPresenter()
        initEvent()
    }

    private fun startActivity(){
        mIntent.setClass(this,SelectActivity::class.java)
        mIntent.putExtra("type",type)
        startActivity(mIntent)
    }

    private fun complete(){
        val selectAddress = et_select_edit.text.toString()
        val detailAddress = et_address_edit.text.toString()
        val contact = et_name_edit.text.toString()
        val phone = et_phone_edit.text.toString()
        if (TextUtils.isEmpty(selectAddress)){
            XToast.warning(this,"请选择地址").show()
            return
        }else if (TextUtils.isEmpty(detailAddress)){
            XToast.warning(this,"详细地址为空").show()
            return
        }else if (TextUtils.isEmpty(contact)){
            XToast.warning(this,"联系人为空").show()
            return
        }else if (TextUtils.isEmpty(phone)){
            XToast.warning(this,"手机号为空").show()
            return
        }else if (!box_man_edit.isChecked && !box_miss_edit.isChecked){
            XToast.warning(this,"性别为空").show()
            return
        }
        val address = Address(1,selectAddress+detailAddress,contact,phone,mSex,mLatLng.latitude,mLatLng.longitude)
        LiveDataBus.get().with("TakeActivity").setStickyData(address)
        finish()
    }

    override fun initView() {
        mBinding = DataBindingUtil.setContentView(this@EditActivity, R.layout.activity_edit)
    }

    override fun initDate() {

    }

    override fun initEvent() {
        LiveDataBus.get().with("EditActivity").observerSticky(this,EditActivityObserver(),true)
        box_man_edit.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                mSex = "先生"
                box_miss_edit.isChecked = false
            }
        }
        box_miss_edit.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                mSex = "女士"
                box_man_edit.isChecked = false
            }
        }
        btn_edit.setOnClickListener {
            complete()
        }
        et_select_edit.setOnClickListener {
            startActivity()
        }
        iv_close_edit.setOnClickListener {
            finish()
        }
    }

    private inner class EditActivityObserver: Observer<HashMap<String,Any>>{
        override fun onChanged(hashMap: HashMap<String, Any>?) {
            val address = hashMap?.get("address")
            mLatLng = hashMap?.get("latLng") as LatLng
            mBinding.etSelectEdit.setText(address.toString())
            Log.e("EditActivity","纬度  ==> ${mLatLng.latitude}")
            Log.e("EditActivity","经度  ==> ${mLatLng.longitude}")
        }

    }
}