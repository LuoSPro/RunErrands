package com.example.runerrands.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import com.example.runerrands.R
import com.example.runerrands.base.BaseActivity
import com.example.runerrands.bmob.BBManager
import com.example.runerrands.memo.UserManager
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: BaseActivity(){
    lateinit var phone: String
    lateinit var mIntent: Intent
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init(){
        initView()
        initPresenter()
        initDate()
        initEvent()
    }

    override fun initView() {

    }

    override fun initDate() {
        mIntent = Intent()
    }

    override fun initEvent() {
        iv_close_login.setOnClickListener{
            finish()
        }
        iv_send_login.setOnClickListener{
            sendCode()
        }
    }

    private fun sendCode() {
        phone = et_phone_login.text.toString()
        if (phone.length != 11){
            XToast.warning(this,"手机号错误").show()
        }
        /**
         * 如果是自定义短信模板，此处替换为你在控制台设置的自定义短信模板名称；如果没有对应的自定义短信模板，则使用默认短信模板。
         */
        BBManager.sendCode(phone,object: QueryListener<Int>() {
            override fun done(smsId: Int?, e: BmobException?) {
                if (e == null) {
                    mIntent.setClass(this@LoginActivity,CheckActivity::class.java)
                    mIntent.putExtra("phone",phone)
                    startActivityForResult(mIntent,1000)
                } else {
                    XToast.warning(this@LoginActivity,"失败    "+ e.errorCode).show()
                }
            }

        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && requestCode == RESULT_OK){
            mIntent.setClass(this@LoginActivity,HomeActivity::class.java)
            startActivity(intent)
            finish()
            Log.e(TAG, UserManager.getUser()?.objectId)
        }
    }
}