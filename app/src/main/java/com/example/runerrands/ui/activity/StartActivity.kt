package com.example.runerrands.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.widget.Toast
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import com.example.runerrands.R
import com.example.runerrands.base.BaseActivity
import com.example.runerrands.bmob.BBManager
import com.example.runerrands.memo.UserManager
import com.example.runerrands.model.bean.MyUser
import com.example.runerrands.ui.custom.CustomDialog
import com.example.runerrands.utils.Constants
import com.permissionx.guolindev.PermissionX
import com.xuexiang.xui.widget.toast.XToast
import java.lang.ref.WeakReference

class StartActivity: BaseActivity() {
    var mHandler: FHandler = FHandler(this)
    private lateinit var mIntent: Intent
    private lateinit var mUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        mIntent = Intent()
        val sharedPreferences = getSharedPreferences(Constants.SP_USER, MODE_PRIVATE)
        mUserId = sharedPreferences.getString(Constants.USER_ID, "").toString()
        if (!TextUtils.isEmpty(mUserId)){//查询到了用户id，就发送一个1000的handler
            findUser()
            mHandler.sendEmptyMessageDelayed(1000,3000)
        }else{//没有查询到用户id，就发送一个1001的handler
            mHandler.sendEmptyMessageDelayed(1001,3000)
        }
        init()
    }

    private fun init(){
        initView()
        initPresenter()
        initDate()
        initEvent()
    }

    private fun findUser() {
        BBManager.findUser(mUserId,object : QueryListener<MyUser>(){
            override fun done(user: MyUser?, e: BmobException?) {
                if (e == null){
                    user?.let { UserManager.saveUser(it) }
                    mHandler.sendEmptyMessageDelayed(1000,2000)
                }else{
                    XToast.warning(this@StartActivity,"失败  ${e.errorCode}").show()
                }
            }
        })
    }

    override fun initView() {

    }

    override fun initDate() {
        PermissionX.init(this)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE)
            .onExplainRequestReason { scope, deniedList, _ ->
                val message = "PermissionX needs following permissions to continue"
                scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
            }
            .onForwardToSettings { scope, deniedList ->
                val message = "Please allow following permissions in settings"
                val dialog = CustomDialog(this,message, deniedList)
                scope.showForwardToSettingsDialog(dialog)
            }
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun initEvent() {

    }

    inner class FHandler(activity: Activity): Handler(){
        var reference: WeakReference<Activity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                1000 -> {//查询到了就直接跳转首页
                    mIntent.setClass(this@StartActivity,HomeActivity::class.java)
                    startActivity(mIntent)
                    finish()
                }
                else -> {//没有查到id，就返回登录页
                    mIntent.setClass(this@StartActivity,LoginActivity::class.java)
                    startActivity(mIntent)
                    finish()
                }
            }
        }
    }
}