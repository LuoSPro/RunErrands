package com.example.runerrands.memo

import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.model.LatLng

class BDMap(context: Context) {
    private val mContext = context
    private lateinit var mLocationClient: LocationClient
    private var mBaiduMap: BaiduMap? = null

    fun location(time: Int,map: BaiduMap?){
        this.mBaiduMap= map
        mBaiduMap?.isMyLocationEnabled = true
        mLocationClient = LocationClient(mContext)
        val option: LocationClientOption = LocationClientOption()
        option.apply {
            setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            setCoorType("bd09ll")
            setScanSpan(time)
            setIsNeedAddress(true)
            setIsNeedLocationDescribe(true)
            isOpenGps = true
        }
        mLocationClient.locOption = option
        mLocationClient.registerLocationListener(object : BDAbstractLocationListener() {

            override fun onReceiveLocation(p0: BDLocation?) {
                val latLng: LatLng = LatLng(38.9433671045255,117.36299499999993)
                mBaiduMap?.apply {
                    setMyLocationData(MyLocationData.Builder().latitude(latLng.latitude).longitude(latLng.longitude).build())
                    setMapStatus(MapStatusUpdateFactory.newLatLng(latLng))
                    animateMapStatus(MapStatusUpdateFactory.zoomTo(18f))
                }
            }
        })
    }

    fun  start(){
        mLocationClient.start()
    }

    fun stop(){
        mLocationClient.stop()
    }
    fun destroy(){
        mLocationClient.stop()
        mBaiduMap?.isMyLocationEnabled = false
        mBaiduMap?.let {
            mBaiduMap = null
        }
    }

    fun dist(dist: Double): String{
        val str: String = dist.toString()
        val of: Int = str.indexOf(".")
        if (of != -1){
            return str.substring(0,of + 2)
        }
        return str
    }
}