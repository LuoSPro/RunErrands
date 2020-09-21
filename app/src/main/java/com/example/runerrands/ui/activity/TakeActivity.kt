package com.example.runerrands.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.model.LatLng
import com.example.runerrands.R
import com.example.runerrands.base.BaseActivity
import com.example.runerrands.databinding.ActivityTakeBinding
import com.example.runerrands.memo.BDMap
import com.example.runerrands.room.bean.Address
import com.example.runerrands.model.bean.LiveDataBus
import com.example.runerrands.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.activity_take.*
import kotlinx.android.synthetic.main.layout_bottom_take.*

/**
 * 展示个人定位(地图)
 */
class TakeActivity: BaseActivity() {
    private var mBaiduMap: BaiduMap? = null
    private lateinit var mLocationClient: LocationClient
    private var isFirst: Boolean = true
    private lateinit var mBinding: ActivityTakeBinding
    private var type: Int = 1
    private val mIntent: Intent = Intent()
    private lateinit var mMap: BDMap
    private lateinit var mBehaviour: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take)
        init()
    }

    private fun init() {
        initView()
        initDate()
        initEvent()
        initPresenter()
    }

    override fun initView() {
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_take)
        mBinding.include.apply {
            takeActivity = this@TakeActivity
            str = "联系人"
        }
    }

    override fun initDate() {
        mBaiduMap = mapView.map
        mMap = BDMap(this)
        mMap.location(0,mBaiduMap)
        mMap.start()
        mBinding.map = mMap


        mBehaviour = BottomSheetBehavior.from(nes_take)
        mBehaviour.apply {
            isHideable = false
            peekHeight = 550
        }

//        mBaiduMap?.isMyLocationEnabled = true
//        mLocationClient = LocationClient(this)
//        var option = LocationClientOption()
//        //设置定位模式
//        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy//高精度
//        option.apply {
//            setCoorType("bd0911")//设置返回定位结果坐标系
//            setScanSpan(0)//设置发器连续定位请求得间隔需要大于等于多少秒才是有效的
//            setIsNeedAddress(true)
//            setIsNeedLocationDescribe(true)//需要地址描述
//            //isIgnoreKillProcess=true//定位SDK内部是一个Service，并放到独立线程，设置是否在stop的时候杀死这个进程，默认不杀死
//            isLocationNotify = true//设置解耦是否当gps有效时，按照1s1次频率输出GPS结果
//            isOpenGps = true//是否打开GPS
//        }
//        mLocationClient.apply {
//            locOption = option
//            registerLocationListener(MyLocationListener())
//            mLocationClient.start()
//        }
    }

    inner class MyLocationListener: BDAbstractLocationListener(){
        override fun onReceiveLocation(bdLocation: BDLocation?) {
            if (bdLocation == null){
                return
            }
            Log.e("TakeActivity","当前地址${bdLocation.addrStr}")
            //定位的坐标和地址信息都会从这里返回
            val myLocationData = MyLocationData.Builder()
                .accuracy(0F)
                .direction(100f)
                .latitude(bdLocation.latitude)
                .longitude(bdLocation.longitude)
                .build()
            mBaiduMap?.setMyLocationData(myLocationData)
            if (isFirst){
                val latLng = LatLng(bdLocation.latitude, bdLocation.longitude)
                val update = MapStatusUpdateFactory.newLatLng(latLng)
                val zoomTo = MapStatusUpdateFactory.zoomTo(18f)//缩放
                mBaiduMap?.apply {
                    setMapStatus(update)
                    animateMapStatus(zoomTo)
                }
                isFirst = false
            }
            mLocationClient.stop()
        }

    }

    fun startActivity(num: Int){
        when (num) {
            Constants.TO_EDIT_ACTIVITY -> {
                mIntent.setClass(this,EditActivity::class.java)
                startActivity(mIntent)
            }
            Constants.TO_ADDRESS_ACTIVITY -> {
                //TODO:测试位置，记得取消
//                if (TextUtils.isEmpty(mBinding.include.tvTakeAddress.text)){
//                    XToast.warning(this,"填写取件地址").show();
//                    return
//                }
                mIntent.apply {
                    setClass(this@TakeActivity,AddressActivity::class.java)
                    putExtra(Constants.ACTIVITY_ADDRESS,Constants.TO_ADDRESS)
                }
                startActivity(mIntent)
            }
            else -> {
//                mIntent.setClass(this,RemarksActivity::class.java)
//                startActivity(mIntent)
            }
        }
    }

    fun commit(){

    }

    fun showBottomDialog(){

    }


    override fun initEvent() {
        LiveDataBus.get().with("TakeActivity").observerSticky(this,TakeActivityObserver(),true)
        iv_close_take.setOnClickListener {
            finish()
        }
        rl_collect.setOnClickListener {
            startActivity(Constants.TO_ADDRESS_ACTIVITY)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        mLocationClient.stop()
        mBaiduMap?.isMyLocationEnabled = false
        mBaiduMap = null//防止内存泄漏
        mMap.destroy()
    }

    private inner class TakeActivityObserver: Observer<Address> {
        override fun onChanged(address: Address) {
            if (address.type == 1){
                mBinding.include.takeAddress = address
            }else{
                mBinding.include.collectAddress = address
            }
        }

    }
}