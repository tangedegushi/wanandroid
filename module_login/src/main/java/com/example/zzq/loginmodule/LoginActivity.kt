package com.example.zzq.loginmodule

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.zzq.loginmodule.model.LoginModel
import com.zzq.commonlib.bar.UltimateBar
import com.zzq.commonlib.base.BaseActivity
import com.zzq.netlib.utils.UtilApp
import com.zzq.netlib.utils.UtilSp

import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via et_userName/et_password.
 */
class LoginActivity : BaseActivity() {
    private var valueUserName: String? = null
    private var valuePassword: String? = null
    private lateinit var et_userNameStr: String
    private lateinit var et_passwordStr: String
    private val loginModel by lazy { LoginModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        valueUserName = UtilSp.getObjectBase64<String>(key = UtilSp.KEY_USER_NAME, spName = UtilSp.SP_NAME_LOGIN)
        valuePassword = UtilSp.getObjectBase64<String>(key = UtilSp.KEY_PASSWORD, spName = UtilSp.SP_NAME_LOGIN)
        if (!TextUtils.isEmpty(valueUserName)) {
            et_userName.setText(valueUserName)
            et_userName.setSelection(valueUserName!!.length)
            addUserNameToAutoComplete(listOf(valueUserName!!))
        }
        et_password.setText(valuePassword)
        et_password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLoginOrRegister(false)
                return@OnEditorActionListener true
            }
            false
        })

        btn_register.setOnClickListener{ attemptLoginOrRegister(true) }
        btn_login.setOnClickListener { attemptLoginOrRegister(false) }
        initStatusBar()
        initModel()
    }

    private fun initModel() {
        loginModel.liveRegisterOrLoginState.observe(this, Observer {
            hideLoadingDialog()
            if (it == LoginModel.ERROR) return@Observer
            UtilSp.saveObjectBase64(UtilSp.KEY_USER_NAME,et_userNameStr,UtilSp.SP_NAME_LOGIN)
            UtilSp.saveObjectBase64(UtilSp.KEY_PASSWORD,et_passwordStr,UtilSp.SP_NAME_LOGIN)
            when (it) {
                LoginModel.REGISTER -> UtilApp.showToast(R.string.login_register_success)
                LoginModel.LOGIN -> UtilApp.showToast(R.string.login_success)
            }
        })
    }

    private fun initStatusBar() {
        supportActionBar?.hide()
        UltimateBar.with(this).statusDrawable(ColorDrawable(Color.TRANSPARENT))
                .applyNavigation(true)
                .statusDark(true)
                .create()
                .transparentBar()
    }

    private fun attemptLoginOrRegister(register:Boolean) {
        et_userName.error = null
        et_password.error = null
        et_userNameStr = et_userName.text.toString()
        et_passwordStr = et_password.text.toString()

        var cancel = false
        var focusView: View? = null

        if (!TextUtils.isEmpty(et_passwordStr) && !iset_passwordValid(et_passwordStr)) {
            et_password.error = getString(R.string.login_error_invalid_password)
            focusView = et_password
            cancel = true
        }

        if (TextUtils.isEmpty(et_userNameStr)) {
            et_userName.error = getString(R.string.login_error_field_required)
            focusView = et_userName
            cancel = true
        } else if (!iset_userNameValid(et_userNameStr)) {
            et_userName.error = getString(R.string.login_error_invalid_user_name)
            focusView = et_userName
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            showLoadingDialog()
            if (register) {
                loginModel.register(et_userNameStr,et_passwordStr,et_passwordStr)
            } else {
                loginModel.login(et_userNameStr, et_passwordStr)
            }
        }
    }

    private fun iset_userNameValid(userName: String): Boolean {
        return userName.length > 3
    }

    private fun iset_passwordValid(et_password: String): Boolean {
        return et_password.length > 5
    }

    private fun addUserNameToAutoComplete(userNameCollection: List<String>) {
        val adapter = ArrayAdapter(this@LoginActivity,
                android.R.layout.simple_dropdown_item_1line, userNameCollection)
        et_userName.setAdapter(adapter)
    }

}
