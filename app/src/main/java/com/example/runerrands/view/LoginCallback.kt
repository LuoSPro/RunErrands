package com.example.runerrands.view

interface LoginCallback {

    fun done(id: String)

    fun error(msg: String?, errorCode: Int)

}