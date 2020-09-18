package com.example.runerrands.memo

import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.geocode.GeoCoder
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener
import com.baidu.mapapi.search.sug.SuggestionSearch
import com.baidu.mapapi.search.sug.SuggestionSearchOption
import com.example.runerrands.model.bean.SelectInfo

class BDManager() {

    private val reverseGeoCodeOption: ReverseGeoCodeOption = ReverseGeoCodeOption()
    private val geoCoder: GeoCoder = GeoCoder.newInstance()
    private lateinit var mOnGetGeoCoderResultListener: OnGetGeoCoderResultListener
    private val suggestionSearChOption: SuggestionSearchOption = SuggestionSearchOption()
    private val suggestionSearch: SuggestionSearch = SuggestionSearch.newInstance()
    private lateinit var onResultListener: OnResultListener
    private val mList: MutableList<SelectInfo> = mutableListOf()

    fun start(){

    }

    fun startPoi(key: String){
        suggestionSearch.requestSuggestion(suggestionSearChOption.citylimit(true).city("成都").keyword(key))
    }

    fun setOnResultListener(onResultListener: OnResultListener){
        this.onResultListener = onResultListener
        val onGetSuggestionResultListener = OnGetSuggestionResultListener {
            val allSuggestions = it.allSuggestions
            if (allSuggestions.size > 0){
                for (suggestion in allSuggestions) {
                    val selectInfo = SelectInfo(suggestion.key,suggestion.city+suggestion.district)
                    suggestion.pt?.let {
                        selectInfo.latitude = it.latitude
                        selectInfo.longitude = it.longitude
                    }
                    mList.add(selectInfo)
                }
                onResultListener.resultLoaded(mList)
            }
        }
        suggestionSearch.setOnGetSuggestionResultListener(onGetSuggestionResultListener)
    }

    interface OnResultListener{
        fun resultLoaded(list: MutableList<SelectInfo>)
    }

    fun setOnGetCodeResultListener(onGetGeoCoderResultListener: OnGetGeoCoderResultListener){
        mOnGetGeoCoderResultListener = onGetGeoCoderResultListener
    }

    fun setLatLng(latLng: LatLng) {
        reverseGeoCodeOption.location(latLng)
        geoCoder.apply {
            reverseGeoCode(reverseGeoCodeOption)
            setOnGetGeoCodeResultListener(mOnGetGeoCoderResultListener)
        }
    }

}