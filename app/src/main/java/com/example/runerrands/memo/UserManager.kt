package com.example.runerrands.memo

import com.example.runerrands.model.bean.MyUser

object UserManager {

    private var mMyUser: MyUser? = null

    fun saveUser(myUser: MyUser){
        mMyUser = myUser
    }

    fun getUser() = mMyUser

    fun removeUser(){
        mMyUser = null
    }
}