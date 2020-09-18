package com.example.runerrands.ui.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.GeoCodeResult
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult
import com.example.runerrands.R
import com.example.runerrands.base.BaseActivity
import com.example.runerrands.databinding.ActivitySelectBinding
import com.example.runerrands.memo.BDManager
import com.example.runerrands.model.bean.LiveDataBus
import com.example.runerrands.model.bean.SelectInfo
import com.example.runerrands.ui.adapter.SelectRecyclerAdapter
import java.util.*
import kotlin.collections.HashMap

class SelectActivity: BaseActivity() {
    private lateinit var mBinding: ActivitySelectBinding
    private lateinit var mBaiduMap: BaiduMap
    private lateinit var mBdManager: BDManager
    private lateinit var mLatLng: LatLng
    private lateinit var mRvAdapter: SelectRecyclerAdapter
    private lateinit var mInputMethodManager: InputMethodManager
    private lateinit var address: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
        init()
    }

    private fun init() {
        initView()
        initPresenter()
        initDate()
        initEvent()
    }

    override fun initView() {
        mBinding = DataBindingUtil.setContentView(this@SelectActivity, R.layout.activity_select)
        mBaiduMap = mBinding.mapView.map
        mBdManager = BDManager()
        mRvAdapter = SelectRecyclerAdapter()
        mInputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mBinding.selectActivity = this
        //拖动地图会返回给你经纬度
        mBaiduMap.isMyLocationEnabled = true
    }

    override fun initDate() {
        //适配器数据初始化
        mBinding.recSelect.apply {
            layoutManager = LinearLayoutManager(this@SelectActivity,LinearLayoutManager.VERTICAL,false)
            adapter = mRvAdapter
        }

        //模拟数据
        val latLng = LatLng(38.9433671045255,117.36299499999993)
        mBaiduMap.apply {
            setMapStatus(MapStatusUpdateFactory.newLatLng(latLng))
            animateMapStatus(MapStatusUpdateFactory.zoomTo(18f))
        }
    }

    override fun initEvent() {
        //监听搜索框的输入
        mBinding.etContentSelect.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! > 0){
                    //当搜索框有输入时
                    //传入数据
                    mBdManager.startPoi(s.toString())
                    //设置RV可见
                    mBinding.recSelect.visibility = View.VISIBLE
                }else{
                    gone()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        //接口回调，将搜索后的结果
        mBdManager.setOnResultListener(object : BDManager.OnResultListener{
            override fun resultLoaded(list: MutableList<SelectInfo>) {
                //将搜索出来的结果给适配器使用
                mRvAdapter.setData(list)
            }

        })
        mBaiduMap.setOnMapStatusChangeListener(object : BaiduMap.OnMapStatusChangeListener {
            override fun onMapStatusChangeStart(p0: MapStatus?) {

            }

            override fun onMapStatusChangeStart(p0: MapStatus?, p1: Int) {

            }

            override fun onMapStatusChange(p0: MapStatus?) {

            }

            override fun onMapStatusChangeFinish(mapStatus: MapStatus?) {
                //地图状态改变完之后
                mapStatus?.let {
                    //保存坐标
                    mLatLng = it.target
                    //给地图设置坐标
                    mBdManager.setLatLng(mLatLng)
                }
                mBinding.apply {
                    tvLatitudeSelect.text = mLatLng.latitude.toString()
                    tvLongitudeSelect.text = mLatLng.longitudeE6.toString()
                }
                gone()
            }
        })

        mBdManager.setOnGetCodeResultListener(object : OnGetGeoCoderResultListener{
            override fun onGetGeoCodeResult(geoCodeResult: GeoCodeResult) {

            }

            //返回地址信息
            override fun onGetReverseGeoCodeResult(reverseGeoCodeResult: ReverseGeoCodeResult?) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR){
                    Log.e("SelectActivity","没有地址")
                }else{
                    //将地址设置到view显示
                    mBinding.tvAddressSelect.text = reverseGeoCodeResult.address
                    address = reverseGeoCodeResult.address
                }
            }

        })

        mRvAdapter.mListener = object : SelectRecyclerAdapter.OnItemClickListener{
            override fun onClick(selectInfo: SelectInfo) {
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(mLatLng))
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(18f))//缩放
            }
        }
    }

    fun hideBehavior(){

    }

    fun addAddress(){

    }

    fun gone(){
        mBinding.apply {
            //将搜索结果框隐藏
            recSelect.visibility = View.GONE
            //关闭按钮隐藏
            ivCloseSelect.visibility = View.GONE
            //隐藏键盘
            mInputMethodManager.hideSoftInputFromWindow(mBinding.etContentSelect.windowToken,0)
        }
    }

    fun complete(){
        val hashMap = HashMap<String, Any>()
        hashMap["address"] = address
        hashMap["latLng"] = mLatLng
        //把数据传到EditActivity去
        LiveDataBus.get().with("EditActivity").setStickyData(hashMap)
        finish()
    }

    override fun onResume() {
        super.onResume()
        //同步生命周期
        mBinding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.mapView.onDestroy()
    }
}