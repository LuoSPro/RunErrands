package com.example.runerrands.model.bean

data class AddressInfo(var type: Int, var address: String, val name: String, val phone: String,
                       val sex: String, val latitude: Double, val longitude: Double) {
}