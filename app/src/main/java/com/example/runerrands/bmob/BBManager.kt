package com.example.runerrands.bmob

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobSMS
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.UpdateListener
import com.example.runerrands.model.bean.MyUser

/**
 * 将class换成object，就相当于使用了单例模式
 */
object BBManager {

    /**
     * 通过id查询用户信息
     */
    fun findUser(id: String,listener: QueryListener<MyUser>){
        var query: BmobQuery<MyUser> = BmobQuery()
        query.getObject(id,listener)
    }

    /**
     * 发送验证码
     */
    fun sendCode(phone: String,listener: QueryListener<Int>): Unit{
        BmobSMS.requestSMSCode(phone,"",listener)
    }

    /**
     * 验证验证码
     */
    fun checkPhoneCode(phone: String,code: String,listener: UpdateListener){
        BmobSMS.verifySmsCode(phone,code,listener)
    }
}