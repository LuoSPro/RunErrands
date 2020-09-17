package com.example.runerrands.app

import android.app.Application
import cn.bmob.v3.Bmob
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.xuexiang.xui.XUI

class App: Application() {

    private val APP_KEY = "3116a8be297ff13a3d15bb00babb2c74"

    override fun onCreate() {
        super.onCreate()
        //初始化Bmob
        Bmob.initialize(this,APP_KEY)
        //初始化XUI
        XUI.init(this)
        //XUI.debug(true)
        //百度SDK初始化
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //SDKInitializer.initialize(this)
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        //SDKInitializer.setCoordType(CoordType.BD09LL)
    }
}