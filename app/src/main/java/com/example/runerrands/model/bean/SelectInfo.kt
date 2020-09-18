package com.example.runerrands.model.bean

data class SelectInfo(var adders: String, var city: String, var latitude: Double, var longitude: Double){
    constructor(adders: String,city: String): this(adders,city,0.0,0.0)

    constructor():this("","")
}