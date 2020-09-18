package com.example.runerrands.ui.activity

import android.os.Bundle
import android.util.Log
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapStatusUpdate
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.model.LatLng
import com.example.runerrands.R
import com.example.runerrands.base.BaseActivity
import kotlinx.android.synthetic.main.activity_take.*

/**
 * 展示个人定位(地图)
 */
class TakeActivity: BaseActivity() {
    private var mBaiduMap: BaiduMap? = null
    private lateinit var mLocationClient: LocationClient
    var isFirst: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take)
    }
    override fun initView() {

    }

    override fun initDate() {
        mBaiduMap = mapView.map
        mBaiduMap?.isMyLocationEnabled = true
        mLocationClient = LocationClient(this)
        var option = LocationClientOption()
        //设置定位模式
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy//高精度
        option.apply {
            setCoorType("bd0911")//设置返回定位结果坐标系
            setScanSpan(0)//设置发器连续定位请求得间隔需要大于等于多少秒才是有效的
            setIsNeedAddress(true)
            setIsNeedLocationDescribe(true)//需要地址描述
            //isIgnoreKillProcess=true//定位SDK内部是一个Service，并放到独立线程，设置是否在stop的时候杀死这个进程，默认不杀死
            isLocationNotify = true//设置解耦是否当gps有效时，按照1s1次频率输出GPS结果
            isOpenGps = true//是否打开GPS
        }
        mLocationClient.apply {
            locOption = option
            registerLocationListener(MyLocationListener())
            mLocationClient.start()
        }
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

    }

    fun commit(){

    }

    fun showBottomDialog(){

    }


    override fun initEvent() {

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
    }
}