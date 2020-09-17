package com.example.runerrands.persenter.impl

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.example.runerrands.bmob.BBManager
import com.example.runerrands.memo.UserManager
import com.example.runerrands.model.bean.MyUser
import com.example.runerrands.persenter.ILoginPresenter
import com.example.runerrands.view.LoginCallback

class LoginPresenterImpl: ILoginPresenter {

    var mLoginCallback: LoginCallback? = null
    lateinit var mPhone: String

    override fun checkPhoneCode(phone: String, code: String, callback: LoginCallback){
        mLoginCallback = callback
        this.mPhone = phone
        BBManager.checkPhoneCode(phone,code,object : UpdateListener(){
            override fun done(e: BmobException?) {
                if (e == null){//发送成功
                    findUserByPhone()
                }else{
                    mLoginCallback?.error(e.message,e.errorCode)
                }
            }
        })
    }

    override fun registerPresenter(callback: LoginCallback) {
        mLoginCallback = callback
    }

    override fun unRegisterPresenter(callback: LoginCallback) {
        mLoginCallback = null
    }

    private fun findUserByPhone() {
        val query = BmobQuery<MyUser>()
        query.addWhereEqualTo("phone",mPhone)
        query.findObjects(object : FindListener<MyUser>(){
            override fun done(p: MutableList<MyUser>?, e: BmobException?) {
                if (e == null){
                    if (p?.size!! > 0){//用户已注册
                        val myUser = p[0]
                        UserManager.saveUser(myUser)
                        UserManager.getUser()?.objectId?.let { mLoginCallback?.done(it) }
                    }else{//没有注册过
                        createUser()
                    }
                }else{//查找失败
                    mLoginCallback?.error(e.message,e.errorCode)
                }
            }
        })
    }

    private fun createUser() {
        var myUser = MyUser("感同身受",mPhone,"null","0000",0.0,0.0)
        myUser.save(object : SaveListener<String>(){
            override fun done(s: String?, e: BmobException?) {
                if (e == null){
                    UserManager.saveUser(myUser)
                    UserManager.getUser()?.objectId?.let { mLoginCallback?.done(it) }
                }else{
                    mLoginCallback?.error(e.message,e.errorCode)
                }
            }
        })
    }


}