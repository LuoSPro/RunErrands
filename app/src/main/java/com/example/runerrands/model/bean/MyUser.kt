package com.example.runerrands.model.bean

import cn.bmob.v3.BmobObject

data class MyUser(val name:String, val phone: String, val phoneUrl: String, val payPwd: String, val money: Double,
                  val upMoney: Double): BmobObject() {
    

}