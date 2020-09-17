package com.example.runerrands.persenter

import com.example.runerrands.view.LoginCallback

interface ILoginPresenter {

    fun checkPhoneCode(phone: String,code: String,callback: LoginCallback)

    fun registerPresenter(callback: LoginCallback)

    fun unRegisterPresenter(callback: LoginCallback)
}