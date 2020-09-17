package com.example.runerrands.ui.activity

import android.animation.IntEvaluator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.core.app.ActivityCompat
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListListener
import cn.bmob.v3.listener.QueryListener
import com.example.runerrands.R
import com.example.runerrands.base.BaseActivity
import com.example.runerrands.bmob.BBManager
import com.example.runerrands.persenter.ILoginPresenter
import com.example.runerrands.persenter.impl.LoginPresenterImpl
import com.example.runerrands.utils.Constants
import com.example.runerrands.view.LoginCallback
import com.xuexiang.xui.utils.WidgetUtils
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog
import com.xuexiang.xui.widget.edittext.verify.VerifyCodeEditText
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.android.synthetic.main.activity_check.*
import java.lang.ref.WeakReference
import java.util.*

class CheckActivity:  BaseActivity(),LoginCallback{
    private lateinit var phone: String
    //发送完短信后倒计时60s
    private var count = 60
    private lateinit var mLoginPresenter: ILoginPresenter
    lateinit var miniLoadingDialog: MiniLoadingDialog
    //标识是否发送
    var isSend: Boolean = true

    var handler = FHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)
        init()
    }

    fun init(){
        initView()
        initPresenter()
        initDate()
        initEvent()
    }

    override fun initPresenter() {
        mLoginPresenter = LoginPresenterImpl()
        mLoginPresenter.registerPresenter(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mLoginPresenter.unRegisterPresenter(this)
    }

    override fun initView() {
        miniLoadingDialog = WidgetUtils.getMiniLoadingDialog(this)
        miniLoadingDialog.setDialogSize(200,200)
    }

    override fun initDate() {
        phone = intent.getStringExtra("phone")
        tv_phone_check.text = phone
    }

    override fun initEvent() {
        iv_close_check.setOnClickListener {
            finish()
        }
        iv_up_check.setOnClickListener {
            finish()
        }
        btn_send_check.setOnClickListener {
            sendCode()
        }
        et_code_check.setOnInputListener(object : VerifyCodeEditText.OnInputListener{
            override fun onComplete(input: String?) {
                //输入完成的时候
                checkPhoneCode(input)
            }

            override fun onChange(input: String?) {
                //输入的时候
            }

            override fun onClear() {
                //输入清空的时候调用
            }

        })
    }

    private fun checkPhoneCode(code: String?) {
        miniLoadingDialog.show()
        mLoginPresenter.checkPhoneCode(phone,code!!,this)
    }

    fun sendCode(){
        BBManager.sendCode(phone,object : QueryListener<Int>(){
            override fun done(integer: Int?, e: BmobException?) {
                if (e == null){
                    XToast.success(this@CheckActivity,"发送成功").show()
                }else{
                    XToast.success(this@CheckActivity,"发送失败").show()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        handler.sendEmptyMessage(count)
    }

    /**
     * 这里使用 静态内部类 + 弱引用 的形式
     * 优点：避免造成内存泄漏
     * 这里使用静态内部类，它默认不持有外部类的引用
     * 弱引用：弱引用拥有极端的生命周期，在GC线程扫描时，一旦发现了只具有弱引用的对象，不管内存空间是否足够，都会将其回收
     */
    inner class FHandler(activity: Activity) : Handler(){
        var reference: WeakReference<Activity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            when(msg.what){
                1 -> {
                    if (isSend){
                        btn_send_check.apply {
                            isEnabled = true
                            text = "重新发送"
                            setBackgroundColor(ActivityCompat.getColor(this@CheckActivity,R.color.main_blue))
                            isSend = false
                            count = 60
                        }
                    }
                }
                10001 -> {
                    miniLoadingDialog.dismiss()
                    setResult(RESULT_OK)//这里调用setResult之后，会跳到LoginActivity的onActivityResult()的地方
                    finish()
                }
                else -> {
                    if (isSend){
                        count--
                        btn_send_check.text = "s$count"
                        handler.sendEmptyMessageDelayed(count, 1000)
                    }
                }
            }
        }
    }

    override fun done(id: String) {
        //将这个id存在本地，下次登录时，直接用这个id去服务器获取数据
        putUserId(id);
    }

    private fun putUserId(id: String) {
        val sharedPreferences = getSharedPreferences(Constants.SP_USER, MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.putString(Constants.USER_ID,id)
        edit.apply()
        //handler发送一个消息
        handler.sendEmptyMessageDelayed(10001,500)
    }

    override fun error(msg: String?, errorCode: Int) {
        miniLoadingDialog.dismiss()
        XToast.warning(this,msg+errorCode)
        Log.e("CheckActivity","错误信息:${msg}  错误码：${errorCode}")
    }
}